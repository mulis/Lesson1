package calculator;

import token.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:05
 */
public class Calculator implements ICalculator {

    MathContext mathContext = MathContext.DECIMAL64;

    private Logger logger = Logger.getLogger(Calculator.class.getName());
    private StringBuffer operationalBuffer = new StringBuffer();

    @Override
    public void setMathContext(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public MathContext getMathContext() {
        return mathContext;
    }

    @Override
    public BigDecimal calculate(String expression) throws CalculationException {

        ArrayList<IToken> tokens;

        try {

            logger.info("Expression:\n\t" + expression);

            operationalBuffer.setLength(0);
            operationalBuffer.append("Process calculation:");
            tokens = makeTokens(expression);

            operationalBuffer.append("\nCalculation:");
            operationalBuffer.append(dumpTokens(tokens));

            int index = 0;

            while (tokens.size() > 1) {

                IToken token = tokens.get(index);

                if (token.getType() == TokenType.OPERATOR) {

                    IOperatorToken operator = (IOperatorToken) token;
                    INumberToken[] operands = new INumberToken[operator.getArgumentCount()];

                    index -= operator.getArgumentCount();
                    if (index < 0) {
                        throw (new AbsentOperandException(operator));
                    }

                    for (int i = 0; i < operator.getArgumentCount(); i++) {

                        IToken operand = tokens.get(index);

                        if (operand.getType() == TokenType.NUMBER) {

                            operands[i] = (INumberToken) operand;
                            tokens.remove(operand);

                        } else {
                            throw (new AbsentOperandException(operator));
                        }

                    }

                    INumberToken result;

                    try {
                        result = operator.operate(operands, mathContext);
                    } catch (Exception ex) {
                        throw (new CalculationException(ex.getMessage(), operator));
                    }

                    tokens.add(index, result);
                    tokens.remove(operator);

                    operationalBuffer.append(dumpTokens(tokens));

                }

                if (token.getType() == TokenType.FUNCTION) {

                    IFunctionToken function = (IFunctionToken) token;
                    INumberToken[] arguments = new INumberToken[function.getArgumentCount()];

                    index -= function.getArgumentCount();
                    if (index < 0) {
                        throw (new AbsentArgumentException(function));
                    }

                    for (int i = 0; i < function.getArgumentCount(); i++) {

                        IToken argument = tokens.get(index);

                        if (argument.getType() == TokenType.NUMBER) {

                            arguments[i] = (INumberToken) argument;
                            tokens.remove(argument);

                        } else {
                            throw (new AbsentArgumentException(function));
                        }

                    }

                    INumberToken result;

                    try {
                        result = function.compute(arguments, mathContext);
                    } catch (Exception ex) {
                        throw (new CalculationException(ex.getMessage(), function));
                    }

                    tokens.add(index, result);
                    tokens.remove(function);

                    operationalBuffer.append(dumpTokens(tokens));

                }

                index++;

                if ((index == tokens.size()) && (tokens.size() > 1)) {
                    throw (new AbsentOperatorException(tokens.get(index - 1)));
                }

            }

            logger.fine(operationalBuffer.toString());

        } catch (CalculationException ex) {
            logger.fine(operationalBuffer.toString());
            logger.severe(ex.toString());
            throw (ex);
        }

        BigDecimal result = ((INumberToken) tokens.get(0)).getValue().add(BigDecimal.ZERO, mathContext);

        logger.info("Result:\n\t" + result.toString());

        return result;

    }

    @Override
    public StringBuffer getProcessBuffer() {
        return operationalBuffer;
    }

    private StringBuffer dumpTokens(ArrayList<IToken> tokens) {

        StringBuffer tokensText = new StringBuffer();
        tokensText.append("\n\t");

        for (IToken token : tokens) {

            tokensText.append(" ");

            if (token.getType() == TokenType.NUMBER) {
                tokensText.append(((INumberToken) token).getValue());
                continue;
            }

            tokensText.append(token.getText());

        }

        return tokensText;

    }

    private ArrayList<IToken> makeTokens(String expression) throws CalculationException {

        // making tokens in RPN
        Tokenizer tokenizer = new Tokenizer(expression);
        ArrayList<IToken> tokens = new ArrayList<IToken>();
        ArrayList<IToken> tokenStack = new ArrayList<IToken>();

        operationalBuffer.append("\nTokenizing:");

        while (tokenizer.hasNext()) {

            // read one token from the input stream
            IToken token = tokenizer.nextToken();
            operationalBuffer.append("\n\ttoken:").append(token.getText()).append(" start:").append(token.getStart()).append(" end:").append(token.getEnd());

            // If the token is a number (identifier), then add it to the output queue.
            if ((token.getType() == TokenType.NUMBER) || (token.getType() == TokenType.SIGNED_NUMBER)) {
                tokens.add(token);
                continue;
            }

            // If the token is a function token, then push it onto the stack.
            else if (token.getType() == TokenType.FUNCTION) {
                tokenStack.add(token);
                continue;
            }

            // If the token is a function argument separator (e.g., a comma):
            else if (token.getType() == TokenType.FUNCTION_ARGUMENT_SEPARATOR) {

                //tokenStack.add(token);

                for (int i = tokenStack.size() - 1; i > -1; --i) {

                    IToken tokenStackItem = tokenStack.get(i);

                    if (tokenStackItem.getType() == TokenType.FUNCTION) {

                        IFunctionToken function = (IFunctionToken) tokenStackItem;
                        function.setArgumentCount(function.getArgumentCount() + 1);
                        break;

                    }

                }

                boolean parenthesesMatch = false;

                while (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    if (tokenStackItem.getType() == TokenType.PARENTHESIS_LEFT) {
                        parenthesesMatch = true;
                        break;
                    } else {
                        // Until the token at the top of the stack is a left parenthesis,
                        // pop operators off the stack onto the output queue.
                        tokenStack.remove(tokenStackItem);
                        tokens.add(tokenStackItem);
                    }

                }

                // If no left parentheses are encountered, either the separator was misplaced
                // or parentheses were mismatched.
                if (!parenthesesMatch) {
                    throw (new ParenthesesNotMatchException(token));
                }

            }

            // If the token is an operator, op1, then:
            else if (token.getType() == TokenType.OPERATOR) {

                while (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    // While there is an operator token, o2, at the top of the stack
                    // op1 is left-associative and its precedence is less than or equal to that of op2,
                    // or op1 is right-associative and its precedence is less than that of op2,
                    if (tokenStackItem.getType() == TokenType.OPERATOR) {
                        IOperatorToken operator1 = (IOperatorToken) token;
                        IOperatorToken operator2 = (IOperatorToken) tokenStackItem;
                        if (((operator1.getAssociation() == AssociativityType.LEFT_TO_RIGHT) && (operator1.getPrecedence() <= operator2.getPrecedence()))
                                || ((operator1.getAssociation() == AssociativityType.RIGHT_TO_LEFT) && (operator1.getPrecedence() < operator2.getPrecedence()))) {
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
            else if (token.getType() == TokenType.PARENTHESIS_LEFT) {
                tokenStack.add(token);
                continue;
            }

            // If the token is a right parenthesis:
            else if (token.getType() == TokenType.PARENTHESIS_RIGHT) {

                boolean parenthesesMatch = false;

                // Until the token at the top of the stack is a left parenthesis,
                // pop operators off the stack onto the output queue
                while (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    if (tokenStackItem.getType() == TokenType.PARENTHESIS_LEFT) {
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
                if (tokenStack.size() > 0) {

                    IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

                    if (tokenStackItem.getType() == TokenType.FUNCTION) {
                        tokenStack.remove(tokenStackItem);
                        tokens.add(tokenStackItem);
                    }

                }

                continue;

            } else if (token.getType() == TokenType.UNKNOWN) {
                throw (new UnknownTokenException(token));
            }

        }

        // When there are no more tokens to read:
        // While there are still operator tokens in the stack:
        while (tokenStack.size() > 0) {

            IToken tokenStackItem = tokenStack.get(tokenStack.size() - 1);

            if ((tokenStackItem.getType() == TokenType.PARENTHESIS_LEFT) || (tokenStackItem.getType() == TokenType.PARENTHESIS_RIGHT)) {
                throw (new ParenthesesNotMatchException(tokenStackItem));
            }

            tokenStack.remove(tokenStackItem);
            tokens.add(tokenStackItem);

        }

        return tokens;

    }

}
