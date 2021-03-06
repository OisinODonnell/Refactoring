/*
 * 
 * This is the summary dialog for displaying all Employee details
 * 
 * */

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class EmployeeSummaryDialog extends Dialog implements ActionListener {

	Vector<Object> allEmployees;
	JButton back;
	
	public EmployeeSummaryDialog(Vector<Object> allEmployees) {
		setTitle("Employee Summary");
		setModal(true);
		this.allEmployees = allEmployees;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(createSummaryPane());
		setContentPane(scrollPane);

		setSize(EMP_WIDTH,EMP_HEIGHT);
		setLocation(X_POS, Y_POS);
		setVisible(true);

	}

	public Container createSummaryPane() {
		JPanel summaryDialog = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JTable employeeTable;
		DefaultTableModel tableModel;
		// column center alignment
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		// column left alignment 
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		Vector<String> header = new Vector<String>();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		// add headers
		for (int i = 0; i < headers.length; i++) {
			header.addElement(headers[i]);
		}
		// construnct table and choose table model for each column
		tableModel = new DefaultTableModel(this.allEmployees, header) {
			public Class getColumnClass(int c) {
				switch (c) {
				case 0:
					return Integer.class;
				case 4:
					return Character.class;
				case 6:
					return Double.class;
				case 7:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};

		employeeTable = new JTable(tableModel);
		// add header names to table
		for (int i = 0; i < employeeTable.getColumnCount(); i++) {
			employeeTable.getColumn(headers[i]).setMinWidth(colWidth[i]);
		}
		// set alignments
		employeeTable.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		employeeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		employeeTable.getColumnModel().getColumn(6).setCellRenderer(new DecimalFormatRenderer());

		employeeTable.setEnabled(false);
		employeeTable.setPreferredScrollableViewportSize(new Dimension(800, (15 * employeeTable.getRowCount() + 15)));
		employeeTable.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane(employeeTable);

		buttonPanel.add(back = new JButton("Back"));
		back.addActionListener(this);
		back.setToolTipText("Return to main screen");
		
		summaryDialog.add(buttonPanel,FIELD_CONSTRAINTS);
		summaryDialog.add(scrollPane,FIELD_CONSTRAINTS);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Details"));
		
		return summaryDialog;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back){
			dispose();
		}

	}
	// format for salary column
	static class DecimalFormatRenderer extends DefaultTableCellRenderer {
		 private static final DecimalFormat format = new DecimalFormat(
		 "\u20ac ###,###,##0.00" );

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			 JLabel label = (JLabel) c;
			 label.setHorizontalAlignment(JLabel.RIGHT);
			 // format salary column
			value = format.format((Number) value);

			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
}
