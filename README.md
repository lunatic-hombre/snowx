SnowX Threat Scanner
====================

This project is the home to a basic command-line Java application 
for scanning 2D binary grids for potential threats using fuzzy logic.
Specifically, it is intended to detect HP Ships and Torpedos from 
the enemy Rejectos.

## Features

- Parallel computation, so you can use *EVERY CPU CORE* to find those Rejectos *FAST*.
- Configurable file formatting, threats detection, matching threshold, etc.
- Awesome coloured result formatting.

## Prerequisites

- Java 8 (or above)
- Maven

## Running

```bash
./run.sh filename
```

Where `filename` can any text file that has 
a 2D grid of spaces and +'s.  By default, the application 
will use `TestData.snw` from the project root.

## Building (and testing)

```bash
mvn package
```

This will run automatically the first time you execute `./run.sh`.

To run tests with coverage:

```bash
mvn verify
```

Then check out the coverage under `target/site/jacoco/index.html`.

## Configuration

All configurable properties are located under `application.properties` 
under the project root.

## Assumptions

- Input files are in grid formats, with + signs for positive points in the grid.
- Similarity to target is determined by % match of bits, and anything greater 
than a given threshold is considered a hit.

## How it works

Given a search grid and an arbitary number of targets to search for:
1. Convert the target into a binary string (big integer).
2. Read every possible block of same size in the search grid.
3. Convert each block into binary string then: 
    1. Using XOR, get the ratio of matching vs non-matching bits with target.
4. Using an arbitrary threshold, decide if the matching ratio can be considered a hit. 