/*
 * Advent of Code 2024
 * Day 17: Chronospatial Computer
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

class Day17 {
    private final ArrayList<Integer> inputProgram = new ArrayList<>();
    private long regA, regB, regC;

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            regA = Long.parseLong(fileReader.nextLine().split(": ")[1]);
            regB = Long.parseLong(fileReader.nextLine().split(": ")[1]);
            regC = Long.parseLong(fileReader.nextLine().split(": ")[1]);
            fileReader.nextLine();
            for (String instruction : fileReader.nextLine().split(": ")[1].split(",")) {
                inputProgram.add(Integer.parseInt(instruction));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return;
    }

    private long comboOperand(int operand) {
        if (operand < 4) {
            return operand;
        }
        return operand == 4 ? regA : operand == 5 ? regB : regC;
    }

    private ArrayList<Long> partOne() {
        ArrayList<Long> systemOutput = new ArrayList<>();
        Set<String> seenInstructions = new HashSet<>();
        for (int i = 0; i < inputProgram.size(); i += 2) {
            int instruction = inputProgram.get(i);
            int operand = inputProgram.get(i + 1);
            if (seenInstructions.contains(String.format("%d-%d,%d:%d,%d,%d", i, instruction, operand, regA, regB, regC))) {
                return new ArrayList<>();
            }
            seenInstructions.add(String.format("%d-%d,%d:%d,%d,%d", i, instruction, operand, regA, regB, regC));
            switch (instruction) {
                case 0 -> regA = (long) (regA / Math.pow(2, comboOperand(operand)));
                case 1 -> regB = regB ^ operand;
                case 2 -> regB = comboOperand(operand) % 8;
                case 3 -> i = regA == 0 ? i : operand - 2;
                case 4 -> regB = regB ^ regC;
                case 5 -> systemOutput.add(comboOperand(operand) % 8);
                case 6 -> regB = (long) (regA / Math.pow(2, comboOperand(operand)));
                case 7 -> regC = (long) (regA / Math.pow(2, comboOperand(operand)));
            }
        }
        return systemOutput;
    }

    private long partTwo() {
        for (long i = 136904920089230L; i < 136904920189230L; i+=1) {
            regA = i; // * sad bruteforce noises *
            regB = 0;
            regC = 0;
            System.out.printf("%d: %s\n", i, partOne().stream().map(Object::toString).collect(Collectors.joining(",")));
        }
        return 1;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day17 solution = new Day17();
        solution.getInput();
        System.out.printf("Part one: %s\n", solution.partOne().stream().map(Object::toString).collect(Collectors.joining(",")));
        System.out.printf("Part two: %s\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

