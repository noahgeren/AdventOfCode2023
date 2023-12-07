package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day07 extends Day {
	
	private static final List<Character> CARDS = Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
	
	private static boolean firstPart = true;
	
	List<Hand> hands;

	@Override
	public void loadResources() throws Exception {
		hands = DataLoader.readLines("day07.txt").stream().map(line -> {
			String[] split = line.split(" ");
			return new Hand(split[0], Long.valueOf(split[1]));
		}).collect(Collectors.toList());
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		Day07.firstPart = firstPart;
		Collections.sort(hands);
		long winnings = 0;
		for(int i = 0; i < hands.size(); i++) {
			winnings += hands.get(i).bid * (i + 1);
		}
		return String.valueOf(winnings);
	}

	private class Hand implements Comparable<Hand> {
		String cards;
		long bid;

		public Hand(String cards, long bid) {
			this.cards = cards;
			this.bid = bid;
		}

		@Override
		public int compareTo(Hand o) {
			int handType = getHandType();
			int otherHandType = o.getHandType();
			if(handType != otherHandType) {
				return handType - otherHandType;
			}
			for(int i = 0; i < cards.length(); i++) {
				int cardType = CARDS.indexOf(cards.charAt(i));
				int otherCardType = CARDS.indexOf(o.cards.charAt(i));
				if(!firstPart) {
					if(cardType == 3) {
						cardType = 100;
					}
					if(otherCardType == 3) {
						otherCardType = 100;
					}
				}
				if(cardType != otherCardType) {
					return otherCardType - cardType; // Backwards
				}
			}
			return 0;
		}
		
		private int getHandType() {
			Map<Character, Integer> charCounts = new HashMap<>();
			for(char c : cards.toCharArray()) {
				charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
			}
			int jokers = 0;
			if(!firstPart) {
				if(charCounts.containsKey('J')) {
					jokers = charCounts.remove('J');
				}
			}
			List<Integer> counts = new ArrayList<>(charCounts.values());
			Collections.sort(counts);
			Collections.reverse(counts);
			int first = counts.size() > 0 ? counts.get(0) : 0;
			if(!firstPart) {
				first += jokers;
			}
			if(first == 5) {
				return 7;
			}
			if(first == 4) {
				return 6;
			}
			int second = counts.get(1);
			if(first == 3 && second == 2) {
				return 5;
			}
			if(first == 3) {
				return 4;
			}
			if(first == 2 && second == 2) {
				return 3;
			}
			if(first == 2) {
				return 2;
			}
			return 1;
		}
	}

}
