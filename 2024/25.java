/*
 * Advent of Code 2024
 * Day 25: Code Chronicle
 */

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

class Day25 {
    private final ArrayList<int[]> locks = new ArrayList<>();
    private final ArrayList<int[]> keys = new ArrayList<>();


    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                int[] columns = new int[] {0, 0, 0, 0, 0};
                String topLine = line;
                line = fileReader.nextLine();
                int height = 0;
                while (height < 5) {
                    for (int i = 0; i < line.length(); i++) {
                        columns[i] += line.charAt(i) == '.' ? 0 : 1;
                    }
                    line = fileReader.nextLine();
                    height++;
                }
                if (topLine.equals("#####")) {
                    locks.add(new int[]{
                            columns[0], columns[1], columns[2], columns[3], columns[4]
                    });
                } else {
                    keys.add(new int[]{
                            columns[0], columns[1], columns[2], columns[3], columns[4]
                    });
                }
                if (fileReader.hasNextLine()) {
                    fileReader.nextLine();
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int partOne() {
        int matches = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                boolean match = true;
                for (int i = 0; i < 5; i++) {
                    if (key[i] + lock[i] > 5) {
                        match = false;
                        break;
                    }
                }
                matches += match ? 1 : 0;
            }
        }
        return matches;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day25 solution = new Day25();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %s\n", "Get 49 stars first \uD83E\uDD72");
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

