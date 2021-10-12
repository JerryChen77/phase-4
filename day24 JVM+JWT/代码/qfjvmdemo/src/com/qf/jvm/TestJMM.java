package com.qf.jvm;

public class TestJMM {
    //静态属性
    public static int baseData = 10;
    //静态属性
    public static Student student = new Student();

    /**
     * 两数相加
     * @return
     */
    public int add() {
        int num1 = 10;
        int num2 = 20;
        int result = (num1 + num2) * 5;
        return result;
    }

    public static void main(String[] args) {

        TestJMM demo = new TestJMM();
        int result = demo.add();
        System.out.println(result);
    }

}
