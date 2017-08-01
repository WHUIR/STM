/**
 *
 */
package Util.paper.index;

import Util.CMPUtil;
import Util.CollectionsUtil;
import Util.IOUtil;
import Util.KeyValueObj;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Chenliang Li [cllee@whu.edu.cn]
 */
public class PaperAbsIndexHandler {

    public static final String TITLE = "title";
    public static final String ABSTRACT = "abs";
    public static final String ID = "id";
    public static final String CATE = "category";
    private String indexPath;
    private static IndexReader indexReader;
    //  private FixedBitSet bits;
    private Bits searchAllBits;

    public PaperAbsIndexHandler(String indexPath) {
        this.indexPath = indexPath;
        try {
            indexReader =
                    DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
//      bits = new FixedBitSet(indexReader.numDocs());  
            searchAllBits = new Bits.MatchAllBits(indexReader.numDocs());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Gets the number of documents indexed
     *
     * @return
     */
    public int getNumDocs() {
        return indexReader.numDocs();
    }

    public void close() {
        try {
            indexReader.close();
        } catch (Exception ignore) {
        }
    }

    public static IndexReader getIndexReader() {
        return indexReader;
    }

    /**
     * Gets the term vector for the document specified
     * by <code>id</code>
     *
     * @param id
     * @return
     */
    public Map<String, Integer> getTermVector(int id) {
        Map<String, Integer> tfMap = new HashMap<String, Integer>();

        // If we want to get a term vector of a document,
        // we need to get this document's index (i.e. DocsEnum.nextDoc()).
        // So we need assign each document a primary key field, and
        // this field must be indexed by Lucene.
        Term term = new Term(ID, String.valueOf(id));
        try {
            PostingsEnum docEnum = MultiFields.getTermDocsEnum(
                    indexReader, searchAllBits,
                    ID, term.bytes());
            int doc = DocsEnum.NO_MORE_DOCS;
            BytesRef ref = null;
            String termText = null;
            DocsEnum reusableDocEnum = null;
            while ((doc = docEnum.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
                Terms terms = indexReader.getTermVector(doc, ABSTRACT);
                //  if(terms==null){
                //   System.out.println(indexReader.getTermVector(doc,TITLE));
                //  }
                TermsEnum termsEnum = null;
                try {
                    termsEnum = terms.iterator();
                } catch (Exception e) {

                }
                if (termsEnum == null)
                    continue;
                // iterate each term for obtaining its tf value
                while ((ref = termsEnum.next()) != null) {
                    termText = ref.utf8ToString();


                    // here, since termsEnum is obtained from a single
                    // document, docFreq will be 1.
                    int docFreq = termsEnum.docFreq();

                    // here, since termsEnum is obtained from a single
                    // document, totalTermFreq() returns tf value.
                    int totalFreq = (int) termsEnum.totalTermFreq();
//          System.out.println(
//              String.format("%s, %d, %d", termText, docFreq, totalFreq));
                    tfMap.put(termText, totalFreq);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tfMap;
    }

    public Map<Integer, String> getIDToTitleMap() {
        Map<Integer, String> idTitleMap = new HashMap<Integer, String>();
        int max = indexReader.numDocs();
        try {
            for (int i = 0; i < max; i++) {
                Document doc = indexReader.document(i);
                String title = doc.get(TITLE);
                String id = doc.get(ID);
                idTitleMap.put(Integer.parseInt(id), title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idTitleMap;
    }

    public String getIDToTitle(int id) throws IOException {

        return indexReader.document(id).get(TITLE);
    }

    protected Set<String> getAllTerms(TermsEnum termsEnum) {
        Set<String> terms = new HashSet<String>();
        try {
            BytesRef ref = null;
            while ((ref = termsEnum.next()) != null) {
                terms.add(ref.utf8ToString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return terms;
    }

    /**
     * Gets a map of term-df pairs for each term from
     * the filed {@link #ABSTRACT}. df refers to document_frequency.
     *
     * @return
     */
    public Map<String, Integer> getTermDFMap() {
        Map<String, Integer> dfMap = new HashMap<String, Integer>();
        try {
            Terms terms = MultiFields.getTerms(indexReader, ABSTRACT);
            TermsEnum termsEnum = terms.iterator();
            BytesRef ref = null;
            while ((ref = termsEnum.next()) != null) {
                dfMap.put(ref.utf8ToString(), termsEnum.docFreq());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dfMap;
    }


    /**
     * Gets a map of term-cf pairs for each term from
     * the filed {@link #ABSTRACT}. cf refers to collection_frequency.
     *
     * @return
     */
    public Map<String, Integer> getTermCFMap() {
        Map<String, Integer> cfMap = new HashMap<String, Integer>();
        try {
            Terms terms = MultiFields.getTerms(indexReader, ABSTRACT);
            TermsEnum termEnum = terms.iterator();
            BytesRef ref = null;
            while ((ref = termEnum.next()) != null) {
                cfMap.put(ref.utf8ToString(), (int) termEnum.totalTermFreq());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cfMap;
    }

    /**
     * Get all terms indexed for the field {@link #ABSTRACT}
     *
     * @return
     */
    public Set<String> termDictionary() {
        Set<String> dic = new HashSet<String>();
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated macroF1 stub
        testCase();
    }

    public static void testCase() {
        // this is the directory we used for indexing
        // in PaperAbsIndexWriter.java
        String indexPath = "20_newsgro_AbsIndex_exam/alt.atheism";
        PaperAbsIndexHandler indexHandler =
                new PaperAbsIndexHandler(indexPath);
        Map<Integer, String> idTitleMap =
                indexHandler.getIDToTitleMap();
        for (Integer id : idTitleMap.keySet()) {
            String title = idTitleMap.get(id);
            System.out.println(id + " : " + title);
        }

        int id = 2;
        Map<String, Integer> tfMap = indexHandler.getTermVector(id);
        for (String term : tfMap.keySet()) {
            int tf = tfMap.get(term);
            System.out.println(String.format("%s : %d", term, tf));
        }

        Map<String, Integer> dfMap = indexHandler.getTermDFMap();
        for (String term : dfMap.keySet()) {
            int df = dfMap.get(term);
            System.out.println(String.format("%s : %d", term, df));
        }

        Map<String, Integer> cfMap = indexHandler.getTermCFMap();
        for (String term : cfMap.keySet()) {
            int cf = cfMap.get(term);
            System.out.println(String.format("%s : %d", term, cf));
        }
    
    /* Let's sort the terms in dictionary by their df / cf
     * in decreasing order.
     * 
     * */
        List<KeyValueObj<Integer, String>> sortlist =
                CollectionsUtil.sort(dfMap, CMPUtil.INT_DESC);
        try {
            PrintWriter out = new PrintWriter("TermDFMap.txt");
            for (int i = 0; i < sortlist.size(); i++) {
                KeyValueObj<Integer, String> kvo =
                        sortlist.get(i);
                int df = kvo.getKey();
                String term = kvo.getValue();
                out.print(term);
                out.print(',');
                out.print(df);
                out.println();
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        sortlist =
                CollectionsUtil.sort(cfMap, CMPUtil.INT_DESC);
        try {
            PrintWriter out = new PrintWriter("TermCFMap.txt");
            for (int i = 0; i < sortlist.size(); i++) {
                KeyValueObj<Integer, String> kvo =
                        sortlist.get(i);
                int cf = kvo.getKey();
                String term = kvo.getValue();
                out.print(term);
                out.print(',');
                out.print(cf);
                out.println();
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    
    /* Sometimes, we would like to output a .csv file that
     * contain both df/cf information. By doing this, we can
     * manually examine the terms of high frequency.
     * */
        try {
            PrintWriter out = new PrintWriter("TermFreqMap.csv");
            for (int i = 0; i < sortlist.size(); i++) {
                KeyValueObj<Integer, String> kvo =
                        sortlist.get(i);
                int cf = kvo.getKey();
                String term = kvo.getValue();

                int df = dfMap.get(term);
                out.print(term);
                out.print(',');
                out.print(df);
                out.print(',');
                out.print(cf);
                out.println();
            }

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        /**
         * Of course, we can load these term-frequency map from
         * a file. The file should contain each pair in one row, and
         * the term and int value are separated by ','.
         */
        dfMap = IOUtil.readStringToIntegerMap(
                "TermDFMap.txt", "utf8");
        for (String term : dfMap.keySet()) {
            int df = dfMap.get(term);
            System.out.println(String.format("%s : %d", term, df));
        }
    }
}
