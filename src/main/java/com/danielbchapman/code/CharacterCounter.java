package com.danielbchapman.code;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * A simple application that counts characters in the text area for submission
 * to dated online forms that have character limits.
 * 
 * (I'm looking at you Oracle!)
 */
@SuppressWarnings("restriction")
public class CharacterCounter extends Application
{
	
  public static void main(String ... args)
  {
    launch();
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
    VBox root = new VBox();
    
    final Label charCount = new Label("Chracter count is unknown");

    final TextArea pre = new TextArea();
    pre.setWrapText(true);
    pre.setMaxWidth(500);
    pre.setPromptText("Enter text to count here");
    pre.textProperty().addListener((_val, _old, _new) ->
    {
    	String inbound = _new;
    	inbound = inbound.replaceAll("\\r\\n","\n");//Windows
    	inbound = inbound.replaceAll("\\r", "\n");//Word
    	pre.setText(inbound);
    	String text = pre.getText();
    	charCount.setText((String.format("Character count is %d", text.length())));
    	System.out.println("------------------------PROCESSED");
    	System.out.println(pre.getText());
    });
        
    root.getChildren().addAll(
        charCount,
        pre
      );
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setTitle("Character Counter");
  }
}
