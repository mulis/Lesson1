package tests;

import org.junit.Assert;
import org.junit.Test;

import calculator.*;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: rees
 * Date: 12.08.12
 * Time: 15:15
 */
public class CalculatorTest {

    Calculator calculator = new Calculator();

    @Test
    public void testSign() {
        testCalculation(" +2 ", new BigDecimal(2));
        testCalculation(" -2 ", new BigDecimal(-2));
        testCalculation(" +2 - 2 ", new BigDecimal(0));
        testCalculation(" -2 + 2 ", new BigDecimal(0));
    }

    @Test
    public void testAddition() {
        testCalculation(" 2 + 2 ", new BigDecimal(4));
    }

    @Test
    public void testSubtraction() {
        testCalculation(" 2 - 1 ", new BigDecimal(1));
        testCalculation(" 1 - 1 ", new BigDecimal(0));
        testCalculation(" 1 - 2 ", new BigDecimal(-1));
    }

    @Test
    public void testMultiply() {
        testCalculation(" 2 * 2 ", new BigDecimal(4));
    }

    @Test
    public void testDivide() {
        testCalculation(" 2 / 2 ", new BigDecimal(1));
    }

    @Test
    public void testPower() {
        testCalculation(" 2 ^ 2 ", new BigDecimal(4));
    }

    @Test
    public void testRealNumber() {
        testCalculation(" 2.2 + 0.8 ", new BigDecimal(3.000));
    }

    @Test
    public void testParenthesis() {
        testCalculation(" 2 - ( 0 - 2 ) ", new BigDecimal(4));
        testCalculation(" 2 - ( ( 1 - 1 ) - 2 ) ", new BigDecimal(4));
    }

    @Test
    public void testExceptions() {
        testCalculation(" 2 + a ", UnknownTokenException.class);
        testCalculation(" 2 2 ", AbsentOperatorException.class);
        testCalculation(" 2 + ", AbsentOperandException.class);
        testCalculation(" ( 2 + 2 ) ) ", ParenthesesNotMatchException.class);
        testCalculation("1(-2)", AbsentOperatorException.class);
        testCalculation(" 2*2 ", UnknownTokenException.class);
        testCalculation(" 2/2 ", UnknownTokenException.class);
        testCalculation(" 2^2 ", UnknownTokenException.class);
    }

    public void testCalculation(String expression, BigDecimal expressionResult) {
        testCalculation(expression, expressionResult, null);
    }

    public void testCalculation(String expression, Class exceptionClass) {
        testCalculation(expression, null, exceptionClass);
    }

    public void testCalculation(String expression, BigDecimal resultExpected, Class exceptionClassExpected) {
        BigDecimal resultReceived = null;
        Exception exceptionReceived = null;
        try {
            resultReceived = calculator.calculate(expression);
            String message = String.format("\n\tExpression: %s \n\tResult expected: %s \n\tResult received: %s \n\tException expected: %s", expression, resultExpected, resultReceived, exceptionClassExpected);
            Assert.assertTrue(message, resultReceived.compareTo(resultExpected) == 0);
        } catch (Exception exception) {
            exceptionReceived = exception;
            String message = String.format("\n\tExpression: %s \n\tResult expected: %s \n\tResult received: %s \n\tException expected: %s \n\tException received: %s \n%s\n%s\n", expression, resultExpected, resultReceived, exceptionClassExpected, exceptionReceived.getClass(), exceptionReceived, calculator.getProcessBuffer().toString());
            Assert.assertTrue(message, exceptionReceived.getClass().equals(exceptionClassExpected));
        }
    }

}
