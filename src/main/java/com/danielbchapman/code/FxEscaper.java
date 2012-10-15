package com.danielbchapman.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * A simple escaper that escapes quotes for converting text to JavaScript...
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Sep 27, 2012
 * @version 2 Development
 * @link http://www.lightassistant.com
 ***************************************************************************
 */

public class FxEscaper extends Application
{
  
  public static void main(String ... args)
  {
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception
  {
    final BorderPane root = new BorderPane();
    final TextArea in = new TextArea();
    final TextArea out = new TextArea();
    final Button button = new Button("Process");
    
    button.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(process(in.getText()));
      }});
    VBox box = new VBox();
    
    box.getChildren().addAll(in, button, out);
    
    root.setCenter(box);
    stage.setScene(new Scene(root));
    stage.show();
  }
  
  private static String process(String input)
  {
    String line = null;
    BufferedReader reader = new BufferedReader(new StringReader(input));
    StringBuilder build = new StringBuilder();
    try
    {
      while((line = reader.readLine()) != null)
      {
        build.append("\"");
        build.append(line.replaceAll("\\\"", "\\\\\""));
        build.append("\"");
        build.append("\r\n");//windows
      }
    }
    catch (IOException e)/* Should not happen */
    {
      throw new RuntimeException(e.getMessage(), e);
    }
    
    return build.toString();
  }
}
