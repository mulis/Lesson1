package lesson1.Calculator;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
class ParenthesesNotMatchException extends CalculationException {
    ParenthesesNotMatchException(Token token) {
        super("Parentheses did not match.", token);
    }
}
