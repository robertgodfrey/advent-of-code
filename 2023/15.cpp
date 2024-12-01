/*
 * Advent of Code 2023
 * Day 15: Lens Library
 */

#include <iostream>
#include <fstream>
#include <string>
#include <unordered_map>
#include <vector>

int hashLabel(std::string label) {
    int boxNum = 0;
    for (char &c : label) {
        boxNum = ((boxNum + (int)c) * 17 ) % 256;
    }
    return boxNum;
}

int main() {
    std::unordered_map<int, std::vector<std::string>> boxes;
    std::ifstream fileStream("input.txt");

    if (fileStream.is_open()) {
        std::string label = "";
        std::string instruction = "";
        char c;

        while (fileStream) {
            fileStream.get(c);
            if ((int)c >= 97 && (int)c <= 122) {
                label += c;
            } else if (c != ',' && c != '\n') {
                instruction += c;
            } else {
                int index = hashLabel(label);
                if (instruction.length() <= 1) {
                    for (int i = 0; i < boxes[index].size(); i++) {
                        if (boxes[index][i].substr(0, boxes[index][i].length() - 2) == label) {
                            boxes[index].erase(boxes[index].begin() + i);
                        }
                    }
                } else if (instruction.length() > 1) {
                    bool found = false;
                    std::string lens = label + " " + instruction.substr(1);
                    for (int i = 0; i < boxes[index].size(); i++) {
                        if (boxes[index][i].substr(0, boxes[index][i].length() - 2) == label) {
                            boxes[index].at(i) = lens;
                            found = true;
                        }
                    }
                    if (!found) {
                        boxes[index].push_back(lens);
                    }
                }
                label = "";
                instruction = "";
            }
        }

        int sum = 0;

        for (auto x : boxes) {
            for (int i = 0; i < x.second.size(); i++) {
                sum += (x.first + 1) * (i + 1) * std::stoi(x.second[i].substr(x.second[i].length() - 2));
            }
        }
        std::cout << "Sum: " << sum << std::endl;
        return 0;
    }
    return 1;
}
