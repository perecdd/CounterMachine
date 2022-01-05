package com.company;

public class Token {
    private String string = null;
    private Lexem lexem = null;

    public Token(Lexem lexem){
        this.lexem = lexem;
    }
    public Token(String string, Lexem lexem){
        this.string = string;
        this.lexem = lexem;
    }

    public String getString() {
        return string;
    }

    public Lexem getLexem() {
        return lexem;
    }
}
