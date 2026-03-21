package sk.tuke.gamestudio.game.SlideaLama.core;

public enum Direction {
    TOP('t'),
    LEFT('l'),
    RIGHT('r');

    private final char symbol;

    Direction(char symbol) {
        this.symbol = symbol;
    }

    // Method to convert char from input (t, l, r) to Direction object
    public static Direction fromChar(char c) {
        for (Direction d : values()) {
            if (d.symbol == c) return d;
        }
        throw new IllegalArgumentException("Unknown direction: " + c);
    }
}