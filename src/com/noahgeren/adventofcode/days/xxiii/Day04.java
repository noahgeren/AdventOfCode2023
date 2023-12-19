package com.noahgeren.adventofcode.days.xxiii;

import java.util.Arrays;
import java.util.List;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day04 extends Day {

	List<String> lines;

	@Override
	public void loadResources() throws Exception {
		lines = DataLoader.readLines("day04.txt");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long totalPoints = 0;
		long[] copies = new long[lines.size()];
		Arrays.fill(copies, 1);
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			long points = 0;
			String[] splitLine = line.split(":")[1].split("\\|");
			String[] winners = splitLine[0].trim().split(" ");
			String[] numbers = splitLine[1].trim().split(" ");
			int matches = 0;
			for (String number : numbers) {
				if (contains(winners, number)) {
					if (firstPart) {
						if (points == 0) {
							points = 1;
						} else {
							points *= 2;
						}
					} else {
						matches++;
					}
				}
			}
			if(!firstPart) {
				for(int j = 1; j <= matches; j++) {
					copies[i + j] += copies[i];
				}
			}
			
			totalPoints += points;
		}
		if(!firstPart) {
			for(long copyCount : copies) {
				totalPoints += copyCount;
			}
		}
		return String.valueOf(totalPoints);
	}

	private boolean contains(String[] arr, String value) {
		if (value.trim().isEmpty())
			return false;
		for (String str : arr) {
			if (value.equals(str)) {
				return true;
			}
		}
		return false;
	}

}
