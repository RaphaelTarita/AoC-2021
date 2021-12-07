![](https://img.shields.io/badge/day%20📅-7-yellow)
![](https://img.shields.io/badge/stars%20⭐-14-blue)
![](https://img.shields.io/badge/days%20completed-7-green)

# AoC-2021

My solutions for the Advent of Code 2021 (in Kotlin)

### Contents:

- input: All input files, named as `day${day-of-month}.txt`
- output: Output files containing results (and possibly benchmarks) for the different days. Named
  either `day${day-of-month}.txt` for a single day or `days.txt`for all outputs in one file
- src/main/kotlin: Kotlin source root
    - io.github.raphaeltarita: packagae root
        - days: package containing all AoC day solutions, beginning with a (fallback) Day 0 and going to Day 25.
          Named `Day${day-of-month}.kt`
        - structure: package containing the infrastructure which defines how AoC Day challenges are defined, executed,
          benchmarked, outputted and printed to files
            - fmt: utility classes helping me to format the outputs of AoC challenges
        - util: package for `.kt` files which contain top-level / extension utilities
            - `internal.kt`: internal utilites that are useful for maintaining the infrastructure
            - `helpers.kt`: a big collection of general-purpose helpers that I collected from various projects of mine
        - `main.kt`: contains the main function which will execute the current AoC day challenge
- other top-level files: build files, license, etc.
