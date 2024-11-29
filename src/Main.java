import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число:");
        int number1 = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число:");
        int number2 = new Scanner(System.in).nextInt();

        System.out.println("Сумма");
        System.out.println(number1+number2);
        System.out.println("Разность Первое число - Второе число");
        System.out.println(number1-number2);
        System.out.println("Умножение двух чисел");
        System.out.println(number1*number2);
        System.out.println("Деление первого числа на второе число");
        System.out.println((double) number1/number2);

    }

}