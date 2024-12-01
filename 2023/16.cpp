/*
 * Advent of Code 2023
 * Day 16: The Floor Will Be Lava
 */

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <unordered_set>

void findPath(std::vector<std::string> &inputGrid,
              std::vector<std::string> &energizedGrid,
              std::unordered_set<std::string> &visitedDirs,
              int x, int y, char direction) {
    while (true) {
        if (x < 0 || y < 0 || x >= inputGrid[0].length() || y >= inputGrid.size()) {
            return;
        }
        if (inputGrid[y][x] == '|' || inputGrid[y][x] == '-') {
            std::string visitedCheck = std::to_string(x) + ":" + std::to_string(y) + ":" + direction;
            if (visitedDirs.find(visitedCheck) != visitedDirs.end()) {
                break;
            }
            visitedDirs.insert(visitedCheck);
        }
        energizedGrid[y][x] = '#';
        switch (direction) {
            case 'r':
                if (inputGrid[y][x] == '/') {
                    direction = 'u';
                    y--;
                } else if (inputGrid[y][x] == '\\') {
                    direction = 'd';
                    y++;
                } else if (inputGrid[y][x] == '|') {
                    findPath(inputGrid, energizedGrid, visitedDirs, x, y - 1, 'u');
                    findPath(inputGrid, energizedGrid, visitedDirs, x, y + 1, 'd');
                } else {
                    x++;
                }
                break;
            case 'l':
                if (inputGrid[y][x] == '/') {
                    direction = 'd';
                    y++;
                } else if (inputGrid[y][x] == '\\') {
                    direction = 'u';
                    y--;
                } else if (inputGrid[y][x] == '|') {
                    findPath(inputGrid, energizedGrid, visitedDirs, x, y - 1, 'u');
                    findPath(inputGrid, energizedGrid, visitedDirs, x, y + 1, 'd');
                } else {
                    x--;
                }
                break;
            case 'u':
                if (inputGrid[y][x] == '/') {
                    direction = 'r';
                    x++;
                } else if (inputGrid[y][x] == '\\') {
                    direction = 'l';
                    x--;
                } else if (inputGrid[y][x] == '-') {
                    findPath(inputGrid, energizedGrid, visitedDirs, x - 1, y, 'l');
                    findPath(inputGrid, energizedGrid, visitedDirs, x + 1, y, 'r');
                } else {
                    y--;
                }
                break;
            case 'd':
                if (inputGrid[y][x] == '/') {
                    direction = 'l';
                    x--;
                } else if (inputGrid[y][x] == '\\') {
                    direction = 'r';
                    x++;
                } else if (inputGrid[y][x] == '-') {
                    findPath(inputGrid, energizedGrid, visitedDirs, x - 1, y, 'l');
                    findPath(inputGrid, energizedGrid, visitedDirs, x + 1, y, 'r');
                } else {
                    y++;
                }
                break;
        }
    }
}

int main() {
    std::vector<std::string> inputGrid;
    std::ifstream fileStream("input.txt");
    int maxEnergized = 0;

    if (fileStream.is_open()) {
        std::string line;
        while (fileStream) {
            std::getline(fileStream, line);
            inputGrid.push_back(line);
        }
        fileStream.close();
    } else {
        std::cout << "Unable to open file" << std::endl;
        return 1;
    }
    for (int side = 0; side < 4; side++) {
        int gridLength = inputGrid[0].length();
        int x = 0, y = 0;
        char direction;
        switch (side) {
            case 0:
                direction = 'd';
                break;
            case 1:
                y = gridLength - 1;
                direction = 'u';
                break;
            case 2:
                direction = 'r';
                break;
            case 3:
                x = gridLength - 1;
                direction = 'l';
                break;
        }
        for (int i = (side % 2 == 0 ? 0 : gridLength); (side % 2 == 0 ? i < gridLength : i > 0); i += (side % 2 == 0 ? 1 : -1)) {
            std::vector<std::string> tempEnergizedGrid;
            std::unordered_set<std::string> visitedDirs;
            int energizedSum = 0;

            for (auto line : inputGrid) {
                tempEnergizedGrid.push_back(std::string(line.length(), '.'));
            }

            findPath(inputGrid, tempEnergizedGrid, visitedDirs, x, y, direction);

            for (auto line : tempEnergizedGrid) {
                for (char c : line) {
                    if (c == '#') {
                        energizedSum++;
                    }
                }
            }
            if (energizedSum > maxEnergized) {
                maxEnergized = energizedSum;
            }
            if (direction == 'r' || direction == 'l') {
                y = i;
            } else {
                x = i;
            }
        }
    }
    std::cout << "Max energized sum: " << maxEnergized << std::endl;
    return 0;
}
