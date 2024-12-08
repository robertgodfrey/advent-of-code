/*
 * Advent of Code 2024
 * Day 7: Bridge Repair
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


class Day07 {
    private final ArrayList<ArrayList<Long>> equations = new ArrayList<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            int index = 0;
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] nums = line.split(" ");
                equations.add(new ArrayList<>());
                equations.get(index).add(Long.parseLong(line.split(":")[0]));
                for (int i = 1; i < nums.length; i++) {
                    equations.get(index).add(Long.parseLong(nums[i]));
                }
                index++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean maths(long target, long current, Long[] nums, boolean concatenate) {
        if (current > target) {
            return false;
        }
        long combinedNum = (long) (current * Math.pow(10, Math.floor(Math.log10(nums[0])) + 1)) + nums[0]; 
        if (nums.length == 1) {
            return nums[0] + current == target || nums[0] * current == target
                    || (concatenate && combinedNum == target);
        }
        return maths(target, current + nums[0], Arrays.copyOfRange(nums, 1, nums.length), concatenate)
                || maths(target, current * nums[0], Arrays.copyOfRange(nums, 1, nums.length), concatenate)
                || (concatenate && maths(target, combinedNum, Arrays.copyOfRange(nums, 1, nums.length), true));
    }

    private void solve() {
        long partOneSum = 0;
        long partTwoSum = 0;
        for (ArrayList<Long> equation : equations) {
            Long[] currentList = equation.toArray(new Long[0]);
            if (maths(currentList[0], currentList[1], Arrays.copyOfRange(currentList, 2, currentList.length), false)) {
                partOneSum += currentList[0];
            }
            long combinedNum = (long) (currentList[1] * Math.pow(10, Math.floor(Math.log10(currentList[2])) + 1)) + currentList[2];
            if (equation.size() <= 3) {
                if (combinedNum == currentList[0]) {
                    partTwoSum += currentList[0];
                    continue;
                }
            } else if (maths(currentList[0], combinedNum, Arrays.copyOfRange(currentList, 3, currentList.length), true)) {
                partTwoSum += currentList[0];
                continue;
            }
            if (maths(currentList[0], currentList[1], Arrays.copyOfRange(currentList, 2, currentList.length), true)) {
                partTwoSum += currentList[0];
            }
        }
        System.out.printf("Part one: %s\n", partOneSum);
        System.out.printf("Part two: %s\n", partTwoSum);
    }

    public static void main(String[] args) {
        Instant start = Instant.now();

        Day07 solution = new Day07();
        solution.getInput();
        solution.solve();

        System.out.println("Execution time: " + Duration.between(start, Instant.now()).toMillis() + " milliseconds");
    }
}

