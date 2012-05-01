package calculator;

import token.INumberToken;
import token.IOperatorToken;
import token.IToken;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:05
 */
public class Calculator implements ICalculator {

    private boolean verbose = false;

    @Override
    public ICalculator setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    @Override
    public BigDecimal calculate(String expression) throws CalculationException {

        ArrayList<IToken> tokens = makeTokens(expression);

        if (verbose) {
            System.out.println("Calculation");
            System.out.println(dumpTokens(tokens));
        }

        int index = 0;

        while (tokens.size() > 1) {

            IToken token = tokens.get(index);

            if (token.getType() == IToken.Type.OPERATOR) {

                IOperatorToken operator = (IOperatorToken) token;
                INumberToken[] operands = new INumberToken[operator.getArgumentCount()];

                index -= operator.getArgumentCount();
                if (index < 0) {
                    throw (new AbsentOperandException(operator));
                }

                for (int i = 0; i < operator.getArgumentCount(); i++) {

                    IToken operand = tokens.get(index);

                    if (operand.getType() == IToken.Type.NUMBER) {

                        operands[i] = (INumberToken) operand;
                        tokens.remove(operand);

                    } else {
                        throw (new AbsentOperandException(operator));
                    }

                }

                INumberToken result = operator.operate(operands);

                tokens.add(index, result);
                tokens.remove(operator);

                if (verbose) {
                    System.out.println(dumpTokens(tokens));
                }

            }

            index++;

            if ((index == tokens.size()) && (tokens.size() > 1)) {
                throw (new AbsentOperatorException(tokens.get(index - 1)));
            }

        }

        return ((INumberToken) tokens.get(0)).getValue();

    }

    private String dumpTokens(ArrayList<IToken> tokens) {

        String tokensText = "";

        for (IToken token : tokens) {

            tokensText += " ";

            if (token.getType() == IToken.Type.NUMBER) {
                tokensText += ((INumberToken) token).getValue();
                continue;
            }

            tokensText += token.getText();

        }

        return tokensText;

    }

    private ArrayList<IToken> makeTokens(String expression) throws CalculationException {

        // making tokens in RPN
        Tokenizer tokenizer = new Tokenizer(expression);
        ArrayList<IToken> tokens = new ArrayList<IToken>();
        ArrayList<IToken> tokenStack = new ArrayList<IToken>();

        if (verbose) {
            System.out.println("Tokenizing");
        }

        while (tokenizer.hasNext()) {

            // read one token from the input stream
            IToken token = tokenizer.nextToken();
            if (verbose) {
                System.out.println("token:" + token.getText() + " start:" + token.getStart() + " end:" + token.getEnd());
            }

            // If the token is a number (identifier), then add it to the output queue.
            if (token.getType() == IToken.Type.NUMBER) {
                tokens.add(token);
                continue;
            }

            // If the token is a function token, then push it onto the stack.
            // TODO

            // If the token is a function argument separator (e.g., a comma):
            // TODO

            // If the token is an operator, op1, then:
            if (token.getType() == IToken.Type.OPERATOR) {

                while (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    // While there is an operator token, o2, at the top of the stack
                    // op1 is left-associative and its precedence is less than or equal to that of op2,
                    // or op1 is right-associative and its precedence is less than that of op2,
                    if (tokenStackItem.getType() == IToken.Type.OPERATOR) {
                        IOperatorToken operator1 = (IOperatorToken) token;
                        IOperatorToken operator2 = (IOperatorToken) tokenStackItem;
                        if (((operator1.getAssociation() == IOperatorToken.LEFT_TO_RIGHT) && (operator1.getPrecedence() <= operator2.getPrecedence()))
                                || ((operator1.getAssociation() == IOperatorToken.RIGHT_TO_LEFT) && (operator1.getPrecedence() < operator2.getPrecedence()))) {
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

            // If the token is a left parenthesis, then push it onto the stack.
            if (token.getType() == IToken.Type.PARENTHESIS_LEFT) {
                tokenStack.add(token);
                continue;
            }

            // If the token is a right parenthesis:
            if (token.getType() == IToken.Type.PARENTHESIS_RIGHT) {

                boolean parenthesesMatch = false;

                // Until the token at the top of the stack is a left parenthesis,
                // pop operators off the stack onto the output queue
                while (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    if (tokenStackItem.getType() == IToken.Type.PARENTHESIS_LEFT) {
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

            if (token.getType() == IToken.Type.UNKNOWN) {
                throw (new UnknownTokenException(token));
            }

        }

        // When there are no more tokens to read:
        // While there are still operator tokens in the stack:
        while (tokenStack.size() > 0) {

            IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

            if ((tokenStackItem.getType() == IToken.Type.PARENTHESIS_LEFT) || (tokenStackItem.getType() == IToken.Type.PARENTHESIS_RIGHT)) {
                throw (new ParenthesesNotMatchException(tokenStackItem));
            }

            tokenStack.remove(tokenStackItem);
            tokens.add(tokenStackItem);

        }

        return tokens;

    }

}
