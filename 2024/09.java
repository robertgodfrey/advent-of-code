/*
 * Advent of Code 2024
 * Day 9: Disk Fragmenter
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

class Day09 {
    ArrayList<Integer> diskMap = new ArrayList<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String inputString = fileReader.nextLine();
                for (int i = 0; i < inputString.length(); i += 2) {
                    int id = i / 2;
                    int takenSpace = inputString.charAt(i) - '0';
                    int freeSpace = i + 1 < inputString.length() ? inputString.charAt(i + 1) - '0' : 0;
                    for (int j = 0; j < takenSpace; j++) {
                        diskMap.add(id);
                    }
                    for (int j = 0; j < freeSpace; j++) {
                        diskMap.add(-1);
                    }
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long calculateChecksum(ArrayList<Integer> diskMapCopy) {
        long checkSum = 0;
        for (int i = 0; i < diskMapCopy.size(); i++) {
            if (diskMapCopy.get(i) == -1) {
                continue;
            }
            checkSum += (long) i * diskMapCopy.get(i);
        }
        return checkSum;
    }

    private long partOne() {
        ArrayList<Integer> diskMapCopy = new ArrayList<>(diskMap);
        int frontPointer = 0;
        int rearPointer = diskMapCopy.size() - 1;
        while (true) {
            while (diskMapCopy.get(frontPointer) != -1) {
                frontPointer++;
            }
            while (diskMapCopy.get(rearPointer) == -1) {
                rearPointer--;
            }
            if (frontPointer > rearPointer) {
                break;
            }
            diskMapCopy.set(frontPointer, diskMapCopy.get(rearPointer));
            diskMapCopy.set(rearPointer, -1);
        }
        return calculateChecksum(diskMapCopy);
    }

    private long partTwo() {
        ArrayList<Integer> diskMapCopy = new ArrayList<>(diskMap);
        ArrayList<Integer[]> freeSpace = new ArrayList<>();
        ArrayList<Integer[]> files = new ArrayList<>();
        boolean foundFreeSpace = false;
        boolean foundFile = false;
        int startIndex = 0;
        int endIndex = 0;
        int fileId = diskMapCopy.get(diskMapCopy.size() - 1);
        for (int i = 0; i < diskMapCopy.size(); i++) {
            if (diskMapCopy.get(i) != -1) {
                if (foundFreeSpace) {
                    foundFreeSpace = false;
                    freeSpace.add(new Integer[] {startIndex, i - 1});
                }
            } else if (!foundFreeSpace) {
                foundFreeSpace = true;
                startIndex = i;
            }
        }
        for (int i = diskMapCopy.size() - 1; i >= 0; i--) {
            if (diskMapCopy.get(i) == -1) {
                if (foundFile) {
                    foundFile = false;
                    files.add(new Integer[] {i + 1, endIndex});
                }
            } else if (!foundFile) {
                foundFile = true;
                fileId = diskMapCopy.get(i);
                endIndex = i;
            } else if (fileId != diskMapCopy.get(i)) {
                fileId = diskMapCopy.get(i);
                files.add(new Integer[] {i + 1, endIndex});
                endIndex = i;
            }
        }
        for (Integer[] file : files) {
            for (Integer[] space : freeSpace) {
                if (file[0] < space[0]) {
                    break;
                }
                int fileSize = file[1] - file[0] + 1;
                int freeSpaceSize = space[1] - space[0] + 1;
                if (fileSize <= freeSpaceSize) {
                    for (int i = 0; i < fileSize; i++) {
                        diskMapCopy.set(space[0] + i, diskMapCopy.get(file[0] + i));
                        diskMapCopy.set(file[0] + i, -1);
                    }
                    space[0] += fileSize;
                    break;
                }
            }
        }
        return calculateChecksum(diskMapCopy);
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day09 solution = new Day09();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

