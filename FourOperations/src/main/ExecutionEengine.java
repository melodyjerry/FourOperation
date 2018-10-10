package main;

import java.util.Random;
import java.util.Scanner;

public class ExecutionEengine {
    public static void main(String[] args) {
        SimpleFourOperation simpleFourOperation = new SimpleFourOperation();
        simpleFourOperation.init();
        FractionOperation fractionOperation = new FractionOperation();
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入生成四则运算题目的个数：");
        int titleNum;
        do {
            titleNum = scanner.nextInt();
            if (titleNum > 0)
                break;
            else {
                System.out.println("输入有误，不能输入非法字符以及小于0，请重新输入！");
                System.out.println("请输入生成四则运算题目的个数：");
            }
        } while (!(titleNum > 0));

        Random random = new Random();
        int simpleFourOperationNum = random.nextInt(titleNum) + 1;
        int FractionOperationNum = titleNum - simpleFourOperationNum;

        int simpleFourOpTrueNum = simpleFourOperation.generateSimpleFourOpExp(simpleFourOperationNum, scanner);
        int fractionOpTrueNum = fractionOperation.generateFractionOpExp(FractionOperationNum, scanner);

        int trueNum = simpleFourOpTrueNum + fractionOpTrueNum;
        int falseNum = titleNum - trueNum;
        System.out.println("恭喜你完成这次练习，题目总数：" + titleNum + ", 你做对了" + trueNum + "道题,"
                + "做错了" + falseNum + "道题, " + "继续加油！");
    }
}
