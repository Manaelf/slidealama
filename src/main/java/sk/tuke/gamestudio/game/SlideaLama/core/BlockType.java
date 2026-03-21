package sk.tuke.gamestudio.game.SlideaLama.core;

import java.util.Random;

public enum BlockType {
    LAMA(5, 100),   // 5% chance, 100 points
    SUN(10, 70),    // 10% chance, 70 points
    MOON(15, 40),   // 15% chance, 40 points
    FRUIT(20, 30),  // 20% chance, 30 points
    SNAKE(25, 20),  // 25% chance, 20 points
    BELL(25, 10);   // 25% chance, 10 points

    private final int probability;
    private final int points;

    BlockType(int probability, int points) {
        this.probability = probability;
        this.points = points;
    }

    public int getPoints() { return points; }

    // A method for generating a random block based on weights
    public static BlockType getRandomWithWeights() {
        int randomValue = new Random().nextInt(100); // 0-99
        int cumulativeSum = 0;

        for (BlockType type : values()) {
            cumulativeSum += type.probability;
            if (randomValue < cumulativeSum) {
                return type;
            }
        }
        return BELL; //  Just in case
    }
}