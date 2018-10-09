package main;

import main.Fraction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 * 分数四则运算
 */
public class Calculator {
    private List<Fraction> fractionsList = new ArrayList<>();
    private int operationNum = 2;   //生成运算数的数量
    private int operatorNum = operationNum - 1;   // 生成运算符的数量


    // 计算运算式的正确结果
    public Fraction generateCorrectAnswer(List<String> operators, List<Fraction> operations) {
        //遍历运算符容器，完成乘除运算
        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("*") || operator.equals("/")) {
                operators.remove(i);                    //乘除符号将其从集合中移除
                Fraction fractionLeft = operations.remove(i);      //拿运算符左侧的数字
                Fraction fractionRight = operations.remove(i);     //拿运算符右侧的数字
                if (operator.equals("*"))
                    operations.add(fractionMultiple(fractionLeft, fractionRight));
                else
                    operations.add(fractionDivide(fractionLeft, fractionRight));
                i--;  //运算符容器的指针回到原来的位置,防止跳过下一个运算符
            }
        }

        //遍历运算符容器，完成加减运算，当运算符容器为空时，运算结束
        while (!operators.isEmpty()) {
            String operator = operators.remove(0);
            Fraction fractionLeft = operations.remove(0);
            Fraction fractionRight = operations.remove(0);
            if (operator.equals("+"))
                operations.add(fractionAdd(fractionLeft, fractionRight));
            else
                operations.add(fractionSubtract(fractionLeft, fractionRight));
            operations.add(0, fractionLeft);
        }

        return operations.get(0);
    }

    // 真分数相乘运算
    public Fraction fractionMultiple(Fraction fractionLeft, Fraction fractionRight){
        int numerator = fractionLeft.getNumerator() * fractionRight.getNumerator();
        int denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();

        Fraction fraction = getGcdAndsimplificationFraction(numerator, denominator);
        return fraction;
    }

    // 真分数相除运算
    public Fraction fractionDivide(Fraction fractionLeft, Fraction fractionRight) {
        int numerator = fractionLeft.getNumerator() / fractionRight.getDenominator();
        int denominator = fractionLeft.getDenominator() / fractionRight.getNumerator();

        Fraction fraction = getGcdAndsimplificationFraction(numerator, denominator);
        return fraction;
    }

    // 真分数相加运算
    public Fraction fractionAdd(Fraction fractionLeft, Fraction fractionRight) {
        int denominator;
        int numerator;
        //如果分母相同时直接分子相加
        if (fractionLeft.getDenominator() == fractionRight.getDenominator()) {
            denominator = fractionLeft.getDenominator();
            numerator = fractionLeft.getNumerator() + fractionRight.getNumerator();
        }
        else {
            denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();
            numerator = fractionLeft.getNumerator() * fractionRight.getDenominator()
                    +  fractionRight.getNumerator() * fractionLeft.getDenominator();
        }
        Fraction fraction = getGcdAndsimplificationFraction(numerator, denominator);
        return fraction;
    }

    // 真分数相减运算
    public Fraction fractionSubtract(Fraction fractionLeft, Fraction fractionRight) {
        int denominator;
        int numerator;
        //如果分母相同时直接分子相加
        if (fractionLeft.getDenominator() == fractionRight.getDenominator()) {
            denominator = fractionLeft.getDenominator();
            numerator = fractionLeft.getNumerator() - fractionRight.getNumerator();
        }
        else {
            denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();
            numerator = fractionLeft.getNumerator() * fractionRight.getDenominator()
                    -  fractionRight.getNumerator() * fractionLeft.getDenominator();
        }
        Fraction fraction = getGcdAndsimplificationFraction(numerator, denominator);
        return fraction;
    }

    //获得最大公约数并且将真分数化简
    public Fraction getGcdAndsimplificationFraction(int numerator, int denominator) {
        int greatestCommonDivisor = greatestCommonDivisor(numerator, denominator);
        numerator /= greatestCommonDivisor;
        denominator /= greatestCommonDivisor;
        return new Fraction(numerator, denominator);
    }

    public void printFractionExpression() {
        FourOperation fourOperation = new FourOperation();
        fourOperation.init();
        List<String> operators = fourOperation.storeOpInList(operatorNum);
        for (int i = 0; i < operationNum; i++) {
            fractionsList.add(generateFraction());
        }

        StringBuilder stringBuilder = new StringBuilder();
        Fraction fraction;
        for (int i = 0; i < operationNum - 1;i++) {
            fraction = fractionsList.get(i);
            stringBuilder.append(fraction.getNumerator() + "/" + fraction.getDenominator());
            stringBuilder.append(" " + operators.get(i) + "");
        }
        fraction = fractionsList.get(operationNum-1);
        stringBuilder.append(fraction.getNumerator() + "/" + fraction.getDenominator() + " = ");
        System.out.println(stringBuilder.toString());
    }


    //生成一个真分数
    private Fraction generateFraction() {
        int numerator = generateNum();
        int denominator = regenerate(numerator, generateNum());
        int greatestCommonDivisor = greatestCommonDivisor(numerator, denominator);
        if (greatestCommonDivisor > 1) {
            numerator /= greatestCommonDivisor;
            denominator /= greatestCommonDivisor;
        }
        Fraction fraction = new Fraction(numerator, denominator);
        return fraction;
    }

    //递归生成与分子不相等的分母
    public int regenerate(int numerator, int denominator) {
        if (numerator == denominator) {
            denominator = generateNum();
            return regenerate(numerator, denominator);
        } else {
            return denominator;
        }
    }

    //生成1-20随机数
    private int generateNum(){
        int num = (int) (Math.random() * 20) + 1;
        return num;
    }

    // 求a和b的最大公约数
    public int greatestCommonDivisor(int a, int b) {
        if (a < b) {
            int c = a;
            a = b;
            b = c;
        }
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    // 求a和b的最小公倍数
    public int leastCommonMultiple(int a, int b){
        return a * b / greatestCommonDivisor(a, b);
    }
}
