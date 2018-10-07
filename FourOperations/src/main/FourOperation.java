import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

/**
 *
 */
public class FourOperation {
    public static List<String> operatorsList;
    private static List<String> operatorExpressionList;
    private static List<String> userAnswerList;
    private static List<String> correctAnswerList;

    // 初始化；
    public void init() {
        String[] operators = {"+", "-", "*", "/"};
        operatorsList = Arrays.asList(operators);
        operatorExpressionList = new ArrayList<>();
        userAnswerList = new ArrayList<>();
        correctAnswerList = new ArrayList<>();
    }

    @Test
    public void test() {
        Random random = new Random();
        int operationNum = random.nextInt(3) + 3;
        System.out.println(operationNum);
    }

    public void generateAll(int operationNum, Scanner scanner) {
        // 循环生成题目，用户做一题生一题
        for (int i = 0; i < operationNum; i++) {
            generateFormula();
            String userAnswer = scanner.next();
            userAnswerList.add(userAnswer);
            if (userAnswer.equals(correctAnswerList.get(i))) {
                System.out.println("计算正确！正确答案为： " + correctAnswerList.get(i));
            } else {
                System.out.println("计算错误！正确答案为： " + correctAnswerList.get(i) +
                        "你的答案为：" + userAnswer);
            }
        }
    }

    // 生成运算公式并存入List中
    public void generateFormula() {
        Random random = new Random();
        int operationNum = random.nextInt(3) + 3;   //运算数的个数
        int operatorNum = operationNum - 1;               //运算符的个数

        List<String> newOperators;
        List<Integer> newOperations;
        int newAnswer;
        //计算运算式答案
        do {
            List<String> operators = storeOpInList(operatorNum);
            List<Integer> operations = operations(operationNum, operators);
            newOperators = deepCopy(operators);
            newOperations = deepCopy(operations);
            newAnswer = generateCorrectAnswer(operators, operations);
        } while (newAnswer < 0);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < operationNum - 1; i++)
            stringBuilder.append(newOperations.get(i) + " " + newOperators.get(i) + " ");
        stringBuilder.append(newOperations.get(operationNum - 1) + " = ");
        System.out.println(stringBuilder.toString());
        operatorExpressionList.add(stringBuilder.toString());
        correctAnswerList.add(String.valueOf(newAnswer));
    }

    // 计算运算式的正确结果
    public int generateCorrectAnswer(List<String> operators, List<Integer> operations) {
        //遍历运算符容器，完成乘除运算
        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("*") || operator.equals("/")) {
                operators.remove(i);                    //乘除符号将其从集合中移除
                int operateLeft = operations.remove(i);      //拿运算符左侧的数字
                int operateRight = operations.remove(i);     //拿运算符右侧的数字
                if (operator.equals("*"))
                    operations.add(i, operateLeft * operateRight);
                else
                    operations.add(i, operateLeft / operateRight);
                i--;  //运算符容器的指针回到原来的位置,防止跳过下一个运算符
            }
        }

        //遍历运算符容器，完成加减运算，当运算符容器为空时，运算结束
        while (!operators.isEmpty()) {
            String operator = operators.remove(0);
            int operateLeft = operations.remove(0);
            int operateRight = operations.remove(0);
            if (operator.equals("+"))
                operateLeft = operateLeft + operateRight;
            else
                operateLeft = operateLeft - operateRight;
            operations.add(0, operateLeft);
        }

        System.out.println(operations.get(0));
        return operations.get(0);
    }

    //生成operationNum个100以内的随机数并存入List集合中
    public List<Integer> operations(int operationNum, List<String> operators) {
        List<Integer> operations = new ArrayList<>();
        for (int i = 0; i < operationNum; i++) {
            int num = generateNum();
            operations.add(num);
        }
        //当被除数不能整除除数时，随机生成能够整除的除数
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("/")) {
                int x = decide(operations.get(i), operations.get(i + 1));
                operations.set(i + 1, x);
            }
        }
        return operations;
    }

    /**
     * 随即取x,y为1-100之间，x可以整除y的y值
     * 通过递归实现整除
     *
     * @param x
     * @param y
     * @return
     */
    private static int decide(int x, int y) {
        Random random = new Random();
        if (x % y != 0) {
            y = random.nextInt(100) + 1;
            return decide(x, y);
        } else
            return y;
    }

    //生成运算数
    private int generateNum() {
        int num = (int) (Math.random() * 99) + 1;
        return num;
    }

    //深拷贝List
    public static <T> List<T> deepCopy(List<T> src) {
        @SuppressWarnings("unchecked")
        List<T> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
            return dest;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dest;
    }


    @Test
    public void testOperators() {
        String[] operators = {"+", "-", "*", "/"};
        operatorsList = Arrays.asList(operators);
        List<String> operatorsList = storeOpInList(2);
        for (String operator : operatorsList) {
            System.out.println(operator);
        }
    }

    //生成operatorsNum个运算符并存入List集合中
    public List<String> storeOpInList(int operatorNum) {
        List<String> operatorsList = new ArrayList<>();
        for (int i = 0; i < operatorNum; i++) {
            String operator = generateOperator();
            operatorsList.add(operator);
        }
        return operatorsList;
    }

    //生成运算符
    private String generateOperator() {
        int x = (int) (Math.random() * 4);
        String operator = operatorsList.get(x);
        return operator;
    }


    public static void main(String[] args) {
        FourOperation fourOperation = new FourOperation();
        fourOperation.init();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入生成四则运算题目的个数：");
        int num = scanner.nextInt();
        fourOperation.generateAll(num, scanner);
    }

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
}
