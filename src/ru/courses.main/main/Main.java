package ru.courses.main.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

    public class Main {

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            int attemptCount = 0;

            while (true) {
                System.out.printf("Введите путь к файлу: ", ++attemptCount);
                String path = scanner.nextLine();

                File file = new File(path);

                if (!file.exists()) {
                    System.out.println("Файла или директории не существует.");
                    continue;
                }

                if (file.isDirectory()) {
                    System.out.println("Указан путь к директории, а не к файлу.");
                    continue;
                }

                System.out.println("Путь к файлу указан верно. Количество затраченных попыток: " + attemptCount);

                try (FileReader fileReader = new FileReader(path);
                     BufferedReader reader = new BufferedReader(fileReader)) {

                    String line;
                    int totalLines = 0;
                    int maxLength = 0;
                    int minLength = Integer.MAX_VALUE;

                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if (length > 1024) {
                            throw new LineTooLongException("В файле найдена строка длиной более 1024 символов.");
                        }

                        totalLines++;
                        if (length > maxLength) maxLength = length;
                        if (length < minLength) minLength = length;
                    }

                    System.out.println("Общее количество строк: " + totalLines);
                    System.out.println("Длина самой длинной строки: " + maxLength);
                    System.out.println("Длина самой короткой строки: " + minLength);

                } catch (IOException | LineTooLongException ex) {
                    ex.printStackTrace();
                }

                break;
            }

            scanner.close();
        }
    }

    class LineTooLongException extends RuntimeException {
        public LineTooLongException(String message) {
            super(message);
        }
    }

