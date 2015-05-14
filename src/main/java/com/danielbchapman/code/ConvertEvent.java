package com.danielbchapman.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
@SuppressWarnings("restriction")
public class ConvertEvent extends Application
{
  public static String text;
  
  public static void main(String ... args) throws IOException
  {
    BufferedReader reader = new BufferedReader(new FileReader("data/event.tsv"));
    StringBuilder builder = new StringBuilder(1024*100);
    String line = null;
    
    while((line = reader.readLine()) != null)
      builder.append(convert(line.split("\t")));
    
    reader.close();
    text = builder.toString();
    launch();
  }
  
  public static String convert(String[] input)
  {
    StringBuilder builder = new StringBuilder(1024);
    
    builder.append("INSERT INTO event (id,cast_event,crew_event,description,time_end,publicevent,time_start,day_id) VALUES (");
    for(int i = 0; i < input.length; i++)
    {
      if(i == 1 || i == 2 || i == 5)
        builder.append(castBool(input[i]));
      else
        builder.append(escape(input[i], i));
      
      if(i + 1 < input.length)
        builder.append(",");
    }
      
    builder.append(");\n");
    return builder.toString();
  }
  
  public static String escape(String input, int i)
  {
    if(3 == i || 4 == i || 6 == i)
      return "'" + input + "'";
    
    return input;
  }
  public static String castBool(String input)
  {
    if("true".equalsIgnoreCase(input) || "1".equals(input))
      return "true";
    
    return "false";
  }

  public static String getText()
  {
    return text;
  }
  
  @Override
  public void start(Stage arg0) throws Exception
  {
    HBox root = new HBox();
    root.setPrefSize(800, 600);
    root.getChildren().add(new TextArea(text));
    arg0.setScene(new Scene(root));
    arg0.show();
  }
}
