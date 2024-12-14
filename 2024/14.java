/*
 * Advent of Code 2024
 * Day 14: Restroom Redoubt
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Day14 {
    ArrayList<Integer[]> robots = new ArrayList<>();
    private final int MAP_WIDTH = 101;
    private final int MAP_HEIGHT = 103;


    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String nextLine = fileReader.nextLine();
                Integer[] robot = new Integer[4];
                String[] chunks = nextLine.split(",");
                robot[0] = Integer.parseInt(chunks[0].substring(chunks[0].indexOf('=') + 1));
                robot[1] = Integer.parseInt(chunks[1].substring(0, chunks[1].indexOf('v') - 1));
                robot[2] = Integer.parseInt(chunks[1].substring(chunks[1].indexOf('=') + 1));
                robot[3] = Integer.parseInt(chunks[2].substring(chunks[2].indexOf(',') + 1));
                robots.add(robot);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateRobots() {
        for (Integer[] robot : robots) {
            int newXPos = robot[0] + robot[2];
            int newYPos = robot[1] + robot[3];
            robot[0] = newXPos < 0 ? MAP_WIDTH + newXPos : newXPos < MAP_WIDTH ? newXPos :  newXPos - MAP_WIDTH;
            robot[1] = newYPos < 0 ? MAP_HEIGHT + newYPos : newYPos < MAP_HEIGHT ? newYPos : newYPos - MAP_HEIGHT;
        }
    }

    private long partOne() {
        for (int i = 0; i < 100; i++) {
            updateRobots();
        }
        int horizontalCenter = MAP_HEIGHT / 2;
        int verticalCenter = MAP_WIDTH / 2;
        int upperLeftQuadrant = 0;
        int upperRightQuadrant = 0;
        int lowerLeftQuadrant = 0;
        int lowerRightQuadrant = 0;
        for (Integer[] robot : robots) {
            if (robot[0] == verticalCenter || robot[1] == horizontalCenter) {
                continue;
            }
            if (robot[0] < verticalCenter) {
                if (robot[1] < horizontalCenter) {
                    upperLeftQuadrant++;
                } else {
                    lowerLeftQuadrant++;
                }
            } else {
                if (robot[1] < horizontalCenter) {
                    upperRightQuadrant++;
                } else {
                    lowerRightQuadrant++;
                }
            }
        }
        return (long) upperRightQuadrant * upperLeftQuadrant * lowerRightQuadrant * lowerLeftQuadrant;
    }

    private void partTwo() {
        Integer[][] updatedMap = new Integer[MAP_HEIGHT][MAP_WIDTH];
        int presses = 100;
        for (int pass = 0; pass < 56; pass++) {
            updateRobots();
            presses++;
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            for (int pass = 0; pass < 101; pass++) {
                updateRobots();
                for (int i = 0; i < MAP_HEIGHT; i++) {
                    for (int j = 0; j < MAP_WIDTH; j++) {
                        updatedMap[i][j] = 0;
                    }
                }
                for (Integer[] robot : robots) {
                    updatedMap[robot[1]][robot[0]]++;
                }
                presses++;
            }
            for (Integer[] line : updatedMap) {
                for (Integer point : line) {
                    System.out.print(point > 0 ? point : ".");
                }
                System.out.println();
            }
            System.out.println(presses);
        }
    }

    public static void main(String[] args) {
        Day14 solution = new Day14();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        solution.partTwo();
    }
}

