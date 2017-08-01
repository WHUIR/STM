package Util;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by XJ on 2016/3/10.
 */
public class LuceneHandler {

    public static final String TITLE = "title";
    public static final String ABSTRACT = "abs";
    public static final String ID = "id";
    public static final String CATE = "category";
    public static final String CHECK = "check";
    private String indexPath;
    private static IndexReader indexReader;


    public static void main(String[] args) {
        demo_readDoc();
    }

    private static void demo_readDoc() {
        LuceneHandler lh = new LuceneHandler("D:\\dataset\\20ng_TLC_luceneIndex\\pc-mac");
        Document doc = lh.getDoc(0);
        System.out.println(doc.get(LuceneHandler.TITLE));
        System.out.println(doc.get(Util.LuceneHandler.CATE));
        System.out.println(doc.get(Util.LuceneHandler.CHECK));
        System.out.println(doc.get(Util.LuceneHandler.ABSTRACT));
        System.out.println();
    }


    public LuceneHandler(String indexPath) {
        this.indexPath = indexPath;
        try {
            indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public int getNumDocs() {
        return indexReader.numDocs();
    }

    public int getNumWords() {

        return getWordSet().size();
    }

    public Document[] getDocs() {
        int docsNum = indexReader.numDocs();
        Document[] documents = new Document[docsNum];
        try {
            for (int i = 0; i < docsNum; i++)
                documents[i] = indexReader.document(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }


    public Document getDoc(int id) {
        Document doc = null;
        try {
            doc = indexReader.document(id);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return doc;
    }


    public Set<String> getWordSet() {
        Set<String> dic = new HashSet<>();
        try {
            Terms terms = MultiFields.getTerms(indexReader, ABSTRACT);
            TermsEnum termEnum = terms.iterator();
            BytesRef ref = null;
            while ((ref = termEnum.next()) != null) {
                dic.add(ref.utf8ToString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dic;
    }

}
