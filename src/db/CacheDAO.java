package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CacheDAO {
    private Connection conn;

    public CacheDAO() {conn = Conn.connectDB();}
    
    public String getDate(){
        conn = Conn.connectDB();
        String query = "select date from cache_record";
        String date = "";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                date = rs.getString("date");
            }
        } catch (SQLException e){
            System.out.println("getDate異常:\n" + e.toString());
        }
        return date;
    }
    
    public void updateDate(String date){
        conn = Conn.connectDB();
        String query = "update cache_record set date = '" + date + "'";
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeQuery();
        } catch(SQLException e){
            System.out.println("updateDate異常:\n" + e.toString());
        }
    }
    
    public int getSaleNum(){
        conn = Conn.connectDB();
        String query = "select saleNum from cache_record";
        int saleNum = 0;
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                saleNum = rs.getInt("saleNum");
            }
        }  catch(SQLException e){
            System.out.println("getSaleNum異常:\n" + e.toString());
        }
        return saleNum;
    }
    
    public void updateSaleNum(int saleNum){
        conn = Conn.connectDB();
        String query = "update cache_record set saleNum = '" + String.valueOf(saleNum) + "'";
        
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.executeQuery();
        } catch(SQLException e){
            System.out.println("updateSaleNum異常:\n" + e.toString());
        }
    }
}
