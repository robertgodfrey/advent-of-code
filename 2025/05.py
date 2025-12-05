"""
Advent of Code 2025
Day 5: Cafeteria
"""

import sys


class Range:
    def __init__(self, low, high):
        self.low = low
        self.high = high


fresh_ranges = []
total = 0

with open(sys.argv[1], "r") as file:
    line = file.readline().strip()
    while line != "":
        low, high = line.split("-")
        low, high = int(low), int(high)
        fresh_ranges.append(Range(low, high))
        line = file.readline().strip()

updated = True

while updated:
    temp_ranges = []
    updated = False
    for fresh_range in fresh_ranges:
        inner_updated = False
        for temp_range in temp_ranges:
            if fresh_range.low >= temp_range.low and fresh_range.high <= temp_range.high:
                inner_updated = True
                continue
            if fresh_range.low <= temp_range.low and fresh_range.high >= temp_range.low:
                temp_range.low = fresh_range.low
                inner_updated = True
                updated = True
            if fresh_range.high >= temp_range.high and fresh_range.low <= temp_range.high:
                temp_range.high = fresh_range.high
                inner_updated = True
                updated = True
        if not inner_updated:
            temp_ranges.append(Range(fresh_range.low, fresh_range.high))
    fresh_ranges = temp_ranges


for fresh_range in fresh_ranges:
    total += fresh_range.high - fresh_range.low + 1 

print(total)

