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
        Map<String, Integer> pointsByTeam = new HashMap<>();

        for (String rawLine : rawLines) {
            if (rawLine == null || rawLine.isBlank()) {
                continue;
            }

            MatchResult result = parser.parse(rawLine);
            pointsByTeam.putIfAbsent(result.homeTeam(), 0);
            pointsByTeam.putIfAbsent(result.awayTeam(), 0);

            if (result.homeScore() > result.awayScore()) {
                pointsByTeam.computeIfPresent(result.homeTeam(), (k, v) -> v + 3);
            } else if (result.homeScore() < result.awayScore()) {
                pointsByTeam.computeIfPresent(result.awayTeam(), (k, v) -> v + 3);
            } else {
                pointsByTeam.computeIfPresent(result.homeTeam(), (k, v) -> v + 1);
                pointsByTeam.computeIfPresent(result.awayTeam(), (k, v) -> v + 1);
            }
        }

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(pointsByTeam.entrySet());
        sorted.sort(
            Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey)
        );

        List<StandingRow> rows = new ArrayList<>();
        int displayedRank = 0;
        Integer previousPoints = null;

        for (int index = 0; index < sorted.size(); index++) {
            Map.Entry<String, Integer> team = sorted.get(index);
            int points = team.getValue();

            if (previousPoints == null || points != previousPoints) {
                displayedRank = index + 1;
                previousPoints = points;
            }

            rows.add(new StandingRow(displayedRank, team.getKey(), points));
        }

        return rows;
    }
}
