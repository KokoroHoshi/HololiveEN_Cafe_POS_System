package scene;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene menu;
    private static Scene saleScene;
    private static Scene managementScene;
    
    public SceneManager(){}
    
    public SceneManager(Stage primaryStage){
        this.primaryStage = primaryStage;
        
        Menu menu = new Menu();
        this.menu = menu.build();
        
        SaleScene saleScene = new SaleScene();
        this.saleScene = saleScene.build();
        
        ManagementScene managementScene = new ManagementScene();
        this.managementScene = managementScene.build();
    }
    
    public void switchToMenu(){
        this.primaryStage.setScene(this.menu);
    }
    
    public void switchToSaleScene(){
        this.primaryStage.setScene(this.saleScene);
    }
    
    public void switchToManagementScene(){
        this.primaryStage.setScene(this.managementScene);
    }
}
