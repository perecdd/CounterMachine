package com.company;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Token{" +
                "string='" + string + '\'' +
                ", lexem=" + lexem +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(string, token.string) && lexem == token.lexem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, lexem);
    }

    public String getString() {
        return string;
    }

    public Lexem getLexem() {
        return lexem;
    }
}
