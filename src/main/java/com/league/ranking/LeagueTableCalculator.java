package com.league.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class LeagueTableCalculator {
    private final MatchParser parser;

    LeagueTableCalculator() {
        this(new MatchParser());
    }

    LeagueTableCalculator(MatchParser parser) {
        this.parser = parser;
    }

    List<StandingRow> calculate(List<String> rawLines) {
        // Accumulates final points per team across all valid match lines.
        Map<String, Integer> pointsByTeam = new HashMap<>();

        for (String rawLine : rawLines) {
            // Accept sparse input (blank lines) to keep CLI usage forgiving.
            if (rawLine == null || rawLine.isBlank()) {
                continue;
            }

            MatchResult result = parser.parse(rawLine);
            // Ensure both teams appear in the table even if they never earn points.
            pointsByTeam.putIfAbsent(result.homeTeam(), 0);
            pointsByTeam.putIfAbsent(result.awayTeam(), 0);

            // Apply 3/1/0 scoring rules.
            if (result.homeScore() > result.awayScore()) {
                pointsByTeam.computeIfPresent(result.homeTeam(), (teamName, currentPoints) -> currentPoints + 3);
            } else if (result.homeScore() < result.awayScore()) {
                pointsByTeam.computeIfPresent(result.awayTeam(), (teamName, currentPoints) -> currentPoints + 3);
            } else {
                pointsByTeam.computeIfPresent(result.homeTeam(), (teamName, currentPoints) -> currentPoints + 1);
                pointsByTeam.computeIfPresent(result.awayTeam(), (teamName, currentPoints) -> currentPoints + 1);
           
            }
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(pointsByTeam.entrySet());
        sorted.sort(
            // Primary sort: points descending. Secondary sort: team name ascending.
            Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey)
        );

        List<StandingRow> rows = new ArrayList<>();
        int displayedRank = 0;
        // Track previous points to detect tie groups for competition ranking.
        Integer previousPoints = null;

        for (int index = 0; index < sorted.size(); index++) {
            Map.Entry<String, Integer> team = sorted.get(index);
            int points = team.getValue();

            if (previousPoints == null || points != previousPoints) {
                // Competition ranking: when points change, rank becomes position + 1 (1,1,3,3,5...).
                displayedRank = index + 1;
                previousPoints = points;
            }

            rows.add(new StandingRow(displayedRank, team.getKey(), points));
        }

        return rows;
    }
}
