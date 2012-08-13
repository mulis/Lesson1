package calculator;

import token.IToken;
import token.TokenFactory;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:29
 */
public class Tokenizer {

    private final String expression;
    private int expressionPosition;
    private Map<IToken.Type, Matcher> matchers;

    public Tokenizer(String expression) {
        this.expression = expression;
        initMatchers();
        reset(expression);
    }

    private void initMatchers() {

        matchers = new EnumMap<IToken.Type, Matcher>(IToken.Type.class);
        matchers.put(IToken.Type.SIGNED_NUMBER, Pattern.compile("\\G\\([\\+\\-]\\d*\\.?\\d+\\)").matcher("")); // signed number must be enclosed in parentheses
        matchers.put(IToken.Type.NUMBER, Pattern.compile("\\G\\d*\\.?\\d+").matcher(""));
        matchers.put(IToken.Type.OPERATOR, Pattern.compile("\\G[\\+\\-]").matcher(""));
        matchers.put(IToken.Type.PARENTHESIS_LEFT, Pattern.compile("\\G\\(").matcher(""));
        matchers.put(IToken.Type.PARENTHESIS_RIGHT, Pattern.compile("\\G\\)").matcher(""));
        matchers.put(IToken.Type.UNKNOWN, Pattern.compile("\\G\\.+").matcher(""));

    }

    public void reset(String expression) {

        for (IToken.Type type : IToken.Type.values()) {
            matchers.get(type).reset(expression);
        }
        expressionPosition = 0;
        skipSpaces();

    }

    public IToken nextToken() {

        IToken.Type nextTokenType = IToken.Type.UNKNOWN;
        int nextTokenStart = expressionPosition;
        int nextTokenEnd = expression.length();

        for (IToken.Type type : IToken.Type.values()) {

            Matcher matcher = matchers.get(type);

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
