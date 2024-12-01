# Advent of Code 2023
# Day 4: Scratchcards

total_points=0
total_cards=0
current_card=1
num_cards=()

for _ in $(seq 1 212)
do
  num_cards+=(1)
done

while read -r line
do
  num_matches=0
  IFS=' ' read -ra winners <<< "${line:10:30}"
  IFS=' ' read -ra numbers <<< "${line: -75}"
  for number in "${winners[@]}"
  do
    if [[ "${numbers[*]}" =~ [[:space:]]${number}$|^${number}[[:space:]]|[[:space:]]${number}[[:space:]] ]]
    then
      matched_nums+=(number)
      num_matches=$((num_matches + 1))
    fi
  done
  if ((num_matches > 0))
  then
    total_points=$((total_points + 2**(num_matches - 1)))
    for i in $(seq $current_card $((current_card + num_matches - 1)));
    do
      num_cards[i]=$((num_cards[i] + num_cards[current_card - 1]))
    done
  fi
  current_card=$((current_card + 1))
done < input.txt

echo "Total points: $total_points"

for i in $(seq 0 211)
do
  total_cards=$((total_cards + num_cards[i]))
done

echo "Total cards: $total_cards"
