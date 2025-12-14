"""
Advent of Code 2025
Day 9: Movie Theater
"""

import sys

from itertools import combinations, pairwise

red_tiles = []

with open(sys.argv[1], "r") as file:
    for line in file:
        x, y = map(int, line.strip().split(","))        
        red_tiles.append((x, y))

border_edges = list(pairwise(red_tiles + [red_tiles[0]]))

max_area = 0

for (x1, y1), (x2, y2) in combinations(red_tiles, 2):
    x_min, x_max = sorted((x1, x2))
    y_min, y_max = sorted((y1, y2))
    area = (x_max - x_min + 1) * (y_max - y_min + 1)
    if area <= max_area:
        continue
    intersects = False
    for (edge_x1, edge_y1), (edge_x2, edge_y2) in border_edges:
        if edge_x1 == edge_x2:  # vertical line
            if x_min < edge_x1 < x_max and y_min < max(edge_y1, edge_y2) and y_max > min(edge_y1, edge_y2):
                intersects = True
                break
        else:  # horizontal line
            if y_min < edge_y1 < y_max and x_min < max(edge_y1, edge_y2) and x_max > min(edge_y1, edge_y2):
                intersects = True
                break
    if not intersects:
        max_area = area

print(max_area)

