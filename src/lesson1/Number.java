package lesson1;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:12
 */
class Number extends Token {

    Number(String expression, int start, int end) {
        super(Token.TYPE_NUMBER, expression, start, end);
    }

}
