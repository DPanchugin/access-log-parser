import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int attemptCount = 0; // Счётчик попыток ввода

        while (true) {
            System.out.printf("Введите путь к файлу: ", ++attemptCount);
            String path = scanner.nextLine();

            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists) {
                System.out.println("Файла или директории не существует.");
                continue;
            }

            if (isDirectory) {
                System.out.println("Указан путь к директории, а не к файлу.");
                continue;
            }

            System.out.println("Путь к файлу указан верно. Количество затраченных попыток: " + attemptCount);
            break; // Если файл найден, выходим из цикла
        }
    }
}