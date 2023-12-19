package com.noahgeren.adventofcode.days.xxiii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day09 extends Day {
	
	private List<String> lines;

	@Override
	public void loadResources() throws Exception {
		lines  = DataLoader.readLines("day09.txt");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long answer = 0;
		for(String line : lines) {
			String[] split = line.split(" ");
			List<Long> sequence = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
			answer += findNext(sequence, firstPart);
		}
		return String.valueOf(answer);
	}
	
	private long findNext(List<Long> sequence, boolean firstPart) {
		if(sequence.stream().allMatch(l -> l == 0)) {
			return 0;
		}
		List<Long> diff = new ArrayList<>();
		for(int i = 0; i < sequence.size() - 1; i++) {
			diff.add(sequence.get(i + 1) - sequence.get(i));
		}
		if(firstPart) {
			return sequence.get(sequence.size() - 1) + findNext(diff, firstPart);
		}
		return sequence.get(0) - findNext(diff, firstPart);
	}

}
