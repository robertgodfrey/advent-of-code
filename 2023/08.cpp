/*
 * Advent of Code 2023
 * Day 8: Haunted Wasteland
 */

#include <iostream>
#include <fstream>
#include <string>
#include <unordered_map>


unsigned long gcd(unsigned long a, unsigned long b) {
    if (!b) {
        return a;
    }
    return gcd(b, a % b);
}


unsigned long lcm(int arr[], int n) {
    unsigned long ans = arr[0];
    for (int i = 1; i < n; i++) {
        ans = (((arr[i] * ans)) / gcd(arr[i], ans));
    }
    return ans;
}


int main() {
    std::string directions;
    std::unordered_map<std::string, std::string> locations;
    std::string aLocations = "";

    std::ifstream fileStream ("input.txt");

    if (fileStream.is_open()) {
        std::getline(fileStream, directions);
        std::string input;

        while (fileStream) {
            std::getline(fileStream, input);
            if (input.size()) {
                locations[input.substr(0, 3)] = input.substr(7, 3) + input.substr(12, 3);
                if (input[2] == 'A') {
                    aLocations += input.substr(0, 3);
                }
            }
        }
    } else {
        std::cout << "Error opening file" << std::endl;
    }

    int directionIndex;
    int *steps = new int[(int)(aLocations.size() / 3)];

    for (int i = 0; i < aLocations.size(); i += 3) {
        std::string currentLocation = aLocations.substr(i, 3);
        int currentIndex = (int)(i / 3);
        bool arrived = false;

        steps[currentIndex] = 0;
        directionIndex = 0;

        while (!arrived) {
            steps[currentIndex]++;

            if (directions[directionIndex] == 'L') {
                currentLocation = locations.at(currentLocation).substr(0, 3);
            } else {
                currentLocation = locations.at(currentLocation).substr(3, 3);
            }
            if (currentLocation[2] == 'Z') {
                arrived = true;
            }
            directionIndex = directionIndex + 1 >= directions.size() ? 0 : directionIndex + 1;
        }
    }

    std::cout << "Total steps: " << lcm(steps, (int)(aLocations.size() / 3))  << std::endl;

    delete [] steps;

    return 0;
}
