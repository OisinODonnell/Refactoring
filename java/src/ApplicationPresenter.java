import javax.swing.*;
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
}
