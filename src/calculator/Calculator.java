package calculator;

import token.NumberToken;
import token.OperatorToken;
import token.Token;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:05
 */
public class Calculator {

    private boolean verbose = false;

    public Calculator setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public BigDecimal calculate(String expression) throws CalculationException {

        ArrayList<Token> tokens = makeTokens(expression);

        if (verbose) {
            System.out.println("Calculation");
            printTokens(tokens);
        }

        int index = 0;

        while (tokens.size() > 1) {

            Token token = tokens.get(index);

            if (token.getType() == Token.TYPE_OPERATOR) {

                OperatorToken operator = (OperatorToken) token;
                NumberToken[] operands = new NumberToken[operator.getArgumentCount()];
                int start = operator.getStart();
                int end = operator.getEnd();

                index -= operator.getArgumentCount();
                if (index < 0) {
                    throw (new AbsentOperandException(operator));
                }

                for (int i = 0; i < operator.getArgumentCount(); i++) {

                    Token operand = tokens.get(index);

                    if (operand.getType() == Token.TYPE_NUMBER) {

                        operands[i] = (NumberToken) operand;

                        if (start > operand.getStart()) {
                            start = operand.getStart();
                        }

                        if (end < operand.getEnd()) {
                            end = operand.getEnd();
                        }

                        tokens.remove(operand);

                    } else {
                        throw (new AbsentOperandException(operator));
                    }

                }

                BigDecimal operationResult = operator.operate(operands);

                tokens.add(index, new NumberToken(expression, start, end, operationResult));
                tokens.remove(operator);

                if (verbose) {
                    printTokens(tokens);
                }

            }

            index++;

            if ((index == tokens.size()) && (tokens.size() > 1)) {
                throw (new AbsentOperatorException(tokens.get(index - 1)));
            }

        }

        return ((NumberToken) tokens.get(0)).getValue();

    }

    private void printTokens(ArrayList<Token> tokens) {

        String tokensText = "";

        for (Token token : tokens) {

            tokensText += " ";

            if (token.getType() == Token.TYPE_NUMBER) {
                tokensText += ((NumberToken) token).getValue();
                continue;
            }

            tokensText += token.getText();

        }

        System.out.println(tokensText);

    }

    ArrayList<Token> makeTokens(String expression) throws CalculationException {

        // making tokens in RPN
        Tokenizer tokenizer = new Tokenizer(expression);
        ArrayList<Token> tokens = new ArrayList<Token>();
        ArrayList<Token> tokenStack = new ArrayList<Token>();

        if (verbose) {
            System.out.println("Tokenizing");
        }

        while (tokenizer.hasNext()) {

            // read one token from the input stream
            Token token = tokenizer.nextToken();
            if (verbose) {
                System.out.println("token:" + token.getText() + " start:" + token.getStart() + " end:" + token.getEnd());
            }

            // If the token is a number (identifier), then add it to the output queue.
            if (token.getType() == Token.TYPE_NUMBER) {
                tokens.add(token);
                continue;
            }

            // If the token is a function token, then push it onto the stack.
            // TODO

            // If the token is a function argument separator (e.g., a comma):
            // TODO

            // If the token is an operator, op1, then:
            if (token.getType() == Token.TYPE_OPERATOR) {

                while (tokenStack.size() > 0) {

                    Token tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    // While there is an operator token, o2, at the top of the stack
                    // op1 is left-associative and its precedence is less than or equal to that of op2,
                    // or op1 is right-associative and its precedence is less than that of op2,
                    if (tokenStackItem.getType() == Token.TYPE_OPERATOR) {
                        OperatorToken operator1 = (OperatorToken) token;
                        OperatorToken operator2 = (OperatorToken) tokenStackItem;
                        if (((operator1.getAssociation() == OperatorToken.LEFT_TO_RIGHT) && (operator1.getPrecedence() <= operator2.getPrecedence()))
                                || ((operator1.getAssociation() == OperatorToken.RIGHT_TO_LEFT) && (operator1.getPrecedence() < operator2.getPrecedence()))) {
                            // Pop o2 off the stack, onto the output queue;
                            tokenStack.remove(tokenStackItem);
                            tokens.add(tokenStackItem);
                        }
                    } else {
                        break;
                    }

                }

                // push op1 onto the stack.
                tokenStack.add(token);
                continue;

            }

            if (token.getType() == Token.TYPE_PARENTHESIS) {

                // If the token is a left parenthesis, then push it onto the stack.
                if (token.getText().charAt(0) == Token.PARENTHESIS_LEFT) {
                    tokenStack.add(token);
                    continue;
                }

                // If the token is a right parenthesis:
                if (token.getText().charAt(0) == Token.PARENTHESIS_RIGHT) {

                    boolean parenthesesMatch = false;

                    // Until the token at the top of the stack is a left parenthesis,
                    // pop operators off the stack onto the output queue
                    while (tokenStack.size() > 0) {

                        Token tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                        if (tokenStackItem.getText().charAt(0) == Token.PARENTHESIS_LEFT) {
                            parenthesesMatch = true;
                            break;
                        } else {
                            tokenStack.remove(tokenStackItem);
                            tokens.add(tokenStackItem);
                        }

                    }

                    // If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
                    if (!parenthesesMatch) {
                        throw (new ParenthesesNotMatchException(token));
                    }

                    // Pop the left parenthesis from the stack, but not onto the output queue.
                    tokenStack.remove(tokenStack.size() - 1);

                    // If the token at the top of the stack is a function token, pop it onto the output queue.
                    // TODO

                    continue;

                }

            }

            if (token.getType() == Token.TYPE_UNKNOWN) {
                throw (new UnknownTokenException(token));
            }

        }

        // When there are no more tokens to read:
        // While there are still operator tokens in the stack:
        while (tokenStack.size() > 0) {

            Token tokenStackItem = tokenStack.get(tokenStack.size() - 1);

            if (tokenStackItem.getType() == Token.TYPE_PARENTHESIS) {
                throw (new ParenthesesNotMatchException(tokenStackItem));
            }

            tokenStack.remove(tokenStackItem);
            tokens.add(tokenStackItem);

        }

        return tokens;

    }

}
