package sk.tuke.gamestudio.game.SlideaLama.consoleui;

import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.game.SlideaLama.core.GameState;

public class ConsoleUI {
    private final Field field;
    private final java.util.Scanner scanner = new java.util.Scanner(System.in);

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        while (!field.isGameOver()) {
            show();
            handleInput();
        }

        show();
        System.out.println("\n****************************************");
        System.out.println("             GAME OVER!              ");
        System.out.println("****************************************");

        if (field.isFieldFull()) {
            System.out.println("There are no cells left!");
        } else {
            System.out.println("One of the players has reached the point limit!");
        }
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

    private void handleInput() {
        String input = scanner.nextLine().toLowerCase().trim();

        if (input.matches("[tlr][1-5]")) {
            field.pushBlock(input);
        } else if (input.equals("exit")) {
            System.exit(0);
        } else {
            System.out.println("!!! Error, enter the correct command (for example, t2) !!!");
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
            default:    return "?";
        }
    }
}