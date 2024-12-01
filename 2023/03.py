"""
Advent of Code 2023
Day 3: Gear Ratios
"""


def parse_numbers(current_line, gear_index):
    nums = []
    j = gear_index - 3
    while j < gear_index + 2:
        if current_line[j].isnumeric():
            number = current_line[j]
            j += 1
            while current_line[j].isnumeric():
                number = f'{number}{current_line[j]}'
                j += 1
            if j >= gear_index:
                nums.append(int(number))
                break
        j += 1
    if current_line[gear_index - 1].isnumeric() \
            and not current_line[i].isnumeric() \
            and current_line[gear_index + 1].isnumeric():
        # two numbers on same line
        j = gear_index + 1
        number = current_line[j]
        j += 1
        while current_line[j].isnumeric():
            number = f'{number}{current_line[j]}'
            j += 1
        nums.append(int(number))
    return nums


with open('input.txt', 'r') as _input:
    part_number_sum = 0
    gear_ratio_sum = 0
    line_number = 0
    special_char_indices = []
    engine_schematic = []

    for line in _input:
        engine_schematic.append(line)

    # get all indices of special chars and numbers
    for line in engine_schematic:
        special_char_indices.append([])
        for i in range(0, len(line)):
            if line[i] not in '.\n' and not line[i].isnumeric():
                special_char_indices[line_number].append(i)
        line_number += 1

    line_number = 0

    # check number positions vs indices
    for line in engine_schematic:
        i = 0
        while line[i] != '\n':
            if line[i].isnumeric():
                number_indices = [i - 1, i]
                num = line[i]
                i += 1
                while line[i].isnumeric():
                    num = f'{num}{line[i]}'
                    number_indices.append(i)
                    i += 1
                number_indices.append(number_indices[-1] + 1)
                for index in number_indices:
                    if line_number > 0 and index in special_char_indices[line_number - 1]:
                        part_number_sum += int(num)
                        break
                    if index in special_char_indices[line_number]:
                        part_number_sum += int(num)
                        break
                    if line_number < len(special_char_indices) - 1 and index in special_char_indices[line_number + 1]:
                        part_number_sum += int(num)
                        break
            elif line[i] == '*':
                numbers = []
                if line_number > 0:
                    for num in parse_numbers(engine_schematic[line_number - 1], i):
                        numbers.append(num)
                for num in parse_numbers(engine_schematic[line_number], i):
                    numbers.append(num)
                if line_number < len(special_char_indices) - 1:
                    for num in parse_numbers(engine_schematic[line_number + 1], i):
                        numbers.append(num)
                if len(numbers) > 1:
                    gear_ratio_sum += numbers[0] * numbers[1]
                i += 1
            else:
                i += 1
        line_number += 1

    print(f'Sum of all part numbers: {part_number_sum}')
    print(f'Sum of all gear ratios: {gear_ratio_sum}')
