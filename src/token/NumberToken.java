package token;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:12
 */
public class NumberToken extends Token {

    final BigDecimal value;

    public NumberToken(String expression, int start, int end, String value) {
        super(TYPE_NUMBER, expression, start, end);
        this.value = new BigDecimal(value).stripTrailingZeros();
    }

    public NumberToken(String expression, int start, int end, BigDecimal value) {
        super(TYPE_NUMBER, expression, start, end);
        this.value = value.stripTrailingZeros();
    }

    public BigDecimal getValue() {
        return value;
    }

}
