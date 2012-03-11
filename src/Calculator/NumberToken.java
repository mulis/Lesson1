package calculator;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:12
 */
public class NumberToken extends Token {

    final String value;

    public NumberToken(String expression, int start, int end, String value) {
        super(TYPE_NUMBER, expression, start, end);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
