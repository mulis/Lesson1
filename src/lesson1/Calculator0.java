package lesson1;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 28.02.12
 * Time: 21:27
 */
class Calculator0 {

    char spaceChar = " ".charAt(0);
    char zeroChar = "0".charAt(0);
    char nineChar = "9".charAt(0);
    char plusChar = "+".charAt(0);
    char minusChar = "-".charAt(0);
    char parenthesesOpenChar = "(".charAt(0);
    char parenthesesCloseChar = ")".charAt(0);

    int expressionPosition = 0;
    String expression = "";

    public Integer calculate(String expressionInput) {

        Integer result;
        expression = expressionInput;

        String firstOperand = scanOperand();
        result = Integer.parseInt(firstOperand);

        while (expressionPosition < expression.length()) {

            String operation = scanOperator();
            String nextOperand = scanOperand();

            if (operation.charAt(0) == plusChar) {
                result += Integer.parseInt(nextOperand);
            }

            if (operation.charAt(0) == minusChar) {
                result -= Integer.parseInt(nextOperand);
            }

        }

        return result;

    }

    String scanOperand() {

        String operand = "";

        while (expressionPosition < expression.length()) {

            if (expression.charAt(expressionPosition) == spaceChar) {
                expressionPosition++;
                continue;
            }

            if (expression.charAt(expressionPosition) == parenthesesOpenChar) {
                operand = scanParentheses();
                break;
            }

            if (expression.charAt(expressionPosition) == parenthesesCloseChar) {
                //throw(new Exception("Close parenthesis without open parenthesis. Error position number: " + expressionPosition));
            }

            if (expression.charAt(expressionPosition) >= zeroChar && expression.charAt(expressionPosition) <= nineChar) {
                operand += expression.charAt(expressionPosition);
                expressionPosition++;
            } else {
                break;
            }

        }

        return operand;

    }

    String scanOperator() {

        String operator = "";

        while (expressionPosition < expression.length()) {

            if (expression.charAt(expressionPosition) == spaceChar) {
                expressionPosition++;
                continue;
            }

            if (expression.charAt(expressionPosition) == plusChar) {
                operator = String.valueOf(plusChar);
                expressionPosition++;
                break;
            }

            if (expression.charAt(expressionPosition) == minusChar) {
                operator = String.valueOf(minusChar);
                expressionPosition++;
                break;
            }

        }

        return operator;

    }

    String scanParentheses() {

        String operand = "";

        int parenthesesOpenPosition = expressionPosition;
        int parenthesesCount = 0;

        while (expressionPosition < expression.length()) {

            if (expression.charAt(expressionPosition) == parenthesesOpenChar) {
                parenthesesCount++;
            }

            if (expression.charAt(expressionPosition) == parenthesesCloseChar) {
                parenthesesCount--;
            }

            if (parenthesesCount == 0) {
                operand = new Calculator0().calculate(expression.substring(parenthesesOpenPosition + 1, expressionPosition)).toString();
            }

            expressionPosition++;

        }

        return operand;

    }

}
