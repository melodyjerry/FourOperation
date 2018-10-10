package main;

import java.io.Serializable;

public class Fraction implements Serializable {
    private static final long serialVersionUID = -3245478690496182643L;
    private int numerator;  //分子
    private int denominator;  //分母

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }
}
