package com.company;

import java.io.IOException;
import java.util.*;

// S -> #F {C} S | e
// F -> id(id)(Counters) EndStates
// Counters -> id : Counters | e
// EndStates -> id : EndStates | id
// C -> Command; C | e
// Command -> id -> id (id) EndStatesCommand | id -> inc (id) id | id -> dec (id) id : id
// EndStatesCommand -> id : EndStatesCommand | e;
//
public class Emulator {
    static private Map<String, Integer> FunctionEndsCount = null;
    static private Map<String, Integer> FunctionArgumentCount = null;
    static private Integer listenIndex = 0;
    static private ArrayList<Token> tokens;

    static private Stack<CounterMachine> machines = null;
    static private ArrayList<String> countersInMachine = null;
    static private ArrayList<String> endStatesInMachine = null;

    static private ArrayList<String> countersForCommand = null;
    static private ArrayList<String> endLabelsForCommand = null;

    public static Stack<CounterMachine> Make(ArrayList<Token> tokenArrayList) throws IOException {
        listenIndex = 0;
        FunctionEndsCount = new TreeMap<>();
        FunctionArgumentCount = new TreeMap<>();
        tokens = tokenArrayList;
        machines = new Stack<>();

        if(S() && listenIndex == tokens.size()) {
            return machines;
        }
        else{
            throw new IOException("Bad program");
        }
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
        String functionName = null;
        String startLabel = null;
        countersInMachine = new ArrayList<>();
        endStatesInMachine = new ArrayList<>();

        if(listenIndex >= tokens.size()) return false;
        functionName = tokens.get(listenIndex).getString();
        FunctionEndsCount.put(functionName, 0);
        if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
        startLabel = tokens.get(listenIndex).getString();
        if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
        FunctionArgumentCount.put(functionName, 0);
        if(!Counters(functionName)) return false;
        if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
        if(!functionName.equals("main") && !EndStates(functionName)) return false;

        machines.push(new CounterMachine(startLabel, countersInMachine, endStatesInMachine));
        return true;
    }

    private static boolean Counters(String id){
        if(listenIndex >= tokens.size()) return true;
        if(tokens.get(listenIndex).getLexem() == Lexem.id){
            countersInMachine.add(tokens.get(listenIndex).getString());
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
            endStatesInMachine.add(tokens.get(listenIndex).getString());
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
        String startLabel = null;
        String command = null;
        countersForCommand = new ArrayList<>();
        endLabelsForCommand = new ArrayList<>();

        if(listenIndex >= tokens.size()) return false;
        startLabel = tokens.get(listenIndex).getString();
        if(tokens.get(listenIndex++).getLexem() == Lexem.id){
            if(tokens.get(listenIndex++).getLexem() != Lexem.arrow) return false;
            Lexem lex = tokens.get(listenIndex).getLexem();
            String id = tokens.get(listenIndex).getString();

            if(lex == Lexem.id) {
                command = id;
            }
            else{
                command = lex.toString();
            }
            listenIndex++;
            if(lex == Lexem.inc){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                countersForCommand.add(tokens.get(listenIndex).getString());
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                endLabelsForCommand.add(tokens.get(listenIndex).getString());
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            }
            else if(lex == Lexem.dec){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                countersForCommand.add(tokens.get(listenIndex).getString());
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                endLabelsForCommand.add(tokens.get(listenIndex).getString());
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.colon) return false;
                endLabelsForCommand.add(tokens.get(listenIndex).getString());
                if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            }
            else if(lex == Lexem.id){
                if(tokens.get(listenIndex++).getLexem() != Lexem.lpar) return false;
                if(!IDCommand(id)) return false;
                if(tokens.get(listenIndex++).getLexem() != Lexem.rpar) return false;
                if(!EndStatesCommand(id)) return false;
            }
            else return false;
            machines.peek().addCommand(new Command(startLabel, command, countersForCommand, endLabelsForCommand));
            return true;
        }
        return false;
    }

    private static boolean IDCommand(String id){
        Integer count = FunctionArgumentCount.get(id);
        while(count > 0){
            countersForCommand.add(tokens.get(listenIndex).getString());
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
            endLabelsForCommand.add(tokens.get(listenIndex).getString());
            if(tokens.get(listenIndex++).getLexem() != Lexem.id) return false;
            if(count - 1 != 0 && tokens.get(listenIndex++).getLexem() != Lexem.colon) return false;
            count--;
        }
        return true;
    }
}
