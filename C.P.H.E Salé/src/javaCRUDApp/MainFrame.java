package javaCRUDApp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.IndexBuilder;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import static net.proteanit.sql.DbUtils.resultSetToTableModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author George
 */
public class MainFrame extends javax.swing.JFrame {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String TextArea=null;
	// declaration of instance variables
//	private  String typeString;
  //  private String ExaminateurString;
	//private String typeDocument;

    protected final JTextField[] informationTextField = new JTextField[11];
    private final JLabel[] informationLabel = new JLabel[11];
    private final JButton[] functionButton = new JButton[3];

    private String[] data;
    private final String[] currentText = new String[10];
    private final String[] originalText = new String[10];
    private ResultSet resultSet = null;
    private ResultSetMetaData metaData = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private Database db = null;
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    private String selectedColumnName = null;

    // function Text Field Color
    private final int fn_r = 160;
    private final int fn_b = 160;
    private final int fn_g = 160;

    boolean rsTest;

    private final String localDisk = "C";
    private final String dbFolderName = "Assets";
    private final String databaseName = "Gestion Pneu Database";
    private final String logFolderName = "LogfilesFolder";
    private String columnTitle = null;
    private boolean dbCreated = false;

    Calendar cal = new GregorianCalendar();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DATE);
    String dateToday = day + "-" + (month + 1) + "-" + year;

    int hr = cal.get(Calendar.HOUR);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    String CURRENT_TIME = hr + ":" + (min) + ":" + sec;
    String myTime;
	private String[] textUpdate = new String[10];

	public final Color grey= new Color(204, 204, 204);
    public final String dbDirectory = localDisk + ":/" + dbFolderName;
    public final String dbPath = dbDirectory + "/" + databaseName + ".accdb";
    private final String driver = "jdbc:ucanaccess://";
    public final String logFileDirectory = dbDirectory + "/" + logFolderName;
    public final String logFilePath = logFileDirectory + "/" + dateToday + ".txt";
    public final String[] Directories = { dbDirectory, logFileDirectory };

   // public final String[] tablesToCreate = { "Inventeur", "Visite" };
    public final String[] tablesToCreate = { "Gestion" };
    public final String[] tableElements= { "Nom Visiteur","Ville","Numero Téléphone","Email","Statut" };
            
    public final String[] tableColumns = { "Date,Destination,Entrees,Sortie,Stocks" };
    //tableColumns = { "Nom Visiteur,Ville,Numero Telephone,Email,Date",
    //"nom_inventeur,nombre_de_visite,numero_de_la_demande,date,commentaires,Type,examinateur" };
    private final int[] limit = new int[tablesToCreate.length];
    private int lim = 0;
    private String table = "";
    private String previousTable = "";
    private String Query = null;
    private String Columns = null;
    private int length = 0;
    private int xPos;
    private int yPos;
    private String id;
    private final String[] generalQuery = new String[tablesToCreate.length];
    private final String[] fnButtonTitle = { "Enregistrer", "Modifier", "Supprimer", "Rechercher", "Vider les champs",
            "Ajouter" };
	private JComboBox<String> ComboBox1;
	//private JComboBox<String> ComboBox2;
	//private JComboBox<String> ComboBox3;
	private JLabel SearchLabel=null;
	private Icon icon=new javax.swing.ImageIcon(getClass().getResource("/images/search-icon.gif"));
//	private JButton ButtonComment;
	protected JFrame MF;
	/**
     * Creates new form NewJFrame
     */
    public MainFrame() {
    	getContentPane().setLayout(null);
    	
    
        if (tablesToCreate.length > tableColumns.length) {
            JOptionPane.showMessageDialog(null, "Il existe une Table sans Colonne\nLe programme ne va pas continuer");

        } else if (tablesToCreate.length == tableColumns.length) {
            initComponents();
            dynamicTime();
            RunApp();
            jTextField1.setEnabled(false);
        } else if (tablesToCreate.length < tableColumns.length) {
            JOptionPane.showMessageDialog(null,
                    "Il existe des Colonne/Colonnes qui n'appartienne pas a une table\nLe programme ne va pas continuer");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	
    	new JFrame();

    	//backgroundImage code
    	ImageIcon img_icon = new ImageIcon(getClass().getResource("/images/tire-icon.png"));
    	 Image image_icon = img_icon.getImage();
    	ImageIcon img = new ImageIcon(getClass().getResource("/images/poster-avec-logo.png"));
        Image image = img.getImage();
        //Image temp_image = image.getScaledInstance(1024, 610, Image.SCALE_SMOOTH);
       
        
        //BackgroundImage = new JLabel("",img,JLabel.CENTER);
        
        //backgroundPanel.add(BackgroundImage);
        backgroundPanel = new BackgroundPanel(image, BackgroundPanel.SCALED, 0.0f, 0.0f);
     
    	//backgroundPanel = new JPanel();
        minimizeButton = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        frameDrag = new javax.swing.JLabel();
        txtTime = new javax.swing.JLabel();
        printButton = new javax.swing.JButton();
        EmailButton = new javax.swing.JButton();
        tableScrollPane = new javax.swing.JScrollPane();
        MainTable = new javax.swing.JTable();
        tableChoicePanel = new javax.swing.JPanel();
        TableComboBox = new javax.swing.JComboBox<String>();
        searchPanel = new javax.swing.JPanel();
        SearchTextField = new javax.swing.JTextField();
        searchInfo = new javax.swing.JLabel();
        SearchLabel = new javax.swing.JLabel();
        tableOperationsPanel = new javax.swing.JPanel();
        
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
     //   jTextField5 = new javax.swing.JTextField();
     //   jTextField6 = new javax.swing.JTextField();
     //   jTextField7 = new javax.swing.JTextField();
     //   jTextField8 = new javax.swing.JTextField();
     //   jTextField9 = new javax.swing.JTextField();
     //   jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();

        
        ComboBox1 = new JComboBox<String>();
     //   ComboBox2 = new JComboBox<String>();
     //   ComboBox3 = new JComboBox<String>();

//        ButtonComment = new JButton();
 
        
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
     //   jLabel5 = new javax.swing.JLabel();
     //   jLabel6 = new javax.swing.JLabel();
     //   jLabel7 = new javax.swing.JLabel();
     //   jLabel8 = new javax.swing.JLabel();
     //   jLabel9 = new javax.swing.JLabel();
     //   jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        
        jPanel3 = new javax.swing.JPanel();
        functionButton1 = new javax.swing.JButton();
        functionButton2 = new javax.swing.JButton();
        functionButton3 = new javax.swing.JButton();
        
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("C.P.H.E Salé");
        setIconImage(image_icon);
        setLocation(new java.awt.Point(2, 2));
        setMinimumSize(new java.awt.Dimension(1020, 610));
        setUndecorated(true);
        setSize(new java.awt.Dimension(1024, 610));
        
        

        backgroundPanel.setBackground(new java.awt.Color(51, 51, 51));
        backgroundPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED,
                new java.awt.Color(255, 0, 0), java.awt.Color.red, java.awt.Color.red, new java.awt.Color(255, 0, 0)));
        backgroundPanel.setPreferredSize(new java.awt.Dimension(1024, 556));
        backgroundPanel.setVerifyInputWhenFocusTarget(false);

        minimizeButton.setBackground(new java.awt.Color(0, 0, 0));
        minimizeButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        minimizeButton.setForeground(new java.awt.Color(255, 255, 255));
        minimizeButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizeButton.setText("--");
        minimizeButton.setToolTipText("Réduire");
        minimizeButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        minimizeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        minimizeButton.setOpaque(true);
        minimizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizeButtonMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizeButtonMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizeButtonMouseExited(evt);
            }
        });

        closeButton.setBackground(new java.awt.Color(0, 0, 0));
        closeButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        closeButton.setForeground(new java.awt.Color(255, 255, 255));
        closeButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeButton.setText("X");
        closeButton.setToolTipText("Fermer");
        closeButton.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        closeButton.setOpaque(true);
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButtonMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButtonMouseExited(evt);
            }
        });
        
        

        frameDrag.setBackground(new java.awt.Color(0, 0, 0));
        frameDrag.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 1, true));
        frameDrag.setOpaque(true);
        frameDrag.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                frameDragMouseDragged(evt);
            }
        });
        frameDrag.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                frameDragMousePressed(evt);
            }
        });

        txtTime.setBackground(new java.awt.Color(0, 0, 0));
        txtTime.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTime.setForeground(new java.awt.Color(51, 255, 255));
        txtTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtTime.setToolTipText("Showing Current Time");
        txtTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtTime.setOpaque(true);
        

        printButton.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        printButton.setForeground(new java.awt.Color(204, 0, 0));
        printButton.setText("Imprimer");
        printButton.setOpaque(false);
        printButton.setPreferredSize(new Dimension(456, 7));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        
        EmailButton.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        EmailButton.setForeground(new java.awt.Color(204, 0, 0));
        EmailButton.setText("Email");
        EmailButton.setOpaque(false);
        EmailButton.setPreferredSize(new Dimension(456, 7));
        EmailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailButtonActionPerformed(evt);
            }
        });
        
       

        tableScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 1, 20), new java.awt.Color(0, 0, 153))); // NOI18N

        MainTable.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        MainTable.setOpaque(false);
        MainTable
                .setModel(new javax.swing.table.DefaultTableModel(
                        new Object[][] { { null, null, null, null }, { null, null, null, null },
                                { null, null, null, null }, { null, null, null, null } },
                        new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        MainTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MainTable.setRowHeight(25);
        MainTable.setRowMargin(2);
        MainTable.setSelectionBackground(new java.awt.Color(51, 51, 51));
        MainTable.setSelectionForeground(new java.awt.Color(0, 255, 255));
        MainTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MainTableMouseClicked(evt);
            }
        });
        MainTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                MainTableKeyReleased(evt);
            }
        });
        tableScrollPane.setOpaque(false);
        tableScrollPane.setViewportView(MainTable);
        
        tableScrollPane.getViewport().setOpaque(false);

        tableChoicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Affichage de:",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 1, 20), new java.awt.Color(51, 255, 255))); // NOI18N
        tableChoicePanel.setOpaque(false);

        TableComboBox.setBackground(new java.awt.Color(0, 0, 204));
        TableComboBox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TableComboBox.setForeground(new java.awt.Color(255, 255, 255));
        TableComboBox.setToolTipText("Séléctionner une Table");
        TableComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TableComboBox.setOpaque(false);
        TableComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TableComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableChoicePanelLayout = new javax.swing.GroupLayout(tableChoicePanel);
        tableChoicePanel.setLayout(tableChoicePanelLayout);
        tableChoicePanelLayout.setHorizontalGroup(
                tableChoicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                        TableComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 190, Short.MAX_VALUE));
        tableChoicePanelLayout
                .setVerticalGroup(tableChoicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TableComboBox, javax.swing.GroupLayout.Alignment.TRAILING));

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, null,
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 1, 20), new java.awt.Color(51, 255, 255))); // NOI18N
        searchPanel.setOpaque(false);
        SearchLabel.setIcon(icon);	
        SearchTextField.setBackground(new java.awt.Color(74, 67, 67));
        SearchTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        SearchTextField.setForeground(new java.awt.Color(255, 255, 255));
        SearchTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        SearchTextField.setCaretColor(new java.awt.Color(255, 0, 0));
        SearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchTextFieldKeyReleased(evt);
            }
        });

        searchInfo.setFont(new java.awt.Font("Cambria Math", 1, 12)); // NOI18N
        searchInfo.setForeground(new java.awt.Color(255, 255, 255));
        searchInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(searchPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(searchPanelLayout.createSequentialGroup()
                		.addComponent(SearchLabel)
                		.addComponent(SearchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                		)
                .addComponent(searchInfo, javax.swing.GroupLayout.Alignment.CENTER,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
      
        searchPanelLayout
                .setVerticalGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(searchPanelLayout.createSequentialGroup()
                        		.addGroup(searchPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(SearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                       javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SearchLabel))
                               //
                                .addComponent(searchInfo, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        tableOperationsPanel.setBackground(new java.awt.Color(255, 255, 255));
        tableOperationsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tableOperationsPanel.setOpaque(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "     ",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 1, 20), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(130, 300));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(130, 300));

        ComboBox1.setEditable(true);
        ComboBox1.setEnabled(true);
        ComboBox1.setRenderer(new MyComboBoxRenderer());
        ComboBox1.setEditor(new MyComboBoxEditor());
        

        
        ComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] {"   ","AGUENDICH", "BAMI", "BENCHEKROUN"}));
        ComboBox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ComboBox1.setToolTipText("Séléctionner une destination");
        ComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ComboBox1.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				ComboBox1ActionPerformed(e);
			}
        });
  
  /*     
        ComboBox2.setEditable(false);
        ComboBox2.setEnabled(false);
        ComboBox2.setForeground(grey);
        ComboBox2.setModel(new DefaultComboBoxModel<String>(new String[] {"  ", "PCT", "MA", "Rédaction", "Information", "Réponse RR"}));
        ComboBox2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ComboBox2.setToolTipText("Séléctionner un Objet");
        ComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ComboBox2.addActionListener(new java.awt.event.ActionListener() {


			public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox2ActionPerformed(evt);
            }

			private void ComboBox2ActionPerformed(ActionEvent evt) {
				// TODO Auto-generated method stub
				boolean t=false;
				typeString=ComboBox2.getSelectedItem().toString();
				if(t==true) textField(7);
				
			}
        });
    */
    /*    
        ComboBox3.setEditable(false);
        ComboBox3.setEnabled(false);
        ComboBox3.setForeground(grey);
        ComboBox3.setModel(new DefaultComboBoxModel<String>(new String[] {"  ", "Exemple brevet", "Flyer Dépot"}));
        ComboBox3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ComboBox3.setToolTipText("Séléctionner un Document");
        ComboBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ComboBox3.addActionListener(new java.awt.event.ActionListener() {


			public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBox3ActionPerformed(evt);
            }
			private void ComboBox3ActionPerformed(ActionEvent evt) {
				// TODO Auto-generated method stub
				boolean t=false;
				typeDocument=ComboBox3.getSelectedItem().toString();
				if(t==true) textField(5);
				
			}
        });

        
        ButtonComment.setEnabled(false);
        ButtonComment.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        ButtonComment.setFont(new java.awt.Font("Arial", 1, 12));
        ButtonComment.setText("Voir/Ajouter");
        ButtonComment.setOpaque(false);
        ButtonComment.setPreferredSize(new Dimension(456, 7));
        ButtonComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCommentActionPerformed(evt);
            }
        });
      */  
        jTextField1.setEditable(false);
        jTextField1.setBackground(grey);
        jTextField1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField1.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField1.setEnabled(false);
        jTextField1.setFocusCycleRoot(true);
        jTextField1.setSelectedTextColor(new java.awt.Color(51, 153, 255));
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jTextField2 = (JTextField) ComboBox1.getEditor().getEditorComponent();
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        /*
        jTextField2.setEditable(false);
        jTextField2.setBackground(grey);
        jTextField2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField2.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField2.setEnabled(false);
        jTextField2.setSelectedTextColor(new java.awt.Color(51, 0, 255));
        

		*/
        jTextField3.setEditable(false);
        jTextField3.setBackground(grey);
        jTextField3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField3.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField3.setEnabled(false);
        jTextField3.setSelectedTextColor(new java.awt.Color(51, 0, 255));
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTextField4.setEditable(false);
        jTextField4.setBackground(grey);
        jTextField4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField4.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField4.setEnabled(false);
        jTextField4.setSelectedTextColor(new java.awt.Color(51, 0, 255));
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });
       
        jTextField11.setEditable(false);
        jTextField11.setBackground(grey);
        jTextField11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField11.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField11.setEnabled(false);
        jTextField11.setSelectedTextColor(new java.awt.Color(51, 0, 255));
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTextField1).addComponent(ComboBox1).addComponent(jTextField3).addComponent(jTextField4).addComponent(jTextField11)
        		);
        jPanel1Layout
                .setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(23, 23, 23)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addContainerGap(30, Short.MAX_VALUE)));

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jTextField1, ComboBox1,
                jTextField3, jTextField4, jTextField11});

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 300));
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 300));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
/*
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
 
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    */    
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE,
                        164, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                );
        jPanel2Layout
                .setVerticalGroup(
                        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup().addGap(63, 63, 63)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addContainerGap(30, Short.MAX_VALUE)));
        

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { jLabel1, jLabel2,
                jLabel3, jLabel4, jLabel11});

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "          ",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 1, 20), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 300));
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 300));
        
       
	        

        functionButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        functionButton1.setBackground(new java.awt.Color(74, 67, 67));
        functionButton1.setEnabled(false);
        functionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionButton1ActionPerformed(evt);
            }
        });

        functionButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        functionButton2.setBackground(new java.awt.Color(74, 67, 67));
        functionButton2.setEnabled(false);
        functionButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionButton2ActionPerformed(evt);
            }
        });

        functionButton3.setBackground(new java.awt.Color(74, 67, 67));
        functionButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        functionButton3.setEnabled(false);
        functionButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                        jPanel3Layout.createSequentialGroup().addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(functionButton3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(functionButton2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(functionButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(functionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(functionButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(functionButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout tableOperationsPanelLayout = new javax.swing.GroupLayout(tableOperationsPanel);
        tableOperationsPanel.setLayout(tableOperationsPanelLayout);
        tableOperationsPanelLayout.setHorizontalGroup(tableOperationsPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tableOperationsPanelLayout.createSequentialGroup().addGap(3, 3, 3)
                        .addGroup(tableOperationsPanelLayout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                                .addGroup(tableOperationsPanelLayout.createSequentialGroup()
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 164,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 140,
                                                Short.MAX_VALUE)))));
        tableOperationsPanelLayout.setVerticalGroup(tableOperationsPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tableOperationsPanelLayout.createSequentialGroup().addGap(100, 100, 100)
                        .addGroup(tableOperationsPanelLayout
                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, Short.MAX_VALUE)
                        .addContainerGap()));

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(backgroundPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundPanelLayout.createSequentialGroup()
                        .addComponent(tableOperationsPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        		.addComponent(searchPanel, javax.swing.GroupLayout.Alignment.TRAILING,
                        				javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                        		.addComponent(tableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
                        		.addComponent(EmailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                        backgroundPanelLayout.createSequentialGroup().addGroup(backgroundPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                
                                               // .addComponent(tableChoicePanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                 //       javax.swing.GroupLayout.DEFAULT_SIZE,
                                                   //     javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(printButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                )))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                        backgroundPanelLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(minimizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0).addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(frameDrag, javax.swing.GroupLayout.DEFAULT_SIZE, 1278, Short.MAX_VALUE))
                .addGroup(backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(backgroundPanelLayout
                                .createSequentialGroup().addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        264, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1014, Short.MAX_VALUE))));
        backgroundPanelLayout
                .setVerticalGroup(
                        backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(backgroundPanelLayout.createSequentialGroup().addGroup(backgroundPanelLayout
                                        .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(minimizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(
                                                closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(9, 9, 9)
                                        .addGroup(backgroundPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(backgroundPanelLayout.createSequentialGroup()
                                                		.addComponent(searchPanel,
                                                				javax.swing.GroupLayout.PREFERRED_SIZE,
                                                				javax.swing.GroupLayout.DEFAULT_SIZE,
                                                				javax.swing.GroupLayout.PREFERRED_SIZE)
                                                		.addGap(100, 100, 100)
                                                		.addComponent(tableScrollPane,
                                                				javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                				Short.MAX_VALUE)
                                                		
                                                        .addGroup(backgroundPanelLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                
                                                                
                                                               
                                                        .addGap(18, 18, 18).addGroup(backgroundPanelLayout.createSequentialGroup()
                                                               /*.addComponent(tableChoicePanel,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE) */
                                                        		
                                                        		
                                                                .addComponent(printButton)
                                                                ).addComponent(EmailButton)))
                                                .addComponent(tableOperationsPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())
                                .addGroup(backgroundPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(backgroundPanelLayout.createSequentialGroup()
                                                .addComponent(frameDrag, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 522, Short.MAX_VALUE)))
                                .addGroup(backgroundPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(backgroundPanelLayout.createSequentialGroup()
                                                .addComponent(txtTime, javax.swing.GroupLayout.PREFERRED_SIZE, 25,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 525, Short.MAX_VALUE))));
setContentPane(backgroundPanel);
setVisible(true);
      /*  javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE));
     */
        pack();
    }// </editor-fold>//GEN-END:initComponents

    protected void ButtonCommentActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
  	  
    	
	}

	protected void ComboBox1ActionPerformed(ItemEvent e) {
		// TODO Auto-generated method stub
		if (actionPerformed == true) {
		System.out.println("combobox action performed");
        textFieldColour(1, fn_r, fn_b, fn_g);
        fnBtnAssign(0, fnButtonTitle[0], "save.gif");
        fnBtnAssign(1, fnButtonTitle[4], "clear.gif");
		//		ExaminateurString=ComboBox1.getSelectedItem().toString();
		}
	}

	private void TableComboBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TableComboBoxActionPerformed
	//	changeCombobox();
		adjustTableValues();
        
    }// GEN-LAST:event_TableComboBoxActionPerformed

   /* private void changeCombobox() {
		// TODO Auto-generated method stub
		if(TableComboBox.getSelectedItem().toString().equals("Visite")){
     		 ComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] {"", "DIV", "PCT", "MA"}));
			 ComboBox1.revalidate();
			 ComboBox2.setEnabled(true);
			 ComboBox2.setModel(new DefaultComboBoxModel<String>(new String[] {"", "BAMI", "FERHANE", "KHASSAL",  "OUBIYI",
			 		 "El KINANI", "SADIKI", "TELLAA", "BRINI", "BENCHEKROUN", "LAHCHIMI", "EL KADIRI", "AGUENDICH", "BENZOHRA"}));
			 ComboBox2.revalidate();
			}
		if(TableComboBox.getSelectedItem().toString().equals("Inventeur")){
			ComboBox1.setModel(new DefaultComboBoxModel<String>(new String[] {"Bloqué", "Active", "Abondonné"}));
			ComboBox1.revalidate();
			ComboBox2.setEnabled(true);
			ComboBox2.revalidate();
		}
		}
	*/		
	private void MainTableKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_MainTableKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {
            tableItemToTetField();
        }
    }// GEN-LAST:event_MainTableKeyReleased

    private void MainTableMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_MainTableMouseClicked
        tableItemToTetField();
    }// GEN-LAST:event_MainTableMouseClicked

    private void SearchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_SearchTextFieldKeyReleased
        addRow();
    }// GEN-LAST:event_SearchTextFieldKeyReleased

    private void functionButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_functionButton1ActionPerformed
        fnBtnSetting(0);
    }// GEN-LAST:event_functionButton1ActionPerformed

    private void functionButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_functionButton2ActionPerformed
        fnBtnSetting(1);
    }// GEN-LAST:event_functionButton2ActionPerformed

    private void functionButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_functionButton3ActionPerformed
        fnBtnSetting(2);
    }// GEN-LAST:event_functionButton3ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField1KeyReleased
        textField(0);
    }// GEN-LAST:event_KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField2KeyReleased
        textField(1);
    }// GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField3KeyReleased
        textField(2);
    }// GEN-LAST:event_jTextField3KeyReleased

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField4KeyReleased
        textField(3);
    }// GEN-LAST:event_jTextField4KeyReleased

    
    private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jTextField5KeyReleased
        textField(4);
    }// GEN-LAST:event_jTextField5KeyReleased

    
   
    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printButtonActionPerformed
        printMainTable();
    }// GEN-LAST:event_printButtonActionPerformed
    
    private void EmailButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printButtonActionPerformed
  	  
    	formulaire.NewScreen();
    	// SendEmail();
    	
    }// GEN-LAST:event_printButtonActionPerformed
     

	private void minimizeButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizeButtonMouseClicked
        this.setState(MainFrame.ICONIFIED);
    }// GEN-LAST:event_minimizeButtonMouseClicked

    private void minimizeButtonMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizeButtonMouseEntered
        minimizeButton.setBackground(new java.awt.Color(204, 204, 204));
    }// GEN-LAST:event_minimizeButtonMouseEntered

    private void minimizeButtonMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_minimizeButtonMouseExited
        minimizeButton.setBackground(new java.awt.Color(0, 0, 0));
    }// GEN-LAST:event_minimizeButtonMouseExited

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_closeButtonMouseClicked
        System.exit(0);
    }// GEN-LAST:event_closeButtonMouseClicked

    private void closeButtonMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_closeButtonMouseEntered
        closeButton.setBackground(new java.awt.Color(255, 0, 0));
        closeButton.setForeground(new java.awt.Color(0, 255, 255));
    }// GEN-LAST:event_closeButtonMouseEntered

    private void closeButtonMouseExited(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_closeButtonMouseExited
        closeButton.setBackground(new java.awt.Color(0, 0, 0));
        closeButton.setForeground(new java.awt.Color(255, 255, 255));
    }// GEN-LAST:event_closeButtonMouseExited

    private void frameDragMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_frameDragMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();

        this.setLocation(x - xPos, y - yPos);
    }// GEN-LAST:event_frameDragMouseDragged

    private void frameDragMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_frameDragMousePressed
        xPos = evt.getX();
        yPos = evt.getY();
    }// GEN-LAST:event_frameDragMousePressed

    /**
     * @param args the command line arguments

     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
    	try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>
        // </editor-fold>

        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
             new MainFrame().setVisible(true);
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable MainTable;
    private javax.swing.JTextField SearchTextField;
    private javax.swing.JComboBox<String> TableComboBox;
    private JPanel backgroundPanel;
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel frameDrag;
    private javax.swing.JButton functionButton1;
    private javax.swing.JButton functionButton2;
    private javax.swing.JButton functionButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
	private JLabel jLabel11;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField11;
    
    private javax.swing.JLabel minimizeButton;
    private javax.swing.JButton printButton;
    private javax.swing.JButton EmailButton;
    private javax.swing.JLabel searchInfo;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel tableChoicePanel;
    private javax.swing.JPanel tableOperationsPanel;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JLabel txtTime;
    private boolean actionPerformed = true;
    // End of variables declaration//GEN-END:variables

    public final void RunApp() {
    	
        TableComboBox.addItem(tablesToCreate[0]);
        for (int i = 1; i < tablesToCreate.length; i++) {
            TableComboBox.addItem(tablesToCreate[i]);
            limit[i] = tableColumns[i].split(",").length;
        }
        informationLabel[0] = jLabel1;
        informationLabel[1] = jLabel2;
        informationLabel[2] = jLabel3;
        informationLabel[3] = jLabel4;
        informationLabel[4] = jLabel11;
        
        
        informationTextField[0] = jTextField1;
        //informationTextField[1] = jTextField2;
        informationTextField[1]= jTextField2;
        informationTextField[2] = jTextField3;
        informationTextField[3] = jTextField4;
        informationTextField[4] = jTextField11;
        
        
        jTextField1.setText(myTime1);
        jTextField1.setBackground(new java.awt.Color(204, 255, 204));
       
        functionButton[0] = functionButton1;
        functionButton[1] = functionButton2;
        functionButton[2] = functionButton3;
        Arrays.fill(currentText, "");
        Arrays.fill(originalText, "");
        
        fnBtnAssign(0, fnButtonTitle[0], "save.gif");
        fnBtnDefaultSetting(0);
        fnBtnAssign(1, fnButtonTitle[4], "clear.gif");
        fnBtnAssign(2, fnButtonTitle[5], "add.gif");

        createDirectory(Directories);
        createLogFile(logFilePath, dateToday);
        createDatabase(dbPath);
        for (int i = 0; i < tablesToCreate.length; i++) {
            createTables(dbPath, tablesToCreate[i]);
            createColumns(dbPath, tablesToCreate[i], tableColumns[i]);
        }
        lim = limit[0];
        Columns = tableColumns[0];
        Query = generalQuery[0];
        for (int i = 0; i < limit[0]; i++) {
            labelTextField(i, generalQuery[0]);
        }
        updateMainTable(generalQuery[0]);
        length = MainTable.getRowCount();
    }

    public void createDirectory(String[] Directory) {
        for (String Directory1 : Directory) {
            File theDirectory = new File(Directory1);
            // if the main Dir does not exist, create it
            if (!theDirectory.exists()) {
                System.out.println("creating directory: " + theDirectory.getName());
                boolean result = false;
                try {
                    theDirectory.mkdir();
                    result = true;
                } catch (SecurityException se) {
                	// handle it
                	System.err.println("Directory not created");
                }
                if (result) {
                    System.out.println(Directory1 + " Directory created");
                }
            } else {
                System.out.println(Directory1 + " Directory  exist");
            }
        }
    }

    public void createLogFile(String logFilePath, String Date) {
        try {
            // Create new logFile
            File logFile = new File(logFilePath);
            // If logFile doesn't exists, then create it
            if (!logFile.exists()) {
                logFile.createNewFile();
                fw = new FileWriter(logFile.getAbsoluteFile());
                bw = new BufferedWriter(fw);

                // Write in logFile
                try {
                    bw.write("\tThis log file shows todays events only ");
                    bw.newLine();
                    bw.write("    NOTE: Log files are Generated on a daily basis ");
                    bw.newLine();
                    bw.write("***********  " + Date + "  ***********");
                    bw.newLine();
                    bw.newLine();
                    bw.write("  TIME  \tEVENT");
                    bw.newLine();
                    bw.write("********\t***********");
                    bw.flush();
                    fw.flush();
                } catch (IOException ioe) {
                } finally { // always close the logFile
                    if (bw != null) {
                        try {
                            bw.close();
                            fw.close();
                        } catch (IOException ioe2) {
                            // just ignore it
                        }
                    }
                } // end try/catch/finally
            }
        } catch (IOException e) {
        }
    }

    public void createDatabase(final String dbPath) {
        File theDatabase = new File(dbPath);
        // if the database does not exist, create it
        if (!theDatabase.exists()) {
            writeIntoLog("Creating database: " + theDatabase.getName() + " in " + dbPath);
            boolean result = false;
            try {
                DatabaseBuilder.create(Database.FileFormat.V2010, theDatabase);
                result = true;
            } catch (IOException ex) {
                writeIntoLog(ex.toString());
            }
            if (result) {
                writeIntoLog("Database created successfully");
            }
        } else {
            writeIntoLog("The database " + theDatabase.getName() + " already Exist and will be used");
        }
    }

    public void createTables(String path, String tableName) {
        // Open a connection
        writeIntoLog("Connecting to database " + path);
        try {
            db = DatabaseBuilder.open(new File(path));
            if (db.getTableNames().contains(tableName)) {
                writeIntoLog("The table *" + tableName + " already Exist and will be used");
            } else {
                writeIntoLog("Creating " + tableName + " table...");
                new TableBuilder(tableName).addColumn(new ColumnBuilder("ID", DataType.LONG).setAutoNumber(true))
                        .addIndex(new IndexBuilder(IndexBuilder.PRIMARY_KEY_NAME).addColumns("ID").setPrimaryKey())
                        .toTable(db);
                
                writeIntoLog(tableName + " table created successfully...");
            }

        } catch (IOException ex) {
            writeIntoLog("Exception occured : " + ex.toString());
        }
    }

    public void createColumns(final String path, final String tableName, final String columns) {
    	columns.length();
        String[] columnsTitle = columns.split(",");
        try {
            db = DatabaseBuilder.open(new File(path));
            writeIntoLog("*************** Columns added to " + tableName + " table ***************");
            Table myTable = db.getTable(tableName);
           
            for (String columnsTitle1 : columnsTitle) {
                if (myTable.getColumns().toString().contains(columnsTitle1)) {
                    writeIntoLog("The column *" + columnsTitle1 + " already Exist and will be used");
                } /*else if(columnsTitle1.equals("id_inventeur") || columnsTitle1.equals("id_visite"))
                	{
                	new ColumnBuilder(columnsTitle1).setAutoNumber(true).setType(DataType.LONG).addToTable(myTable);
                	writeIntoLog(columnsTitle1);
                } */else {
                    new ColumnBuilder(columnsTitle1).setType(DataType.MEMO).addToTable(myTable);
                    writeIntoLog(columnsTitle1);
                }
                
                            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            writeIntoLog("Exception: " + ex.getMessage());
        } finally {
            dbCreated = true;
        }
    }

    public void writeIntoLog(String AddToLog) {
        try {
            // APPEND MODE SET HERE
            bw = new BufferedWriter(new FileWriter(logFilePath, true));
            bw.newLine();
            bw.write(CURRENT_TIME + "   " + AddToLog);
            bw.flush();

        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally { // always close the logFile
            if (bw != null) {
                try {
                    bw.close();

                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public ResultSet getResultSet(String Query) {
        try {
            resultSet = DriverManager.getConnection(driver + dbPath).prepareStatement(Query).executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            writeIntoLog(ex.toString());
            resultSet = null;
        }
        return resultSet;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            preparedStatement = DriverManager.getConnection(driver + dbPath).prepareStatement(sql);

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            writeIntoLog(ex.toString());
        }
        return preparedStatement;
    }

    public void getExecutedUpdate(String sql) {
        try {
            DriverManager.getConnection(driver + dbPath).createStatement().executeUpdate(sql);

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            writeIntoLog(ex.toString());
        }
    }

    public Statement getStatement(int constant1, int constant2) {
        try {
            statement = DriverManager.getConnection(driver + dbPath).createStatement(constant1, constant2);

        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            writeIntoLog(ex.toString());
        }
        return statement;
    }

    private void updateMainTable(String DEFAULT_QUERY) {
        if (getResultSet(DEFAULT_QUERY) != null) {
            DefaultTableModel model = (DefaultTableModel) MainTable.getModel();
            String[] rowToAdd = new String[lim];
            String query2 = "SELECT * FROM " + table;
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            MainTable.setModel(resultSetToTableModel(getResultSet(DEFAULT_QUERY)));
            MainTable.getTableHeader().setReorderingAllowed(false);
            try {
                resultSet = getResultSet(query2);
                while (resultSet.next()) {
                    id = (String) resultSet.getString(columnTitle(query2, 0));
                    for (int j = 0; j < lim; j++) {
                        String text = (String) resultSet.getString(columnTitle(DEFAULT_QUERY, j));
                        rowToAdd[j] = text;
                    }
                    model.addRow(rowToAdd);
                    /*
                     * if (!Arrays.asList(identity).contains(id)) { model.addRow(rowToAdd);
                     * identity[k] = id; k++; }
                     */
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            MainTable.setRowSorter(new TableRowSorter<DefaultTableModel>(model));
        } else {
        }
    }

    private void tableItemToTetField() {
        actionPerformed = false;
        Arrays.fill(currentText, "");
        int selectedRow = MainTable.convertRowIndexToModel(MainTable.getSelectedRow());
        int selectedCol = MainTable.convertColumnIndexToModel(MainTable.getSelectedColumn());
        selectedColumnName = MainTable.getColumnName(selectedCol);
        String value = (String) (MainTable.getModel().getValueAt(selectedRow, selectedCol));

        if (value == null) {
            value = (MainTable.getModel().getValueAt(selectedRow, 0)).toString();
            selectedColumnName = MainTable.getColumnName(0);
        }
        String query = "SELECT " + Columns + " FROM " + table + " WHERE " + selectedColumnName + " = '" + value + "'";
        
        
        
        resultSet = getResultSet(query);
        try {
            
            if (resultSet.next()) {
                for (int i = 0; i < lim; i++) {
                    String text = resultSet.getString(columnTitle(Query, i));
                    textUpdate[i] = text;
                    labelTextField(i, Query);
                    fnBtnToDisplay();
                    informationTextField[i].setText(text);
                    informationTextField[i].setEditable(false);
                    informationTextField[i].setEnabled(false);
                    ComboBox1.setEnabled(false);
    /*              ComboBox2.setSelectedItem(informationTextField[8].getText());
                    ComboBox2.setEnabled(false);
                    ComboBox2.revalidate();
                    ComboBox3.setSelectedItem(informationTextField[5].getText());
                    ComboBox3.setEnabled(false);
                    ComboBox3.revalidate();
                    
                    ButtonComment.setEnabled(false);
                    ButtonComment.revalidate();
              */      
                    if (!informationTextField[i].getText().trim().equals("")) {
                        originalText[i] = informationTextField[i].getText().trim();
                        System.out.println("original text de i = "+ i + " : "+ originalText[i]);
                    }
                }
            } 
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        actionPerformed = true;
    }

    private void labelTextField(int fieldToLabel, String Query) {
        String colTitle = columnTitle(Query, fieldToLabel);
        informationLabel[fieldToLabel].setText(colTitle);
        textFieldColour(fieldToLabel, 204, 204, 204);
        informationTextField[fieldToLabel].setEnabled(true);
        informationTextField[fieldToLabel].setEditable(true);
        ComboBox1.setSelectedItem(jTextField2.getText().toString());
        ComboBox1.setEnabled(true);
        ComboBox1.revalidate();
        /*     ComboBox2.setSelectedItem(jTextField8.getText().toString());
        ComboBox1.revalidate();
        ComboBox2.setEnabled(true);
        ComboBox2.revalidate();
        ComboBox3.setSelectedItem(jTextField5.getText().toString());
        ComboBox3.setEnabled(true);
        ComboBox3.revalidate();
        
        ButtonComment.setEnabled(true);
        ButtonComment.revalidate();
   */   
    }

    public String columnTitle(String query, int currentColumn) {
        try {
            statement = getStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(query);
            metaData = rs.getMetaData();
            columnTitle = metaData.getColumnName(currentColumn + 1);
        } catch (SQLException ex) {
            columnTitle = null;
        }
        return columnTitle;
    }

    private void textFieldColour(int field, int r, int b, int g) {
        if(field == 1) {
          ComboBox1.getEditor().getEditorComponent().setBackground(new java.awt.Color(r, b, g));
        }
        informationTextField[field].setBackground(new java.awt.Color(r, b, g));
    }

    private void fnBtnToDisplay() {
        int j = 0;
        String emptyString1 = "";
        String emptyString2 = "";
        for (int i = 0; i < lim; i++) {
            emptyString1 = emptyString1.concat(originalText[i]);
            emptyString2 = emptyString2.concat(currentText[i]);
        }
        if (emptyString1.equals("") && emptyString2.equals("")) {
            for (int k = 0; k < functionButton.length; k++) {
                fnBtnDefaultSetting(k);
            }
        }
        if (emptyString1.equals("") && !emptyString2.equals("")) {
            fnBtnAssign(j, fnButtonTitle[0], "save.gif");// save
            j = j + 1;
            fnBtnAssign(j, fnButtonTitle[4], "clear.gif");// clear
            j = j + 1;
            fnBtnAssign(j, fnButtonTitle[5], "add.gif");// add new
            j= j +1 ;
            fnBtnDefaultSetting(j);
        }
        if (!emptyString1.equals("") && emptyString2.equals("")) {
            fnBtnAssign(j, fnButtonTitle[1], "Edit.gif");// edit
            j = j + 1;
            fnBtnAssign(j, fnButtonTitle[2], "delete.gif");// delete
            j = j + 1;
            fnBtnAssign(j, fnButtonTitle[5], "add.gif");// add new
        }
        if (!emptyString1.equals("") && !emptyString2.equals("")) {
            if (emptyString1.equals(emptyString2)) {
                fnBtnAssign(j, fnButtonTitle[1], "Edit.gif");// edit
                j = j + 1;
                fnBtnAssign(j, fnButtonTitle[2], "delete.gif");// delete
                j = j + 1;
                fnBtnAssign(j, fnButtonTitle[5], "add.gif");// add new
            } else {
                fnBtnAssign(j, fnButtonTitle[0], "save.gif");// save
                j = j + 1;
                fnBtnAssign(j, fnButtonTitle[4], "clear.gif");// clear
                j = j + 1;
                fnBtnAssign(j, fnButtonTitle[5], "add.gif");// add new
                j= j +1 ;
                fnBtnDefaultSetting(j);
            }
        }
    }

    private void fnBtnAssign(int j, String text, String image) {
        functionButton[j].setEnabled(true);
        functionButton[j].setBackground(new java.awt.Color(74, 67, 67));
        functionButton[j].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        functionButton[j].setFont(new java.awt.Font("Tahoma", 0, 12));
        functionButton[j].setText(text);
        functionButton[j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/" + image)));
    }

    private void fnBtnDefaultSetting(int whichButton) {
        functionButton[whichButton].setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
       // functionButton[whichButton].setText(null);
       // functionButton[whichButton].setIcon(null);
        functionButton[whichButton].setEnabled(false);
        functionButton[whichButton].setBackground(new java.awt.Color(74, 67, 67));

    }
    public boolean saveoredit=true;
	private String myTime1;
    private void fnBtnSetting(int button) {
    	
        String action = functionButton[button].getText();
        if (action.equalsIgnoreCase(fnButtonTitle[0])) { // save
        	if(!saveoredit) {DataUpdate();

        	 jTextField1.setText(myTime1);
        	 jTextField1.setEnabled(false);
             jTextField1.setBackground(new java.awt.Color(204, 255, 204));
            }
        	else {saveData();
        	fnBtnDefaultSetting(0);
        	fnBtnDefaultSetting(1);
        	
        	 jTextField1.setText(myTime1);
        	 jTextField1.setEnabled(false);
             jTextField1.setBackground(new java.awt.Color(204, 255, 204));
            }
            saveoredit=true;
        }
        if (action.equalsIgnoreCase(fnButtonTitle[1])) {// edit i=1 for disabled date
            for (int i = 1; i < lim; i++) {	
            	
                informationTextField[i].setEditable(true);
                informationTextField[i].setEnabled(true);
                
            }
            ComboBox1.setEnabled(true);
            ComboBox1.setEditable(true);
            fnBtnDefaultSetting(0);
            saveoredit=false;

        }
        if (action.equalsIgnoreCase(fnButtonTitle[2])) {// delete
            deleteData();
            jTextField1.setText(myTime1);
            jTextField1.setEnabled(false);
            jTextField1.setBackground(new java.awt.Color(204, 255, 204));
            saveoredit = true;
        }
        if (action.equalsIgnoreCase(fnButtonTitle[3])) {// search
            JOptionPane.showMessageDialog(null, fnButtonTitle[3] + " is not yet programed", "WE ARE SORRY",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if (action.equalsIgnoreCase(fnButtonTitle[4])) {// clear
            clearTextField(lim);
            jTextField1.setText(myTime1);
       	 	jTextField1.setEnabled(false);
            saveoredit = true;
        }
        if (action.equalsIgnoreCase(fnButtonTitle[5])) {// new item
            clearTextField(lim);
            jTextField1.setText(myTime1);
       	 	jTextField1.setEnabled(false);
            saveoredit = true;
        }
    }

    public boolean isCorrect() {
        data = new String[lim];
     /*   informationTextField[7].setText(ExaminateurString);
        informationTextField[8].setText(typeString);
        informationTextField[5].setText(typeDocument);
      */  
        for (int i = 0; i < lim; i++) {
            if (!informationTextField[i].getText().trim().equals("")) {
                data[i] = informationTextField[i].getText().trim();
                
                
            } else { 
            	data[i] = "null";
               // return false;
            }
        }
        return true;
    }

    public void clearTextField(int limit) {
        for (int i = 0; i < limit; i++) {
            informationTextField[i].setText(null);
            informationTextField[i].setEditable(true);
            informationTextField[i].setEnabled(true);
            textFieldColour(i, 204, 204, 204);
            System.out.println("i: "+i+" limit: "+limit);
            if (i < functionButton.length) {
                fnBtnDefaultSetting(1);
                fnBtnDefaultSetting(0);
            }
        }
        ComboBox1.setEnabled(true);
    }

    public void DataUpdate() {
        if (isCorrect()) {
            try {
            	String[] Colm = Columns.split(",");
                String fieldData = "";
                for (int i = 0; i < lim; i++) {
                    fieldData = fieldData.concat( Colm[i] + " = " + "'" + data[i] + "'");
                    if (i < lim - 1) {
                        fieldData = fieldData.concat(", ");
                    }
                    System.out.println("Colm:" + Colm[i]);
                    System.out.println("firlddata:" + fieldData);

                }
                
                //String sql = "UPDATE " + table + " SET " + fieldData + " WHERE " + Colm[0] + " = " + "'" + data[0] + "'" ;
                String sql = "UPDATE " + table + " SET " + fieldData + " WHERE " + Colm[0] + " = " + "'" + textUpdate[0] + "'" + "AND " + Colm[1] + " = " + "'" + textUpdate[1] + "'" +"AND " + Colm[2] + " = " + "'" + textUpdate[2] + "'" + "AND " + Colm[3] + " = " + "'" + textUpdate[3] + "'" +"AND " + Colm[4] + " = " + "'" + textUpdate[4] + "'"; ;
                System.out.println("sql:"+sql);
                PreparedStatement ps = getPreparedStatement(sql);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Données modifiées avec succès !");
                updateMainTable(Query);
                clearTextField(lim);
            } catch (SQLException ex) {
                System.err.println(ex.toString());
            }
        } // if there is a missing data, then display Message Dialog
        else {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    
    public void saveData() {
        if (isCorrect()) {
            try {
            	
                String fieldData = "";
                for (int i = 0; i < lim; i++) {
                    fieldData = fieldData.concat("'" + data[i] + "'");
                    if (i < lim - 1) {
                        fieldData = fieldData.concat(", ");
                    }
                }
                String sql = "INSERT INTO " + table + " (" + Columns + ") VALUES (" + fieldData + ")";
                PreparedStatement ps = getPreparedStatement(sql);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Données ajoutées avec succès !");
                updateMainTable(Query);
                clearTextField(lim);
            } catch (SQLException ex) {
                System.err.println(ex.toString());
            }
        } // if there is a missing data, then display Message Dialog
        else {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        fnBtnAssign(1, fnButtonTitle[4], "clear.gif");
        fnBtnAssign(2, fnButtonTitle[5], "add.gif");
    }

    private void textField(int key) {
        textFieldColour(key, fn_r, fn_b, fn_g);
        if (!informationTextField[key].getText().trim().equals("")) {
            currentText[key] = informationTextField[key].getText().trim();
            System.out.println("currentText for i = "+key+" :"+currentText[key]);
            
        }
        fnBtnToDisplay();
    }

    private void deleteData() {
        int i = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer ces données?", fnButtonTitle[2],
                JOptionPane.YES_NO_OPTION);
       
        if (i == 0) {
        	if (isCorrect()) {
                try {
                		 String[] Colm = Columns.split(",");
                		 System.out.println("Colm:" + Colm[0]);
                		 System.out.println(data[0]);
                		String sql = "DELETE " + " FROM " + table + " WHERE "+ Colm[0] + " = " + "'" + data[0] + "'" + "AND " + Colm[1] + " = " + "'" + data[1] + "'" +"AND " + Colm[2] + " = " + "'" + data[2] + "'" + "AND " + Colm[3] + " = " + "'" + data[3] + "'" +"AND " + Colm[4] + " = " + "'" + data[4] + "'" ;
                	
                    PreparedStatement ps = getPreparedStatement(sql);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Données supprimées avec succès !");
                    updateMainTable(Query);
                    clearTextField(lim);
                	 } catch (SQLException ex) {
                         System.err.println(ex.toString());
                     }
        	}
        }
    }
        	   
        	

    private void printMainTable() {
        MessageFormat header = new MessageFormat(table + " as at " + myTime);
        MessageFormat footer = new MessageFormat("Page{0,number,integer}");
        try {
            MainTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException ex) {
            System.err.format("Cannot print %s%n", ex.getMessage());
        }
    }

    private void dynamicTime() {
        Thread clock = new Thread() {
            
			@Override
            public void run() {
                for (;;) {
                    try {
                        Calendar cal = new GregorianCalendar();
                        int yr = cal.get(Calendar.YEAR);
                        int mon = cal.get(Calendar.MONTH) + 1;
                        int dy = cal.get(Calendar.DATE);

                        int hr = cal.get(Calendar.HOUR_OF_DAY);
                        int min = cal.get(Calendar.MINUTE);
                        int sec = cal.get(Calendar.SECOND);

                        String year = Integer.toString(yr);
                        String month = Integer.toString(mon);
                        String day = Integer.toString(dy);

                        String hour = Integer.toString(hr);
                        String minute = Integer.toString(min);
                        String second = Integer.toString(sec);
                        if (mon < 10) {
                            month = "0".concat(month);
                        }
                        if (dy < 10) {
                            day = "0".concat(day);
                        }
                        if (hr < 10) {
                            hour = "0".concat(hour);
                        }
                        if (min < 10) {
                            minute = "0".concat(minute);
                        }
                        if (sec < 10) {
                            second = "0".concat(second);
                        }
                        myTime ="Date "+ day + "-" + (month) + "-" + year + "    Time " + hour + ":" + (minute) + ":"
                                + second;
                        myTime1 = day + "-" + (month) + "-" + year;
                        txtTime.setText(myTime);
                        
                        sleep(1000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        clock.start();
    }

    private void adjustTableValues() {
        previousTable = table;
        table = (String) TableComboBox.getSelectedItem();
        if (!previousTable.equals(table)) {
            for (int i = 0; i < lim; i++) {
                informationLabel[i].setText(null);
                textFieldColour(i, 204, 204, 204);
                informationTextField[i].setText(null);
                informationTextField[i].setEditable(false);
                informationTextField[i].setEnabled(false);
                ComboBox1.setSelectedIndex(0);
                ComboBox1.setEnabled(false);
                ComboBox1.revalidate();
          /*      ComboBox2.setSelectedIndex(0);
                ComboBox2.setEnabled(false);
                ComboBox2.revalidate();
                ComboBox3.setSelectedIndex(0);
                ComboBox3.setEnabled(false);
                ComboBox3.revalidate();
           
                ButtonComment.setEnabled(false);
                ButtonComment.revalidate();
         */     
            }
            for (int j = 0; j < tablesToCreate.length; j++) {
                limit[j] = tableColumns[j].split(",").length;
                generalQuery[j] = "SELECT " + tableColumns[j] + " FROM " + table;
                for (int i = 0; i < limit[j]; i++) {
                    if (dbCreated && table.equals(tablesToCreate[j])) {
                        labelTextField(i, generalQuery[j]);
                        Query = generalQuery[j];
                        Columns = tableColumns[j];
                        lim = limit[j];
                        if (i == 0) {
                            for (int k = 0; k < functionButton.length; k++) {
                                fnBtnDefaultSetting(k);
                            }
                        }
                    }
                }
                if (dbCreated && table.equals(tablesToCreate[j])) {
                    updateMainTable(generalQuery[j]);
                }
            }
        }
    }

    private void addRow() {
        length = MainTable.getRowCount();
        DefaultTableModel model = (DefaultTableModel) MainTable.getModel();
        String[] rowToAdd = new String[lim];
        String input = SearchTextField.getText().trim();
        String search = "%" + input + "%";
        String[] identity = new String[length];
        Arrays.fill(identity, "");
        searchInfo.setText("");
        if (!input.equals("")) {
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            String[] Colm = Columns.split(",");
            int k = 0;
            for (int i = 0; i < lim; i++) {
                try {
                    String query = "SELECT " + Columns + " FROM " + table + " WHERE  " + Colm[i] + " LIKE  '" + search
                            + "'";
                    String query2 = "SELECT * FROM " + table + " WHERE  " + Colm[i] + " LIKE  '" + search + "'";

                    resultSet = getResultSet(query2);
                    while (resultSet.next()) {
                        id = (String) resultSet.getString(columnTitle(query2, 0));
                        for (int j = 0; j < lim; j++) {
                            String text = (String) resultSet.getString(columnTitle(query, j));
                            rowToAdd[j] = text;
                        }
                        if (!Arrays.asList(identity).contains(id)) {
                            model.addRow(rowToAdd);
                            identity[k] = id;
                            k++;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            MainTable.setRowSorter(new TableRowSorter<DefaultTableModel>(model));
            int results = MainTable.getRowCount();
            searchInfo.setText(results + " Résultats Trouvés");
        } else {
            updateMainTable(Query);
        }
    }
    static class Mailer{  
		
	    public static void send(String from,String password,String to,String sub,String msg,String path){  
	          //Get properties object    
	          Properties props = new Properties();    
	          props.put("mail.smtp.host", "smtp.g.com");    
	          props.put("mail.smtp.socketFactory.port", "465");    
	          props.put("mail.smtp.socketFactory.class",    
	                    "javax.net.ssl.SSLSocketFactory");    
	          props.put("mail.smtp.auth", "true");    
	          props.put("mail.smtp.port", "465");    
	          //get Session   
	          Session session = Session.getDefaultInstance(props,    
	           new javax.mail.Authenticator() {    
	           protected PasswordAuthentication getPasswordAuthentication() {    
	           return new PasswordAuthentication(from,password);  
	           }    
	          });    
	          //compose message    
	          try {    
	           MimeMessage message = new MimeMessage(session);    
	           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
	           message.setSubject(sub);    
	           //message.setText(msg); 
	           
	           //Attachement
	           BodyPart messageBodyPart = new MimeBodyPart();

	           // Now set the actual message
	           messageBodyPart.setText(msg);

	           // Create a multipar message
	           Multipart multipart = new MimeMultipart();

	           // Set text message part
	           multipart.addBodyPart(messageBodyPart);

	           // Part two is attachment
	           messageBodyPart = new MimeBodyPart();
	           DataSource source = new FileDataSource(path);
	           messageBodyPart.setDataHandler(new DataHandler(source));
	           messageBodyPart.setFileName(path);
	           multipart.addBodyPart(messageBodyPart);

	           // Send the complete message parts
	           message.setContent(multipart);
	           
	           //send message  
	           Transport.send(message);    
	           System.out.println("message sent successfully"); 
	           
	           
	           
	          } catch (MessagingException e) {throw new RuntimeException(e);}    
	             
	         

	    }  
	}  
}



	

