/*
 * Advent of Code 2023
 * Day 12: Hot Springs
 */

const fs = require('fs');
const input = fs.readFileSync('input.txt').toString().split('\n');
const memo = {};
let sum = 0;

const isMatch = (conditionStr, index, recordLength, lastFlag) => {
    return !(lastFlag && conditionStr.substring(index + recordLength).includes('#')
      || (index > 0 && conditionStr.substring(0, index).includes('#'))
      || (conditionStr.length > recordLength && conditionStr[index + recordLength] === '#')
      || (index > 0 && conditionStr[index - 1] === '#')
      || !(index + recordLength <= conditionStr.length && !conditionStr.substring(index, index + recordLength).includes('.')));
}

function findMatches(conditionStr, records) {
    const memoKey = `${conditionStr}[${records.join(',')}]`;

    if (memo.hasOwnProperty(memoKey)) {
        return memo[memoKey];
    }

    let possibleCombinations = 0;

    for (let i = 0; i < conditionStr.length; i++) {
        if (conditionStr[i] === '.') {
            continue;
        }
        if (isMatch(conditionStr, i, records[0], records.length === 1)) {
            if (records.length <= 1) {
                if (conditionStr.length >= records[0]) {
                    possibleCombinations += 1;
                }
            } else {
                possibleCombinations += findMatches(conditionStr.substring(i + records[0] + 1), records.slice(1));
            }
        }
    }
    memo[memoKey] = possibleCombinations;
    return possibleCombinations;
}

for (const line of input) {
    if (!line) continue;

    const conditionStr = line.split(' ')[0];
    const records = line.split(' ')[1].split(',').map((record) => parseInt(record));

    let unfoldedConditionStr = conditionStr;
    const unfoldedRecords = [...records];

    for (let i = 0; i < 4; i++) {
        unfoldedConditionStr += `?${conditionStr}`;
        unfoldedRecords.push(...records);
    }

    for (const key of Object.keys(memo)){
        if (memo.hasOwnProperty(key)){
            delete memo[key];
        }
    }
    sum += findMatches(unfoldedConditionStr, unfoldedRecords);
}

console.log(sum);
