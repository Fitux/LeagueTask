package com.league.ranking;

record StandingRow(int rank, String teamName, int points) {
    String format() {
        // Output contract: singular/plural points label should be grammatically correct.
        String pointLabel = points == 1 ? "pt" : "pts";
        return rank + ". " + teamName + ", " + points + " " + pointLabel;
    }
}
