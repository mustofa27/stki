/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.form;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Myrna
 */
public class DocPageTableModel extends AbstractTableModel{

    private String[] columnName = {"kitabid","docID","term"};

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }
    private String[][] listHasil = {{"","",""}};
    
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
