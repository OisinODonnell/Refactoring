import javax.swing.*;

/**
 * Created by Oisin on 3/8/2017.
 */
public interface View {

    JMenuBar menuBar();
    JPanel searchPanel();
    JPanel navigPanel();
    JPanel buttonPanel();
    JPanel detailsPanel();
}