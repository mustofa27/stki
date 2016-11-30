/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.IOException;

/**
 *
 * @author Indra
 */
public class Indexing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        DatabaseIndexer indexer = null;
        try {
            indexer = new DatabaseIndexer("FileIndex");
        } catch (IOException ex) {
            System.out.println("Tidak bisa Index");
        }
        indexer.setConnection("localhost", "tafsirjalalain", "root", "");
        indexer.indexDatabaseRow();

        FileIndexer fileIndexer = new FileIndexer("FileIndex");
        fileIndexer.indexFileOrDirectory("data");
        fileIndexer.closeIndex();

    }
}
