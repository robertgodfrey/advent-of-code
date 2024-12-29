/*
 * Advent of Code 2024
 * Day 24: Crossed Wires
 */

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Day24 {
    private final Map<String, Map<String, String>> xyMap = new HashMap<>();
    private final ArrayList<String> rules = new ArrayList<>();
    private final Map<String, Integer> initialBits = new HashMap<>();
    Map<String, Integer> gateValues;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            String line = fileReader.nextLine();
            while (fileReader.hasNextLine() && !line.equals("")) {
                initialBits.put(line.split(": ")[0], Integer.parseInt(line.split(": ")[1]));
                line = fileReader.nextLine();
            }
            while (fileReader.hasNextLine()) {
                line = fileReader.nextLine();
                rules.add(line);
            }
            gateValues = new HashMap<>(initialBits);
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateXyMap() {
        for (String gate : rules) {
            String[] parts = gate.split(" ");
            String gateOne = parts[0];
            String res = parts[4];
            String action = parts[1];
            if (gateOne.charAt(0) != 'x' && gateOne.charAt(0) != 'y') {
                continue;
            }
            if (!xyMap.containsKey(gateOne.substring(1))) {
                xyMap.put(gateOne.substring(1), new HashMap<>());
            }
            xyMap.get(gateOne.substring(1)).put(action, res);
        }
    }

    private void findSuspiciousGates() {
        ArrayList<String> sortedKeys = new ArrayList<>(xyMap.keySet());
        Collections.sort(sortedKeys);
        String carry = "";
        String carry2 = "";
        String xorGate;
        String andGate;
        for (int currentGate = 0; currentGate < sortedKeys.size(); currentGate++) {
            String key = sortedKeys.get(currentGate);
            xorGate = xyMap.get(key).get("XOR");
            andGate = xyMap.get(key).get("AND");
            if (xorGate.equals("z00") && currentGate == 0) {
                carry = xyMap.get(key).get("AND");
                continue;
            }
            for (String rule : rules) {
                String leftSide = rule.split(" -> ")[0];
                String rightSide = rule.split(" -> ")[1];
                if (leftSide.contains(xorGate)) {
                    if (leftSide.contains("XOR")) {
                        // should lead to current gate
                        String goalGate = String.format("z%2d", currentGate).replace(" ", "0");
                        if (!rightSide.equals(goalGate)) {
                            System.out.println("INCORRECT: right side != goal gate (expected " + goalGate + ")\n" + rule);
                        }
                        if (!leftSide.contains(carry)) {
                            System.out.println("INCORRECT: left side does not contain carry (expected " + carry + " XOR " + xorGate + ")\n" + rule);
                        }
                    } else if (leftSide.contains("AND")) {
                        if (!leftSide.contains(carry)) {
                            System.out.println("INCORRECT: left side does not contain carry (expected " + xorGate + " AND " + carry + ")\n" + rule);
                        } else if (!leftSide.contains(xorGate)) {
                            System.out.println("INCORRECT: left side does not contain XOR gate (expected " + xorGate + ")\n" + rule);
                        } else {
                            carry2 = rightSide;
                        }
                    }
                }
            }
            for (String rule : rules) {
                String leftSide = rule.split(" -> ")[0];
                String rightSide = rule.split(" -> ")[1];
                if (leftSide.contains("OR") && leftSide.contains(andGate) && leftSide.contains(carry2)) {
                    carry = rightSide;
                    break;
                }
            }
        }
    }

    private long bitStringToLong(String bits) {
        long result = 0;
        for (int i = 0; i < bits.length(); i++) {
            result += bits.charAt(i) == '1' ? Math.pow(2, i) : 0;
        }
        return result;
    }

    private String longToBitStringReversed(long num) {
        StringBuilder result = new StringBuilder();
        for (int i = 45; i >= 0; i--) {
            result.append(num >= Math.pow(2, i) ? "1" : "0");
            num %= Math.pow(2, i);
        }
        return result.reverse().toString();
    }

    private long partOne() {
        ArrayList<String> rulesCopy = new ArrayList<>(rules);
        ArrayList<Integer> indicesToRemove = new ArrayList<>();
        while (!rulesCopy.isEmpty()) {
            for (int i = 0; i < rulesCopy.size(); i++) {
                String rule = rulesCopy.get(i);
                String gateOne = rule.split(" ")[0];
                String gateTwo = rule.split(" ")[2];
                if (!gateValues.containsKey(gateOne) || !gateValues.containsKey(gateTwo)) {
                    continue;
                }
                int gateOneVal = gateValues.get(gateOne);
                int gateTwoVal = gateValues.get(gateTwo);
                String command = rule.split(" ")[1];
                String writeGate = rule.split(" ")[4];
                int writeVal = switch (command) {
                    case "AND" -> gateOneVal == 1 && gateTwoVal == 1 ? 1 : 0;
                    case "OR" -> gateOneVal == 1 || gateTwoVal == 1 ? 1 : 0;
                    default -> gateOneVal != gateTwoVal ? 1 : 0;
                };
                gateValues.put(writeGate, writeVal);
                indicesToRemove.add(i);
            }
            for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
                rulesCopy.remove((int) indicesToRemove.get(i));
            }
            indicesToRemove.clear();
        }
        return getVarValue('z');
    }

    private long getVarValue(char key) {
        StringBuilder bitString = new StringBuilder();
        ArrayList<String> sortedKeys = new ArrayList<>(gateValues.keySet());
        Collections.sort(sortedKeys);
        for (String gate : sortedKeys) {
            if (gate.charAt(0) == key) {
                bitString.append(gateValues.get(gate));
            }
        }
        return bitStringToLong(bitString.toString());
    }

    private void partTwo() {
        long x = getVarValue('x');
        long y = getVarValue('y');
        System.out.println("X = " + x);
        System.out.println("Y = " + y);
        System.out.println("EXPECTED Z = " + (x + y));
        System.out.println("Diff = " + (x + y - getVarValue('z')));
        System.out.println(longToBitStringReversed(x + y - getVarValue('z')));
        findSuspiciousGates();
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day24 solution = new Day24();
        solution.getInput();
        solution.populateXyMap();
        System.out.printf("Part one: %d\n", solution.partOne());
        solution.partTwo();
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

