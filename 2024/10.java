/*
 * Advent of Code 2024
 * Day 10: Hoof It
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Day10 {
    ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    Set<Integer[]> trailheads = new HashSet<>();
    Set<String> reachableSummits = new HashSet<>();
    Set<String> distinctTrails = new HashSet<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            for (int row = 0; fileReader.hasNextLine(); row++) {
                String inputString = fileReader.nextLine();
                map.add(new ArrayList<>());
                for (int col = 0; col < inputString.length(); col++) {
                    map.get(row).add(inputString.charAt(col) - '0');
                    if (inputString.charAt(col) - '0' == 0) {
                        trailheads.add(new Integer[] {col, row});
                    }
                }
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isInBounds(int posX, int posY) {
        return posX >= 0 && posX < map.get(0).size() && posY >= 0 && posY < map.size();
    }

    private void checkAndCall(int posX, int posY, int elevation, ArrayList<Integer[]> path) {
        if (isInBounds(posX, posY) && map.get(posY).get(posX) == elevation) {
            ArrayList<Integer[]> tempPath = new ArrayList<>(path);
            tempPath.add(new Integer[]{posX, posY});
            dfs(posX, posY, elevation, tempPath);
        }
    }

    private void dfs(int posX, int posY, int elevation, ArrayList<Integer[]> path) {
        if (elevation == 9) {
            reachableSummits.add(Arrays.toString(new int[] {posX, posY}));
            distinctTrails.add(path.toString());
            return;
        }
        checkAndCall(posX, posY - 1, elevation + 1, path); // up
        checkAndCall(posX, posY + 1, elevation + 1, path); // down
        checkAndCall(posX - 1, posY, elevation + 1, path); // left
        checkAndCall(posX + 1, posY, elevation + 1, path); // right
    }

    private void solve() {
        int paths = 0;
        int score = 0;
        for (Integer[] trailhead: trailheads) {
            dfs(trailhead[0], trailhead[1], 0, new ArrayList<>());
            paths += reachableSummits.size();
            score += distinctTrails.size();
            reachableSummits.clear();
            distinctTrails.clear();
        }
        System.out.printf("Part one: %d\n", paths);
        System.out.printf("Part two: %d\n", score);
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day10 solution = new Day10();
        solution.getInput();
        solution.solve();
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

