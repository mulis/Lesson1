package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 12:04
 */
public class TokenFactory {

    public static IToken makeToken(IToken.Type type, String expression, int start, int end) {

        if (type == IToken.Type.NUMBER) {
            return new NumberToken(expression, start, end);
        }

        if (type == IToken.Type.OPERATOR) {
            return new OperatorToken(expression, start, end);
        }

        if (type == IToken.Type.PARENTHESIS_LEFT) {
            return new Token(IToken.Type.PARENTHESIS_LEFT, expression, start, end);
        }

        if (type == IToken.Type.PARENTHESIS_RIGHT) {
            return new Token(IToken.Type.PARENTHESIS_RIGHT, expression, start, end);
        }

        return new Token(IToken.Type.UNKNOWN, expression, start, end);

    }

}
