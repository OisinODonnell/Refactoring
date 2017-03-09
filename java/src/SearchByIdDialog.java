/*
 * 
 * This is the dialog for Employee search by ID
 * 
 * */

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class SearchByIdDialog extends Dialog implements ActionListener {

	ApplicationView parent;
	JButton search, cancel;
	JTextField searchField;

	public SearchByIdDialog(ApplicationView parent) {
		setTitle("Search by Id");
		setModal(true);
		this.parent = parent;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(createSearchPanel());
		setContentPane(scrollPane);

		getRootPane().setDefaultButton(search);
		
		setSize(SEARCH_WIDTH, SEARCH_HEIGHT);
		setLocation(X_POS, Y_POS);
		setVisible(true);
	}
	

	public Container createSearchPanel() {
		JPanel searchPanel = new JPanel(new GridLayout(3, 1));
		JPanel textPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel searchLabel;

		searchPanel.add(new JLabel("Search by ID"));

		textPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		textPanel.add(searchLabel = new JLabel("Enter ID:"));
		searchLabel.setFont(FONT1);
		textPanel.add(searchField = new JTextField(COLUMNS));
		searchField.setFont(FONT1);
		searchField.setDocument(new JTextFieldLimit(COLUMNS));
		
		buttonPanel.add(search = new JButton("Search"));
		search.addActionListener(this);
		search.requestFocus();
		
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);

		searchPanel.add(textPanel);
		searchPanel.add(buttonPanel);

		return searchPanel;
	}


	public void actionPerformed(ActionEvent e) {
		// if option search, search for Employee
		if (e.getSource() == search) {
			// try get correct valus from text field
			try {
				Double.parseDouble(searchField.getText());
				this.parent.searchByIdField.setText(searchField.getText());
				this.parent.searchEmployeeById();
				dispose();
			}
			catch (NumberFormatException num) {
				// display message and set colour to text field if entry is wrong
				searchField.setBackground(PINK);
				JOptionPane.showMessageDialog(null, "Wrong ID format!");
			}
		}

		else if (e.getSource() == cancel)
			dispose();
	}
}
