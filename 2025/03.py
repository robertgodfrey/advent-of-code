"""
Advent of Code 2025
Day 3: Lobby
"""

import sys

sum = 0

def find_next_biggest_jolt_index(batteries, start_index, end_index):
    max_jolt = 0
    max_jolt_index = 0
    for i in range(start_index, len(batteries) - end_index):
        if int(batteries[i]) > max_jolt:
            max_jolt = int(batteries[i])
            max_jolt_index = i
    return max_jolt_index


with open(sys.argv[1], "r") as _input:
    for batteries in _input:
        batteries = batteries.strip()
        start_index = 0
        joltage = ''
        for end_index in range(11, -1, -1):
            start_index = find_next_biggest_jolt_index(batteries, start_index, end_index)
            joltage += batteries[start_index]
            start_index += 1  
        sum += int(joltage)
print(sum)

