package token;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:16
 */
public class OperatorToken extends Token implements IOperatorToken {

    private final OperatorType operatorType;

    public OperatorToken(String expression, int start, int end) {
        super(TokenType.OPERATOR, expression, start, end);
        operatorType = OperatorType.getOperatorType(text.charAt(0));
    }

    @Override
    public int getPrecedence() {

        return operatorType.precedence;

    }

    @Override
    public int getAssociation() {

        return operatorType.associativity;

    }

    @Override
    public int getArgumentCount() {

        return operatorType.argumentCount;

    }

    @Override
    public INumberToken operate(INumberToken[] operands) {

        return operate(operands, MathContext.UNLIMITED);

    }

    @Override
    public INumberToken operate(INumberToken[] operands, MathContext mathContext) {

        int start = getStart();
        int end = getEnd();

        for (IToken operand : operands) {
            if (start > operand.getStart()) {
                start = operand.getStart();
            }

            if (end < operand.getEnd()) {
                end = operand.getEnd();
            }
        }

        BigDecimal result = null;

        if (operatorType == OperatorType.ADDITION) {
            result = operands[0].getValue().add(operands[1].getValue(), mathContext);
        }

        if (operatorType == OperatorType.SUBTRACTION) {
            result = operands[0].getValue().subtract(operands[1].getValue(), mathContext);
        }

        if (operatorType == OperatorType.MULTIPLICATION) {
            result = operands[0].getValue().multiply(operands[1].getValue(), mathContext);
        }

        if (operatorType == OperatorType.DIVISION) {
            result = operands[0].getValue().divide(operands[1].getValue(), mathContext);
        }

        if (operatorType == OperatorType.EXPONENTIATION) {
            result = new BigDecimal(Math.pow(operands[0].getValue().doubleValue(), operands[1].getValue().doubleValue()), mathContext);
        }

        return new NumberToken(expression, start, end, result);

    }

}
