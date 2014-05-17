package com.danielbchapman.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;

public class UtilityText
{
  public final static char[] NUMBERS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-' };
  public final static char[] DIMENSIONS_SE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '\'', '"', '-' };
  // public final static char[] DIMENSIONS_METRIC = new
  // char[]{'0','1','2','3','4','5','6','7','8','9','.','c','m','d','l'};//INCOMPLETE
  public final static char[] WHITESPACE = new char[] { ' ', '\n', '\r', '\t' };

  public static String stripWhitespace(String value)
  {
    return stripIllegalChars(value, WHITESPACE);
  }

  public static String stripIllegalChars(String value, char[] legal)
  {
    char[] newString = new char[value.length()];
    int index = 0;
    for (char c : value.toCharArray())
      if (validateCharacter(c, legal))
        newString[index++] = c;

    char[] ret = new char[index];

    for (int i = 0; i < index; i++)
      ret[i] = newString[i];

    return new String(ret);
  }

  public static boolean validateCharacter(char c, char[] values)
  {
    for (int i = 0; i < values.length; i++)
      if (values[i] == c)
        return true;

    return false;
  }

  public static BigDecimal processForBigDecimal(String value)
  {
    value = UtilityText.stripIllegalChars(value, UtilityText.NUMBERS);
    try
    {
      return new BigDecimal(value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return new BigDecimal(0.00);
    }
  }

  public static BigDecimal processForInches(String value) throws IllegalArgumentException
  {
    BigDecimal feet = null;
    BigDecimal inches = null;

    value = stripIllegalChars(value, DIMENSIONS_SE);

    int feetLocal = value.indexOf("'");
    // Feet
    if (feetLocal != -1)
    {
      String feetS = value.substring(0, feetLocal);
      feet = processForBigDecimal(feetS);
      value = value.substring(feetLocal, value.length());
    }
    // Inches
    inches = processForBigDecimal(value);

    if (feet != null)
      return feet.multiply(new BigDecimal(12)).add(inches);
    else
      return inches;
  }
  
  public static boolean isEmpty(String string)
  {
    if(string == null)
      return true;
    if(string.trim().length() == 0)
      return true;
    return false;
  }
  
  public static void main(String[] args)
  {
    String test = "15.5' 3\"";
    System.out.println(test + " value in inches is: " + UtilityText.processForInches(test));
    test = "q15' 7\"";
    System.out.println(test + " value in inches is: " + UtilityText.processForInches(test));
    test = "abcsd";
    System.out.println(test + " value in inches is: " + UtilityText.processForInches(test));
    test = "17\"";
    System.out.println(test + " value in inches is: " + UtilityText.processForInches(test));
  }
  
  
  public static class Code
  {
    public static String staticCase(String text)
    {
      char[] chars = text.toCharArray();
      char[] buffer = new char[chars.length * 2]; 
      
      int add = 0;
      for(int i = 0; i < chars.length; i++)
      {
        if(Character.isUpperCase(chars[i]))
        {
          buffer[add + i] = '_';
          add++;
          buffer[add + i] = Character.toUpperCase(chars[i]);
        }
        else
          buffer[add + i] = Character.toUpperCase(chars[i]);
      }
      
      return new String(Arrays.copyOf(buffer, chars.length + add));
    }    
    
    public static String clean(String string)
    {
      return string.replaceAll("\\-", "").replaceAll("\\s+", "");
    }
  }
  
  public static String capitalizeFirst(String text)
  {
    char[] ret = text.toCharArray();
    ret[0] = Character.toUpperCase(ret[0]);
    return new String(ret);
  }
  
  public static String lowerCaseFirst(String text)
  {
    char[] ret = text.toCharArray();
    ret[0] = Character.toLowerCase(ret[0]);
    return new String(ret);
  }
  
  /**
   * <JavaDoc>
   * @param file
   * @param data
   * @throws IOException <Return Description>  
   * 
   */
  //FIXME Java Doc Needed
  public static void writeFile(File file, String data) throws IOException
  {
    try(BufferedWriter out = new BufferedWriter(new FileWriter(file))){
      out.write(data);  
    }
  }
  
  public static String format(String input, Object ... args)
  {
    return MessageFormat.format(input, args);
  }
}
