package Main;

import db.CacheDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scene.Menu;
import scene.SceneManager;

public class main extends Application {
    private LocalDate localDate;
    private String date;
    
    private Menu menu;
    private SceneManager sceneManager;
    
    private final double SCENE_WIDTH = 850;
    private final double SCENE_HEIGHT = SCENE_WIDTH / 600 * 450;
    private final String CSS = getClass().getResource("/css/main.css").toExternalForm();
    private final Image ICON = new Image("/img/UI/hololive_icon.png");
    private String salesRecordPath;
   
    private CacheDAO cacheDAO = new CacheDAO();
    
    @Override
    public void start(Stage primaryStage) {          
        localDate = LocalDate.now();
        date = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // reset saleNum when it's another day
        if(!date.equalsIgnoreCase(cacheDAO.getDate())){
            cacheDAO.updateSaleNum(0);
            cacheDAO.updateDate(date);
        }
        
        this.sceneManager = new SceneManager(primaryStage);

        this.sceneManager.switchToMenu();
        
        primaryStage.setTitle("HololiveEN Café POS System");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(ICON);
        primaryStage.show(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}