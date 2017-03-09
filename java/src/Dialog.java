import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Created by Oisin on 3/9/2017.
 */
public class Dialog extends JDialog {

    Vector<Object> allEmployees;
    JButton back, search, save, cancel;
    JTextField idField, ppsField, surnameField, firstNameField, salaryField, searchField;
    JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    ApplicationView parent;
    final int COLUMNS = 20;
    final Color PINK = new Color(255, 150, 150);
    final Color WRITE = Color.WHITE;
    final int X_POS = 350;
    final int Y_POS = 250;
    final String FIELD_CONSTRAINTS = "growx, pushx, wrap";
    final String LABEL_CONSTRAINTS = "growx, pushx";

    final Font FONT1 = new Font("SansSerif", Font.BOLD, 16);

    final int SEARCH_WIDTH = 500;
    final int SEARCH_HEIGHT = 190;

    final int ADD_WIDTH = 500;
    final int ADD_HEIGHT = 370;

    final int EMP_WIDTH = 850;
    final int EMP_HEIGHT = 500;

    private int employeeId;
    private String pps;
    private String surname;
    private String firstName;
    private char gender;
    private String department;
    private double salary;
    private boolean fullTime;

    final String EMP_ID = "ID";
    final String EMP_PPS = "PPS Number";
    final String EMP_SNAME = "Surname";
    final String EMP_FNAME = "First Name";
    final String EMP_GEN = "Gender";
    final String EMP_DEP = "Department";
    final String EMP_SAL = "Salary";
    final String EMP_FULL = "Full Time";
}
