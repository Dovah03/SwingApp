package javaCRUDApp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import javaCRUDApp.MainFrame.Mailer;
import javaCRUDApp.MainFrame;

import java.awt.Color;



public class formulaire extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	protected static final String password ="";//ici mdp du compte

	
	
	JFrame Secondframe;


	private JTextArea textField;
	protected JTextField textField_1;
	protected JTextField textField_2;
	
	private JTextField PathField;
	
	public JFileChooser fc = new JFileChooser();
	
	private String destination;

	private String subject;

	private String emailbody;

	private String path;

	//private JScrollPane JScrollPane;



	
	protected static final String Email="maguendich@gmail.com";//C'est ici ou en change lemail du Compte

	/**
	 * Launch the application.
	 */
	public static void NewScreen() {
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
				java.awt.EventQueue.invokeLater(() -> {
					new formulaire().setVisible(true);
					System.out.println("nimbus");

				});
	}

	/**
	 * Create the application.
	 */
	public formulaire() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		new JFrame();
		setTitle("Formulaire Email");
		setType(Type.NORMAL);
		setBounds(100, 100, 450, 400);
		
		getContentPane().setLayout(null);
		
		
		textField = new JTextArea();
		
		
		textField.setEnabled(true);
		textField.setBounds(150, 148, 231, 100);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setLineWrap(true);
		
		
		textField_1 = new JTextField();
		textField_1.setBounds(150, 94, 231, 30);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(150, 42, 231, 30);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(150, 148, 231, 100);

		
		scroll.setOpaque(false);
        scroll.setViewportView(textField);
        
        scroll.getViewport().setOpaque(false);
		getContentPane().add(scroll);

		
		
		PathField = new JTextField();
		PathField.setBounds(150, 269, 231, 30);
		//PathField.setBounds(150, 300, 231, 30);
		PathField.setEditable(false);
		PathField.setEnabled(false);
		PathField.setColumns(10);
		getContentPane().add(PathField);

		
		
		JLabel lblNewLabel = new JLabel("Email Destination");
		lblNewLabel.setBounds(22, 47, 120, 14);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Subject");
		lblNewLabel_1.setBounds(22, 99, 77, 14);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Email Body");
		lblNewLabel_2.setBounds(22, 175, 77, 14);
		getContentPane().add(lblNewLabel_2);
		
		JButton btnNewButton_1 = new JButton("Envoyer");
		btnNewButton_1.setEnabled(true);
		btnNewButton_1.setBounds(170, 320, 89, 23);
		//btnNewButton_1.setBounds(170, 269, 89, 23);
		getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				boolean test=false;
				
				destination = textField_2.getText();
				subject = textField_1.getText();
				System.out.println("destination : " +destination);
				emailbody = textField.getText();
				
				try {
				
					send();
					test = true;
				} catch (Exception e2) {JOptionPane.showMessageDialog(null, "Email n'as pas pu être envoyer, veuiller verifier votre connexion internet ou la validité des champs", "Error",
	                      JOptionPane.ERROR_MESSAGE);}
				
					// TODO: handle exception
				
				if(test==true) {JOptionPane.showMessageDialog(null, "Email envoyer avec succés", "Email Sent",
	                      JOptionPane.INFORMATION_MESSAGE);}
				
				
			}});
		
		JButton Attachement = new JButton("Pièce jointe");
		Attachement.setEnabled(true);
		Attachement.setBackground(Color.DARK_GRAY);
		Attachement.setBounds(22, 273, 110, 23);
		getContentPane().add(Attachement);
		Attachement.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				 FileFilter java = new FileFilter() {
					
					@Override
					public String getDescription() {
						String description = "pdf files";
						// TODO Auto-generated method stub
						return description ;
					}
					
					@Override
					public boolean accept(File f) {
						// TODO Auto-generated method stub
						if (f.getName().endsWith(".pdf")) {
			                return true;
			             }
			             return false;
					}
				};
				
				 FileFilter classes = null ;
				 FileFilter jar = null ;
				   
				 JFileChooser chooser = new JFileChooser("."); 
				 chooser.addChoosableFileFilter(java); 
				 chooser.setFileFilter(java);
				 chooser.addChoosableFileFilter(classes); 
				 chooser.addChoosableFileFilter(jar); 
				 chooser.showOpenDialog(null);
				 if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					 
				Attachement.setBackground(Color.GREEN);

				path=chooser.getSelectedFile().getAbsolutePath();
				PathField.setText(path);
				PathField.setEditable(false);
				PathField.setEnabled(true);
				}
			}
		});
		

}
		public void send() {
			 System.out.println("destination : "+destination+" Subject : "+subject+" emailbody : "+emailbody+"Path : "+path);

			

			// TODO Auto-generated method stub
			 Mailer.send(Email,password,destination,subject,emailbody,path); 
    	     //change from, password and to  
		}}
