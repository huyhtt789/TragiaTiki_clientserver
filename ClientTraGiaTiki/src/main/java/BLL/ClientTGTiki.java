package BLL;

import ENTITY.sanpham;
import GUI.JFrameErrorMess;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class ClientTGTiki {
    public static Socket socket = null;
    public static int port = 60000;
    public static String host = "127.0.0.1";
    private static KhoaAES khoaAES = null;
    private static BufferedReader reader = null;
    private static BufferedWriter writer = null;

    public ClientTGTiki(){
        try {
            socket = new Socket(host, port);
            System.out.println("Client connected");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            khoaAES = new KhoaAES();
            KhoaRSA khoaRSA = new KhoaRSA();
            khoaRSA.initFromStrings();
            String key = khoaAES.getStringKey();
            String keyMaHoa = khoaRSA.encrypt(key);
            writer.write(keyMaHoa+"\n");
            writer.flush();
        }
        catch (IOException e){
            System.out.println("Bll -> ClientTGTiki -> ClientTGTiki -> "+e.getMessage());
            JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            jFrameErrorMess.mess("Server","Close -> disconnect server");
            System.exit(0);
        }
    }

    public void CloseTGTiki(){
        try {
            sent("byeservertiki");
            System.out.println("Client socket closed");
            reader.close();
            writer.close();
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Bll -> ClientTGTiki -> CloseTGTiki -> "+e.getMessage());
            //e.printStackTrace();
        }
    }

    public void sent(String line){
        try {
            String lineMaHoa = khoaAES.encrypt(line);
            writer.write(lineMaHoa + "\n");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            jFrameErrorMess.mess("Server", "Không tìm thấy socket liên kết server nữa");
            System.exit(0);
        }
    }

    public String receive(){
        try{
            String lineMaHoa = reader.readLine();
            String line = khoaAES.decrypt(lineMaHoa);
//            System.out.println(line);
            return line;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi");
            return "false";
        }
    }
    //Search()
        //Sent
    public void sentSearch(String namesp){
        String line = "search^"+namesp.trim();
        sent(line);
    }

    //Send id draw chart
    public void sentID(String idsp){
        String line = "detail^"+idsp;
        sent(line);
    }
    public void sentidReview(String idsp){
        String line = "review^"+idsp;
        sent(line);
    }

    public void getListNames()
    {
        String line = "name^1";
        sent(line);
    }



        //Recevice
    public int checkServerSearch(){
        String line = receive();
        if(line.equalsIgnoreCase("Không tìm thấy sản phẩm nào cả")){
            System.out.println("Không tìm thấy sản phẩm nào cả");
            return 0;
        }
        try{
            int n = Integer.parseInt(line);
            return n;
        }
        catch (NumberFormatException e){
            System.out.println("Lỗi");
            return 0;
        }
    }

    /*
    public ArrayList<String> getnamelist(int n){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<n; i++){
            list.add(receive());
        }
        return list;
    }
     */

    public ArrayList<sanpham> getlist(int n){
        ArrayList<sanpham> list = new ArrayList<sanpham>();
        for(int i=0; i<n; i++){
            String line = receive();
            StringTokenizer stringTokenizer = new StringTokenizer(line, "^");
            sanpham sp = new sanpham();
            sp.setId(stringTokenizer.nextToken());
            sp.setTen(stringTokenizer.nextToken());
            sp.setPath(stringTokenizer.nextToken());
            sp.setPrice(stringTokenizer.nextToken());
            list.add(sp);
        }
        return list;
    }

    public ArrayList<sanpham> getlistPrice(int n){
        ArrayList<sanpham> list = new ArrayList<sanpham>();
        int soluong = 4;
        for(int i=0; i<n; i++){
            String line = receive();
            StringTokenizer stringTokenizer = new StringTokenizer(line, "^");
            sanpham sp = new sanpham();
            sp.setTime(stringTokenizer.nextToken());
            sp.setPrice(stringTokenizer.nextToken());
            String priceTruoc = "";
            if(list.size()==0 || i<3){
                list.add(sp);
            }
            if(list.size()>0){
                String price = list.get(list.size()-1).getPrice();
                if(!price.equals(sp.getPrice()) && soluong>0){
                    list.add(sp);
                    soluong--;
                }
            }
        }
        Collections.sort(list, Comparator.comparing(sanpham::getTime));
        return list;
    }

}
