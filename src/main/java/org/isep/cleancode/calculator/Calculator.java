package org.isep.cleancode.calculator;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    public double evaluateMathExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        return evaluate(expression);
    }

    private double evaluate(String expression) {
        // priorité des parenthèses
        while (expression.contains("(")) {
            int start = expression.lastIndexOf('(');
            int end = expression.indexOf(')', start);
            if (end == -1) throw new IllegalArgumentException("Parenthèse fermante manquante");

            String inner = expression.substring(start + 1, end);
            double innerResult = evaluate(inner);
            expression = expression.substring(0, start) + innerResult + expression.substring(end + 1);
        }

        List<String> tokens = tokenize(expression);

        // priorité multiplications
        tokens = handleMultiplications(tokens);

        return handleAddSub(tokens);
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        boolean lastWasOperator = true;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                number.append(c);
                lastWasOperator = false;
            } else if (c == '-' && lastWasOperator) {
                // nombre négatif
                number.append(c);
            } else {
                if (number.length() > 0) {
                    tokens.add(number.toString());
                    number.setLength(0);
                }
                tokens.add(Character.toString(c));
                lastWasOperator = true;
            }
        }

        if (number.length() > 0) {
            tokens.add(number.toString());
        }

        return tokens;
    }

    private List<String> handleMultiplications(List<String> tokens) {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            if (token.equals("*")) {
                double left = Double.parseDouble(result.remove(result.size() - 1));
                double right = Double.parseDouble(tokens.get(i + 1));
                result.add(String.valueOf(left * right));
                i += 2;
            } else {
                result.add(token);
                i++;
            }
        }
        return result;
    }

    private double handleAddSub(List<String> tokens) {
        double result = Double.parseDouble(tokens.get(0));
        for (int i = 1; i < tokens.size(); i += 2) {
            String op = tokens.get(i);
            double next = Double.parseDouble(tokens.get(i + 1));
            if (op.equals("+")) {
                result += next;
            } else if (op.equals("-")) {
                result -= next;
            }
        }
        return result;
    }
}
