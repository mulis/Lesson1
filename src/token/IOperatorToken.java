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
    int LEFT_TO_RIGHT = 1;
    int RIGHT_TO_LEFT = -1;

    int getPrecedence();

    int getAssociation();

    int getArgumentCount();

    INumberToken operate(INumberToken[] operands);

}
