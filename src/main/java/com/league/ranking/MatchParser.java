package com.league.ranking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MatchParser {
    private static final Pattern LINE_PATTERN = Pattern.compile("^(.+)\\s(\\d+),\\s(.+)\\s(\\d+)$");

    MatchResult parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Input line cannot be null.");
        }

        String trimmed = line.trim();
        Matcher matcher = LINE_PATTERN.matcher(trimmed);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid match format: " + line);
        }

        String homeTeam = matcher.group(1).trim();
        String awayTeam = matcher.group(3).trim();

        if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
            throw new IllegalArgumentException("Team names cannot be empty: " + line);
        }

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("A team cannot play against itself: " + line);
        }

        int homeScore = Integer.parseInt(matcher.group(2));
        int awayScore = Integer.parseInt(matcher.group(4));

        return new MatchResult(homeTeam, homeScore, awayTeam, awayScore);
    }
}
