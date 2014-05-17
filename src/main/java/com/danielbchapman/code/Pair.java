package com.danielbchapman.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<A,B>
{
  A one;
  B two;
  
  public static <X,Y>  Pair<X,Y> create(X x, Y y)
  {
    return new Pair<X, Y>(x, y);
  }
}
