package com.league.ranking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class LeagueRankingApp {
    private LeagueRankingApp() {
    }

    public static void main(String[] args) {
        int exitCode = run(args, new LeagueTableCalculator());
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, LeagueTableCalculator calculator) {
        try {
            List<String> lines = readInputLines(args);
            List<StandingRow> standings = calculator.calculate(lines);
            standings.stream()
                .map(StandingRow::format)
                .forEach(System.out::println);
            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return 2;
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            return 1;
        }
    }

    private static List<String> readInputLines(String[] args) throws IOException {
        if (args.length > 1) {
            throw new IllegalArgumentException("Usage: league-ranking [optional-input-file]");
        }

        if (args.length == 1) {
            Path filePath = Path.of(args[0]);
            return Files.readAllLines(filePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
