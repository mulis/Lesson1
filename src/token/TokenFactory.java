package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 12:04
 */
public class TokenFactory {

    public static IToken makeToken(TokenType type, String expression, int start, int end) {

        if (type == TokenType.SIGNED_NUMBER) {
            return new NumberToken(expression, start + 1, end - 1);
        }

        if (type == TokenType.NUMBER) {
            return new NumberToken(expression, start, end);
        }

        if (type == TokenType.OPERATOR) {
            return new OperatorToken(expression, start, end);
        }

        if (type == TokenType.PARENTHESIS_LEFT) {
            return new Token(TokenType.PARENTHESIS_LEFT, expression, start, end);
        }

        if (type == TokenType.PARENTHESIS_RIGHT) {
            return new Token(TokenType.PARENTHESIS_RIGHT, expression, start, end);
        }

        return new Token(TokenType.UNKNOWN, expression, start, end);

    }

}
