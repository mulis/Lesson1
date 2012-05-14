package token;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:16
 */
public class OperatorToken extends Token implements IOperatorToken {

    // precedence   operators       associativity
    // 4            !               right to left
    // 3            * / %           left to right
    // 2            + -             left to right
    // 1            =               right to left

    final int precedence;
    final int association;
    final int argumentCount;

    public OperatorToken(String expression, int start, int end) {
        super(Type.OPERATOR, expression, start, end);
        this.precedence = getPrecedence();
        this.association = getAssociation();
        this.argumentCount = getArgumentCount();
    }

    @Override
    public int getPrecedence() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return 2;
        }

//        if (text.charAt(0) == Token.MULTIPLIER && text.charAt(0) == Token.DIVIDER) {
//            return 3;
//        }

        return 0;

    }

    @Override
    public int getAssociation() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return LEFT_TO_RIGHT;
        }

        return LEFT_TO_RIGHT;

    }

    @Override
    public int getArgumentCount() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return 2;
        }

        return 2;

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

        if (text.charAt(0) == IOperatorToken.PLUS) {
            result = operands[0].getValue().add(operands[1].getValue());
        }

        if (text.charAt(0) == IOperatorToken.MINUS) {
            result = operands[0].getValue().subtract(operands[1].getValue());
        }

        return new NumberToken(expression, start, end, result);

    }

}
