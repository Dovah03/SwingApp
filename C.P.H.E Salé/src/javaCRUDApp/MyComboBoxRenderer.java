package javaCRUDApp;

 
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
 
public class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
 
    public MyComboBoxRenderer() {
        setOpaque(true);
        setFont(new java.awt.Font("Cambria", 0, 14));
    //    setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
        setBackground(new java.awt.Color(51, 51, 51));
        setForeground(new java.awt.Color(0, 255, 255));
    }
     
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        return this;
    }
 
}