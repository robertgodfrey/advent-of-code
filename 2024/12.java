/*
 * Advent of Code 2024
 * Day 12: Garden Groups
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Day12 {
    ArrayList<String> farm = new ArrayList<>();
    ArrayList<Set<String>> regions = new ArrayList<>();
    Set<String> visitedPlots = new HashSet<>();

    private void getInput() {
        try {
            Scanner fileReader = new Scanner(new File("input.txt"));
            while (fileReader.hasNextLine()) {
                farm.add(fileReader.nextLine());
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkAndSearch(int row, int col, char plantId, int regionIndex) {
        if (visitedPlots.contains(String.format("%d,%d", col, row))) {
            return;
        }
        if (row >= 0 && row < farm.size() && col >= 0 && col < farm.get(0).length() // in bounds
                && farm.get(row).charAt(col) == plantId) {
            regions.get(regionIndex).add(String.format("%d,%d", col, row));
            bfs(row, col, plantId, regionIndex);
        }
    }

    private void bfs(int row, int col, char plantId, int regionIndex) {
        String currentPos = String.format("%d,%d", col, row);
        if (visitedPlots.contains(currentPos)) {
            return;
        }
        visitedPlots.add(currentPos);
        checkAndSearch(row - 1, col, plantId, regionIndex); // up
        checkAndSearch(row + 1, col, plantId, regionIndex); // down
        checkAndSearch(row, col - 1, plantId, regionIndex); // left
        checkAndSearch(row, col + 1, plantId, regionIndex); // right
    }

    private void getRegions() {
        int regionIndex = -1;
        for (int row = 0; row < farm.size(); row++) {
            for (int col = 0; col < farm.get(0).length(); col++) {
                if (visitedPlots.contains(String.format("%d,%d", col, row))) {
                    continue;
                }
                regions.add(new HashSet<>());
                regionIndex++;
                regions.get(regionIndex).add(String.format("%d,%d", col, row));
                bfs(row, col, farm.get(row).charAt(col), regionIndex);
            }
        }
    }

    private boolean neighborInSet(int row, int col, Set<String> region) {
        return region.contains(String.format("%d,%d", col, row));
    }

    private int countPerimeter(Set<String> region) {
        int perimeter = 0;
        for (String plot : region) {
            int row = Integer.parseInt(plot.split(",")[1]);
            int col = Integer.parseInt(plot.split(",")[0]);
            int numNeighbors = 0;
            // check existence of neighbors
            numNeighbors += neighborInSet(row - 1, col, region) ? 1 : 0; // up
            numNeighbors += neighborInSet(row + 1, col, region) ? 1 : 0; // down
            numNeighbors += neighborInSet(row, col - 1, region) ? 1 : 0; // left
            numNeighbors += neighborInSet(row, col + 1, region) ? 1 : 0; // right
            perimeter += 4 - numNeighbors;
        }
        return perimeter;
    }

    private int countSides(Set<String> region) {
        int corners = 0;
        for (String plot : region) {
            int row = Integer.parseInt(plot.split(",")[1]);
            int col = Integer.parseInt(plot.split(",")[0]);
            boolean topNeighbor = neighborInSet(row - 1, col, region);
            boolean bottomNeighbor = neighborInSet(row + 1, col, region);
            boolean leftNeighbor = neighborInSet(row, col - 1, region);
            boolean rightNeighbor = neighborInSet(row, col + 1, region);
            boolean topLeftNeighbor = neighborInSet(row - 1, col - 1, region);
            boolean topRightNeighbor = neighborInSet(row - 1, col + 1, region);
            boolean bottomLeftNeighbor = neighborInSet(row + 1, col - 1, region);
            boolean bottomRightNeighbor = neighborInSet(row + 1, col + 1, region);
            if (topNeighbor) {
                if (!bottomNeighbor) {
                    if ((leftNeighbor && !rightNeighbor) || (rightNeighbor && !leftNeighbor)) {
                        corners++;
                    } else if (!leftNeighbor) {
                        corners += 2;
                    }
                }
            } else if (bottomNeighbor) {
                if ((leftNeighbor && !rightNeighbor) || (rightNeighbor && !leftNeighbor)) {
                    corners++;
                } else if (!leftNeighbor) {
                    corners += 2;
                }
            } else if (leftNeighbor) {
                if (!rightNeighbor) {
                    corners += 2;
                }
            } else if (rightNeighbor) {
                corners += 2;
            } else {
                corners += 4;
            }
            if (topNeighbor && leftNeighbor && !topLeftNeighbor) {
                corners++;
            }
            if (topNeighbor && rightNeighbor && !topRightNeighbor) {
                corners++;
            }
            if (bottomNeighbor && leftNeighbor && !bottomLeftNeighbor) {
                corners++;
            }
            if (bottomNeighbor && rightNeighbor && !bottomRightNeighbor) {
                corners++;
            }
        }
        return corners;
    }

    private int partOne() {
        int totalCost = 0;
        for (Set<String> region : regions) {
            totalCost += countPerimeter(region) * region.size();
        }
        return totalCost;
    }

    private int partTwo() {
        int discountCost = 0;
        for (Set<String> region : regions) {
            discountCost += countSides(region) * region.size();
        }
        return discountCost;
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Day12 solution = new Day12();
        solution.getInput();
        solution.getRegions();
        System.out.printf("Part one: %d\n", solution.partOne());
        System.out.printf("Part two: %d\n", solution.partTwo());
        System.out.printf("Execution time: %d milliseconds", Duration.between(start, Instant.now()).toMillis());
    }
}

