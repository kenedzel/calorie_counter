package util;

import java.text.DecimalFormat;

/**
 * Created by kenneth on 6/1/2016.
 */
public class Util
{
    public static String formatNumber(int value)
    {
        DecimalFormat format = new DecimalFormat("#,###,###");

        String formatted = format.format(value);

        return formatted;
    }
}
