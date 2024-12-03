/*
 * Advent of Code 2024
 * Day 3: Mull It Over 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class Day03 {
    private ArrayList<String> lines = new ArrayList<String>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                lines.add(line);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private void solve() {
        int partOneTotal = 0;
        int partTwoTotal = 0;
        boolean enabled = true;
        for (String line : lines) {
            for (int i = 0; i < line.length() - 4; i++) {
                if (line.substring(i, i + 4).equals("mul(")) {
                    int j = i + 4;
                    while (line.substring(j, j + 1).matches("\\d")) {
                        j++;
                    }
                    if (line.charAt(j) != ',') {
                        i = j - 1;
                        continue;
                    }
                    int commaIndex = j;
                    int firstNum = Integer.parseInt(line.substring(i + 4, j));
                    j++;
                    while (line.substring(j, j + 1).matches("\\d")) {
                        j++;
                    }
                    if (line.charAt(j) != ')') {
                        i = j - 1;
                        continue;
                    }
                    int secondNum = Integer.parseInt(line.substring(commaIndex + 1, j));
                    partOneTotal += firstNum * secondNum;
                    if (enabled) {
                        partTwoTotal += firstNum * secondNum;
                    }
                    i = j;
                } else if (line.substring(i, i + 4).equals("do()")) {
                    enabled = true;
                    i += 3;
                } else if (i < line.length() - 7 && line.substring(i, i + 7).equals("don't()")) {
                    enabled = false;
                    i += 6;
                }
            }
        }
        System.out.println(String.format("Part one: %d", partOneTotal));
        System.out.println(String.format("Part two: %d", partTwoTotal));
    }

    public static void main(String[] args) {
        Day03 solution = new Day03();
        solution.getInput();
        solution.solve();
    }
}
