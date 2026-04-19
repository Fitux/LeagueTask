package com.league.ranking;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeagueTableCalculatorTest {
    private final LeagueTableCalculator calculator = new LeagueTableCalculator();

    @Test
    void calculatesExpectedSampleOutput() {
        List<String> input = List.of(
            "Lions 3, Snakes 3",
            "Tarantulas 1, FC Awesome 0",
            "Lions 1, FC Awesome 1",
            "Tarantulas 3, Snakes 1",
            "Lions 4, Grouches 0"
        );

        List<String> actual = calculator.calculate(input).stream()
            .map(StandingRow::format)
            .toList();

        assertEquals(List.of(
            "1. Tarantulas, 6 pts",
            "2. Lions, 5 pts",
            "3. FC Awesome, 1 pt",
            "3. Snakes, 1 pt",
            "5. Grouches, 0 pts"
        ), actual);
    }

    @Test
    void ordersTiedTeamsAlphabetically() {
        List<String> input = List.of(
            "Zebras 1, Alphas 1",
            "Monkeys 2, Bears 2"
        );

        List<StandingRow> rows = calculator.calculate(input);

        assertEquals(4, rows.size());
        assertEquals("Alphas", rows.get(0).teamName());
        assertEquals("Bears", rows.get(1).teamName());
        assertEquals("Monkeys", rows.get(2).teamName());
        assertEquals("Zebras", rows.get(3).teamName());
        assertEquals(1, rows.get(0).rank());
        assertEquals(1, rows.get(3).rank());
    }

    @Test
    void skipsRanksAfterTies() {
        List<String> input = List.of(
            "A 1, B 0",
            "C 1, D 0"
        );

        List<StandingRow> rows = calculator.calculate(input);

        assertEquals(1, rows.get(0).rank());
        assertEquals(1, rows.get(1).rank());
        assertEquals(3, rows.get(2).rank());
        assertEquals(3, rows.get(3).rank());
    }
}
