/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends Dialog implements ActionListener {


	public AddRecordDialog(ApplicationView parent) {
		setTitle("Add Record");
		setModal(true);
		this.parent = parent;
		this.parent.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane(dialogPane());
		setContentPane(scrollPane);
		
		getRootPane().setDefaultButton(save);
		
		setSize(ADD_WIDTH,ADD_HEIGHT);
		setLocation(X_POS, Y_POS);
		setVisible(true);
	}


	public Container dialogPane() {
		JPanel empDetails, buttonPanel;
		empDetails = new JPanel(new MigLayout());
		buttonPanel = new JPanel();
		JTextField field;

		empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		empDetails.add(new JLabel(EMP_ID), LABEL_CONSTRAINTS);
		empDetails.add(idField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);
		idField.setEditable(false);
		

		empDetails.add(new JLabel(EMP_PPS), LABEL_CONSTRAINTS);
		empDetails.add(ppsField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_SNAME), LABEL_CONSTRAINTS);
		empDetails.add(surnameField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_FNAME), LABEL_CONSTRAINTS);
		empDetails.add(firstNameField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_GEN), LABEL_CONSTRAINTS);
		empDetails.add(genderCombo = new JComboBox<String>(this.parent.gender), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_DEP), LABEL_CONSTRAINTS);
		empDetails.add(departmentCombo = new JComboBox<String>(this.parent.department), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_SAL), LABEL_CONSTRAINTS);
		empDetails.add(salaryField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

		empDetails.add(new JLabel(EMP_FULL), LABEL_CONSTRAINTS);
		empDetails.add(fullTimeCombo = new JComboBox<String>(this.parent.fullTime), FIELD_CONSTRAINTS);

		buttonPanel.add(save = new JButton("Save"));
		save.addActionListener(this);
		save.requestFocus();
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);

		empDetails.add(buttonPanel, "span 2,"+FIELD_CONSTRAINTS);

		// loop through all panel components and add fonts and listeners
		for (int i = 0; i < empDetails.getComponentCount(); i++) {
			empDetails.getComponent(i).setFont(FONT1);
			if (empDetails.getComponent(i) instanceof JComboBox) {
				empDetails.getComponent(i).setBackground(Color.WHITE);
			}// end if
			else if(empDetails.getComponent(i) instanceof JTextField){
				field = (JTextField) empDetails.getComponent(i);
				if(field == ppsField)
					field.setDocument(new JTextFieldLimit(9));
				else
				field.setDocument(new JTextFieldLimit(COLUMNS));
			}// end else if
		}// end for
		idField.setText(Integer.toString(this.parent.getNextFreeId()));
		return empDetails;
	}


	public void addRecord() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(), surnameField.getText().toUpperCase(),
				firstNameField.getText().toUpperCase(), genderCombo.getSelectedItem().toString().charAt(0),
				departmentCombo.getSelectedItem().toString(), Double.parseDouble(salaryField.getText()), fullTime);
		this.parent.currentEmployee = theEmployee;
		this.parent.addRecord(theEmployee);
		this.parent.displayRecords(theEmployee);
	}

	// check for input in text fields
	public boolean checkInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display message
		if (ppsField.getText().equals("")) {
			ppsField.setBackground(PINK);
			valid = false;
		}// end if
		if (this.parent.correctPps(this.ppsField.getText().trim(), -1)) {
			ppsField.setBackground(PINK);
			valid = false;
		}// end if
		if (surnameField.getText().isEmpty()) {
			surnameField.setBackground(PINK);
			valid = false;
		}// end if
		if (firstNameField.getText().isEmpty()) {
			firstNameField.setBackground(PINK);
			valid = false;
		}// end if
		if (genderCombo.getSelectedIndex() == 0) {
			genderCombo.setBackground(PINK);
			valid = false;
		}// end if
		if (departmentCombo.getSelectedIndex() == 0) {
			departmentCombo.setBackground(PINK);
			valid = false;
		}// end if
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(PINK);
				valid = false;
			}// end if
		}// end try
		catch (NumberFormatException num) {
			salaryField.setBackground(PINK);
			valid = false;
		}// end catch
		if (fullTimeCombo.getSelectedIndex() == 0) {
			fullTimeCombo.setBackground(PINK);
			valid = false;
		}// end if
		return valid;
	}// end checkInput

	// set text field to white colour
	public void setToWhite() {
		ppsField.setBackground(Color.WHITE);
		surnameField.setBackground(Color.WHITE);
		firstNameField.setBackground(Color.WHITE);
		salaryField.setBackground(Color.WHITE);
		genderCombo.setBackground(Color.WHITE);
		departmentCombo.setBackground(Color.WHITE);
		fullTimeCombo.setBackground(Color.WHITE);
	}// end setToWhite

	// action performed
	public void actionPerformed(ActionEvent e) {
		// if chosen option save, save record to file
		if (e.getSource() == save) {
			// if inputs correct, save record
			if (checkInput()) {
				addRecord();// add record to file
				dispose();// dispose dialog
				this.parent.changesMade = true;
			}// end if
			// else display message and set text fields to white colour
			else {
				JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
				setToWhite();
			}// end else
		}// end if
		else if (e.getSource() == cancel)
			dispose();// dispose dialog
	}// end actionPerformed
}// end class AddRecordDialog