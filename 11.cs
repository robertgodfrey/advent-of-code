/*
 * Advent of Code 2023
 * Day 11: Cosmic Expansion
 */

using System;
using System.IO;
using System.Collections.Generic;

class Day11 {
    static void Main() {
        if (!File.Exists("input.txt")) {
            Console.WriteLine("input.txt does not exist");
            return;
        }
        string[] universe = File.ReadAllLines("input.txt");

        int width = universe[0].Length;
        int height = universe.Length;
        bool[] blankLines = new bool[height];
        bool[] blankCols = new bool[width];

        Array.Fill(blankLines, true);
        Array.Fill(blankCols, true);

        for (int lineNum = 0; lineNum < height; lineNum++) {
            for (int charNum = 0; charNum < width; charNum++) {
                if (universe[lineNum][charNum] != '.') {
                    blankLines[lineNum] = false;
                    blankCols[charNum] = false;
                }
            }
        }

        List<int> blankLineNums = new List<int>();
        List<int> blankColNums = new List<int>();
        List<(int, int)> galaxies = new List<(int, int)>();

        for (int i = 0; i < width; i++) {
            if (blankCols[i]) {
                blankColNums.Add(i);
            }
        }
        for (int i = 0; i < height; i++) {
            if (blankLines[i]) {
                blankLineNums.Add(i);
            }
        }
        for (int lineNum = 0; lineNum < height; lineNum++) {
            for (int charNum = 0; charNum < width; charNum++) {
                if (universe[lineNum][charNum] == '#') {
                    galaxies.Add((lineNum, charNum));
                }
            }
        }

        ulong sumDistances = 0;

        for (int startGalaxy = 0; startGalaxy < galaxies.Count - 1; startGalaxy++) {
            int startLineNum = galaxies[startGalaxy].Item1;
            int startCharNum = galaxies[startGalaxy].Item2;
            for (int endGalaxy = startGalaxy + 1; endGalaxy < galaxies.Count; endGalaxy++) {
                int endLineNum = galaxies[endGalaxy].Item1;
                int endCharNum = galaxies[endGalaxy].Item2;
                int verticalDistance = Math.Abs(startLineNum - endLineNum);
                int horizontalDistance = Math.Abs(startCharNum - endCharNum);

                foreach(int blankLineNum in blankLineNums) {
                    if ((blankLineNum > startLineNum && blankLineNum < endLineNum)
                            || (blankLineNum < startLineNum && blankLineNum > endLineNum)) {
                        verticalDistance += 999999;
                    }
                }
                foreach(int blankColNum in blankColNums) {
                    if ((blankColNum > startCharNum && blankColNum < endCharNum)
                            || (blankColNum < startCharNum && blankColNum > endCharNum)) {
                        horizontalDistance += 999999;
                    }
                }
                sumDistances += (ulong)(verticalDistance + horizontalDistance);
            }
        }
        Console.WriteLine(sumDistances);
    }
}
