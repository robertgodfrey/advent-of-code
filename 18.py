"""
Advent of Code 2023
Day 18: Lavaduct Lagoon
"""

instructions = [line.strip() for line in open('input.txt', 'r')]
prev_pos = (0, 0)
s1, s2, perimeter = 0, 0, 0

for i in instructions:
    dist = int(i[-7:-2], 16)
    if i[-2] == '0':
        curr_pos = (prev_pos[0] + dist, prev_pos[1])
    elif i[-2] == '2':
        curr_pos = (prev_pos[0] - dist, prev_pos[1])
    elif i[-2] == '3':
        curr_pos = (prev_pos[0], prev_pos[1] + dist)
    else:
        curr_pos = (prev_pos[0], prev_pos[1] - dist)
    s1 += prev_pos[0] * curr_pos[1]
    s2 += prev_pos[1] * curr_pos[0]
    perimeter += dist
    prev_pos = curr_pos

print(abs((s1 - s2) // 2) + perimeter // 2 + 1)
