package sk.tuke.gamestudio.game.SlideaLama.core;

public enum BlockType {
    LAMA, SUN, MOON, FRUIT, SNAKE;

    // Метод для получения случайного типа блока
    public static BlockType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}