package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexAnalyzer {
    static public ArrayList<Token> AnalyzeLine(String line) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+|(->)|((dec)|(inc))|:|[(]|[)]|;|#|[{]|[}]");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String lexLine = line.substring(matcher.start(), matcher.end());
            switch (lexLine){
                case "inc" -> tokens.add(new Token(Lexem.inc));
                case "->" -> tokens.add(new Token(Lexem.arrow));
                case "dec" -> tokens.add(new Token(Lexem.dec));
                case ":" -> tokens.add(new Token(Lexem.colon));
                case ";" -> tokens.add(new Token(Lexem.semicolon));
                case "(" -> tokens.add(new Token(Lexem.lpar));
                case ")" -> tokens.add(new Token(Lexem.rpar));
                case "{" -> tokens.add(new Token(Lexem.clb));
                case "}" -> tokens.add(new Token(Lexem.crb));
                case "#" -> tokens.add(new Token(Lexem.sharp));
                default -> tokens.add(new Token(lexLine, Lexem.id));
            }
        }
        return tokens;
    }

    static public ArrayList<Token> Analyze(BufferedReader stream) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        while(stream.ready()) {
            tokens.addAll(AnalyzeLine(stream.readLine()));
        }
        return tokens;
    }
}
