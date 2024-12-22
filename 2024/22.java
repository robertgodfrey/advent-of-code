/*
 * Advent of Code 2024
 * Day 22: Monkey Market
 */

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

class Day22 {
    private final ArrayList<Long> initialSecretNumbers = new ArrayList<>();
    private final HashMap<String, int[]> sequencePrices = new HashMap<>();
    private final Queue<Integer> currentSequence = new LinkedList<>();
    private int monkeyIndex = 0;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                initialSecretNumbers.add(Long.parseLong(fileReader.nextLine()));
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDiffToSequence(long prev, long current) {
        String originalNum = String.valueOf(prev);
        String newNum = String.valueOf(current);
        int diff = Integer.parseInt(newNum.substring(newNum.length() - 1))
                - Integer.parseInt(originalNum.substring(originalNum.length() - 1));
        if (currentSequence.size() == 4) {
            currentSequence.poll();
        }
        currentSequence.add(diff);
        StringBuilder seq = new StringBuilder();
        for (int num : currentSequence) {
            seq.append(num).append(",");
        }
        String key = seq.toString();
        if (!sequencePrices.containsKey(key)) {
            sequencePrices.put(key, new int[initialSecretNumbers.size()]);
            Arrays.fill(sequencePrices.get(key), -1);
        }
        if (sequencePrices.get(key)[monkeyIndex] == -1) {
            sequencePrices.get(key)[monkeyIndex] = Integer.parseInt(newNum.substring(newNum.length() - 1));
        }
    }

    private long calcNextSecretNumber(long number) throws Exception {
        long result = number;
        long multiplier = result * 64;
        result = result ^ multiplier;
        result = result % 16777216;

        long divisor = (long) Math.floor((double) result / 32);
        result = result ^ divisor;
        result = result % 16777216;

        multiplier = result * 2048;
        result = result ^ multiplier;
        result = result % 16777216;

        addDiffToSequence(number, result);
        return result;
    }

    private long partOne() throws Exception {
        long sum = 0;
        for (long num : initialSecretNumbers) {
            long result = num;
            for (int i = 0; i < 2000; i++) {
                result = calcNextSecretNumber(result);
            }
            sum += result;
            monkeyIndex++;
        }
        return sum;
    }

    private int partTwo() {
        int maxPrice = 0;
        for (Map.Entry<String, int[]> seqPrices : sequencePrices.entrySet()) {
            int thisPrice = 0;
            for (int price : seqPrices.getValue()) {
                thisPrice += Math.max(price, 0);
            }
            maxPrice = Math.max(thisPrice, maxPrice);
        }
        return maxPrice;
    }

    public static void main(String[] args) throws Exception {
        Instant start = Instant.now();
        Day22 solution = new Day22();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

