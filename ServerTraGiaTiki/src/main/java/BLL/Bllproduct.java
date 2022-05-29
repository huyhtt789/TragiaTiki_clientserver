package BLL;

import DAL.DALproduct;
import ENTITY.product;

import java.util.ArrayList;

public class Bllproduct {
    DALproduct daLproduct;

    public Bllproduct(){ daLproduct = new DALproduct(); }

    public ArrayList<product> getProducts(String namesp){
        ArrayList<product> products = null;
        if(daLproduct.getProducts(namesp).size() > 0){
            products = daLproduct.getProducts(namesp);
        }
        return products;
    }

    public ArrayList<product> getProductsprice(String idsp){
        ArrayList<product> products = null;
        if(daLproduct.getProductsprice(idsp).size() > 0){
            products = daLproduct.getProductsprice(idsp);
        }
        return products;
    }

    public ArrayList<String> getProductsName(){
        ArrayList<String> products = null;
        if(daLproduct.getProductsName().size() > 0){
            products = daLproduct.getProductsName();
        }
        return products;
    }
}
