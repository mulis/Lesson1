package token;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:12
 */
public class NumberToken extends Token implements INumberToken {

    final BigDecimal value;

    public NumberToken(String expression, int start, int end) {
        super(TokenType.NUMBER, expression, start, end);
        this.value = new BigDecimal(this.text);
    }

    public NumberToken(String expression, int start, int end, BigDecimal value) {
        super(TokenType.NUMBER, expression, start, end);
        this.value = value;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

}
