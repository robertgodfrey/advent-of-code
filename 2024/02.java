/*
 * Advent of Code 2024
 * Day 2: Red-Nosed Reports 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class Day02 {
    private ArrayList<ArrayList<Integer>> reports = new ArrayList<ArrayList<Integer>>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            for (int i = 0; fileReader.hasNextLine(); i++) {
                reports.add(new ArrayList<Integer>());
                String line = fileReader.nextLine();
                for (String item : line.split(" ")) {
                    reports.get(i).add(Integer.parseInt(item));
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private boolean isSafe(ArrayList<Integer> list, boolean removedLevel) {
        boolean isDecreasing = list.get(0) > list.get(1);
        for (int i = 1; i < list.size(); i++) {
            int thisNum = list.get(i);
            int prevNum = list.get(i - 1);
            if (
                Math.abs(thisNum - prevNum) < 1 ||
                Math.abs(thisNum - prevNum) > 3 ||
                (isDecreasing && prevNum < thisNum) ||
                (!isDecreasing && prevNum > thisNum)
            ) {
                if (removedLevel) {
                    return false;
                }
                boolean removedPreviousIsSafe = false;
                ArrayList<Integer> removedCurrent = new ArrayList<Integer>(list);
                ArrayList<Integer> removedNext = new ArrayList<Integer>(list);
                removedCurrent.remove(i - 1);
                removedNext.remove(i);
                if (i > 1) {
                    ArrayList<Integer> removedPrevious = new ArrayList<Integer>(list);
                    removedPrevious.remove(i - 2);
                    removedPreviousIsSafe = isSafe(removedPrevious, true);
                }
                return removedPreviousIsSafe || isSafe(removedCurrent, true) || isSafe(removedNext, true);
            }
        }
        return true;
    }

    private void partOne() {
        int safeReports = 0;
        for (ArrayList<Integer> levelsList : reports) {
            safeReports += isSafe(levelsList, true) ? 1 : 0;
        }
        System.out.println(String.format("Part one: %d", safeReports));
    }

    private void partTwo() {
        int safeReports = 0;
        for (ArrayList<Integer> levelsList : reports) {
            safeReports += isSafe(levelsList, false) ? 1 : 0;
        }
        System.out.println(String.format("Part two: %d", safeReports));
    }

    public static void main(String[] args) {
        Day02 solution = new Day02();
        solution.getInput();
        solution.partOne();
        solution.partTwo();
    }
}
