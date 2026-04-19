package com.league.ranking;

record StandingRow(int rank, String teamName, int points) {
    String format() {
        String pointLabel = points == 1 ? "pt" : "pts";
        return rank + ". " + teamName + ", " + points + " " + pointLabel;
    }
}
