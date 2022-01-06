package Tests;

import com.company.LexAnalyzer;
import com.company.Lexem;
import com.company.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LexAnalyzerTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void analyzeLine0() {
        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("");
            assert (tokens.size() == 0);
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine(" \t\n");
            assert (tokens.size() == 0);
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine(" \t\n");
            assert (tokens.size() == 0);
        }
    }

    @org.junit.jupiter.api.Test
    void analyzeLine1() {
        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("inc \tinc \ninc inc");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.inc), new Token(Lexem.inc), new Token(Lexem.inc), new Token(Lexem.inc)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("-> \t-> \n->->");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.arrow), new Token(Lexem.arrow), new Token(Lexem.arrow), new Token(Lexem.arrow)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("dec \tdec \ndec dec");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.dec), new Token(Lexem.dec), new Token(Lexem.dec), new Token(Lexem.dec)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine(": \t: \n: :");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.colon), new Token(Lexem.colon), new Token(Lexem.colon), new Token(Lexem.colon)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("; \t; \n; ;");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.semicolon), new Token(Lexem.semicolon), new Token(Lexem.semicolon), new Token(Lexem.semicolon)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("( \t( \n( (");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.lpar), new Token(Lexem.lpar), new Token(Lexem.lpar), new Token(Lexem.lpar)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine(") \t) \n) )");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.rpar), new Token(Lexem.rpar), new Token(Lexem.rpar), new Token(Lexem.rpar)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("{ \t{ \n{ {");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.clb), new Token(Lexem.clb), new Token(Lexem.clb), new Token(Lexem.clb)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("} \t} \n} }");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.crb), new Token(Lexem.crb), new Token(Lexem.crb), new Token(Lexem.crb)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("#\n# \t##");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token(Lexem.sharp), new Token(Lexem.sharp), new Token(Lexem.sharp), new Token(Lexem.sharp)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("ab \tba \ncd drewo");
            assert (tokens.size() == 4);
            assert (Arrays.asList(new Token("ab", Lexem.id), new Token("ba", Lexem.id), new Token("cd", Lexem.id), new Token("drewo", Lexem.id)).equals(tokens));
        }

        {
            ArrayList<Token> tokens = LexAnalyzer.AnalyzeLine("#MyFunc(q0)(SomeCounters)");
            assert (tokens.size() == 8);
            assert (Arrays.asList(new Token(Lexem.sharp),
                    new Token("MyFunc", Lexem.id),
                    new Token(Lexem.lpar),
                    new Token("q0", Lexem.id),
                    new Token(Lexem.rpar),
                    new Token(Lexem.lpar),
                    new Token("SomeCounters", Lexem.id),
                    new Token(Lexem.rpar)).equals(tokens));
        }
    }

    @org.junit.jupiter.api.Test
    void analyze() throws IOException {
        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyFunc(q0)(SomeCounters)"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (tokens.size() == 8);
            assert (Arrays.asList(new Token(Lexem.sharp),
                    new Token("MyFunc", Lexem.id),
                    new Token(Lexem.lpar),
                    new Token("q0", Lexem.id),
                    new Token(Lexem.rpar),
                    new Token(Lexem.lpar),
                    new Token("SomeCounters", Lexem.id),
                    new Token(Lexem.rpar)).equals(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyFunc(q0)(SomeCounters) % Some Comments"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (tokens.size() == 8);
            assert (Arrays.asList(new Token(Lexem.sharp),
                    new Token("MyFunc", Lexem.id),
                    new Token(Lexem.lpar),
                    new Token("q0", Lexem.id),
                    new Token(Lexem.rpar),
                    new Token(Lexem.lpar),
                    new Token("SomeCounters", Lexem.id),
                    new Token(Lexem.rpar)).equals(tokens));
        }
    }
}