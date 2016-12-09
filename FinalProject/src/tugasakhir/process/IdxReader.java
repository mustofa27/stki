/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.index.TermVectorOffsetInfo;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Myrna
 */
public class IdxReader  {
    IndexReader ir;
    
    //TermDocs termfreq =null ;    
    
    public IdxReader(String indexPath) throws IOException {
        Directory index = FSDirectory.open(new File(indexPath));
        this.ir = IndexReader.open(index);
    }
    
    public void getTermPos(String termStr) throws IOException{
       int docfreq = 0;
       TermFreqVector vector=null;
       TermPositionVector tpvector;
       TermVectorOffsetInfo[] tvoffsetinfo;
       
       Term myterm = new Term("nass",termStr);
       //this.ir.        
       docfreq = this.ir.docFreq(myterm);       
       int[] docs = new int[docfreq];
       int[] freqs = new int[docfreq];
       int count = this.ir.termDocs(myterm).read(docs, freqs);       
    
       System.out.println("--------------------------- term occurence : ");  
       int termidx;
       Document document ;
       
       //idHalaman[i] = Integer.parseInt(document.get("halKitab"));
       for(int x =0; x<docs.length; x++){
          document = this.ir.document(docs[x]); 
          
          System.out.println("Kitab ID : " +  Integer.parseInt(document.get("idKitab")) 
                  + ".  halaman : " + Integer.parseInt(document.get("halKitab")) 
                  + "term occ: " +  freqs[x]);
          vector  = this.ir.getTermFreqVector(docs[x], "nass");
          
          tpvector = (TermPositionVector)vector;
          termidx = vector.indexOf(termStr);
          int[] termposx = tpvector.getTermPositions(termidx);  
          tvoffsetinfo = tpvector.getOffsets(termidx);  
          for (int j=0;j<termposx.length;j++)   
            System.out.println("termpos : " +termposx[j]);
       }
    }    
       

    public void getTerms() throws IOException{
       Set<String> uniqueTerms = new HashSet<>();
       TermEnum term_enum = this.ir.terms();
       while (term_enum.next()) {
          final Term term = term_enum.term();
          if (term.field().equals("nass")) {
                uniqueTerms.add(term.text());
          }          
       } 
       int count = 0;
       for (String str: uniqueTerms){
           System.out.println(str);
           
       System.out.println(uniqueTerms.size());
    }
    
    }
}
