/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Indra
 */
public class DatabaseIndexer {
    private IndexWriter writer;
    private Connection conn;

    public DatabaseIndexer(String indexdir) throws IOException {

        //Menentukan versi lucene dan analyzer yang digunakan
        Version version = Version.LUCENE_34;
        ArabicAnalyzer arabicAnalyzer = new ArabicAnalyzer(version);
        IndexWriterConfig config = new IndexWriterConfig(version, arabicAnalyzer);
       
        //Membuat index di folder "indexdir"
        FSDirectory directory = null;
        try {
            directory = FSDirectory.open(new File(indexdir));
            this.writer = new IndexWriter(directory, config);
        } catch (IOException ex) {
            System.out.println("Tidak bisa membuat index di folder: " + indexdir);
        }
        
    }
    
    public void setConnection(String ip, String database, String username, String password) {
		
		
		String Driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException ce) {
			// if the class driver not found
			System.out.println(ce.getMessage());
		}
		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+ database ,username, password);
		} catch (SQLException se) {
			// get the sql error message
			System.out.println(se.getMessage());
		}
		
	}
    
    private ArrayList<String[]> getNass(String sqlQuery){

        ArrayList<String[]> nass = new ArrayList<>();
        try {
            
            Statement stmt = this.conn.createStatement();
            this.conn.setAutoCommit(false);            
            ResultSet rs = stmt.executeQuery(sqlQuery);
            
            while(rs.next()){
                String nassKitab = rs.getString(1);
                String idNass = rs.getString(2);
                nass.add(new String []{nassKitab, idNass});
            }
            
        } catch (SQLException ex) {
            System.out.println("Tidak bisa membaca query" + sqlQuery);
        }
        
        return nass;
    }
   
    
    
    public void indexDatabaseRow() throws IOException{
        String nassQuery = "SELECT `nass`,`id` FROM `table 3`";

        String idKitab = "22";
        String judulKitab = "تفسير الجلالين";
        ArrayList<String[]> nassList = this.getNass(nassQuery);
        
        int originalNumDocs = this.writer.numDocs();
        
        for (Iterator<String[]> it = nassList.iterator(); it.hasNext();) {
            String[] nassString = it.next();
            
            if (nassString[0].length() > 3) {
                
            try {
                Document document = new Document();

                Field fieldIdKitab = new Field ("idKitab",idKitab, Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldJudulKitab = new Field ("judulKitab",judulKitab, Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldHalKitab = new Field ("halKitab",nassString[1], Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldNass = new Field("nass", nassString[0], Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
                Field fieldPath = new Field("path", "Database", Field.Store.YES, Field.Index.NOT_ANALYZED);

                document.add(fieldIdKitab);
                document.add(fieldJudulKitab);
                document.add(fieldHalKitab);
                document.add(fieldNass);
                document.add(fieldPath);

                
                this.writer.addDocument(document);
                System.out.println("Index file: " + nassString[1]);
            } catch (Exception e) {
                System.out.println("Tidak bisa meng-index: kitab" + judulKitab +" hal"+ nassString[1]);
            }
            }
            
        }
        
        int indexedNumDocs = this.writer.numDocs();
        this.closeIndex();
        System.out.println("Mengindex: " + (indexedNumDocs-originalNumDocs) + " dokumen");
        
    }

    
    private void closeIndex() throws IOException{
        this.writer.optimize();
        this.writer.close();
    }
    
}
