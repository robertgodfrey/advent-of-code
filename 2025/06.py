"""
Advent of Code 2025
Day 6: Trash Compactor
"""

import math
import re
import sys


equation_rows = []

with open(sys.argv[1], "r") as file:
    for line in file:
        equation_rows.append(line.replace("\n", ""))

total = 0
col = 0
row = 0
operator = ''
operands = []

while col < len(equation_rows[0]):
    bottom_line = equation_rows[len(equation_rows) - 1][col]
    operator = bottom_line if bottom_line != " " else operator
    operand = ""
    while row < len(equation_rows) - 1:
        operand += equation_rows[row][col] if equation_rows[row][col] != " " else ""
        row += 1
    if col == len(equation_rows[0]) - 1:
        operands.append(int(operand))
        operand = ""
    if operand == "": 
        total += sum(operands) if operator == "+" else math.prod(operands)
        operands = []
        row = 0
        col += 1
        continue
    operands.append(int(operand))
    row = 0
    col += 1
    

print(total)
