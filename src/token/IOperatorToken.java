package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 12:01
 */
public interface IOperatorToken extends IToken {

    char PLUS = '+';
    char MINUS = '-';
    boolean LEFT_TO_RIGHT = true;
    boolean RIGHT_TO_LEFT = false;

    int getPrecedence();

    boolean getAssociation();

    int getArgumentCount();

    INumberToken operate(INumberToken[] operands);

}
