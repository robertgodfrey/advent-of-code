/*
 * Advent of Code 2024
 * Day 4: Ceres Search
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class Day04 {
    private ArrayList<String> puzzle = new ArrayList<String>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                puzzle.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private boolean xmasMatch(int[][] c) {
        return String.format("%c%c%c%c",
            puzzle.get(c[0][0]).charAt(c[0][1]),
            puzzle.get(c[1][0]).charAt(c[1][1]),
            puzzle.get(c[2][0]).charAt(c[2][1]),
            puzzle.get(c[3][0]).charAt(c[3][1])).equals("XMAS");
    }

    private void partOne() {
        int numMatches = 0;
        for (int row = 0; row < puzzle.size(); row++) {
            for (int col = 0; col < puzzle.get(0).length(); col++) {
                if (col < puzzle.get(0).length() - 3) {
                    // foward
                    if (puzzle.get(row).substring(col, col + 4).equals("XMAS")) {
                        numMatches++;
                    }
                }
                if (col > 2) {
                    // backward
                    if (puzzle.get(row).substring(col - 3, col + 1).equals("SAMX")) {
                        numMatches++;
                    }
                }
                if (row < puzzle.size() - 3) {
                    // down
                    int[][] coordinates = {{row, col}, {row + 1, col}, {row + 2, col}, {row + 3, col}};
                    if (xmasMatch(coordinates)) {
                        numMatches++;
                    }

                }
                if (row > 2) {
                    // up
                    int[][] coordinates = {{row, col}, {row - 1, col}, {row - 2, col}, {row - 3, col}};
                    if (xmasMatch(coordinates)) {
                          numMatches++;
                       }

                }
                if (col < puzzle.get(0).length() - 3 && row < puzzle.size() - 3) {
                    // diagonal down right
                    int[][] coordinates = {{row, col}, {row + 1, col + 1}, {row + 2, col + 2}, {row + 3, col + 3}};
                    if (xmasMatch(coordinates)) {
                        numMatches++;
                    }
                }
                if (col > 2 && row < puzzle.size() - 3) {
                    // diagonal down left
                    int[][] coordinates = {{row, col}, {row + 1, col - 1}, {row + 2, col - 2}, {row + 3, col - 3}};
                    if (xmasMatch(coordinates)) {
                        numMatches++;
                    }
                }
                if (col > 2 && row > 2) {
                    // diagonal up left
                    int[][] coordinates = {{row, col}, {row - 1, col - 1}, {row - 2, col - 2}, {row - 3, col - 3}};
                    if (xmasMatch(coordinates)) {
                        numMatches++;
                    }
                }
                if (col < puzzle.get(0).length() - 3 && row > 2) {
                    // diagonal up right
                    int[][] coordinates = {{row, col}, {row - 1, col + 1}, {row - 2, col + 2}, {row - 3, col + 3}};
                    if (xmasMatch(coordinates)) {
                        numMatches++;
                    }
                }
            }
        }
        System.out.println(String.format("Part one: %d", numMatches));
    }

    private void partTwo() {
        int numMatches = 0;
        for (int row = 1; row < puzzle.size() - 1; row++) {
            for (int col = 1; col < puzzle.get(0).length() - 1; col++) {
                if (puzzle.get(row).charAt(col) == 'A') {
                    char topLeft = puzzle.get(row - 1).charAt(col - 1);
                    char bottomRight = puzzle.get(row + 1).charAt(col + 1);
                    char bottomLeft = puzzle.get(row + 1).charAt(col - 1);
                    char topRight = puzzle.get(row - 1).charAt(col + 1);
                    
                    if (topLeft == 'M' && bottomRight == 'S' && bottomLeft == 'M' && topRight == 'S') {
                        // MAS MAS
                        numMatches++;
                    } else if (topLeft == 'M' && bottomRight == 'S' && bottomLeft == 'S' && topRight == 'M') {
                        // MAS SAM
                        numMatches++;
                    } else if (topLeft == 'S' && bottomRight == 'M' && bottomLeft == 'M' && topRight == 'S') {
                        // SAM MAS
                        numMatches++;
                    } else if (topLeft == 'S' && bottomRight == 'M' && bottomLeft == 'S' && topRight == 'M') {
                        // SAM SAM
                        numMatches++;
                    }
                }
            }
        }
        System.out.println(String.format("Part two: %d", numMatches));
    }

    public static void main(String[] args) {
        Day04 solution = new Day04();
        solution.getInput();
        solution.partOne();
        solution.partTwo();
    }
}
