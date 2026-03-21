package sk.tuke.gamestudio.game.SlideaLama.core;

import java.util.Random;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final BlockType[][] grid;
    private GameState state;
    private BlockType nextBlock;

    private int scoreP1 = 0;
    private int scoreP2 = 0;

    private boolean firstMoveMade = false;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.grid = new BlockType[rowCount][columnCount];
        this.state = GameState.PLAYER1_TURN;
        this.nextBlock = BlockType.getRandomWithWeights();
        generate();
    }

    private void generate() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                BlockType type;
                // We're trying to find a block that doesn't form a “three in a row”
                do {
                    type = BlockType.getRandomWithWeights();
                } while (isCreatingMatch(r, c, type));

                grid[r][c] = type;
            }
        }
    }

    public void pushBlock(String command) {
        Direction dir = Direction.fromChar(command.charAt(0));
        int index = Character.getNumericValue(command.charAt(1)) - 1;

        switch (dir) {
            case TOP:    shiftVertical(index); break;
            case LEFT:   shiftHorizontal(index, true); break;
            case RIGHT:  shiftHorizontal(index, false); break;
        }

        this.nextBlock = BlockType.getRandomWithWeights();
        this.state = (state == GameState.PLAYER1_TURN) ? GameState.PLAYER2_TURN : GameState.PLAYER1_TURN;

        applyGravity();
        resolveMatches();
    }

    private void shiftVertical(int col) {
        // Find the first empty cell from top to bottom
        int emptyIndex = -1;
        for (int r = 0; r < rowCount; r++) {
            if (grid[r][col] == null) {
                emptyIndex = r;
                break;
            }
        }

        if (emptyIndex != -1) {
            // If there is a gap, move everything only up to it.
            for (int r = emptyIndex; r > 0; r--) {
                grid[r][col] = grid[r-1][col];
            }
        } else {
            // If the row is full, move everything and push out the bottom block
            for (int r = rowCount - 1; r > 0; r--) {
                grid[r][col] = grid[r-1][col];
            }
        }
        grid[0][col] = nextBlock; // Inserting a new block
    }

    private void shiftHorizontal(int row, boolean fromLeft) {
        if (fromLeft) {
            // Looking for emptiness from left to right
            int emptyIndex = -1;
            for (int c = 0; c < columnCount; c++) {
                if (grid[row][c] == null) {
                    emptyIndex = c;
                    break;
                }
            }

            if (emptyIndex != -1) {
                // Move to the right until empty
                for (int c = emptyIndex; c > 0; c--) {
                    grid[row][c] = grid[row][c-1];
                }
            } else {
                // Move everything to the right, pushing out the one on the far right
                for (int c = columnCount - 1; c > 0; c--) {
                    grid[row][c] = grid[row][c-1];
                }
            }
            grid[row][0] = nextBlock;

        } else {
            // Looking for emptiness from right to left
            int emptyIndex = -1;
            for (int c = columnCount - 1; c >= 0; c--) {
                if (grid[row][c] == null) {
                    emptyIndex = c;
                    break;
                }
            }

            if (emptyIndex != -1) {
                // Move to the left until empty
                for (int c = emptyIndex; c < columnCount - 1; c++) {
                    grid[row][c] = grid[row][c+1];
                }
            } else {
                // Move everything to the left, pushing out the leftmost one
                for (int c = 0; c < columnCount - 1; c++) {
                    grid[row][c] = grid[row][c+1];
                }
            }
            grid[row][columnCount - 1] = nextBlock;
        }
    }

    // Check whether a new block at position [row, col] will create a line
    private boolean isCreatingMatch(int row, int col, BlockType type) {
        // Check horizontally (look only to the left, since we're filling in from left to right)
        if (col >= 2) {
            if (grid[row][col-1] == type && grid[row][col-2] == type) {
                return true;
            }
        }
        // Check vertically (look only upward, since we're filling in from top to bottom)
        if (row >= 2) {
            if (grid[row-1][col] == type && grid[row-2][col] == type) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        if (scoreP1 >= 1000 || scoreP2 >= 1000) {
            state = GameState.FINISHED;
            return true;
        }
        return false;
    }

    public void applyGravity() {
        // Go through each column separately.
        for (int c = 0; c < columnCount; c++) {

            int emptySpot = rowCount - 1; // Start looking for empty space from the very bottom.

            // Go along the column from bottom to top
            for (int r = rowCount - 1; r >= 0; r--) {
                // If we found a block (not an empty cell)
                if (grid[r][c] != null) {
                    // If the empty space is lower than the current block, the block "falls"
                    if (emptySpot != r) {
                        grid[emptySpot][c] = grid[r][c]; // Move the block down
                        grid[r][c] = null;               // We are freeing up the old place
                    }
                    // Move the empty space pointer up one cell
                    emptySpot--;
                }
            }
        }
    }

    private void resolveMatches() {
        boolean matchFound;
        do {
            matchFound = false;
            boolean[][] toRemove = new boolean[rowCount][columnCount];

            // Horizontal check
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount - 2; c++) {
                    BlockType type = grid[r][c];
                    if (type != null && type == grid[r][c+1] && type == grid[r][c+2]) {
                        toRemove[r][c] = toRemove[r][c+1] = toRemove[r][c+2] = true;
                        matchFound = true;
                    }
                }
            }

            // Vertical check
            for (int c = 0; c < columnCount; c++) {
                for (int r = 0; r < rowCount - 2; r++) {
                    BlockType type = grid[r][c];
                    if (type != null && type == grid[r+1][c] && type == grid[r+2][c]) {
                        toRemove[r][c] = toRemove[r+1][c] = toRemove[r+2][c] = true;
                        matchFound = true;
                    }
                }
            }

            if (matchFound) {
                int turnPoints = 0;
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < columnCount; c++) {
                        if (toRemove[r][c]) {
                            turnPoints += grid[r][c].getPoints();
                            grid[r][c] = null;
                        }
                    }
                }

                // Add points to current player (state still points to current player before flip)
                if (state == GameState.PLAYER1_TURN) scoreP1 += turnPoints;
                else scoreP2 += turnPoints;

                applyGravity();
            }
        } while (matchFound);
    }

    // Method to pass the turn if time is up
    public void skipTurn() {
        // Change player state without making a move
        this.state = (state == GameState.PLAYER1_TURN) ? GameState.PLAYER2_TURN : GameState.PLAYER1_TURN;
        // Generate a new next block for the next player
        this.nextBlock = BlockType.getRandomWithWeights();
    }

    public void setupLossScenario() {
        this.scoreP1 = 1000;
        this.scoreP2 = 500;
        isGameOver();
    }

    public void setupDrawScenario() {
        this.scoreP1 = 1000;
        this.scoreP2 = 1000;
        isGameOver();
    }

    public BlockType getNextBlock() {
        return nextBlock;
    }
    public BlockType getBlock(int row, int col) {
        return grid[row][col];
    }
    public GameState getState() {
        return state;
    }
    public int getScoreP1() {
        return scoreP1;
    }
    public int getScoreP2() {
        return scoreP2;
    }
}