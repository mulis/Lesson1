package lesson1;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:16
 */
class Operator extends Token {

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

    Operator(String expression, int start, int end) {
        super(Token.TYPE_OPERATOR, expression, start, end);
        this.precedence = getPrecedence();
        this.association = getAssociation();
        this.argumentCount = getArgumentCount();
    }

    int getPrecedence() {

        if (text.charAt(0) == Token.PLUS && text.charAt(0) == Token.MINUS) {
            return 2;
        }

//        if (text.charAt(0) == Token.MULTIPLIER && text.charAt(0) == Token.DIVIDER) {
//            return 3;
//        }

        return 0;

    }

    boolean getAssociation() {

        if (text.charAt(0) == Token.PLUS && text.charAt(0) == Token.MINUS) {
            return Operator.LEFT_TO_RIGHT;
        }

        return Operator.LEFT_TO_RIGHT;

    }

    int getArgumentCount() {

        if (text.charAt(0) == Token.PLUS && text.charAt(0) == Token.MINUS) {
            return 2;
        }

        return 2;

    }

}
