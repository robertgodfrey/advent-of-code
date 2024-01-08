/*
 * Advent of Code 2023
 * Day 20: Pulse Propagation
 */

const fs = require('fs');
const input = fs.readFileSync('input.txt').toString().trim().split('\n');
const nodes = {};
let xfHigh = 0, fzHigh = 0, mpHigh = 0, hnHigh = 0;
let buttonPresses = 0;

function pushButton() {
    buttonPresses++;
    const queue = [['broadcaster', 'L']];
    while (queue.length > 0) {
        const currentNode = queue[0][0];
        const pulse = queue[0][1];
        if (currentNode === 'xf' && pulse === 'H') {
            xfHigh = buttonPresses;
        }
        if (currentNode === 'fz' && pulse === 'H') {
            fzHigh = buttonPresses;
        }
        if (currentNode === 'mp' && pulse === 'H') {
            mpHigh = buttonPresses;
        }
        if (currentNode === 'hn' && pulse === 'H') {
            hnHigh = buttonPresses;
        }
        for (const node of nodes[currentNode].outNodes) {
            if (nodes[node].type === '%') {
                if (pulse === 'H') {
                    continue;
                }
                nodes[node].on = !nodes[node].on;
                if (nodes[node].on) {
                    queue.push([node, 'H']);
                } else {
                    queue.push([node, 'L']);
                }
            } else if (nodes[node].type === '&') {
                nodes[node].inNodes[currentNode] = pulse;
                let allHigh = true;
                for (const inNode of Object.keys(nodes[node].inNodes)) {
                    if (nodes[node].inNodes[inNode] === 'L') {
                        allHigh = false;
                        break;
                    }
                }
                if (allHigh) {
                    queue.push([node, 'L']);
                } else {
                    queue.push([node, 'H']);
                }
            }
        }
        queue.shift();
    }
}

for (const line of input) {
    const nodeKey = line.split(' -> ')[0] === 'broadcaster' ? 'broadcaster' : line.split(' -> ')[0].substring(1);
    const type = line.split(' -> ')[0][0];
    const outNodes = line.split(' -> ')[1].split(', ');
    if (!Object.keys(nodes).includes(nodeKey)) {
        nodes[nodeKey] = {
            on: false,
            type,
            outNodes,
        };
    } else {
        nodes[nodeKey] = {
            ...nodes[nodeKey],
            on: false,
            type,
            outNodes,
        };
    }
    for (const destination of outNodes) {
        if (!Object.keys(nodes).includes(destination)) {
            nodes[destination] = {...nodes[destination], inNodes: []};
        }
        if (!(Object.keys(nodes[destination]).includes('inNodes'))) {
            nodes[destination].inNodes = {};
        }
        nodes[destination].inNodes[nodeKey] = 'L';
    }
}

while (xfHigh === 0 || fzHigh === 0 || mpHigh === 0 || hnHigh === 0) {
    pushButton();
}

console.log(lcm(xfHigh, fzHigh, mpHigh, hnHigh));
