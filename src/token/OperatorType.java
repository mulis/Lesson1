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

    ADDITION('+', 2, AssociativityType.LEFT_TO_RIGHT, 2),
    SUBTRACTION('-', 2, AssociativityType.LEFT_TO_RIGHT, 2),
    MULTIPLICATION('*', 3, AssociativityType.LEFT_TO_RIGHT, 2),
    DIVISION('/', 3, AssociativityType.LEFT_TO_RIGHT, 2),
    EXPONENTIATION('^', 3, AssociativityType.LEFT_TO_RIGHT, 2);

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

    static OperatorType getOperatorType(char sign) {
        if (sign == ADDITION.sign) return ADDITION;
        if (sign == SUBTRACTION.sign) return SUBTRACTION;
        if (sign == MULTIPLICATION.sign) return MULTIPLICATION;
        if (sign == DIVISION.sign) return DIVISION;
        if (sign == EXPONENTIATION.sign) return EXPONENTIATION;
        return null;
    }

}
