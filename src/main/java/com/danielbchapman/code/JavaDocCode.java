package com.danielbchapman.code;
import java.io.BufferedReader;
import java.io.StringReader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class JavaDocCode extends Application
{

  public static void main(String ... args)
  {
    launch();
  }

  @Override
  public void start(Stage arg0) throws Exception
  {
    final TextArea in = new TextArea();
    in.setPromptText("<Paste code to prefix for JavaDoc>");
    
    final TextArea out = new TextArea();
    
    final Button button = new Button("Process");
    button.setText("Process");
    
    button.onActionProperty().set(new EventHandler<ActionEvent>(){

      @Override
      public void handle(ActionEvent arg0)
      {
        out.setText(process(in.getText()));
      }
      
    });
    VBox box = new VBox();
    box.getChildren().addAll(in, button, out);
    box.setAlignment(Pos.TOP_LEFT);
    box.setStyle("-fx-padding: 10;");

    Scene scene = new Scene(box);
    arg0.setScene(scene);
    arg0.setTitle("Java Doc Processor");
    arg0.show();
  }
  
  private String process(String text)
  {
    try
    {
      StringBuilder build = new StringBuilder();
      BufferedReader reader = new BufferedReader(new StringReader(text));
      String line = null;
      build.append("\t* <code>\n");
      build.append("\t* <pre>\n");
      while((line = reader.readLine()) != null)
      {
        line = line.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;").replaceAll("\\@", "{literal @}");
        build.append("\t* ");
        build.append(line);
        build.append("\n");
      }
      build.append("\t* </pre>\n");
      build.append("\t* </code>\n");
      return build.toString();  
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read text: " + e.getMessage(), e);
    }
  }
}
