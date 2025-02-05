package ru.courses.main.main;


import ru.courses.main.main.LogEntry;
import ru.courses.main.main.MaxLengthLineException;
import ru.courses.main.main.Statistics;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 1;
        int countLine = 0;
        int countGoogle = 0;
        int countYandex = 0;
        String userAgent;
        Statistics statistics = new Statistics();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = scanner.nextLine().trim();

            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Файла или директории не существует.");
                continue;
            }

            if (file.isDirectory()) {
                System.out.println("Указан путь к директории, а не к файлу.");
                continue;
            }
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) {
                        throw new MaxLengthLineException("Maximum line length exceeded 1024");
                    }
                    countLine++;
                    LogEntry lg = new LogEntry(line);
                    statistics.addEntry(lg);
                    List<String> l = List.of(line.split(" "));
                    userAgent = "";
                    if (!l.get(11).equals("\"-\"")) {
                        for (int i = 0; i < l.size() - 11; i++) {
                            userAgent = userAgent + l.get(11 + i);
                        }
                    } else userAgent = l.get(11);
                    try {
                        if (!(userAgent.equals("\"-\""))) {
                            userAgent = userAgent.substring(userAgent.indexOf('('));
                            String[] parts = userAgent.split(";");
                            if (parts.length >= 2) {
                                String fragment = parts[1];
                                fragment = fragment.trim().substring(0, fragment.indexOf('/'));
                                if (fragment.equals("Googlebot")) {
                                    countGoogle++;
                                }
                                if (fragment.equals("YandexBot")) {
                                    countYandex++;
                                }
                            }
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
            } catch (MaxLengthLineException ex) {
                System.out.println(ex);
                System.exit(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
            // Завершаем цикл после успешной обработки
            break;
        }
        System.out.println("Путь к файлу указан верно. Количество затраченных попыток " + count);
        System.out.println("Общее количество строк в файле: " + countLine);
        System.out.println("Средний объём трафика за час: " + statistics.getTrafficRate());
        System.out.println("Доля запросов от Googlebot в %: " + (double) (countGoogle*100) / countLine);
        System.out.println("Доля запросов от YandexBot в %: " + (double) (countYandex*100) / countLine);

        scanner.close();
    }

}
