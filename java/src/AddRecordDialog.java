/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Color.WHITE;

public class AddRecordDialog extends Dialog implements ActionListener, Constants {

    ApplicationPresenter appPresenter = new ApplicationPresenter();


	public AddRecordDialog(ApplicationView parent) {
        setTitle("Add Record");
        setModal(true);
        this.parent = parent;
        this.parent.setEnabled(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JScrollPane scrollPane = new JScrollPane(createDialogPane());
		setContentPane(scrollPane);
		
		getRootPane().setDefaultButton(saveBtn);
		
		setSize(ADD_WIDTH,ADD_HEIGHT);
		setLocation(X_POS, Y_POS);
		setVisible(true);
	}


    public JPanel setFontsAndListeners(JPanel empDetails) {
        JTextField field;
        // loop through all panel components and add fonts and listeners
        for (int i = 0; i < empDetails.getComponentCount(); i++) {
            empDetails.getComponent(i).setFont(FONT1);
            if (empDetails.getComponent(i) instanceof JComboBox) {
                empDetails.getComponent(i).setBackground(WHITE);

            }
            else if(empDetails.getComponent(i) instanceof JTextField){
                field = (JTextField) empDetails.getComponent(i);
                if(field == parent.ppsField)
                    field.setDocument(new JTextFieldLimit(9));
                else
                    field.setDocument(new JTextFieldLimit(COLUMNS));
            }
        }
        return empDetails;
    }

	public Container createDialogPane() {

		JPanel empDetails, buttonPanel;
		empDetails = new JPanel(new MigLayout());
		buttonPanel = new JPanel();

        buttonPanel = appPresenter.createButtonPanel(buttonPanel, parent );
        empDetails = appPresenter.buildEmpDetails(empDetails, buttonPanel, parent);
        empDetails = setFontsAndListeners(empDetails);

		return empDetails;
	}

	public void addRecord() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee = new Employee(Integer.parseInt(parent.idField.getText()),
                parent.ppsField.getText().toUpperCase(), parent.surnameField.getText().toUpperCase(),
				parent.firstNameField.getText().toUpperCase(), genderCombo.getSelectedItem().toString().charAt(0),
				departmentCombo.getSelectedItem().toString(), Double.parseDouble(parent.salaryField.getText()), fullTime);
		this.parent.currentEmployee = theEmployee;
		this.parent.addRecord(theEmployee);
		this.parent.displayEmployee(theEmployee);
	}

	public boolean checkTextFieldInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display message
		if (parent.ppsField.getText().equals("")) {
			parent.ppsField.setBackground(PINK);
			valid = false;
		}// end if
		if (this.parent.correctPps(parent.ppsField.getText().trim(), -1)) {
			parent.ppsField.setBackground(PINK);
			valid = false;
		}// end if
		if (parent.surnameField.getText().isEmpty()) {
			parent.surnameField.setBackground(PINK);
			valid = false;
		}// end if
		if (parent.firstNameField.getText().isEmpty()) {
			parent.firstNameField.setBackground(PINK);
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
			Double.parseDouble(parent.salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(parent.salaryField.getText()) < 0) {
				parent.salaryField.setBackground(PINK);
				valid = false;
			}// end if
		}// end try
		catch (NumberFormatException num) {
			parent.salaryField.setBackground(PINK);
			valid = false;
		}// end catch
		if (fullTimeCombo.getSelectedIndex() == 0) {
			fullTimeCombo.setBackground(PINK);
			valid = false;
		}// end if
		return valid;
	}

	public void setTextFieldToWhite() {
		parent.ppsField.setBackground(WHITE);
		parent.surnameField.setBackground(WHITE);
		parent.firstNameField.setBackground(WHITE);
		parent.salaryField.setBackground(WHITE);
		genderCombo.setBackground(WHITE);
		departmentCombo.setBackground(WHITE);
		fullTimeCombo.setBackground(WHITE);
	}

	public void actionPerformed(ActionEvent e) {
		// if chosen option save, save record to file
		if (e.getSource() == saveBtn) {
			// if inputs correct, save record
			if (checkTextFieldInput()) {
				addRecord();// add record to file
				dispose();// dispose dialog
				this.parent.changesMade = true;
			}// end if
			// else display message and set text fields to white colour
			else {
				JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
				setTextFieldToWhite();
			}// end else
		}// end if
		else if (e.getSource() == cancel)
			dispose();// dispose dialog
	}
}