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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import tugasakhir.form.HasilThesaurus;

/**
 *
 * @author Myrna, Mustofa
 */
public class IdxReader  {
    IndexReader ir;
    
    //TermDocs termfreq =null ;    
    
    public IdxReader(String indexPath) throws IOException {
        Directory index = FSDirectory.open(new File(indexPath));
        this.ir = IndexReader.open(index);
    }
    
    //public List<Bobot> getThesaurus(String q) throws ParseException, IOException{
    public List<Bobot> getThesaurus(String[] q) throws ParseException, IOException{    
//        Query query = new QueryParser(Version.LUCENE_34, "nass", new ArabicAnalyzer(Version.LUCENE_34)).parse(q);
//        String tmp = query.toString("nass");
//        String[] kueri = tmp.split(" ");
        String[] kueri = q;
        //Set<String> uniqueTerms = new HashSet<>();
        Set<Term> termCorpus = new HashSet<>();
        List<Bobot> bobotList = new ArrayList<>();
        //int[][] bobot = new int[n_q][n_t];
        
        //get corpus distinct/unique terms --------------------------------
        TermEnum term_enum = this.ir.terms();
        while (term_enum.next()) {
            final Term term = term_enum.term();
            if (term.field().equals("nass"))                 
                termCorpus.add(term);
        }        
        int n_q = kueri.length;         //Number or Query terms
        int n_t = termCorpus.size();    //Number of Corpus unique terms
        List<String> termUnik = new ArrayList<>();
        List<Term> termObjUnik = new ArrayList<>();
       
        for (Term term: termCorpus){
           termObjUnik.add(term);
           termUnik.add(term.text());
        }        
        //------------------------------------------------------------------
        int counter=0;
        int jumlah_dokumen = this.ir.maxDoc();        
        for(int i = 0; i < n_q; i++){           //For each Query term
            for(int j = 0; j < n_t; j++){       //For each unique terms in corpus
                Bobot bobot = new Bobot(0,termUnik.get(j));
                double sigmaWQuery = 0, sigmaWTerm = 0, sigmaItf=0;
                
                for(int k = 0; k < jumlah_dokumen; k++){  //For each doc
                    //read document's unique terms
                    TermFreqVector termUnikD = ir.getTermFreqVector(k, "nass");
                    TermPositionVector tpVector = (TermPositionVector)termUnikD;
                    List<String> termStr = Arrays.asList(termUnikD.getTerms());
                    //
                    if(!kueri[i].equals(termUnik.get(j))){
                        if(termStr.contains(kueri[i]) || termStr.contains(termUnik.get(j))){
                            int[] termFreq = termUnikD.getTermFrequencies();
                            int idxQuery = termStr.indexOf(kueri[i]);       //get index of 1st occurence of query in termStr
                            int idxTerm = termStr.indexOf(termUnik.get(j)); //get index of 1st occurence of each doc's terms in termStr
                            double jarak = 0;
                            if(idxQuery != -1 && idxTerm != -1){
                                int[] posQuery = tpVector.getTermPositions(idxQuery); 
                                int[] posTerm = tpVector.getTermPositions(idxTerm);

                                //calculates mean distance of Qterm and each term in doc --------------------------------
                                for(int a = 0; a < posQuery.length; a++){   
                                    int[] temp_jarak = new int[posTerm.length];
                                    for(int b = 0; b < posTerm.length; b++){
                                        temp_jarak[b] = Math.abs(posQuery[a]-posTerm[b]);
                                    }
                                    Arrays.sort(temp_jarak);
                                    jarak+=temp_jarak[0];
                                }
                                jarak/=posQuery.length; // jarak is mean distance d(ti, tk)
                            }
                            // ---------------------------------------------------------------------------------------
                            int dfQuery = ir.docFreq(termObjUnik.get(termUnik.indexOf(kueri[i]))); //df of query term(i)
                            double idfQuery = Math.log10(jumlah_dokumen/dfQuery); //inverse-df (idf) of query term(i)
                            int dfTerm = ir.docFreq(termObjUnik.get(j)); //df of corpus term(j)
                            double idfTerm = Math.log10(jumlah_dokumen/dfTerm); //inverse-df (idf) of corpus term(j)

                            //double itf = Math.log10(n_t/termUnikD.size());
                            double tfidfQuery = idfQuery*(idxQuery != -1 ? termFreq[idxQuery] : 0);
                            double tfidfTerm = idfTerm*(idxTerm != -1 ? termFreq[idxTerm]: 0);
                            double normJarak = Math.log10(jarak)+1;

                            sigmaWQuery += Math.pow(tfidfQuery, 2);
                            sigmaWTerm += Math.pow(tfidfTerm, 2);
                            //sigmaItf += Math.pow(itf, 2);
                            if(jarak != 0){
                                double w = (tfidfQuery * tfidfTerm/* * itf*/)/normJarak;
                                //double w = (tfidfQuery * tfidfTerm * itf)/jarak;
                                bobot.setSimilarity(bobot.getSimilarity()+w);
                            }
                        }
                    }//end of IF
                }//end of FOR k : each doc
                if(bobot.getSimilarity() != 0){
                    sigmaWTerm = Math.sqrt(sigmaWTerm);
                    sigmaWQuery = Math.sqrt(sigmaWQuery);
                    //sigmaItf = Math.sqrt(sigmaItf);
                    
                    if(sigmaWQuery != 0 && sigmaWTerm != 0 /*&& sigmaItf != 0*/)
                        //Sim(tQuery, t(j)) in doc(k)
                        //bobot.setSimilarity(bobot.getSimilarity()/(sigmaWQuery * sigmaWTerm * sigmaItf)); 
                        bobot.setSimilarity(bobot.getSimilarity()/(sigmaWQuery * sigmaWTerm)); 
                    bobotList.add(bobot);
                    counter++;
                    System.out.println(counter + ". Query : " + kueri[i] + " term: " + bobot.getKata()  
                            + " similarity: " + bobot.getSimilarity());
                }
            }//end of FOR j : each unique term in corpus
        }//end of FOR i : each Query term
        
        Collections.sort(bobotList);
        return bobotList;
    }
    public double getDocVector(ScoreDoc[] topDocs) throws IOException{
        List<Bobot> bobotList = new ArrayList<>();
        double sigmaWTerm = 0, sigmaItf=0;
        //==========================================
        Set<Term> termCorpus = new HashSet<>();
        
        TermEnum term_enum = this.ir.terms();
        while (term_enum.next()) {
            final Term term = term_enum.term();
            if (term.field().equals("nass"))                 
                termCorpus.add(term);
        }
        
        int vectorLen = termCorpus.size();    //Number of Corpus unique terms
        List<String> termUnik = new ArrayList<>();
        List<Term> termObjUnik = new ArrayList<>();
       
        for (Term term: termCorpus){
           termObjUnik.add(term);
           termUnik.add(term.text());
        }
        TermFreqVector docVector ;
        //int n_t = termCorpus.size();    //Number of Corpus unique terms
        int Ndokcorpus = this.ir.maxDoc();
        int topdocLen = topDocs.length;
        List<List<Double>> docVectorList = new ArrayList<>();
        List<Double> docVectorLen = new ArrayList<>();
        List<Double> idf = new ArrayList<>();
        List<Double> docTengah = new ArrayList<>();
        double docTengahLength = 0;
        for(int i = 0; i < vectorLen; i++){
            int df = ir.docFreq(new Term("nass",termUnik.get(i)));
            idf.add(Math.log10(Ndokcorpus/df));
            docTengah.add(new Double(0));
        }
        for(ScoreDoc topdoc : topDocs){  //For each doc
          int dokID  = topdoc.doc;
          docVector = ir.getTermFreqVector(dokID, "nass");

          List<String> termStr = Arrays.asList(docVector.getTerms());
          int[] doctermFreq = docVector.getTermFrequencies();
          List<Double> docTermFrec = new ArrayList<>();
          for(int i = 0; i < vectorLen; i++){
              docTermFrec.add(idf.get(i)*(termStr.contains(termUnik.get(i)) ? doctermFreq[termStr.indexOf(termUnik.get(i))] : 0));
              docTengah.set(i, docTengah.get(i)+docTermFrec.get(i));
          }
          docVectorList.add(docTermFrec);
          
          double sigmaW = 0, docVLen = 0;
          for (Double w : docTermFrec)
              sigmaW = sigmaW + Math.pow(w, 2);          
          docVLen = Math.sqrt(sigmaW);
          docVectorLen.add(docVLen);
        }
        for(int i = 0; i < vectorLen; i++){
            docTengah.set(i, docTengah.get(i)/topdocLen);
            docTengahLength = docTengahLength+Math.pow(docTengah.get(i), 2);
        }
        docTengahLength = Math.sqrt(docTengahLength);
        //count sim(di, dj) within top documen retrieved 
        double sim = 0;
        for(int i = 0; i < docVectorList.size(); i++){
             List<Double> v1 = docVectorList.get(i);
             double v1Len = docVectorLen.get(i);

             double dotProduct=0;
             for(int x=0; x<vectorLen; x++)
               dotProduct = dotProduct + (v1.get(x) * docTengah.get(x));

             sim =  sim + dotProduct / (v1Len * docTengahLength);
        }
        return sim/topdocLen;   //final result : average similarity
        
    }
    public boolean constructThesaurus(String[] q) throws ParseException, IOException{    
//        Query query = new QueryParser(Version.LUCENE_34, "nass", new ArabicAnalyzer(Version.LUCENE_34)).parse(q);
//        String tmp = query.toString("nass");
//        String[] kueri = tmp.split(" ");
        Set<Term> termCorpus = new HashSet<>();
        //List<Bobot> bobotList = new ArrayList<>();
        TermEnum term_enum = this.ir.terms();
        while (term_enum.next()) {
            final Term term = term_enum.term();
            if (term.field().equals("nass"))                 
                termCorpus.add(term);
        }        
        int n_t = termCorpus.size();    //Number of Corpus unique terms
        List<String> termUnik = new ArrayList<>();
        List<Term> termObjUnik = new ArrayList<>();
       
        for (Term term: termCorpus){
           termObjUnik.add(term);
           termUnik.add(term.text());
        }        
        //------------------------------------------------------------------
        int counter=0;
        int jumlah_dokumen = this.ir.maxDoc();      
        String[][] hasil = new String[2][10];
        for(int i = 0; i < 2; i++){//For each Query term
            List<Bobot> bobotList = new ArrayList<>();
            for(int j = 0; j < n_t; j++){       //For each unique terms in corpus
                Bobot bobot = new Bobot(0,termUnik.get(j));
                double sigmaWQuery = 0, sigmaWTerm = 0, sigmaItf=0;
                
                for(int k = 0; k < jumlah_dokumen; k++){  //For each doc
                    //read document's unique terms
                    TermFreqVector termUnikD = ir.getTermFreqVector(k, "nass");
                    TermPositionVector tpVector = (TermPositionVector)termUnikD;
                    List<String> termStr = Arrays.asList(termUnikD.getTerms());
                    //
                    if(termStr.contains(termUnik.get(i)) && termStr.contains(termUnik.get(j)) && !termUnik.get(i).equals(termUnik.get(j))){
                        int[] termFreq = termUnikD.getTermFrequencies();
                        int idxQuery = termStr.indexOf(termUnik.get(i));       //get index of 1st occurence of query in termStr
                        int idxTerm = termStr.indexOf(termUnik.get(j)); //get index of 1st occurence of each doc's terms in termStr
                        int[] posQuery = tpVector.getTermPositions(idxQuery); 
                        int[] posTerm = tpVector.getTermPositions(idxTerm);
                        
                        //calculates mean distance of Qterm and each term in doc --------------------------------
                        int jarak = 0;
                        for(int a = 0; a < posQuery.length; a++){   
                            int[] temp_jarak = new int[posTerm.length];
                            for(int b = 0; b < posTerm.length; b++){
                                temp_jarak[b] = Math.abs(posQuery[a]-posTerm[b]);
                            }
                            Arrays.sort(temp_jarak);
                            jarak+=temp_jarak[0];
                        }
                        jarak/=posQuery.length; // jarak is mean distance d(ti, tk)
                        // ---------------------------------------------------------------------------------------
                       
                        int dfQuery = ir.docFreq(termObjUnik.get(i)); //df of query term(i)
                        double idfQuery = Math.log10(jumlah_dokumen/dfQuery); //inverse-df (idf) of query term(i)
                        int dfTerm = ir.docFreq(termObjUnik.get(j)); //df of corpus term(j)
                        double idfTerm = Math.log10(jumlah_dokumen/dfTerm); //inverse-df (idf) of corpus term(j)
               
                        double itf = Math.log10(n_t/termUnikD.size());
                        double tfidfQuery = idfQuery*termFreq[idxQuery];
                        double tfidfTerm = idfTerm*termFreq[idxTerm];
                        double normJarak = Math.log10(jarak)+1;
                        
                        sigmaWQuery += Math.pow(tfidfQuery, 2);
                        sigmaWTerm += Math.pow(tfidfTerm, 2);
                        sigmaItf += Math.pow(itf, 2);
                        
                        double w = (tfidfQuery * tfidfTerm * itf)/normJarak;
                        //double w = (tfidfQuery * tfidfTerm * itf)/jarak;
                        bobot.setSimilarity(bobot.getSimilarity()+w);
                    }//end of IF
                }//end of FOR k : each doc
                if(bobot.getSimilarity() != 0){
                    sigmaWTerm = Math.sqrt(sigmaWTerm);
                    sigmaWQuery = Math.sqrt(sigmaWQuery);
                    sigmaItf = Math.sqrt(sigmaItf);
                    
                    if(sigmaWQuery != 0 && sigmaWTerm != 0 && sigmaItf != 0)
                        //Sim(tQuery, t(j)) in doc(k)
                        bobot.setSimilarity(bobot.getSimilarity()/(sigmaWQuery * sigmaWTerm * sigmaItf)); 
                        //bobot.setSimilarity(bobot.getSimilarity()/(sigmaWQuery * sigmaWTerm)); 
                    bobotList.add(bobot);
                    counter++;
                    System.out.println(counter + ". Query : " + termUnik.get(i) + " term: " + bobot.getKata()  
                            + " similarity: " + bobot.getSimilarity());
                }
            }//end of FOR j : each unique term in corpus
            Collections.sort(bobotList);
            hasil[i][0] = termUnik.get(i);
            for(int idx = 1; idx < 10; idx++){
                if(idx < bobotList.size()){
                    hasil[i][idx] = bobotList.get(i).getKata() + ":" + bobotList.get(i).getSimilarity();
                }
            }
        }//end of FOR i : each Query term
        HasilThesaurus hasilThesaurus = new HasilThesaurus();
        hasilThesaurus.populateList(hasil);
        ExportToExcel.expToCSV(hasilThesaurus, "D:/hasilThesaurus.csv");
        return true;
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
