package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
    private static String DB_URL = "jdbc:mariadb://localhost:3307/hololive_en_cafe";
    private static String USER = "holomen";
    private static String PSW = "sodesune";
    private static Connection conn = null;

    public static Connection connectDB() {
        
        try {
            if(conn != null && !conn.isClosed()){
//                System.out.println("已連線成功");
                return conn;
            } else {
                conn = DriverManager.getConnection(DB_URL, USER, PSW);
                System.out.println("資料庫連線成功");
            }
        } catch (SQLException ex) {
            System.out.println("資料庫連線出問題:" + ex.toString());
        }
        return conn;
    }

}
