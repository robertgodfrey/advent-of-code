/*
 * Advent of Code 2023
 * Day 10: Pipe Maze (wrong day to pick C)
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"


struct position {
    unsigned short x;
    unsigned short y;
    char prevDir;
};


void findFirst(struct position *pos, unsigned short limitX, unsigned short limitY, char **lines) {
    const unsigned short x = (*pos).x;
    const unsigned short y = (*pos).y;
    const char prev = (*pos).prevDir;
    char temp;

    if (x > 0 && prev != 'l') { // search left
        temp = lines[y][x - 1];
        if (temp == '-' || temp == 'F' || temp == 'L') {
            (*pos).x--;
            (*pos).prevDir = 'r';
            return;
        }
    }
    if (x < limitX && prev != 'r') { // search right
        temp = lines[y][x + 1];
        if (temp == '-' || temp == 'J' || temp == '7') {
            (*pos).x++;
            (*pos).prevDir = 'l';
            return;
        }
    }
    if (y > 0 && prev != 'u') { // search up
        temp = lines[y - 1][x];
        if (temp == '|' || temp == 'F' || temp == '7') {
            (*pos).y--;
            (*pos).prevDir = 'd';
            return;
        }
    }
    if (y < limitY && prev != 'd') { // search down
        temp = lines[y + 1][x];
        if (temp == '|' || temp == 'J' || temp == 'L') {
            (*pos).y++;
            (*pos).prevDir = 'u';
            return;
        }
    }
}


void findNext(struct position *pos, char **lines) {
    switch (lines[(*pos).y][(*pos).x]) {
        case '-':
            if ((*pos).prevDir == 'l') {
                (*pos).x++;
            } else {
                (*pos).x--;
            }
            break;
        case '|':
            if ((*pos).prevDir == 'u') {
                (*pos).y++;
            } else {
                (*pos).y--;
            }
            break;
        case 'L':
            if ((*pos).prevDir == 'u') {
                (*pos).prevDir = 'l';
                (*pos).x++;
            } else {
                (*pos).prevDir = 'd';
                (*pos).y--;
            }
            break;
        case 'J':
            if ((*pos).prevDir == 'u') {
                (*pos).prevDir = 'r';
                (*pos).x--;
            } else {
                (*pos).prevDir = 'd';
                (*pos).y--;
            }
            break;
        case '7':
            if ((*pos).prevDir == 'd') {
                (*pos).prevDir = 'r';
                (*pos).x--;
            } else {
                (*pos).prevDir = 'u';
                (*pos).y++;
            }
            break;
        case 'F':
            if ((*pos).prevDir == 'd') {
                (*pos).prevDir = 'l';
                (*pos).x++;
            } else {
                (*pos).prevDir = 'u';
                (*pos).y++;
            }
            break;
    }
}


int main() {
    FILE *fp;
    char **lines = NULL;
    char *line = NULL;
    size_t len = 0;
    ssize_t read;
    unsigned short lineCount = 0;

    char *startPos;
    struct position first;
    struct position second;

    fp = fopen("input.txt", "r");

    if (fp == NULL) {
        printf(":(\n");
        return 1;
    }

    while ((read = getline(&line, &len, fp)) != -1) {
        lines = realloc(lines, (lineCount + 1) * sizeof(char *));

        if (lines == NULL) {
            printf(":(\n");
            return 1;
        }

        lines[lineCount] = malloc(strlen(line) + 1);

        if (lines[lineCount] == NULL) {
            printf(":(\n");
            return 1;
        }

        strcpy(lines[lineCount], line);

        startPos = strchr(line, 'S');

        if (startPos) {
            first.y = lineCount;
            first.x = startPos - line;
            second.y = first.y;
            second.x = first.x;
        }
        lineCount++;
    }

    free(line);
    fclose(fp);

    char pipeSquares[(size_t)(lineCount * len)][9];
    char tempString[9];

    sprintf(tempString, "%hu,%hu", first.x, first.y);
    strcpy(pipeSquares[0], tempString);

    findFirst(&first, len, lineCount, lines);
    sprintf(tempString, "%hu,%hu", first.x, first.y);
    strcpy(pipeSquares[1], tempString);

    second.prevDir = first.x != second.x ? (first.prevDir == 'r' ? 'l' : 'r') : (first.prevDir == 'u' ? 'd' : 'u');
    findFirst(&second, len, lineCount, lines);
    sprintf(tempString, "%hu,%hu", second.x, second.y);
    strcpy(pipeSquares[2], tempString);

    unsigned short steps = 1;
    unsigned short pipeSquareIndex = 3;

    while (first.x != second.x || first.y != second.y) {
        findNext(&first, lines);
        sprintf(tempString, "%hu,%hu", first.x, first.y);
        strcpy(pipeSquares[pipeSquareIndex], tempString);
        pipeSquareIndex++;

        findNext(&second, lines);
        sprintf(tempString, "%hu,%hu", second.x, second.y);
        strcpy(pipeSquares[pipeSquareIndex], tempString);
	    pipeSquareIndex++;
        steps++;
    }

    printf("Max steps: %hu\n", steps);

    const long unsigned int lineLen = strlen(lines[0]);
    unsigned short numInsideSquares = 0;

    for (unsigned short i = 0; i < lineCount; i++) {
        char tempChar = 'x';
        char inside = 0;
        for (unsigned short j = 0; j < lineLen - 1; j++) {
            sprintf(tempString, "%hu,%hu", j, i);
            char partPipe = 0;
            // can be vastly improved by sorting first, but i've already spent too long on this
            for (unsigned short k = 0; k < pipeSquareIndex; k++) {
                if (strcmp(tempString, pipeSquares[k]) == 0) {
                    partPipe = 1;
                    break;
                }
            }
            if (partPipe) {
                switch (lines[i][j]) {
                    case 'L':
                        tempChar = 'L';
                        printf(KGRN "└");
                        break;
                    case 'F':
                        tempChar = 'F';
                        printf(KGRN "┌");
                        break;
                    case 'J':
                        if (tempChar == 'F') {
                            inside = inside == 0 ? 1 : 0;
                        }
                        tempChar = 'J';
                        printf(KGRN "┘");
                        break;
                    case '7':
                        if (tempChar == 'L') {
                            inside = inside == 0 ? 1 : 0;
                        }
                        tempChar = '7';
                        printf(KGRN "┐");
                        break;
                    case 'S': // my S is a |
                    case '|':
                        inside = inside == 0 ? 1 : 0;
                        printf(KGRN "|");
                        break;
                    default:
                        printf(KGRN "%c", lines[i][j]);
                        break;
                }
            } else {
                if (inside) {
                    numInsideSquares++;
                    printf(KNRM "*");
                } else {
                    printf(KRED ".");
                }
            }
        }
        printf("\n");
    }

    printf(KNRM "Number of squares inside: %hu\n", numInsideSquares);

    for (unsigned short i = 0; i < lineCount; i++) {
        free(lines[i]);
    }

    free(lines);

    return 0;
}
