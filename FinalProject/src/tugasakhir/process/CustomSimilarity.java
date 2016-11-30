/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Indra
 */
public class CustomSimilarity extends Similarity {
    private int docId;
    

    @Override
    public float computeNorm(String field, FieldInvertState state) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public float queryNorm(float sumOfSquaredWeights) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public float sloppyFreq(int distance) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public float tf(float freq) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public float idf(int docFreq, int numDocs) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public float coord(int overlap, int maxOverlap) {
        throw new UnsupportedOperationException("");
        
    }
    
    public float phraseWeighting(String query, int hits) throws CorruptIndexException, IOException{
        float weight = 0;
        TermFreqVector vector = null;
        Directory indexDirectory = FSDirectory.open(new File("FileIndex"));
        IndexReader ir = IndexReader.open(indexDirectory);
        try {
            vector = ir.getTermFreqVector(docId, query);
        } catch (IOException ex) {
        }
        
        weight = (float) Math.sqrt((this.sigmaOccurance(query)/hits)/(vector.size()/ir.maxDoc()));
        
        return weight;
    }
    
    public float sigmaOccurance(String query) throws CorruptIndexException, IOException{
        float occurance = 0; 
        TermFreqVector vector = null;
        Directory indexDirectory = FSDirectory.open(new File("FileIndex"));
        IndexReader ir = IndexReader.open(indexDirectory);
        try {
            vector = ir.getTermFreqVector(docId, query);
        } catch (IOException ex) {
        }
        
        int [] terms = vector.getTermFrequencies();
        
        for (int i = 0; i < terms.length; i++) {
            if(terms[i]>0)
                occurance += terms[i];
        }
        return occurance;
    }

   
    public ScoreDoc[] decode(String query, ScoreDoc[] score) {
        try {
            for (int i = 0; i < score.length; i++) {
                ScoreDoc scoreDoc = score[i];
                if (scoreDoc.toString().length() > 1) {
                    scoreDoc.score = scoreDoc.score*this.phraseWeighting(query, score.length);
                }
            }
        } catch (CorruptIndexException ex) {
        } catch (IOException ex) {
        }
        
        return score;
    }
    
    
}
