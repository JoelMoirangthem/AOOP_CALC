package com.lexer;

public enum TokenType {
    NUMBER,
    STRING,
    IDENTIFIER,

    ASSIGN, // :=
    PRINT, // >>
    IF ,// ?
    LOOP, // @
    ARROW , // =>
    
    PLUS,MINUS,MUL,DIV,MOD,GT,LT,EQ,NEQ,EQEQ,GEQ,LEQ,AND,OR,NOT,

    NEWLINE,
    EOF
}
