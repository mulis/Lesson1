package calculator;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 17:56
 */
public interface ICalculator {

    ICalculator setVerbose(boolean verbose);

    BigDecimal calculate(String expression) throws CalculationException;

}
