package com.league.ranking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandingRowTest {
    @Test
    void formatsSinglePointAsPt() {
        StandingRow row = new StandingRow(3, "Snakes", 1);
        assertEquals("3. Snakes, 1 pt", row.format());
    }

    @Test
    void formatsZeroAndMultiplePointsAsPts() {
        StandingRow zeroPoints = new StandingRow(5, "Grouches", 0);
        StandingRow multiplePoints = new StandingRow(1, "Tarantulas", 6);

        assertEquals("5. Grouches, 0 pts", zeroPoints.format());
        assertEquals("1. Tarantulas, 6 pts", multiplePoints.format());
    }
}
