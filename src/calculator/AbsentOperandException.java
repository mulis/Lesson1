package calculator;

import token.IToken;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 15:00
 */
public class AbsentOperandException extends CalculationException {
    AbsentOperandException(IToken token) {
        super("Absent operand.", token);
    }
}
