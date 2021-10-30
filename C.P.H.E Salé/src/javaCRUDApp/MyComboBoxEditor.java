package javaCRUDApp;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class MyComboBoxEditor extends BasicComboBoxEditor {
	private JTextField jTextField = new JTextField();
	private Object selectedItem;
	public MyComboBoxEditor() {
	
		jTextField.setEditable(true);
        jTextField.setBackground( new Color(204, 204, 204));
        jTextField.setOpaque(true);
        jTextField.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField.setCaretColor(new java.awt.Color(255, 0, 0));
        jTextField.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jTextField.setEnabled(true);
        jTextField.setSelectedTextColor(new java.awt.Color(51, 0, 255));
		
	}

	public JTextField getEditorComponent() {
		return this.jTextField;
	}
	public void setBackGroundColor (int r, int b, int g) {
		this.jTextField.setBackground(new Color(r,b,g));
	}

	public Object getItem() {
	return this.selectedItem.toString();
	}
	
	public void setItem(Object item) {
		if (item == null) {
            return;
		}
		this.selectedItem = item;
		jTextField.setText(item.toString());
	}
}