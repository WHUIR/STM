
import Util.Dictionary;
import Util.paper.index.PaperAbsIndexHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by XJ on 2016/8/5.
 */
public class Co_occurrence implements Decorator {

    private static HashMap<String, HashSet<String>> word_doc;
    private static PaperAbsIndexHandler handler;
    private SModel model;

    public Co_occurrence(SModel model) {
        this.model = model;
    }
    protected void initwcRelation() {
        double[] sum = new double[model.cateNum];

        double thr = (double) 1 / model.cateNum;

        double temp;
        for (int i = 0; i < model.wordNum; i++) {
            for (int c = 0; c < model.cateNum; c++) {
                model.tao[i][c] = Co_occurrence.catecoRelation(model.seedSet[c], Dictionary.getWord(i));
                sum[c] += model.tao[i][c];
            }
        }


        for (int i = 0; i < model.wordNum; i++) {
            temp = 0;
            for (int c = 0; c < model.cateNum; c++) {
                model.tao[i][c] /= sum[c];
                temp += model.tao[i][c];
            }

            if (temp == 0)
                for (int c = 0; c < model.cateNum; c++)
                    model.tao[i][c] = (double) 1 / Math.pow(model.cateNum, 2);
            else
                for (int c = 0; c < model.cateNum; c++) {
                    model.tao[i][c] /= temp;
                    model.tao[i][c] = model.tao[i][c] - thr > 0 ? model.tao[i][c] - thr : 0;
                }
        }
    }
    protected void initCoo() {
        try {
            handler = new PaperAbsIndexHandler(model.luceneIndexPath);
            word_doc = new HashMap<>();
        } catch (Exception e) {
            System.out.println("lucene Index Path is illegal");
            e.printStackTrace();
            System.exit(-1);
        }

        Set<String> termVec;
        Set<String> word_docList;
        Map<String, Integer> map;
        try {
            for (int i = 0; i < model.docNum; i++) {

                map = handler.getTermVector(i);
                if (map == null)
                    continue;
                else termVec = map.keySet();
                for (String s : termVec) {
                    if (!word_doc.containsKey(s))
                        word_doc.put(s, new HashSet<>());
                    word_docList = word_doc.get(s);
                    word_docList.add(handler.getIDToTitle(i));
                }
            }
        } catch (Exception e) {
          //  e.printStackTrace();
        }
    }

    private static int intersection(String seedWord, String word) {
        HashSet<String> l, s;
        if (!word_doc.containsKey(seedWord)) {
            return 0;
        }
        if (!word_doc.containsKey(word)) {
            //  System.out.println(word);
            return 0;
        }
        int re = 0;
        if (word_doc.get(word).size() > word_doc.get(seedWord).size()) {
            l = word_doc.get(word);
            s = word_doc.get(seedWord);
        } else {
            l = word_doc.get(seedWord);
            s = word_doc.get(word);
        }
        for (String title : s) {
            if (l.contains(title))
                re++;
        }
        return re;
    }

    private static double is_un(String seedWord, String word) {
        if (intersection(seedWord, word) == 0)
            return 0;
        double re = (double) intersection(seedWord, word) / word_doc.get(seedWord).size();
        return re;
    }

    //provide the
    public static double catecoRelation(HashSet<String> seedset, String word) {
        double re = 0;
        for (String s : seedset) {
            re += is_un(s, word);
        }
        if (re == 0)
            return 0;
        re /= seedset.size();
        return re;
    }

    @Override
    public SModel decorateSModel() {
        System.out.println("calculate co-occurrence...");
        initCoo();
        initwcRelation();
        return model;
    }
}
