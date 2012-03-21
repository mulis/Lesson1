package calculator;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 14:59
 */
public class CalculationException extends Exception {

    final public Token token;
    final public String message;

    CalculationException(String message, Token token) {
        super();
        this.message = message;
        this.token = token;
    }

    @Override
    public String toString() {
        String position = "position: " + token.getStart() + "\n";
        String expression = "expression: " + token.getExpression().substring(0, token.getStart()) + " >>--> " + token.getExpression().substring(token.getStart(), token.getEnd()) + " <--<< " + token.getExpression().substring(token.getEnd());
        return message + "\n" + position + expression;
    }

}
