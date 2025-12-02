"""
Advent of Code 2025
Day 1: Secret Entrance
"""

import math
import sys

dial_pos = 50
count_zeros = 0

with open(sys.argv[1], "r") as _input:
    for line in _input:
        operation = line.strip()
        dist = int(operation[1:])
        prev_pos = dial_pos
        dial_pos = dial_pos + dist if operation.startswith("R") else dial_pos - dist
        if dial_pos > 99 or dial_pos <= 0:
            if dial_pos <= 0 and prev_pos != 0:
                count_zeros += 1
            count_zeros += math.floor(abs(dial_pos / 100))
            dial_pos %= 100
    print(count_zeros)

