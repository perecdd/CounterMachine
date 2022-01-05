package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

// S -> #F {C} S | e
// F -> id(id)(Counters) EndStates
// Counters -> id : Counters | e
// EndStates -> id : EndStates | id
// C -> Command; C | e
// Command -> id -> id (IDCommand) EndStatesCommand | id -> inc (id) id | id -> dec (id) id : id
// EndStatesCommand -> id : EndStatesCommand | id;
// IDCommand -> id : IDCommand | e
public class SyntaxAnalyzer {
    static private Map<String, Integer> FunctionEndsCount = null;
    static private Map<String, Integer> FunctionArgumentCount = null;
    static private Integer listenIndex = 0;
    static private ArrayList<Token> tokens;

    public static boolean Analyze(ArrayList<Token> tokenArrayList){
        listenIndex = 0;
        FunctionEndsCount = new TreeMap<>();
        FunctionArgumentCount = new TreeMap<>();
        tokens = tokenArrayList;
        return S() && listenIndex == tokens.size();
    }

    private static boolean S(){
        if(listenIndex >= tokens.size()) return true;
        if(tokens.get(listenIndex).getLexem() == Lexem.sharp){
            listenIndex++;
            if(!F()) return false;
            if(tokens.get(listenIndex++).getLexem() == Lexem.clb){
                if(!C()) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.crb) return false;
                if(!S()) return false;
                return true;
            }
            else return false;
        }
        else return false;
    }

    private static boolean F(){
        if(listenIndex >= tokens.size()) return false;
        String functionName = tokens.get(listenIndex).getString();
        FunctionEndsCount.put(functionName, 0);
        if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
        FunctionArgumentCount.put(functionName, 0);
        if(!Counters(functionName)) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
        if(!functionName.equals("main") && !EndStates(functionName)) return false;
        return true;
    }

    private static boolean Counters(String id){
        if(listenIndex >= tokens.size()) return true;
        if(tokens.get(listenIndex).getLexem() == Lexem.id){
            listenIndex++;
            FunctionArgumentCount.put(id, FunctionArgumentCount.get(id) + 1);
            if(tokens.get(listenIndex).getLexem() != Lexem.colon) return true;
            listenIndex++;
            if(!Counters(id)) return false;
            return true;
        }
        return true;
    }

    private static boolean C(){
        if(listenIndex >= tokens.size()) return true;
        if(tokens.get(listenIndex).getLexem() == Lexem.id){
            if(!Command()) return false;
            if(tokens.get(listenIndex++).getLexem() != Lexem.semicolon) return false;
            if(!C()) return false;
        }
        return true;
    }

    private static boolean EndStates(String id){
        if(listenIndex >= tokens.size()) return true;
        if(tokens.get(listenIndex).getLexem() == Lexem.id){
            listenIndex++;
            FunctionEndsCount.put(id, FunctionEndsCount.get(id) + 1);
            if(tokens.get(listenIndex).getLexem() != Lexem.colon) return true;
            listenIndex++;
            if(!EndStates(id)) return false;
            return true;
        }
        return false;
    }

    private static boolean Command(){
        if(listenIndex >= tokens.size()) return false;
        if(tokens.get(listenIndex++).getLexem() == Lexem.id){
            if(tokens.get(listenIndex++).getLexem() != Lexem.arrow) return false;
            Lexem lex = tokens.get(listenIndex).getLexem();
            String id = tokens.get(listenIndex).getString();
            listenIndex++;
            if(lex == Lexem.inc){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            }
            else if(lex == Lexem.dec){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.colon) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            }
            else if(lex == Lexem.id){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                if(!IDCommand(id)) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                if(!EndStatesCommand(id)) return false;
            }
            else return false;
            return true;
        }
        return false;
    }

    private static boolean IDCommand(String id){
        Integer count = FunctionArgumentCount.get(id);
        while(count > 0){
            if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            if(count - 1 != 0 && tokens.get(listenIndex++).getLexem() != Lexem.colon) return false;
            count--;
        }
        return true;
    }

    private static boolean EndStatesCommand(String id){
        Integer count = FunctionEndsCount.get(id);
        if(tokens.get(listenIndex).getLexem() != Lexem.id) return false;
        while(count > 0){
            if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            if(count - 1 != 0 && tokens.get(listenIndex++).getLexem() != Lexem.colon) return false;
            count--;
        }
        return true;
    }
}
