/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Indra
 */
public class Searcher {

    IndexReader indexReader;
    TopDocs top = null;
    Query q = null;
    public ScoreDoc[] topDocs;
    public Searcher(String indexPath) throws IOException {
        Directory index = FSDirectory.open(new File(indexPath));
        this.indexReader = IndexReader.open(index);
    }
    
    public String[][] search(String q) throws ParseException, IOException{
        
        Query query = new QueryParser(Version.LUCENE_34, "nass", new ArabicAnalyzer(Version.LUCENE_34)).parse(q);
//        this.q = query;
        
        String[] queryWords = q.split(" ");
        PhraseProcesser phrase = new PhraseProcesser();
        phrase.setSlop(0);
        
        for (String string : queryWords) {
            phrase.add(new Term("nass",string));
        }
        
        IndexSearcher indexSearcher = new IndexSearcher(this.indexReader);
        
        int hits = 20;
        TopScoreDocCollector collector = TopScoreDocCollector.create(hits, true);
        
        //!!!
        //indexSearcher.setSimilarity(new CustomSimilarity());
        indexSearcher.search(query, collector);
//        this.top = collector.topDocs();

        topDocs = collector.topDocs().scoreDocs;
        indexSearcher.setSimilarity(new CustomSimilarity());
        
        String[][] hasil = new String[topDocs.length][5];
        for (int i = 0; i < topDocs.length; i++) {
            int docId = topDocs[i].doc;
            Document document = this.indexReader.document(docId);
            
            hasil[i][0] = String.valueOf(i+1);
            hasil[i][1] = document.get("idKitab");
            hasil[i][2] = document.get("halKitab");
            hasil[i][3] = document.get("nass");
            hasil[i][4] = String.valueOf(topDocs[i].score);            
        }
        return hasil;
        
    }
    
    public void getHighlight() throws IOException, InvalidTokenOffsetsException{
        
        ScoreDoc[] topDocs = this.top.scoreDocs;
        String[][] hasil = new String[topDocs.length][5];
        
        for (int i = 0; i < topDocs.length; i++) {
            int docId = topDocs[i].doc;
            Document document = this.indexReader.document(docId);
            
            
            String text = document.get("nass");
            Analyzer  analyzer = new ArabicAnalyzer(Version.LUCENE_34);
            CachingTokenFilter tokenStream = new CachingTokenFilter(analyzer.tokenStream("nass", new StringReader(text)));
            Highlighter highlighter = new Highlighter(new QueryScorer(q, indexReader, "nass"));
            highlighter.setTextFragmenter(new SimpleFragmenter(40));
            String result = highlighter.getBestFragments(tokenStream, text, 10, "..");
            System.out.println("\t" + result);
        }
    }
    
}
