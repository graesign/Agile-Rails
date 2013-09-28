package Gui;

import Database.MySQLConnection;
//import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import Database.MySQLConnection;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

public class RailcabCMS extends JPanel {
    
    MySQLConnection ms = new MySQLConnection();
    DrawGraph DG = new DrawGraph();
    
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    //private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    
    private javax.swing.JComboBox jComboBox1;
    //private javax.swing.JComboBox jComboBox5;
    
    
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    //private javax.swing.JLabel jLabel14;
    //private javax.swing.JLabel jLabel15;
    
    private javax.swing.JPanel jPanel1;
    //private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane ps;
    
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    //private javax.swing.JSeparator jSeparator5;
    
    private javax.swing.JTextArea jTextArea1;
    
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    
    private JTable m_table;
    private TableData m_data;
    
    
    public RailcabCMS() {        
        
        this.initialize();
        
    }
    
    private void initialize()
    {
       
        JFrame frame = new JFrame("Railcab CMS");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("C:/rail.png");
               
        //JComponent panel1 = makeGraphPanel();
        //panel1.setPreferredSize(new Dimension(800, 600));
        //tabbedPane.addTab("Grafieken", icon, panel1);
        //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel4 = makeAdminPanel();
        panel4.setPreferredSize(new Dimension(850, 600));
        tabbedPane.addTab("Administratie paneel", icon, panel4);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel3 = makeTabPanel();
        panel3.setPreferredSize(new Dimension(850, 600));
        tabbedPane.addTab("Reis Statistieken", icon, panel3);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        frame.add(tabbedPane);
        frame.setLocation(250, 100);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    
    protected JComponent makeAdminPanel()
    {   
        String[] accounts = new String[ms.getUserAmount()]; 
        accounts = ms.fetchUsers();
        
        jPanel1 = new javax.swing.JPanel();
        
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        
        jComboBox1 = new javax.swing.JComboBox();
        
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        
        jLabel1  = new javax.swing.JLabel();
        jLabel2  = new javax.swing.JLabel();
        jLabel3  = new javax.swing.JLabel();
        jLabel4  = new javax.swing.JLabel();
        jLabel5  = new javax.swing.JLabel();
        jLabel6  = new javax.swing.JLabel();
        jLabel7  = new javax.swing.JLabel();
        jLabel8  = new javax.swing.JLabel();
        jLabel9  = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        
        jPanel1.setLayout(null);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(0, 350, 850, 20);
        jPanel1.add(jTextField6);
        jTextField6.setBounds(530, 230, 170, 20);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(accounts));
        jPanel1.add(jComboBox1);
        jComboBox1.setBounds(0, 50, 140, 20);

        jButton1.setText("Verwijderen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(720, 50, 120, 20);

        jLabel1.setText("Account Toevoegen:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 90, 140, 14);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(0, 80, 850, 20);

        jLabel2.setText("Account Verwijderen:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(0, 30, 140, 14);

        jLabel3.setText("Naam:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(0, 120, 44, 14);
        jPanel1.add(jTextField1);
        jTextField1.setBounds(0, 140, 150, 20);
        jPanel1.add(jTextField2);
        jTextField2.setBounds(170, 140, 160, 20);

        jLabel4.setText("Wachtwoord:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(170, 120, 80, 14);

        jButton2.setText("Aanmaken");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(720, 140, 120, 20);

        jLabel5.setText("Account Aanpassen:");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(0, 180, 130, 14);

        jLabel6.setText("Naam:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(0, 210, 40, 14);

        jLabel7.setText("Wachtwoord:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(170, 210, 90, 14);

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField3);
        jTextField3.setBounds(0, 230, 150, 20);
        jPanel1.add(jTextField4);
        jTextField4.setBounds(350, 230, 160, 20);

        jLabel8.setText("Nieuw Wachtwoord:");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(530, 210, 120, 14);
        jPanel1.add(jTextField5);
        jTextField5.setBounds(170, 230, 160, 20);

        jLabel9.setText("Nieuwe Naam:");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(350, 210, 120, 14);

        jButton3.setText("Update");
        jPanel1.add(jButton3);
        jButton3.setBounds(720, 227, 120, 23);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(0, 170, 850, 20);

        jLabel10.setText("Errorlog legen:");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(0, 280, 100, 14);

        jButton4.setText("legen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(0, 310, 100, 20);
        jPanel1.add(jSeparator4);
        jSeparator4.setBounds(0, 270, 850, 20);

        jLabel11.setText("Error Log:");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(0, 360, 60, 14);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText(ms.getErrorLog());
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 380, 830, 210);
        
        return jPanel1;
    }
    
    /*protected JComponent makeGraphPanel()
    {
        jPanel2 = new javax.swing.JPanel();

	jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        
	jComboBox5 = new javax.swing.JComboBox();
        
	jSeparator5 = new javax.swing.JSeparator();
        
	jButton5 = new javax.swing.JButton();
	
        jPanel2.setLayout(null);

        jPanel2.add(jSeparator5);
        jSeparator5.setBounds(0, 100, 850, 10);

        jButton5.setText("Genereer Grafiek");
        jPanel2.add(jButton5);
        jButton5.setBounds(670, 60, 150, 20);
        
        String[] names = new String[ms.getTrains()];
        names = ms.fetchTrainids();
        
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel14.setText("Object naam:");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(10, 20, 80, 14);
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(names));
        jPanel2.add(jComboBox5);
        jComboBox5.setBounds(10, 60, 150, 20);
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 110, 830, 480);
        jLabel15.setBackground(Color.WHITE);
        jLabel15.setOpaque(true);

        
        return jPanel2;
    }*/
    
    protected JComponent makeTabPanel()
    {
    jButton6 = new javax.swing.JButton();    
    jPanel3 = new javax.swing.JPanel();
    jPanel3.setLayout(null);
    m_data = new TableData();
    m_table = new JTable();
    m_table.setModel(m_data); 
    m_table.setAutoCreateColumnsFromModel( true );    
    
    JTableHeader header = m_table.getTableHeader();
    header.setUpdateTableInRealTime(true);
    ps = new javax.swing.JScrollPane();
    
    ps.setBounds(30, 30, 800, 500);
    ps.setViewportView(m_table);
    jButton6.setBounds(350, 550, 150, 25);
    jButton6.setVisible(true);
    jButton6.setText("Ververs");
    jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
    jPanel3.add(jButton6);
    jPanel3.add(ps);
    
    
    return jPanel3;
    }
    
    
    
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        String imgURL = "C:/rail.png";
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println(ms.getUserAmount());
        if(ms.getUserAmount() == 1){}
        
        else{
        ms.deleteUser(this.jComboBox1.getSelectedItem().toString());
        
        String[] accounts = new String[ms.getUserAmount()];
        accounts = ms.fetchUsers();
        
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(accounts));
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
    if(ms.getUserAmount()<15)
    {
    ms.CreateUser(jTextField1.getText(),jTextField2.getText());
    String[] accounts = new String[ms.getUserAmount()];
    accounts = ms.fetchUsers();
    
    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(accounts));
    jTextField1.setText("");
    jTextField2.setText("");
    }
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    ms.UpdateUser(jTextField3.getText(), jTextField4.getText(), jTextField5.getText(), jTextField6.getText());
    String[] accounts = new String[ms.getUserAmount()];
    accounts = ms.fetchUsers();
    jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(accounts));
    jTextField3.setText("");
    jTextField4.setText("");
    jTextField5.setText("");
    jTextField6.setText("");
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
    ms.ClearTable("errorlog");
    jTextArea1.setText(ms.getErrorLog());
    jScrollPane1.setViewportView(jTextArea1);
    }
    
    /*private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
    ImageIcon img = new ImageIcon();
    img.setImage(DG.createGraph(this.jComboBox5.getSelectedItem().toString()));
    jLabel15.setIcon(img);
    }*/
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
    m_table = new JTable();
    m_data = new TableData();
    m_table.setModel(m_data); 
    ps.setViewportView(m_table);
    }
    
}

class StockData
{
  public long t_stamp;
  public int s_station;
  public int e_station;
  public int passengers;
  public int m_passengers;

  public StockData(long stamp, int start, int end, int passenger,int moved) {
    t_stamp = stamp;
    s_station = start;
    e_station = end;
    passengers = passenger;
    m_passengers = moved;
  }
}

class ColumnData
{
  public String  m_title;
  public int     m_alignment;

  public ColumnData( String title, int alignment) 
  {
    m_title = title;
    m_alignment = alignment;
  }
}

class TableData extends AbstractTableModel 
{
  private MySQLConnection ms = new MySQLConnection();
  static final public 
    ColumnData m_columns[] = { new ColumnData( "TimeStamp", JLabel.LEFT ),
                               new ColumnData( "StartStation", JLabel.LEFT ),
                               new ColumnData( "EndStation", JLabel.RIGHT ),
                               new ColumnData( "Passengers", JLabel.RIGHT ),
                               new ColumnData( "PassengersMoved", JLabel.RIGHT )
                             };
  
  protected long[] TimeStamp    =  new long[ms.getSize2()];
  protected int [] Start        =  new  int[ms.getSize2()];
  protected int [] End          =  new  int[ms.getSize2()];
  protected int [] passengers   =  new  int[ms.getSize2()];
  protected int [] m_passengers =  new  int[ms.getSize2()];
  
  protected Vector m_vector;

  public TableData() {
    
    this.TimeStamp    = ms.getLongData       ("timestamp");
    this.Start        = ms.getIntData     ("startStation");
    this.End          = ms.getIntData       ("endStation");
    this.passengers   = ms.getIntData       ("passengers");
    this.m_passengers = ms.getIntData  ("passengersMoved");
    
    m_vector = new Vector();
    setDefaultData();
  }

  public void setDefaultData() {
    
    m_vector.removeAllElements();
    
    for(int i=0; i<ms.getSize2(); i++)
    {
    m_vector.addElement(new StockData( this.TimeStamp[i], this.Start[i], this.End[i], this.passengers[i], this.m_passengers[i]));
    }
  }

  public int getRowCount() {
    return m_vector==null ? 0 : m_vector.size(); 
  }

  public int getColumnCount() { 
    return m_columns.length; 
  } 

  public String getColumnName(int column) { 
    return m_columns[column].m_title; 
  }
 
  public boolean isCellEditable(int nRow, int nCol) {
  return false;
  }

  public Object getValueAt(int nRow, int nCol) 
  {
    if (nRow < 0 || nRow >= getRowCount())return "";
    StockData row = (StockData)m_vector.elementAt(nRow);
    switch (nCol) 
    {   
      case 0: return row.t_stamp;
      case 1: return row.s_station;
      case 2: return row.e_station;
      case 3: return row.passengers;
      case 4: return row.m_passengers;
    }
    return "";
  }
}