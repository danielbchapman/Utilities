package com.danielbchapman.code;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.danielbchapman.utility.UtilityText;


public class TSVDataReader extends Application
{

  public final static String LOWER = "#lower";
  public final static String STATIC = "#static";
  public final static String VALUE = "#value";
  public final static String RAW = "#raw";
  public static void main(String ... args)
  {
    launch();
  }
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox root = new VBox();
    
    final Label Instructions = new Label("Input a TSV/Newline separates set of data to be converted into a decimal[][]");
    final Label lower = new Label("Use " + LOWER + " for lowerCase (method)");
    final Label staticc = new Label("Use " + STATIC + " for STATIC_CASE");
    final Label value = new Label("Use " + VALUE + " for ObjectCase");
    final Label raw = new Label("Use " + RAW + " for 'Raw Value'");

    final TextArea pre = new TextArea();
    final TextArea data = new TextArea();
    final TextArea out = new TextArea();
    
    pre.setText("public void " + LOWER + "(String "+ LOWER+ ")\n{\n\tthis." + LOWER +" = " + LOWER + ";\n}\\\\" + STATIC +" " + RAW + ";");
    pre.setPromptText("Code before element");
    data.setPromptText("TSV elements");
    

    Button process =new Button("Process");
    process.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(process(pre.getText(), data.getText()));
      }});
        
    root.getChildren().addAll(
        //lower,
        //staticc,
        //value,
        //raw,
        //pre,
        data, 
        process,
        out);
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("TSV Code Builder");
  }

  public static String process(String template, String data)
  {
    StringBuilder builder = new StringBuilder();
    String[] raw = data.split("\\n");
    
    boolean firstRow = true;
    for(String rowData : raw)
    { 
      if(firstRow)
      {
        builder.append("decimal[][] ARRAY_NAME = new decimal[][]\n{\n");
        firstRow = false;
        builder.append("\tnew decimal[]{ ");
      }
      else
      {
        builder.append(",\n\tnew decimal[]{ ");  
      }
      
      String[] row = rowData.split("\\t");
      boolean start = true;
      for(String s : row)
      {
        if(!start)
        {
          builder.append(", ");
        }
        else
        {
          start = false;
        }
        Object parsed = parse(s);
        if(parsed == null)
          builder.append("null");
        else if(parsed instanceof String)
          builder.append(parsed);
        else
        {
          BigDecimal big = (BigDecimal) parsed;
          DecimalFormat format = new DecimalFormat("#0.00####");
          builder.append(format.format(big));
          builder.append("M");
        }
      }
      builder.append("}");
    }
    builder.append("\n};");
    
    return builder.toString();
  }
  
  public static Object parse(String input)
  {
    String[] illegal 
      = new String[]
      {
        "%",
        "¥",
        "\\$",
        ","
      };
    
    String raw = input;
    if(input == null)
      return null;
    
    //PERCENT
    boolean percent = false;
    if(input.contains("%"))
    {
      percent = true;
    }
    
    for(String strip : illegal)
      input = input.replaceAll(strip, "");
    
    try
    {
      BigDecimal value = new BigDecimal(input);
      if(value != null && percent)
        value = value.divide(new BigDecimal("100"));
    
      return value;
    }
    catch(Exception e)
    {
      System.out.println(String.format("Could not convert {0} to a big decimal", input));
    }
    
    return input; //Rwo
  }
}
