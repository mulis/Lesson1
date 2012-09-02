package token;

/**
 * Created with IntelliJ IDEA.
 * User: rees
 * Date: 02.09.12
 * Time: 1:02
 */
public enum OperatorType {

    // operators       precedence   associativity
    // !               4            right to left
    // * / ^           3            left to right
    // + -             2            left to right
    // =               1            right to left

    PLUS('+', 2, OperatorType.LEFT_TO_RIGHT, 2),
    MINUS('-', 2, OperatorType.LEFT_TO_RIGHT, 2),
    MULTIPLIER('*', 3, OperatorType.LEFT_TO_RIGHT, 2),
    DIVIDER('/', 3, OperatorType.LEFT_TO_RIGHT, 2),
    EXPONENT('^', 3, OperatorType.LEFT_TO_RIGHT, 2);

    public final static int LEFT_TO_RIGHT = 1;
    public final static int RIGHT_TO_LEFT = -1;

    public final char sign;
    public final int precedence;
    public final int associativity;
    public final int argumentCount;

    OperatorType(char sign, int precedence, int associativity, int argumentCount) {
        this.sign = sign;
        this.precedence = precedence;
        this.associativity = associativity;
        this.argumentCount = argumentCount;
    }

    static OperatorType getOperator(char sign) {
        if (sign == PLUS.sign) return PLUS;
        if (sign == MINUS.sign) return MINUS;
        if (sign == MULTIPLIER.sign) return MULTIPLIER;
        if (sign == DIVIDER.sign) return DIVIDER;
        if (sign == EXPONENT.sign) return EXPONENT;
        return null;
    }

}
