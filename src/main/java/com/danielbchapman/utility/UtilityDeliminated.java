package com.danielbchapman.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class UtilityDeliminated
{
  public static String[][] readFile(InputStream in, char separator)
  {
    CSVReader reader = new CSVReader(new InputStreamReader(in), separator);
    
    List<String[]> all = null;
    try
    {
      all = reader.readAll();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    finally 
    {
      try
      {
        reader.close();
      }
      catch (IOException e)
      {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
    
    String[][] results = new String[all.size()][];
    
    for(int i = 0; i < results.length; i++)
      results[i] = all.get(i);
    
    return results;
      
  }
  
  /**
   * This method inverts the rows and columns so that a 
   * tabular format now would have headers as the first column.
   * 
   * This assumes a "square" input but will resize the min/max to generate a square
   * using nulls.
   * 
   * @param input
   * @return A String[][] of inverted data  
   * 
   */
  //FIXME Java Doc Needed
  public static String[][] invert(String[][] input)
  {
    int rowMax = input.length;
    int colMax = -1;
    
    for(String[] array : input)
    {
      if(array.length > colMax)
        colMax = array.length;
    }
    
    String[][] inversion = new String[colMax][];
    for(int i = 0; i < inversion.length; i++)
      inversion[i] = new String[rowMax];
    
    int i = 0;
    int j = 0;
    for(i = 0; i < input.length; i++)
      for(j = 0; j < input[i].length; j++)
      {
        String x = input[i][j];
        inversion[j][i] = x;        
      }

            
    
    return inversion;
  }
}
