package com.noahgeren.adventofcode.days.xxiii;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day15 extends Day {

	private String[] sequence;

	@Override
	public void loadResources() throws Exception {
		sequence = DataLoader.readLines("day15.txt").get(0).split(",");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (firstPart) {
			return solveFirstPart();
		}
		return solveSecondPart();
	}

	private String solveFirstPart() {
		long sum = 0;
		for (String str : sequence) {
			sum += hash(str);
		}
		return String.valueOf(sum);
	}

	private String solveSecondPart() {
		long sum = 0;
		HashMap<Long, LinkedHashMap<String, Long>> map = new HashMap<>();
		for (String str : sequence) {
			if (str.endsWith("-")) {
				String label = str.substring(0, str.length() - 1);
				long hash = hash(label);
				map.computeIfAbsent(hash, (x) -> new LinkedHashMap<String, Long>()).remove(label);
			} else {
				String label = str.substring(0, str.length() - 2);
				long hash = hash(label);
				long focalLength = str.charAt(str.length() - 1) - '0';
				map.computeIfAbsent(hash, (x) -> new LinkedHashMap<String, Long>()).put(label, focalLength);
			}
		}
		for (Entry<Long, LinkedHashMap<String, Long>> entry : map.entrySet()) {
			long box = entry.getKey() + 1;
			LinkedHashMap<String, Long> contents = entry.getValue();
			long i = 1;
			for (Long focalLength : contents.values()) {
				sum += box * i * focalLength;
				i++;
			}
		}
		return String.valueOf(sum);
	}

	private long hash(String str) {
		long hash = 0;
		for (char c : str.toCharArray()) {
			hash += c;
			hash *= 17;
			hash %= 256;
		}
		return hash;
	}

}
