package com.elijahcorp.testcalculator;

public class Calculation {

    public Calculation() {
    }

    public boolean isOperator(char c) {
        return "+-/*".contains(String.valueOf(c));
    }
}
