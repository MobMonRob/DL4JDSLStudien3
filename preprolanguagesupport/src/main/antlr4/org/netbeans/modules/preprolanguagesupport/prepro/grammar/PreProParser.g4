parser grammar PreProParser;

options { tokenVocab=PreProLexer; }

prepro
    :(mainFunction | function) (mainFunction | function)* EOF
    ;

mainFunction
    :   FUNCTIONK MAIN LPAREN RPAREN 
        LBRACE
            (statement)*
        RBRACE
    ;

function
    :   FUNCTIONK IDENTIFIER LPAREN functionArguments RPAREN
        (RETURNS (VEC3| VEC4 |MAT|MAT3|MAT4|SCAL|CONST))?
        LBRACE
            (statement)*
            (RETURN arithmetic';')?
        RBRACE
    ;

functionArguments
    :   (((VEC3| VEC4 |MAT|MAT3|MAT4|SCAL|CONST) IDENTIFIER) (COMMA ((VEC3| VEC4 |MAT|MAT3|MAT4|SCAL|CONST) IDENTIFIER))*)?
    ;

statement
    :   
    (   assignment SEMI
    |   arithmetic SEMI
    |   throwStatement SEMI
    |   printStatement SEMI
    |   exportStatement SEMI
    |   DEBUGGER SEMI
    )
    ;

printStatement
    :
       PRINT LPAREN (arithmetic|STRING_LITERAL) RPAREN
    ;

assignment
    :   (VEC3| VEC4 |MAT|MAT3|MAT4|SCAL|CONST)? IDENTIFIER ASSIGN
    (
        arithmetic | importStatement
    )
    ;

importStatement
    :
        IMPORT LPAREN STRING_LITERAL RPAREN
    ;

exportStatement
    :
        EXPORT LPAREN STRING_LITERAL COMMA IDENTIFIER RPAREN
    ;
throwStatement
    :   THROW
    (
        STRING_LITERAL
    )
    ;

arithmetic 
    :   term                                            
    (
        (PLUS | MINUS) term
    )*
    ;


term
    :   factor
    (
        (MUL|DIV|CROSS|MUL2|EQUAL|LTE|GTE|AND|OR|LT|GT)
        factor                                      
    )*
    ;


factor 
    :   IDENTIFIER
    (
        functionCallStatement
        |
                                                
    )
    | 
        STRING_LITERAL                    
    | 
        NUMERIC_LITERAL                   
    |   
        LPAREN arithmetic RPAREN
    ;

functionCallStatement
    :   LPAREN 
        ((arithmetic|importStatement) (COMMA (arithmetic|importStatement))*)?
        RPAREN
    ;

