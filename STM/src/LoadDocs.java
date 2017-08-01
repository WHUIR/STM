import Util.Dictionary;
import Util.LuceneHandler;
import Util.MathUtil;
import Util.Pair;
import org.apache.lucene.document.Document;

import java.util.ArrayList;

/**
 * Created by XJ on 2016/3/10.
 */
public class LoadDocs implements Decorator {

    private SModel model;
    private float soomth;

    public LoadDocs(SModel model) {
        this.model = model;
        soomth = (float) 1 / model.cateNum;
    }

    private void load() {
        LuceneHandler lh = new LuceneHandler(model.luceneIndexPath);
        Document[] lucDocs = lh.getDocs();

        int len = model.docNum;
        if (len == 0) {
            System.out.println("there is no document in the path");
            System.exit(-1);
        }
        SDocument sDoc;
        Document luceneDoc;

        for (int i = 0; i < len; i++) {
            model.documents[i] = new SDocument();
            luceneDoc = lucDocs[i];
            sDoc = model.documents[i];
            sDoc.index = i;
            sDoc.scores = new double[model.cateNum];
            sDoc.title = luceneDoc.get(LuceneHandler.TITLE);
            sDoc.groundTruth = Integer.parseInt(luceneDoc.get(LuceneHandler.CATE));
            sDoc.check = Integer.parseInt(luceneDoc.get(LuceneHandler.CHECK)) == 1 ? true : false;
            sDoc.xvec = new ArrayList<>();
            if (sDoc.check)
                model.groundTruth[sDoc.groundTruth]++;
            initCont(luceneDoc.get(LuceneHandler.ABSTRACT), sDoc);
        }
    }

    private void initCont(String cont, SDocument sDoc) {

        String[] ves = cont.split(" ");
        double[] re = new double[model.cateNum];

        for (String s : ves)
            for (int cate = 0; cate < model.cateNum; cate++)
                if (model.seedSet[cate].contains(s))
                    re[cate]++;
        initEta(re, sDoc.index);
        sDoc.prediction = MathUtil.sample(model.eta[sDoc.index]);
        int word, topic;

        for (String s : ves) {
            word = Dictionary.contains(s);
            if (word == -1)
                continue;
            if (model.seedSet[sDoc.prediction].contains(Dictionary.getWord(word)) && Math.random() < 0.5 || Math.random() < (model.rho)) {
                sDoc.xvec.add(0);
                model.docX[sDoc.index][0]++;
                sDoc.contents.add(new Pair(word, -1));
                model.categoryWord[sDoc.prediction][word]++;
                model.numCWords4Cate[sDoc.prediction]++;
            } else {
                model.docX[sDoc.index][1]++;
                sDoc.xvec.add(1);
                topic = (int) (Math.random() * model.topicNum);
                sDoc.contents.add(new Pair(word, topic));
                model.categoryTopic[sDoc.prediction][topic]++;
                model.docTopic[sDoc.index][topic]++;
                model.wordTopic[word][topic]++;
                model.numRWords4Cate[sDoc.prediction]++;
                model.numRWords4Topic[topic]++;
            }
        }
    }

    private void initEta(double[] raw, int index) {
        int sum = 0;

        for (int i = 0; i < raw.length; i++)
            raw[i] = Math.log(1 + raw[i]);

        for (double d : raw)
            sum += d;
        if (sum == 0) {
            for (int i = 0; i < raw.length; i++)
                raw[i] = soomth;
        } else {
            for (int i = 0; i < raw.length; i++)
                raw[i] = raw[i] / sum;
        }
        model.eta[index] = raw;
    }

    private void initPhi() {
        for (int c = 0; c < model.cateNum; c++)
            for (int k = 0; k < model.topicNum; k++) {
                model.phi[c][k] = (model.categoryTopic[c][k] + model.alpha0) / (model.numRWords4Cate[c] + model.topicNum * model.alpha0);
                model.phi[c][k] *= model.alpha1;
                model.phiSum[c] += model.phi[c][k];
            }
    }

    @Override
    public SModel decorateSModel() {
        System.out.println("loading documents...");
        load();
        initPhi();
        return model;
    }
}
