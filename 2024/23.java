/*
 * Advent of Code 2024
 * Day 23: LAN Party
 */

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Day23 {
    private final Map<String, Set<String>> computers = new HashMap<>();
    private final Set<String> checkedConnections = new HashSet<>();
    private int maxGroupSize = 0;
    private String maxGroup = "";


    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String leftComputer = line.split("-")[0];
                String rightComputer = line.split("-")[1];
                if (!computers.containsKey(leftComputer)) {
                    computers.put(leftComputer, new HashSet<>());
                }
                computers.get(leftComputer).add(rightComputer);
                if (!computers.containsKey(rightComputer)) {
                    computers.put(rightComputer, new HashSet<>());
                }
                computers.get(rightComputer).add(leftComputer);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int partOne() {
        Set<String> uniqueConnections = new HashSet<>();
        for (String rootComputer : computers.keySet()) {
            Set<String> rootConnections = computers.get(rootComputer);
            for (String rootConnection : rootConnections) {
                Set<String> leafConnections = computers.get(rootConnection);
                for (String leafConnection : leafConnections) {
                    if (computers.get(leafConnection).contains(rootComputer)
                            && (leafConnection.charAt(0) == 't'
                            || rootConnection.charAt(0) == 't'
                            || rootComputer.charAt(0) == 't')) {
                        ArrayList<String> lanParty = new ArrayList<>();
                        lanParty.add(rootComputer);
                        lanParty.add(rootConnection);
                        lanParty.add(leafConnection);
                        Collections.sort(lanParty);
                        uniqueConnections.add(String.join(",", lanParty));
                    }
                }
            }
        }
        return uniqueConnections.size();
    }

    private void dfs(Set<String> currentConnections, String rootComputer) {
        Set<String> tempSet = new HashSet<>(currentConnections);
        tempSet.retainAll(computers.get(rootComputer));
        if (tempSet.size() != currentConnections.size()) {
            return;
        }
        tempSet.add(rootComputer);
        ArrayList<String> check = new ArrayList<>(tempSet);
        Collections.sort(check);
        String sortedAndJoined = String.join(",", check);
        if (checkedConnections.contains(sortedAndJoined)) {
            return;
        }
        checkedConnections.add(sortedAndJoined);
        if (tempSet.size() > maxGroupSize) {
            maxGroupSize = tempSet.size();
            maxGroup = String.join(",", sortedAndJoined);
        }
        for (String computer : computers.get(rootComputer)) {
            if (tempSet.contains(computer)) {
                continue;
            }
            dfs(tempSet, computer);
        }
    }

    private String partTwo() {
        for (String rootComputer : computers.keySet()) {
            Set<String> thisConnectionSet = new HashSet<>();
            thisConnectionSet.add(rootComputer);
            for (String computer : computers.get(rootComputer)) {
                dfs(thisConnectionSet, computer);
            }
        }
        return maxGroup;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day23 solution = new Day23();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %s\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

