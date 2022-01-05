package com.company;

import java.io.IOException;
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
    static public Map<String, CounterMachine> libs = new TreeMap<>();

    private String startLabel = null;
    private String currentLabel = null;
    private Map<String, Command> commands = null;

    private ArrayList<String> labelsQueue = null;
    private ArrayList<String> countersQueue = null;
    private Map<String, Integer> counters = null;

    public void addLib(String name, CounterMachine counterMachine){
        libs.put(name, counterMachine);
    }

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

    boolean DoWork(){
        Command nextCommand = commands.get(currentLabel);
        if(nextCommand == null) return false;

        if(nextCommand.command.equals("inc")){
            String counterName = nextCommand.counters.get(0);
            Integer value = counters.getOrDefault(counterName, 0);
            counters.put(counterName, value + 1);
            currentLabel = nextCommand.endLabels.get(0);
        }
        else if(nextCommand.command.equals("dec")){
            String counterName = nextCommand.counters.get(0);
            Integer value = counters.getOrDefault(counterName, 0);

            if(value > 0) {
                counters.put(counterName, value - 1);
                currentLabel = nextCommand.endLabels.get(0);
            }
            else{
                currentLabel = nextCommand.endLabels.get(1);
            }
        }
        else{
            ArrayList<Integer> localCounters = new ArrayList<>();
            for(int i = 0; i < nextCommand.counters.size(); i++){
                localCounters.add(counters.get(nextCommand.counters.get(i)));
            }
            var resultPair = libs.get(nextCommand.command).startMachine(nextCommand.counters, localCounters, nextCommand.endLabels);
            currentLabel = nextCommand.endLabels.get(resultPair.first);
            counters.putAll(resultPair.second);
        }
        return true;
    }

    public Map<String, Integer> startLikeMain(Map<String, Integer> args){
        currentLabel = startLabel;

        if(args != null) {
            this.counters = args;
        }
        else{
            this.counters = new TreeMap<>();
        }
        while(DoWork());

        return counters;
    }

    // returns end label and map of new counters values.
    public Pair<Integer, Map<String, Integer>> startMachine(ArrayList<String> namesCountersQueue, ArrayList<Integer> argsCountersQueue, ArrayList<String> endLabels) {
        this.counters = new TreeMap<>();
        for(int i = 0; i < countersQueue.size(); i++){
            if(argsCountersQueue.get(i) != null) {
                counters.put(countersQueue.get(i), argsCountersQueue.get(i));
            }
            else{
                counters.put(countersQueue.get(i), 0);
            }
        } // insert start counters

        currentLabel = startLabel;
        while(DoWork());

        Map<String, Integer> newCounters = new TreeMap<>(); // replace inside counters to outside counters
        for(int i = 0; i < namesCountersQueue.size(); i++){
            newCounters.put(namesCountersQueue.get(i), counters.get(countersQueue.get(i)));
        }

        for(int i = 0; i < labelsQueue.size(); i++){
            if(currentLabel.equals(labelsQueue.get(i))){
                return new Pair<>(i, newCounters); // get all needed info form this counters on other side
            }
        }
        throw new RuntimeException("This exit state is not exists");
    }
}
