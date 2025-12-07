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
        String[] operations = {"+", "-", "*"};

        isComplex = false;
        
        // For HARD difficulty, generate more challenging equations
        if (difficulty == DifficultySelect.Difficulty.HARD) {
            if (rand.nextDouble() < 0.6) {
                // 60% chance of complex equation (e.g., "9 × 9 - 8")
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
            } else if (rand.nextDouble() < 0.8) {
                // Division with larger numbers
                operation = "/";
                num2 = rand.nextInt(12) + 3; // divisor 3-14
                int quotient = rand.nextInt(20) + 3; // quotient 3-22
                num1 = num2 * quotient; // dividend (can be quite large)
                answer = quotient;
                return;
            }
            // Otherwise continue to simple operations but with large numbers
        }

        // Simple equations for Easy/Medium or simple Hard
        int opIndex;
        if (difficulty == DifficultySelect.Difficulty.EASY) {
            opIndex = rand.nextInt(2); // Only + and -
        } else if (difficulty == DifficultySelect.Difficulty.MEDIUM) {
            opIndex = rand.nextInt(3); // All operations (+, -, *)
        } else { // HARD (simple case)
            // For hard, can also include division sometimes
            if (rand.nextDouble() < 0.3) {
                operation = "/";
                num2 = rand.nextInt(15) + 5; // divisor 5-19
                int quotient = rand.nextInt(25) + 5; // quotient 5-29
                num1 = num2 * quotient; // dividend (can be quite large)
                answer = quotient;
                return;
            }
            opIndex = rand.nextInt(3); // +, -, *
        }

        operation = operations[opIndex];

        // Determine range based on difficulty ONLY (ignore level scaling)
        int maxRange;
        switch (difficulty) {
            case EASY:
                maxRange = 10; // Easy: numbers 1-10
                break;
            case MEDIUM:
                maxRange = 20; // Medium: numbers 1-20
                break;
            case HARD:
                maxRange = 100; // Hard: numbers can go up to 100
                break;
            default:
                maxRange = 20;
        }
        
        // Use fixed range based on difficulty (no level scaling)
        int range = maxRange;

        switch(operation) {
            case "+":
                if (difficulty == DifficultySelect.Difficulty.HARD) {
                    // Hard: larger numbers 20-100
                    num1 = rand.nextInt(81) + 20; // 20-100
                    num2 = rand.nextInt(81) + 20; // 20-100
                } else {
                    num1 = rand.nextInt(range) + 1;
                    num2 = rand.nextInt(range) + 1;
                }
                answer = num1 + num2;
                break;
            case "-":
                if (difficulty == DifficultySelect.Difficulty.HARD) {
                    // Hard: larger numbers
                    num1 = rand.nextInt(81) + 50; // 50-130
                    num2 = rand.nextInt(num1 - 20) + 20; // 20 to (num1 - 1)
                } else {
                    num1 = rand.nextInt(range) + 10;
                    num2 = rand.nextInt(num1) + 1;
                }
                answer = num1 - num2;
                break;
            case "*":
                // For multiplication, use difficulty-based range
                int multRange;
                if (difficulty == DifficultySelect.Difficulty.EASY) {
                    multRange = 10; // Easy: up to 10x10
                } else if (difficulty == DifficultySelect.Difficulty.MEDIUM) {
                    multRange = 12; // Medium: up to 12x12
                } else { // HARD
                    multRange = 20; // Hard: up to 20x20 for simple multiplications
                }
                num1 = rand.nextInt(multRange) + 1;
                num2 = rand.nextInt(multRange) + 1;
                answer = num1 * num2;
                break;
        }
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