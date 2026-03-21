package sk.tuke.gamestudio.game.SlideaLama.consoleui;

import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.game.SlideaLama.core.GameState;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleUI {
    private final Field field;
    private final java.util.Scanner scanner = new java.util.Scanner(System.in);
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final int TURN_LIMIT_SECONDS = 30; // Time limit for one turn

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        while (!field.isGameOver()) {
            show();
            handleInputWithTimer();
        }

        show();
        System.out.println("\n****************************************");
        System.out.println("             GAME OVER!              ");
        System.out.println("****************************************");

        int p1 = field.getScoreP1();
        int p2 = field.getScoreP2();

        System.out.println("GAME SCORE: P1 [" + p1 + "] : P2 [" + p2 + "]");

        if (p1 > p2) {
            System.out.println("RESULT: Player 1 has won! Congratulations!");
        } else if (p2 > p1) {
            System.out.println("RESULT: Player 2 has won! Congratulations!");
        } else {
            System.out.println("RESULT: Draw!");
        }
        System.out.println("****************************************");
    }

    public void show() {
        System.out.println("\n========================================");
        System.out.println("Score: P1 [" + field.getScoreP1() + "] | P2 [" + field.getScoreP2() + "]");
        System.out.println("Current move: " + field.getState());
        System.out.println("Next Block: [" + getSymbol(field.getNextBlock()) + "]");
        System.out.println("========================================");


        System.out.println("    1   2   3   4   5");
        System.out.println("  ---------------------");
        for (int r = 0; r < 5; r++) {
            System.out.print((r + 1) + " | ");
            for (int c = 0; c < 5; c++) {
                System.out.print(getSymbol(field.getBlock(r, c)) + " | ");
            }
            System.out.println();
        }
        System.out.println("  ---------------------");
        System.out.println("Enter the command (t1-5, l1-5, r1-5):");
    }

    private void handleInputWithTimer() {
        long startTime = System.currentTimeMillis();
        System.out.println("Time limit: " + TURN_LIMIT_SECONDS + " seconds!");

        try {
            while (true) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;

                // Check if time is exceeded
                if (elapsed >= TURN_LIMIT_SECONDS) {
                    System.out.println("\n!!! TIME'S UP! Switching turn. !!!");
                    field.skipTurn();
                    return;
                }

                // Non-blocking check for input
                if (System.in.available() > 0) {
                    String input = reader.readLine().toLowerCase().trim();
                    if (input.equals("exit")) System.exit(0);

                    if (input.matches("[tlr][1-5]")) {
                        field.pushBlock(input);
                        return;
                    } else {
                        System.out.println("Invalid command. Try t1-5, l1-5, or r1-5:");
                    }
                }

                // Small sleep to prevent high CPU usage
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Input error occurred.");
        }
    }

    private String getSymbol(BlockType type) {
        if (type == null) return " ";
        switch (type) {
            case LAMA:  return "L";
            case SUN:   return "S";
            case MOON:  return "M";
            case FRUIT: return "F";
            case SNAKE: return "X";
            case BELL: return "B";
            default:    return "?";
        }
    }
}