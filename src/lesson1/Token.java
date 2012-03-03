package lesson1;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 29.02.12
 * Time: 22:35
 */
public class Token {

    public static final int TYPE_UNKNOWN = 0;
    public static final char SPACE = " ".charAt(0);
    public static final int TYPE_NUMBER = 1;
    public static final char ZERO = "0".charAt(0);
    public static final char NINE = "9".charAt(0);
    public static final int TYPE_OPERATOR = 2;
    public static final char PLUS = "+".charAt(0);
    public static final char MINUS = "-".charAt(0);
    public static final int TYPE_PARENTHESIS = 3;
    public static final char PARENTHESISLEFT = "(".charAt(0);
    public static final char PARENTHESISRIGHT = ")".charAt(0);
    
    int type;
    String expression;
    int start;
    int end;
    String text;
    
    Token(int type, String expression, int start, int end) {
        this.type = type;
        this.expression = expression;
        this.start = start;
        this.end = end;
        this.text = expression.substring(start, end);
    }

}
