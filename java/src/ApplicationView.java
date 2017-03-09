
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class ApplicationView extends JFrame implements Constants, View, ActionListener, ItemListener, DocumentListener, WindowListener {

    private Presenter presenter;

    // hold object start position in file
    private long currentByteStart = 0;
    private RandomFile randomFile = new RandomFile();
    // display files in File Chooser only with extension .dat
    private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
    // hold file name and path for current file in use
    private File file;
    // holds true or false if any changes are made for text fields
    private boolean change = false;
    // holds true or false if any changes are made for file content
    boolean changesMade = false;
    public JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
            searchBySurname, closeApp;
    public JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
            saveChange, cancelChange;
    public JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    public JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private static ApplicationView frame = new ApplicationView();

    // holds automatically generated file name
    String generatedFileName;
    Employee currentEmployee;
    JTextField searchByIdField, searchBySurnameField;




    public static void main(String args[]) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final ApplicationPresenter domain = new ApplicationPresenter();
                final ApplicationView application = new ApplicationView();
                domain.setView(application);
                application.setPresenter(domain);

                createAndShowGUI(application, domain);
            }
        });
    }

    private static void createAndShowGUI(ApplicationView application, ApplicationPresenter domain) {

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.createContentPane(application, domain);// add content pane to frame
        frame.setSize(760, 600);
        frame.setLocation(250, 200);
        frame.setVisible(true);
    }

    public void displayEmployee(Employee thisEmployee) {
        int countGender = 0;
        int countDep = 0;
        boolean found = false;

        searchByIdField.setText("");
        searchBySurnameField.setText("");
        // if Employee is null or ID is 0 do nothing else display Employee
        // details
        if(!((thisEmployee == null) || (thisEmployee.getEmployeeId() == 0))) {
            // find corresponding gender combo box value to current employee
            while (!found && countGender < gender.length - 1) {
                if (Character.toString(thisEmployee.getGender()).equalsIgnoreCase(gender[countGender]))
                    found = true;
                else
                    countGender++;
            }
            found = false;
            // find corresponding department combo box value to current employee
            while (!found && countDep < department.length - 1) {
                if (thisEmployee.getDepartment().trim().equalsIgnoreCase(department[countDep]))
                    found = true;
                else
                    countDep++;
            }
            idField.setText(Integer.toString(thisEmployee.getEmployeeId()));
            ppsField.setText(thisEmployee.getPps().trim());
            surnameField.setText(thisEmployee.getSurname().trim());
            firstNameField.setText(thisEmployee.getFirstName());
            genderCombo.setSelectedIndex(countGender);
            departmentCombo.setSelectedIndex(countDep);
            salaryField.setText(format.format(thisEmployee.getSalary()));
            // set corresponding full time combo box value to current employee
            if (thisEmployee.getFullTime() == true)
                fullTimeCombo.setSelectedIndex(1);
            else
                fullTimeCombo.setSelectedIndex(2);
        }
        change = false;
    }

    private void displayEmployeeSummaryDialog() {
        // display Employee summary dialog if these is someone to display
        if (isSomeoneToDisplay())
            new EmployeeSummaryDialog(getAllEmloyees());
    }


    private void displaySearchBySurnameDialog() {
        if (isSomeoneToDisplay())
            new SearchBySurnameDialog(ApplicationView.this);
    }

    private void firstRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            randomFile.openReadFile(file.getAbsolutePath());
            // get byte start in file for first record
            currentByteStart = randomFile.getFirstRecordPosition();
            // assign current Employee to first record in file
            currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            randomFile.closeFile();
            // if first record is inactive look for next record
            if (currentEmployee.getEmployeeId() == 0)
                nextRecord();
        }
    }

    private void previousRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            randomFile.openReadFile(file.getAbsolutePath());
            currentByteStart = randomFile.getPreviousRecordPosition(currentByteStart);
            // assign current Employee to previous record in file
            currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            // loop to previous record until Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                currentByteStart = randomFile.getPreviousRecordPosition(currentByteStart);
                // assign current Employee to previous record in file
                currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            }
            randomFile.closeFile();
        }
    }

    private void nextRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            randomFile.openReadFile(file.getAbsolutePath());
            // get byte start in file for next record
            currentByteStart = randomFile.getNextRecordPosition(currentByteStart);
            // assign current Employee to record in file
            currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            // loop to previous next until Employee is active - ID is not 0
            while (currentEmployee.getEmployeeId() == 0) {
                // get byte start in file for next record
                currentByteStart = randomFile.getNextRecordPosition(currentByteStart);
                // assign current Employee to next record in file
                currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            }
            randomFile.closeFile();
        }
    }

    private void lastRecord() {
        // if any active record in file look for first record
        if (isSomeoneToDisplay()) {
            randomFile.openReadFile(file.getAbsolutePath());
            // get byte start in file for last record
            currentByteStart = randomFile.getLastRecordPosition();
            // assign current Employee to first record in file
            currentEmployee = randomFile.readRecordAtPosition(currentByteStart);
            randomFile.closeFile();
            // if last record is inactive look for previous record
            if (currentEmployee.getEmployeeId() == 0)
                previousRecord();
        }
    }

    // search Employee by ID
    public void searchEmployeeById() {
        boolean found = false;

        try {// try to read correct correct from input
            // if any active Employee record search for ID else do nothing
            if (isSomeoneToDisplay()) {
                firstRecord();
                int firstId = currentEmployee.getEmployeeId();
                // if ID to search is already displayed do nothing else loop through
                if (searchByIdField.getText().trim().equals(idField.getText().trim()))
                    found = true;
                else if (searchByIdField.getText().trim().equals(Integer.toString(currentEmployee.getEmployeeId()))) {
                    found = true;
                    displayEmployee(currentEmployee);
                }
                else {
                    nextRecord();
                    // loop until Employee found or until all Employees have been checked
                    while (firstId != currentEmployee.getEmployeeId()) {
                        // if found break from loop and display Employee details else look for next record
                        if (Integer.parseInt(searchByIdField.getText().trim()) == currentEmployee.getEmployeeId()) {
                            found = true;
                            displayEmployee(currentEmployee);
                            break;
                        } else
                            nextRecord();
                    }
                }
                // if Employee not found display message
                if (!found)
                    JOptionPane.showMessageDialog(null, "Employee not found!");
            }
        }
        catch (NumberFormatException e) {
            searchByIdField.setBackground(PINK);
            JOptionPane.showMessageDialog(null, "Wrong ID format!");
        }
        searchByIdField.setBackground(Color.WHITE);
        searchByIdField.setText("");
    }

    // search Employee by surname
    public void searchEmployeeBySurname() {
        boolean found = false;
        // if any active Employee record search for ID else do nothing
        if (isSomeoneToDisplay()) {
            firstRecord();
            String firstSurname = currentEmployee.getSurname().trim();
            // if ID to search is already displayed do nothing else loop through records
            if (searchBySurnameField.getText().trim().equalsIgnoreCase(surnameField.getText().trim()))
                found = true;
            else if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                found = true;
                displayEmployee(currentEmployee);
            }
            else {
                nextRecord();
                // loop until Employee found or until all Employees have been checked

                while (!firstSurname.trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                    // if found break from loop and display Employee details
                    // else look for next record
                    if (searchBySurnameField.getText().trim().equalsIgnoreCase(currentEmployee.getSurname().trim())) {
                        found = true;
                        displayEmployee(currentEmployee);
                        break;
                    }
                    else
                        nextRecord();
                }
            }
            if (!found)
                JOptionPane.showMessageDialog(null, "Employee not found!");
        }
        searchBySurnameField.setText("");
    }

    public int getNextFreeId() {
        int nextFreeId = 0;
        // if file is empty or all records are empty start with ID 1 else look
        // for last active record
        if (file.length() == 0 || !isSomeoneToDisplay())
            nextFreeId++;
        else {
            lastRecord();
            // add 1 to last active records ID to get next ID
            nextFreeId = currentEmployee.getEmployeeId() + 1;
        }
        return nextFreeId;
    }

    // get values from text fields and create Employee object
    private Employee getChangedDetails() {
        boolean fullTime = false;
        Employee theEmployee;
        if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
            fullTime = true;

        theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsField.getText().toUpperCase(),
                surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
                genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
                Double.parseDouble(salaryField.getText()), fullTime);

        return theEmployee;
    }

    // add Employee object to fail
    public void addRecord(Employee newEmployee) {
        // open file for writing
        randomFile.openWriteFile(file.getAbsolutePath());
        // write into a file
        currentByteStart = randomFile.addRecords(newEmployee);
        randomFile.closeFile();// close file for writing
    }

    // delete (make inactive - empty) record from file
    private void deleteRecord() {
        if (isSomeoneToDisplay()) {// if any active record in file display
            // message and delete record
            int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to delete record?", "Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if answer yes delete (make inactive - empty) record
            if (returnVal == JOptionPane.YES_OPTION) {
                // open file for writing
                randomFile.openWriteFile(file.getAbsolutePath());
                // delete (make inactive - empty) record in file proper position
                randomFile.deleteRecords(currentByteStart);
                randomFile.closeFile();// close file for writing
                // if any active record in file display next record
                if (isSomeoneToDisplay()) {
                    nextRecord();
                    displayEmployee(currentEmployee);
                }
            }
        }
    }

    // create vector of vectors with all Employee details
    private Vector<Object> getAllEmloyees() {
        Vector<Object> allEmployee = new Vector<Object>();
        Vector<Object> empDetails;
        long byteStart = currentByteStart;
        int firstId;

        firstRecord();
        firstId = currentEmployee.getEmployeeId();
        // loop until all Employees are added to vector
        do {
            empDetails = new Vector<Object>();
            empDetails.addElement(new Integer(currentEmployee.getEmployeeId()));
            empDetails.addElement(currentEmployee.getPps());
            empDetails.addElement(currentEmployee.getSurname());
            empDetails.addElement(currentEmployee.getFirstName());
            empDetails.addElement(new Character(currentEmployee.getGender()));
            empDetails.addElement(currentEmployee.getDepartment());
            empDetails.addElement(new Double(currentEmployee.getSalary()));
            empDetails.addElement(new Boolean(currentEmployee.getFullTime()));

            allEmployee.addElement(empDetails);
            nextRecord();// look for next record
        } while (firstId != currentEmployee.getEmployeeId());// end do - while
        currentByteStart = byteStart;

        return allEmployee;
    }

    // activate field for editing
    private void editDetails() {
        // activate field for editing if there is records to display
        if (isSomeoneToDisplay()) {
            // remove euro sign from salary text field
            salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
            change = false;
            setEnabled(true);
        }
    }

    // ignore changes and set text field unenabled
    private void cancelChange() {
        setEnabled(false);
        displayEmployee(currentEmployee);
    }

    // check if any of records in file is active - ID is not 0
    private boolean isSomeoneToDisplay() {
        boolean someoneToDisplay = false;
        // open file for reading
        randomFile.openReadFile(file.getAbsolutePath());
        // check if any of records in file is active - ID is not 0
        someoneToDisplay = randomFile.isSomeoneToDisplay();
        randomFile.closeFile();// close file for reading
        // if no records found clear all text fields and display message
        if (!someoneToDisplay) {
            currentEmployee = null;
            idField.setText("");
            ppsField.setText("");
            surnameField.setText("");
            firstNameField.setText("");
            salaryField.setText("");
            genderCombo.setSelectedIndex(0);
            departmentCombo.setSelectedIndex(0);
            fullTimeCombo.setSelectedIndex(0);
            JOptionPane.showMessageDialog(null, "No Employees registered!");
        }
        return someoneToDisplay;
    }

    // check for correct PPS format and look if PPS already in use
    public boolean correctPps(String pps, long currentByte) {
        boolean ppsExist = false;
        // check for correct PPS format based on assignment description
        if (pps.length() == 8 || pps.length() == 9) {
            if (Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
                    && Character.isDigit(pps.charAt(2)) && Character.isDigit(pps.charAt(3))
                    && Character.isDigit(pps.charAt(4)) && Character.isDigit(pps.charAt(5))
                    && Character.isDigit(pps.charAt(6)) && Character.isLetter(pps.charAt(7))
                    && (pps.length() == 8 || Character.isLetter(pps.charAt(8)))) {
                randomFile.openReadFile(file.getAbsolutePath());
                // look in file is PPS already in use
                ppsExist = randomFile.isPpsExist(pps, currentByte);
                randomFile.closeFile();
            }
            else
                ppsExist = true;
        }
        else
            ppsExist = true;

        return ppsExist;
    }

    private boolean confirmDatExtension(File fileName) {
        boolean checkFile = false;
        int length = fileName.toString().length();

        // check if last characters in file name is .dat
        if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
                && fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
            checkFile = true;
        return checkFile;
    }

    private boolean checkTextFieldForChanges() {
        boolean anyChanges = false;
        // if changes where made, allow user to save there changes
        if (change) {
            saveChanges();
            anyChanges = true;
        }
        // if no changes made, set text fields as unenabled and display current Employee
        else {
            setEnabled(false);
            displayEmployee(currentEmployee);
        }
        return anyChanges;
    }

    private boolean checkInput() {
        boolean valid = true;
        // if any of inputs are in wrong format, colour text field and display
        // message
        if (ppsField.isEditable() && ppsField.getText().trim().isEmpty()) {
            ppsField.setBackground(PINK);
            valid = false;
        }
        if (ppsField.isEditable() && correctPps(ppsField.getText().trim(), currentByteStart)) {
            ppsField.setBackground(PINK);
            valid = false;
        }
        if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
            surnameField.setBackground(PINK);
            valid = false;
        }
        if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
            firstNameField.setBackground(PINK);
            valid = false;
        }
        if (genderCombo.getSelectedIndex() == 0 && genderCombo.isEnabled()) {
            genderCombo.setBackground(PINK);
            valid = false;
        }
        if (departmentCombo.getSelectedIndex() == 0 && departmentCombo.isEnabled()) {
            departmentCombo.setBackground(PINK);
            valid = false;
        }
        try {// try to get values from text field
            Double salary = Double.parseDouble(salaryField.getText());
            // check if salary is greater than 0
            if (salary < 0) {
                salaryField.setBackground(PINK);
                valid = false;
            }
        }
        catch (NumberFormatException num) {
            if (salaryField.isEditable()) {
                salaryField.setBackground(PINK);
                valid = false;
            }
        }
        if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
            fullTimeCombo.setBackground(PINK);
            valid = false;
        }
        // display message if any input or format is wrong
        if (!valid)
            JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
        // set text field to white colour if text fields are editable
        if (ppsField.isEditable())
            setToWhite();

        return valid;
    }

    // set text field background colour to white
    private void setToWhite() {
        ppsField.setBackground(UIManager.getColor("TextField.background"));
        surnameField.setBackground(UIManager.getColor("TextField.background"));
        firstNameField.setBackground(UIManager.getColor("TextField.background"));
        salaryField.setBackground(UIManager.getColor("TextField.background"));
        genderCombo.setBackground(UIManager.getColor("TextField.background"));
        departmentCombo.setBackground(UIManager.getColor("TextField.background"));
        fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
    }

    // enable text fields for editing
    public void setEnabled(boolean booleanValue) {
        boolean search;
        if (booleanValue)
            search = false;
        else
            search = true;
        ppsField.setEditable(booleanValue);
        surnameField.setEditable(booleanValue);
        firstNameField.setEditable(booleanValue);
        genderCombo.setEnabled(booleanValue);
        departmentCombo.setEnabled(booleanValue);
        salaryField.setEditable(booleanValue);
        fullTimeCombo.setEnabled(booleanValue);
        saveChange.setVisible(booleanValue);
        cancelChange.setVisible(booleanValue);
        searchByIdField.setEnabled(search);
        searchBySurnameField.setEnabled(search);
        searchId.setEnabled(search);
        searchSurname.setEnabled(search);
    }

    private void openFile() {
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open");
        // display files in File Chooser only with extension .dat
        fc.setFileFilter(datfilter);
        File newFile; // holds opened file name and path
        // if old file is not empty or changes has been made, offer user to save
        // old file
        if (file.length() != 0 || change) {
            int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            // if user wants to save file, save it
            if (returnVal == JOptionPane.YES_OPTION) {
                saveFile();
            }
        }

        int returnVal = fc.showOpenDialog(ApplicationView.this);
        // if file been chosen, open it
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();
            // if old file wasn't saved and its name is generated file name,
            // delete this file
            if (file.getName().equals(generatedFileName))
                file.delete();
                file = newFile;
                randomFile.openReadFile(file.getAbsolutePath());
                firstRecord();
                displayEmployee(currentEmployee);
                randomFile.closeFile();
        }
    }

    private void saveFile() {
        // if file name is generated file name, save file as 'save as' else save
        // changes to file
        if (file.getName().equals(generatedFileName))
            saveFileAs();
        else {
            // if changes has been made to text field offer user to save these
            // changes
            if (change) {
                int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                // save changes if user choose this option
                if (returnVal == JOptionPane.YES_OPTION) {
                    // save changes if ID field is not empty
                    if (!idField.getText().equals("")) {

                        randomFile.openWriteFile(file.getAbsolutePath());
                        currentEmployee = getChangedDetails();
                        randomFile.changeRecords(currentEmployee, currentByteStart);
                        randomFile.closeFile();
                    }
                }
            }
            displayEmployee(currentEmployee);
            setEnabled(false);
        }
    }

    private void saveChanges() {
        int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes to current Employee?", "Save",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        // if user choose to save changes, save changes
        if (returnVal == JOptionPane.YES_OPTION) {

            randomFile.openWriteFile(file.getAbsolutePath());
            currentEmployee = getChangedDetails();
            randomFile.changeRecords(currentEmployee, currentByteStart);
            randomFile.closeFile();
            changesMade = false;
        }
        displayEmployee(currentEmployee);
        setEnabled(false);
    }

    private void saveFileAs() {
        final JFileChooser fc = new JFileChooser();
        File newFile;
        String defaultFileName = "new_Employee.dat";
        fc.setDialogTitle("Save As");
        // display files only with .dat extension
        fc.setFileFilter(datfilter);
        fc.setApproveButtonText("Save");
        fc.setSelectedFile(new File(defaultFileName));

        int returnVal = fc.showSaveDialog(ApplicationView.this);
        // if file has chosen or written, save old file in new file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            newFile = fc.getSelectedFile();

            if (!confirmDatExtension(newFile)) {
                // add .dat extension if it was not there
                newFile = new File(newFile.getAbsolutePath() + ".dat");
                randomFile.createFile(newFile.getAbsolutePath());
            }
            else

                randomFile.createFile(newFile.getAbsolutePath());

            try {// try to copy old file to new file
                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // if old file name was generated file name, delete it
                if (file.getName().equals(generatedFileName))
                    file.delete();
                file = newFile;
            }
            catch (IOException e) {
            }
        }
        changesMade = false;
    }

    // allow to save changes to file when exiting the randomFile
    private void exitApp() {

        if (file.length() != 0) {
            if(changesMade) {
                int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (returnVal == JOptionPane.YES_OPTION) {
                saveFile();
                }
            }
        }
        if(file.getName().equals(generatedFileName))
        file.delete();
        System.exit(0);

    }

	// generate 20 character long file name
	private String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}

	// create file with generated file name when randomFile is opened
	private void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		// assign generated file name to file
		file = new File(generatedFileName);
		randomFile.createFile(file.getName());
	}

    public void actionPerformed(ActionEvent e){
        String action = e.getActionCommand();
        if (e.getSource() == searchId)              searchEmployeeById();
        else if (e.getSource() == searchByIdField)  searchEmployeeById();
        else if (e.getSource() == searchSurname)    searchEmployeeBySurname();
        else if (e.getSource() == searchBySurnameField)   searchEmployeeBySurname();
        else if (e.getSource() == cancelChange)      cancelChange();

        if(checkInput() && !checkTextFieldForChanges()) {

            switch(action) {
                case "Close App":
                case "Close":
                    exitApp();
                    break;
                case "Open":
                    openFile();
                    break;
                case "Save":
                case "Save As":
                case "Save Change":
                    saveFile();
                    change = false;
                    break;
                case "Search by Surname":
                    displaySearchBySurnameDialog();
                    break;
                case "First":
                    firstRecord();
                    displayEmployee(currentEmployee);
                    break;
                case "Previous":
                    previousRecord();
                    displayEmployee(currentEmployee);
                    break;
                case "Next":
                    nextRecord();
                    displayEmployee(currentEmployee);
                    break;
                case "Last":
                    lastRecord();
                    displayEmployee(currentEmployee);
                    break;
                case "List all Records":
                    if(isSomeoneToDisplay()) displayEmployeeSummaryDialog();
                    break;
                case "Edit Record":
                    editDetails();
                    break;
                case "Create new Record":
                case "Add Record":
                    new AddRecordDialog(ApplicationView.this);
                    break;
                case "Delete Record":
                    deleteRecord();
                    break;
                case "Search by ID":
                    new SearchByIdDialog(ApplicationView.this);
                    break;
            } // switch
        }
    }

	private void createContentPane(ApplicationView application, ApplicationPresenter domain) {
		setTitle("Employee Details");
		createRandomFile();
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(domain.createMenuBar(this, domain));// add menu bar to frame
		// add search panel to frame
		dialog.add(domain.createSearchPanel(this), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(domain.createNavigPanel(this), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(domain.createButtonPanel(this), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(domain.createDetailsPanel(this), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}

    public void setPresenter(ApplicationPresenter presenter) {
        this.presenter = presenter;
    }

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		change = true;
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit randomFile
		exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}


}
