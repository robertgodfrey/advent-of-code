/*
 */

const fs = require('fs');
const input = fs.readFileSync('input.txt').toString().trim().split('\n');
const stepsGoal = 64;

for (let i = 0; i < input.length; i++) {
  input[i] = input[i].split('');
}

const templateMap = JSON.parse(JSON.stringify(input));
let currentMap = JSON.parse(JSON.stringify(input));
let stepsTaken = 0;
let possiblePlots = 0;
let prevPossiblePlots = 0;

const possibleStep = (row, col) => {
  if (row < 0 || row > templateMap.length) {
    return false;
  }
  if (col < 0 || col > templateMap[0].length) {
    return false;
  }
  return templateMap[row][col] !== '#';
};

function step(nextStepMap, row, col) {
  // check down
  if (possibleStep(row + 1, col)) {
    nextStepMap[row + 1][col] = 'O';
  }
  // check up
  if (possibleStep(row - 1, col)) {
    nextStepMap[row - 1][col] = 'O';
  }
  // check left
  if (possibleStep(row, col - 1)) {
    nextStepMap[row][col - 1] = 'O';
  }
  // check right
  if (possibleStep(row, col + 1)) {
    nextStepMap[row][col + 1] = 'O';
  }
}

for (let i = 0; i < input.length; i++) {
  if (input[i].includes('S')) {
    templateMap[i][input[i].indexOf('S')] = '.';
    currentMap[i][input[i].indexOf('S')] = 'O';
    break;
  }
}

while(stepsTaken < stepsGoal) {
  // TODO consider adding coordinates to a set
  stepsTaken++;
  const nextStepMap = JSON.parse(JSON.stringify(templateMap)); 
  for (let row = 0; row < input.length; row++) {
    for (let col = 0; col < input[0].length; col++) {
      if (currentMap[row][col] == 'O') {
        step(nextStepMap, row, col);
      }
    }
  }
  currentMap = JSON.parse(JSON.stringify(nextStepMap));
  possiblePlots = 0;

  for (const row of currentMap) {
    for (const col of row) {
      if (col === 'O') {
        possiblePlots++;
      }
    }
  }
  console.log(possiblePlots - prevPossiblePlots);
  prevPossiblePlots = possiblePlots;
}

possiblePlots = 0;

for (const row of currentMap) {
  for (const col of row) {
    if (col === 'O') {
      possiblePlots++;
    }
  }
}

console.log(possiblePlots);
