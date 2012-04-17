package calculator;

import token.IToken;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 11.03.12
 * Time: 14:59
 */
public class CalculationException extends Exception {

    final IToken token;
    final String message;

    CalculationException(String message, IToken token) {
        super();
        this.message = message;
        this.token = token;
    }

    @Override
    public String toString() {
        String position = "\tposition: " + token.getStart() + "\n";
        String expression = "\texpression: " + token.getExpression().substring(0, token.getStart()) + " >>--> " + token.getExpression().substring(token.getStart(), token.getEnd()) + " <--<< " + token.getExpression().substring(token.getEnd());
        return message + "\n" + position + expression;
    }

}
