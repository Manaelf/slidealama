package sk.tuke.gamestudio.game.SlideaLama.consoleui;

import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.game.SlideaLama.core.BlockType;
import sk.tuke.gamestudio.game.SlideaLama.core.GameState;

import java.util.Scanner;

public class ConsoleUI {
    private final Field field;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(Field field){
        this.field = field;
    }
    public void play(){
        System.out.println("--- Welcome to Slide A Lama Game ---");

        while (field.getState() != GameState.FINISHED) {
            show();
            handleInput();
        }

        System.out.println("Game Over!");
        System.out.println("Score Player1: " + field.getScoreP1());
        System.out.println("Score Player2: " + field.getScoreP2());
    }
    public void show(){
        System.out.println("\nCurrent score: P1 [" + field.getScoreP1() + "] | P2 |" + field.getScoreP2() + "]");
        System.out.println("Move: " + field.getState());
        System.out.println("-------------------------");
        for (int row = 0; row<5; row++){
            System.out.print("| ");
            for(int column = 0; column < 5; column++){
                System.out.print(getSymbol(field.getBlock(row,column)) + " | ");
            }
            System.out.println(" ");
        }
        System.out.println("-------------------------");
        System.out.println("Введите команду (например: 'top 2' или 'exit'):");
    }
    private void handleInput() {
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("exit")) {
            System.exit(0);
        }

        // Пока мы не написали метод pushBlock, просто имитируем чтение
        System.out.println("> Вы ввели: " + input + " (Логика сдвига будет в следующем шаге)");
    }
    private String getSymbol(BlockType type) {
        switch (type) {
            case LAMA:  return "L";
            case SUN:   return "S";
            case MOON:  return "M";
            case FRUIT: return "F";
            case SNAKE: return "X";
            default:    return " ";
        }
    }
}


