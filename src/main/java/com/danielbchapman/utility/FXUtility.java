package com.danielbchapman.utility;

import java.util.Random;

public class FXUtility
{
  /**
   * @return a new double from a unique random object.
   */
  public double random()
  {
    return new Random().nextDouble();
  }
}
