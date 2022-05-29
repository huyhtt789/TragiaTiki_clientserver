package GUI;

import BLL.AutoSuggestor;
import BLL.ClientTGTiki;
import ENTITY.sanpham;
import com.formdev.flatlaf.FlatLightLaf;
import org.jfree.ui.RefineryUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import static BLL.ClientTGTiki.socket;


public class Home extends JFrame{

    private JPanel JPanel1;
    private JButton exitButton;
    private JTable table1;
    private JTextField textField;
    private JButton searchButton;
    private javax.swing.JScrollPane JScrollPane;
    private ClientTGTiki clientTGTiki;
    private ArrayList<String> keywords = new ArrayList<>();
    private JComboBox comboBox = new JComboBox();

    public Home(){
        //Chay giao dien len
        try {
            UIManager.setLookAndFeel( new FlatLightLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        setTitle("JFrame menu");
        setContentPane(JPanel1);
        setSize(1000,700);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                sysExit();
            }
        });
        setResizable(false);

        setVisible(true);

        //Ket noi voi server
        clientTGTiki = new ClientTGTiki();
        textField.setFocusTraversalKeysEnabled(false);
        try {
            clientTGTiki.getListNames();
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
            try {
                Object object = objectInput.readObject();
                keywords =  (ArrayList<String>) object;
                AutoSuggestor autoSuggestor = new AutoSuggestor(textField, this, keywords, Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f) {
                    @Override
                    public boolean wordTyped(String typedWord) {
                        setDictionary(keywords);
                        return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
                    }
                };
            } catch (ClassNotFoundException e) {
                System.out.println("The title list has not come from the server");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("The socket for reading the object has problem");
            e.printStackTrace();
        }
        //Button
        //search
        // -> khi nhấn tìm kiếm -> gửi lên server search tra tên (tra ra danh sách (mã id và tên))
        // danh sách sẽ được client nhận -> hiện lên table
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textsearach = textField.getText();
                StringTokenizer stringTokenizer = new StringTokenizer(textsearach,"^",true);
                JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
                if(stringTokenizer.countTokens()==1){
                    clientTGTiki.sentSearch(textsearach);
                    int n = clientTGTiki.checkServerSearch();
                    if (n > 0) {
                        ArrayList<sanpham> list = clientTGTiki.getlist(n);
                        setTableSanPham(list);
                    } else {

                        jFrameErrorMess.mess("Search", "Không tìm thấy sản phẩm");
                    }
                }
                else{
                    jFrameErrorMess.mess("Search", "không được để trống hoặc chứa dấu ^");
                }
            }
        });

        //Exit
        // -> khi ấn -> ngắt kết nối server và tắt giao diện và ứng dụng.
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sysExit();
            }
        });

        //Table
        //-> nhấp chọn -> số row của table -> tương ứng với số thứ tự list -> lấy id
        // ->Mở trang chi tiết sp và id gửi cho server
        // -> server gửi lại những thông tin cần thiết -> client bố trí thông tin lên trang chi tiết sp
//        table1.addRowSelectionInterval(new MouseEvent());


//        table1.addPropertyChangeListener(new PropertyChangeListener() {
//        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickTable();
//                super.mouseClicked(e);
            }
        });
    }

    //Exit
    public int sysExit(){
        clientTGTiki.CloseTGTiki();
        setVisible(false);
        System.exit(0);
        return 3;
    }

    //Chỉnh sửa ảnh
    // can chinh image
    public ImageIcon load(URL linkImage, int k, int m) {/*linkImage là tên icon, k kích thước chiều rộng mình muốn,m chiều dài và hàm này trả về giá trị là 1 icon.*/
        try {
            BufferedImage image = ImageIO.read(linkImage);//đọc ảnh dùng BufferedImage
            if(image != null) {
                int x = k;
                int y = m;
                int ix = image.getWidth();
                int iy = image.getHeight();
                int dx = 0, dy = 0;

                if (x / y > ix / iy) {
                    dy = y;
                    dx = dy * ix / iy;
                } else {
                    dx = x;
                    dy = dx * iy / ix;
                }

                return new ImageIcon(image.getScaledInstance(dx, dy,
                        image.SCALE_SMOOTH));
            }
            else return null;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    //Table
    public void setTableSanPham(ArrayList<sanpham> list) {
        DefaultTableModel tableModel = null;
        Vector header = null;
        header = new Vector();

        header.add("Picture");

        header.add("Name");
        header.add("Price now");
        header.add("id");
        header.add("STT");
        tableModel = new DefaultTableModel(header, 0);
        int stt=0;
        for(sanpham sp : list){
            Vector row = new Vector();

            //Ảnh
            try {
                URL url = new URL(sp.getPath());
                if(url!=null){
                    ImageIcon imageIcon = load(url,60,60);
                    JLabel image = new JLabel();
                    if(imageIcon != null) {
                        image.setIcon(imageIcon);
                    }
                    else{
                        image.setText("error");
                    }
                    row.add(0, image);
                }
            }
            catch (IOException e){
                row.add(0, "error");
            }

            //id
            //row.add(1, sp.getId());
            //Tên
            row.add(1, sp.getTen());

            //price
            if(sp.getPrice().equals("0")) row.add(2, "NGƯNG BÁN");
            else row.add(2, sp.getPrice());
            row.add(3,sp.getId());
            stt++;
            row.add(4, stt);
            tableModel.addRow(row);
        }


        table1.setModel(tableModel);
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table1.getColumnModel().getColumn(0).setPreferredWidth(80);
        table1.getColumnModel().getColumn(1).setPreferredWidth(640);
        table1.getColumnModel().getColumn(2).setPreferredWidth(160);
        table1.getColumnModel().getColumn(3).setPreferredWidth(70);
        table1.getColumn("Picture").setCellRenderer(new myTableCellRenderer());
        table1.getColumn("Name").setResizable(false);
        table1.getColumn("Price now").setResizable(false);
        table1.getColumn("id").setResizable(false);
        table1.getColumn("STT").setResizable(false);
        table1.setAutoCreateColumnsFromModel(false);
    }
//    private void tbl1MouseClicked(java.awt.event.MouseEvent evt){
//        clickTable();
//
//    }
    class myTableCellRenderer implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            table1.setRowHeight(60);
            /*
            JLabel label = new JLabel();
            label.setText("false");
            if(value == null) return (Component) label;
            return (Component) value;
             */
            JLabel label = new JLabel();
            if(row>=0 && row<=100) {
                if (column == 0) {
                    label.setText("false");
                    if (value == null) return label;
                    return (Component) value;
                } else {
                    label.setText(value.toString());
                    return label;
                }
            }
            return null;
        }
    }


    //Test
    public void testTable(){

        sanpham sp = new sanpham();
        sp.setId("p123");
        sp.setTen("123");
        sp.setPath("https://salt.tikicdn.com/cache/400x400/ts/product/65/ef/8f/6be7c1cce5b425cbd3d4c6eb8e280dfe.jpg");
        ArrayList<sanpham> list = new ArrayList<sanpham>();
        list.add(sp);

        setTableSanPham(list);
    }

    //lay du lieu tu jtable
    private void clickTable() {
    sanpham sp=new sanpham();
        int Row = table1.getSelectedRow();

        String namesp = (String.valueOf(table1.getValueAt(Row, 1)));
        String id =String.valueOf(table1.getValueAt(Row, 3)) ;
//        System.out.println(id);
        clientTGTiki.sentID(id);
        ArrayList<sanpham> list=new ArrayList<>();
        int n = clientTGTiki.checkServerSearch();
        if(n>0){
          list = clientTGTiki.getlistPrice(n);
        }
        else{
            JFrameErrorMess jFrameErrorMess = new JFrameErrorMess();
            jFrameErrorMess.mess("Detail","Không tìm thấy sản phẩm");
        }
//        for(sanpham List :list){
//            System.out.println(List.getPrice()+"# "+List.getTime());
//        }
        String tableprices = table1.getModel().getValueAt(Row, 2).toString();
        //boolean boolreview = true;
        //if(tableprices.equalsIgnoreCase("NGƯNG BÁN")) boolreview = false;
        chartProduct chart = new chartProduct(
                "bien dong gia TIKI" ,
                "Bien dong gia TIKI theo ngay", namesp, list, id, clientTGTiki);

        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }


    public static void main(String[] args){
        Home home = new Home();
    }

}
