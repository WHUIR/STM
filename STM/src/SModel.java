
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by XJ on 2016/3/10.
 */
public class SModel {
    protected int topicNum;
    protected int cateNum;
    protected int docNum;
    protected int wordNum;
    protected int numTestSet;
    protected int corpusLength;
    protected int maxDocLen;
    protected int inter;
    protected int report; // 0 = accuracy ; 1 = f1
    protected float rho;
    protected float alpha0;
    protected float alpha1;
    protected float beta0;
    protected float beta1;

    protected String seedwordPath;
    protected String testSetPath;
    protected String trainSetPath;
    protected String luceneIndexPath;
    protected String catalogPath;

    protected int[][] wordTopic; //int[word][topic]
    protected int[][] docTopic; //int[doc][topic]
    protected int[][] categoryWord; //int[category][word]
    protected int[][] categoryTopic; //int[category][topic]
    protected int[][] docX; //int[doc][x]

    protected int[] groundTruth; //int[category]
    protected int[] numRWords4Topic; //int[topic];  number of regular words for one topic
    protected int[] numCWords4Cate; //int[category]; the number of category words for one category
    protected int[] numRWords4Cate; //int[category]

    protected double[][] phi; //double[category][topic]
    protected double[][] eta; //double[doc][category]
    protected double[][] tao; //double[word][category]; the relevance weight between word w and category

    protected double[] phiSum;
    protected double[] temp;

    protected SDocument[] documents;
    protected HashSet<String>[] seedSet;

}
