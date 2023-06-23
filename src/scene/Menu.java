package scene;

import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Menu {
    private final double SCENE_WIDTH = 850;
    private final double SCENE_HEIGHT = SCENE_WIDTH / 600 * 450;
    private final String MAIN_CSS = getClass().getResource("/css/main.css").toExternalForm();
    private final String CSS = getClass().getResource("/css/menu.css").toExternalForm();
    private final String BG_IMG_PATH_0 = "/img/UI/bar_dffaff.png";
    private final String BG_IMG_PATH_1 = "/img/UI/bar_21cbfd.png";
    private final String BG_IMG_PATH_2 = "/img/UI/bar_77e7ef.png";
    
    private SceneManager sceneManager = new SceneManager();
    
    private Random rand = new Random();
    
    private StackPane root = new StackPane();
        private Pane BG_Pane = new Pane();
            // BG_ImageView
        private VBox UI_VBox = new VBox();
            private Label titleLabel = new Label();
            private HBox menuButtonsHBox = new HBox();
                private Button saleSceneButton = new Button();
                private Button managementSceneButton = new Button();
    
    public Menu(){}
                  
    public Scene build(){        
        this.root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
       
        this.setBG_Pane();
        this.setUI_VBox();
        
        this.root.getChildren().add(this.BG_Pane);
        this.root.getChildren().add(this.UI_VBox);
        
        Scene scene = new Scene(this.root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(MAIN_CSS);
        scene.getStylesheets().add(CSS);
        return scene;
    }
    
    private void setBG_Pane(){
        this.BG_Pane.setPrefSize(this.root.getPrefWidth(), this.root.getPrefHeight());
        
        for(int i = 0; i < 20; i++){
            int randInt = rand.nextInt(6);
            String imagePath = "";
            
            if(randInt % 3 == 0){
                imagePath = BG_IMG_PATH_0;
            }else if(randInt % 3 == 1){
                imagePath = BG_IMG_PATH_1;
            }else{
                imagePath = BG_IMG_PATH_2;
            }
            
            if(randInt % 2 == 0){
                this.BG_Pane.getChildren().add(this.setBG_ImageView(new ImageView(new Image(imagePath)), Math.random() * this.BG_Pane.getPrefWidth(), 0));
            }else{
                this.BG_Pane.getChildren().add(this.setBG_ImageView(new ImageView(new Image(imagePath)), 0, Math.random() * this.BG_Pane.getPrefHeight()));
            }
        }
    }
    
    private void setImageViewTranslateTransition(ImageView imageView){
        double translateDuration = 2500;
        TranslateTransition translateTransition = new TranslateTransition();
        double x = this.BG_Pane.getPrefWidth() + imageView.getFitWidth();
        translateTransition.setByX(x);
        translateTransition.setByY(0.74 * x + 3.1);
        translateTransition.setNode(imageView);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition.setDuration(Duration.millis(translateDuration));
        translateTransition.setCycleCount(-1);
        translateTransition.setAutoReverse(false);
        translateTransition.setDelay(Duration.millis(rand.nextInt(3000)));
        translateTransition.play();
        
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setByX(rand.nextInt(9)+1);
        scaleTransition.setNode(imageView);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setDuration(Duration.millis(translateDuration * 1 / 2));
        scaleTransition.setCycleCount(-1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setDelay(translateTransition.getDelay());
        scaleTransition.play();
    }
    
    private ImageView setBG_ImageView(ImageView imageView, double x, double y){
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(40);
        imageView.setFitHeight(imageView.getFitWidth() * 2 / 3);
        imageView.setRotate(39);
        imageView.setLayoutX(x - 1.5 * imageView.getFitWidth());
        imageView.setLayoutY(y - 1.5 * imageView.getFitHeight());
        this.setImageViewTranslateTransition(imageView);
        return imageView;
    }
    
    private void setUI_VBox(){
        this.UI_VBox.setPrefSize(this.root.getPrefWidth(), this.root.getPrefHeight());
        this.UI_VBox.setAlignment(Pos.CENTER);
        
        this.setTitleLabel();
        this.setMenuButtonsHBox();
        
        this.UI_VBox.getChildren().add(this.titleLabel);
        this.UI_VBox.getChildren().add(this.menuButtonsHBox);
    }
    
    private void setTitleLabel(){
        this.titleLabel.setPrefSize(this.root.getPrefWidth(), this.root.getPrefHeight()*1/2);
        this.titleLabel.setText("HololiveEN Café POS System");
        this.titleLabel.setId("titleLabel");
    }
    
    private void setMenuButtonsHBox(){
        this.menuButtonsHBox.setPrefSize(this.root.getPrefWidth(), this.root.getPrefHeight()*1/2);
        this.menuButtonsHBox.setId("menuButtonsHBox");
        
        this.saleSceneButton.setText("銷售端");
        this.saleSceneButton.setOnAction((ActionEvent event) -> {
//            this.primaryStage.setScene(saleScene.build());
            sceneManager.switchToSaleScene();
        });
        
        this.managementSceneButton.setText("管理端");
        this.managementSceneButton.setOnAction((ActionEvent event) -> {
//            this.primaryStage.setScene(managementScene.build());
            this.sceneManager.switchToManagementScene();
        });
        
        this.menuButtonsHBox.getChildren().add(this.saleSceneButton);
        this.menuButtonsHBox.getChildren().add(this.managementSceneButton);
    }
}
