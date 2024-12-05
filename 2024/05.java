/*
 * Advent of Code 2024
 * Day 5: Print Queue
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


class Day05 {
    private Hashtable<Integer, Set<Integer>> rules = new Hashtable<Integer, Set<Integer>>();
    private ArrayList<ArrayList<Integer>> updates = new ArrayList<ArrayList<Integer>>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            // get rules
            String line = fileReader.nextLine();
            while (!line.equals("")) {
                int key = Integer.parseInt(line.split("\\|")[0]);
                int value = Integer.parseInt(line.split("\\|")[1]);
                if (!rules.containsKey(key)) {
                    rules.put(key, new HashSet<Integer>());
                } 
                rules.get(key).add(value);
                line = fileReader.nextLine(); 
            }
            // get updates
            int index = 0;
            while (fileReader.hasNextLine()) {
                updates.add(new ArrayList<Integer>());
                for (String num : fileReader.nextLine().split(",")) {
                    updates.get(index).add(Integer.parseInt(num));
                }
                index++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private ArrayList<Integer> fixProblems(ArrayList<Integer> problemList) {
        ArrayList<Integer> fixedList = new ArrayList<Integer>(problemList);
        boolean stop = false;
        for (int i = 0; i < problemList.size(); i++) {
            for (int j = i + 1; j < problemList.size(); j++) {
                if (!rules.containsKey(problemList.get(i))) {
                    fixedList.set(i, problemList.get(problemList.size() - 1));
                    fixedList.set(problemList.size() - 1, problemList.get(i));
                    fixedList = fixProblems(fixedList);
                    stop = true;
                    break;
                } else if (!rules.get(problemList.get(i)).contains(problemList.get(j))) {
                    fixedList.set(i, problemList.get(j));
                    fixedList.set(j, problemList.get(i));
                    fixedList = fixProblems(fixedList);
                    stop = true;
                    break;
                }
            }
            if (stop) {
                break;
            }
        }
        return fixedList;
    }

    private void solve() {
        int partOneSum = 0;
        int partTwoSum = 0;
        for (ArrayList<Integer> updateList : updates) {
            boolean breaksRules = false;
            for (int i = 0; i < updateList.size(); i++) {
                for (int j = i + 1; j < updateList.size(); j++) {
                    if (!rules.containsKey(updateList.get(i)) || !rules.get(updateList.get(i)).contains(updateList.get(j))) {
                        breaksRules = true;
                        ArrayList<Integer> fixedList = fixProblems(updateList);
                        partTwoSum += fixedList.get(fixedList.size() / 2);
                        break;
                    }
                }
                if (breaksRules) {
                    break;
                }
            }
            if (!breaksRules) {
                partOneSum += updateList.get(updateList.size() / 2);
            }
        }
        System.out.println(String.format("Part one: %d", partOneSum));
        System.out.println(String.format("Part two: %d", partTwoSum));
    }

    public static void main(String[] args) {
        Day05 solution = new Day05();
        solution.getInput();
        solution.solve();
    }
}
