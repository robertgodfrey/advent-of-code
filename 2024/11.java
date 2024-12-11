/*
 * Advent of Code 2024
 * Day 11: Plutonian Pebbles
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.Scanner;
import java.util.HashMap;

class Day11 {
    HashMap<Long, Long> stones = new HashMap<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            for (String stone : fileReader.nextLine().split(" ")) {
                long stoneNum = Long.parseLong(stone);
                if (!stones.containsKey(stoneNum)) {
                    stones.put(stoneNum, 0L);
                }
                stones.put(stoneNum, stones.get(stoneNum) + 1);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long blink(int numTimes) {
        HashMap<Long, Long> stonesCopyRef = new HashMap<>(stones);
        HashMap<Long, Long> stonesCopyWorking = new HashMap<>();
        long numStones = 0;
        for (int blinks = 0; blinks < numTimes; blinks++) {
            for (long engravedNum : stonesCopyRef.keySet()) {
                if (engravedNum == 0) {
                    stonesCopyWorking.put(1L, stonesCopyWorking.getOrDefault(1L, 0L) + stonesCopyRef.get(0L));
                } else if (String.valueOf(engravedNum).length() % 2 == 0) {
                    String numString = String.valueOf(engravedNum);
                    long leftHalf = Long.parseLong(numString.substring(0, numString.length() / 2));
                    long rightHalf = Long.parseLong(numString.substring(numString.length() / 2));
                    long newSumLeft = stonesCopyWorking.getOrDefault(leftHalf, 0L) + stonesCopyRef.get(engravedNum);
                    stonesCopyWorking.put(leftHalf, newSumLeft);
                    long newSumRight = stonesCopyWorking.getOrDefault(rightHalf, 0L) + stonesCopyRef.get(engravedNum);
                    stonesCopyWorking.put(rightHalf, newSumRight);
                } else {
                    stonesCopyWorking.put(engravedNum * 2024L,
                            stonesCopyWorking.getOrDefault(engravedNum * 2024L, 0L) + stonesCopyRef.get(engravedNum));
                }
            }
            stonesCopyRef = new HashMap<>(stonesCopyWorking);
            stonesCopyWorking = new HashMap<>();
        }
        for (Long engravedNum : stonesCopyRef.keySet()) {
            numStones += stonesCopyRef.get(engravedNum);
        }
        return numStones;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day11 solution = new Day11();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.blink(25));
        System.out.printf("Part two: %d\n", solution.blink(75));
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

