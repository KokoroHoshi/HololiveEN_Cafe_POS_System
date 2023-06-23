package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import models.Product;

public class ProductDAO {
    private Connection conn;

    public ProductDAO() {conn = Conn.connectDB();}
    
    public TreeMap<String, Product> getProductDictByCategory(String category){
        conn = Conn.connectDB();
        TreeMap<String, Product> productDict = new TreeMap();
        String query = "select * from product where category=" + "'" + category + "'";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                Product product = new Product(
                        rs.getString("id"), 
                        rs.getString("category"), 
                        rs.getString("name"), 
                        rs.getInt("price"),
                        rs.getString("imagePath"), 
                        rs.getString("description"));
                
                productDict.put(product.getId(), product);
             }
        } catch(SQLException e){
            System.out.println("getProductDictByCategory異常:\n" + e.toString());
        }
        
        return productDict;
    }
            
    public void insert(Product product){
        conn = Conn.connectDB();
        String query = "Insert Into product (id,category,name,price,imagePath,description) values (?,?,?,?,?,?)";
        try {
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setString(1, product.getId());
            state.setString(2, product.getCategory());
            state.setString(3, product.getName());
            state.setInt(4, product.getPrice());
            state.setString(5, product.getImagePath());
            state.setString(6, product.getDescription());
            
            state.execute();
            System.out.println("新增紀錄成功");

        } catch (SQLException e) {
            System.out.println("insertProduct異常:\n" + e.toString());
        }

    }
}
