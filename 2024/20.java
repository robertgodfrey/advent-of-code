/*
 * Advent of Code 2024
 * Day 20: Race Condition
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

class Day20 {
    private int[][][] map;
    private int startPosX;
    private int startPosY;
    private int endPosX;
    private int endPosY;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            map = new int[line.length()][line.length()][line.length()];
            for (int row = 0; fileReader.hasNextLine(); row++) {
                line = fileReader.nextLine();
                for (int col = 0; col < line.length(); col++) {
                    map[row][col] = new int[] {line.charAt(col) == '#' ? 1 : 0, 0, Integer.MAX_VALUE - 1}; // path, visited, dist
                    if (line.charAt(col) == 'S') {
                        startPosX = col;
                        startPosY = row;
                        map[row][col][2] = 0;
                    } else if (line.charAt(col) == 'E') {
                        endPosX = col;
                        endPosY = row;
                    }
                }
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateDistancesNoCheats() {
        int posX = startPosX;
        int posY = startPosY;
        while (posX != endPosX || posY != endPosY) {
            int prevPosX = posX;
            int prevPosY = posY;
            if (map[posY - 1][posX][0] == 0 && map[posY - 1][posX][1] != 1) {
                posY--; // up
            } else if (map[posY + 1][posX][0] == 0 && map[posY + 1][posX][1] != 1) {
                posY++; // down
            } else if (map[posY][posX - 1][0] == 0 && map[posY][posX - 1][1] != 1) {
                posX--; // left
            } else {
                posX++; // right
            }
            map[posY][posX][1] = 1;
            map[posY][posX][2] = map[prevPosY][prevPosX][2] + 1;
        }
    }

    private int findGoodCheats(int cheatDistance, int visited) {
        int numCheats = 0;
        int posX = startPosX;
        int posY = startPosY;
        while (posX != endPosX || posY != endPosY) {
            map[posY][posX][1] = visited;
            int checkPosY = posY - cheatDistance;
            while (checkPosY <= posY + cheatDistance) {
                if (checkPosY < 0) {
                    checkPosY = 0;
                    continue;
                }
                if (checkPosY >= map.length) {
                    break;
                }
                int checkPosX = posX - cheatDistance;
                while (checkPosX <= posX + cheatDistance) {
                    if (checkPosX < 0) {
                        checkPosX = 0;
                        continue;
                    }
                    if (checkPosX >= map.length) {
                        break;
                    }
                    int diff = Math.abs(posY - checkPosY) + Math.abs(posX - checkPosX);
                    if (diff <= cheatDistance && map[checkPosY][checkPosX][0] == 0
                            && map[checkPosY][checkPosX][2] - map[posY][posX][2] - diff >= 100) {
                        numCheats++;
                    }
                    checkPosX++;
                }
                checkPosY++;
            }
            if (map[posY - 1][posX][0] == 0 && map[posY - 1][posX][1] != visited) {
                posY--; // up
            } else if (map[posY + 1][posX][0] == 0 && map[posY + 1][posX][1] != visited) {
                posY++; // down
            } else if (map[posY][posX - 1][0] == 0 && map[posY][posX - 1][1] != visited) {
                posX--; // left
            } else {
                posX++; // right
            }
        }
        return numCheats;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day20 solution = new Day20();
        solution.getInput();
        solution.populateDistancesNoCheats();
        System.out.printf("Part one: %d\n", solution.findGoodCheats(2, 2));
        System.out.printf("Part two: %d\n", solution.findGoodCheats(20, 3));
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

