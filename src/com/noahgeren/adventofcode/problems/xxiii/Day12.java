package com.noahgeren.adventofcode.problems.xxiii;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day12 extends Day {
	
	private final int DUPLICATE = 5;

	char[][] arrangements;
	int[][] counts;
	BigInteger[][] arrangementCounts;

	@Override
	public void loadResources() throws Exception {
		List<String> linesTemp = DataLoader.readLines("day12.txt");
		arrangements = new char[linesTemp.size()][];
		counts = new int[linesTemp.size()][];
		arrangementCounts = new BigInteger[arrangements.length][];
		for (int i = 0; i < linesTemp.size(); i++) {
			String[] line = linesTemp.get(i).split(" ");
			arrangements[i] = line[0].toCharArray();
			String[] strCounts = line[1].split(",");
			counts[i] = new int[strCounts.length];
			for (int j = 0; j < strCounts.length; j++) {
				counts[i][j] = Integer.valueOf(strCounts[j]);
			}
			arrangementCounts[i] = new BigInteger[2];
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (!firstPart) {
			char[][] newArrangements = new char[arrangements.length][];
			for (int i = 0; i < arrangements.length; i++) {
				List<Character> line = new ArrayList<>();
				for(int k = 0; k < DUPLICATE; k++) {
					for (int j = 0; j < arrangements[i].length; j++) {
						line.add(arrangements[i][j]);
					}
					if(k != DUPLICATE - 1) {
						line.add('?');
					}
				}
				newArrangements[i] = new char[line.size()];
				for(int j = 0; j < line.size(); j++) {
					newArrangements[i][j] = line.get(j);
				}
			}
			arrangements = newArrangements;
			
			int[][] newCounts = new int[counts.length][];
			for (int i = 0; i < counts.length; i++) {
				List<Integer> line = new ArrayList<>();
				for(int k = 0; k < DUPLICATE; k++) {
					for (int j = 0; j < counts[i].length; j++) {
						line.add(counts[i][j]);
					}
				}
				newCounts[i] = new int[line.size()];
				for(int j = 0; j < line.size(); j++) {
					newCounts[i][j] = line.get(j);
				}
			}
			counts = newCounts;
		}

		BigInteger totalArrangements = BigInteger.ZERO;
		for (int i = 0; i < arrangements.length && i < 18; i++) {
			System.out.print((i + 1) + "/" + arrangements.length + ": ");
			BigInteger count = countArrangements(arrangements[i], counts[i], 0);
			arrangementCounts[i][firstPart ? 0 : 1] = count;
			if (!firstPart && DUPLICATE == 2) {
				BigInteger ratio = arrangementCounts[i][1].divide(arrangementCounts[i][0]);
				for (int j = 0; j < 3; j++) {
					count = count.multiply(ratio);
				}
			}
			totalArrangements = totalArrangements.add(count);
			System.out.println(count);
			if (!firstPart && DUPLICATE == 2 && arrangementCounts[i][1].mod(arrangementCounts[i][0]).compareTo(BigInteger.ZERO) != 0) {
				System.out.println("here");
//				System.out.println(arrangementCounts[i][1] + "%" + arrangementCounts[i][0] + "="
//						+ arrangementCounts[i][1].mod(arrangementCounts[i][0]));

			}
//			break;
		}
		return String.valueOf(totalArrangements);
	}

	private BigInteger countArrangements(char[] arr, int[] counts, int index) {
		if (index >= arr.length) {
//			if (checkValidUpTo(arr, counts, index)) {
//				System.out.println();
//				for (char c : arr) {
//					System.out.print(c);
//				}
//				System.out.println();
//			}
			return checkValidUpTo(arr, counts, index) ? BigInteger.ONE : BigInteger.ZERO;
		}
		if (arr[index] == '?') {
			arr[index] = '#';
			BigInteger filled = countArrangements(arr, counts, index + 1);
			arr[index] = '.';
			BigInteger notFilled = BigInteger.ZERO;
			if (checkValidUpTo(arr, counts, index)) {
				notFilled = countArrangements(arr, counts, index + 1);
			}
			arr[index] = '?';
			return filled.add(notFilled);
		} else if (arr[index] == '.' && !checkValidUpTo(arr, counts, index)) {
			return BigInteger.ZERO;
		}
		return countArrangements(arr, counts, index + 1);
	}

	private boolean checkValidUpTo(char[] arr, int[] counts, int bound) {
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
		if (countCheck.size() > counts.length || (bound >= arr.length - 1 && countCheck.size() != counts.length)) {
//			printData(arr, countCheck);
			return false;
		}
		for (int i = 0; i < countCheck.size(); i++) {
			if (counts[i] != countCheck.get(i)) {
//				System.out.println("bound " + bound);
//				printData(arr, countCheck);
				return false;
			}
		}
		return true;
	}

	private void printData(char[] arr, List<Integer> countCheck) {
		for (char c : arr) {
			System.out.print(c);
		}
		System.out.println();
		System.out.println(countCheck.stream().map(String::valueOf).collect(Collectors.joining(", ")));
	}

}