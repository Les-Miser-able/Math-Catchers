package com.mathcatcher.utils;

import java.util.Random;

public class MathEquation {
    private int num1;
    private int num2;
    private String operation;
    private int answer;
    private Random rand;

    public MathEquation(int level) {
        rand = new Random();
        generateEquation(level);
    }

    private void generateEquation(int level) {
        String[] operations = {"+", "-", "*"};

        // Higher levels include more operations
        int opIndex;
        if (level < 3) {
            opIndex = rand.nextInt(2); // Only + and -
        } else {
            opIndex = rand.nextInt(3); // All operations
        }

        operation = operations[opIndex];

        // Scale difficulty with level
        int range = Math.min(10 + level * 5, 50);

        switch(operation) {
            case "+":
                num1 = rand.nextInt(range) + 1;
                num2 = rand.nextInt(range) + 1;
                answer = num1 + num2;
                break;
            case "-":
                num1 = rand.nextInt(range) + 10;
                num2 = rand.nextInt(num1) + 1;
                answer = num1 - num2;
                break;
            case "*":
                num1 = rand.nextInt(Math.min(12, 5 + level)) + 1;
                num2 = rand.nextInt(Math.min(12, 5 + level)) + 1;
                answer = num1 * num2;
                break;
        }
    }

    public int getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return num1 + " " + operation + " " + num2 + " = ?";
    }
}