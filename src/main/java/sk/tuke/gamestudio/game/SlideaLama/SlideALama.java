package sk.tuke.gamestudio.game.SlideaLama;

import sk.tuke.gamestudio.game.SlideaLama.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import java.util.Scanner;

public class SlideALama {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select mode:");
        System.out.println("1. Normal Mode");
        System.out.println("2. Debug Mode");

        String choice = scanner.nextLine();
        Field field = new Field(5, 5);
        ConsoleUI ui = new ConsoleUI(field);

        if (choice.equals("2")) {
            System.out.println("\n--- DEBUG MENU ---");
            System.out.println("1. Test Player 2 loses (Score check)");
            System.out.println("2. Test Draw scenario");

            String debugChoice = scanner.nextLine();
            switch (debugChoice) {
                case "1": field.setupLossScenario(); break;
                case "2": field.setupDrawScenario(); break;
            }
        }

        ui.play();
    }
}