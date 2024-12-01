<?php
/*
 * Advent of Code 2023
 * Day 7: Camel Cards
 */


function addToHandArray($handArray, $hand, $bet) {
  if (array_key_exists($hand, $handArray)) {
    $handArray[$hand][] = $bet;
    return $handArray[$hand];
  }
  return array($bet);
}


function sortHands($hand1, $hand2) {
  $sortOrder = array("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J");
  for ($i = 0; $i < 5; $i++) {
    $index1 = array_search(((string)$hand1)[$i], $sortOrder);
    $index2 = array_search(((string)$hand2)[$i], $sortOrder);

    if ($index1 != $index2) {
      return $index2 - $index1;
    }
  }
  return 0;
}


function sortAndAdd($array, $winnings, $rank): array {
  uksort($array, function($a, $b){
    return sortHands($a, $b);
  });
  foreach($array as $value) {
    foreach($value as $bet) {
      $winnings += $bet * $rank;
      $rank++;
    }
  }
  return array($winnings, $rank);
}

$fileInput = fopen("input.txt", "r") or die ("Unable to open file");

$five = array();
$four = array();
$full = array();
$three = array();
$two = array();
$pair = array();
$high = array();


while (!feof($fileInput)) {
  $line = fgets($fileInput);
  if ($line == "") break;

  $hand = explode(" ", $line)[0];
  $bet = str_replace("\n", "", explode(" ", $line)[1]);

  $cards = array();
  $jokerCount = 0;

  foreach (str_split($hand) as $card) {
    if ($card == "J") {
      $jokerCount++;
    } else if (array_key_exists($card, $cards)) {
      $cards[$card]++;
    } else {
      $cards[$card] = 1;
    }
  }

  if ($jokerCount > 0) {
    $max = 0;
    $maxKey = "J";
    foreach($cards as $key=>$value) {
      if ($value > $max) {
        $max = $value;
        $maxKey = $key;
      }
    }
    if ($maxKey == "J") {
      $cards["J"] = 5;
    } else {
      $cards[$maxKey] += $jokerCount;
    }
  }

  switch (count($cards)) {
  case 1:
    $five[$hand] = addToHandArray($five, $hand, $bet);
    break;
  case 2:
    if (in_array(4, $cards)) {
      $four[$hand] = addToHandArray($four, $hand, $bet);
    } else {
      $full[$hand] = addToHandArray($full, $hand, $bet);
    }
    break;
  case 3:
    if (in_array(3, $cards)) {
      $three[$hand] = addToHandArray($three, $hand, $bet);
    } else {
      $two[$hand] = addToHandArray($two, $hand, $bet);
    }
    break;
  case 4:
    $pair[$hand] = addToHandArray($pair, $hand, $bet);
    break;
  default:
    $high[$hand] = addToHandArray($high, $hand, $bet);
  }
}

fclose($fileInput);

$rank = 1;
$winnings = 0;

[$winnings, $rank] = sortAndAdd($high, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($pair, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($two, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($three, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($full, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($four, $winnings, $rank);
[$winnings, $rank] = sortAndAdd($five, $winnings, $rank);

echo "Total winnings: " . $winnings . "\n";
