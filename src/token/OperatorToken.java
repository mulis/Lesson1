package token;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

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
    public INumberToken operate(INumberToken[] operandTokens) {

        return operate(operandTokens, MathContext.UNLIMITED);

    }

    @Override
    public INumberToken operate(INumberToken[] operandTokens, MathContext mathContext) {

        int start = getStart();
        int end = getEnd();
        ArrayList<BigDecimal> operandsList = new ArrayList<BigDecimal>();

        for (INumberToken operandToken : operandTokens) {

            if (start > operandToken.getStart()) {
                start = operandToken.getStart();
            }

            if (end < operandToken.getEnd()) {
                end = operandToken.getEnd();
            }

            operandsList.add(operandToken.getValue());

        }

        BigDecimal result = operatorType.operate(operandsList.toArray(new BigDecimal[operandsList.size()]), mathContext);

        return new NumberToken(expression, start, end, result);

    }

}
