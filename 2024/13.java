/*
 * Advent of Code 2024
 * Day 13: Claw Contraption
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

class Day13 {
    ArrayList<Integer[]> machines = new ArrayList<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                String nextLine = fileReader.nextLine();
                String[] split = nextLine.split(",");
                int buttonAX = Integer.parseInt(split[0].substring(split[0].indexOf('+')));
                int buttonAY = Integer.parseInt(split[1].substring(split[1].indexOf('+')));
                nextLine = fileReader.nextLine();
                split = nextLine.split(",");
                int buttonBX = Integer.parseInt(split[0].substring(split[0].indexOf('+')));
                int buttonBY = Integer.parseInt(split[1].substring(split[1].indexOf('+')));
                nextLine = fileReader.nextLine();
                split = nextLine.split(",");
                int prizeX = Integer.parseInt(split[0].substring(split[0].indexOf('=') + 1));
                int prizeY = Integer.parseInt(split[1].substring(split[1].indexOf('=') + 1));
                Integer[] machine = new Integer[] {prizeX, prizeY, buttonAX, buttonAY, buttonBX, buttonBY};
                machines.add(machine);
                if (fileReader.hasNextLine()) {
                    fileReader.nextLine();
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private long solve(String offset) {
        long totalCost = 0;
        for (Integer[] machine : machines) {
            BigDecimal prizeX = new BigDecimal(String.valueOf(machine[0]));
            BigDecimal prizeY = new BigDecimal(String.valueOf(machine[1]));
            prizeX = prizeX.add(new BigDecimal(offset));
            prizeY = prizeY.add(new BigDecimal(offset));
            BigDecimal buttonAX = new BigDecimal(String.valueOf(machine[2]));
            BigDecimal buttonAY = new BigDecimal(String.valueOf(machine[3]));
            BigDecimal buttonBX = new BigDecimal(String.valueOf(machine[4]));
            BigDecimal buttonBY = new BigDecimal(String.valueOf(machine[5]));
            BigInteger buttonAPresses = prizeY.divide(buttonBY, MathContext.DECIMAL128)
                    .subtract(prizeX.divide(buttonBX, MathContext.DECIMAL128))
                    .divide(buttonAY.divide(buttonBY, MathContext.DECIMAL128)
                            .subtract(buttonAX.divide(buttonBX, MathContext.DECIMAL128)), MathContext.DECIMAL128)
                    .setScale(0, RoundingMode.HALF_UP).toBigInteger();
            BigInteger buttonBPresses = prizeY.divide(buttonAY, MathContext.DECIMAL128)
                    .subtract(prizeX.divide(buttonAX, MathContext.DECIMAL128))
                    .divide(buttonBY.divide(buttonAY, MathContext.DECIMAL128)
                            .subtract(buttonBX.divide(buttonAX, MathContext.DECIMAL128)), MathContext.DECIMAL128)
                    .setScale(0, RoundingMode.HALF_UP).toBigInteger();
            if (buttonAPresses.multiply(buttonAX.toBigInteger()).add(buttonBPresses
                    .multiply(buttonBX.toBigInteger())).equals(prizeX.toBigInteger())
                    && buttonAPresses.multiply(buttonAY.toBigInteger()).add(buttonBPresses
                    .multiply(buttonBY.toBigInteger())).equals(prizeY.toBigInteger())) {
                totalCost += buttonAPresses.longValue() * 3 + buttonBPresses.longValue();
            }
        }
        return totalCost;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day13 solution = new Day13();
        solution.getInput();
        System.out.printf("Part one: %d\n", solution.solve("0"));
        System.out.printf("Part two: %d\n", solution.solve("10000000000000"));
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

