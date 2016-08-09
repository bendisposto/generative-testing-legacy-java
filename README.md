# Legacy Kata

This is a prototypical implementation of an idea I had when doing the Trivia kata at the Dortmund Dojo. We used the original version of the trivia game as an oracle to verify that the refactored version of the game does the same thing as the original program.

We used a single test case (implied by the GameRuner class) and ran it with a bunch of different seeds. This leads to several system tests, but they all shared their context, in this case 3 players and a very specific sequence of method calls.

It remembered me of a technique that was used to find nasty bugs in Clojure collections (https://github.com/ztellman/collection-check). The basic idea is that a new collection can be checked against a standard implementation of that collection type. For example, if we want to implement a new set collection type, the tool will randomly create actions that can be used with a set (insert elements, delete elements, check for membership, ...). It will perfom these actions on the new collection and compare the outcome with a standard implementation.

It came to me, that the exact same techique can be applied to our refactoring problem. Instead of hard wiring the sequence into a test, we can automatically generate a runner program that calls the original Game class and the refactored game class and we can check that the outcome is identical.


## Usage

```lein test``` runs the generated tests, comparing the original version of the program with the changed version

## License

Copyright © 2016 Jens Bendisposto
