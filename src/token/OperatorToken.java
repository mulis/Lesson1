package token;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:16
 */
public class OperatorToken extends Token implements IOperatorToken {

    private final OperatorType operator;

    public OperatorToken(String expression, int start, int end) {
        super(Type.OPERATOR, expression, start, end);
        operator = OperatorType.getOperator(text.charAt(0));
    }

    @Override
    public int getPrecedence() {

        return operator.precedence;

    }

    @Override
    public int getAssociation() {

        return operator.associativity;

    }

    @Override
    public int getArgumentCount() {

        return operator.argumentCount;

    }

    @Override
    public INumberToken operate(INumberToken[] operands) {

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

        if (operator == OperatorType.PLUS) {
            result = operands[0].getValue().add(operands[1].getValue());
        }

        if (operator == OperatorType.MINUS) {
            result = operands[0].getValue().subtract(operands[1].getValue());
        }

        if (operator == OperatorType.MULTIPLIER) {
            result = operands[0].getValue().multiply(operands[1].getValue());
        }

        if (operator == OperatorType.DIVIDER) {
            result = operands[0].getValue().divide(operands[1].getValue());
        }

        if (operator == OperatorType.EXPONENT) {
            result = operands[0].getValue().pow(operands[1].getValue().intValueExact());
        }

        return new NumberToken(expression, start, end, result);

    }

}
