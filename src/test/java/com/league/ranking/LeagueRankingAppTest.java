package com.league.ranking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeagueRankingAppTest {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final java.io.InputStream originalIn = System.in;

    @AfterEach
    void restoreSystemStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    void readsFromStdinWhenNoFileArgument() {
        String input = String.join("\n",
            "Lions 3, Snakes 3",
            "Lions 1, FC Awesome 1"
        );

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        int code = LeagueRankingApp.run(new String[0], new LeagueTableCalculator());

        String output = stdout.toString(StandardCharsets.UTF_8).trim();
        assertEquals(0, code);
        assertEquals(String.join("\n",
            "1. Lions, 2 pts",
            "2. FC Awesome, 1 pt",
            "2. Snakes, 1 pt"
        ), output);
    }

    @Test
    void readsFromInputFileWhenArgumentProvided() throws Exception {
        Path tempFile = Files.createTempFile("league-input", ".txt");
        Files.writeString(tempFile, String.join("\n",
            "Tarantulas 1, FC Awesome 0",
            "Lions 1, FC Awesome 1"
        ));

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));

        int code = LeagueRankingApp.run(new String[]{tempFile.toString()}, new LeagueTableCalculator());

        String output = stdout.toString(StandardCharsets.UTF_8).trim();
        assertEquals(0, code);
        assertEquals(String.join("\n",
            "1. Tarantulas, 3 pts",
            "2. FC Awesome, 1 pt",
            "2. Lions, 1 pt"
        ), output);
    }

    @Test
    void returnsErrorForInvalidInput() {
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream("Bad line".getBytes(StandardCharsets.UTF_8)));

        int code = LeagueRankingApp.run(new String[0], new LeagueTableCalculator());

        String error = stderr.toString(StandardCharsets.UTF_8);
        assertEquals(2, code);
        assertTrue(error.contains("Error: Invalid match format"));
    }

    @Test
    void returnsUsageErrorWhenTooManyArgumentsProvided() {
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));

        int code = LeagueRankingApp.run(new String[]{"a.txt", "b.txt"}, new LeagueTableCalculator());

        String error = stderr.toString(StandardCharsets.UTF_8);
        assertEquals(2, code);
        assertTrue(error.contains("Usage: league-ranking [optional-input-file]"));
    }

    @Test
    void returnsIoErrorWhenInputFileDoesNotExist() {
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        System.setErr(new PrintStream(stderr, true, StandardCharsets.UTF_8));

        int code = LeagueRankingApp.run(new String[]{"this-file-does-not-exist.txt"}, new LeagueTableCalculator());

        String error = stderr.toString(StandardCharsets.UTF_8);
        assertEquals(1, code);
        assertTrue(error.contains("I/O error:"));
    }

    @Test
    void printsNothingAndSucceedsForEmptyStdin() {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stdout, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream(new byte[0]));

        int code = LeagueRankingApp.run(new String[0], new LeagueTableCalculator());

        String output = stdout.toString(StandardCharsets.UTF_8).trim();
        assertEquals(0, code);
        assertEquals("", output);
    }
}
