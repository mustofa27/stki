/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir.process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.lucene.analysis.ar.ArabicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Indra
 */
public class FileIndexer {
    
    private IndexWriter writer;

    public FileIndexer(String indexdir) {

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
    
    public void indexOneFile(String path, String[] identitasKitab) throws CorruptIndexException, IOException{
        File f = new File(path);
        
        HashMap<Integer,String> judulKitabList = this.getJudul();
        String judulKitab = null;

        TextFileReader textReader = new TextFileReader(f.getAbsolutePath());
        String nass = null;
        try {
            nass = textReader.Read();
        } catch (IOException ex) {
        }
        
            Document document = new Document();


            judulKitab = judulKitabList.get(Integer.parseInt(identitasKitab[0]));

            if(judulKitab==null)
                judulKitab = identitasKitab[0];

            Field fieldIdKitab = new Field ("idKitab",identitasKitab[0], Field.Store.YES, Field.Index.NOT_ANALYZED);
            Field fieldHalKitab = new Field ("halKitab",identitasKitab[1], Field.Store.YES, Field.Index.NOT_ANALYZED);
            Field fieldJudulKitab = new Field ("judulKitab",judulKitab, Field.Store.YES, Field.Index.NOT_ANALYZED);
            Field fieldNass = new Field("nass", nass, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            Field fieldPath = new Field("path", f.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED);

            document.add(fieldIdKitab);
            document.add(fieldHalKitab);
            document.add(fieldJudulKitab);
            document.add(fieldNass);
            document.add(fieldPath);

            this.writer.addDocument(document);
            System.out.println("Index file: " + f.getAbsolutePath());
        
    }
    
    public void indexFileOrDirectory(String path) {
        ArrayList<File> queue = this.listFiles(new File(path));
        
        int originalNumDocs = 0;
        try {
            originalNumDocs = this.writer.numDocs();
        } catch (IOException ex) {
        }
        for (File f : queue) {
            
            String filename = f.getName().replaceFirst(".txt", "");
            String[] identitasKitab = filename.split("_");
            HashMap<Integer,String> judulKitabList = this.getJudul();
            String judulKitab = null;
            
            TextFileReader textReader = new TextFileReader(f.getAbsolutePath());
            String nass = null;
            try {
                nass = textReader.Read();
            } catch (IOException ex) {
            }
            try {
                Document document = new Document();
           
                
                judulKitab = judulKitabList.get(Integer.parseInt(identitasKitab[0]));
                     
              
                Field fieldIdKitab = new Field ("idKitab",identitasKitab[0], Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldHalKitab = new Field ("halKitab",identitasKitab[1], Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldJudulKitab = new Field ("judulKitab",judulKitab, Field.Store.YES, Field.Index.NOT_ANALYZED);
                Field fieldNass = new Field("nass", nass, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
                Field fieldPath = new Field("path", f.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED);
                
                document.add(fieldIdKitab);
                document.add(fieldHalKitab);
                document.add(fieldJudulKitab);
                document.add(fieldNass);
                document.add(fieldPath);
                
                this.writer.addDocument(document);
                System.out.println("Index file: " + f.getAbsolutePath());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Tidak bisa meng-index: " + f.getAbsolutePath());
            }
            
        }//end for
        
        int indexedNumDocs = 0;
        try {
            indexedNumDocs = this.writer.numDocs();
        } catch (IOException ex) {
        }
        
        System.out.println("Mengindex: " + (indexedNumDocs-originalNumDocs) + " dokumen");
        
    }
    
    private ArrayList<File> listFiles(File file){
        ArrayList<File> queue = new ArrayList<>();
        
        //Jida file tidak ada keluarkan peringatan
        if (!file.exists()) {
            System.out.println("File/Folder: " + file.getName() + " tidak ada");
        }
        
        //jika direktory maka rekursi, jika file maka index
        if (file.isDirectory()) {
            
            for (File f : file.listFiles()) {
                queue.addAll(this.listFiles(f));
            }
            
        } else {
            String filename = file.getName().toLowerCase();
            
            if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith("txt")) {
                queue.add(file);
            } else {
                System.out.println("File: " +filename+ " dilewati");
            }
            
        }
        
        return queue;
    }
    public HashMap getJudul(){
    
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
{"20","حاشية البجيرمي على الخطيب"}
};
     
        HashMap<Integer,String> judulMap = new HashMap<>();
        
        for (int i = 0; i < judul.length; i++) {

            judulMap.put(Integer.parseInt(judul[i][0]), judul[i][1]);
            
        }
        
        return judulMap;
    }    
    public void closeIndex() throws IOException{
        this.writer.optimize();
        this.writer.close();
    }
    
}
