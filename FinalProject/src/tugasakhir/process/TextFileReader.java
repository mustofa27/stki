/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Indra
 */
public class TextFileReader {
    
    FileReader fr = null;
    File file = null;

    public TextFileReader(String path) {
        file = new File(path);

    }
    
    public String Read() throws IOException{
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException ex) {
        }
        StringBuilder contents;
        try (BufferedReader input = new BufferedReader(fr)) {
            String line = null;
            contents = new StringBuilder();
            try {
                while ((line = input.readLine()) != null) {                
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
                
            } catch (Exception e) {
            }
        }
        this.fr.close();
        
        return contents.toString();
    }
    
    
    
}
