package models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NumberPad extends Stage{
    private double SCENE_WIDTH = 300;
    private double SCENE_HEIGHT = 400;
    
    VBox root = new VBox();
    GridPane keypadGridPane = new GridPane();
    
    private void setKeypadGridPane(Stage thisStage){
        keypadGridPane.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
        keypadGridPane.setAlignment(Pos.BOTTOM_LEFT);
        
        double keyButtonWidth = keypadGridPane.getPrefWidth() * 1 / 4;
//        double keyButtonHeight = keypadGridPane.getPrefHeight() * 1 / 3;
        
        int j = 0; 
        for(int i = 0; i < 10; i++){
            Button key = new Button();
            key.setPrefSize(keyButtonWidth, keyButtonWidth);
            key.setText(String.valueOf(i+1));
            
            keypadGridPane.add(key, i%3, j);
            
            if(i%3 == 2){
                j++;
            }
        }
        
        Button cancelButton = new Button();
        cancelButton.setPrefSize(keyButtonWidth, keyButtonWidth);
        cancelButton.setText("Cancel");
        cancelButton.setOnAction(new EventHandler <ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                thisStage.hide();
            }
        });
        Button clearButton = new Button();
        clearButton.setPrefSize(keyButtonWidth, keyButtonWidth);
        clearButton.setText("C");
        Button numberZeroKeyButton = new Button();
        numberZeroKeyButton.setPrefSize(keyButtonWidth, keyButtonWidth);
        numberZeroKeyButton.setText("0");
        Button backspaceButton = new Button();
        backspaceButton.setPrefSize(keyButtonWidth, keyButtonWidth);
        backspaceButton.setText("←");
        Button okButton = new Button();
        okButton.setPrefSize(keyButtonWidth, keyButtonWidth);
        okButton.setText("OK");
        okButton.setOnAction(new EventHandler <ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                thisStage.hide();
            }
        });
        
        keypadGridPane.add(cancelButton, 3, j-1);
        keypadGridPane.add(clearButton, 0, j);
        keypadGridPane.add(numberZeroKeyButton, 1, j);
        keypadGridPane.add(backspaceButton, 2, j);
        keypadGridPane.add(okButton, 3, j);
        
        root.getChildren().add(keypadGridPane);
    }
    
    public NumberPad(){
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);

        setKeypadGridPane(this);
        
        this.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        this.initStyle(StageStyle.UTILITY);
        this.setTitle("改變訂單數量");
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public void setVisible(Boolean visible){
        if(visible){
            this.show();
        }else{
            this.hide();
        }
    }
}
