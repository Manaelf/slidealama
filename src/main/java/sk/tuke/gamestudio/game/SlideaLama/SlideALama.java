package sk.tuke.gamestudio.game.SlideaLama;

import sk.tuke.gamestudio.game.SlideaLama.core.Field;
import sk.tuke.gamestudio.game.SlideaLama.consoleui.ConsoleUI;

public class SlideALama{
    public static void main(String[] args) {
        // Making field 5x5
        Field field = new Field(5, 5);

        // Создаем интерфейс и передаем ему поле
        ConsoleUI ui = new ConsoleUI(field);

        ui.play();
    }
}