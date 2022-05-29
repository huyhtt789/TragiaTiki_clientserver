package BLL;

import ENTITY.product;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class WorkTGTiki implements Runnable {
    private Socket socket;
    private BufferedReader reader = null;
    private BufferedWriter writer = null;
    private KhoaAES khoaAES = null;

    public WorkTGTiki() {
    }

    public WorkTGTiki(Socket socket, String key) {
        this.khoaAES = new KhoaAES();
        this.khoaAES.setStringKey(key);
        this.socket = socket;
    }

    public void run() {
        System.out.println("Client " + socket.toString() + " accepted");
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String lineMaHoa = reader.readLine();
                String line = khoaAES.decrypt(lineMaHoa);
                if (line.equalsIgnoreCase("byeservertiki")) break;
                work(line);
            }
            System.out.println("Client " + socket.toString() + " close");
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            //Lỗi
            System.out.println("Lỗi Bll -> WorkTGTiki -> run -> "+e.getMessage());
            //e.printStackTrace();
        }
    }

    public void work(String line) {
        StringTokenizer stringTokenizer = new StringTokenizer(line, "^");
        String bool = stringTokenizer.nextToken();
        String nameorid = stringTokenizer.nextToken();
//        System.out.println("line "+nameorid);
        if (bool.equalsIgnoreCase("search")) {
            SearchSP(nameorid);
        }
        if (bool.equalsIgnoreCase("detail")) {
            DetailSP(nameorid);
        }
        if  (bool.equalsIgnoreCase("review")){
            readReview(nameorid);
        }
        if (bool.equalsIgnoreCase("name")) {
            getNames();
        }
    }

    public void sent(String line){
        try {
            String lineMaHoa = khoaAES.encrypt(line);
            writer.write(lineMaHoa + "\n");
            writer.flush();
        } catch (IOException e) {
            //Lỗi
            System.out.println("Lỗi Bll -> WorkTGTiki -> sent");
            e.printStackTrace();
        }
    }

    //Search sent
    public void SearchSP(String line){
        Bllproduct bllproduct = new Bllproduct();
        ArrayList<product> productList = bllproduct.getProducts(line);
        if(productList!=null){
            sent(""+productList.size());
            for(product product : productList){
                String linesent = product.getId()+"^"+product.getName()+"^"+product.getPath()+"^"+product.getPrice();
                sent(linesent);
            }
        }
        else {
            sent("Không tìm thấy sản phẩm nào cả");
        }
    }


    //Detail
    //lay price va time ve bieu do
    public void DetailSP(String line){
        Bllproduct bllproduct = new Bllproduct();
        ArrayList<product> productList = bllproduct.getProductsprice(line);
        if(productList!=null){
            sent(""+productList.size());
            for(product product : productList){
                String linesent = product.getTime()+"^"+product.getPrice();
//                System.out.println("linesent"+linesent);
                sent(linesent);
//                readReview(line);
            }
        }
        else {
            sent("Không tìm thấy sản phẩm nào cả");
        }
    }

    //lay thong tin review cua 1 ma san pham
    public void readReview(String pID) {
        try {
            String line="";
            String urlapiReview="https://tiki.vn/api/v2/reviews?product_id="+pID;
            Connection.Response response1= Jsoup.connect(urlapiReview)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            JSONObject productReview = new JSONObject(response1.body());
            JSONArray content= productReview.getJSONArray("data");
            for(int i=0;i<content.length();i++) {
                String contents=content.getJSONObject(i).getString("content");
                //System.out.println(contents);
                line=line+contents;
                line+="\n";
            }
//            System.out.println(line);
            sent(line);
        }
        catch (Exception e) {
            System.out.println("khong co review");
            sent("khong co review");
        }
    }

    public void getNames() {
        try {
            Bllproduct bllproduct = new Bllproduct();
            ArrayList<String> productList = bllproduct.getProductsName();
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(productList);
        }
        catch (Exception e) {
            System.out.println("khong co Names");
        }
    }
}