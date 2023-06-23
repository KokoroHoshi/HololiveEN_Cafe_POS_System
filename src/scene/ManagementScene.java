package scene;

import db.CacheDAO;
import db.OrderDAO;
import db.ProductDAO;
import db.StaffDAO;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.TreeMap;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Product;
import models.Staff;

public class ManagementScene {
    private final double SCENE_WIDTH = 850;
    private final double SCENE_HEIGHT = SCENE_WIDTH / 600 * 450;
    private final String MAIN_CSS = getClass().getResource("/css/main.css").toExternalForm();
    private final String CSS = getClass().getResource("/css/managementScene.css").toExternalForm();
    private final String QUANTITY_BAR_IMG_PATH_0 = "/img/UI/bar_21cbfd.png";
    private final String QUANTITY_BAR_IMG_PATH_1 = "/img/UI/bar_77e7ef.png";
    
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private CacheDAO cacheDAO = new CacheDAO();
    private StaffDAO staffDAO = new StaffDAO();
    
    private SceneManager sceneManager = new SceneManager();
    
    private TreeMap<String, Staff> staffDict = null;
    
    private String curStaticsFunctionsOption = "";
    private String curStatisticsDate = "";
    private String curProductCategoryInStatisticsItems = "";
    private String curOrderByMode = "";
    private String curStatisticsFromDate = "";
    private String curStatisticsToDate = "";
    private String curStatisticsMonth = "";
    
    private HBox root = new HBox();
        private VBox functionButtonsVBox = new VBox();
            private Button statisticsFunctionButton = new Button();
            private Button staffFunctionButton = new Button();
            private Button backToMenuFunctionButton = new Button();
        // statisticsScene
        private VBox statisticsSceneVBox = new VBox();
            private HBox statisticsSettingHBox = new HBox();
                private ChoiceBox<String> statisticsFunctionsChoiceBox = new ChoiceBox<String>();
                    private String[] statisticsfunctionsOptions = {"日銷售數量", "週銷售數量", "月銷售數量"};
                private DatePicker dailyDatePicker = new DatePicker(LocalDate.now());
                private DatePicker weeklyDatePicker = new DatePicker(LocalDate.now());
                private DatePicker monthlyDatePicker = new DatePicker(LocalDate.now());
                private ChoiceBox<String> statisticsCategoryChoiceBox = new ChoiceBox<String>();
                    private String[] statisticsCategoryOptions = {"飲料", "食物", "周邊"};
                private ChoiceBox<String> statisticsOrderByChoiceBox = new ChoiceBox<String>();
                    private String[] statisticsOrderByOptions = {"預設", "降序", "升序"};
            private ScrollPane statisticsItemsScrollPane = new ScrollPane();
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // imgView
                        // itemSaleQuantityLabel
                private VBox drinkItemsVBox = new VBox();
                private VBox foodItemsVBox = new VBox();
                private VBox merchItemsVBox = new VBox();
                private VBox[] statisticsItemsVBoxs = {drinkItemsVBox, foodItemsVBox, merchItemsVBox};         
        // staffScene
        private VBox staffSceneVBox = new VBox();
            private ScrollPane staffScrollPane = new ScrollPane();
                private FlowPane staffFlowPane = new FlowPane();
    
    public ManagementScene(){}
    
    public Scene build(){
        this.root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        
        this.setFunctionButtonsVBox();
        this.setStatisticsSceneVBox();
        this.setStaffSceneVBox();
        
        this.root.getChildren().add(this.functionButtonsVBox);
        this.root.getChildren().add(this.statisticsSceneVBox);
        
        Scene scene = new Scene(this.root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(MAIN_CSS);
        scene.getStylesheets().add(CSS);
        return scene;
    }
    
    // function ↓
    private void refreshStatisticsItemsVBoxOrderByDefault(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String fromDate, String toDate){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByWeeklyDate(this.curProductCategoryInStatisticsItems, fromDate, toDate, this.curOrderByMode);

        int quantityBarImgIndex = 0;

        for(String productId : productDict.keySet()){
            Product product = productDict.get(productId);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        if(productQuantityDict.containsKey(productId)){
                            itemSaleQantity = productQuantityDict.get(productId);
                        }
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
    }
    
    private void refreshStatisticsItemsVBoxOrderByDefault(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String date){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByDate(this.curProductCategoryInStatisticsItems, date, this.curOrderByMode);

        int quantityBarImgIndex = 0;

        for(String productId : productDict.keySet()){
            Product product = productDict.get(productId);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        if(productQuantityDict.containsKey(productId)){
                            itemSaleQantity = productQuantityDict.get(productId);
                        }
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
    }
    
    private void refreshStatisticsItemsVBoxOrderByASC(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String fromDate, String toDate){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByWeeklyDate(this.curProductCategoryInStatisticsItems, fromDate, toDate, this.curOrderByMode);
        LinkedHashMap<Product, Boolean> tempProductDict = new LinkedHashMap<Product, Boolean>();
        for(Product product : productDict.values()){
            tempProductDict.put(product, Boolean.FALSE);
        }

        int quantityBarImgIndex = 0;

        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            tempProductDict.replace(product, Boolean.TRUE);
        }
        for(Product product : productDict.values()){
            if(Objects.equals(tempProductDict.get(product), Boolean.FALSE)){
                HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        quantityBarImgView.setFitWidth(5);

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
                quantityBarImgIndex++;
            }
        }
        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = productQuantityDict.get(productId);
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
    }
    
    private void refreshStatisticsItemsVBoxOrderByASC(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String date){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByDate(this.curProductCategoryInStatisticsItems, date, this.curOrderByMode);
        LinkedHashMap<Product, Boolean> tempProductDict = new LinkedHashMap<Product, Boolean>();
        for(Product product : productDict.values()){
            tempProductDict.put(product, Boolean.FALSE);
        }

        int quantityBarImgIndex = 0;

        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            tempProductDict.replace(product, Boolean.TRUE);
        }
        for(Product product : productDict.values()){
            if(Objects.equals(tempProductDict.get(product), Boolean.FALSE)){
                HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        quantityBarImgView.setFitWidth(5);

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
                quantityBarImgIndex++;
            }
        }
        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = productQuantityDict.get(productId);
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
    }
    
    private void refreshStatisticsItemsVBoxOrderByDESC(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String fromDate, String toDate){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByWeeklyDate(this.curProductCategoryInStatisticsItems, fromDate, toDate, this.curOrderByMode);
        LinkedHashMap<Product, Boolean> tempProductDict = new LinkedHashMap<Product, Boolean>();
        for(Product product : productDict.values()){
            tempProductDict.put(product, Boolean.FALSE);
        }

        int quantityBarImgIndex = 0;

        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            tempProductDict.replace(product, Boolean.TRUE);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = productQuantityDict.get(productId);
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
        for(Product product : productDict.values()){
            if(Objects.equals(tempProductDict.get(product), Boolean.FALSE)){
                HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        quantityBarImgView.setFitWidth(5);

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
                quantityBarImgIndex++;
            }
        }
    }
    
    private void refreshStatisticsItemsVBoxOrderByDESC(VBox statisticsItemsVBox, TreeMap<String, Product> productDict, String date){
        LinkedHashMap<String, Integer> productQuantityDict = orderDAO.getProductQuantityByDate(this.curProductCategoryInStatisticsItems, date, this.curOrderByMode);
        LinkedHashMap<Product, Boolean> tempProductDict = new LinkedHashMap<Product, Boolean>();
        for(Product product : productDict.values()){
            tempProductDict.put(product, Boolean.FALSE);
        }

        int quantityBarImgIndex = 0;

        for(String productId : productQuantityDict.keySet()){
            Product product = productDict.get(productId);
            tempProductDict.replace(product, Boolean.TRUE);
            try{
                // statisticsItemsVBox
                    // statisticsItemHBox
                        // productImgView
                        // itemSaleQuantityLabel
                        // quantityBarImgView
                    HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = productQuantityDict.get(productId);
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        double widthOfQuantityBarImgView = 0;
                        if(this.curStaticsFunctionsOption.equalsIgnoreCase("月銷售數量")){
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 5;
                        }else{
                            widthOfQuantityBarImgView = 5 + itemSaleQantity * 15;
                        }
                        if(widthOfQuantityBarImgView > 500){
                            quantityBarImgView.setFitWidth(500);
                        }else{
                            quantityBarImgView.setFitWidth(widthOfQuantityBarImgView);
                        }

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
            } catch(Exception e){
                System.out.println(e);
                Label itemNameLabel = new Label();
                itemNameLabel.setPrefHeight(120);
                itemNameLabel.setText(product.getName());
                statisticsItemsVBox.getChildren().add(itemNameLabel);
            }
            quantityBarImgIndex++;
        }
        for(Product product : productDict.values()){
            if(Objects.equals(tempProductDict.get(product), Boolean.FALSE)){
                HBox statisticsItemHBox = new HBox();
                    statisticsItemHBox.setId("statisticsItemHBox");
                        String productImgPath = "/img/" + this.curProductCategoryInStatisticsItems + "/" + product.getImagePath();
                        Image productImg = new Image(productImgPath);
                        ImageView productImgView = new ImageView(productImg);
                        productImgView.setFitWidth(120);
                        productImgView.setPreserveRatio(true);

                        Label itemSaleQuantityLabel = new Label();
                        itemSaleQuantityLabel.setPrefWidth(50);
                        itemSaleQuantityLabel.setId("itemSaleQuantityLabel");
                        int itemSaleQantity = 0;
                        String itemSaleQantityText = String.valueOf(itemSaleQantity);
                        itemSaleQuantityLabel.setText(itemSaleQantityText);

                        Image quantityBarImg = null;
                        if(quantityBarImgIndex % 2 == 0){
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_0);
                        }else{
                            quantityBarImg = new Image(this.QUANTITY_BAR_IMG_PATH_1);
                        }
                        // width of quantityBarImgView: 5 -- 500
                        ImageView quantityBarImgView = new ImageView(quantityBarImg);
                        quantityBarImgView.setPreserveRatio(false);
                        quantityBarImgView.setFitHeight(50);
                        quantityBarImgView.setFitWidth(5);

                    statisticsItemHBox.getChildren().add(productImgView);
                    statisticsItemHBox.getChildren().add(itemSaleQuantityLabel);
                    statisticsItemHBox.getChildren().add(quantityBarImgView);
                statisticsItemsVBox.getChildren().add(statisticsItemHBox);
                quantityBarImgIndex++;
            }
        }
    }
    
    private void refreshStatisticsItemsVBoxs(String fromDate, String toDate){
        if(this.curProductCategoryInStatisticsItems.isEmpty() || this.curOrderByMode.isEmpty()){
            return;
        }

        TreeMap<String, Product> productDict = null;
        VBox statisticsItemsVBox = new VBox();
        switch(this.curProductCategoryInStatisticsItems){
            case "Drink":
                productDict = productDAO.getProductDictByCategory("Drink");
                this.drinkItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.drinkItemsVBox;
                break;
            case "Food":
                productDict = productDAO.getProductDictByCategory("Food");
                this.foodItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.foodItemsVBox;
                break;
            case "Merch":
                productDict = productDAO.getProductDictByCategory("Merch");
                this.merchItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.merchItemsVBox;
                break;
        }
        
        switch(this.curOrderByMode){
            case "default":
                this.refreshStatisticsItemsVBoxOrderByDefault(statisticsItemsVBox, productDict, fromDate, toDate);
                break;
            case "ASC":
                this.refreshStatisticsItemsVBoxOrderByASC(statisticsItemsVBox, productDict, fromDate, toDate);
                break;
            case "DESC":
                this.refreshStatisticsItemsVBoxOrderByDESC(statisticsItemsVBox, productDict, fromDate, toDate);
                break;
        } 
        
        this.statisticsItemsScrollPane.setContent(statisticsItemsVBox);
    }
    
    private void refreshStatisticsItemsVBoxs(String date){
        if(this.curProductCategoryInStatisticsItems.isEmpty() || this.curOrderByMode.isEmpty()){
            return;
        }
        
        TreeMap<String, Product> productDict = null;
        VBox statisticsItemsVBox = new VBox();
        switch(this.curProductCategoryInStatisticsItems){
            case "Drink":
                productDict = productDAO.getProductDictByCategory("Drink");
                this.drinkItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.drinkItemsVBox;
                break;
            case "Food":
                productDict = productDAO.getProductDictByCategory("Food");
                this.foodItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.foodItemsVBox;
                break;
            case "Merch":
                productDict = productDAO.getProductDictByCategory("Merch");
                this.merchItemsVBox.getChildren().clear();
                statisticsItemsVBox = this.merchItemsVBox;
                break;
        }
        
        switch(this.curOrderByMode){
            case "default":
                this.refreshStatisticsItemsVBoxOrderByDefault(statisticsItemsVBox, productDict, date);
                break;
            case "ASC":
                this.refreshStatisticsItemsVBoxOrderByASC(statisticsItemsVBox, productDict, date);
                break;
            case "DESC":
                this.refreshStatisticsItemsVBoxOrderByDESC(statisticsItemsVBox, productDict, date);
                break;
        } 
        
        this.statisticsItemsScrollPane.setContent(statisticsItemsVBox);
    } 
    
    private void runStatisticsOrderByChoiceBox(){
        switch(this.statisticsOrderByChoiceBox.getValue()){
            case "預設":
                this.curOrderByMode = "default";
                break;
            case "降序":
                this.curOrderByMode = "DESC";
                break;
            case "升序":
                this.curOrderByMode = "ASC";
                break;
        }
        switch(this.curStaticsFunctionsOption){
            case "日銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsDate);
                break;
            case "週銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsFromDate, this.curStatisticsToDate);
                break;
            case "月銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsMonth);
                break;
        }
    }
    
    private void runStatisticsCategoryChoiceBox(){
        switch(this.statisticsCategoryChoiceBox.getValue()){
            case "飲料":
                this.curProductCategoryInStatisticsItems = "Drink";
                break;
            case "食物":
                this.curProductCategoryInStatisticsItems = "Food";
                break;
            case "周邊":
                this.curProductCategoryInStatisticsItems = "Merch";
                break;
        }
        switch(this.curStaticsFunctionsOption){
            case "日銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsDate);
                break;
            case "週銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsFromDate, this.curStatisticsToDate);
                break;
            case "月銷售數量":
                this.refreshStatisticsItemsVBoxs(this.curStatisticsMonth);
                break;
        }
    }
    
    private void runMonthlyDatePicker(){
        LocalDate dateFromDatePicker = this.monthlyDatePicker.getValue();

        String textOfDate = dateFromDatePicker.format(DateTimeFormatter.ofPattern("yyyy/MM"));
        if(monthlyDatePicker.getSkin() != null){
            Platform.runLater(() -> {
                SkinBase<DatePicker> skin = (SkinBase<DatePicker>) monthlyDatePicker.getSkin();
                for (Node child : skin.getChildren()) {
                    if(child instanceof TextField){
                        TextField textField = (TextField) child;
                        textField.setText(textOfDate);
                        textField.setEditable(false);
                        break;
                    }
                }
            });
        }
        
        this.curStatisticsMonth = dateFromDatePicker.format(DateTimeFormatter.ofPattern("yyyyMM"));
        this.refreshStatisticsItemsVBoxs(this.curStatisticsMonth);
    }
    
    private void runWeeklyDatePicker(){
        LocalDate dateFromDatePicker = this.weeklyDatePicker.getValue();

        LocalDate sundayOfWeek = null;
        switch(dateFromDatePicker.getDayOfWeek().toString()){
            case "SUNDAY":
                sundayOfWeek = dateFromDatePicker;
               break;
            case "MONDAY":
                sundayOfWeek = dateFromDatePicker.minusDays(1);
                break;
            case "TUESDAY":
                sundayOfWeek = dateFromDatePicker.minusDays(2);
                break;
            case "WEDNESDAY":
                sundayOfWeek = dateFromDatePicker.minusDays(3);
                break;
            case "THURSDAY":
                sundayOfWeek = dateFromDatePicker.minusDays(4);
                break;
            case "FRIDAY":
                sundayOfWeek = dateFromDatePicker.minusDays(5);
                break;
            case "SATURDAY":  
                sundayOfWeek = dateFromDatePicker.minusDays(6);
                break;
        }

        String textOfDate = sundayOfWeek.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + " - " + sundayOfWeek.plusWeeks(1).minusDays(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if(weeklyDatePicker.getSkin() != null){
            Platform.runLater(() -> {
                SkinBase<DatePicker> skin = (SkinBase<DatePicker>) weeklyDatePicker.getSkin();
                for (Node child : skin.getChildren()) {
                    if(child instanceof TextField){
                        TextField textField = (TextField) child;
                        textField.setText(textOfDate);
                        textField.setEditable(false);
                        break;
                    }
                }
            });
        }
        
        this.curStatisticsFromDate = sundayOfWeek.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.curStatisticsToDate = sundayOfWeek.plusWeeks(1).minusDays(1).plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.refreshStatisticsItemsVBoxs(this.curStatisticsFromDate, this.curStatisticsToDate);
    }
    
    private void runDailyDatePicker(){
        LocalDate dateFromDatePicker = this.dailyDatePicker.getValue();

        if(dailyDatePicker.getSkin() != null){
            Platform.runLater(() -> {
                SkinBase<DatePicker> skin = (SkinBase<DatePicker>) dailyDatePicker.getSkin();
                for (Node child : skin.getChildren()) {
                    if(child instanceof TextField){
                        TextField textField = (TextField) child;
                        textField.setEditable(false);
                    }
                }
            });
        }
        
        this.curStatisticsDate = dateFromDatePicker.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.refreshStatisticsItemsVBoxs(this.curStatisticsDate);
    }
    
    private void refreshStatisticsSettingHBox(String curOption){
        if(curOption != null){
            this.statisticsSettingHBox.getChildren().remove(1, this.statisticsSettingHBox.getChildren().size());
            switch(curOption){
                case "日銷售數量":
                    this.statisticsSettingHBox.getChildren().add(this.dailyDatePicker);
                    this.runDailyDatePicker();
                    break;
                case "週銷售數量":
                    this.statisticsSettingHBox.getChildren().add(this.weeklyDatePicker);  
                    this.runWeeklyDatePicker();
                    break;
                case "月銷售數量":
                    this.statisticsSettingHBox.getChildren().add(this.monthlyDatePicker);
                    this.runMonthlyDatePicker();
                    break;
            }
            if(!this.statisticsSettingHBox.getChildren().contains(this.statisticsCategoryChoiceBox)){
                this.statisticsSettingHBox.getChildren().add(this.statisticsCategoryChoiceBox);
            }
            if(!this.statisticsSettingHBox.getChildren().contains(this.statisticsOrderByChoiceBox)){
                this.statisticsSettingHBox.getChildren().add(this.statisticsOrderByChoiceBox);
            }
        }
    }
    // function ↑
    
    private void setFunctionButtonsVBox(){
      this.functionButtonsVBox.setId("functionButtonsVBox");
      this.functionButtonsVBox.setPrefSize(this.root.getPrefWidth()*1/9, this.root.getPrefHeight());
      
      this.setStatisticsFunctionButton();
      this.setStaffFunctionButton();
      this.setBackToMenuFunctionButton();
      
      this.functionButtonsVBox.getChildren().add(this.statisticsFunctionButton);
      this.functionButtonsVBox.getChildren().add(this.staffFunctionButton);
      this.functionButtonsVBox.getChildren().add(this.backToMenuFunctionButton);
    }
    
        private void setStatisticsFunctionButton(){
            this.statisticsFunctionButton.setText("銷售\n統計");
            this.statisticsFunctionButton.setOnAction((ActionEvent event) -> {
                this.root.getChildren().clear();
                this.root.getChildren().add(this.functionButtonsVBox);
                this.root.getChildren().add(this.statisticsSceneVBox);
            });
        }
    
        private void setStaffFunctionButton(){
            this.staffFunctionButton.setText("員工\n資訊");
            this.staffFunctionButton.setOnAction((ActionEvent event) -> {
                this.root.getChildren().clear();
                this.root.getChildren().add(this.functionButtonsVBox);
                this.root.getChildren().add(this.staffSceneVBox);
            });
        }
    
        private void setBackToMenuFunctionButton(){
            this.backToMenuFunctionButton.setText("返回\n選單");
            this.backToMenuFunctionButton.setOnAction((ActionEvent event) -> {
                this.sceneManager.switchToMenu();
            });
        }

    private void setStatisticsSceneVBox(){
        this.statisticsSceneVBox.setPrefSize(this.root.getPrefWidth()*8/9, this.root.getPrefHeight());
        
        this.setStatisticsSettingHBox();
        this.setStatisticsItemsScrollPane();
        
        this.statisticsSceneVBox.getChildren().add(this.statisticsSettingHBox);
        this.statisticsSceneVBox.getChildren().add(this.statisticsItemsScrollPane);
    }
    
        private void setStatisticsSettingHBox(){
            this.statisticsSettingHBox.setPrefSize(this.statisticsSceneVBox.getPrefWidth(), this.statisticsSceneVBox.getPrefHeight()*1/9);
            this.statisticsSettingHBox.setId("statisticsSettingHBox");
            
            this.setStatisticsFunctionsChoiceBox();
            this.setDailyDatePicker();
            this.setWeeklyDatePicker();
            this.setMonthlyDatePicker();
            this.setStatisticsCategoryChoiceBox();
            this.setStatisticsOrderByChoiceBox();

            this.statisticsSettingHBox.getChildren().add(this.statisticsFunctionsChoiceBox);
        }
    
            private void setStatisticsFunctionsChoiceBox(){
                this.statisticsFunctionsChoiceBox.setId("statisticsFunctionsChoiceBox");
                this.statisticsFunctionsChoiceBox.getItems().addAll(this.statisticsfunctionsOptions);
                
                // if click the weekly or monthly date picker then click this choice box 
                // there is a bug that text in (weekly or monthly) date picker change back
                // set this on mouse clicked event is only a temp solution  
                this.statisticsFunctionsChoiceBox.setOnMouseClicked((MouseEvent event) -> {
                    this.curStaticsFunctionsOption = this.statisticsFunctionsChoiceBox.getValue();
                    this.refreshStatisticsSettingHBox(this.curStaticsFunctionsOption);
                });
                this.statisticsFunctionsChoiceBox.setOnAction((ActionEvent event) -> {
                    this.curStaticsFunctionsOption = this.statisticsFunctionsChoiceBox.getValue();
                    this.refreshStatisticsSettingHBox(this.curStaticsFunctionsOption);
                    switch(this.curStaticsFunctionsOption){
                        case "日銷售數量":
                            if(this.curStatisticsDate != null && this.curProductCategoryInStatisticsItems != null){
                                this.refreshStatisticsItemsVBoxs(this.curStatisticsDate);
                            }
                            break;
                        case "週銷售數量":
                            if(this.curStatisticsFromDate != null && this.curStatisticsToDate != null && this.curProductCategoryInStatisticsItems != null){
                                this.refreshStatisticsItemsVBoxs(this.curStatisticsFromDate, this.curStatisticsToDate);
                            }
                            break;
                        case "月銷售數量":
                            if(this.curStatisticsMonth != null && this.curProductCategoryInStatisticsItems != null){
                                this.refreshStatisticsItemsVBoxs(this.curStatisticsMonth);
                            }
                            break;
                    }
                });
                this.statisticsFunctionsChoiceBox.skinProperty().addListener(new ChangeListener(){
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if(statisticsFunctionsChoiceBox.getSkin() != null){
                            Platform.runLater(() -> {
                                SkinBase<ChoiceBox<String>> choiceBoxSkin = (SkinBase<ChoiceBox<String>>) statisticsFunctionsChoiceBox.getSkin();
                                for (Node child : choiceBoxSkin.getChildren()) {
                                    if (child instanceof Label) {
                                        Label label = (Label) child;
                                        if (label.getText().isEmpty()) {
                                            label.setText("功能選擇");
                                        }
                                        break;
                                    }
                                }
                            });
                        }
                    }
                });

            }
    
            private void setDailyDatePicker(){
                this.dailyDatePicker.setValue(LocalDate.now());
                this.dailyDatePicker.setOnAction((ActionEvent event) -> {
                    this.runDailyDatePicker();
                });
                this.dailyDatePicker.skinProperty().addListener((Observable observable) -> {
                    runDailyDatePicker();
                });
            }
             
            private void setWeeklyDatePicker(){
                this.weeklyDatePicker.setValue(LocalDate.now());
                this.weeklyDatePicker.setOnAction((ActionEvent event) -> {
                    this.runWeeklyDatePicker();
                });
                
                // it's seem somehow runWeeklyDatePicker() isn't work when the program first run
                // text in textField of weeklyDatePicker doesn't change till choose other functions then back to it
                // however while add this listener it fix the problem
                this.weeklyDatePicker.skinProperty().addListener((Observable observable) -> {
                    runWeeklyDatePicker();
                });
            }

            private void setMonthlyDatePicker(){
                this.monthlyDatePicker.setValue(LocalDate.now());
                this.monthlyDatePicker.setOnAction((ActionEvent event) -> {
                    this.runMonthlyDatePicker();
                });
                this.monthlyDatePicker.skinProperty().addListener((Observable observable) -> {
                    runMonthlyDatePicker();
                });
            }
            
            private void setStatisticsCategoryChoiceBox(){
                this.statisticsCategoryChoiceBox.getItems().addAll(this.statisticsCategoryOptions);
                this.statisticsCategoryChoiceBox.setOnMouseClicked((MouseEvent event) -> {
                    this.refreshStatisticsSettingHBox(this.curStaticsFunctionsOption);
                });
                this.statisticsCategoryChoiceBox.setOnAction((ActionEvent event) -> {
                    this.runStatisticsCategoryChoiceBox();
                });
                this.statisticsCategoryChoiceBox.skinProperty().addListener(new ChangeListener(){
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if(statisticsCategoryChoiceBox.getSkin() != null){
                            Platform.runLater(() -> {
                                SkinBase<ChoiceBox<String>> choiceBoxSkin = (SkinBase<ChoiceBox<String>>) statisticsCategoryChoiceBox.getSkin();
                                for (Node child : choiceBoxSkin.getChildren()) {
                                    if (child instanceof Label) {
                                        Label label = (Label) child;
                                        if (label.getText().isEmpty()) {
                                            label.setText("商品類型");
                                        }
                                        break;
                                    }
                                }
                            });
                        }
                    }
                });
                
            }
            
            private void setStatisticsOrderByChoiceBox(){
                this.statisticsOrderByChoiceBox.getItems().addAll(this.statisticsOrderByOptions);
                this.statisticsOrderByChoiceBox.setOnMouseClicked((MouseEvent event) -> {
                    this.refreshStatisticsSettingHBox(this.curStaticsFunctionsOption);
                });
                this.statisticsOrderByChoiceBox.setOnAction((ActionEvent event) -> {
                    this.runStatisticsOrderByChoiceBox();
                });
                this.statisticsOrderByChoiceBox.skinProperty().addListener(new ChangeListener(){
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if(statisticsOrderByChoiceBox.getSkin() != null){
                            Platform.runLater(() -> {
                                SkinBase<ChoiceBox<String>> choiceBoxSkin = (SkinBase<ChoiceBox<String>>) statisticsOrderByChoiceBox.getSkin();
                                for (Node child : choiceBoxSkin.getChildren()) {
                                    if (child instanceof Label) {
                                        Label label = (Label) child;
                                        if (label.getText().isEmpty()) {
                                            label.setText("排序方式");
                                        }
                                        break;
                                    }
                                }
                            });
                        }
                    }
                });
            }
            
        private void setStatisticsItemsScrollPane(){
            this.statisticsItemsScrollPane.setPrefSize(this.statisticsSceneVBox.getPrefWidth(), this.statisticsSceneVBox.getPrefHeight()*8/9);

            this.setStatisticsItemsVBoxs();
        }
    
            private void setStatisticsItemsVBoxs(){
                for(VBox statisticsItemsVBox : this.statisticsItemsVBoxs){
                    statisticsItemsVBox.setId("statisticsItemsVBox");
                    statisticsItemsVBox.setPrefSize(this.statisticsItemsScrollPane.getPrefWidth(), this.statisticsItemsScrollPane.getPrefHeight());
                }
        }
    
    private void setStaffSceneVBox(){
        this.staffSceneVBox.setPrefSize(this.root.getPrefWidth()*8/9, this.root.getPrefHeight());
        
        this.setStaffScrollPane();
        
        this.staffSceneVBox.getChildren().add(this.staffScrollPane);
    }
    
        private void setStaffScrollPane(){
            this.staffScrollPane.setPrefSize(this.staffSceneVBox.getPrefWidth(), this.staffSceneVBox.getPrefHeight());
            
            this.setStaffFlowPane();
            
            this.staffScrollPane.setContent(this.staffFlowPane);
        }
        
            private void setStaffFlowPane(){
                this.staffDict = this.staffDAO.getAllStaffDict();
                
                this.staffFlowPane.setId("staffFlowPane");
                this.staffFlowPane.setPrefSize(this.staffScrollPane.getPrefWidth(), this.staffScrollPane.getPrefHeight());
                this.staffFlowPane.setPrefWrapLength(this.staffFlowPane.getPrefWidth());

                for(Staff staff : this.staffDict.values()){
                    VBox staffFlowPaneVBox = new VBox();
                    staffFlowPaneVBox.setId("staffFlowPaneVBox");
                    try{
                        // staffFlowPaneVBox
                            // staffImageView
                            // staffNameLabel
                            // staffDepartmentLabel
                            String photoPath = "/img/Staff/" + staff.getImagePath();
                            Image img = new Image(photoPath);
                            ImageView staffImageView = new ImageView(img);
                            staffImageView.setFitWidth(180);
                            staffImageView.setPreserveRatio(true);

                            Label staffNameLabel = new Label();
                            staffNameLabel.setId("staffNameLabel");
                            staffNameLabel.setText(staff.getName());

                            Label staffDepartmentLabel = new Label();
                            staffDepartmentLabel.setId("staffDepartmentLabel");
                            staffDepartmentLabel.setText(staff.getDepartment());
                        
                        staffFlowPaneVBox.getChildren().add(staffImageView);
                        staffFlowPaneVBox.getChildren().add(staffNameLabel);
                        staffFlowPaneVBox.getChildren().add(staffDepartmentLabel);
                    } catch(Exception e){
                        System.out.println(e);
                    }
                    this.staffFlowPane.getChildren().add(staffFlowPaneVBox);
                }
            }
}
