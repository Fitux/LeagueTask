package com.league.ranking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class LeagueRankingApp {
    // Utility-style class; no instances required.
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
            // Keep I/O concerns in the app layer; calculator stays pure and testable.
            List<String> lines = readInputLines(args);
            List<StandingRow> standings = calculator.calculate(lines);
            standings.stream()
                .map(StandingRow::format)
                .forEach(System.out::println);
            return 0;
        } catch (IllegalArgumentException e) {
            // Usage/validation failures are distinct from I/O failures for scripting ergonomics.
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
            // Path.of keeps separators platform-neutral (Windows/Linux/macOS).
            Path filePath = Path.of(args[0]);
            return Files.readAllLines(filePath);
        }

        // No file provided: read newline-delimited matches from stdin.
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
