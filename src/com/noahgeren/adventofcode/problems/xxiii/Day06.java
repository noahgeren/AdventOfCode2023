package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day06 extends Day {
	
	List<Race> races = new ArrayList<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day06.txt");
		String[] times = lines.get(0).split(":")[1].trim().split("\\s+");
		String[] records = lines.get(1).split(":")[1].trim().split("\\s+");
		for(int i = 0; i < times.length; i++) {
			races.add(new Race(times[i], records[i]));
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long product = 1;
		String time = "", record = "";
		for(Race race : races) {
			if(firstPart) {
				product *= countWaysToWin(race);
			} else {
				time += race.time;
				record += race.record;
			}
		}
		if(firstPart) {
			return String.valueOf(product);
		}
		return String.valueOf(countWaysToWin(new Race(time, record)));
	}
	
	private long countWaysToWin(Race race) {
		long ways = 0;
		for(int t = 0; t < race.time; t++) {
			if(t * (race.time - t) > race.record) {
				ways++;
			}
		}
		return ways;
	}
	
	private class Race {
		long time;
		long record;
		
		public Race(String time, String record) {
			this.time = Long.valueOf(time);
			this.record = Long.valueOf(record);
		}
	}

}
