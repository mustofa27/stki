/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author myrna
 */
package tugasakhir.process;

import java.io.FileNotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

 
public class ExportToExcel {
   
    public static void expToCSV(AbstractTableModel dtm, String destfile){ 
        Workbook wb = new HSSFWorkbook();
        try {
          
          CreationHelper createhelper = wb.getCreationHelper();
          Sheet sheet = wb.createSheet("new sheet");
          Row row = null;
          Cell cell = null;
          for (int i=0;i<dtm.getRowCount();i++) {
             row = sheet.createRow(i);
             for (int j=0;j<dtm.getColumnCount();j++) {
             cell = row.createCell(j);
             cell.setCellValue((String) dtm.getValueAt(i, j));
             }
           }
          
           FileOutputStream out = new FileOutputStream(destfile);
           wb.write(out);
           out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExportToExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportToExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
     
   }
   
}
   
   
