package calculator;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:16
 */
public class OperatorToken extends Token {

    public static final char PLUS = '+';
    public static final char MINUS = '-';

    // precedence   operators       associativity
    // 4            !               right to left
    // 3            * / %           left to right
    // 2            + -             left to right
    // 1            =               right to left

    public static final boolean LEFT_TO_RIGHT = true;
    public static final boolean RIGHT_TO_LEFT = false;

    final int precedence;
    final boolean association;
    final int argumentCount;

    public OperatorToken(String expression, int start, int end) {
        super(TYPE_OPERATOR, expression, start, end);
        this.precedence = getPrecedence();
        this.association = getAssociation();
        this.argumentCount = getArgumentCount();
    }

    public int getPrecedence() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return 2;
        }

//        if (text.charAt(0) == Token.MULTIPLIER && text.charAt(0) == Token.DIVIDER) {
//            return 3;
//        }

        return 0;

    }

    public boolean getAssociation() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return LEFT_TO_RIGHT;
        }

        return LEFT_TO_RIGHT;

    }

    public int getArgumentCount() {

        if (text.charAt(0) == PLUS && text.charAt(0) == MINUS) {
            return 2;
        }

        return 2;

    }

}
