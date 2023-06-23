package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;
import models.Staff;

public class StaffDAO {
    private Connection conn;

    public StaffDAO() {conn = Conn.connectDB();}
    
    public TreeMap<String, Staff> getAllStaffDict(){
        conn = Conn.connectDB();
        TreeMap<String, Staff> productDict = new TreeMap();
        String query = "SELECT * FROM staff";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
             while (rs.next()) {
                Staff staff = new Staff(
                        rs.getString("id"), 
                        rs.getString("department"), 
                        rs.getString("name"), 
                        rs.getString("imagePath"), 
                        rs.getString("description"));
                
                productDict.put(staff.getId(), staff);
             }
        } catch(SQLException e){
            System.out.println("getAllStaffDict異常:\n" + e.toString());
        }
        
        return productDict;
    }

}
