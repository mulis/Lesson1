package token;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created with IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 08.10.12
 * Time: 2:15
 */
public enum FunctionType {

    SQR("sqr"),
    MIN("min"),
    MAX("max"),
    SUM("sum");

    public final String abbreviation;

    FunctionType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static FunctionType getFunctionType(String abbreviation) {
        if (abbreviation.equals(SQR.abbreviation)) return SQR;
        else if (abbreviation.equals(MAX.abbreviation)) return MAX;
        else if (abbreviation.equals(MIN.abbreviation)) return MIN;
        else if (abbreviation.equals(SUM.abbreviation)) return SUM;
        return null;
    }

    public BigDecimal compute(BigDecimal[] arguments) {

        return compute(arguments, MathContext.UNLIMITED);

    }

    public BigDecimal compute(BigDecimal[] arguments, MathContext mathContext) {

        BigDecimal result = null;

        if (abbreviation.equals(SQR.abbreviation)) {
            if (arguments.length == 1) {
                result = new BigDecimal(Math.sqrt(arguments[0].doubleValue()), mathContext);
            }
        } else if (abbreviation.equals(MIN.abbreviation)) {
            if (arguments.length > 1) {
                result = arguments[0];
                for (int i = 1; i < arguments.length; ++i) {
                    result = new BigDecimal(Math.min(result.doubleValue(), arguments[i].doubleValue()), mathContext);
                }
            }
        } else if (abbreviation.equals(MAX.abbreviation)) {
            if (arguments.length > 1) {
                result = arguments[0];
                for (int i = 1; i < arguments.length; ++i) {
                    result = new BigDecimal(Math.max(result.doubleValue(), arguments[i].doubleValue()), mathContext);
                }
            }
        } else if (abbreviation.equals(SUM.abbreviation)) {
            if (arguments.length > 0) {
                result = arguments[0];
                for (int i = 1; i < arguments.length; ++i) {
                    result = result.add(arguments[i], mathContext);
                }
            }
        }

        return result;

    }

}
