package com.league.ranking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatchParserTest {
    private final MatchParser parser = new MatchParser();

    @Test
    void parsesValidLine() {
        MatchResult result = parser.parse("Lions 3, Snakes 3");

        assertEquals("Lions", result.homeTeam());
        assertEquals(3, result.homeScore());
        assertEquals("Snakes", result.awayTeam());
        assertEquals(3, result.awayScore());
    }

    @Test
    void parsesWithExtraWhitespace() {
        MatchResult result = parser.parse("  FC Awesome 1,  Tarantulas 0  ");
        assertEquals("FC Awesome", result.homeTeam());
        assertEquals("Tarantulas", result.awayTeam());
    }

    @Test
    void rejectsInvalidFormat() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> parser.parse("Lions vs Snakes"));

        assertEquals(true, exception.getMessage().startsWith("Invalid match format:"));
    }

    @Test
    void rejectsSameTeamPlayingItself() {
        IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> parser.parse("Lions 1, Lions 1"));

        assertEquals(true, exception.getMessage().startsWith("A team cannot play against itself:"));
    }
}
