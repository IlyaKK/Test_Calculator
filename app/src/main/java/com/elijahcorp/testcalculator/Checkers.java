package com.elijahcorp.testcalculator;

public class Checkers {

    public Checkers() {
    }

    public boolean checkInsertSymbol(CharSequence expression, Character pastSymbol, Character newSymbol) {
        boolean pastSymbolIsOperation = isOperator(pastSymbol);
        boolean newSymbolIsOperation = isOperator(newSymbol);
        boolean pastSymbolIsPoint = pastSymbol == '.';
        boolean newSymbolIsPoint = newSymbol == '.';
        boolean pastSymbolIsZero = pastSymbol == '0';
        boolean newSymbolIsZero = newSymbol == '0';
        if (pastSymbolIsOperation && newSymbolIsOperation) {
            return false;
        } else if (pastSymbolIsOperation && newSymbolIsPoint) {
            return false;
        } else if (pastSymbolIsPoint && newSymbolIsOperation) {
            return false;
        } else if (expression.length() - 1 == 1 && !pastSymbolIsZero) {
            return true;
        } else if (expression.length() - 1 == 1 && newSymbolIsZero) {
            return false;
        } else if (expression.length() - 1 == 1 && !newSymbolIsZero && (newSymbolIsOperation || newSymbolIsPoint)) {
            return true;
        } else if (expression.length() - 1 == 1 && !newSymbolIsZero && !newSymbolIsPoint) {
            return true;
        } else if (newSymbolIsPoint) {
            int countPoint = 0;
            for (int i = expression.length() - 1 - 1; i >= 0; i--) {
                if (expression.charAt(i) == '.') {
                    countPoint++;
                }
                if (isOperator(expression.charAt(i))) {
                    return countPoint <= 0;
                } else if (i == 0 && countPoint > 0) {
                    return false;
                }
            }
            return true;
        } else if (newSymbolIsZero) {
            for (int i = expression.length() - 1 - 1; i >= 0; i--) {
                if ((isOperator(expression.charAt(i))) && (i + 1 < expression.length()) && expression.charAt(i + 1) == '0') {
                    return false;
                } else if (expression.charAt(i) == '.') {
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public boolean isOperator(char c) {
        return "+-/*".contains(String.valueOf(c));
    }
}
