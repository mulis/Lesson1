package calculator;

import token.Token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 15:00
 */
public class AbsentOperatorException extends CalculationException {
    AbsentOperatorException(Token token) {
        super("Absent operator.", token);
    }
}
