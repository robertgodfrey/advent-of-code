/*
 * Advent of Code 2024
 * Day 7: Bridge Repair
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


class Day07 {
    private final ArrayList<ArrayList<BigInteger>> equations = new ArrayList<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            int index = 0;
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] nums = line.split(" ");
                equations.add(new ArrayList<>());
                equations.get(index).add(new BigInteger(line.split(":")[0]));
                for (int i = 1; i < nums.length; i++) {
                    equations.get(index).add(new BigInteger(nums[i]));
                }
                index++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean maths(BigInteger target, BigInteger current, BigInteger[] nums, boolean concatenate) {
        BigInteger combinedNum = new BigInteger(String.format("%d%d", current, nums[0]));
        if (nums.length == 1) {
            return nums[0].add(current).equals(target) || nums[0].multiply(current).equals(target)
                    || (concatenate && combinedNum.equals(target));
        }
        return maths(target, current.add(nums[0]), Arrays.copyOfRange(nums, 1, nums.length), concatenate)
                || maths(target, current.multiply(nums[0]), Arrays.copyOfRange(nums, 1, nums.length), concatenate)
                || (concatenate && maths(target, combinedNum, Arrays.copyOfRange(nums, 1, nums.length), concatenate));
    }

    private void solve() {
        BigInteger partOneSum = new BigInteger("0");
        BigInteger partTwoSum = new BigInteger("0");
        for (ArrayList<BigInteger> equation : equations) {
            BigInteger[] currentList = equation.toArray(new BigInteger[0]);
            if (maths(currentList[0], currentList[1], Arrays.copyOfRange(currentList, 2, currentList.length), false)) {
                partOneSum = partOneSum.add(currentList[0]);
            }
            if (equation.size() <= 3) {
                if (new BigInteger(String.format("%d%d", currentList[1], currentList[2])).equals(currentList[0])) {
                    partTwoSum = partTwoSum.add(currentList[0]);
                    continue;
                }
            } else if (maths(currentList[0], new BigInteger(String.format("%d%d", currentList[1], currentList[2])),
                    Arrays.copyOfRange(currentList, 3, currentList.length), true)) {
                partTwoSum = partTwoSum.add(currentList[0]);
                continue;
            }
            if (maths(currentList[0], currentList[1], Arrays.copyOfRange(currentList, 2, currentList.length), true)) {
                partTwoSum = partTwoSum.add(currentList[0]);
            }
        }
        System.out.printf("Part one: %s\n", partOneSum);
        System.out.printf("Part two: %s\n", partTwoSum);
    }

    public static void main(String[] args) {
        Day07 solution = new Day07();
        solution.getInput();
        solution.solve();
    }
}
