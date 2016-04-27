package com.danielbchapman.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import com.danielbchapman.text.Text;

@Deprecated
public class UtilityText
{
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
	if(Text.isEmpty(text))
		return "";
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
