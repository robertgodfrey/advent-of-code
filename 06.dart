/*
 * Advent of Code 2023
 * Day 6: Wait For It
 */

import 'dart:io';

void main() {
  /* Part One */ 
  List<int> times = [];
  List<int> distances = [];
  List<int> possibleSolutions = [];

  try {
    List<String> lines = File('input.txt').readAsLinesSync();
    for (final match in new RegExp(r'(\d+)').allMatches(lines[0])) {
      times.add(int.parse(match.group(0)!));
    }
    for (final match in new RegExp(r'(\d+)').allMatches(lines[1])) {
      distances.add(int.parse(match.group(0)!));
    }
  } catch (Exception) {
    print(Exception.toString());
  }
  for (int i = 0; i < times.length; i++) {
    int solutions = 0;
    for (int secondsHeld = 1; secondsHeld < times[i]; secondsHeld++) {
      if (secondsHeld * (times[i] - secondsHeld) > distances[i]) {
        solutions++;
      }
    }
    possibleSolutions.add(solutions);
  }
  print(possibleSolutions.reduce((total, currentNum) => total * currentNum));

  /* Part Two */
  String tempNum = '';
  times.forEach((time) => tempNum += '$time');
  int actualTime = int.parse(tempNum);
  tempNum = '';
  distances.forEach((distance) => tempNum += '$distance');
  int actualDistance = int.parse(tempNum);

  int actualSolutions = 0;
  for (int secondsHeld = 1; secondsHeld < actualTime; secondsHeld++) {
    if (secondsHeld * (actualTime - secondsHeld) > actualDistance) {
      actualSolutions++;
    }
  }
  print(actualSolutions);
}

