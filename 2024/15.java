/*
 * Advent of Code 2024
 * Day 15: Warehouse Woes
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Day15 {
    ArrayList<char[]> warehouse = new ArrayList<>();
    ArrayList<Character> robotMoves = new ArrayList<>();
    private int robotX = 0;
    private int robotY = 0;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            int robotY = 0;
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                while (line.length() != 0) {
                    warehouse.add(line.toCharArray());
                    if (line.contains("@")) {
                        this.robotY = robotY;
                        this.robotX = line.indexOf("@");
                    }
                    line = fileReader.hasNextLine() ? fileReader.nextLine() : "";
                    robotY++;
                }
                while (fileReader.hasNextLine()) {
                    String[] moves = fileReader.nextLine().split("");
                    for (String move : moves) {
                        robotMoves.add(move.charAt(0));
                    }
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void moveRobot(ArrayList<char[]> workingWarehouse, int xDir, int yDir) {
        if (workingWarehouse.get(robotY + yDir)[robotX + xDir] == '.') {
            workingWarehouse.get(robotY)[robotX] = '.';
            workingWarehouse.get(robotY + yDir)[robotX + xDir] = '@';
            robotX += xDir;
            robotY += yDir;
        } else if (workingWarehouse.get(robotY + yDir)[robotX + xDir] == 'O') {
            int endBoxPosX = robotX + xDir;
            int endBoxPosY = robotY + yDir;
            while (workingWarehouse.get(endBoxPosY)[endBoxPosX] == 'O') {
                endBoxPosX += xDir;
                endBoxPosY += yDir;
            }
            if (workingWarehouse.get(endBoxPosY)[endBoxPosX] == '.') {
                workingWarehouse.get(endBoxPosY)[endBoxPosX] = 'O';
                workingWarehouse.get(robotY)[robotX] = '.';
                workingWarehouse.get(robotY + yDir)[robotX + xDir] = '@';
                robotX += xDir;
                robotY += yDir;
            }
        }
    }

    private void moveBoxesRecursive(ArrayList<char[]> workingWarehouse, int leftSide, int row, int dir) {
        for (int i = leftSide - 1; i < leftSide + 2; i++) {
            if (workingWarehouse.get(row + dir)[i] == '[') {
                moveBoxesRecursive(workingWarehouse, i, row + dir, dir);
            }
        }
        workingWarehouse.get(row + dir)[leftSide] = '[';
        workingWarehouse.get(row + dir)[leftSide + 1] = ']';
        workingWarehouse.get(row)[leftSide] = '.';
        workingWarehouse.get(row)[leftSide + 1] = '.';
    }

    private void moveRobotHorizontalWide(ArrayList<char[]> workingWarehouse, int xDir, char startBox, char endBox) {
        if (workingWarehouse.get(robotY)[robotX + xDir] == '.') {
            workingWarehouse.get(robotY)[robotX] = '.';
            workingWarehouse.get(robotY)[robotX + xDir] = '@';
            robotX += xDir;
        } else if (workingWarehouse.get(robotY)[robotX + xDir] == startBox) {
            int startBoxPos = robotX + xDir;
            int endBoxPos = startBoxPos;
            while (workingWarehouse.get(robotY)[endBoxPos] == startBox || workingWarehouse.get(robotY)[endBoxPos] == endBox) {
                endBoxPos += xDir;
            }
            if (workingWarehouse.get(robotY)[endBoxPos] == '.') {
                for (int i = endBoxPos; xDir == 1 ? i > startBoxPos : i < startBoxPos; i -= xDir * 2) {
                    workingWarehouse.get(robotY)[i] = endBox;
                    workingWarehouse.get(robotY)[i - xDir] = startBox;
                }
                workingWarehouse.get(robotY)[robotX] = '.';
                workingWarehouse.get(robotY)[robotX + xDir] = '@';
                robotX += xDir;
            }
        }
    }

    private void moveRobotVerticalWide(ArrayList<char[]> workingWarehouse, int dirY) {
        if (workingWarehouse.get(robotY + dirY)[robotX] == '.') {
            workingWarehouse.get(robotY)[robotX] = '.';
            workingWarehouse.get(robotY + dirY)[robotX] = '@';
            robotY += dirY;
        } else if (workingWarehouse.get(robotY + dirY)[robotX] == '[' || workingWarehouse.get(robotY + dirY)[robotX] == ']') {
            final int leftSideBox = workingWarehouse.get(robotY + dirY)[robotX] == '[' ? robotX : robotX - 1;
            final int rightSideBox = workingWarehouse.get(robotY + dirY)[robotX] == ']' ? robotX : robotX + 1;
            ArrayList<Integer> rowBoxIndices = new ArrayList<>();
            rowBoxIndices.add(leftSideBox);
            rowBoxIndices.add(rightSideBox);
            int currentRow = robotY + dirY;
            int rowLeftBound = leftSideBox;
            int rowRightBound = rightSideBox;
            boolean foundBoxEnd = false;
            boolean ableToPush = true;
            while (!foundBoxEnd) {
                ArrayList<Integer> thisRowBoxIndices = new ArrayList<>();
                for (int i = rowLeftBound - 1; i < rowRightBound + 2; i++) {
                    if ((workingWarehouse.get(currentRow + dirY)[i] == ']' && i >= rowLeftBound)
                            || (workingWarehouse.get(currentRow + dirY)[i] == '[' && i <= rowRightBound)) {
                        thisRowBoxIndices.add(i);
                    }
                }
                for (int i = rowLeftBound; i < rowRightBound + 1; i++) {
                    if (workingWarehouse.get(currentRow + dirY)[i] == '#' && rowBoxIndices.contains(i)) {
                        ableToPush = false;
                        foundBoxEnd = true;
                        break;
                    }
                }
                if (thisRowBoxIndices.size() == 0) {
                    foundBoxEnd = true;
                } else {
                    rowBoxIndices = new ArrayList<>(thisRowBoxIndices);
                    rowLeftBound = rowBoxIndices.get(0);
                    rowRightBound = rowBoxIndices.get(rowBoxIndices.size() - 1);
                    currentRow += dirY;
                }
            }
            if (ableToPush) {
                moveBoxesRecursive(workingWarehouse, leftSideBox, robotY + dirY, dirY);
                workingWarehouse.get(robotY + dirY)[robotX] = '@';
                workingWarehouse.get(robotY)[robotX] = '.';
                robotY += dirY;
            }
        }
    }

    private int partOne() {
        ArrayList<char[]> warehouseCopy = new ArrayList<>();
        for (char[] line : warehouse) {
            warehouseCopy.add(Arrays.copyOf(line, line.length));
        }
        int gpsSum = 0;
        for (Character move : robotMoves) {
            switch (move) {
                case 94 -> moveRobot(warehouseCopy, 0, -1); // up
                case 118 -> moveRobot(warehouseCopy, 0, 1); // down
                case 60 -> moveRobot(warehouseCopy, -1, 0); // left
                default -> moveRobot(warehouseCopy, 1, 0);  // right
            }
        }
        for (int i = 0; i < warehouseCopy.size(); i++) {
            for (int j = 0; j < warehouseCopy.get(0).length; j++) {
                if (warehouseCopy.get(i)[j] == 'O') {
                    gpsSum += i * 100 + j;
                }
            }
        }
        return gpsSum;
    }

    private void widenWarehouse(ArrayList<char[]> wideWarehouse) {
        for (int i = 0; i < warehouse.size(); i++) {
            wideWarehouse.add(new char[warehouse.get(0).length * 2]);
            for (int j = 0; j < warehouse.get(0).length * 2; j += 2) {
                if (warehouse.get(i)[j / 2] == 'O') {
                    wideWarehouse.get(i)[j] = '[';
                    wideWarehouse.get(i)[j + 1] = ']';
                } else if (warehouse.get(i)[j / 2] == '@') {
                    robotX = j;
                    robotY = i;
                    wideWarehouse.get(i)[j] = '@';
                    wideWarehouse.get(i)[j + 1] = '.';
                } else {
                    wideWarehouse.get(i)[j] = warehouse.get(i)[j / 2];
                    wideWarehouse.get(i)[j + 1] = warehouse.get(i)[j / 2];
                }
            }
        }
    }

    private int partTwo() {
        ArrayList<char[]> wideWarehouse = new ArrayList<>();
        int gpsSum = 0;
        widenWarehouse(wideWarehouse);
        for (Character move : robotMoves) {
            switch (move) {
                case 94 -> moveRobotVerticalWide(wideWarehouse, -1); // up
                case 118 -> moveRobotVerticalWide(wideWarehouse, 1); // down
                case 60 -> moveRobotHorizontalWide(wideWarehouse, -1, ']', '['); // left
                default -> moveRobotHorizontalWide(wideWarehouse, 1, '[', ']');  // right
            }
        }
        for (int i = 0; i < wideWarehouse.size(); i++) {
            for (int j = 0; j < wideWarehouse.get(0).length; j++) {
                if (wideWarehouse.get(i)[j] == '[') {
                    gpsSum += i * 100 + j;
                }
            }
        }
        return gpsSum;
    }

    public static void main(String[] args) {
        Day15 solution = new Day15();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
    }
}

