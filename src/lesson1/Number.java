package lesson1;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.03.12
 * Time: 1:12
 */
class Number extends Token {

    String value;

    Number(String expression, int start, int end, String value) {
        super(Token.TYPE_NUMBER, expression, start, end);
        this.value = value;
    }

}
