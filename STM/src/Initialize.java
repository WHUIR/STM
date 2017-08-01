
import Util.Dictionary;
import Util.LuceneHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by XJ on 2016/3/11.
 */
public class Initialize implements Decorator {

    private SModel model;
    private LuceneHandler luceneHandler;

    public Initialize(SModel model) {
        this.model = model;
        try {
            luceneHandler = new LuceneHandler(model.luceneIndexPath);
        } catch (Exception e) {
            System.out.println("the lucene index path is invalid");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    protected void initVariate() {

       // model.wordNum=luceneHandler.getNumWords();
        model.docNum = luceneHandler.getNumDocs();
        model.phi = new double[model.cateNum][model.topicNum];
        model.phiSum = new double[model.cateNum];
        model.documents = new SDocument[model.docNum];
        model.numRWords4Topic = new int[model.topicNum];
        model.numRWords4Cate = new int[model.cateNum];
        model.numCWords4Cate = new int[model.cateNum];
        model.categoryWord = new int[model.cateNum][model.wordNum];
        model.docTopic = new int[model.docNum][model.topicNum];
        model.wordTopic = new int[model.wordNum][model.topicNum];
        model.categoryTopic = new int[model.cateNum][model.topicNum];
        model.docX = new int[model.docNum][2];
        model.eta = new double[model.docNum][model.cateNum];
        model.numTestSet = 0;
        model.corpusLength = 0;
        model.groundTruth = new int[model.cateNum];
        model.tao = new double[model.wordNum][model.cateNum];
        model.temp = new double[1 + model.topicNum];
        model.seedSet = new HashSet[model.cateNum];

        for (int i = 0; i < model.cateNum; i++)
            model.seedSet[i] = new HashSet<>();


    }

    protected void initSeedSet() {
        String line;
        int cate = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(model.seedwordPath));
        } catch (Exception e) {
            System.out.println("the seed word file path is illegal");
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            while ((line = br.readLine()) != null) {
                model.seedSet[cate] = new HashSet<>();
                String[] vecs = line.split(" ");
                for (String s : vecs)
                    model.seedSet[cate].add(s);
                cate++;
            }
            br.close();
        } catch (Exception e) {
            System.out.println("the seed word file is null or ill-formed , please see the 'readme.txt' for more information");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void initDocStru() {
        DocMirroe.init(model.topicNum, model.wordNum);
    }

    private void initDictionary() {
        Dictionary.initDic();
        Set<String> sets = luceneHandler.getWordSet();
        model.wordNum = sets.size();
        for (String s : sets)
            Dictionary.add(s);
    }

    @Override
    public SModel decorateSModel() {
        initDictionary();
        initVariate();
        initSeedSet();
        initDocStru();
        return model;
    }
}
