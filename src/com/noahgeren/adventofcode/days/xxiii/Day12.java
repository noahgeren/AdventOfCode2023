package com.noahgeren.adventofcode.days.xxiii;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day12 extends Day {

	private final int DUPLICATE = 5;

	char[][] arrangements;
	int[][] counts;
	Map<String, BigInteger> cache = new HashMap<>();

	@Override
	public void loadResources() throws Exception {
		List<String> linesTemp = DataLoader.readLines("day12.txt");
		arrangements = new char[linesTemp.size()][];
		counts = new int[linesTemp.size()][];
		for (int i = 0; i < linesTemp.size(); i++) {
			String[] line = linesTemp.get(i).split(" ");
			arrangements[i] = line[0].toCharArray();
			String[] strCounts = line[1].split(",");
			counts[i] = new int[strCounts.length];
			for (int j = 0; j < strCounts.length; j++) {
				counts[i][j] = Integer.valueOf(strCounts[j]);
			}
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (!firstPart) {
			char[][] newArrangements = new char[arrangements.length][];
			for (int i = 0; i < arrangements.length; i++) {
				List<Character> line = new ArrayList<>();
				for (int k = 0; k < DUPLICATE; k++) {
					for (int j = 0; j < arrangements[i].length; j++) {
						line.add(arrangements[i][j]);
					}
					if (k != DUPLICATE - 1) {
						line.add('?');
					}
				}
				newArrangements[i] = new char[line.size()];
				for (int j = 0; j < line.size(); j++) {
					newArrangements[i][j] = line.get(j);
				}
			}
			arrangements = newArrangements;

			int[][] newCounts = new int[counts.length][];
			for (int i = 0; i < counts.length; i++) {
				List<Integer> line = new ArrayList<>();
				for (int k = 0; k < DUPLICATE; k++) {
					for (int j = 0; j < counts[i].length; j++) {
						line.add(counts[i][j]);
					}
				}
				newCounts[i] = new int[line.size()];
				for (int j = 0; j < line.size(); j++) {
					newCounts[i][j] = line.get(j);
				}
			}
			counts = newCounts;
		}

		BigInteger totalArrangements = BigInteger.ZERO;
		for (int i = 0; i < arrangements.length; i++) {
			System.out.print((i + 1) + "/" + arrangements.length + ": ");
			BigInteger count = countArrangements(arrangements[i], counts[i], 0);
			totalArrangements = totalArrangements.add(count);
			System.out.println(count);
			cache.clear();
		}
		return String.valueOf(totalArrangements);
	}

	private BigInteger countArrangements(char[] arr, int[] counts, int index) {
		String hash = hash(arr, counts, index);
		if (cache.containsKey(hash)) {
			return cache.get(hash);
		}
		if (index >= arr.length) {
			return checkValidUpTo(arr, counts, index) ? BigInteger.ONE : BigInteger.ZERO;
		}
		BigInteger answer;
		if (arr[index] == '?') {
			arr[index] = '#';
			BigInteger filled = countArrangements(arr, counts, index + 1);
			arr[index] = '.';
			BigInteger notFilled = BigInteger.ZERO;
			if (checkValidUpTo(arr, counts, index)) {
				notFilled = countArrangements(arr, counts, index + 1);
			}
			arr[index] = '?';
			answer = filled.add(notFilled);
		} else if (arr[index] == '.' && !checkValidUpTo(arr, counts, index)) {
			answer = BigInteger.ZERO;
		} else {
			answer = countArrangements(arr, counts, index + 1);
		}
		cache.put(hash, answer);
		return answer;
	}

	private boolean checkValidUpTo(char[] arr, int[] counts, int bound) {
		List<Integer> countCheck = getCountCheck(arr, counts, bound);
		if (countCheck.size() > counts.length || (bound >= arr.length - 1 && countCheck.size() != counts.length)) {
			return false;
		}
		for (int i = 0; i < countCheck.size(); i++) {
			if (counts[i] != countCheck.get(i)) {
				return false;
			}
		}
		return true;
	}

	private List<Integer> getCountCheck(char[] arr, int[] counts, int bound) {
		List<Integer> countCheck = new ArrayList<>();
		countCheck.add(0);
		for (int i = 0; i < arr.length && i < bound; i++) {
			char c = arr[i];
			if (c == '#') {
				countCheck.set(countCheck.size() - 1, countCheck.get(countCheck.size() - 1) + 1);
			} else if (countCheck.get(countCheck.size() - 1) != 0) {
				countCheck.add(0);
			}
		}
		if (countCheck.get(countCheck.size() - 1) == 0) {
			countCheck.remove(countCheck.size() - 1);
		}
		return countCheck;
	}

	private String hash(char[] arr, int[] counts, int bound) {
		StringBuilder hash = new StringBuilder();
		for (int i = Math.max(bound - 1, 0); i < arr.length; i++) {
			hash.append(arr[i]);
		}
		List<Integer> countCheck = getCountCheck(arr, counts, bound);
		for (int c : countCheck) {
			hash.append(c);
			hash.append(',');
		}
		return hash.toString();
	}

}