package lesson1;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:05
 */
class Calculator {

    String calculate(String expression) throws CommonException {

        ArrayList<Token> tokens = makeTokens(expression);

        int index = 0;

        while (tokens.size() > 1) {

            Token token = tokens.get(index);

            String operationBefore = token.expression.substring(0, token.start);
            String operationAfter = token.expression.substring(token.end);

            if (token.type == Token.TYPE_OPERATOR) {
                Operator operator = (Operator) token;
                Number[] operands = new Number[operator.argumentCount];
                index -= operator.argumentCount;
                if (index < 0) {
                    throw (new AbsentOperandException(operator));
                }
                for (int i = 0; i < operator.argumentCount; i++) {
                    Token operand = tokens.get(index);
                    if (operand.type == Token.TYPE_NUMBER) {
                        operands[i] = (Number) operand;
                        // remove parentheses from operand
                        while ((operand.start > 0) && (operand.expression.charAt(operand.start - 1) == Token.PARENTHESISLEFT)
                                && (operand.end < operand.expression.length()) && (operand.expression.charAt(operand.end) == Token.PARENTHESISRIGHT)) {
                            operand.expression = operand.expression.substring(0, operand.start - 1) + operand.text + operand.expression.substring(operand.end + 1);
                            operand.start--;
                            operand.end--;
                        }
                        // update expression before operation tokens
                        if (operationBefore.length() > operand.expression.substring(0, operand.start).length()) {
                            operationBefore = operand.expression.substring(0, operand.start);
                        }
                        // update expression after operation tokens
                        if (operationAfter.length() > operand.expression.substring(operand.end).length()) {
                            operationAfter = operand.expression.substring(operand.end);
                        }
                        tokens.remove(operand);
                    } else {
                        throw (new AbsentOperandException(operator));
                    }
                }

                String operationResult = "";

                if (operator.text.charAt(0) == Token.PLUS) {
                    operationResult = Integer.parseInt(operands[0].text) + Integer.parseInt(operands[1].text) + "";
                }

                if (operator.text.charAt(0) == Token.MINUS) {
                    operationResult = Integer.parseInt(operands[0].text) - Integer.parseInt(operands[1].text) + "";
                }

                String expressionResult = operationBefore + operationResult + operationAfter;
                tokens.add(index, new Number(expressionResult, operationBefore.length(), operationBefore.length() + operationResult.length()));
                tokens.remove(token);

            }

            index++;

            if ((index == tokens.size()) && (tokens.size() > 1)) {
                throw (new AbsentOperatorException(tokens.get(index - 1)));
            }

        }

        return tokens.get(0).text;

    }

    ArrayList<Token> makeTokens(String expression) throws CommonException {

        // making tokens in RPN
        Tokenizer tokenizer = new Tokenizer(expression);
        ArrayList<Token> tokens = new ArrayList<Token>();
        ArrayList<Token> tokensStack = new ArrayList<Token>();

        while (tokenizer.hasNext()) {

            // read one token from the input stream
            Token token = tokenizer.nextToken();

            // If the token is a number (identifier), then add it to the output queue.
            if (token.type == Token.TYPE_NUMBER) {
                tokens.add(token);
                continue;
            }

            // If the token is a function token, then push it onto the stack.
            // TODO

            // If the token is a function argument separator (e.g., a comma):
            // TODO

            // If the token is an operator, op1, then:
            if (token.type == Token.TYPE_OPERATOR) {

                while (tokensStack.size() > 0) {

                    Token tokenStack = tokensStack.get(tokensStack.size() - 1);

                    // While there is an operator token, o2, at the top of the stack
                    // op1 is left-associative and its precedence is less than or equal to that of op2,
                    // or op1 is right-associative and its precedence is less than that of op2,
                    if (tokenStack.type == Token.TYPE_OPERATOR) {
                        Operator operator1 = (Operator) token;
                        Operator operator2 = (Operator) tokenStack;
                        if (((operator1.association == Operator.LEFT_TO_RIGHT) && (operator1.precedence <= operator2.precedence))
                                || ((operator1.association == Operator.RIGHT_TO_LEFT) && (operator1.precedence < operator2.precedence))) {
                            // Pop o2 off the stack, onto the output queue;
                            tokensStack.remove(tokenStack);
                            tokens.add(tokenStack);
                        }
                    } else {
                        break;
                    }

                }

                // push op1 onto the stack.
                tokensStack.add(token);
                continue;

            }

            if (token.type == Token.TYPE_PARENTHESIS) {

                // If the token is a left parenthesis, then push it onto the stack.
                if (token.text.charAt(0) == Token.PARENTHESISLEFT) {
                    tokensStack.add(token);
                    continue;
                }

                // If the token is a right parenthesis:
                if (token.text.charAt(0) == Token.PARENTHESISRIGHT) {

                    boolean parenthesesMatch = false;

                    // Until the token at the top of the stack is a left parenthesis,
                    // pop operators off the stack onto the output queue
                    while (tokensStack.size() > 0) {

                        Token tokenStack = tokensStack.get(tokensStack.size() - 1);

                        if (tokenStack.text.charAt(0) == Token.PARENTHESISLEFT) {
                            parenthesesMatch = true;
                            break;
                        } else {
                            tokensStack.remove(tokenStack);
                            tokens.add(tokenStack);
                        }

                    }

                    // If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
                    if (!parenthesesMatch) {
                        throw (new ParenthesesNotMatchException(token));
                    }

                    // Pop the left parenthesis from the stack, but not onto the output queue.
                    tokensStack.remove(tokensStack.size() - 1);

                    // If the token at the top of the stack is a function token, pop it onto the output queue.
                    // TODO

                    continue;

                }

            }

            if (token.type == Token.TYPE_UNKNOWN) {
                throw (new UnknownTokenException(token));
            }

        }

        // When there are no more tokens to read:
        // While there are still operator tokens in the stack:
        while (tokensStack.size() > 0) {

            Token tokenStack = tokensStack.get(tokensStack.size() - 1);

            if (tokenStack.type == Token.TYPE_PARENTHESIS) {
                throw (new ParenthesesNotMatchException(tokenStack));
            }

            tokensStack.remove(tokenStack);
            tokens.add(tokenStack);

        }

        return tokens;

    }

    class CommonException extends Exception {

        final Token token;
        final String message;

        CommonException(String message, Token token) {
            super();
            this.message = message;
            this.token = token;
        }

        @Override
        public String toString() {
            String position = "\tposition: " + token.start + "\n";
            String expression = "\texpression: " + token.expression.substring(0, token.start) + ">>-->" + token.expression.substring(token.start, token.end) + "<--<<" + token.expression.substring(token.end);
            return message + "\n" + position + expression;
        }

    }

    class ParenthesesNotMatchException extends CommonException {
        ParenthesesNotMatchException(Token token) {
            super("Parentheses did not match.", token);
        }
    }

    class UnknownTokenException extends CommonException {
        UnknownTokenException(Token token) {
            super("Unknown token.", token);
        }
    }

    class AbsentOperandException extends CommonException {
        AbsentOperandException(Token token) {
            super("Absent operand.", token);
        }
    }

    class AbsentOperatorException extends CommonException {
        AbsentOperatorException(Token token) {
            super("Absent operator.", token);
        }
    }

}
