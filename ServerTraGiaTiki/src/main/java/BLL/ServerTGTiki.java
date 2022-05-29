package BLL;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTGTiki implements Runnable{
    private static int port = 60000;
    private static int numThread = 2;
    private static ServerSocket serverSocket = null;
    private static ExecutorService executorService = null;
    private static BufferedReader reader = null;
    private static KhoaRSA rsa = null;
    private static boolean running = true;

    public ServerTGTiki(){
        rsa = new KhoaRSA();
        rsa.initFromStrings();
        executorService = Executors.newFixedThreadPool(numThread);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Bll -> ServerTGTiki -> ServerTGTiki -> "+e.getMessage());
            //e.printStackTrace();
        }
    }

    public void ServerTGTikiConnect(){
        System.out.println("Server binding at port "+port);
        System.out.println("Waiting for client...");
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String keyMaHoa = reader.readLine();
                String key = rsa.decrypt(keyMaHoa);
                if(key.equalsIgnoreCase("ServerClose")) break;
                executorService.execute(new WorkTGTiki(socket, key));
            }
            executorService.shutdown();
            reader.close();
            serverSocket.close();
            System.out.println("Server Close");
        }
        catch (IOException e) {
            System.out.println("Bll -> ServerTGTiki -> ServerTGTikiConnect -> "+e.getMessage());
            //e.printStackTrace();
        }
    }

    public void ServerTGTikiClose(){
        try {
            Socket socket = new Socket("127.0.0.1", port);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Client connected");
            String key="ServerClose";
            String keyMaHoa = rsa.encrypt(key);
            writer.write(keyMaHoa + "\n");
            writer.flush();
        }
        catch (IOException e){
            System.out.println("Bll -> ServerTGTiki -> ServerTGTikiClose -> "+e.getMessage());
        }
    }

    public void setRunningTrue(){
        running = true;
    }

    public void close(){
        running = false;
        ServerTGTikiClose();
    }

    public void run(){
        while (running) {
            ServerTGTikiConnect();
        }
        System.out.println("Close");
        //running = true;
    }

/*
    public static void main(String[] args){
        ServerTGTiki serverTGTiki = new ServerTGTiki();
        serverTGTiki.ServerTGTikiConnect();
    }
*/
}
