package Tests;

import com.company.LexAnalyzer;
import com.company.SyntaxAnalyzer;
import com.company.Token;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SyntaxAnalyzerTest {

    @Test
    void analyze() throws IOException {
        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#main(q0)(){}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#main(q0)(){}" +
                    "#main(q0)(){}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (!SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyProg(q0)(Counter) E {}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyProg(q0)(Counter) E {}" +
                    "#MyProg(q0)(Counter) E {}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (!SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyProg(q0)(Counter) E {q0 -> inc(Counter) E;}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyProg(q0)(Counter) E {q0 -> myfunc(Counter) E;}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (!SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#MyProg(q0)(Counter) E {q0 -> myfunc(Counter) E;}" +
                    "#myfunc(q0)(Counter) E {q0 -> inc(Counter) E;}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (!SyntaxAnalyzer.Analyze(tokens));
        }

        {
            BufferedReader bufferedReader = new BufferedReader(new StringReader("#myfunc(q0)(Counter) E {q0 -> inc(Counter) E;}" +
                    "#MyProg(q0)(Counter) E {q0 -> myfunc(Counter) E;}"));
            ArrayList<Token> tokens = LexAnalyzer.Analyze(bufferedReader);
            assert (SyntaxAnalyzer.Analyze(tokens));
        }
    }
}