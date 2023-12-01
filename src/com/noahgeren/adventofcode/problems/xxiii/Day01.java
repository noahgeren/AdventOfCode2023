package com.noahgeren.adventofcode.problems.xxiii;

import java.io.IOException;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day01 extends Day {

	private static final String[] NUMBERS = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

	private List<String> rows;

	@Override
	public void loadResources() throws IOException {
		rows = DataLoader.readLines("day01.txt");
	}

	@Override
	public String solve(boolean firstPart) {
		long sum = 0;
		for (String row : rows) {
			String firstDigit = "", lastDigit = "";
			for (int i = 0; i < row.length(); i++) {
				final Integer value = getValue(row, i, firstPart);
				if (value != null) {
					firstDigit = value.toString();
					break;
				}
			}
			for (int i = row.length() - 1; i >= 0; i--) {
				final Integer value = getValue(row, i, firstPart);
				if (value != null) {
					lastDigit = value.toString();
					break;
				}
			}
			System.out.println(firstDigit + lastDigit);
			sum += Long.valueOf(firstDigit + lastDigit);
		}
		return String.valueOf(sum);
	}

	public Integer getValue(String str, int index, boolean firstPart) {
		final int c = str.charAt(index) - '0';
		if (c >= 0 && c <= 9) {
			return c;
		}
		if (!firstPart) {
			for (int i = 0; i < NUMBERS.length; i++) {
				int endIndex;
				if ((endIndex = index + NUMBERS[i].length()) <= str.length()
						&& NUMBERS[i].equals(str.substring(index, endIndex))) {
					return i + 1;
				}
			}
		}
		return null;
	}

}
