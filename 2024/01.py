def parse_input():
    list_a = []
    list_b = []
    with open('input.txt', 'r') as _input:
        for line in _input: 
            line = line.strip().split('   ')
            list_a.append(int(line[0]))
            list_b.append(int(line[1]))
    return list_a, list_b

def part_one(list_a, list_b):
    total_distance = 0
    list_a.sort()
    list_b.sort()
    for i in range(len(list_a)):
        total_distance += abs(list_a[i] - list_b[i])
    print(total_distance)


def part_two(list_a, list_b):
    total_distance = 0
    occurences = {}
    for num in list_b:
        occurences[num] = occurences[num] + 1 if num in occurences else 1
    for num in list_a:
        total_distance += num * occurences[num] if num in occurences else 0
    print(total_distance)


if __name__ == '__main__':
    list_a, list_b = parse_input()
    part_one(list_a, list_b)
    part_two(list_a, list_b)
