"""
Advent of Code 2025
Day 2: Gift Shop 
"""

import sys

def is_invalid(num):
    if len(num) == 1:
        return False
    if num[0] * len(num) == num:
        return True
    if len(num) % 2 == 0:
        split = int(len(num) / 2)
        if num[:split] == num[split:]:
            return True
    for i in range(1, len(num) // 2 + 1):
        remaining_len = len(num) - i
        if num[:i] * int(remaining_len / i + 1) == num:
            return True
    return False

ranges = []
sum = 0

with open(sys.argv[1], "r") as _input:
    for line in _input:
        for _range in line.split(","):
            ranges.append(_range)

for _range in ranges:
    left, right = _range.split("-")
    for num in range(int(left), int(right) + 1):
        if is_invalid(str(num)):
            sum += num

print(sum)

