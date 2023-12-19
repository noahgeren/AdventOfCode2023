package com.noahgeren.adventofcode.days.xxiii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day05 extends Day {

	Map<String, long[]> types = new HashMap<>();
	Map<String, List<SourceDestinationRange>> seedMap = new HashMap<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day05.txt");
		String sourceType = null, destinationType = null;
		for (String line : lines) {
			if (line.trim().isEmpty())
				continue;
			if (line.startsWith("seeds:")) {
				String[] seedStr = line.substring(6).trim().split(" ");
				long[] seeds = new long[seedStr.length];
				for (int i = 0; i < seeds.length; i++) {
					seeds[i] = Long.valueOf(seedStr[i]);
				}
				types.put("seed", seeds);
			} else if (line.endsWith("map:")) {
				String[] types = line.split(" ")[0].split("-to-");
				sourceType = types[0];
				destinationType = types[1];
				seedMap.put(sourceType, new ArrayList<>());
			} else {
				String[] map = line.split(" ");
				seedMap.get(sourceType).add(new SourceDestinationRange(destinationType, Long.valueOf(map[0]),
						Long.valueOf(map[1]), Long.valueOf(map[2])));
			}
		}
	}

	@Override
	public void reset() throws Exception {
		types = new HashMap<>();
		seedMap = new HashMap<>();
		loadResources();
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (!firstPart) {
			for(String key : seedMap.keySet()) {
				fillGaps(seedMap.get(key));
				Collections.sort(seedMap.get(key), (a, b) -> Long.compare(a.destinationStart, b.destinationStart));
			}
			return String.valueOf(solveSecondPart("location", 0, Long.MAX_VALUE));
		}
		String type = "seed";
		while (!type.equals("location")) {
			List<SourceDestinationRange> map = seedMap.get(type);
			long[] typeValues = types.get(type);
			long[] newTypeValues = Arrays.copyOf(typeValues, typeValues.length);
			for (int i = 0; i < typeValues.length; i++) {
				for (SourceDestinationRange range : map) {
					long diff = typeValues[i] - range.sourceStart;
					if (diff >= 0 && diff < range.length) {
						newTypeValues[i] = range.destinationStart + diff;
					}
				}
			}
			type = map.get(0).destinationType;
			types.put(type, newTypeValues);
		}
		long[] locations = types.get("location");
		long lowest = Arrays.stream(locations).min().getAsLong();
		return String.valueOf(lowest);
	}

	private Long solveSecondPart(String type, long min, long max) {
		if (type.equals("seed")) {
			long[] seeds = types.get("seed");
			Long minSeed = null;
			for (int i = 0; i < seeds.length; i += 2) {
				if (hasOverlap(min, max, seeds[i], seeds[i] + seeds[i + 1])) {
					long seed = Math.max(min, seeds[i]);
					if(minSeed == null) {
						minSeed = seed;
					} else {
						minSeed = Math.min(minSeed, seed);
					}
				}
			}
			return minSeed;
		}
		for (Entry<String, List<SourceDestinationRange>> entry : seedMap.entrySet()) {
			if (!entry.getValue().get(0).destinationType.equals(type))
				continue;
			for (SourceDestinationRange range : entry.getValue()) {
				long newMin = Math.max(range.sourceStart, min - range.destinationStart + range.sourceStart);
				long newMax = Math.min(range.sourceStart + range.length, max - range.destinationStart + range.sourceStart);
				if (newMin > newMax) {
					continue;
				}
				Long value = solveSecondPart(entry.getKey(), newMin, newMax);
				if (value != null) {
					return range.destinationStart + (value - range.sourceStart);
				}
			}
		}
		return null;
	}

	private void fillGaps(List<SourceDestinationRange> ranges) {
		Collections.sort(ranges, (a, b) -> Long.compare(a.sourceStart, b.sourceStart));
		long start = 0;
		String type = ranges.get(0).destinationType;
		List<SourceDestinationRange> toAdd = new ArrayList<>();
		for (SourceDestinationRange range : ranges) {
			if (range.sourceStart > start) {
				toAdd.add(new SourceDestinationRange(type, start, start, range.sourceStart - start));
			}
			start = range.sourceStart + range.length;
		}
		toAdd.add(new SourceDestinationRange(type, start, start, Long.MAX_VALUE - start));
		ranges.addAll(toAdd);
	}

	private boolean hasOverlap(long aStart, long aEnd, long bStart, long bEnd) {
		if (bStart < aStart) {
			long temp = aStart;
			aStart = bStart;
			bStart = temp;
			temp = aEnd;
			aEnd = bEnd;
			bEnd = temp;
		}
		return bStart > aStart && bStart < aEnd;
	}

	private class SourceDestinationRange {
		String destinationType;
		long destinationStart;
		long sourceStart;
		long length;

		public SourceDestinationRange(String destinationType, long destinationStart, long sourceStart, long length) {
			super();
			this.destinationType = destinationType;
			this.destinationStart = destinationStart;
			this.sourceStart = sourceStart;
			this.length = length;
		}
	}

}
