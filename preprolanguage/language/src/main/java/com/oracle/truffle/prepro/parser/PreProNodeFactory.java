/*
 * Copyright (c) 2012, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.prepro.parser;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.prepro.PreProLanguage;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;
import com.oracle.truffle.prepro.nodes.PreProRootNode;
import com.oracle.truffle.prepro.nodes.PreProStatementNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProBlockNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProDebuggerNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProFunctionBodyNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProReturnNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProAddNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProCrossProductNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProDivNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProEqualNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProGreaterOrEqualNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProGreaterThanNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLazyMultiplicationNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLessOrEqualNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLessThanNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProMulNodeGen;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProSubNodeGen;
import com.oracle.truffle.prepro.nodes.expression.builtin.PreProExistsNode;
import com.oracle.truffle.prepro.nodes.expression.function.PreProFunctionLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.function.PreProInvokeNode;
import com.oracle.truffle.prepro.nodes.expression.function.PreProParenExpressionNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProConstantLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProStringLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.logic.PreProLogicalAndNode;
import com.oracle.truffle.prepro.nodes.expression.logic.PreProLogicalOrNode;
import com.oracle.truffle.prepro.nodes.local.PreProReadArgumentNode;
import com.oracle.truffle.prepro.nodes.local.PreProReadLocalVariableNode;
import com.oracle.truffle.prepro.nodes.local.PreProReadLocalVariableNodeGen;
import com.oracle.truffle.prepro.nodes.local.PreProWriteLocalVariableNode;
import com.oracle.truffle.prepro.nodes.util.PreProUnboxNodeGen;
import com.oracle.truffle.prepro.runtime.PreProUndefinedNameException;
import com.oracle.truffle.prepro.runtime.PreProVariableAlreadyDefinedException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class used by the PrePro {@link Parser} to create nodes. The code is factored out of the
 * automatically generated parser to keep the attributed grammar of PrePro small.
 */
public class PreProNodeFactory {

    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }

    /* State while parsing a source unit. */
    private final Source source;
    private final Map<String, RootCallTarget> allFunctions;

    /* State while parsing a function. */
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private Token returnType;
    private FrameDescriptor frameDescriptor;
    private List<PreProStatementNode> methodNodes;

    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    private final PreProLanguage language;

    public PreProNodeFactory(PreProLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, RootCallTarget> getAllFunctions() {
        return allFunctions;
    }

    public void startFunction(Token nameToken, Token bodyStartToken) {
        assert functionStartPos == 0;
        assert functionName == null;
        assert functionBodyStartPos == 0;
        assert parameterCount == 0;
        assert frameDescriptor == null;
        assert lexicalScope == null;
        assert returnType == null;

        functionStartPos = nameToken.getStartIndex();
        functionName = nameToken.getText();
        functionBodyStartPos = bodyStartToken.getStartIndex();
        frameDescriptor = new FrameDescriptor();
        methodNodes = new ArrayList<>();

        startBlock();
    }

    public void addReturnType(Token typeToken) {
        returnType = typeToken;
    }

    public void finishFunction(PreProStatementNode bodyNode) {
        if (bodyNode == null) {
            // a state update that would otherwise be performed by finishBlock
            lexicalScope = lexicalScope.outer;
        } else {
            methodNodes.add(bodyNode);
            final int bodyEndPos = bodyNode.getSourceEndIndex();
            final SourceSection functionSrc = source.createSection(functionStartPos, bodyEndPos - functionStartPos);
            final PreProStatementNode methodBlock = finishBlock(methodNodes, functionBodyStartPos, bodyEndPos - functionBodyStartPos);
            assert lexicalScope == null : "Wrong scoping of blocks in parser";

            final PreProFunctionBodyNode functionBodyNode = new PreProFunctionBodyNode(methodBlock);
            functionBodyNode.setSourceSection(functionSrc.getCharIndex(), functionSrc.getCharLength());

            final PreProRootNode rootNode = new PreProRootNode(language, frameDescriptor, functionBodyNode, functionSrc, functionName);
            allFunctions.put(functionName, Truffle.getRuntime().createCallTarget(rootNode));
        }

        functionStartPos = 0;
        functionName = null;
        functionBodyStartPos = 0;
        parameterCount = 0;
        frameDescriptor = null;
        lexicalScope = null;
        returnType = null;
    }

    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
    }

    public PreProStatementNode finishBlock(List<PreProStatementNode> bodyNodes, int startPos, int length) {
        lexicalScope = lexicalScope.outer;

        if (containsNull(bodyNodes)) {
            return null;
        }

        for (PreProStatementNode statement : bodyNodes) {
            if (statement.hasSource()) {
                statement.addStatementTag();
            }
        }
        PreProBlockNode blockNode = new PreProBlockNode(bodyNodes.toArray(new PreProStatementNode[0]));
        blockNode.setSourceSection(startPos, length);
        return blockNode;
    }

    /**
     * Returns an {@link PreProDebuggerNode} for the given token.
     *
     * @param debuggerToken The token containing the debugger node's info.
     * @return A SLDebuggerNode for the given token.
     */
    PreProStatementNode createDebugger(Token debuggerToken) {
        final PreProDebuggerNode debuggerNode = new PreProDebuggerNode();
        srcFromToken(debuggerNode, debuggerToken);
        return debuggerNode;
    }

    /**
     * Returns an {@link PreProReturnNode} for the given parameters.
     *
     * @param t         The token containing the return node's info
     * @param valueNode The value of the return (null if not returning a value)
     * @return An SLReturnNode for the given parameters.
     */
    public PreProStatementNode createReturn(Token t, PreProExpressionNode valueNode) {
        final int start = t.getStartIndex();
        final int length = valueNode == null ? t.getText().length() : valueNode.getSourceEndIndex() - start;
        final PreProReturnNode returnNode = new PreProReturnNode(valueNode);
        returnNode.setSourceSection(start, length);
        if (this.returnType == null) {
            if (valueNode != null) {
                throw new RuntimeException("Function " + functionName + " must have NO return statement or specify the return type with \"<funtion>() returns <type>\"");
            }
        } else {
            if (valueNode == null) {
                throw new RuntimeException("Function " + functionName + " must have an return statement.");
            }
        }
        return returnNode;
    }

    /**
     * Returns the corresponding subclass of {@link PreProExpressionNode} for binary expressions. </br>
     * These nodes are currently not instrumented.
     *
     * @param opToken   The operator of the binary expression
     * @param leftNode  The left node of the expression
     * @param rightNode The right node of the expression
     * @return A subclass of SLExpressionNode using the given parameters based on the given opToken.
     * null if either leftNode or rightNode is null.
     */
    public PreProExpressionNode createBinary(Token opToken, PreProExpressionNode leftNode, PreProExpressionNode rightNode) {
        if (leftNode == null || rightNode == null) {
            return null;
        }
        final PreProExpressionNode leftUnboxed = PreProUnboxNodeGen.create(leftNode);
        final PreProExpressionNode rightUnboxed = PreProUnboxNodeGen.create(rightNode);

        final PreProExpressionNode result;
        switch (opToken.getText()) {
            case "+":
                result = PreProAddNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "*":
                result = PreProMulNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "/":
                result = PreProDivNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "-":
                result = PreProSubNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "X":
                result = PreProCrossProductNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "<":
                result = PreProLessThanNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "<=":
                result = PreProLessOrEqualNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case ">":
                result = PreProGreaterOrEqualNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case ">=":
                result = PreProGreaterThanNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "==":
                result = PreProEqualNodeGen.create(leftUnboxed, rightUnboxed);
                break;
            case "&&":
                result = new PreProLogicalAndNode(leftUnboxed, rightUnboxed);
                break;
            case "||":
                result = new PreProLogicalOrNode(leftUnboxed, rightUnboxed);
                break;
            case "**":
                result = new PreProLazyMultiplicationNode(leftUnboxed, rightUnboxed);
                break;
            default:
                throw new RuntimeException("unexpected operation: " + opToken.getText());
        }

        int start = leftNode.getSourceCharIndex();
        int length = rightNode.getSourceEndIndex() - start;
        result.setSourceSection(start, length);
        result.addExpressionTag();

        return result;
    }

    /**
     * Returns an {@link PreProInvokeNode} for the given parameters.
     *
     * @param functionNode   The function being called
     * @param parameterNodes The parameters of the function call
     * @param finalToken     A token used to determine the end of the sourceSelection for this call
     * @return A PreProInvokeNode for the given parameters. null if functionNode or any of the
     * parameterNodes are null.
     */
    public PreProExpressionNode createCall(PreProExpressionNode functionNode, List<PreProExpressionNode> parameterNodes, Token finalToken) {
        if (functionNode == null || containsNull(parameterNodes)) {
            return null;
        }

        final PreProExpressionNode result = new PreProInvokeNode(functionNode, parameterNodes.toArray(new PreProExpressionNode[0]));

        final int startPos = functionNode.getSourceCharIndex();
        final int endPos = finalToken.getStartIndex() + finalToken.getText().length();
        result.setSourceSection(startPos, endPos - startPos);
        result.addExpressionTag();

        return result;
    }

    public void addFormalParameter(Token typeToken, PreProExpressionNode nameNode) {
        final PreProReadArgumentNode readArg = new PreProReadArgumentNode(parameterCount);
        PreProWriteLocalVariableNode assignment = createAssignment(typeToken, nameNode, readArg, parameterCount);
        methodNodes.add(assignment);
        parameterCount++;
    }

    /**
     * Returns a {@link PreProWriteLocalVariableNode} for the given parameters.
     *
     * @param nameNode  The name of the variable being assigned
     * @param valueNode The value to be assigned
     * @return A PreProStatementNode for the given parameters. null if nameNode or valueNode is null.
     */
    public PreProStatementNode createAssignment(Token typeToken, PreProExpressionNode nameNode, PreProExpressionNode valueNode) {
        if (nameNode == null || valueNode == null) {
            return null;
        }
        if (typeToken == null) {
            return createAssignmentToExisting(nameNode, valueNode);
        } else {
            return createAssignment(typeToken, nameNode, valueNode, null);
        }
    }

    /**
     * Returns an {@link PreProWriteLocalVariableNode} for the given parameters.
     *
     * @param nameNode      The name of the variable being assigned
     * @param valueNode     The value to be assigned
     * @param argumentIndex null or index of the argument the assignment is assigning
     * @return An SLExpressionNode for the given parameters. null if nameNode or valueNode is null.
     */
    public PreProWriteLocalVariableNode createAssignment(Token typeToken, PreProExpressionNode nameNode, PreProExpressionNode valueNode, Integer argumentIndex) throws PreProVariableAlreadyDefinedException {
        String name = ((PreProStringLiteralNode) nameNode).executeGeneric(null);
        if (lexicalScope.locals.containsKey(name))
            throw PreProVariableAlreadyDefinedException.variableAlreadyExists(nameNode, name);
        FrameSlot frameSlot = frameDescriptor.addFrameSlot(
                name,
                argumentIndex,
                FrameSlotKind.Illegal);
        lexicalScope.locals.put(name, frameSlot);
        final PreProWriteLocalVariableNode result = new PreProWriteLocalVariableNode(frameSlot, typeToken.getText(), valueNode);

        if (valueNode.hasSource()) {
            final int start = nameNode.getSourceCharIndex();
            final int length = valueNode.getSourceEndIndex() - start;
            result.setSourceSection(start, length);
        }
        result.addStatementTag();

        return result;
    }

    public PreProWriteLocalVariableNode createAssignmentToExisting(PreProExpressionNode nameNode, PreProExpressionNode valueNode) throws PreProUndefinedNameException {
        String name = ((PreProStringLiteralNode) nameNode).executeGeneric(null);
        if (!lexicalScope.locals.containsKey(name))
            throw PreProUndefinedNameException.undefinedVariable(nameNode, name);
        FrameSlot frameSlot = frameDescriptor.findFrameSlot(name);
        final PreProWriteLocalVariableNode result = new PreProWriteLocalVariableNode(frameSlot, valueNode);

        if (valueNode.hasSource()) {
            final int start = nameNode.getSourceCharIndex();
            final int length = valueNode.getSourceEndIndex() - start;
            result.setSourceSection(start, length);
        }
        result.addStatementTag();

        return result;
    }

    /**
     * Returns a {@link PreProReadLocalVariableNode} if this read is a local variable or a
     * {@link PreProFunctionLiteralNode} if this read is global. In PrePro, the only global names are
     * functions.
     *
     * @param nameNode The name of the variable/function being read
     * @return either:
     * <ul>
     * <li>A PreProReadLocalVariableNode representing the local variable being read.</li>
     * <li>A PreProFunctionLiteralNode representing the function definition.</li>
     * <li>null if nameNode is null.</li>
     * </ul>
     */
    public PreProExpressionNode createRead(PreProExpressionNode nameNode) {
        if (nameNode == null) {
            return null;
        }

        String name = ((PreProStringLiteralNode) nameNode).executeGeneric(null);
        final PreProExpressionNode result;
        final FrameSlot frameSlot = lexicalScope.locals.get(name);
        if (frameSlot != null) {
            /* Read of a local variable. */
            result = PreProReadLocalVariableNodeGen.create(frameSlot);
        } else {
            /* Read of a global name. In our language, the only global names are functions. */
            result = new PreProFunctionLiteralNode(name);
        }
        result.setSourceSection(nameNode.getSourceCharIndex(), nameNode.getSourceLength());
        result.addExpressionTag();
        return result;
    }

    public PreProExpressionNode createStringLiteral(Token literalToken, boolean removeQuotes) {
        /* Remove the trailing and ending " */
        String literal = literalToken.getText();
        if (removeQuotes) {
            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
            literal = literal.substring(1, literal.length() - 1);
        }

        final PreProStringLiteralNode result = new PreProStringLiteralNode(literal.intern());
        srcFromToken(result, literalToken);
        result.addExpressionTag();
        return result;
    }

    public PreProExpressionNode createNumericLiteral(Token literalToken) {
        PreProExpressionNode result = new PreProConstantLiteralNode(literalToken.getText());
        srcFromToken(result, literalToken);
        result.addExpressionTag();
        return result;
    }

    public PreProExpressionNode createExistsExpression(Token nameToken) {
        final PreProExistsNode preProExistsNode = new PreProExistsNode(nameToken.getText());
        srcFromToken(preProExistsNode, nameToken);
        return preProExistsNode;
    }

    public PreProExpressionNode createParenExpression(PreProExpressionNode expressionNode, int start, int length) {
        if (expressionNode == null) {
            return null;
        }

        final PreProParenExpressionNode result = new PreProParenExpressionNode(expressionNode);
        result.setSourceSection(start, length);
        return result;
    }

    /**
     * Creates source description of a single token.
     */
    private static void srcFromToken(PreProStatementNode node, Token token) {
        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    /**
     * Checks whether a list contains a null.
     */
    private static boolean containsNull(List<?> list) {
        for (Object e : list) {
            if (e == null) {
                return true;
            }
        }
        return false;
    }

}
