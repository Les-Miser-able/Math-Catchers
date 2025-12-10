package com.mathcatcher.utils;

import com.mathcatcher.game.DifficultySelect;
import java.util.Random;

public class MathEquation {
    private int num1;
    private int num2;
    private int num3; // For complex equations
    private String operation;
    private String operation2; // For complex equations (e.g., "2 × 9 - 9")
    private boolean isComplex; // True for multi-step equations
    private int answer;
    private Random rand;

    public MathEquation(int level) {
        this(level, DifficultySelect.Difficulty.MEDIUM);
    }

    public MathEquation(int level, DifficultySelect.Difficulty difficulty) {
        rand = new Random();
        generateEquation(level, difficulty);
    }

    private void generateEquation(int level, DifficultySelect.Difficulty difficulty) {
        isComplex = false;
        
        // EASY: Only addition and subtraction
        if (difficulty == DifficultySelect.Difficulty.EASY) {
            String[] easyOps = {"+", "-"};
            operation = easyOps[rand.nextInt(2)];
            
            // Easy: numbers 1-10
            int maxRange = 10;
            if (operation.equals("+")) {
                num1 = rand.nextInt(maxRange) + 1;
                num2 = rand.nextInt(maxRange) + 1;
                answer = num1 + num2;
            } else { // subtraction
                num1 = rand.nextInt(maxRange) + 10; // 10-20
                num2 = rand.nextInt(num1 - 1) + 1; // 1 to (num1 - 1)
                answer = num1 - num2;
            }
            return;
        }
        
        // MEDIUM: Addition, subtraction, multiplication, and division
        if (difficulty == DifficultySelect.Difficulty.MEDIUM) {
            int opChoice = rand.nextInt(4); // 0=+, 1=-, 2=*, 3=/
            
            if (opChoice == 0) { // Addition
                operation = "+";
                num1 = rand.nextInt(20) + 1; // 1-20
                num2 = rand.nextInt(20) + 1; // 1-20
                answer = num1 + num2;
            } else if (opChoice == 1) { // Subtraction
                operation = "-";
                num1 = rand.nextInt(20) + 10; // 10-30
                num2 = rand.nextInt(num1 - 1) + 1; // 1 to (num1 - 1)
                answer = num1 - num2;
            } else if (opChoice == 2) { // Multiplication
                operation = "*";
                num1 = rand.nextInt(12) + 1; // 1-12
                num2 = rand.nextInt(12) + 1; // 1-12
                answer = num1 * num2;
            } else { // Division
                operation = "/";
                num2 = rand.nextInt(12) + 2; // divisor 2-13
                int quotient = rand.nextInt(12) + 2; // quotient 2-13
                num1 = num2 * quotient; // dividend
                answer = quotient;
            }
            return;
        }
        
        // HARD: All operations including complex equations
        if (difficulty == DifficultySelect.Difficulty.HARD) {
            // 40% chance of complex equation (e.g., "9 × 9 - 8")
            if (rand.nextDouble() < 0.4) {
                isComplex = true;
                
                // Use larger numbers for multiplication (6-15 range)
                num1 = rand.nextInt(10) + 6; // 6-15
                num2 = rand.nextInt(10) + 6; // 6-15
                int multResult = num1 * num2;
                
                // Second operation: + or -
                operation = "*";
                operation2 = rand.nextBoolean() ? "+" : "-";
                
                if (operation2.equals("+")) {
                    // Add a number between 5-50
                    num3 = rand.nextInt(46) + 5; // 5-50
                    answer = multResult + num3;
                } else {
                    // Subtract a number between 5 and (multResult - 1)
                    int maxSub = Math.min(multResult - 5, 50);
                    if (maxSub > 0) {
                        num3 = rand.nextInt(maxSub) + 5; // 5 to (multResult - 5)
                    } else {
                        num3 = rand.nextInt(20) + 1; // Fallback
                    }
                    answer = multResult - num3;
                }
                return;
            }
            
            // 60% chance of simple operations (including division)
            int opChoice = rand.nextInt(4); // 0=+, 1=-, 2=*, 3=/
            
            if (opChoice == 0) { // Addition
                operation = "+";
                num1 = rand.nextInt(81) + 20; // 20-100
                num2 = rand.nextInt(81) + 20; // 20-100
                answer = num1 + num2;
            } else if (opChoice == 1) { // Subtraction
                operation = "-";
                num1 = rand.nextInt(81) + 50; // 50-130
                num2 = rand.nextInt(num1 - 20) + 20; // 20 to (num1 - 1)
                answer = num1 - num2;
            } else if (opChoice == 2) { // Multiplication
                operation = "*";
                num1 = rand.nextInt(20) + 1; // 1-20
                num2 = rand.nextInt(20) + 1; // 1-20
                answer = num1 * num2;
            } else { // Division
                operation = "/";
                num2 = rand.nextInt(15) + 5; // divisor 5-19
                int quotient = rand.nextInt(25) + 5; // quotient 5-29
                num1 = num2 * quotient; // dividend
                answer = quotient;
            }
            return;
        }
        
        // Fallback (shouldn't reach here)
        operation = "+";
        num1 = rand.nextInt(10) + 1;
        num2 = rand.nextInt(10) + 1;
        answer = num1 + num2;

    }

    public int getAnswer() {
        return answer;
    }

    private String getDisplaySymbol(String op) {
        switch(op) {
            case "*":
                return "×";
            case "/":
                return "÷";
            case "+":
                return "+";
            case "-":
                return "-";
            default:
                return op;
        }
    }

    @Override
    public String toString() {
        if (isComplex) {
            // Complex equation: "2 × 9 - 9 = ?"
            return num1 + " " + getDisplaySymbol(operation) + " " + num2 + " " + 
                   getDisplaySymbol(operation2) + " " + num3 + " = ?";
        } else {
            // Simple equation: "2 + 3 = ?" or "6 ÷ 2 = ?"
            return num1 + " " + getDisplaySymbol(operation) + " " + num2 + " = ?";
        }
    }
}