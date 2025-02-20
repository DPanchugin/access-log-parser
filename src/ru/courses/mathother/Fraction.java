package ru.courses.mathother;

public class Fraction {
    private final int numerator;
    private final int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator <= 0) {
            throw new IllegalArgumentException("Denominator must be positive");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public Fraction sum(Fraction other) {
        int num = this.numerator * other.denominator + other.numerator * this.denominator;
        int denom = this.denominator * other.denominator;
        return new Fraction(num, denom);
    }

    public Fraction sum(int integer) {
        int num = this.numerator + integer * this.denominator;
        return new Fraction(num, this.denominator);
    }

    public Fraction minus(Fraction other) {
        int num = this.numerator * other.denominator - other.numerator * this.denominator;
        int denom = this.denominator * other.denominator;
        return new Fraction(num, denom);
    }

    public Fraction minus(int integer) {
        int num = this.numerator - integer * this.denominator;
        return new Fraction(num, this.denominator);
    }
}
