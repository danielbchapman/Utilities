package com.danielbchapman.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<A,B>
{
  public A one;
  public B two;
  
  /**
   * Creates a pair of objects strongly typed based on the
   * parameters.
   * @param x item one
   * @param y item two
   * @return <Return Description>  
   * 
   */
  public static <X, Y> Pair<X, Y> create(X x, Y y)
  {
    return new Pair<X, Y>(x, y);
  }
}
