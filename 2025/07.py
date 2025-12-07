"""
Advent of Code 2025
Day 7: Laboratories
"""

import sys


with open(sys.argv[1], "r") as file:
    line = file.readline().strip()
    beam_indices = {index: 0 for index in range(len(line))}
    for index, char in enumerate(line):
        if char == "S":
            beam_indices[index] = 1
    line = file.readline().strip()
    while line:
        line = file.readline().strip()
        for index, char in enumerate(line):
            if char == "^":
                beam_indices[index - 1] += beam_indices[index]
                beam_indices[index + 1] += beam_indices[index]
                beam_indices[index] = 0

print(sum(beam_indices.values()))
