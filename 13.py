"""
Advent of Code 2023
Day 13: Point of Incidence
"""


def is_reflective(_test, index):
    left = _test[:index]
    right = _test[index:]
    return left[-len(right):][::-1] == right[:len(left)]


def find_vertical_split(mirror, old_split=None) -> int:
    v_split = []
    for i in range(1, len(mirror[0])):
        if is_reflective(mirror[0], i) and i != old_split:
            v_split.append(i)
    for i in range(1, len(mirror)):
        for j in range(1, len(mirror[i])):
            if not is_reflective(mirror[i], j) and j in v_split:
                v_split.pop(v_split.index(j))
        if len(v_split) < 1:
            break
    if len(v_split) == 1:
        return v_split[0]
    return -1


def find_horizontal_split(mirror, old_split=None) -> int:
    horizontal_split = []
    for i in range(1, len(mirror)):
        if is_reflective(mirror, i) and i != old_split:
            horizontal_split.append(i)
    for i in range(1, len(mirror[0])):
        for j in range(1, len(mirror)):
            if not is_reflective(mirror, j) and j in horizontal_split:
                horizontal_split.pop(horizontal_split.index(j))
        if len(horizontal_split) < 1:
            break
    if len(horizontal_split) == 1:
        return horizontal_split[0]
    return -1


def main():
    mirrors = [[]]
    _sum = 0
    with open('input.txt', 'r') as _input:
        m = 0
        for line in _input:
            if line == '\n':
                m += 1
                mirrors.append([])
            else:
                mirrors[m].append(line.strip())
    for mirr in mirrors:
        smudged_split = find_vertical_split(mirr)
        smudged_m = f'VS:{smudged_split}'
        if smudged_split == -1:
            smudged_split = find_horizontal_split(mirr)
            smudged_m = f'HS:{smudged_split}'
        for i in range(len(mirr)):
            found = False
            for j in range(len(mirr[i])):
                temp_mirr = mirr.copy()
                temp_mirr[i] = f'{mirr[i][:j]}{"#" if mirr[i][j] == "." else "."}{mirr[i][j+1:]}'
                split = find_vertical_split(temp_mirr, smudged_split if 'VS:' in smudged_m else None)
                if split == -1 or f'VS:{split}' == smudged_m:
                    split = find_horizontal_split(temp_mirr, smudged_split if 'HS:' in smudged_m else None)
                    if split != -1 and f'HS:{split}' != smudged_m:
                        _sum += 100 * split
                        found = True
                        break
                else:
                    _sum += split
                    found = True
                    break
            if found:
                break
    print(_sum)


if __name__ == '__main__':
    main()
