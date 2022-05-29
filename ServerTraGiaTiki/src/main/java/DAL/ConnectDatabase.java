package DAL;

import GUI.JFrameErrorMess;

import java.sql.*;

public class ConnectDatabase {
    static Connection connection;
    static Statement statement;
    static PreparedStatement preparedStatement;

    //Connection
    public static void ConnectionDB(){
        String host = "localhost";
        String port = "3306";
        String dbname = "price_statistics";
        String dbuser = "root";
        String dbpwd = "";

        String dbPath ="jdbc:mysql://"+host+":" + port + "/" + dbname + "?useUnicode=yes&characterEncoding=UTF-8";

        try{
            connection = (Connection) DriverManager.getConnection(dbPath, dbuser, dbpwd);
            statement = connection.createStatement();
            System.out.println("connected");
        }
        catch (SQLException e){
            System.out.println("ERROR -> DatabaseManager -> ConectionDB -> "+e.getMessage());
            JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            jFrameErrorMess.mess("Database","Can't connection");
            System.exit(0);
        }
    }

    public static ResultSet readTable(String namesp){
        //String sql = "SELECT * FROM products WHERE name LIKE '%"+namesp+"%' ORDER BY id ASC LIMIT 100";
        String sql = "SELECT products.id, name, img_url, nPrices.price FROM products LEFT JOIN (SELECT id, price FROM prices WHERE time = CURDATE() )as nPrices ON nPrices.id = products.id WHERE name LIKE '%"+namesp+"%' ORDER BY name LIMIT 100;";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
        }
        catch (SQLException e) {
            System.out.println("DAL -> ConnectDatabase -> readTable -> "+e.getMessage());
        }
        return rs;
    }

    public static ResultSet readprice(String idsp){

        String sql = "SELECT prices.time, price FROM prices WHERE id="+idsp+" ORDER BY time desc";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
        }
        catch (SQLException e) {
            System.out.println("DAL -> ConnectDatabase -> readTable -> "+e.getMessage());
        }
        return rs;
    }

    public static ResultSet readName(){
        String sql = "SELECT name FROM products";
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(sql);
        }
        catch (SQLException e) {
            System.out.println("DAL -> ConnectDatabase -> readTable -> "+e.getMessage());
        }
        return rs;
    }
/*
    public static void main(String[] args){
        ConnectionDB();
    }

 */

}
