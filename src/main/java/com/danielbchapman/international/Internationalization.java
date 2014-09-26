package com.danielbchapman.international;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A simple wrapper for the master properties file. In the event of an error this will show simple *string
 * 
 *************************************************************************** @author Daniel B. Chapman <br />
 *         <i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Jul 2, 2010 2010
 * @version 2 Development
 * @link http://www.lightassistant.com
 */
public class Internationalization
{
  public static boolean CURRENCY_BEFORE = true;
  public static String CURRENCY_SYMBOL = "$";
  public static String DATE_FORMAT = "MM/dd/yyyy";
  public static String LONG_DATE_FORMAT = "MMMMM dd yyyy";
  public static String TIME_FORMAT = "hh:mm a";

  /**
   * @param decimal the amount to calculate
   * @return an internationalized value of this currency   
   * 
   */
  public static String formatCurrency(BigDecimal decimal)
  {
    NumberFormat format = new DecimalFormat("$#,###.00");

    if (decimal == null)
      return format.format(0.00);

    return format.format(decimal);
  }
}
