/**
 * @author: Wat Wing Huen 21085591d
 * @author: LI Xiaoyang 21074627d
 */

import javax.swing.table.DefaultTableModel;

public class TableSetting extends DefaultTableModel{

	
	public TableSetting() {

	}
	
	public TableSetting(String[] header) {
		for (String s : header) {
			addColumn(s);
		}
	}
	
	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}
		
	public int getRowCount() {
		return super.getRowCount();
	}
	
	public int getColumnCount() {
		return super.getColumnCount();
	}
	
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
	}
	
	public String getColumnName(int column) {
		return super.getColumnName(column);
	}
	
	public void removeRow(int row) {
		super.removeRow(row);
	}
	
	public void addRow(Object[] rowData) {
		super.addRow(rowData);
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
}