/*
 * Advent of Code 2024
 * Day 19: Linen Layout
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

class Day19 {
    private final Set<String> designsToDisplay = new HashSet<>();
    private final HashMap<String, Boolean> knownDesigns = new HashMap<>();
    private final HashMap<String, Long> knownDesignsNums = new HashMap<>();
    private Set<String> availableTowels = new HashSet<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            this.availableTowels = new HashSet<>(List.of(fileReader.nextLine().split(", ")));
            fileReader.nextLine();
            while (fileReader.hasNextLine()) {
                designsToDisplay.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean designPossible(String design) {
        if (knownDesigns.containsKey(design)) {
            return knownDesigns.get(design);
        }
        if (availableTowels.contains(design)) {
            knownDesigns.put(design, true);
            return true;
        }
        if (design.length() == 1) {
            return false;
        }
        for (int i = 1; i < design.length(); i++) {
            if (designPossible(design.substring(0, i)) && designPossible(design.substring(i))) {
                knownDesigns.put(design, true);
                return true;
            }
        }
        knownDesigns.put(design, false);
        return false;
    }

    private long countPossibleCombos(String design) {
        if (knownDesignsNums.containsKey(design)) {
            return knownDesignsNums.get(design);
        }
        if (design.length() == 0) {
            return 1;
        }
        long numCombos = 0L;
        for (String towel : availableTowels) {
            if (design.startsWith(towel)) {
                numCombos += countPossibleCombos(design.substring(towel.length()));
            }
        }
        knownDesignsNums.put(design, numCombos);
        return numCombos;
    }

    private int partOne() {
        int possibleDesigns = 0;
        for (String design : designsToDisplay) {
            boolean possible = designPossible(design);
            possibleDesigns += possible ? 1 : 0;
        }
        return possibleDesigns;
    }

    private long partTwo() {
        long possibleDesigns = 0;
        for (String design : designsToDisplay) {
            possibleDesigns += countPossibleCombos(design);
        }
        return possibleDesigns;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day19 solution = new Day19();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

