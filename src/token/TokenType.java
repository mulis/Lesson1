package token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 02.09.12
 * Time: 3:27
 */
public enum TokenType {

    SIGNED_NUMBER("\\G\\([\\+\\-]\\d*\\.?\\d+\\)"),
    NUMBER("\\G\\d*\\.?\\d+"),
    OPERATOR("\\G[\\+\\-\\*\\/\\^]"),
    PARENTHESIS_LEFT("\\G\\("),
    PARENTHESIS_RIGHT("\\G\\)"),
    FUNCTION_ARGUMENT_SEPARATOR("\\G,"),
    FUNCTION("\\Gsqr|min|max|sum"),
    UNKNOWN("\\G\\.+");

    public final Matcher matcher;

    TokenType(String pattern) {
        matcher = Pattern.compile(pattern).matcher("");
    }

}
