SlideaLama

SlideaLama is a console-based logic game for two players, inspired by classic match-3 puzzles but featuring a unique row-sliding mechanic and gravity.

Players take turns pushing blocks into a 5x5 game board. 
The goal is to align three or more identical blocks horizontally or vertically. The first player to reach 1000 points wins.

 Dynamic Board: When matches are removed, blocks above them fall down due to gravity, potentially triggering chain reactions (combos).
 Variable Block Values: Each block type has its own rarity and point value.
 Turn Timer: Players have a limited amount of time to make a move before their turn is automatically skipped.
 Debug Mode: A dedicated mode to test specific scenarios such as victory, draws, or board filling.


Launching the Game

Run the Main class. You will be prompted to choose a mode:
 1. Normal Mode - Standard gameplay.
 2. Debug Mode - Quick access to specific test scenarios.

Controls

Commands are entered in the format [direction][number]:
 t1-5 (Top) - Push a block from the top into columns 1-5.
 l1-5 (Left) - Push a block from the left into rows 1-5.
 r1-5 (Right) - Push a block from the right into rows 1-5.
 exit - Close the game.

Block Value Table
| Symbol | Type  | Points | Probability |
|    L   | LAMA  | 100    | 5%          |
|    S   | SUN   | 70     | 10%         |
|    M   | MOON  | 40     | 15%         |
|    F   | FRUIT | 30     | 20%         |
|    X   | SNAKE | 20     | 25%         |
|    B   | BELL  | 10     | 25%         |



Scoring Rules

Points are awarded immediately after a move and subsequent gravity shifts. 
If a move results in multiple matches (combos), the points are accumulated. 
The total points for a single match are calculated as the sum of the values of all removed blocks.

Video pre druhú obhajobu - https://youtu.be/vxP-QtQ88KI