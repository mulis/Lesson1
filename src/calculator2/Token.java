package calculator2;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:35
 */
public class Token {

    public static final int TYPE_UNKNOWN = 0;
    public static final char SPACE = ' ';
    public static final int TYPE_NUMBER = 1;
    //public static final char ZERO = "0".charAt(0);
    //public static final char NINE = "9".charAt(0);
    public static final int TYPE_OPERATOR = 2;
    public static final int TYPE_PARENTHESIS = 3;
    public static final char PARENTHESISLEFT = '(';
    public static final char PARENTHESISRIGHT = ')';

    final int type;
    final String expression;
    final int start;
    final int end;
    final String text;

    public Token(int type, String expression, int start, int end) {
        this.type = type;
        this.expression = expression;
        this.start = start;
        this.end = end;
        this.text = expression.substring(start, end);
    }

    public int getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getExpression() {
        return expression;
    }

    public String getText() {
        return text;
    }

}
