
lexer grammar PreProLexer;

// Keywords

IMPORT:             'import';
EXPORT:             'export';
THROW:              'throw';
PRINT:              'print';
RETURN:             'return';
RETURNS:            'returns';
DEBUGGER:           'debugger';
FUNCTIONK:           'function';
MAIN:               'main';



//primitive types
VEC3:               'vec3'; 
VEC4:               'vec4';
MAT:                'mat';
MAT3:               'mat3';
MAT4:               'mat4';
SCAL:               'scal';
CONST:              'const';

//TYPE:               VEC3|VEC4|MAT|MAT3|MAT4|SCAL|CONST;

// Separators
LPAREN:             '(';
RPAREN:             ')';
LBRACE:             '{';
RBRACE:             '}';
LBRACK:             '[';
RBRACK:             ']';
SEMI:               ';';
COMMA:              ',';
DOT:                '.';
COLON:              ':';

// Operators
ASSIGN:             '=';
PLUS:               '+';
MINUS:              '-';
MUL:                '*';
DIV:                '/';
CROSS:              'X';
MUL2:               '**';
EQUAL:              '==';
LTE:                '<=';
GTE:                '>=';
AND:                '&&';
OR:                 '||';
LT:                 '<';
GT:                 '>';



// Literals


// Whitespace
WS:                 [ \t\r\n\u000C]+ -> skip;
//comments
LINE_COMMENT:       '//' ~[\r\n]*            -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/' [\r\n]*    -> channel(HIDDEN);



CHAR_LITERAL:       '\'' (~["\\\r\n] | EscapeSequence)* '\'';
STRING_LITERAL:     '"' (~["\\\r\n] | EscapeSequence)* '"';
NUMERIC_LITERAL : '-'? ('0' | NON_ZERO_DIGIT DIGIT*) ('.' DIGIT*)?;

IDENTIFIER:         LetterOrDigit+;

// Fragment rules

fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    ;


fragment Digits
    : [0-9] ([0-9_]* [0-9])?
    ;
fragment NON_ZERO_DIGIT 
    : [1-9];

fragment DIGIT 
    : [0-9];

fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment Letter
    : [a-zA-Z$_] // these are below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;
