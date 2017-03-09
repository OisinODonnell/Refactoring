import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Oisin on 3/9/2017.
 */
public class ApplicationPresenter implements Presenter {

    private View view;

    @Override
    public void setView(View view) {
        this.view = view;

    }

    private View getView() {
        if(view == null) {
            throw new IllegalStateException("The view is not set");
        }
        return view;
    }

    public JMenuBar menuBar(ApplicationView context, ApplicationPresenter domain) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        recordMenu = new JMenu("Records");
        recordMenu.setMnemonic(KeyEvent.VK_R);
        navigateMenu = new JMenu("Navigate");
        navigateMenu.setMnemonic(KeyEvent.VK_N);
        closeMenu = new JMenu("Exit");
        closeMenu.setMnemonic(KeyEvent.VK_E);

        menuBar.add(fileMenu);
        menuBar.add(recordMenu);
        menuBar.add(navigateMenu);
        menuBar.add(closeMenu);

        fileMenu.add(context.open = new JMenuItem("Open")).addActionListener(context);
        context.open.setMnemonic(KeyEvent.VK_O);
        context.open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(context.save = new JMenuItem("Save")).addActionListener(context);
        context.save.setMnemonic(KeyEvent.VK_S);
        context.save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(context.saveAs = new JMenuItem("Save As")).addActionListener(context);
        context.saveAs.setMnemonic(KeyEvent.VK_F2);
        context.saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

        recordMenu.add(context.create = new JMenuItem("Create new Record")).addActionListener(context);
        context.create.setMnemonic(KeyEvent.VK_N);
        context.create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        recordMenu.add(context.modify = new JMenuItem("Modify Record")).addActionListener(context);
        context.modify.setMnemonic(KeyEvent.VK_E);
        context.modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        recordMenu.add(context.delete = new JMenuItem("Delete Record")).addActionListener(context);

        navigateMenu.add(context.firstItem = new JMenuItem("First"));
        context.firstItem.addActionListener(context);
        navigateMenu.add(context.prevItem = new JMenuItem("Previous"));
        context.prevItem.addActionListener(context);
        navigateMenu.add(context.nextItem = new JMenuItem("Next"));
        context.nextItem.addActionListener(context);
        navigateMenu.add(context.lastItem = new JMenuItem("Last"));
        context.lastItem.addActionListener(context);
        navigateMenu.addSeparator();
        navigateMenu.add(context.searchById = new JMenuItem("Search by ID")).addActionListener(context);
        navigateMenu.add(context.searchBySurname = new JMenuItem("Search by Surname")).addActionListener(context);
        navigateMenu.add(context.listAll = new JMenuItem("List all Records")).addActionListener(context);

        closeMenu.add(context.closeApp = new JMenuItem("Close")).addActionListener(context);
        context.closeApp.setMnemonic(KeyEvent.VK_F4);
        context.closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

        return menuBar;
    }

    public JPanel searchPanel(ApplicationView context, ApplicationPresenter domain) {
        JPanel searchPanel = new JPanel(new MigLayout());

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
        searchPanel.add(context.searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
        context.searchByIdField.addActionListener(context);
        context.searchByIdField.setDocument(new JTextFieldLimit(20));
        searchPanel.add(context.searchId = new JButton(new ImageIcon(
                        new ImageIcon("imgres.png").getImage().getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
                "width 35:35:35, height 20:20:20, growx, pushx, wrap");
        context.searchId.addActionListener(context);
        context.searchId.setToolTipText("Search Employee By ID");

        searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
        searchPanel.add(context.searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
        context.searchBySurnameField.addActionListener(context);
        context.searchBySurnameField.setDocument(new JTextFieldLimit(20));
        searchPanel.add(
                context.searchSurname = new JButton(new ImageIcon(new ImageIcon("imgres.png").getImage()
                        .getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
                "width 35:35:35, height 20:20:20, growx, pushx, wrap");
        context.searchSurname.addActionListener(context);
        context.searchSurname.setToolTipText("Search Employee By Surname");

        return searchPanel;
    }

    public JPanel navigPanel(ApplicationView context, ApplicationPresenter domain) {
        JPanel navigPanel = new JPanel();

        navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
        navigPanel.add(context.first = new JButton(new ImageIcon(
                new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.first.setPreferredSize(new Dimension(17, 17));
        context.first.addActionListener(context);
        context.first.setToolTipText("Display first Record");

        navigPanel.add(context.previous = new JButton(new ImageIcon(new ImageIcon("previous.png").getImage()
                .getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.previous.setPreferredSize(new Dimension(17, 17));
        context.previous.addActionListener(context);
        context.previous.setToolTipText("Display next Record");

        navigPanel.add(context.next = new JButton(new ImageIcon(
                new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.next.setPreferredSize(new Dimension(17, 17));
        context.next.addActionListener(context);
        context.next.setToolTipText("Display previous Record");

        navigPanel.add(context.last = new JButton(new ImageIcon(
                new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.last.setPreferredSize(new Dimension(17, 17));
        context.last.addActionListener(context);
        context.last.setToolTipText("Display last Record");

        return navigPanel;
    }

    public JPanel buttonPanel(ApplicationView context, ApplicationPresenter domain) {
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(context.add = new JButton("Add Record"), "growx, pushx");
        context.add.addActionListener(context);
        context.add.setToolTipText("Add new Employee Record");
        buttonPanel.add(context.edit = new JButton("Edit Record"), "growx, pushx");
        context.edit.addActionListener(context);
        context.edit.setToolTipText("Edit current Employee");
        buttonPanel.add(context.deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
        context.deleteButton.addActionListener(context);
        context.deleteButton.setToolTipText("Delete current Employee");
        buttonPanel.add(context.displayAll = new JButton("List all Records"), "growx, pushx");
        context.displayAll.addActionListener(context);
        context.displayAll.setToolTipText("List all Registered Employees");

        return buttonPanel;
    }

    public JPanel detailsPanel(ApplicationView context, ApplicationPresenter domain) {
        JPanel empDetails = new JPanel(new MigLayout());
        JPanel buttonPanel = new JPanel();
        JTextField field;

        empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        empDetails.add(new JLabel("ID:"), "growx, pushx");
        empDetails.add(context.idField = new JTextField(20), "growx, pushx, wrap");
        context.idField.setEditable(false);

        empDetails.add(new JLabel("PPS Number:"), "growx, pushx");
        empDetails.add(context.ppsField = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Surname:"), "growx, pushx");
        empDetails.add(context.surnameField = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("First Name:"), "growx, pushx");
        empDetails.add(context.firstNameField = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Gender:"), "growx, pushx");
        empDetails.add(context.genderCombo = new JComboBox<String>(context.gender), "growx, pushx, wrap");

        empDetails.add(new JLabel("Department:"), "growx, pushx");
        empDetails.add(context.departmentCombo = new JComboBox<String>(context.department), "growx, pushx, wrap");

        empDetails.add(new JLabel("Salary:"), "growx, pushx");
        empDetails.add(context.salaryField = new JTextField(20), "growx, pushx, wrap");

        empDetails.add(new JLabel("Full Time:"), "growx, pushx");
        empDetails.add(context.fullTimeCombo = new JComboBox<String>(context.fullTime), "growx, pushx, wrap");

        buttonPanel.add(context.saveChange = new JButton("Save"));
        context.saveChange.addActionListener(context);
        context.saveChange.setVisible(false);
        context.saveChange.setToolTipText("Save changes");
        buttonPanel.add(context.cancelChange = new JButton("Cancel"));
        context.cancelChange.addActionListener(context);
        context.cancelChange.setVisible(false);
        context.cancelChange.setToolTipText("Cancel edit");

        empDetails.add(buttonPanel, "span 2,growx, pushx,wrap");

        // loop through panel components and add listeners and format
        for (int i = 0; i < empDetails.getComponentCount(); i++) {
            empDetails.getComponent(i).setFont(context.font1);
            if (empDetails.getComponent(i) instanceof JTextField) {
                field = (JTextField) empDetails.getComponent(i);
                field.setEditable(false);
                if (field == context.ppsField)
                    field.setDocument(new JTextFieldLimit(9));
                else
                    field.setDocument(new JTextFieldLimit(20));
                field.getDocument().addDocumentListener(context);
            } // end if
            else if (empDetails.getComponent(i) instanceof JComboBox) {
                empDetails.getComponent(i).setBackground(Color.WHITE);
                empDetails.getComponent(i).setEnabled(false);
                ((JComboBox<String>) empDetails.getComponent(i)).addItemListener(context);
                ((JComboBox<String>) empDetails.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
                    // set foregroung to combo boxes
                    public void paint(Graphics g) {
                        setForeground(new Color(65, 65, 65));
                        super.paint(g);
                    }// end paint
                });
            } // end else if
        } // end for
        return empDetails;
    }
}
