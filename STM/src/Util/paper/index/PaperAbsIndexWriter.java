/**
 *
 */
package Util.paper.index;

import Util.IOUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 */
public class PaperAbsIndexWriter {

    public static final String TITLE = "title";
    public static final String ABSTRACT = "abs";
    public static final String ID = "id";
    public static final String CATEGORY = "category";
    public static final String CHECK = "check";//check=true时表示测试集，参加最后的acc计算


    private static final String SUFFIX = ".txt";


    /**
     * Create a new index under the specified indexPath for
     * the abstracts from the specified directory absPath.
     * Note: the old index files with the same name will be removed.
     *
     * @param absPath
     * @param indexPath
     */
    public static int startIndex(String absPath, String indexPath) {
        return startIndex(absPath, indexPath, 0, true);
    }


    public static int startIndex(String absPath, String indexPath, int cate, boolean createNew) {
        return startIndex(absPath, indexPath, cate, createNew, false);
    }

    public static int startIndex(String absPath, String indexPath, int cate, boolean createNew, boolean check) {
        int beginId = 0;
        try {
            List<String[]> paperList = loadAbs(absPath, cate);
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            // BufferedReader stopwordsReader = new BufferedReader(new FileReader("D:\\dataSet\\stopwords-1.txt"));
            Analyzer analyzer =
                    // new StandardAnalyzer(stopwordsReader);
                    new StandardAnalyzer();
//          new PorterStemAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if (createNew) {
                // Create a new index in the directory, removing any
                // previously indexed documents:
                iwc.setOpenMode(OpenMode.CREATE);

            } else {
                // Add new documents to an existing index:
                try {
                    IndexReader indexReader =
                            DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
                    iwc.setOpenMode(OpenMode.APPEND);
                    beginId = indexReader.numDocs();
                    // iwc.setOpenMode(OpenMode.APPEND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer.  But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);

            // make a new, empty document
            Document doc = new Document();
            Field titleField = new StoredField(TITLE, "");
            doc.add(titleField);

            // indexed, not tokenized, and stored.
            Field idField = new StringField(ID, "", Store.YES);
            doc.add(idField);

            Field cateField = new StringField(CATEGORY, String.valueOf(cate), Store.YES);
            doc.add(cateField);

            Field checkField = new IntField(CHECK, 0, Store.YES);
            doc.add(checkField);

            FieldType fieldTypeForAbs = new FieldType();
            fieldTypeForAbs.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            fieldTypeForAbs.setTokenized(true); // we need to tokenize the abstract
            fieldTypeForAbs.setStored(true); // we do not need to store the whole abstract

            // Yes, we always need term vectors for our research work.
            fieldTypeForAbs.setStoreTermVectors(true);
            // Sometimes, we need the token's positions for this field.
            fieldTypeForAbs.setStoreTermVectorPositions(true);
            fieldTypeForAbs.freeze(); // no further change can be made.
            Field absField = new Field(ABSTRACT, "", fieldTypeForAbs);
            doc.add(absField);

            int id = beginId;
            for (String[] record : paperList) {
                idField.setStringValue(String.valueOf(id));
                titleField.setStringValue(
                        record[0] == null ? "null" : record[0]);
                absField.setStringValue(
                        record[1] == null ? "null" : record[1]);
                checkField.setIntValue(check == true ? 1 : 0);
                // cateField.setStringValue(record[2]==null?"null":record[2]);

                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    // New index, so we just add the document (no old document can be there):
                    writer.addDocument(doc);
                } else {
                    // Existing index (an old copy of this document may have been indexed) so
                    // we use updateDocument instead to replace the old one matching the exact
                    // path, if present:
                    // System.out.println("updating " + record[0]);
                    //   writer.updateDocument(
                    //         new Term(ID, String.valueOf(id)), doc);
                    writer.addDocument(doc);

                }

                if (++id % 2 == 0) {
                    // with this information, we can guess how much time
                    // it cost to index the whole corpus
                    System.out.println(id + " have been indexed...");
                }

            }

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // ****** In our research work, the dataset is almost  *****
            //******* static, and we often index them only once. *****
            //******* So, we need turn this call on. ******
            writer.forceMerge(1);
            writer.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static int startIndex_random(String absPath, String indexPath, int cate, boolean createNew, boolean check, int num) {
        int beginId = 0;
        try {
            List<String[]> paperList = loadAbs_randomSelect(absPath, cate, num);
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            // BufferedReader stopwordsReader = new BufferedReader(new FileReader("D:\\dataSet\\stopwords-1.txt"));
            Analyzer analyzer =
                    // new StandardAnalyzer(stopwordsReader);
                    new StandardAnalyzer();
//          new PorterStemAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if (createNew) {
                // Create a new index in the directory, removing any
                // previously indexed documents:
                iwc.setOpenMode(OpenMode.CREATE);

            } else {
                // Add new documents to an existing index:
                try {
                    IndexReader indexReader =
                            DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
                    iwc.setOpenMode(OpenMode.APPEND);
                    beginId = indexReader.numDocs();
                    // iwc.setOpenMode(OpenMode.APPEND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            IndexWriter writer = new IndexWriter(dir, iwc);

            // make a new, empty document
            Document doc = new Document();
            Field titleField = new StoredField(TITLE, "");
            doc.add(titleField);

            // indexed, not tokenized, and stored.
            Field idField = new StringField(ID, "", Store.YES);
            doc.add(idField);

            Field cateField = new StringField(CATEGORY, String.valueOf(cate), Store.YES);
            doc.add(cateField);

            Field checkField = new IntField(CHECK, 0, Store.YES);
            doc.add(checkField);

            FieldType fieldTypeForAbs = new FieldType();
            fieldTypeForAbs.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            fieldTypeForAbs.setTokenized(true); // we need to tokenize the abstract
            fieldTypeForAbs.setStored(true); // we do not need to store the whole abstract

            fieldTypeForAbs.setStoreTermVectors(true);
            fieldTypeForAbs.setStoreTermVectorPositions(true);
            fieldTypeForAbs.freeze(); // no further change can be made.
            Field absField = new Field(ABSTRACT, "", fieldTypeForAbs);
            doc.add(absField);

            int id = beginId;
            for (String[] record : paperList) {
                idField.setStringValue(String.valueOf(id));
                titleField.setStringValue(
                        record[0] == null ? "null" : record[0]);
                absField.setStringValue(
                        record[1] == null ? "null" : record[1]);
                checkField.setIntValue(check == true ? 1 : 0);

                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    writer.addDocument(doc);
                } else {
                    writer.addDocument(doc);
                }
                ++id;
//                if (++id % 2 == 0) {
//                    System.out.println(id + " have been indexed...");
//                }
            }
            System.out.println(id + " have been indexed...");
            writer.forceMerge(1);
            writer.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * P
     * Load all title-abstract pairs from .txt files under the
     * specified directory absPath. The title is the file name,
     * the abstract is the contents of the file.
     *
     * @param absPath
     * @return A list of two-elements String[]:
     * [0]=title; [1]=abstract.
     * @throws IOException
     */
    public static List<String[]> loadAbs(String absPath, int cate) throws IOException {
        ArrayList<String[]> list = new ArrayList<String[]>();
        File file = new File(absPath);

        if (!file.exists() || !file.isDirectory()) {
               throw new IllegalArgumentException(
               "absPath must be a folder path");
        }
        File[] txtFiles = file.listFiles();

            for (File txtFile : txtFiles) {
                String title = txtFile.getName();
                // title = title.substring(0,
                //    title.length()-SUFFIX.length());
                String abs = IOUtil.getFileText(txtFile);
                String[] pair = new String[3];
                pair[0] = title;
                pair[1] = abs;
                pair[2] = String.valueOf(cate);
                list.add(pair);
            }

        return list;
    }

    //random select document form corpus
    public static List<String[]> loadAbs_randomSelect(String absPath, int cate, int num) throws IOException {
        ArrayList<String[]> list = new ArrayList<String[]>();
        File file = new File(absPath);
        int len;


        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException(
                    "absPath must be a folder path");
        }
        File[] txtFiles = file.listFiles();
        len = txtFiles.length;
        if (num > len || num == 0)
            return loadAbs(absPath, cate);

        TreeSet<Integer> set = new TreeSet<>();
        while (set.size() < num)
            set.add((int) (Math.random() * len));

        File f;
        for (Integer i : set) {
            f = txtFiles[i];
            String abs = IOUtil.getFileText(f);
            String title = f.getName();
            String[] pair = new String[3];
            pair[0] = title;
            pair[1] = abs;
            pair[2] = String.valueOf(cate);
            list.add(pair);
        }
        return list;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated macroF1 stub
        //loadAbs("abstracts");
        startIndex("20_newsgroup/alt.atheism", "20_newsgro_AbsIndex/alt.atheism");
    }

}
