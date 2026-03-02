package sk.tuke.gamestudio.game.SlideaLama.core;

import java.util.Random;

public class Field {
    private final int rowCount;
    private final int columnCount;
    private final BlockType[][] grid;
    private GameState state;

    private final int scoreP1 = 0;
    private final int scoreP2 = 0;

    public Field(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.grid = new BlockType[rowCount][columnCount];
        this.state = GameState.PLAYER1_TURN;
        generate();
    }

    // 1. Generating game field without combination 3 in a row
    private void generate() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                BlockType type;
                do {
                    type = BlockType.getRandom();
                } while (isCreatingMatch(r, c, type)); // Check that there aren't three in a row
                grid[r][c] = type;
            }
        }
    }

    // Auxiliary method: checks whether the new block will create a combination during generation.
    private boolean isCreatingMatch(int row, int col, BlockType type) {
        if (row >= 2 && grid[row-1][col] == type && grid[row-2][col] == type) return true;
        if (col >= 2 && grid[row][col-1] == type && grid[row][col-2] == type) return true;
        return false;
    }

    // 2. Checking the status of the game (Is it finished?)
    public boolean isGameOver() {
        // Condition: someone has scored 500 points (simplified)
        if (scoreP1 >= 500 || scoreP2 >= 500) {
            state = GameState.FINISHED;
            return true;
        }
        return false;
    }

    public BlockType getBlock(int row, int col) {
        return grid[row][col];
    }

    public GameState getState() {
        return state;
    }

    public int getScoreP1() { return scoreP1; }
    public int getScoreP2() { return scoreP2; }
}