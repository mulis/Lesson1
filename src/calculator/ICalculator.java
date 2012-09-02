package calculator;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 17:56
 */
public interface ICalculator {

    void setMathContext(MathContext mathContext);

    MathContext getMathContext();

    BigDecimal calculate(String expression) throws CalculationException;

    StringBuffer getProcessBuffer();

}
