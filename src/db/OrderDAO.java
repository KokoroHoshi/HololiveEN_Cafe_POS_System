package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import javafx.collections.ObservableList;
import models.Order;
import models.Sale;

public class OrderDAO {
    private Connection conn;
    
    public OrderDAO() {conn = Conn.connectDB();}
    
    public void insertSaleRecord(Sale sale){
        conn = Conn.connectDB();
        String query = "Insert Into sales_record (id,totalPrice,paymentMethod,LINE_PayCode,creditCardCode,discountCode,carrierCode) values (?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setString(1, sale.getId());
            state.setInt(2, sale.getTotalPrice());
            state.setString(3, sale.getPaymentMethod());
            state.setString(4, sale.getLINE_PayCode());
            state.setString(5, sale.getCreditCardCode());
            state.setString(6, sale.getDiscountCode());
            state.setString(7, sale.getCarrierCode());
            
            state.execute();    
            System.out.println("新增SaleRecord成功");
        } catch (SQLException e) {
            System.out.println("insertSaleRecord異常:\n" + e.toString());
        }
    }
    
    public void insertOrdersRecord(String saleId, ObservableList<Order> ordersList){
       conn = Conn.connectDB();
       
       String query = "Insert Into orders_record (saleId,productId,productName,productPrice,productQuantity) values (?,?,?,?,?)";
       try {
           for(Order order: ordersList){
                PreparedStatement state = conn.prepareStatement(query);

                state.setString(1, saleId);
                state.setString(2, order.getProductId());
                state.setString(3, order.getProductName());
                state.setInt(4, order.getProductPrice());
                state.setInt(5, order.getProductQuantity());
                
                state.execute();
           }
           System.out.println("新增OrdersRecord成功");
        } catch (SQLException e) {
            System.out.println("insertOrdersRecord異常:\n" + e.toString());
        }
    }
    
    public int getProductQuantityByDate(String productId, String date){
        conn = Conn.connectDB();
        int productQuantity = 0;
        String query = "select productId, sum(productQuantity) from orders_record where productId=" + "'" + productId + "'" + "AND saleId LIKE" + "'%" + date + "%' GROUP BY productId;";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                productQuantity = rs.getInt("sum(productQuantity)");
             }
        } catch(SQLException e){
            System.out.println("getProductQuantityByDate異常:\n" + e.toString());
        }
        
        return productQuantity;
    }
    
    public int getProductQuantityByWeeklyDate(String productId, String fromDate, String toDate){
        conn = Conn.connectDB();
        int productQuantity = 0;
        String query = "SELECT orders_record.productId, sum(orders_record.productQuantity) FROM sales_record, orders_record where orders_record.productId=" + "'" + productId + "'" + "AND sales_record.DATE >= '" + fromDate + "' AND sales_record.DATE <= '" + toDate + "' AND sales_record.id = orders_record.saleId GROUP BY orders_record.productId;";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                productQuantity = rs.getInt("sum(orders_record.productQuantity)");
             }
        } catch(SQLException e){
            System.out.println("getProductQuantityByDate異常:\n" + e.toString());
        }
        
        return productQuantity;
    }
    
    public LinkedHashMap<String, Integer> getProductQuantityByDate(String productCategory, String date, String orderBy){
        conn = Conn.connectDB();
        int productQuantity = 0;
        String category = "";
        String query = "";
        LinkedHashMap<String, Integer> productQuantityDict = new LinkedHashMap<String, Integer>();
        
        switch(productCategory){
            case "Drink":
                category = "d";
                break;
            case "Food":
                category = "f";
                break;
            case "Merch":
                category = "m";
                break;
        }
        
        switch(orderBy){
            case "default":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "	from product" +
                        "	left JOIN orders_record" +
                        "		ON product.id = orders_record.productId" +
                        "	WHERE orders_record.saleId LIKE '%" + date + "%'" +
                        "		and product.id LIKE '%" + category + "%'" +
                        "	GROUP BY product.id;";
                break;
            case "ASC":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "	from product" +
                        "	left JOIN orders_record" +
                        "		ON product.id = orders_record.productId" +
                        "	WHERE orders_record.saleId LIKE '%" + date + "%'" +
                        "               and product.id LIKE '%" + category + "%'" +
                        "	GROUP BY product.id" +
                        "	ORDER BY sum(orders_record.productQuantity);";
                break;
            case "DESC":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "	from product" +
                        "	left JOIN orders_record" +
                        "		ON product.id = orders_record.productId" +
                        "	WHERE (orders_record.saleId LIKE '%" + date + "%'" +
                        "		or orders_record.saleId IS NULL)" +
                        "		and product.id LIKE '%" + category + "%'" +
                        "	GROUP BY product.id" +
                        "	ORDER BY sum(orders_record.productQuantity) DESC;";
                break;
            default:
                System.out.println(orderBy);
        }
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                productQuantityDict.put(rs.getString("product.id"), rs.getInt("sum(orders_record.productQuantity)"));
             }
             
        } catch(SQLException e){
            System.out.println("getProductQuantityByDate異常:\n" + e.toString());
        }
        
        return productQuantityDict;
    }
    
    public LinkedHashMap<String, Integer> getProductQuantityByWeeklyDate(String productCategory, String fromDate, String toDate, String orderBy){
        conn = Conn.connectDB();
        int productQuantity = 0;
        String category = "";
        String query = "";
        LinkedHashMap<String, Integer> productQuantityDict = new LinkedHashMap<String, Integer>();
        
        switch(productCategory){
            case "Drink":
                category = "d";
                break;
            case "Food":
                category = "f";
                break;
            case "Merch":
                category = "m";
                break;
        }
        
        switch(orderBy){
            case "default":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "   from  sales_record, product" +
                        "   left JOIN orders_record" +
                        "   ON product.id = orders_record.productId" +
                        "   WHERE sales_record.id = orders_record.saleId" +
                        "		AND (sales_record.DATE >= '" + fromDate + "' AND sales_record.DATE <= '" + toDate + "')" +
                        "      AND product.id LIKE '%" + category + "%'" +
                        "   GROUP BY product.id" +
                        "   ORDER BY product.id;";
                break;
            case "ASC":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "   from  sales_record, product" +
                        "   left JOIN orders_record" +
                        "   ON product.id = orders_record.productId" +
                        "   WHERE sales_record.id = orders_record.saleId" +
                        "		AND (sales_record.DATE >= '" + fromDate + "' AND sales_record.DATE <= '" + toDate + "')" +
                        "      AND product.id LIKE '%" + category + "%'" +
                        "   GROUP BY product.id" +
                        "   ORDER BY sum(orders_record.productQuantity);";
                break;
            case "DESC":
                query = "select product.id, sum(orders_record.productQuantity)" +
                        "   from  sales_record, product" +
                        "   left JOIN orders_record" +
                        "   ON product.id = orders_record.productId" +
                        "   WHERE sales_record.id = orders_record.saleId" +
                        "		AND (sales_record.DATE >= '" + fromDate + "' AND sales_record.DATE <= '" + toDate + "')" +
                        "      AND product.id LIKE '%" + category + "%'" +
                        "   GROUP BY product.id" +
                        "   ORDER BY sum(orders_record.productQuantity) DESC;";
                break;
        }
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                productQuantityDict.put(rs.getString("product.id"), rs.getInt("sum(orders_record.productQuantity)"));
             }
             
        } catch(SQLException e){
            System.out.println("getProductQuantityByWeeklyDate異常:\n" + e.toString());
        }
        
        return productQuantityDict;
    }
}
