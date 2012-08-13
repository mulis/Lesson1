package token;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 16.04.12
 * Time: 18:15
 */
public interface IToken {

    enum Type {SIGNED_NUMBER, NUMBER, OPERATOR, PARENTHESIS_LEFT, PARENTHESIS_RIGHT, UNKNOWN}

    public Type getType();

    public int getStart();

    public int getEnd();

    public String getExpression();

    public String getText();

}
