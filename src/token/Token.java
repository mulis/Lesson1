package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:35
 */
public class Token implements IToken {

    final TokenType type;
    final String expression;
    final int start;
    final int end;
    final String text;

    public Token(TokenType type, String expression, int start, int end) {
        this.type = type;
        this.expression = expression;
        this.start = start;
        this.end = end;
        this.text = expression.substring(start, end);
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public String getText() {
        return text;
    }

}
