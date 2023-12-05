/*
 * Advent of Code 2023
 * Day 5: If You Give A Seed A Fertilizer
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


class Day05 {
    public static void main(String[] args) {
        ArrayList<Long> seedNums = new ArrayList<Long>();
        ArrayList<ArrayList<Long>> maps = new ArrayList<ArrayList<Long>>(7);
        Long lowestLocationNumber = Long.MAX_VALUE;

        try {
            File fileInput = new File("input.txt");
            Scanner fileReader = new Scanner(fileInput);
            int index = -1;
            while (fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                if (seedNums.size() == 0) {
                    for (String num : data.substring(7, data.length()).split(" ")) {
                        seedNums.add(Long.parseLong(num));
                    }
                } else if (data.length() == 0) {
                    continue;
                } else if (Character.isDigit(data.charAt(0))) {
                    for (String num : data.split(" ")) {
                        maps.get(index).add(Long.parseLong(num));
                    }
                } else {
                    maps.add(new ArrayList<Long>());
                    index++;
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < seedNums.size(); i += 2) {
            final Long seedNum = seedNums.get(i);
            final Long numToIncrement = seedNums.get(i + 1);
            Long tempNum = seedNum;
            System.out.println("Seed Number: " + seedNum + ", incrementing " + numToIncrement + " times...");

            for (Long l = new Long(0); l < numToIncrement; l++) {
                tempNum = seedNum + l;
                for (int j = 0; j < 7; j++) {
                    ArrayList<Long> currentMap = maps.get(j);
                    for (int k = 0; k < currentMap.size(); k += 3) {
                        if (tempNum >= currentMap.get(k + 1) && tempNum <= currentMap.get(k + 1) + currentMap.get(k + 2) - 1) {
                            tempNum = tempNum + currentMap.get(k) - currentMap.get(k + 1);
                            break;
                        }
                    }
                }
                if (tempNum < lowestLocationNumber) {
                    lowestLocationNumber = tempNum;
                }
            }
        }
        System.out.println("Lowest location number: " + lowestLocationNumber);
    }
}
