package DAL;

import ENTITY.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DALproduct extends ConnectDatabase{
    public DALproduct(){ConnectionDB();}

    //Search SP
    public ArrayList<product> getProducts(String namesp){
        ArrayList<product> products = new ArrayList<product>();
        product sp = new product();
        ResultSet resultSet = readTable(namesp);
        if(resultSet != null){
            try{
                while(resultSet.next()){
                    sp = new product();
                    sp.setId(resultSet.getString("id"));
                    sp.setName(resultSet.getString("name"));
                    sp.setPath(resultSet.getString("img_url"));
                    sp.setPrice(resultSet.getInt("price"));
                    products.add(sp);
                }
            }
            catch (SQLException e){
                System.out.println("DAL -> ConnectDatabase -> getProducts -> "+e.getMessage());
            }
        }
        return products;
    }

    //lay price va time ve so do
    public ArrayList<product> getProductsprice(String namesp){
        ArrayList<product> products = new ArrayList<product>();
        product sp = new product();
        ResultSet resultSet = readprice(namesp);
        if(resultSet != null){
            try{
                while(resultSet.next()){
                    sp = new product();
                    sp.setTime(resultSet.getString("time"));
                    sp.setPrice(resultSet.getInt("price"));

                    products.add(sp);
                }
            }
            catch (SQLException e){
                System.out.println("DAL -> ConnectDatabase -> getProductsprice -> "+e.getMessage());
            }
        }
        return products;
    }

    public ArrayList<String> getProductsName()
    {
        ArrayList<String> productsName = new ArrayList<String>();
        ResultSet resultSet = readName();
        if(resultSet != null){
            try{
                while(resultSet.next()){
                    productsName.add(resultSet.getString("name"));
                }
            }
            catch (SQLException e){
                System.out.println("DAL -> ConnectDatabase -> getProductsName -> "+e.getMessage());
            }
        }
        return productsName;
    }
}
