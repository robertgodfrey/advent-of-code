/*
 * Advent of Code 2024
 * Day 21: Keypad Conundrum
 */

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Day21 {
    private final ArrayList<String> buttonCombos = new ArrayList<>();
    private final Map<Character, int[]> numPad = new HashMap<>();
    private final Map<Character, int[]> dirPad = new HashMap<>();
    private final Map<String, Long> cachedLength = new HashMap<>();
    private Map<String, String> numPadGraph;
    private Map<String, String> dirPadGraph;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                buttonCombos.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMaps() {
        numPad.put('7', new int[] {0, 3}); numPad.put('8', new int[] {1, 3}); numPad.put('9', new int[] {2, 3});
        numPad.put('4', new int[] {0, 2}); numPad.put('5', new int[] {1, 2}); numPad.put('6', new int[] {2, 2});
        numPad.put('1', new int[] {0, 1}); numPad.put('2', new int[] {1, 1}); numPad.put('3', new int[] {2, 1});
                                           numPad.put('0', new int[] {1, 0}); numPad.put('A', new int[] {2, 0});

                                           dirPad.put('^', new int[] {1, 1}); dirPad.put('A', new int[] {2, 1});
        dirPad.put('<', new int[] {0, 0}); dirPad.put('v', new int[] {1, 0}); dirPad.put('>', new int[] {2, 0});
    }

    private Map<String, String> createGraph(Map<Character, int[]> keypad, int invalidY) {
        Map<String, String> graph = new HashMap<>();
        for (Map.Entry<Character, int[]> fromKey : keypad.entrySet()) {
            for (Map.Entry<Character, int[]> toKey : keypad.entrySet()) {
                int xDiff = fromKey.getValue()[0] - toKey.getValue()[0];
                int yDiff = fromKey.getValue()[1] - toKey.getValue()[1];
                String path = "<".repeat(Math.max(xDiff, 0)) + "v".repeat(Math.max(yDiff, 0))
                        + "^".repeat(Math.max(-yDiff, 0)) + ">".repeat(Math.max(-xDiff, 0));
                if ((fromKey.getValue()[0] == 0 && toKey.getValue()[1] == invalidY)
                        || (toKey.getValue()[0] == 0 && fromKey.getValue()[1] == invalidY)) {
                    path = new StringBuilder(path).reverse().toString();
                }
                graph.put(String.format("%c:%c", fromKey.getKey(), toKey.getKey()), path + "A");
            }
        }
        return graph;
    }

    private String convert(String sequence, Map<String, String> graph) {
        StringBuilder conversion = new StringBuilder();
        char prevKey = 'A';
        for (char nextKey : sequence.toCharArray()) {
            conversion.append(graph.get(String.format("%c:%c", prevKey, nextKey)));
            prevKey = nextKey;
        }
        return conversion.toString();
    }

    private long getLength(String buttonSequence, int iterations) {
        if (iterations == 0) {
            return buttonSequence.length();
        }
        char prevButton = 'A';
        long length = 0;
        for (char nextButton : buttonSequence.toCharArray()) {
            long thisLength;
            if (cachedLength.containsKey(String.format("%c:%c:%d", prevButton, nextButton, iterations))) {
                thisLength = cachedLength.get(String.format("%c:%c:%d", prevButton, nextButton, iterations));
            } else {
                thisLength = getLength(dirPadGraph.get(String.format("%c:%c", prevButton, nextButton)), iterations - 1);
                cachedLength.put(String.format("%c:%c:%d", prevButton, nextButton, iterations), thisLength);
            }
            length += thisLength;
            prevButton = nextButton;
        }
        return length;
    }

    private int partOne() {
        int total = 0;
        for (String buttonCombo : buttonCombos) {
            String conversion = convert(buttonCombo, numPadGraph);
            conversion = convert(conversion, dirPadGraph);
            conversion = convert(conversion, dirPadGraph);
            total += Integer.parseInt(buttonCombo.substring(0, 3)) * conversion.length();
        }
        return total;
    }

    private long partTwo() {
        long total = 0;
        for (String buttonCombo : buttonCombos) {
            String conversion = convert(buttonCombo, numPadGraph);
            total += Integer.parseInt(buttonCombo.substring(0, 3)) * getLength(conversion, 25);
        }
        return total;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day21 solution = new Day21();
        solution.initMaps();
        solution.getInput();
        solution.numPadGraph = solution.createGraph(solution.numPad, 0);
        solution.dirPadGraph = solution.createGraph(solution.dirPad, 1);
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

