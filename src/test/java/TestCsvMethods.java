import org.junit.Test;

import com.danielbchapman.utility.UtilityDeliminated;


public class TestCsvMethods
{
  @Test
  public void testInversion()
  {

    String[][] nums = new String[4][];
    int index = 1;
    for(int i = 0; i < 4; i++)
    {
      nums[i] = new String[3];
      for(int j = 0; j < 3; j++)
        nums[i][j] = Integer.toString(index++);
    }
    
    System.out.println("INPUT-----------------");
    for(String[] line : nums)
    {
      for(String s : line)
        System.out.print(s + "\t");
      System.out.println();
    }
    
    String[][] invert = UtilityDeliminated.invert(nums);
    
    System.out.println("INVERSION-----------------");
    for(String[] line : invert)
    {
      for(String s : line)
        System.out.print(s + "\t");
      System.out.println();
    }
  }
  
}
