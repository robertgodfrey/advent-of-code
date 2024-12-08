/*
 * Advent of Code 2024
 * Day 8: Resonant Collinearity
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


class Day08 {
    private final ArrayList<String> map = new ArrayList<>();
    HashMap<Character, ArrayList<Integer[]>> nodes = new HashMap<>();
    Set<String> antinodes = new HashSet<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                map.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getNodes() {
        for (int row = 0; row < map.size(); row++) {
            for (int col = 0; col < map.get(0).length(); col++) {
                char currentPos = map.get(row).charAt(col);
                if (currentPos  != '.') {
                    if (!nodes.containsKey(currentPos)) {
                        nodes.put(currentPos, new ArrayList<>());
                    }
                    nodes.get(currentPos).add(new Integer[] {col, row});
                }
            }
        }
    }

    private boolean isInBounds(Integer[] node) {
        return node[0] >= 0 && node[0] < map.get(0).length() && node[1] >= 0 && node[1] < map.size();
    }

    private void addAntinodes(Integer[] nodeA, Integer[] nodeB, boolean harmonics) {
        int distX = nodeA[0] - nodeB[0];
        int distY = nodeA[1] - nodeB[1];
        Integer[] antinodeA = new Integer[] {nodeA[0] + distX, nodeA[1] + distY};
        Integer[] antinodeB = new Integer[] {nodeB[0] - distX, nodeB[1] - distY};
        if (harmonics) {
            while (isInBounds(antinodeA)) {
                antinodes.add(Arrays.toString(antinodeA));
                antinodeA = new Integer[] {antinodeA[0] + distX, antinodeA[1] + distY}; 
            }
            while (isInBounds(antinodeB)) {
                antinodes.add(Arrays.toString(antinodeB));
                antinodeB = new Integer[] {antinodeB[0] - distX, antinodeB[1] - distY}; 
            }
        } else {
            if (isInBounds(antinodeA)) {
                antinodes.add(Arrays.toString(antinodeA));
            }
            if (isInBounds(antinodeB)) {
                antinodes.add(Arrays.toString(antinodeB));
            }
        }
    }

    private void partOne() {
        Set<Character> keys = nodes.keySet();
        for (char key : keys) {
            for (int i = 0; i < nodes.get(key).size(); i++) {
                Integer[] nodeA = nodes.get(key).get(i);
                for (int j = i + 1; j < nodes.get(key).size(); j++) {
                    Integer[] nodeB = nodes.get(key).get(j);
                    addAntinodes(nodeA, nodeB, false);
                }
            }
        }
        System.out.printf("Part one: %s\n", antinodes.size());
    }

    private void partTwo() {
        antinodes.clear();
        Set<Character> keys = nodes.keySet();
        for (char key : keys) {
            for (int i = 0; i < nodes.get(key).size(); i++) {
                Integer[] nodeA = nodes.get(key).get(i);
                antinodes.add(Arrays.toString(nodeA));
                for (int j = i + 1; j < nodes.get(key).size(); j++) {
                    Integer[] nodeB = nodes.get(key).get(j);
                    addAntinodes(nodeA, nodeB, true);
                }
            }
        }
        System.out.printf("Part two: %s\n", antinodes.size());
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day08 solution = new Day08();
        solution.getInput();
        solution.getNodes();
        solution.partOne();
        solution.partTwo();
        System.out.println("Execution time: " + Duration.between(start, Instant.now()).toMillis() + " milliseconds");
    }
}

