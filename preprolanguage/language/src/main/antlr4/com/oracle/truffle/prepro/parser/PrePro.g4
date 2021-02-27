/*
 * Copyright (c) 2012, 2018, Oracle and/or its affiliates. All rights reserved.
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

/*
 * The parser and lexer need to be generated using "mx create-sl-parser".
 */

grammar PrePro;

@parser::header
{
// DO NOT MODIFY - generated from PrePro.g4 using "mx create-sl-parser"

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.prepro.PreProLanguage;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;
import com.oracle.truffle.prepro.nodes.PreProRootNode;
import com.oracle.truffle.prepro.nodes.PreProStatementNode;
}

@lexer::header
{
// DO NOT MODIFY - generated from PrePro.g4 using "mx create-sl-parser"
}

@parser::members
{
private PreProNodeFactory factory;
private Source source;

private static final class BailoutErrorListener extends BaseErrorListener {
    private final Source source;
    BailoutErrorListener(Source source) {
        this.source = source;
    }
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throwParseError(source, line, charPositionInLine, (Token) offendingSymbol, msg);
    }
}

public void SemErr(Token token, String message) {
    assert token != null;
    throwParseError(source, token.getLine(), token.getCharPositionInLine(), token, message);
}

private static void throwParseError(Source source, int line, int charPositionInLine, Token token, String message) {
    int col = charPositionInLine + 1;
    String location = "-- line " + line + " col " + col + ": ";
    int length = token == null ? 1 : Math.max(token.getStopIndex() - token.getStartIndex(), 0);
    throw new PreProParseError(source, line, col, length, String.format("Error(s) parsing script:%n" + location + message));
}

public static Map<String, RootCallTarget> parsePrePro(PreProLanguage language, Source source) {
    PreProLexer lexer = new PreProLexer(CharStreams.fromString(source.getCharacters().toString()));
    PreProParser parser = new PreProParser(new CommonTokenStream(lexer));
    lexer.removeErrorListeners();
    parser.removeErrorListeners();
    BailoutErrorListener listener = new BailoutErrorListener(source);
    lexer.addErrorListener(listener);
    parser.addErrorListener(listener);
    parser.factory = new PreProNodeFactory(language, source);
    parser.source = source;
    parser.prepro();
    return parser.factory.getAllFunctions();
}
}

// parser


prepro
:
(mainFunction | function) (mainFunction | function)* EOF
;

mainFunction:
'function'
main='main'
s='('
                                                { factory.startFunction($main, $s); }
')'
                                                { factory.startBlock();
                                                  List<PreProStatementNode> body = new ArrayList<>(); }
'{'
(
    statement                                   { body.add($statement.result); }
)*
e='}'
                                                {factory.finishFunction(
                                                  factory.finishBlock(body, $s.getStartIndex(), $e.getStopIndex() - $s.getStartIndex() + 1)
                                                );}
;

function
:
'function'
IDENTIFIER
s='('
                                                { factory.startFunction($IDENTIFIER, $s); }
functionArguments
')'
(
    'returns'
    TYPE                                        {factory.addReturnType($TYPE);}
)?
                                                { factory.startBlock();
                                                  List<PreProStatementNode> body = new ArrayList<>(); }
'{'
(
    statement                                   { body.add($statement.result); }
)*
(
    r='return'
    arithmetic                                  { body.add(factory.createReturn($r, $arithmetic.result));}
    ';'
)?
e='}'
                                                {factory.finishFunction(
                                                  factory.finishBlock(body, $s.getStartIndex(), $e.getStopIndex() - $s.getStartIndex() + 1)
                                                );}
;

functionArguments:
(
    TYPE IDENTIFIER                                  { factory.addFormalParameter($TYPE, factory.createStringLiteral($IDENTIFIER, false)); }
    (
        ','
        TYPE IDENTIFIER                              { factory.addFormalParameter($TYPE, factory.createStringLiteral($IDENTIFIER, false)); }
    )*
)?
;

statement returns [PreProStatementNode result]
:
(
    assignment ';'                              { $result = $assignment.result; }
|
    arithmetic ';'                              { $result = $arithmetic.result; }
|   
    d='debugger'                                { $result = factory.createDebugger($d); }
    ';'
)
;

assignment returns [PreProStatementNode result]:
TYPE?
IDENTIFIER                        { PreProExpressionNode assignmentName = factory.createStringLiteral($IDENTIFIER, false); }
'='
(
arithmetic                        { $result = factory.createAssignment($TYPE, assignmentName, $arithmetic.result); }
)
;

arithmetic returns [PreProExpressionNode result]
:
term                                            { $result = $term.result; }
(
    op=('+' | '-')
    term                                        { $result = factory.createBinary($op, $result, $term.result); }
)*
;


term returns [PreProExpressionNode result]
:
factor                                          { $result = $factor.result; }
(
    op=('*' | '/'| 'X' | '**'| '=='|'<=' | '>=' | '&&' | '||' |'<' |'>')
    factor                                      { $result = factory.createBinary($op, $result, $factor.result); }
)*
;


factor returns [PreProExpressionNode result]
:
IDENTIFIER                                      { PreProExpressionNode assignmentName = factory.createStringLiteral($IDENTIFIER, false); }
    (
        functionCallStatement[assignmentName]   { $result = $functionCallStatement.result; }
    |
                                                { $result = factory.createRead(assignmentName); }
    )
| STRING_LITERAL                    { $result = factory.createStringLiteral($STRING_LITERAL, true); }
| NUMERIC_LITERAL                   { $result = factory.createNumericLiteral($NUMERIC_LITERAL); }
|
    s='('
    expr=arithmetic
    e=')'                           { $result = factory.createParenExpression($expr.result, $s.getStartIndex(), $e.getStopIndex() - $s.getStartIndex() + 1); }
;

functionCallStatement [PreProExpressionNode assignmentName] returns [PreProExpressionNode result]:
'('                                 { List<PreProExpressionNode> parameters = new ArrayList<>();
                                      $result = factory.createRead(assignmentName); }
(
    arithmetic                      { parameters.add($arithmetic.result); }
    (
        ',' arithmetic              { parameters.add($arithmetic.result); }
    )*
)?
e=')'
                                    { $result = factory.createCall($result, parameters, $e); }
;

// lexer

WS : [ \t\r\n\u000C]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;

fragment LETTER : [A-Z] | [a-z] | '_' | '$';
fragment NON_ZERO_DIGIT : [1-9];
fragment DIGIT : [0-9];
// fragment HEX_DIGIT : [0-9] | [a-f] | [A-F];
// fragment OCT_DIGIT : [0-7];
// fragment BINARY_DIGIT : '0' | '1';
// fragment TAB : '\t';
fragment STRING_CHAR : ~('"' | '\\' | '\r' | '\n');

TYPE : 'vec3' | 'vec4' | 'mat' | 'mat3' | 'mat4' | 'scal' | 'const';

IDENTIFIER : LETTER (LETTER | DIGIT)*;
STRING_LITERAL : '"' STRING_CHAR* '"';
NUMERIC_LITERAL : '-'? ('0' | NON_ZERO_DIGIT DIGIT*) ('.' DIGIT*)?;
