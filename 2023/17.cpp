/*
 * Advent of Code 2023
 * Day 17: Clumsy Crucible
 */

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <array>
#include <queue>
#include <set>

struct ArrayComparator {
    bool operator()(const std::array<int, 6> &a, const std::array<int, 6> &b) const {
        return a[0] > b[0];
    }
};

int main() {
    std::vector<std::string> inputMap;
    std::vector<std::string> tempMap;
    std::ifstream fileStream("input.txt");

    if (fileStream.is_open()) {
        int lineNum = 0;
        std::string line;
        while (fileStream) {
            std::getline(fileStream, line);
            if (line.length()) {
                inputMap.push_back(line);
                tempMap.push_back(line);
                lineNum++;
            }
        }
        fileStream.close();
    } else {
        std::cout << "Unable to open file" << std::endl;
        return 1;
    }

    std::set<std::array<int, 5>> checked;
    std::priority_queue<std::array<int, 6>, std::vector<std::array<int, 6>>, ArrayComparator> coordinate_queue;
    coordinate_queue.push({0, 0, 0, 0, 0, 0});

    while (coordinate_queue.size()) {
        int heatLoss = coordinate_queue.top()[0];
        int y = coordinate_queue.top()[1];
        int x = coordinate_queue.top()[2];
        int dy = coordinate_queue.top()[3];
        int dx = coordinate_queue.top()[4];
        int dist = coordinate_queue.top()[5];

        coordinate_queue.pop();

        if (checked.find({y, x, dy, dx, dist}) != checked.end()) {
            continue;
        }

        if (x == inputMap[0].length() - 1 && y == inputMap.size() - 1 && dist > 3) {
            std::cout << dist << std::endl;
            std::cout << "Minimum heat loss: " << heatLoss << std::endl;
            break;
        }

        checked.insert({y, x, dy, dx, dist});

        // continue in same direction
        if (dist < 10 && (dy != 0 || dx != 0)) {
            int nextY = y + dy;
            int nextX = x + dx;
            if (nextY >= 0 && nextY < inputMap.size() && nextX >= 0 && nextX < inputMap[nextY].size()) {
                coordinate_queue.push({heatLoss + (int)(inputMap[nextY][nextX] - '0'), nextY, nextX, dy, dx, dist + 1});
            }
        }

        if (dist > 3 || (dy == 0 && dx == 0)) {
            // change direction
            if (y > 0 && dy == 0) { // up
                coordinate_queue.push({heatLoss + (int)(inputMap[y - 1][x] - '0'), y - 1, x, -1, 0, 1});
            }
            if (y < inputMap.size() - 1 && dy == 0) { // down
                coordinate_queue.push({heatLoss + (int)(inputMap[y + 1][x] - '0'), y + 1, x, 1, 0, 1});
            }
            if (x > 0 && dx == 0) { // left
                coordinate_queue.push({heatLoss + (int)(inputMap[y][x - 1] - '0'), y, x - 1, 0, -1, 1});
            }
            if (x < inputMap[y].size() - 1 && dx == 0) { // right
                coordinate_queue.push({heatLoss + (int)(inputMap[y][x + 1] - '0'), y, x + 1, 0, 1, 1});
            }
        }
    }

    return 0;
}
