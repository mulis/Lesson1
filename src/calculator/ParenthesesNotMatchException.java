package calculator;

import token.IToken;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 14:59
 */
public class ParenthesesNotMatchException extends CalculationException {
    ParenthesesNotMatchException(IToken token) {
        super("Parentheses did not match.", token);
    }
}
