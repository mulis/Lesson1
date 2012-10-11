package calculator;

import token.IToken;

/**
 * Created with IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 09.10.12
 * Time: 4:10
 */
public class AbsentArgumentException extends CalculationException {
    AbsentArgumentException(IToken token) {
        super("Absent argument.", token);
    }
}
