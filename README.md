# League Ranking CLI

A production-ready Java command-line application that computes league standings from match results.

## Requirements

- Java 17+
- Maven 3.9+ (or any recent Maven that supports Java 17)

## Input Format

Each input line must follow:

`<Team A> <scoreA>, <Team B> <scoreB>`

Example:

`Lions 3, Snakes 3`

## Scoring Rules

- Win: 3 points
- Draw: 1 point each team
- Loss: 0 points

Standings are sorted by:

1. points (descending)
2. team name (alphabetical ascending)

Tied points share rank, and the next rank is skipped (competition ranking), e.g. `1,2,3,3,5`.

## Build and Test

```bash
mvn test
```

## Run

### From stdin

```bash
printf "Lions 3, Snakes 3\nTarantulas 1, FC Awesome 0\nLions 1, FC Awesome 1\nTarantulas 3, Snakes 1\nLions 4, Grouches 0\n" | mvn -q exec:java
```

### From a file argument

```bash
mvn -q exec:java -Dexec.args="matches.txt"
```

## Expected Output (Sample)

```text
1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
5. Grouches, 0 pts
```
