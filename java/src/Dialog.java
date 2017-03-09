import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Oisin on 3/9/2017.
 */
public class Dialog extends JDialog implements Constants {

    Vector<Object> allEmployees;
    JButton back, search, saveBtn, cancel;
    // JTextField idField, ppsField, surnameField, firstNameField, salaryField, searchField;
    JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    ApplicationView parent;

    private int employeeId;
    private String pps;
    private String surname;
    private String firstName;
    private char gender;
    private String department;
    private double salary;
    private boolean fullTime;


}
