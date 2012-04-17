package token;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Mulishov Serhij
 * Date: 17.04.12
 * Time: 11:57
 */
public interface INumberToken extends IToken {
    BigDecimal getValue();
}
