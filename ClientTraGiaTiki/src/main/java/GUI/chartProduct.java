package GUI;

import javax.swing.*;

import BLL.ClientTGTiki;
import ENTITY.sanpham;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class chartProduct extends JFrame {
    private JPanel Panel2;
    private JScrollPane JScroll1;
    private ClientTGTiki clientTGTiki;
    public chartProduct( String applicationTitle, String chartTitle, String namesp, ArrayList<sanpham> list, String id, ClientTGTiki clientTGTiki) {
        super(applicationTitle);

        this.clientTGTiki = clientTGTiki;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        JPanel panel=new JPanel();
        panel.setPreferredSize( new java.awt.Dimension( 1000 , 700 ) );

        //Lấy max
        Collections.sort(list, (sp1, sp2) -> sp1.getPrice().compareTo(sp2.getPrice()));
        String pricemax = list.get(list.size() - 1).getPrice();
        String pricemin = list.get(0).getPrice();
        //System.out.println("max = "+pricemax);
        Double max = Double.valueOf(1000000);
        Double min = Double.valueOf(100);
        try{
            max=Double.parseDouble(pricemax);
            min=Double.parseDouble(pricemin);
        }
        catch(NumberFormatException e){
            System.out.println("Lỗi: "+e.getMessage());
        }
        //System.out.println("max = "+max);
        Collections.sort(list, Comparator.comparing(sanpham::getTime));

        BoxLayout bl=new BoxLayout(panel,BoxLayout.X_AXIS);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Ngay","Gia",
                createDataset(namesp,list),
                PlotOrientation.VERTICAL,
                true,true,false);

        lineChart.getPlot().setBackgroundPaint(Color.white);
        lineChart.getCategoryPlot().setRangeGridlinePaint(Color.black);
        lineChart.getCategoryPlot().setDomainGridlinesVisible(true);
        lineChart.getCategoryPlot().setDomainGridlineStroke(new BasicStroke(1.0f));
        lineChart.getCategoryPlot().getRenderer().setSeriesPaint(0,new Color(26,148,255));
        lineChart.getCategoryPlot().getRenderer().setDefaultStroke(new BasicStroke(3.0f));
        lineChart.getCategoryPlot().getRenderer().setSeriesStroke(0,new BasicStroke(3.0f));

        final CategoryPlot plot = lineChart.getCategoryPlot();
        plot.getRangeAxis().setRange(0, max+min);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000, 400 ) );
        setContentPane(chartPanel);



//        setLayout(new GroupLayout(Panel2));
        // create JLabel




        JButton btn_review=new JButton("Xem Review");
        TextArea ta=new TextArea();
        ta.setPreferredSize(new java.awt.Dimension( 1000 , 400 ));
//        ta.setVisible(false);
        btn_review.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ta.setVisible(true);
                //ClientTGTiki clientTGTiki= new ClientTGTiki();
                clientTGTiki.sentidReview(id);
                String t=clientTGTiki.receive();
                String line="";
                for (int i=0;i<t.length();i++){
                    if(t.charAt(i)=='.')
                    {
                        line=line+t.charAt(i);
                        line+="\n";
                    }else line=line+t.charAt(i);
                }
                ta.setText(line);
                ta.setEditable(false);
                btn_review.setVisible(false);
            }
        });

        //if(!boolreview) btn_review.setVisible(false);

//        textField.setText();
        JButton button=new JButton();
        button.setText("Quay lai");
        button.setSize(20,20);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        panel.add(btn_review);
        panel.add(button);
       panel.add(chartPanel);
//        add(btn_review);
//        add(textField);
        panel.add(ta);
//        add(button);
//       setContentPane(Panel2);
        setContentPane( panel );
//        setContentPane(panel);
//      setLocationRelativeTo(null);
//        setVisible(true);

    }

    private DefaultCategoryDataset createDataset(String namesp,ArrayList<sanpham>list ) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );

        for(sanpham List :list){
            dataset.addValue( Integer.parseInt(List.getPrice()) , namesp , List.getTime() );
        }
        return dataset;
    }

//    public static void main( String[ ] args ) {
//        chartProduct chart = new chartProduct(
//                "bien dong gia TIKI" ,
//                "Bien dong gia TIKI theo ngay",namesp,list,id);
//chart.
//        chart.pack( );
//        RefineryUtilities.centerFrameOnScreen( chart );
//        chart.setVisible( true );
//    }
}
