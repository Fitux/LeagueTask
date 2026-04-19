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

## Continuous Integration

GitHub Actions runs tests on every push and pull request to `main`:

- Java 17 (Temurin)
- Maven dependency cache
- `mvn -B -ntp verify`

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

## Design Decisions

- **Pure domain logic**: `LeagueTableCalculator` is deterministic and side-effect free, which keeps business rules easy to test.
- **CLI as thin I/O layer**: `LeagueRankingApp` only handles input/output and exit codes.
- **Competition ranking**: tied teams share rank and the next rank is skipped (e.g. `1,1,3,3,5`).
- **Stable tie-breaks**: when points tie, teams are sorted alphabetically for predictable output.

## Complexity

- Let `m` be number of matches and `t` be number of teams.
- Parsing and score aggregation: `O(m)`.
- Sorting table output: `O(t log t)`.
- Overall: `O(m + t log t)`.

## Exit Codes

- `0`: success
- `1`: I/O error (e.g. unreadable file)
- `2`: validation/usage error (e.g. malformed line or too many arguments)

## Future Improvements

- Add property-based tests for ranking and points invariants.
- Add support for reporting malformed lines with line numbers.
- Add static analysis gates (e.g. Checkstyle/SpotBugs) to CI.
