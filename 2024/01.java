/*
 * Advent of Code 2024
 * Day 1: Historian Hysteria
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Scanner;


class Day01 {
    private ArrayList<Integer> listA = new ArrayList<Integer>();
    private ArrayList<Integer> listB = new ArrayList<Integer>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                listA.add(Integer.parseInt(line.split("   ")[0]));
                listB.add(Integer.parseInt(line.split("   ")[1]));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private void partOne() {
        int distance = 0;
        listA.sort(Comparator.naturalOrder());
        listB.sort(Comparator.naturalOrder());
        for (int i = 0; i < listA.size(); i++) {
            distance += Math.abs(listA.get(i) - listB.get(i));
        }
        System.out.println(String.format("Part one: %d", distance));
    }

    private void partTwo() {
        int distance = 0;
        Hashtable<Integer, Integer> occurences = new Hashtable<>();
        for (int num : listB) {
            if (occurences.containsKey(num)) {
                occurences.put(num, occurences.get(num) + 1);
            } else {
                occurences.put(num, 1);
            }
        }
        for (int num : listA) {
            distance += occurences.containsKey(num) ? occurences.get(num) * num : 0;
        }
        System.out.println(String.format("Part two: %d", distance));
    }

    public static void main(String[] args) {
        Day01 solution = new Day01();
        solution.getInput();
        solution.partOne();
        solution.partTwo();
    }
}
