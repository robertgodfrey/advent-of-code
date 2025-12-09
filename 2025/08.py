"""
Advent of Code 2025
Day 8: Playground
"""

import sys
import math

points = []
distances = {}
joined_circuits = []

with open(sys.argv[1], "r") as file:
    for line in file:
        points.append([int(coord) for coord in line.strip().split(",")])

for i in range(len(points)):
    for j in range(i + 1, len(points)):
        p1 = points[i]
        p2 = points[j]
        dist = math.sqrt((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2 + (p1[2] - p2[2]) ** 2)
        distances[dist] = (",".join([str(p) for p in p1]), ",".join([str(p) for p in p2])) 

sorted_keys = sorted(distances.keys())
i = 0
junctions = None

while len(joined_circuits) == 0 or not len(joined_circuits[0]) == len(points):
    junctions = distances[sorted_keys[i]]
    junc_0_index = None
    junc_1_index = None
    for index, circut in enumerate(joined_circuits):
        if junctions[0] in circut:
            junc_0_index = index
        if junctions[1] in circut:
            junc_1_index = index
    if junc_0_index is not None and junc_1_index is not None:
        if junc_0_index == junc_1_index:
            i += 1
            continue
        joined_circuits[junc_0_index] = joined_circuits[junc_0_index].union(joined_circuits[junc_1_index])
        del joined_circuits[junc_1_index]
    elif junc_0_index is not None:
        joined_circuits[junc_0_index].add(junctions[1])
    elif junc_1_index is not None: 
        joined_circuits[junc_1_index].add(junctions[0])
    else:
        cir_set = set()
        cir_set.add(junctions[0])
        cir_set.add(junctions[1])
        joined_circuits.append(cir_set)
    i += 1

print(junctions)
print(int(junctions[0].split(",")[0]) * int(junctions[1].split(",")[0]))

