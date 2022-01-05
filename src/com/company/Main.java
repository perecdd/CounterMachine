package com.company;

import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("program.txt"));
        var a = LexAnalyzer.Analyze(br);
        if(SyntaxAnalyzer.Analyze(a)){
            System.out.println("Good program");
            Stack<CounterMachine> result = Emulator.Make(a);
            for(CounterMachine machine : result){
                machine.getInfo();
            }
        }
        else{
            System.out.println("Bad program");
        }
    }
}
