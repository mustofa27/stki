/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.IOException;
import java.util.Set;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Weight;

/**
 *
 * @author Indra
 */
public class PhraseProcesser extends PhraseQuery{

    @Override
    public int[] getPositions() {
        return super.getPositions();
    }

    @Override
    public Weight createWeight(Searcher searcher) throws IOException {
        return super.createWeight(searcher);
    }

    @Override
    public void extractTerms(Set<Term> queryTerms) {
        super.extractTerms(queryTerms);
    }
    
    
}
