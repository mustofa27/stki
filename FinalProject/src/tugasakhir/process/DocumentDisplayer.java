/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Indra
 */
public class DocumentDisplayer {

    IndexReader ir;
    
    public DocumentDisplayer(String indexPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(new File(indexPath));
        this.ir = IndexReader.open(indexDirectory);
    }
    
    public String[][] getDocumentList() throws CorruptIndexException, IOException{
        int maxDoc = this.ir.numDocs();
        String[][] docList = new String[maxDoc][3];
        
        for (int i = 0; i < maxDoc; i++) {            
                Document doc = this.ir.document(i);
                docList[i][0] = doc.get("idkitab");
                docList[i][1] = doc.get("halkitab");
                //docList[i][1] = doc.get("nass");
                docList[i][2] = doc.get("nass"); 
        }        
        return docList;
    }
    
    public String[][] getKitabList() throws IOException{
        String[][] idKitab = new String[21][2];  //cek kitab_id list below
        int i = 0;
        HashMap<Integer,String> judulKitab = this.getJudul();
        try (TermEnum terms = this.ir.terms(new Term("idKitab"))) {
            while ("idKitab".equals(terms.term().field())) {                
                idKitab[i][0] = terms.term().text();
                idKitab[i][1] = judulKitab.get(Integer.parseInt(idKitab[i][0]));
                
                i++;
                if(!terms.next())
                    break;
            }
        }
            
//        Arrays.sort
        
        
        

        return idKitab;
    }
    
        public HashMap<Integer,String> getJudul(){
    
String[][] judul = {
    {"1","نهاية المطلب في دراية المذهب"},
{"2","المجموع شرح المهذب"},
{"3","التذكرة في الفقه الشافعي لابن الملقن"},
{"4","كفاية الأخيار في حل غاية الإختصار"},
{"5","نهاية المحتاج إلى شرح المنهاج"},
{"6"," الوسيط في المذهب"},
{"7"," البيان في مذهب الإمام الشافعي"},
{"8","فتح العزيز بشرح الوجيز = الشرح الكبير"},
{"9","الغرر البهية في شرح البهجة الوردية"},
{"10","غاية البيان شرح زبد ابن رسلان"},
{"11","فتح المعين بشرح قرة العين بمهمات الدين"},
{"12","إعانة الطالبين على حل ألفاظ فتح المعين"},
{"13","المقدمة الحضرمية"},
{"14","أسنى المطالب في شرح روض الطالب"},
{"15"," المنهاج القويم"},
{"16","تحفة المحتاج في شرح المنهاج"},
{"17","الإقناع في حل ألفاظ أبي شجاع"},
{"18","حاشيتا قليوبي وعميرة"},
{"19","فتح الوهاب بشرح منهج الطلاب"},
{"22","تفسر جلالين"},
{"20","حاشية البجيرمي على الخطيب"}

};
     
        HashMap<Integer,String> judulMap = new HashMap<>();
        
        for (int i = 0; i < judul.length; i++) {

            judulMap.put(Integer.parseInt(judul[i][0]), judul[i][1]);
            
        }
        
        return judulMap;
    } 
    
    public int[] getHalamanList(String idKitab) throws IOException, ParseException{
        Query query = new QueryParser(Version.LUCENE_34, "idKitab", new ArabicAnalyzer(Version.LUCENE_34)).parse(idKitab);
        IndexSearcher indexSearcher = new IndexSearcher(this.ir);
        
        int hits = 1000;
        TopScoreDocCollector collector = TopScoreDocCollector.create(hits, true);
        indexSearcher.search(query, collector);

        ScoreDoc[] topDocs = collector.topDocs().scoreDocs;
        int[] idHalaman = new int[topDocs.length];
        
        for (int i = 0; i < topDocs.length; i++) {
            int docId = topDocs[i].doc;
            Document document = this.ir.document(docId);
            idHalaman[i] = Integer.parseInt(document.get("halKitab"));
            
        }
        Arrays.sort(idHalaman);
        return idHalaman;
    }
    
    public String getNass(String idKitab,String halKitab){
        String nass = null;
        
        IndexSearcher indexSearcher = new IndexSearcher(this.ir);
        
        Query idKitabQuery = new TermQuery(new Term("idKitab", idKitab));
        Query halKitabQuery = new TermQuery(new Term("halKitab", halKitab));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(idKitabQuery, Occur.MUST);
        booleanQuery.add(halKitabQuery, Occur.MUST);
        
        
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
        try {
            indexSearcher.search(booleanQuery, collector);
        } catch (IOException ex) {
        }

        ScoreDoc[] topDocs = collector.topDocs().scoreDocs;
        int docId = topDocs[0].doc;
        Document document = null;
        try {
            document = this.ir.document(docId);
        } catch (CorruptIndexException ex) {
        } catch (IOException ex) {
        }
        
        nass = document.get("nass");
            
       
        return nass;
    }
    
    public String[] getDocumentTerms(String idKitab, String halKitab){
        
        IndexSearcher indexSearcher = new IndexSearcher(this.ir);
        
        Query idKitabQuery = new TermQuery(new Term("idKitab", idKitab));
        Query halKitabQuery = new TermQuery(new Term("halKitab", halKitab));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(idKitabQuery, Occur.MUST);
        booleanQuery.add(halKitabQuery, Occur.MUST);
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
        try {
            indexSearcher.search(booleanQuery, collector);
        } catch (IOException ex) {
        }

        ScoreDoc[] topDocs = collector.topDocs().scoreDocs;
        int docId = topDocs[0].doc;
        
        String[] terms = null;
        TermFreqVector vector = null;
        
        try {
            vector = this.ir.getTermFreqVector(docId, "nass");
        } catch (IOException ex) {
        }
        
        terms = vector.getTerms();
        
        return terms;
    }
}
