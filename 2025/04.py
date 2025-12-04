"""
Advent of Code 2025
Day 4: Printing Department
"""

import sys


map = []
prev_total_removed = -1
total_removed = 0

def is_accessible(x, y):
    surrounding_rolls = 0
    if y > 0:
        surrounding_rolls += 1 if map[y - 1][x] == "@" else 0
    if y < len(map) - 1:
        surrounding_rolls += 1 if map[y + 1][x] == "@" else 0
    if x > 0:    
        surrounding_rolls += 1 if map[y][x - 1] == "@" else 0
    if x < len(map[x]) - 1:
        surrounding_rolls += 1 if map[y][x + 1] == "@" else 0
    if y > 0 and x > 0:
        surrounding_rolls += 1 if map[y - 1][x - 1] == "@" else 0
    if y > 0 and x < len(map[x]) - 1:
        surrounding_rolls += 1 if map[y - 1][x + 1] == "@" else 0
    if y < len(map) - 1 and x < len(map[x]) - 1:
        surrounding_rolls += 1 if map[y + 1][x + 1] == "@" else 0
    if y < len(map) - 1 and x > 0: 
        surrounding_rolls += 1 if map[y + 1][x - 1] == "@" else 0
    return surrounding_rolls < 4

with open(sys.argv[1], "r") as _input:
    for line in _input:
        row = []
        for char in line.strip():
            row.append(char)
        map.append(row)

while prev_total_removed != total_removed:
    prev_total_removed = total_removed
    for y, line in enumerate(map):
        for x, char in enumerate(line):
            if char == "@" and is_accessible(x, y):
                total_removed += 1
                map[y][x] = "."

print(total_removed)

