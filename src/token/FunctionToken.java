package token;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 08.10.12
 * Time: 1:54
 */
public class FunctionToken extends Token implements IFunctionToken {

    private final FunctionType functionType;
    private int argumentCount = 1;

    public FunctionToken(String expression, int start, int end) {

        super(TokenType.FUNCTION, expression, start, end);
        functionType = FunctionType.getFunctionType(text);

    }

    @Override
    public int getArgumentCount() {
        return argumentCount;
    }

    @Override
    public void setArgumentCount(int argumentCount) {
        this.argumentCount = argumentCount;
    }

    @Override
    public INumberToken compute(INumberToken[] argumentTokens) {

        return compute(argumentTokens, MathContext.UNLIMITED);

    }

    @Override
    public INumberToken compute(INumberToken[] argumentTokens, MathContext mathContext) {

        int start = getStart();
        int end = getEnd();
        ArrayList<BigDecimal> argumentsList = new ArrayList<BigDecimal>();

        for (INumberToken argumentToken : argumentTokens) {

            if (start > argumentToken.getStart()) {
                start = argumentToken.getStart();
            }

            if (end < argumentToken.getEnd()) {
                end = argumentToken.getEnd();
            }

            argumentsList.add(argumentToken.getValue());

        }

        BigDecimal result = functionType.compute(argumentsList.toArray(new BigDecimal[argumentsList.size()]), mathContext);

        return new NumberToken(expression, start, end, result);

    }

}
