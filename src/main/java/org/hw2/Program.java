package org.hw2;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static final int WIN_COUNT = 4;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {
        do {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
        } while (scanner.next().equalsIgnoreCase("Y"));
    }

    /**
     * Инициализация игрового поля
     */
    static void initialize() {
        fieldSizeY = 5;
        fieldSizeX = 5;

        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    private static void printField() {
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn() {
        int x;
        int y;

        do {
            System.out.printf("Введите координаты хода X и Y (от 1 до %d)\nчерез пробел: ", fieldSizeX);
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) {
                    field[y][x] = DOT_HUMAN;
                    if (checkHorizontal(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkVertical(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkDiagonalUp(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkDiagonalDown(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkDiagonalUpLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkDiagonalDownLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkHorizontalLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkVerticalUp(x, y, DOT_HUMAN, WIN_COUNT)) {
                        field[y][x] = DOT_AI;
                        return;
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y)) {
                    field[y][x] = DOT_HUMAN;
                    if (checkHorizontal(x, y, DOT_HUMAN, WIN_COUNT - 1) ||
                            checkVertical(x, y, DOT_HUMAN, WIN_COUNT - 1) ||
                            checkDiagonalUp(x, y, DOT_HUMAN, WIN_COUNT - 1) ||
                            checkDiagonalDown(x, y, DOT_HUMAN, WIN_COUNT - 1) ||
                            checkDiagonalUpLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkDiagonalDownLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkHorizontalLeft(x, y, DOT_HUMAN, WIN_COUNT) ||
                            checkVerticalUp(x, y, DOT_HUMAN, WIN_COUNT)) {
                        field[y][x] = DOT_AI;
                        return;
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }


        int xx;
        int yy;
        do {
            xx = random.nextInt(fieldSizeX);
            yy = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(xx, yy));
        field[yy][xx] = DOT_AI;
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     */
    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    /**
     * Проверка существования ячейки игрового поля
     */
    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }


    /**
     * Метод проверки состояния игры
     *
     * @param dot фишка игрока
     * @param s   победный слоган
     * @return результат проверки состояния игры
     */
    static boolean checkGameState(char dot, String s) {
        if (checkWin(dot, WIN_COUNT)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    /**
     * Проверка на ничью
     */
    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка победы игрока
     *
     * @param dot фишка игрока
     * @return признак победы
     */
    static boolean checkWin(char dot, int winCount) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (checkHorizontal(x, y, dot, winCount) ||
                        checkVertical(x, y, dot, winCount) ||
                        checkDiagonalUp(x, y, dot, winCount) ||
                        checkDiagonalDown(x, y, dot, winCount)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean checkHorizontal(int x, int y, char dot, int winCount) {
        if (isCellValid(x + winCount - 1, y)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y][x + i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkVertical(int x, int y, char dot, int winCount) {
        if (isCellValid(x, y + winCount - 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y + i][x];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkDiagonalUp(int x, int y, char dot, int winCount) {
        if (isCellValid(x + winCount - 1, y - winCount + 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y - i][x + i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkDiagonalDown(int x, int y, char dot, int winCount) {
        if (isCellValid(x + winCount - 1, y + winCount - 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y + i][x + i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    /**
     * Проверка победы игрока при ходе компьютера
     *
     * @param dot фишка игрока
     * @return признак победы
     */

    static boolean checkHorizontalLeft(int x, int y, char dot, int winCount) {
        if (isCellValid(x - winCount + 1, y)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y][x - i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkVerticalUp(int x, int y, char dot, int winCount) {
        if (isCellValid(x, y - winCount + 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y - i][x];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkDiagonalUpLeft(int x, int y, char dot, int winCount) {
        if (isCellValid(x - winCount + 1, y - winCount + 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y - i][x - i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }

    static boolean checkDiagonalDownLeft(int x, int y, char dot, int winCount) {
        if (isCellValid(x - winCount + 1, y + winCount - 1)) {
            char[] array = new char[winCount];
            for (int i = 0; i < winCount; i++) {
                array[i] = field[y + i][x - i];
            }
            int coincidence = 0;
            for (int i = 0; i < winCount; i++) {
                if (array[i] == dot) {
                    coincidence += 1;
                }
            }
            return coincidence == array.length;
        }
        return false;
    }


}