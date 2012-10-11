package token;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created with IntelliJ IDEA.
 * User: Mulishov Serhij
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
        else if (sign == SUBTRACTION.sign) return SUBTRACTION;
        else if (sign == MULTIPLICATION.sign) return MULTIPLICATION;
        else if (sign == DIVISION.sign) return DIVISION;
        else if (sign == EXPONENTIATION.sign) return EXPONENTIATION;
        return null;
    }

    public BigDecimal operate(BigDecimal[] operands) {

        return operate(operands, MathContext.UNLIMITED);

    }

    public BigDecimal operate(BigDecimal[] operands, MathContext mathContext) {

        BigDecimal result = null;

        if (sign == ADDITION.sign) {
            result = operands[0].add(operands[1], mathContext);
        } else if (sign == SUBTRACTION.sign) {
            result = operands[0].subtract(operands[1], mathContext);
        } else if (sign == MULTIPLICATION.sign) {
            result = operands[0].multiply(operands[1], mathContext);
        } else if (sign == DIVISION.sign) {
            result = operands[0].divide(operands[1], mathContext);
        } else if (sign == EXPONENTIATION.sign) {
            result = new BigDecimal(Math.pow(operands[0].doubleValue(), operands[1].doubleValue()), mathContext);
        }

        return result;

    }

}
