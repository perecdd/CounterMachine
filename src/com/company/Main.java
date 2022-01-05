package com.company;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("program.txt"));
        var a = LexAnalyzer.Analyze(br);
        if(SyntaxAnalyzer.Analyze(a)){
            System.out.println("Good program");
            Stack<Pair<String, CounterMachine>> result = Emulator.Make(a);
            for(var resultPair : result){
                CounterMachine.libs.put(resultPair.first, resultPair.second);
            }

            Map<String, Integer> programArgs = new TreeMap<>();
            Scanner in = new Scanner(System.in);
            while(in.hasNext()){
                programArgs.put(in.next(), in.nextInt());
            }

            Map<String, Integer> programResult = CounterMachine.libs.get("main").startLikeMain(programArgs);
            for(var programPair : programResult.entrySet()){
                System.out.println(programPair.getKey() + " " + programPair.getValue());
            }
        }
        else{
            System.out.println("Bad program");
        }
    }
}
