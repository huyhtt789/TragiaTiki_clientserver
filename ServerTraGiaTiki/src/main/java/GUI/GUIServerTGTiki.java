package GUI;

import BLL.ServerTGTiki;
import DAL.ConnectDatabase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIServerTGTiki extends JFrame{
    private JButton closeButton;
    private JButton openButton;
    private JPanel jPanel1;
    private JLabel TTJLabel;
    private ServerTGTiki serverTGTiki = null;

    public GUIServerTGTiki(){
        serverTGTiki = null;
        final Thread[] thread = {null};

        setContentPane(jPanel1);
        setSize(500,250);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                TTJLabel.setText("Close");
                if (thread[0] != null) {
                    serverTGTiki.close();
                    try {
                        thread[0].join();
                        thread[0].interrupt();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TTJLabel.setText("Close");

                if (thread[0] != null) {
                    serverTGTiki.close();
                    try {
                        thread[0].join();
                        thread[0].interrupt();
                        thread[0] = null;
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                //System.exit(0);
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Kiểm tra kết nối database
                ConnectDatabase connectDatabase = new ConnectDatabase();
                connectDatabase.ConnectionDB();

                //Nếu kết nối được thì chạy ở dưới đây -> ko là sẽ tự động tắt.
                TTJLabel.setText("Open");
                serverTGTiki = new ServerTGTiki();
                serverTGTiki.setRunningTrue();
                thread[0] = new Thread(serverTGTiki);
                thread[0].start();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args){
        GUIServerTGTiki guiServerTGTiki = new GUIServerTGTiki();
    }

}
