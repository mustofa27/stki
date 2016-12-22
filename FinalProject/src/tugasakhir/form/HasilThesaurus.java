/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.form;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Indra
 */
public class HasilThesaurus extends AbstractTableModel{

    private String[] columnName = {"Term","1","2","3","4","5","6","7","8","9","10"};

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }
    private String[][] listHasil = {{"","","","","","","","","","",""}};
    
    public void populateList(String[][] data){
        this.listHasil = data;
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return this.listHasil.length;
    }

    @Override
    public int getColumnCount() {
        return this.columnName.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return this.listHasil[row][col];
    }

    @Override
    public String getColumnName(int col) {
        return this.columnName[col];
    }

    
}
