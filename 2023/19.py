"""
Advent of Code 2023
Day 19: Aplenty
"""

import copy

workflows, parts = {}, []
workflow = True

for line in open('input.txt', 'r'):
    if line == '\n':
        workflow = False
    elif workflow:
        checks = line[line.index('{') + 1:line.index('}')].split(',')
        for i in range(len(checks) - 1):
            checks[i] = [checks[i].split(':')[0], checks[i].split(':')[1]]
        workflows[line[0:line.index('{')]] = checks
    else:
        part = {}
        part_categories = line.strip().replace('{', '').replace('}', '').split(',')
        for i in range(len(part_categories)):
            cat = part_categories[i].split('=')[0]
            rating = int(part_categories[i].split('=')[1])
            part[cat] = rating
        parts.append(part)

sum = 0


def drill(ranges, current_workflow):
    if current_workflow == 'A':
        return (ranges['x'][1] - ranges['x'][0] + 1) * (ranges['m'][1] - ranges['m'][0] + 1) * (ranges['a'][1] - ranges['a'][0] + 1) * (ranges['s'][1] - ranges['s'][0] + 1)
    elif current_workflow == 'R':
        return 0
    combos = 0
    for flow in workflows[current_workflow]:
        if flow == 'A':
            combos += (ranges['x'][1] - ranges['x'][0] + 1) * (ranges['m'][1] - ranges['m'][0] + 1) * (ranges['a'][1] - ranges['a'][0] + 1) * (ranges['s'][1] - ranges['s'][0] + 1)
        elif flow == 'R':
            continue
        else:
            if type(flow) == list:
                cat_to_check = flow[0][0]
                operator = flow[0][1]
                value = int(flow[0][2:])
                if ranges[cat_to_check][0] < value < ranges[cat_to_check][1]:
                    temp_ranges = copy.deepcopy(ranges)
                    if operator == '>':
                        temp_ranges[cat_to_check][0] = value + 1
                        combos += drill(temp_ranges, flow[1])
                        ranges[cat_to_check][1] = value
                    elif operator == '<':
                        temp_ranges[cat_to_check][1] = value - 1
                        combos += drill(temp_ranges, flow[1])
                        ranges[cat_to_check][0] = value
            else:
                combos += drill(ranges, flow)
    return combos


print(drill({'x': [1, 4000], 'm': [1, 4000], 'a': [1, 4000], 's': [1, 4000]}, 'in'))
