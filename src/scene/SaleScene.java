package scene;

import db.OrderDAO;
import db.ProductDAO;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Order;
import models.Product;
import models.Sale;

public class SaleScene {
    private final double SCENE_WIDTH = 850;
    private final double SCENE_HEIGHT = SCENE_WIDTH / 600 * 450;
    private final String MAIN_CSS = getClass().getResource("/css/main.css").toExternalForm();
    private final String CSS = getClass().getResource("/css/saleScene.css").toExternalForm();
    
    private double mousePressedX, mousePressedY;
    private double cancelOrderDeltaX;
    
    private TreeMap<String, Product> productDict;
    private ObservableList<Order> ordersList = FXCollections.observableArrayList();  
    private Sale curSale;
    
    private String curProductTypeInFlowPane = "Drink";
    private String paymentMethod = "Cash";
    private String LINE_PayCode = "";
    private String creditCardCode = "";
    private String discountCode = "";
    private String carrierCode = "";
    private boolean discount = false;
    private float discountRate = 0.8f;
    
    private Stage primaryStage;
    
    private HBox root = new HBox();
        private VBox left = new VBox();
            private HBox functionButtonsHBox = new HBox();
                private Button backToMenuFunctionButton = new Button();
                private HBox categoriesHBox = new HBox();
            private ScrollPane itemsScrollPane = new ScrollPane();
                private FlowPane drinkItemsFlowPane = new FlowPane();
                private FlowPane foodItemsFlowPane = new FlowPane();
                private FlowPane merchItemsFlowPane = new FlowPane();
                private FlowPane[] itemsFlowPanes = {drinkItemsFlowPane, foodItemsFlowPane, merchItemsFlowPane};
        private VBox right = new VBox();
            private ScrollPane ordersScrollPane = new ScrollPane();
            //ordersFlowPane
            private GridPane paymentFunctionGridPane = new GridPane();
            private Label paymentLabel = new Label();
            private HBox buttonsHBox = new HBox();
            
    private Stage hitStage = new Stage();
    
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();
    
    private SceneManager sceneManager = new SceneManager();
    
    public SaleScene(){}
    
    public Scene build(){
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
        
        setLeftMenu();
        setRightMenu();
        
        root.getChildren().add(left);
        root.getChildren().add(right);
        
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(MAIN_CSS);
        scene.getStylesheets().add(CSS);
        return scene;
    }
    
    private void writeSalesRecordToDB(){
        curSale = new Sale(getPayment(discount), paymentMethod, LINE_PayCode, creditCardCode, discountCode, carrierCode);
        orderDAO.insertSaleRecord(curSale);
        orderDAO.insertOrdersRecord(curSale.getId(), ordersList);
    }
    
    public int getPayment(boolean discounted){
        int payment = 0;
        for(Order order : ordersList){
            if(order.getProductQuantity()== 0){
                ordersList.remove(order);
                refreshOrdersFlowPane();
            }
            payment += order.getProductPrice()* order.getProductQuantity();
        }
        if(!discounted){
            return payment;
        }
        return Math.round(payment * discountRate);
    }        
    
    private void refreshPaymentLabel(){
        if(discount){
            String text = String.format("總金額：%d * %.2f = %d", getPayment(false), discountRate, getPayment(true));
            paymentLabel.setText(text);
        }else{
            paymentLabel.setText("總金額：" + String.valueOf(getPayment(false)));
        }
    }        
    
    private void addOrderedItem(Product orderedItem){
        int orderIdInOrdersList = -1;
        for(int i = 0; i < ordersList.size(); i++){
            Order order = ordersList.get(i);
            if(order.getProductId().equalsIgnoreCase(orderedItem.getId())){
                orderIdInOrdersList = i;
                break;
            }
        }
        
        if(orderIdInOrdersList == -1){
            ordersList.add(new Order(orderedItem.getId(), orderedItem.getName(), orderedItem.getPrice()));
        }else{
            Order order = ordersList.get(orderIdInOrdersList);
            order.setProductQuantity(order.getProductQuantity() + 1);
        }
    }
    
    private void refreshOrdersFlowPane(){
        FlowPane ordersFlowPane = new FlowPane();
        ordersFlowPane.setId("ordersFlowPane");
        ordersFlowPane.setPrefSize(ordersScrollPane.getPrefWidth(), ordersScrollPane.getPrefHeight());
        
        for(Order order : ordersList){
            VBox orderVBox = new VBox();
            orderVBox.setPrefSize(ordersFlowPane.getPrefWidth(), 60);
            
            orderVBox.setOnMousePressed((MouseEvent event) -> {
                mousePressedX = event.getSceneX();
                mousePressedY = event.getSceneY();
            });
            orderVBox.setOnMouseDragged((MouseEvent event) -> {
                double deltaX = event.getSceneX() - mousePressedX;
                double deltaY = event.getSceneY() - mousePressedY;
                
                if(!hitStage.isShowing()){
                    orderVBox.setTranslateX(deltaX);
                    orderVBox.setTranslateY(deltaY);
                }
            });
            orderVBox.setOnMouseReleased((MouseEvent event) -> {
                if(!hitStage.isShowing()){
                    if(Math.abs(orderVBox.getTranslateX()) > cancelOrderDeltaX){
                        ordersList.remove(order);
                        refreshOrdersFlowPane();
                    }else{
                        orderVBox.setTranslateX(0.0);
                        orderVBox.setTranslateY(0.0);
                    }
                }
            });
            
                Label orderLabel = new Label();
                orderLabel.setPrefSize(orderVBox.getPrefWidth(), (double)orderVBox.getPrefHeight() * 2 / 3);
                orderLabel.setAlignment(Pos.CENTER);
                orderLabel.setText(order.getProductName());

                HBox orderButtonHBox = new HBox();
                orderButtonHBox.setId("orderButtonHBox");
                orderButtonHBox.setPrefSize(orderVBox.getPrefWidth(), (double)orderVBox.getPrefHeight() * 2 / 3);
                    Button orderDecreaseButton = new Button();
                    orderDecreaseButton.setPrefSize((double)orderButtonHBox.getPrefWidth() * 1 / 6, orderButtonHBox.getPrefHeight());
                    orderDecreaseButton.setId("orderDecreaseButton");
                    orderDecreaseButton.setText("-");
                    orderDecreaseButton.setOnAction((ActionEvent event) -> {
                        if(!hitStage.isShowing()){
                            if(order.getProductQuantity()> 1){
                                order.setProductQuantity(order.getProductQuantity() - 1);
                            } else if(order.getProductQuantity() == 1){
                                ordersList.remove(order);
                            }
                            refreshOrdersFlowPane();
                        }
                    });

                    TextField orderTextField = new TextField();
                    orderTextField.setPrefSize((double)orderButtonHBox.getPrefWidth() * 1 / 3, orderButtonHBox.getPrefHeight());
                    orderTextField.setText(String.valueOf(order.getProductQuantity()));
                    orderTextField.setOnMouseClicked((MouseEvent event) -> {
                        if(!hitStage.isShowing()){
                            orderTextField.clear();
                        }
                    });
                    orderTextField.setOnKeyPressed((KeyEvent event) -> {
                        if(!hitStage.isShowing()){
                            if(orderTextField.getText().contains("字")){
                                orderTextField.clear();
                            }
                            if(event.getCode() == KeyCode.ENTER){
                                refreshOrdersFlowPane();
                            }
                            if(event.getCode().isLetterKey()){
                                orderTextField.clear();
                                orderTextField.setText("請輸入數字");
                            }
                        }
                    });
                    orderTextField.setOnKeyReleased((KeyEvent event) -> {
                        if(!hitStage.isShowing()){
                            if(orderTextField.getText().contains("字")){
                                orderTextField.clear();
                            }else{
                                String inputStr = orderTextField.getText();
                                try{
                                    order.setProductQuantity(Integer.parseInt(inputStr));
                                } catch(Exception e){
                                    orderTextField.clear();
                                    orderTextField.setText("請輸入數字");
                                    System.out.println(e);
                                }
                            }
                        }
                    });
                    
                    
                    Button orderIncreaseButton = new Button();
                    orderIncreaseButton.setPrefSize((double)orderButtonHBox.getPrefWidth() * 1 / 6, orderButtonHBox.getPrefHeight());
                    orderIncreaseButton.setId("orderIncreaseButton");
                    orderIncreaseButton.setText("+");
                    orderIncreaseButton.setOnAction((ActionEvent event) -> {
                        if(!hitStage.isShowing()){
                            order.setProductQuantity(order.getProductQuantity() + 1);
                            refreshOrdersFlowPane();
                        }
                    });
                    
                orderButtonHBox.getChildren().add(orderDecreaseButton);
                orderButtonHBox.getChildren().add(orderTextField);
                orderButtonHBox.getChildren().add(orderIncreaseButton);
                
            orderVBox.getChildren().add(orderLabel);
            orderVBox.getChildren().add(orderButtonHBox);
            
            ordersFlowPane.getChildren().add(orderVBox);
        }
        
        ordersScrollPane.setContent(ordersFlowPane);
        
        refreshPaymentLabel();
    }   
    
    private void refreshItemsFlowPane(String productType){
        if(productType.equalsIgnoreCase("Drink")){
//            productDict = CategoryProducts.readDrinkProductFromFile();
            productDict = productDAO.getProductDictByCategory("Drink");
            itemsScrollPane.setContent(drinkItemsFlowPane);
        }else if(productType.equalsIgnoreCase("Food")){
//            productDict = CategoryProducts.readFoodProductFromFile();
            productDict = productDAO.getProductDictByCategory("Food");
            itemsScrollPane.setContent(foodItemsFlowPane);
        }else if(productType.equalsIgnoreCase("Merch")){
//            productDict = CategoryProducts.readMerchProductFromFile();
            productDict = productDAO.getProductDictByCategory("Merch");
            itemsScrollPane.setContent(merchItemsFlowPane);
        }
    }
    
    private void setLeftMenu(){
        left.setPrefSize((double)root.getPrefWidth() * 2 / 3, root.getPrefHeight());

        setFunctionButtonsHBox();
        setItemsScrollPane();

        left.getChildren().add(functionButtonsHBox);
        left.getChildren().add(itemsScrollPane);
    }
    
    private void setFunctionButtonsHBox(){
        this.functionButtonsHBox.setPrefSize(left.getPrefWidth(), left.getPrefHeight() * 1 / 9);
        this.functionButtonsHBox.setId("functionButtonsHBox");
        
        this.setBackToMenuFunctionButton();
        this.setCategoriesHBox();
        
        this.functionButtonsHBox.getChildren().add(this.backToMenuFunctionButton);
        this.functionButtonsHBox.getChildren().add(this.categoriesHBox);
    }
    
    private void setBackToMenuFunctionButton(){
        this.backToMenuFunctionButton.setPrefSize(this.functionButtonsHBox.getPrefWidth() * 1 / 9, this.functionButtonsHBox.getPrefHeight() * 1 / 3);
        this.backToMenuFunctionButton.setId("backToMenuFunctionButton");
        this.backToMenuFunctionButton.setText("返回選單");
        this.backToMenuFunctionButton.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing()){
                checkoutedReset();
                this.sceneManager.switchToMenu();
            }
        });
    }
    
    private void setCategoriesHBox(){
        categoriesHBox.setPrefSize(this.functionButtonsHBox.getPrefWidth() * 8 / 9, this.functionButtonsHBox.getPrefHeight());
        categoriesHBox.setId("categoriesHBox");
        
        Button drinkBtn = new Button();
        drinkBtn.setPrefHeight(120);
        drinkBtn.setText(" 飲料 ");
        drinkBtn.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing()){
                curProductTypeInFlowPane = "Drink";
                refreshItemsFlowPane(curProductTypeInFlowPane);
            }
        });
        
        Button foodBtn = new Button();
        foodBtn.setPrefHeight(120);
        foodBtn.setText(" 食物 ");
        foodBtn.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing()){
                curProductTypeInFlowPane = "Food";
                refreshItemsFlowPane(curProductTypeInFlowPane);
            }
        });
        
        Button merchBtn = new Button();
        merchBtn.setPrefHeight(120);
        merchBtn.setText(" 周邊 ");
        merchBtn.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing()){
                curProductTypeInFlowPane = "Merch";
                refreshItemsFlowPane(curProductTypeInFlowPane);
            }
        });

        categoriesHBox.getChildren().add(drinkBtn);
        categoriesHBox.getChildren().add(foodBtn);
        categoriesHBox.getChildren().add(merchBtn);
    }
    
    private void setItemsScrollPane(){
        itemsScrollPane.setPrefSize(left.getPrefWidth(), (double)left.getPrefHeight() * 8 / 9);
        
        setItemsFlowPanes();
        
        refreshItemsFlowPane(curProductTypeInFlowPane);
    }
    
    private void setItemsFlowPanes(){
        String productType = "";
        for(FlowPane itemsFlowPane : itemsFlowPanes){
            if(itemsFlowPane.equals(drinkItemsFlowPane)){
                productType = "Drink";
                productDict = productDAO.getProductDictByCategory("Drink");
            }else if(itemsFlowPane.equals(foodItemsFlowPane)){
                productType = "Food";
                productDict = productDAO.getProductDictByCategory("Food");
            }else if(itemsFlowPane.equals(merchItemsFlowPane)){
                productType = "Merch";
                productDict = productDAO.getProductDictByCategory("Merch");
            }
            itemsFlowPane.setId("itemsFlowPane");
            itemsFlowPane.setPrefSize(itemsScrollPane.getPrefWidth(), itemsScrollPane.getPrefHeight());
            itemsFlowPane.setPrefWrapLength(itemsFlowPane.getPrefWidth());

            for(String itemId : productDict.keySet()){
                Button itemBtn = new Button();
                try{
                    String photoPath = "/img/" + productType + "/" + productDict.get(itemId).getImagePath();
                    Image img = new Image(photoPath);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(150);
                    imgView.setPreserveRatio(true);

                    itemBtn.setGraphic(imgView);
                } catch(Exception e){
                    System.out.println(e);
                    itemBtn.setText(productDict.get(itemId).getName());
                }
                itemBtn.setOnAction((ActionEvent event) -> {
                    if(!hitStage.isShowing()){
                        addOrderedItem(productDict.get(itemId));
                        refreshOrdersFlowPane();
                    }
                });

                itemsFlowPane.getChildren().add(itemBtn);
            }
        }
    }
    
    private void setRightMenu(){
        right.setPrefSize((double)root.getPrefWidth() * 1 / 3, root.getPrefHeight());
        
        setOrderScrollPane();
        setPaymentFunctionGridPane();
        setPaymentLabel();
        setButtonsHBox();
        
        right.getChildren().add(ordersScrollPane);
        right.getChildren().add(paymentFunctionGridPane);
        right.getChildren().add(paymentLabel);
        right.getChildren().add(buttonsHBox);
    }
    
    private void setOrderScrollPane(){
        ordersScrollPane.setPrefSize(right.getPrefWidth(), (double)right.getPrefHeight() * 5 / 9);
        ordersScrollPane.setOnMouseClicked((MouseEvent event) -> {
            refreshPaymentLabel();
        });
        
        cancelOrderDeltaX = (double)ordersScrollPane.getPrefWidth() * 1 / 2;
        
        refreshOrdersFlowPane();
    }
    
    public void showHitStage(String function, String hit){
        if(hitStage.isShowing()){
            return;
        }
        
        hitStage = new Stage();
        
        double hitSceneWidth = 300.0;
        double hitSceneHeight = 200.0;
        
        VBox hitStageRoot = new VBox();
        hitStageRoot.setPrefSize(hitSceneWidth, hitSceneHeight);
        hitStageRoot.setAlignment(Pos.CENTER);

            Label hitLabel = new Label();
            hitLabel.setAlignment(Pos.CENTER);
            hitLabel.setPrefSize(hitSceneWidth, (double)hitSceneHeight * 1 / 3);
            hitLabel.setText(hit);

            TextField barcodeTextField = new TextField();
            barcodeTextField.setMaxSize(hitSceneWidth * 0.9, (double)hitSceneHeight * 1 / 3);

            HBox hitStagebuttonsHBox = new HBox();
            hitStagebuttonsHBox.setPrefSize(hitSceneWidth, (double)hitSceneHeight * 1 / 3);
            hitStagebuttonsHBox.setPadding(new Insets(15, 0, 0, 0));
            hitStagebuttonsHBox.setAlignment(Pos.CENTER);
            hitStagebuttonsHBox.setSpacing(10);

                Button cancelButton = new Button();
                cancelButton.setPrefSize((double)hitStagebuttonsHBox.getPrefWidth() * 1/ 3, hitStagebuttonsHBox.getPrefHeight());
                cancelButton.setText("取消");
                cancelButton.setOnAction((ActionEvent event) -> {
                    if((function.equalsIgnoreCase("LINE_Pay") && !paymentMethod.equalsIgnoreCase("CreditCard")) 
                        || (function.equalsIgnoreCase("CreditCard") && !paymentMethod.equalsIgnoreCase("LINE_Pay"))){
                        paymentMethod = "Cash";
                    }
                    
                    hitStage.close();
                });

                Button confirmButton = new Button();
                confirmButton.setPrefSize((double)hitStagebuttonsHBox.getPrefWidth() * 1/ 3, hitStagebuttonsHBox.getPrefHeight());
                confirmButton.setText("確認");
                confirmButton.setOnAction((ActionEvent event) -> {
                    if(!barcodeTextField.getText().isEmpty()){
                        if(function.equalsIgnoreCase("LINE_Pay")){
                            LINE_PayCode = barcodeTextField.getText();
                        }else if(function.equalsIgnoreCase("CreditCard")){
                            creditCardCode = barcodeTextField.getText();
                        }else if(function.equalsIgnoreCase("Discount")){
                            discountCode = barcodeTextField.getText();
                            
                            // 假設折扣代碼可行
                            discount = true;
                            refreshPaymentLabel();
                        }else if(function.equalsIgnoreCase("Carrier")){
                            carrierCode = barcodeTextField.getText();
                        }
                        hitStage.close();
                    }
                });

            hitStagebuttonsHBox.getChildren().add(cancelButton);
            hitStagebuttonsHBox.getChildren().add(confirmButton);

        hitStageRoot.getChildren().add(hitLabel);
        hitStageRoot.getChildren().add(barcodeTextField);
        hitStageRoot.getChildren().add(hitStagebuttonsHBox);

        Scene hitScene = new Scene(hitStageRoot, hitSceneWidth, hitSceneHeight);
        hitScene.getStylesheets().add(MAIN_CSS);

        
        hitStage.setTitle("提示");
        hitStage.setScene(hitScene);
        hitStage.setResizable(false);
        hitStage.setAlwaysOnTop(true);
        hitStage.show();
        hitStage.setOnCloseRequest((WindowEvent event) -> {
            if((function.equalsIgnoreCase("LINE_Pay") && !paymentMethod.equalsIgnoreCase("CreditCard")) 
                || (function.equalsIgnoreCase("CreditCard") && !paymentMethod.equalsIgnoreCase("LINE_Pay"))){
                paymentMethod = "Cash";
            }
        });
    }
    
    private void setPaymentFunctionGridPane(){
        paymentFunctionGridPane.setPrefSize(right.getPrefWidth(),  (double)right.getPrefHeight() * 2 / 9);
        paymentFunctionGridPane.setId("paymentFunctionGridPane");
        
        Button LINE_PayButton = new Button();
        LINE_PayButton.setText("LINE Pay");
        LINE_PayButton.setPrefSize((double)paymentFunctionGridPane.getPrefWidth() * 1 / 2, (double)paymentFunctionGridPane.getPrefHeight() * 1 / 2);
        LINE_PayButton.setOnAction((ActionEvent event) -> {
            paymentMethod = "LINE_Pay";
            showHitStage("LINE_Pay", "請客人掃QR code或幫客人掃條碼");
        });
        
        Button creditCardButton = new Button();
        creditCardButton.setText("信用卡支付");
        creditCardButton.setPrefSize((double)paymentFunctionGridPane.getPrefWidth() * 1 / 2, (double)paymentFunctionGridPane.getPrefHeight() * 1 / 2);
        creditCardButton.setOnAction((ActionEvent event) -> {
            paymentMethod = "CreditCard";
            showHitStage("CreditCard", "請幫客人刷信用卡");
        });
        
        Button discountButton = new Button();
        discountButton.setText("折扣");
        discountButton.setPrefSize((double)paymentFunctionGridPane.getPrefWidth() * 1 / 2, (double)paymentFunctionGridPane.getPrefHeight() * 1 / 2);
        discountButton.setOnAction((ActionEvent event) -> {
            showHitStage("Discount", "請輸入折扣代碼");
        });
        
        Button carrierButton = new Button();
        carrierButton.setText("載具");
        carrierButton.setPrefSize((double)paymentFunctionGridPane.getPrefWidth() * 1 / 2, (double)paymentFunctionGridPane.getPrefHeight() * 1 / 2);
        carrierButton.setOnAction((ActionEvent event) -> {
            showHitStage("Carrier", "請幫客人刷載具");
        });
        
        paymentFunctionGridPane.add(LINE_PayButton, 0, 0);
        paymentFunctionGridPane.add(creditCardButton, 1, 0);
        paymentFunctionGridPane.add(discountButton, 0, 1);
        paymentFunctionGridPane.add(carrierButton, 1, 1);
    }
    
    private void setPaymentLabel(){
        paymentLabel.setPrefSize(right.getPrefWidth(), (double)right.getPrefHeight() * 1 / 9);
        refreshPaymentLabel();
    }
    
    private void checkoutedReset(){
        discount = false;
        ordersList.clear();
        refreshOrdersFlowPane();
        paymentMethod = "Cash";
        LINE_PayCode = "";
        creditCardCode = "";
        discountCode = "";
        carrierCode = "";
    }
    
    private void showCheckoutStage(){        
        hitStage = new Stage();
        
        double hitSceneWidth = 300.0;
        double hitSceneHeight = 200.0;
        
        VBox hitStageRoot = new VBox();
        hitStageRoot.setPrefSize(hitSceneWidth, hitSceneHeight);
        hitStageRoot.setAlignment(Pos.CENTER);

            Label hitLabel = new Label();
            hitLabel.setAlignment(Pos.CENTER);
            hitLabel.setPrefSize(hitSceneWidth, (double)hitSceneHeight * 1 / 3);
            hitLabel.setText("付款成功！");

            HBox hitStagebuttonsHBox = new HBox();
            hitStagebuttonsHBox.setPrefSize(hitSceneWidth, (double)hitSceneHeight * 1 / 3);
            hitStagebuttonsHBox.setPadding(new Insets(15, 0, 0, 0));
            hitStagebuttonsHBox.setAlignment(Pos.CENTER);
            hitStagebuttonsHBox.setSpacing(10);

                Button confirmButton = new Button();
                confirmButton.setPrefSize((double)hitStagebuttonsHBox.getPrefWidth() * 1/ 3, hitStagebuttonsHBox.getPrefHeight());
                confirmButton.setText("確認");
                confirmButton.setOnAction((ActionEvent event) -> {    
                    checkoutedReset();
                    hitStage.close();
                });
                
            hitStagebuttonsHBox.getChildren().add(confirmButton);

        hitStageRoot.getChildren().add(hitLabel);
        hitStageRoot.getChildren().add(hitStagebuttonsHBox);

        Scene hitScene = new Scene(hitStageRoot, hitSceneWidth, hitSceneHeight);
        hitScene.getStylesheets().add(MAIN_CSS);

        
        hitStage.setTitle("結帳完成");
        hitStage.setScene(hitScene);
        hitStage.setResizable(false);
        hitStage.setAlwaysOnTop(true);
        hitStage.show();
        hitStage.setOnCloseRequest((WindowEvent event) -> {
            checkoutedReset();
        });
    }
    
    private void setButtonsHBox(){
        buttonsHBox.setPrefSize(right.getPrefWidth(), (double)right.getPrefHeight() * 1 / 9);
        buttonsHBox.setId("buttonsHBox");
        
        Button cancelButton = new Button();
        cancelButton.setId("cancelButton");
        cancelButton.setPrefSize((double)buttonsHBox.getPrefWidth() * 1 / 2, buttonsHBox.getPrefHeight());
        cancelButton.setText("取消");
        cancelButton.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing()){
                checkoutedReset();
            }
        });
        
        Button checkoutButton = new Button();
        checkoutButton.setId("checkoutButton");
        checkoutButton.setPrefSize((double)buttonsHBox.getPrefWidth() * 1 / 2, buttonsHBox.getPrefHeight());
        checkoutButton.setText("結帳");
        checkoutButton.setOnAction((ActionEvent event) -> {
            if(!hitStage.isShowing() && !ordersList.isEmpty()){  
                showCheckoutStage();
                writeSalesRecordToDB();
            }
        });
        
        buttonsHBox.getChildren().add(cancelButton);
        buttonsHBox.getChildren().add(checkoutButton);
    }
}
