/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

/**
 *
 * @author LaptopKU
 */
public class MyDoc {
    //public int docID;
    public String kitabTitle;
    public int[] docList;
    public String[] docTerms;
        
    public MyDoc(String title, int[] pages, String[] terms){
        
        kitabTitle = title;
        docList = pages;
        docTerms = terms;
    }
    
    
}
