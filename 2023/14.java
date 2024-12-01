/*
 * Advent of Code 2023
 * Day 14: Parabolic Reflector Dish
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

class Platform {
    private final ArrayList<ArrayList<Character>> platform;
    private int size;

    public Platform() {
        this.platform = new ArrayList<ArrayList<Character>>();
        this.size = 0;
    }

    public int loadData() {
        try {
            File fileInput = new File("input.txt");
            Scanner fileReader = new Scanner(fileInput);
            int row = 0;
            while (fileReader.hasNextLine()) {
                this.platform.add(new ArrayList<Character>());
                String data = fileReader.nextLine();
                for (int col = 0; col < data.length(); col++) {
                    this.platform.get(row).add(data.charAt(col));
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
        this.size = this.platform.size();
        return 0;
    }

    public void printPlatform() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(this.platform.get(i).get(j) + " ");
            }
            System.out.print('\n');
        }
        System.out.println();
    }

    public String dumpString() {
        String dump = "";
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size - 1; j++) {
                dump += this.platform.get(i).get(j);
            }
            dump += this.platform.get(i).get(this.size - 1);
        }
        return dump;
    }

    public void tilt(char direction) {
        // ride of the ternaries
        boolean northSouth = direction == 'n' || direction == 's';
        boolean decreasing = direction == 'n' || direction == 'e';

        for (int i = decreasing ? 0 : this.size - 1; decreasing ? i < this.size : i >= 0; i += (decreasing ? 1 : -1)) {
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            int oCount = 0;
            int startIndex = decreasing ? 0 : this.size - 1;

            for (int j = decreasing ? 0 : this.size - 1; decreasing ? j < this.size : j >= 0; j += (decreasing ? 1 : -1)) {
                if (this.platform.get(northSouth ? j : i).get(northSouth ? i : j) == '#') {
                    if (oCount > 0) {
                        map.put(startIndex, oCount);
                    }
                    oCount = 0;
                    startIndex = j + (decreasing ? 1 : -1);
                } else if (this.platform.get(northSouth ? j : i).get(northSouth ? i : j) == 'O') {
                    this.platform.get(northSouth ? j : i).set(northSouth ? i : j, '.');
                    oCount++;
                }
            }
            if (oCount > 0) {
                map.put(startIndex, oCount);
            }
            for (int key : map.keySet()) {
                int tempKey = key;
                while (this.platform.get(northSouth ? tempKey : i).get(northSouth ? i : tempKey) == '#') {
                    tempKey += (decreasing ? 1 : -1);
                }
                for (int j = tempKey; decreasing ? j < key + map.get(key) : j > key - map.get(key); j += (decreasing ? 1 : -1)) {
                    this.platform.get(northSouth ? j : i).set(northSouth ? i : j, 'O');
                }
            }
        }
    }

    public void cycle() {
        this.tilt('n');
        this.tilt('e');
        this.tilt('s');
        this.tilt('w');
    }

    public int getTotalLoad(String dumpString) {
        int sum = 0;
        int multiple = this.size;
        for (int i = 0; i < this.size * this.size; i++) {
            if (dumpString.charAt(i) == 'O') {
                sum += multiple;
            }
            if ((i + 1) % this.size == 0 && i + 1 >= this.size) {
                multiple--;
            }
        }
        return sum;
    }
}


class Day14 {
    public static void main(String[] args) {
        Platform platform = new Platform();
        ArrayList<String> dumps = new ArrayList<String>();

        if (platform.loadData() == 1) {
            return;
        }

        int indexOfInterest = 0;

        for (int i = 0; i < 100000; i++) {
            platform.cycle();
            String dump = platform.dumpString();
            if (dumps.contains(dump)) {
                int startIndex = dumps.indexOf(dump);
                indexOfInterest = (1000000000 - startIndex) % (i - startIndex) + startIndex - 1;
                break;
            }
            dumps.add(dump);
        }

        System.out.println("Total load: " + platform.getTotalLoad(dumps.get(indexOfInterest)));
    }
}
