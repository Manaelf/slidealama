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

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.grid = new BlockType[rowCount][columnCount];
        this.state = GameState.PLAYER1_TURN;
        this.nextBlock = BlockType.getRandom();
        generate();
    }

    private void generate() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                // For testing firts to rows generating empty
                if (r < 2) {
                    grid[r][c] = null;
                    continue;
                }

                BlockType type;
                do {
                    type = BlockType.getRandom();
                } while (isCreatingMatch(r, c, type));
                grid[r][c] = type;
            }
        }
    }

    public void pushBlock(String command) {
        char dirChar = command.charAt(0);
        int index = Character.getNumericValue(command.charAt(1)) - 1;

        if (dirChar == 't') shiftVertical(index);
        else if (dirChar == 'l') shiftHorizontal(index, true);
        else if (dirChar == 'r') shiftHorizontal(index, false);

        this.nextBlock = BlockType.getRandom();
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

    private boolean isCreatingMatch(int row, int col, BlockType type) {
        if (row >= 2 && grid[row-1][col] != null && grid[row-2][col] != null &&
                grid[row-1][col] == type && grid[row-2][col] == type) return true;

        if (col >= 2 && grid[row][col-1] != null && grid[row][col-2] != null &&
                grid[row][col-1] == type && grid[row][col-2] == type) return true;

        return false;
    }

    public boolean isGameOver() {
        if (scoreP1 >= 500 || scoreP2 >= 500) {
            state = GameState.FINISHED;
            return true;
        }

        boolean isFull = true;
        for (int c = 0; c < columnCount; c++) {
            if (grid[0][c] == null) {
                isFull = false;
                break;
            }
        }

        if (isFull) {
            state = GameState.FINISHED;
            return true;
        }

        return false;
    }

    public boolean isFieldFull() {
        for (int c = 0; c < columnCount; c++) {
            if (grid[0][c] == null) return false;
        }
        return true;
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
            for (int r = 0; r < rowCount; r++) {
                for (int c = 0; c < columnCount - 2; c++) {
                    BlockType type = grid[r][c];
                    if (type != null && type == grid[r][c+1] && type == grid[r][c+2]) {
                        toRemove[r][c] = true;
                        toRemove[r][c+1] = true;
                        toRemove[r][c+2] = true;
                        matchFound = true;
                    }
                }
            }

            for (int c = 0; c < columnCount; c++) {
                for (int r = 0; r < rowCount - 2; r++) {
                    BlockType type = grid[r][c];
                    if (type != null && type == grid[r+1][c] && type == grid[r+2][c]) {
                        toRemove[r][c] = true;
                        toRemove[r+1][c] = true;
                        toRemove[r+2][c] = true;
                        matchFound = true;
                    }
                }
            }

            if (matchFound) {
                int blocksRemoved = 0;
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < columnCount; c++) {
                        if (toRemove[r][c]) {
                            grid[r][c] = null;
                            blocksRemoved++;
                        }
                    }
                }

                int points = blocksRemoved * 10;
                if (state == GameState.PLAYER1_TURN) {
                    scoreP1 += points;
                } else {
                    scoreP2 += points;
                }

                applyGravity();
            }

        } while (matchFound);
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