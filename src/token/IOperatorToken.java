package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 12:01
 */
public interface IOperatorToken extends IToken {

    int getPrecedence();

    int getAssociation();

    int getArgumentCount();

    INumberToken operate(INumberToken[] operands);

}
