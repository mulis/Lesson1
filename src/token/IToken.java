package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 16.04.12
 * Time: 18:15
 */
public interface IToken {

    enum Type {NUMBER, OPERATOR, PARENTHESIS_LEFT, PARENTHESIS_RIGHT, UNKNOWN}

    Type getType();

    int getStart();

    int getEnd();

    String getExpression();

    String getText();

}
