/*
 * Advent of Code 2023
 * Day 9: Mirage Maintenance
 */

import Foundation

var input = ""
var sumNext = 0
var sumPrev = 0

do {
    input = try String(contentsOf: URL(fileURLWithPath: "input.txt"))
} catch {
    print(":(")
}

for line in input.components(separatedBy: "\n") {
    if line.count == 0 { continue }

    let nums = line.components(separatedBy: " ")
    var tempNums = nums.map { Int($0) ?? 0 }
    var toAddNext = 0
    var toSubPrev = 0
    var subtract = true

    repeat {
        var evenMoreTempNums = [Int]()
        for i in 1...tempNums.count - 1 {
            evenMoreTempNums.append(tempNums[i] - tempNums[i - 1])
        }
        tempNums = evenMoreTempNums
        toAddNext += tempNums.last ?? 0
        toSubPrev += subtract ? -(tempNums.first ?? 0) : (tempNums.first ?? 0)
        subtract = !subtract
    } while !tempNums.dropLast().allSatisfy { $0 == tempNums.last ?? 0 }
    sumNext += ((Int(nums.last ?? "") ?? 0) + toAddNext)
    sumPrev += ((Int(nums.first ?? "") ?? 0) + toSubPrev)
}

print("Sum of next vals: \(sumNext)")
print("Sum of previous vals: \(sumPrev)")
