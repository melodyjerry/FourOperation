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

//    @Test
//    void Test(){
//        for (int i=0;i<30; i++) {
//            Fraction fraction = generateFraction();
//            System.out.println(fraction.getNumerator() + " / " + fraction.getDenominator());
//        }
//    }

//    @Test
//    public void printFractionExpression() {
//        FourOperation fourOperation = new FourOperation();
//        fourOperation.init();
//        List<String> operators = fourOperation.storeOpInList(operatorNum);
//        for (int i = 0; i < operationNum; i++) {
//            fractionsList.add(generateFraction());
//        }
//
//        StringBuilder stringBuilder = new StringBuilder();
//        Fraction fraction;
//        for (int i = 0; i < operationNum - 1;i++) {
//            fraction = fractionsList.get(i);
//            stringBuilder.append(fraction.getNumerator() + "/" + fraction.getDenominator());
//            stringBuilder.append(" " + operators.get(i) + "");
//        }
//        fraction = fractionsList.get(operationNum-1);
//        stringBuilder.append(fraction.getNumerator() + "/" + fraction.getDenominator() + " = ");
//        System.out.println(stringBuilder.toString());
//    }

//    //生成一个真分数
//    private Fraction generateFraction() {
//        int numerator = generateNum();
//        int denominator = regenerate(numerator, generateNum());
//        int greatestCommonDivisor = greatestCommonDivisor(numerator, denominator);
//        if (greatestCommonDivisor > 1) {
//            numerator /= greatestCommonDivisor;
//            denominator /= greatestCommonDivisor;
//        }
//        Fraction fraction = new Fraction(numerator, denominator);
//        return fraction;
//    }

    //递归生成与分子不相等的分母数
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

    @Test
    public void test() {
        int te = greatestCommonDivisor(10,5);
        System.out.println(te);
    }

    // 求a和b的最大公约数
    public int greatestCommonDivisor(int a, int b) {
        int r;
        while(b!=0)
        {
            r=a%b;
            a=b;
            b=r;
        }
        return a;
    }

    @Test
    public void test1(){
        System.out.println(leastCommonMultiple(4, 5));
    }

    // 求a禾b的最小公倍数
    public int leastCommonMultiple(int a, int b){
        return a * b / greatestCommonDivisor(a, b);
    }



}
