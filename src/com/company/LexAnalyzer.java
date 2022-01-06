package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexAnalyzer {
    /**
     * @param line - string of program
     * @return Array of tokens, which contains language lexemes
     */
    static public ArrayList<Token> AnalyzeLine(String line) {
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

    /**
     * @param stream - buffered reader, which needed to read program code to transform it to tokens.
     * @return Array of tokens, which contains lexemes of program
     * @throws IOException If an I/O error occurs
     */
    static public ArrayList<Token> Analyze(BufferedReader stream) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        Set<String> libs = new TreeSet<>();

        while(stream.ready()) {
            String line = stream.readLine();
            if(line == null) break;

            if(line.contains("%")){
                line = line.substring(0, line.indexOf('%'));
            }

            if(!line.contains("$")) {
                tokens.addAll(AnalyzeLine(line));
            }
            else {
                String libName = line.split("[$]")[1];
                if(!libs.contains(libName)) {
                    libs.add(libName);
                    BufferedReader br = new BufferedReader(new FileReader(libName));
                    tokens.addAll(LexAnalyzer.Analyze(br));
                }
            }
        }
        return tokens;
    }
}
