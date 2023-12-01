package io.droidtech.app.binarycalculator.model;

import java.math.BigInteger;
import java.util.HashMap;

public class Calculation {

    private HashMap<String, String> cal;
    private BigInteger leftNbr, rightNbr;
    private String left, right;

    public Calculation() {
        this.cal = new HashMap<>();
        leftNbr = null;
        rightNbr = null;
        left = null;
        right = null;
    }

    public HashMap<String, String> binaryCalculator(String expression) {

        char operator = op(expression);

        if (operator == ' ') {
            cal = binaryToDecimal(expression);
        } else {

            cal.put("operator", String.valueOf(operator));

            this.left = expression.substring(0, expression.indexOf(operator));
            this.right = expression.substring(expression.indexOf(operator) + 1);

            cal.put("left", left);
            cal.put("right", right);

            this.leftNbr = new BigInteger(left, 2);
            this.rightNbr = new BigInteger(right, 2);

            cal.put("leftNbr", leftNbr.toString());
            cal.put("rightNbr", rightNbr.toString());

            String result = "";
            switch (operator) {
                case '+':
                    result = leftNbr.add(rightNbr).toString(2);
                    break;
                case '-':
                    result = leftNbr.subtract(rightNbr).toString(2);
                    break;
                case '*':
                    result = leftNbr.multiply(rightNbr).toString(2);
                    break;
                case '/':
                    result = leftNbr.divide(rightNbr).toString(2);
                    break;
            }

            cal.put("init_expression", expression + "=");
            cal.put("result", result);
            cal.put("error", "0");
            cal.put("error_message", "");

        }

        return cal;
    }

    public HashMap<String, String> binaryToDecimal(String expression) {

        this.leftNbr = new BigInteger(expression, 2);
        cal.put("init_expression", expression + "=");
        cal.put("result", String.valueOf(leftNbr));
        cal.put("error", "0");
        cal.put("error_message", "");

        return cal;
    }

    public HashMap<String, String> decimalToBinary(String expression) {

        this.leftNbr = new BigInteger(expression);
        cal.put("init_expression", expression + "=");
        cal.put("result", leftNbr.toString(2));
        cal.put("error", "0");
        cal.put("error_message", "");

        return cal;
    }

    private char op(String currentExpression) {
        if (currentExpression.contains("+"))
            return '+';
        else if (currentExpression.contains("-"))
            return '-';
        else if (currentExpression.contains("/"))
            return '/';
        else if (currentExpression.contains("*"))
            return '*';
        else
            return ' ';
    }

}
