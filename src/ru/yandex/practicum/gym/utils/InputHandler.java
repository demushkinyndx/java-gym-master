package ru.yandex.practicum.gym.utils;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt(String message) {
        while (true) {
            if (!message.isEmpty()) {
                System.out.println(message);
            }
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число.");
            }
        }
    }

    public static int getIntInRange(String message, int min, int max) {
        while (true) {
            int value = getInt(message);
            if (value >= min && value <= max) {
                return value;
            } else {
                System.out.println("Введите число в диапазоне от " + min + " до " + max);
            }
        }
    }

    public static String getString(String message) {
        while (true) {
            if (!message.isEmpty()) {
                System.out.println(message);
            }
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input;
            } else {
                System.out.println("Ввод не должен быть пустым.");
            }
        }
    }
}