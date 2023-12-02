# Advent of Code 2023
# Day 2: Cube Conundrum

use strict;
use warnings;

open inFH, "< input.txt" or die "\nUnable to open file\n";

my @input = <inFH>;
my $sumPossibleIndices = 0;
my $sumPowers = 0;

foreach my $line (@input) {
    my $possible = 1;
    my $maxRed = 0;
    my $maxGreen = 0;
    my $maxBlue = 0;

    $line =~ m/Game (\d+):/;
    my $gameNum = $1;

    # check red
    while ($line =~ m/.*?(\d+) red/g) {
        if ($1 > 12) {
            $possible = 0;
        }
        if ($1 > $maxRed) {
            $maxRed = $1;
        }
    }

    # check green
    while ($line =~ m/.*?(\d+) green/g) {
        if ($1 > 13) {
            $possible = 0;
        }
        if ($1 > $maxGreen) {
            $maxGreen = $1;
        }
    }

    # check blue
    while ($line =~ m/.*?(\d+) blue/g) {
        if ($1 > 14) {
            $possible = 0;
        }
        if ($1 > $maxBlue) {
            $maxBlue = $1;
        }
    }

    if ($possible) {
        $sumPossibleIndices += $gameNum;
    }
    $sumPowers += $maxRed * $maxGreen * $maxBlue;
}

close inFH;

print "\nSum of possible game indices: $sumPossibleIndices\n";
print "\nSum of powers: $sumPowers\n\n";
