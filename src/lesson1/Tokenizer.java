package lesson1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:29
 */
public class Tokenizer {

    String expression;
    int expressionPosition;

    //Matcher digitsMatcher = Pattern.compile("\\G\\d+").matcher("");
    Matcher digitsMatcher = Pattern.compile("\\G\\d*\\.?\\d+").matcher("");
    Matcher operatorsMatcher = Pattern.compile("\\G[\\+?\\-?]").matcher("");
    Matcher parenthesesMatcher = Pattern.compile("\\G[\\(?\\)?]").matcher("");

    Tokenizer(String expression) {
        this.expression = expression;
        expressionPosition = 0;
        digitsMatcher.reset(expression);
        operatorsMatcher.reset(expression);
        parenthesesMatcher.reset(expression);
        skipSpaces();
    }

    Token nextToken() {

        if (digitsMatcher.find(expressionPosition)) {
            expressionPosition = digitsMatcher.end();
            skipSpaces();
            return new Number(expression, digitsMatcher.start(), digitsMatcher.end());
        }

        if (operatorsMatcher.find(expressionPosition)) {
            expressionPosition = operatorsMatcher.end();
            skipSpaces();
            return new Operator(expression, operatorsMatcher.start(), operatorsMatcher.end());
        }

        if (parenthesesMatcher.find(expressionPosition)) {
            expressionPosition = parenthesesMatcher.end();
            skipSpaces();
            return new Token(Token.TYPE_PARENTHESIS, expression, parenthesesMatcher.start(), parenthesesMatcher.end());
        }

        return new Token(Token.TYPE_UNKNOWN, expression, expressionPosition, expression.length());

    }
    
    Boolean hasNext() {
        return (expressionPosition < expression.length()) ? true : false;
    }

    void skipSpaces() {

        while (hasNext() && (expression.charAt(expressionPosition) == Token.SPACE)) {
            expressionPosition++;
        }

    }

}
