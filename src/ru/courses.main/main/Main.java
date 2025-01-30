package ru.courses.main.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    // Регулярное выражение для разбора строки журнала
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

            // Инициализируем счетчики
            int totalRequests = 0;
            int googleBotCount = 0;
            int yandexBotCount = 0;

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;

                while ((line = reader.readLine()) != null) {
                    totalRequests++;

                    Matcher matcher = LOG_PATTERN.matcher(line);
                    if (!matcher.matches()) {
                        // Если строка не соответствует шаблону, пропускаем её
                        continue;
                    }

                    // Извлекаем необходимые поля
                    String ipAddress = matcher.group(1);
                    // matcher.group(2) и matcher.group(3) — это два дефиса или другие значения, которые мы игнорируем
                    String dateTime = matcher.group(4);
                    String method = matcher.group(5);
                    String pathRequested = matcher.group(6);
                    String protocol = matcher.group(7);
                    String responseCode = matcher.group(8);
                    String dataSize = matcher.group(9);
                    String referer = matcher.group(10);
                    String userAgent = matcher.group(11);

                    // Обработка User-Agent
                    String program = extractProgramFromUserAgent(userAgent);

                    // Проверяем, является ли программа Googlebot или YandexBot
                    if ("Googlebot".equalsIgnoreCase(program)) {
                        googleBotCount++;
                    } else if ("YandexBot".equalsIgnoreCase(program)) {
                        yandexBotCount++;
                    }
                }

                // После обработки всех строк выводим результаты
                if (totalRequests == 0) {
                    System.out.println("Файл пуст или не содержит валидных записей.");
                } else {
                    double googleBotPercentage = (googleBotCount * 100.0) / totalRequests;
                    double yandexBotPercentage = (yandexBotCount * 100.0) / totalRequests;

                    System.out.printf("Доля запросов от YandexBot: %.2f%%%n", yandexBotPercentage);
                    System.out.printf("Доля запросов от Googlebot: %.2f%%%n", googleBotPercentage);

                }

            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении файла: " + e.getMessage());
            }

            // Завершаем цикл после успешной обработки
            break;
        }

        scanner.close();
    }
       private static String extractProgramFromUserAgent(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        // Простой поиск по подстрокам
        if (userAgent.toLowerCase().contains("googlebot")) {
            return "Googlebot";
        } else if (userAgent.toLowerCase().contains("yandexbot")) {
            return "YandexBot";
        }

        return null;
    }
}
