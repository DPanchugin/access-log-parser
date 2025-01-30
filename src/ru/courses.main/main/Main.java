package ru.courses.main.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String LOG_ENTRY_REGEX =
            "^(\\S+) (\\S+) (\\S+) \\[(.+?)\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+|-) \"([^\"]*)\" \"([^\"]*)\"$";
    private static final Pattern LOG_PATTERN = Pattern.compile(LOG_ENTRY_REGEX);
     public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attemptCount = 0;
        while (true) {
            System.out.printf("Введите путь к файлу: ");
            String path = scanner.nextLine().trim();
            attemptCount++;
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
            int totalRequests = 0;
            int googleBotCount = 0;
            int yandexBotCount = 0;

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader reader = new BufferedReader(fileReader)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    Matcher matcher = LOG_PATTERN.matcher(line);
                    if (!matcher.matches()) {
                        continue;
                    }
                    totalRequests++;

                    String userAgent = matcher.group(11);
                    if (userAgent != null) {
                        if (userAgent.toLowerCase().contains("googlebot")) {
                            googleBotCount++;
                        } else if (userAgent.toLowerCase().contains("yandexbot")) {
                            yandexBotCount++;
                        }
                    }
                }
                if (totalRequests == 0) {
                    System.out.println("Файл пуст или не содержит валидных записей");
                } else {
                    double yandexBotPercentage = (yandexBotCount * 100.0) / totalRequests;
                    double googleBotPercentage = (googleBotCount * 100.0) / totalRequests;

    ;

                    System.out.printf("Доля запросов от YandexBot: %.2f%%%n", yandexBotPercentage);
                    System.out.printf("Доля запросов от Googlebot: %.2f%%%n", googleBotPercentage);
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла: " + e.getMessage());
            }
            break;
        }
        scanner.close();
    }
    private static String extractProgramFromUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        int firstOpenBracket = userAgent.indexOf('(');
        int firstCloseBracket = userAgent.indexOf(')', firstOpenBracket);
        if (firstOpenBracket == -1 || firstCloseBracket == -1) {
            return null;
        }
        String insideBrackets = userAgent.substring(firstOpenBracket + 1, firstCloseBracket);
        String[] parts = insideBrackets.split(";");
        if (parts.length < 2) {
            return null;
        }
        String fragment = parts[1].trim();
        int slashIndex = fragment.indexOf('/');
        if (slashIndex == -1) {
            return fragment;
        } else {
            return fragment.substring(0, slashIndex);
        }
    }
}
