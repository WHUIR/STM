
import java.util.HashMap;

/**
 * Created by XJ on 2015/12/23.
 */
public class DocMirroe {
    protected static HashMap<Integer, Integer> cwordList;
    protected static HashMap<Integer, Integer> rwordList;
    protected static int[][] topic_word_;
    protected static int[] categoryTopic_word_;
    protected static int[] topicSum;
    // protected static int[] inner_topicSum;
    protected static int rwordSum, cwordSum;


    static void init(int numtopic, int numword) {
        topicSum = new int[numtopic];
        topic_word_ = new int[numtopic][numword];
        categoryTopic_word_ = new int[numword];
        cwordList = new HashMap<>();
        rwordList = new HashMap<>();
        cwordSum = 0;
        rwordSum = 0;
    }


    static void mirror(int[] xmirror, int[] zmirror, int[] wmirror,int len) {
        int word, topic, x;
        for (int i = 0; i < len; i++) {
            word = wmirror[i];
            topic = zmirror[i];
            x = xmirror[i];

            if (x == 0) {
                if (cwordList.containsKey(word))
                    cwordList.put(word, cwordList.get(word) + 1);
                else
                    cwordList.put(word, 1);
                categoryTopic_word_[word]++;
                cwordSum++;
            } else {
                if (rwordList.containsKey(word))
                    rwordList.put(word, rwordList.get(word) + 1);
                else rwordList.put(word, 1);
                topicSum[topic]++;
                topic_word_[topic][word]++;
                rwordSum++;
            }
        }
    }

    static void clean(int[] xmirror, int[] zmirror, int[] wmirror,int len) {

        int word, x, topic;
        for (int i = 0; i < len; i++) {
            word = wmirror[i];
            topic = zmirror[i];
            x = xmirror[i];

            if (x == 0) {
                cwordList.remove(word);
                categoryTopic_word_[word] = 0;
            } else {
                rwordList.remove(word);
                topic_word_[topic][word] = 0;
            }
        }

        for (int i = 0; i < topicSum.length; i++)
            topicSum[i] = 0;

        cwordSum = 0;
        rwordSum = 0;
    }

}
