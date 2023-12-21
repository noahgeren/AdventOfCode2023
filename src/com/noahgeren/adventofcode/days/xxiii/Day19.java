package com.noahgeren.adventofcode.days.xxiii;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day19 extends Day {

	Map<String, Workflow> workflows;
	List<Part> parts;

	@Override
	public void loadResources() throws Exception {
		workflows = new HashMap<>();
		parts = new ArrayList<>();
		List<String> lines = DataLoader.readLines("day19.txt");
		boolean onParts = false;
		for (String line : lines) {
			if (line.trim().isEmpty()) {
				onParts = true;
			} else if (onParts) {
				parts.add(new Part(line));
			} else {
				Workflow workflow = new Workflow(line);
				workflows.put(workflow.name, workflow);
			}
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (firstPart) {
			long answer = 0;
			for (Part part : parts) {
				String currentWorkflow = "in";
				while (!currentWorkflow.equals("A") && !currentWorkflow.equals("R")) {
					currentWorkflow = workflows.get(currentWorkflow).applyWorkflow(part);
					if (currentWorkflow.equals("A")) {
						answer += part.getSumOfRatings();
					}
				}
			}
			return String.valueOf(answer);
		}
		// TODO: Solve part 2
		return null;
	}

	private static class Workflow {
		String name;
		List<Condition> conditions;
		String fallback;

		public Workflow(String line) {
			String[] nameAndConditions = line.split("\\{");
			name = nameAndConditions[0];
			String[] unprocessedConditions = nameAndConditions[1].substring(0, nameAndConditions[1].length() - 1)
					.split(",");
			conditions = new ArrayList<>();
			for (int i = 0; i < unprocessedConditions.length - 1; i++) {
				conditions.add(new Condition(unprocessedConditions[i]));
			}
			fallback = unprocessedConditions[unprocessedConditions.length - 1];
		}

		public String applyWorkflow(Part part) {
			for (Condition condition : conditions) {
				String nextWorkflow = condition.applyCondition(part);
				if (nextWorkflow != null) {
					return nextWorkflow;
				}
			}
			return fallback;
		}

		private static class Condition {
			String rating;
			char operator;
			Long value;
			String nextWorkflow;

			public Condition(String condition) {
				rating = String.valueOf(condition.charAt(0));
				operator = condition.charAt(1);
				String[] valueAndNextWorkflow = condition.substring(2).split(":");
				value = Long.valueOf(valueAndNextWorkflow[0]);
				nextWorkflow = valueAndNextWorkflow[1];
			}

			public String applyCondition(Part part) {
				Long ratingValue = part.ratings.get(rating);
				if (operator == '>') {
					if (ratingValue > value) {
						return nextWorkflow;
					}
				} else {
					if (ratingValue < value) {
						return nextWorkflow;
					}
				}
				return null;
			}
		}
	}

	private static class Part {
		Map<String, Long> ratings;

		public Part(String line) {
			ratings = new HashMap<>();
			line = line.substring(1, line.length() - 1);
			String[] tokens = line.split(",");
			for (String token : tokens) {
				String[] rating = token.split("=");
				ratings.put(rating[0], Long.valueOf(rating[1]));
			}
		}

		public long getSumOfRatings() {
			return ratings.values().stream().collect(Collectors.summingLong(a -> a));
		}
	}

}
