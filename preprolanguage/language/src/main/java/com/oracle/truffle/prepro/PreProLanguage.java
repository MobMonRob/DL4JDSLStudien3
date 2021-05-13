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
package com.oracle.truffle.prepro;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Scope;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.TruffleLanguage.ContextPolicy;
import com.oracle.truffle.api.TruffleLogger;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;
import com.oracle.truffle.prepro.builtins.PreProBuiltinNode;
import com.oracle.truffle.prepro.builtins.PreProPrintBuiltin;
import com.oracle.truffle.prepro.nodes.PreProEvalRootNode;
import com.oracle.truffle.prepro.nodes.PreProTypes;
import com.oracle.truffle.prepro.nodes.controlflow.PreProBlockNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProDebuggerNode;
import com.oracle.truffle.prepro.nodes.controlflow.PreProReturnNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProAddNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProCrossProductNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProDivNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProEqualNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLazyMultiplicationNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLessOrEqualNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProLessThanNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProMulNode;
import com.oracle.truffle.prepro.nodes.expression.arithmetic.PreProSubNode;
import com.oracle.truffle.prepro.nodes.expression.function.PreProFunctionLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProConstantLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProDoubleLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProMatrix3LiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProMatrix4LiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProMatrixLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProScalarLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProStringLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProVector3LiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProVector4LiteralNode;
import com.oracle.truffle.prepro.nodes.expression.literal.PreProVectorLiteralNode;
import com.oracle.truffle.prepro.nodes.expression.logic.PreProLogicalAndNode;
import com.oracle.truffle.prepro.nodes.expression.logic.PreProLogicalOrNode;
import com.oracle.truffle.prepro.nodes.local.PreProLexicalScope;
import com.oracle.truffle.prepro.nodes.local.PreProReadLocalVariableNode;
import com.oracle.truffle.prepro.nodes.local.PreProWriteLocalVariableNode;
import com.oracle.truffle.prepro.parser.PreProLexer;
import com.oracle.truffle.prepro.parser.PreProNodeFactory;
import com.oracle.truffle.prepro.parser.PreProParser;
import com.oracle.truffle.prepro.runtime.PreProContext;
import com.oracle.truffle.prepro.runtime.PreProFunction;
import com.oracle.truffle.prepro.runtime.PreProFunctionRegistry;
import com.oracle.truffle.prepro.runtime.types.PreProConstant;
import com.oracle.truffle.prepro.runtime.types.PreProMatrix;
import com.oracle.truffle.prepro.runtime.types.PreProScalar;
import com.oracle.truffle.prepro.runtime.types.PreProVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * The PrePro Language is designed to simplify calculations with time series.
 * <p>
 * PrePro is statically typed, i.e., type names of variables must be specified. PrePro is
 * strongly typed, i.e., there is no automatic conversion between types. If an operation is not
 * available for the types encountered at run time, a type error is reported and execution is
 * stopped. For example, {@code vec - mat} results in a type error because subtraction is not defined
 * for time series with an unequal amount of data rows.
 *
 * <p>
 * <b>Types:</b>
 * <ul>
 * <li>Time Series: most PrePro types are time series of values, represented as vectors or matrices.
 * The "number" value {@link PreProConstant}, which is simply a time series of length 1, is transformed
 * internally into the Java primitive {@code double} type for performance optimization.
 * Boolean types are represented as {@link PreProConstant}s, 0 for false and 1 for true.
 * <li>String: implemented as the Java standard type {@link String}.
 * <li>Function: implementation type {@link PreProFunction}.
 * </ul>
 * The class {@link PreProTypes} lists these types for the Truffle DSL, i.e., for type-specialized
 * operations that are specified using Truffle DSL annotations.
 *
 * <p>
 * <b>Language concepts:</b>
 * <ul>
 * <li>Literals for {@link PreProConstantLiteralNode numbers}, {@link PreProDoubleLiteralNode},
 * {@link PreProMatrixLiteralNode}, {@link PreProMatrix3LiteralNode}, {@link PreProMatrix4LiteralNode},
 * {@link PreProVectorLiteralNode}, {@link PreProVector3LiteralNode}, {@link PreProVector4LiteralNode},
 * {@link PreProScalarLiteralNode}, {@link PreProStringLiteralNode strings}, and {@link PreProFunctionLiteralNode functions}.
 * <li>Basic arithmetic, logical, and comparison operations: {@link PreProAddNode +}, {@link PreProSubNode
 * -}, {@link PreProMulNode *}, {@link PreProDivNode /}, {@link PreProCrossProductNode X}, {@link PreProLogicalAndNode &&},
 * {@link PreProLogicalOrNode ||}, {@link PreProLazyMultiplicationNode **}, {@link PreProEqualNode ==}, !=,
 * {@link PreProLessThanNode &lt;}, {@link PreProLessOrEqualNode &le;}, &gt;, &ge;.
 * <li>Local variables: local variables must be defined (via a {@link PreProWriteLocalVariableNode
 * write}) before they can be used (by a {@link PreProReadLocalVariableNode read}). Local variables are
 * not visible outside of the block where they were first defined.
 * <li>Basic control flow statements: {@link PreProBlockNode blocks} and {@link PreProReturnNode return}.
 * <li>Debugging control: {@link PreProDebuggerNode debugger} statement uses
 * {@link DebuggerTags.AlwaysHalt} tag to halt the execution when run under the debugger.
 * </ul>
 *
 * <p>
 * <b>Syntax and parsing:</b><br>
 * The syntax is described as an attributed grammar. The {@link PreProParser} and
 * {@link PreProLexer} are automatically generated by ANTLR 4. The grammar contains semantic
 * actions that build the AST for a method. To keep these semantic actions short, they are mostly
 * calls to the {@link PreProNodeFactory} that performs the actual node creation. All functions found in
 * the PrePro source are added to the {@link PreProFunctionRegistry}, which is accessible from the
 * {@link PreProContext}.
 *
 * <p>
 * <b>Builtin functions:</b><br>
 * Library functions that are available to every PrePro source without prior definition are called
 * builtin functions. They are added to the {@link PreProFunctionRegistry} when the {@link PreProContext} is
 * created. Some of the current builtin functions are
 * <ul>
 * <li>{@link PreProPrintBuiltin print}: Write a value to the {@link PreProContext#getOutput() standard
 * output}.
 * </ul>
 */
@TruffleLanguage.Registration(id = PreProLanguage.ID, name = "PrePro", defaultMimeType = PreProLanguage.MIME_TYPE, characterMimeTypes = PreProLanguage.MIME_TYPE, contextPolicy = ContextPolicy.SHARED, fileTypeDetectors = PreProFileDetector.class)
@ProvidedTags({
    StandardTags.CallTag.class, 
    StandardTags.StatementTag.class, 
    StandardTags.RootTag.class, 
    StandardTags.RootBodyTag.class, 
    StandardTags.ExpressionTag.class,
    StandardTags.ReadVariableTag.class,
    StandardTags.WriteVariableTag.class,
    DebuggerTags.AlwaysHalt.class
})
public final class PreProLanguage extends TruffleLanguage<PreProContext> {
    public static volatile int counter;

    public static final String MIME_TYPE = "application/x-prepro";
    public static final String ID = "prepro"; 
    
    
    private static final TruffleLogger LOG = TruffleLogger.getLogger(PreProLanguage.ID, PreProLanguage.class);

    public PreProLanguage() {
        int tempCounter = counter;
        counter = tempCounter + 1;
    }

    @Override
    protected PreProContext createContext(Env env) {
        LOG.finer("Entered #createContext");
        return new PreProContext(this, env, new ArrayList<>(EXTERNAL_BUILTINS));
    }

    @Override
    protected CallTarget parse(ParsingRequest request) {
        LOG.finer("Entered #parse");
        Source source = request.getSource();
        Map<String, RootCallTarget> functions;
        /*
         * Parse the provided source. At this point, we do not have a PreProContext yet. Registration of
         * the functions with the PreProContext happens lazily in PreProEvalRootNode.
         */
        if (request.getArgumentNames().isEmpty()) {
            functions = PreProParser.parsePrePro(this, source);
        } else {
            Source requestedSource = request.getSource();
            StringBuilder sb = new StringBuilder();
            sb.append("function main(");
            String sep = "";
            for (String argumentName : request.getArgumentNames()) {
                sb.append(sep);
                sb.append(argumentName);
                sep = ",";
            }
            sb.append(") { return ");
            sb.append(request.getSource().getCharacters());
            sb.append(";}");
            String language = requestedSource.getLanguage() == null ? ID : requestedSource.getLanguage();
            Source decoratedSource = Source.newBuilder(language, sb.toString(), request.getSource().getName()).build();
            functions = PreProParser.parsePrePro(this, decoratedSource);
        }

        RootCallTarget main = functions.get("main");
        RootNode evalMain;
        if (main != null) {
            /*
             * We have a main function, so "evaluating" the parsed source means invoking that main
             * function. However, we need to lazily register functions into the PreProContext first, so
             * we cannot use the original PreProRootNode for the main function. Instead, we create a new
             * PreProEvalRootNode that does everything we need.
             */
            evalMain = new PreProEvalRootNode(this, main, functions);
        } else {
            /*
             * Even without a main function, "evaluating" the parsed source needs to register the
             * functions into the PreProContext.
             */
            evalMain = new PreProEvalRootNode(this, null, functions);
        }
        return Truffle.getRuntime().createCallTarget(evalMain);
    }

    /*
     * Still necessary for the old PrePro TCK to pass. We should remove with the old TCK. New language
     * should not override this.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected Object findExportedSymbol(PreProContext context, String globalName, boolean onlyExplicit) {
        LOG.finer("Entered #findExportedSymbol");
        return context.getFunctionRegistry().lookup(globalName, false);
    }

    @Override
    protected boolean isVisible(PreProContext context, Object value) {
        LOG.finer("Entered #isVisible");
        return !InteropLibrary.getFactory().getUncached(value).isNull(value);
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        LOG.finer("Entered #isObjectOfLanguage");
        if (!(object instanceof TruffleObject)) {
            return false;
        } else return object instanceof PreProConstant || object instanceof PreProScalar
                || object instanceof PreProMatrix || object instanceof PreProVector
                || object instanceof PreProFunction;
    }

    @Override
    protected String toString(PreProContext context, Object value) {
        return toString(value);
    }

    public static String toString(Object value) {
        try {
            if (value == null) {
                return "ANY";
            }
            InteropLibrary interop = InteropLibrary.getFactory().getUncached(value);
            if (interop.fitsInDouble(value)) {
                return Double.toString(interop.asDouble(value));
            } else if (interop.isString(value)) {
                return interop.asString(value);
            } else if (interop.isNull(value)) {
                return "NULL";
            } else if (interop.isExecutable(value)) {
                if (value instanceof PreProFunction) {
                    return ((PreProFunction) value).getName();
                } else {
                    return "Function";
                }
                // No Objects in PrePro for now
            } /*else if (interop.hasMembers(value)) {
                return "Object";
            } */ else if (value instanceof PreProConstant || value instanceof PreProScalar
                    || value instanceof PreProMatrix || value instanceof PreProVector) {
                return value.toString();
            } else {
                return "Unsupported";
            }
        } catch (UnsupportedMessageException e) {
            CompilerDirectives.transferToInterpreter();
            throw new AssertionError();
        }
    }

    @Override
    protected Object findMetaObject(PreProContext context, Object value) {
        return getMetaObject(value);
    }

    public static String getMetaObject(Object value) {
        if (value == null) {
            return "ANY";
        }
        InteropLibrary interop = InteropLibrary.getFactory().getUncached(value);
        if (interop.isNumber(value)) {
            return "Number";
        } else if (value instanceof PreProConstant) {
            return "Constant";
        } else if (value instanceof PreProScalar) {
            return "Scalar";
        } else if (value instanceof PreProMatrix) {
            return "Matrix";
        } else if (value instanceof PreProVector) {
            return "Vector";
        } else if (interop.isString(value)) {
            return "String";
        } else if (interop.isNull(value)) {
            return "NULL";
        } else if (interop.isExecutable(value)) {
            return "Function";
            // No Objects in PrePro for now
        } /*else if (interop.hasMembers(value)) {
            return "Object";
        } */ else {
            return "Unsupported";
        }
    }

    @Override
    protected SourceSection findSourceLocation(PreProContext context, Object value) {
        LOG.finer("Entered #findSourceLocation");
        if (value instanceof PreProFunction) {
            return ((PreProFunction) value).getDeclaredLocation();
        }
        LOG.finer("Returned null for #findSourceLocation");
        return null;
    }

    @Override
    public Iterable<Scope> findLocalScopes(PreProContext context, Node node, Frame frame) {
        LOG.finer("Entered #findLocalScopes");
        final PreProLexicalScope scope = PreProLexicalScope.createScope(node);
        return new Iterable<Scope>() {
            @Override
            public Iterator<Scope> iterator() {
                return new Iterator<>() {
                    
                    private PreProLexicalScope previousScope;
                    private PreProLexicalScope nextScope = scope;

                    @Override
                    public boolean hasNext() {
                        if (nextScope == null) {
                            nextScope = previousScope.findParent();
                        }
                        return nextScope != null;
                    }

                    @Override
                    public Scope next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        Object functionObject = findFunctionObject();
                        Scope vscope = Scope.newBuilder(nextScope.getName(), nextScope.getVariables(frame)).node(nextScope.getNode()).arguments(nextScope.getArguments(frame)).rootInstance(
                                functionObject).build();
                        previousScope = nextScope;
                        nextScope = null;
                        return vscope;
                    }

                    private Object findFunctionObject() {
                        String name = node.getRootNode().getName();
                        return context.getFunctionRegistry().getFunction(name);
                    }
                };
            }
        };
    }

    @Override
    protected Iterable<Scope> findTopScopes(PreProContext context) {
        LOG.finer("Endered #FindTopScopes");
        return context.getTopScopes();
    }

    public static PreProContext getCurrentContext() {
        LOG.finer("Endered getCurrentContext");
        return getCurrentContext(PreProLanguage.class);
    }

    private static final List<NodeFactory<? extends PreProBuiltinNode>> EXTERNAL_BUILTINS = Collections.synchronizedList(new ArrayList<>());
}
