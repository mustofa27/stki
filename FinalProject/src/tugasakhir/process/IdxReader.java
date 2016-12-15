/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.index.TermVectorOffsetInfo;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

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
    public List<Bobot> getThesaurus(String q) throws ParseException, IOException{
        Query query = new QueryParser(Version.LUCENE_34, "nass", new ArabicAnalyzer(Version.LUCENE_34)).parse(q);
        String tmp = query.toString("nass");
        String[] kueri = tmp.split(" ");
        //Set<String> uniqueTerms = new HashSet<>();
        Set<Term> termCorpus = new HashSet<>();
        List<Bobot> bobotList = new ArrayList<>();
        //int[][] bobot = new int[n_q][n_t];
        TermEnum term_enum = this.ir.terms();
        while (term_enum.next()) {
            final Term term = term_enum.term();
            if (term.field().equals("nass")) {
                //uniqueTerms.add(term.text());
                termCorpus.add(term);
            }          
        }
        int n_q = kueri.length;
        int n_t = termCorpus.size();
        List<String> termUnik = new ArrayList<>();
        List<Term> termObjUnik = new ArrayList<>();
        /*for (String str: uniqueTerms){
           termUnik.add(str);
        }*/
        for (Term term: termCorpus){
           termObjUnik.add(term);
           termUnik.add(term.text());
        }
        int jumlah_dokumen = this.ir.maxDoc();
        //System.out.print(tmp);
        for(int i = 0; i < n_q; i++){
            for(int j = 0; j < n_t; j++){
                Bobot bobot = new Bobot(0,termUnik.get(j));
                double sigmaWQuery = 0, sigmaWTerm = 0;
                for(int k = 0; k < jumlah_dokumen; k++){
                    //termunik dokumen
                    TermFreqVector termUnikD = ir.getTermFreqVector(k, "nass");
                    TermPositionVector tpVector = (TermPositionVector)termUnikD;
                    List<String> termStr = Arrays.asList(termUnikD.getTerms());
                    if(termStr.contains(kueri[i]) && termStr.contains(termUnik.get(j)) && !kueri[i].equals(termUnik.get(j))){
                        int[] termFreq = termUnikD.getTermFrequencies();
                        int idxQuery = termStr.indexOf(kueri[i]);
                        int idxTerm = termStr.indexOf(termUnik.get(j));
                        int[] posQuery = tpVector.getTermPositions(idxQuery);
                        int[] posTerm = tpVector.getTermPositions(idxTerm);
                        int jarak = 0;
                        for(int a = 0; a < posQuery.length; a++){
                            int[] temp_jarak = new int[posTerm.length];
                            for(int b = 0; b < posTerm.length; b++){
                                temp_jarak[b] = Math.abs(posQuery[a]-posTerm[b]);
                            }
                            Arrays.sort(temp_jarak);
                            jarak+=temp_jarak[0];
                        }
                        jarak/=posQuery.length;
                        int dfTerm = ir.docFreq(termObjUnik.get(j));
                        int dfQuery = ir.docFreq(termObjUnik.get(termUnik.indexOf(kueri[i])));
                        double idfQuery = Math.log10(jumlah_dokumen/dfQuery);
                        double idfTerm = Math.log10(jumlah_dokumen/dfTerm);
                        double itf = Math.log10(n_t/termUnikD.size());
                        double tfidfQuery = idfQuery*termFreq[idxQuery];
                        double tfidfTerm = idfTerm*termFreq[idxTerm];
                        sigmaWQuery += Math.pow(tfidfQuery, 2);
                        sigmaWTerm += Math.pow(tfidfTerm, 2);
                        double w = (tfidfQuery*tfidfTerm*itf)/jarak;
                        bobot.setSimilarity(bobot.getSimilarity()+w);
                    }
                }
                if(bobot.getSimilarity() != 0){
                    sigmaWTerm = Math.sqrt(sigmaWTerm);
                    sigmaWQuery = Math.sqrt(sigmaWQuery);
                    if(sigmaWQuery != 0 && sigmaWTerm != 0)
                        bobot.setSimilarity(bobot.getSimilarity()/(sigmaWQuery*sigmaWTerm));
                    bobotList.add(bobot);
                    System.out.println(j);
                }
            }
        }
        Collections.sort(bobotList);
        return bobotList;
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
