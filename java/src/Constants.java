import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Oisin on 3/9/2017.
 */
public interface Constants {

    // decimal format for inactive currency text field
    DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
    // decimal format for active currency text field
    DecimalFormat fieldFormat = new DecimalFormat("0.00");

     int COLUMNS = 20;
     Color PINK = new Color(255, 150, 150);
     Color WRITE = Color.WHITE;

     int X_POS = 350;
     int Y_POS = 250;

     String FIELD_CONSTRAINTS = "growx, pushx, wrap";
     String LABEL_CONSTRAINTS = "growx, pushx";

     Font FONT1 = new Font("SansSerif", Font.BOLD, 16);

     int SEARCH_WIDTH = 500;
     int SEARCH_HEIGHT = 190;

     int ADD_WIDTH = 500;
     int ADD_HEIGHT = 370;

     int EMP_WIDTH = 850;
     int EMP_HEIGHT = 500;

     String EMP_ID = "ID";
     String EMP_PPS = "PPS Number";
     String EMP_SNAME = "Surname";
     String EMP_FNAME = "First Name";
     String EMP_GEN = "Gender";
     String EMP_DEP = "Department";
     String EMP_SAL = "Salary";
     String EMP_FULL = "Full Time";

    String[] gender = {"", "M", "F"};
    // department combo box values
    String[] department = {"", "Administration", "Production", "Transport", "Management"};
    // full time combo box values
    String[] fullTime = {"", "Yes", "No"};

    String[] headers = { EMP_ID, EMP_PPS, EMP_SNAME, EMP_FNAME, EMP_GEN, EMP_DEP, EMP_SAL, EMP_FULL};

    // column widths
    int[] colWidth = { 15, 100, 120, 120, 50, 120, 80, 80 };

}
