package calculator;

import token.TokenType;
import token.IToken;
import token.TokenFactory;

import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:29
 */
public class Tokenizer {

    private final String expression;
    private int expressionPosition;

    public Tokenizer(String expression) {
        this.expression = expression;
        reset(expression);
    }

    public void reset(String expression) {

        for (TokenType type : TokenType.values()) {
            type.matcher.reset(expression);
        }
        expressionPosition = 0;
        skipSpaces();

    }

    public IToken nextToken() {

        TokenType nextTokenType = TokenType.UNKNOWN;
        int nextTokenStart = expressionPosition;
        int nextTokenEnd = expression.length();

        for (TokenType type : TokenType.values()) {

            Matcher matcher = type.matcher;

            if (matcher.find(expressionPosition)) {
                nextTokenType = type;
                nextTokenStart = matcher.start();
                nextTokenEnd = matcher.end();
                expressionPosition = matcher.end();
                skipSpaces();
                break;
            }

        }

        return TokenFactory.makeToken(nextTokenType, expression, nextTokenStart, nextTokenEnd);

    }

    public Boolean hasNext() {
        return (expressionPosition < expression.length());
    }

    void skipSpaces() {

        while (hasNext() && (expression.charAt(expressionPosition) == ' ')) {
            expressionPosition++;
        }

    }

}
