package sk.tuke.gamestudio.game.SlideaLama.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

public class ConsoleUI {
    private final Field field;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final int TURN_LIMIT_SECONDS = 30;
    private final String GAME_NAME = "SlideaLama";

    // Инициализация сервисов
    private final ScoreService scoreService = new ScoreServiceJDBC();
    private final CommentService commentService = new CommentServiceJDBC();
    private final RatingService ratingService = new RatingServiceJDBC();

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        while (!field.isGameOver()) {
            show();
            handleInputWithTimer();
        }

        show();
        printGameResult();

        handleDatabaseInteraction();
    }

    private void printGameResult() {
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

    private void handleDatabaseInteraction() {
        try {
            System.out.print("Enter winner's name to save score: ");
            String name = reader.readLine();
            if (name == null || name.trim().isEmpty()) name = "Anonymous";

            int winnerScore = Math.max(field.getScoreP1(), field.getScoreP2());
            scoreService.addScore(new Score(GAME_NAME, name, winnerScore, new Date()));
            System.out.println("Score saved!");

            showTopScores();

            System.out.print("Rate the game (1-5) or press Enter to skip: ");
            String ratingInput = reader.readLine();
            if (!ratingInput.isEmpty()) {
                int ratingValue = Integer.parseInt(ratingInput);
                ratingService.setRating(new Rating(GAME_NAME, name, ratingValue, new Date()));
                System.out.println("Rating saved!");
            }

            System.out.print("Leave a comment or press Enter to skip: ");
            String commentText = reader.readLine();
            if (!commentText.isEmpty()) {
                commentService.addComment(new Comment(GAME_NAME, name, commentText, new Date()));
                System.out.println("Comment saved!");
            }

        } catch (Exception e) {
            System.err.println("Error while interacting with database:");
            e.printStackTrace(); // Это выведет полную цепочку ошибок в консоль красным цветом
        }
    }

    private void showTopScores() {
        try {
            List<Score> topScores = scoreService.getTopScores(GAME_NAME);
            System.out.println("\n--- TOP 10 PLAYERS ---");
            for (Score s : topScores) {
                System.out.printf("%-15s %5d  %s%n", s.getPlayer(), s.getPoints(), s.getPlayedOn());
            }
        } catch (ScoreException e) {
            System.err.println("Could not load top scores: " + e.getMessage());
        }
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
        System.out.println("Enter command (t1-5, l1-5, r1-5):");
    }

    private void handleInputWithTimer() {
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                if (elapsed >= TURN_LIMIT_SECONDS) {
                    System.out.println("\n!!! TIME'S UP! !!!");
                    field.skipTurn();
                    return;
                }
                if (System.in.available() > 0) {
                    String input = reader.readLine().toLowerCase().trim();
                    if (input.equals("exit")) System.exit(0);
                    if (input.matches("[tlr][1-5]")) {
                        field.pushBlock(input);
                        return;
                    } else {
                        System.out.println("Invalid command!");
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Input error.");
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
            case BELL:  return "B";
            default:    return "?";
        }
    }
}