package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

class Command{
    public String startLabel = null;
    public String command = null;
    public ArrayList<String> counters = null;
    public ArrayList<String> endLabels = null;

    public Command(String startLabel, String command, ArrayList<String> counters, ArrayList<String> endLabels){
        this.startLabel = startLabel;
        this.command = command;
        this.counters = counters;
        this.endLabels = endLabels;
    }
}

class Pair<T, V>{
    public T first = null;
    public V second = null;

    Pair(T first, V second){
        this.first = first;
        this.second = second;
    }
}

public class CounterMachine {
    private String startLabel = null;
    private String currentLabel = null;
    private Map<String, Command> commands = null;

    private ArrayList<String> labelsQueue = null;
    private ArrayList<String> countersQueue = null;
    private Map<String, Integer> counters = null;

    // initialize new counter machine
    public CounterMachine(String startLabel, ArrayList<String> countersQueue, ArrayList<String> labelsQueue){
        this.startLabel = startLabel;
        this.countersQueue = countersQueue;
        this.labelsQueue = labelsQueue;
        commands = new TreeMap<>();
    }

    public void getInfo(){
        System.out.println("Start Label: " + startLabel);
        System.out.println("All counters:");
        for(int i = 0; i < countersQueue.size(); i++){
            System.out.println(countersQueue.get(i));
        }
        System.out.println("All end states:");
        for(int i = 0; i < labelsQueue.size(); i++){
            System.out.println(labelsQueue.get(i));
        }
        System.out.println("All commands:");
        for(var entry : commands.entrySet()){
            System.out.print(entry.getValue().startLabel + " ");
            System.out.print(entry.getValue().command + " (");
            for(String s : entry.getValue().counters){
                System.out.print(s + " ");
            }
            System.out.print(") (");
            for(String s : entry.getValue().endLabels){
                System.out.print(s + " ");
            }
            System.out.println(")");
        }
    }

    public void addCommand(Command command){
        commands.put(command.startLabel, command);
    }

    // returns end label and map of new counters values.
    public Pair<String, Map<String, Integer>> startMachine(ArrayList<Integer> argsCountersQueue, ArrayList<String> endLabels) {
        for(int i = 0; i < countersQueue.size(); i++){
            counters.put(countersQueue.get(i), argsCountersQueue.get(i));
        } // insert start counters
        // DO SOME WORK
        return new Pair<>(new String(), counters); // get all needed info form this counters on other side
    }
}
