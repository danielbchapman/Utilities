package com.danielbchapman.code.jdk;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;


/**
 * @see <a href="http://stackoverflow.com/questions/4463440/compile-java-source-code-from-a-string">
 * http://stackoverflow.com/questions/4463440/compile-java-source-code-from-a-string
 * </a>
 * 
 * <p>Borrowed from the above URL.</p>
 */
public class JavaSourceString extends SimpleJavaFileObject
{
  final String code;

  public JavaSourceString(String name, String code)
  {
    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
    this.code = code;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors)
  {
    return code;
  }
}