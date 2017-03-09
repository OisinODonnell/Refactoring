import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ApplicationPresenter implements Presenter, Constants {

    private View view;

    @Override
    public void setView(View view) {
        this.view = view;
    }

    private View getView() {
        if (view == null) {
            throw new IllegalStateException("The view is not set");
        }
        return view;
    }

    public JMenu createFileMenu(ApplicationView context) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        context.open   = createJMenuItem  ("Open", KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        context.save   = createJMenuItem  ("Save", KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        context.saveAs = createJMenuItem("Save As", KeyEvent.VK_F2, ActionEvent.CTRL_MASK);

        fileMenu.add(context.open).addActionListener(context);
        fileMenu.add(context.save).addActionListener(context);
        fileMenu.add(context.saveAs).addActionListener(context);

        return fileMenu;
    }

    public JMenuItem createJMenuItem(String title, int key, int event) {
        JMenuItem newItem = new JMenuItem();
        newItem.setText(title);
        newItem.setMnemonic(key);
        newItem.setAccelerator(KeyStroke.getKeyStroke(key,event));

        return newItem;
    }
    public JMenuItem createJMenuItem(String title) {
        JMenuItem newItem = new JMenuItem();
        newItem.setText(title);
        return newItem;
    }
    public JMenu createRecordMenu(ApplicationView context) {
        JMenu recordMenu = new JMenu("Records");
        recordMenu.setMnemonic(KeyEvent.VK_R);

        context.create = createJMenuItem("Create new Record", KeyEvent.VK_N, ActionEvent.CTRL_MASK);
        context.modify = createJMenuItem("Modify Record", KeyEvent.VK_E, ActionEvent.CTRL_MASK);
        context.delete = createJMenuItem("Delete Record");

        recordMenu.add(context.create).addActionListener(context);
        recordMenu.add(context.modify).addActionListener(context);
        recordMenu.add(context.delete).addActionListener(context);

        return recordMenu;
    }

    public JMenu createNavigateMenu(ApplicationView context) {
        JMenu navigateMenu = new JMenu("Navigate");
        navigateMenu.setMnemonic(KeyEvent.VK_N);

        context.firstItem = createJMenuItem("First");
        context.prevItem = createJMenuItem("Previous");
        context.nextItem = createJMenuItem("Next");
        context.lastItem = createJMenuItem("Last");
        context.searchById      = createJMenuItem("Search by ID");
        context.searchBySurname = createJMenuItem("Search by Surname");

        navigateMenu.add(context.firstItem      ).addActionListener(context);
        navigateMenu.add(context.prevItem       ).addActionListener(context);
        navigateMenu.add(context.nextItem       ).addActionListener(context);
        navigateMenu.add(context.lastItem       ).addActionListener(context);
        navigateMenu.add(context.searchById     ).addActionListener(context);
        navigateMenu.add(context.searchBySurname).addActionListener(context);

        return navigateMenu;
    }

    public JMenu createCloseMenu(ApplicationView context) {
        JMenu closeMenu = new JMenu("Exit");
        closeMenu.setMnemonic(KeyEvent.VK_E);

        context.closeApp = createJMenuItem("Close", KeyEvent.VK_F4, ActionEvent.CTRL_MASK);
        closeMenu.add(context.closeApp).addActionListener(context);

        return closeMenu;

    }


    public JMenuBar createMenuBar(ApplicationView context, ApplicationPresenter domain) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = domain.createFileMenu(context);
        JMenu recordMenu = domain.createRecordMenu(context);
        JMenu navigateMenu = domain.createNavigateMenu(context);
        JMenu closeMenu = domain.createCloseMenu(context);

        menuBar.add(fileMenu);
        menuBar.add(recordMenu);
        menuBar.add(navigateMenu);
        menuBar.add(closeMenu);

        return menuBar;
    }

    public JPanel createButtonPanel(ApplicationView context) {
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

    public JPanel createSearchPanel(ApplicationView context) {
        JPanel searchPanel = new JPanel(new MigLayout());

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
        searchPanel.add(context.searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
        context.searchByIdField.addActionListener(context);
        context.searchByIdField.setDocument(new JTextFieldLimit(20));
        //context.searchByIdField.setText("Search by ID");

        searchPanel.add(context.searchId = new JButton(new ImageIcon(
                        new ImageIcon("imgres.png").getImage().getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
                "width 35:35:35, height 20:20:20, growx, pushx, wrap");
        context.searchId.addActionListener(context);
        context.searchId.setToolTipText("Search Employee By ID");

        searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
        searchPanel.add(context.searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
        context.searchBySurnameField.addActionListener(context);
        context.searchBySurnameField.setDocument(new JTextFieldLimit(20));
        // context.searchBySurnameField.setText("Search by Surname");
        searchPanel.add(
                context.searchSurname = new JButton(new ImageIcon(new ImageIcon("imgres.png").getImage()
                        .getScaledInstance(35, 20, java.awt.Image.SCALE_SMOOTH))),
                "width 35:35:35, height 20:20:20, growx, pushx, wrap");
        context.searchSurname.addActionListener(context);
        context.searchSurname.setToolTipText("Search Employee By Surname");

        return searchPanel;
    }

    public JPanel createNavigPanel(ApplicationView context) {
        JPanel navigPanel = new JPanel();

        navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
        navigPanel.add(context.first = new JButton(new ImageIcon(
                new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.first.setPreferredSize(new Dimension(17, 17));
        context.first.addActionListener(context);
        context.first.setToolTipText("Display first Record");
        context.first.setText("First");

        navigPanel.add(context.previous = new JButton(new ImageIcon(new ImageIcon("previous.png").getImage()
                .getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.previous.setPreferredSize(new Dimension(17, 17));
        context.previous.addActionListener(context);
        context.previous.setToolTipText("Display next Record");
        context.previous.setText("Previous");


        navigPanel.add(context.next = new JButton(new ImageIcon(
                new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.next.setPreferredSize(new Dimension(17, 17));
        context.next.addActionListener(context);
        context.next.setToolTipText("Display previous Record");
        context.next.setText("Next");

        navigPanel.add(context.last = new JButton(new ImageIcon(
                new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
        context.last.setPreferredSize(new Dimension(17, 17));
        context.last.addActionListener(context);
        context.last.setToolTipText("Display last Record");
        context.last.setText("Last");


        return navigPanel;
    }

    public JPanel createDetailsPanel(ApplicationView context) {
        JPanel empDetails = new JPanel(new MigLayout());
        JPanel buttonPanel = new JPanel();
        JTextField field;

        buttonPanel = createButtonPanel(buttonPanel, context);
        empDetails = buildEmpDetails(empDetails, buttonPanel, context);

        empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        // loop through panel components and add listeners and format
        for (int i = 0; i < empDetails.getComponentCount(); i++) {
            empDetails.getComponent(i).setFont(FONT1);
            if (empDetails.getComponent(i) instanceof JTextField) {
                field = (JTextField) empDetails.getComponent(i);
                field.setEditable(false);
                if (field == context.ppsField)
                    field.setDocument(new JTextFieldLimit(9));
                else
                    field.setDocument(new JTextFieldLimit(COLUMNS));
                field.getDocument().addDocumentListener(context);
            }
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
            }
        }
        return empDetails;
    }
    public JPanel buildEmpDetails(JPanel empDetails, JPanel buttonPanel, ApplicationView context)
    {
        empDetails.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        empDetails.add(new JLabel(EMP_ID), LABEL_CONSTRAINTS);
        empDetails.add(context.idField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);
        context.idField.setEditable(false);

        empDetails.add(new JLabel(EMP_PPS), LABEL_CONSTRAINTS);
        empDetails.add(context.ppsField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

        empDetails.add(new JLabel(EMP_SNAME), LABEL_CONSTRAINTS);
        empDetails.add(context.surnameField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);


        empDetails.add(new JLabel(EMP_FNAME), LABEL_CONSTRAINTS);
        empDetails.add(context.firstNameField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

        empDetails.add(new JLabel(EMP_GEN), LABEL_CONSTRAINTS);
        empDetails.add(context.genderCombo = new JComboBox<String>(context.gender), FIELD_CONSTRAINTS);

        empDetails.add(new JLabel(EMP_DEP), LABEL_CONSTRAINTS);
        empDetails.add(context.departmentCombo = new JComboBox<String>(context.department), FIELD_CONSTRAINTS);

        empDetails.add(new JLabel(EMP_SAL), LABEL_CONSTRAINTS);
        empDetails.add(context.salaryField = new JTextField(COLUMNS), FIELD_CONSTRAINTS);

        empDetails.add(new JLabel(EMP_FULL), LABEL_CONSTRAINTS);
        empDetails.add(context.fullTimeCombo = new JComboBox<String>(context.fullTime), FIELD_CONSTRAINTS);

        empDetails.add(buttonPanel, "span 2,"+FIELD_CONSTRAINTS);
        context.idField.setText(Integer.toString(context.getNextFreeId()));

        return empDetails;
    }
    public JPanel createButtonPanel(JPanel buttonPanel, ApplicationView context) {

//        buttonPanel.add(context.saveBtn = new JButton("Save"));
//        context.saveBtn.addActionListener(context);
//        context.saveBtn.requestFocus();
//        buttonPanel.add(context.cancel = new JButton("Cancel"));
//        context.cancel.addActionListener(context);


        buttonPanel.add(context.saveChange = new JButton("Save"));
        context.saveChange.addActionListener(context);
        context.saveChange.setVisible(false);
        context.saveChange.setToolTipText("Save changes");
        buttonPanel.add(context.cancelChange = new JButton("Cancel"));
        context.cancelChange.addActionListener(context);
        context.cancelChange.setVisible(false);
        context.cancelChange.setToolTipText("Cancel edit");

        return buttonPanel;
    }
}