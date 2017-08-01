
import Util.MathUtil;
import Util.Pair;

/**
 * Created by XJ on 2016/8/5.
 */
public class Sampling {
	
	//sampling x and topic for words
    protected static void sampleXZ(int index, SModel model, int category, int[][] xmirror, int[][] zmirror) {
        SDocument doc = model.documents[index];
        int len = doc.contents.size();
        int x, word, topic, sampleRes;

        for (int i = 0; i < len; i++) {
            word = doc.contents.get(i).word;

            x = xmirror[category][i];
            topic = zmirror[category][i];

            if (x == 0) {
                //need rollback
                model.categoryWord[doc.prediction][word]--;
                model.numCWords4Cate[doc.prediction]--;
                model.docX[index][0]--;
            } else {
                //need rollback
                model.wordTopic[word][topic]--;
                model.numRWords4Topic[topic]--;
                model.docTopic[index][topic]--;
                model.docX[index][1]--;
            }

            kernel(model, index, category, word);
            sampleRes = MathUtil.sample(model.temp);

            if (sampleRes == 0) {
                xmirror[category][i] = 0;
                zmirror[category][i] = -1;
                model.categoryWord[category][word]++;
                model.numCWords4Cate[category]++;
                model.docX[index][0]++;
            } else {
                xmirror[category][i] = 1;
                zmirror[category][i] = sampleRes - 1;
                model.wordTopic[word][sampleRes - 1]++;
                model.numRWords4Topic[sampleRes - 1]++;
                model.docTopic[index][sampleRes - 1]++;
                model.docX[index][1]++;
            }
        }
    }
//sampling category label for doucments
    protected static double sampleC(int index, SModel model, int c, int[] xmirror, int[] zmirror, int[] wmirror) {

        double re;
        DocMirroe.mirror(xmirror, zmirror, wmirror,model.documents[index].contents.size());
        double part1 = 0, part2, part3, part4;
        double pp1 = 0, pp2 = 0;

        //part1
        for (int k = 0; k < model.topicNum; k++) {
            if (DocMirroe.topicSum[k] == 0)
                continue;
            pp1 = 0;
            int word;
            for (Integer i : DocMirroe.rwordList.keySet()) {
                word = i;
                if (DocMirroe.topic_word_[k][word] == 0)
                    continue;
                pp1 += logAccum(model.wordTopic[word][k] + model.beta1 - 1, model.wordTopic[word][k] + model.beta1 - DocMirroe.topic_word_[k][word]);
            }
            pp2 = logAccum(model.numRWords4Topic[k] + model.beta1 * model.wordNum - 1, model.numRWords4Topic[k] - DocMirroe.topicSum[k] + model.beta1 * model.wordNum);
            part1 += (pp1 - pp2);
        }

        //part2
        /**----------------------------------------------------*/
        pp1 = 0;
        for (int k = 0; k < model.topicNum; k++)
            pp1 += logAccum(DocMirroe.topicSum[k] + model.phi[c][k] - 1, model.phi[c][k]);
        pp2 = logAccum(DocMirroe.rwordSum + model.phiSum[c] - 1, model.phiSum[c]);
        /**----------------------------------------------------*/
        part2 = pp1 - pp2;

        //part3
        pp1 = 0;
        int word;
        for (Integer i : DocMirroe.cwordList.keySet()) {
            word = i;
            if (DocMirroe.categoryTopic_word_[word] == 0)
                continue;
            pp1 += logAccum(model.categoryWord[c][word] + model.beta0, model.categoryWord[c][word] - DocMirroe.categoryTopic_word_[word] + model.beta0);
        }
        pp2 = logAccum(model.numCWords4Cate[c] + model.beta0 * model.wordNum - 1, model.numCWords4Cate[c] - DocMirroe.cwordSum + model.beta0 * model.wordNum);
        part3 = (pp1 - pp2);

        part4 = Math.log(1 + model.eta[index][c]);

        re = part1 + part2 + part3 + part4;
        DocMirroe.clean(xmirror, zmirror, wmirror,model.documents[index].contents.size());
        return re;
    }

    private static void kernel(SModel model, int docIndex, int category, int word) {
        double temp;

        model.temp[0] = model.rho * model.tao[word][category];
        model.temp[0] *= (model.categoryWord[category][word] + model.beta0) / (model.numCWords4Cate[category] + model.beta0 * model.numCWords4Cate[category]);

        for (int z = 0; z < model.topicNum; z++) {
            //note that phi had been multiplied by alpha1
            temp = ((model.wordTopic[word][z] + model.beta1) / (model.numRWords4Topic[z] + model.wordNum * model.beta1)) * ((model.docTopic[docIndex][z] + model.phi[category][z]) / (model.docX[docIndex][1] + model.phiSum[category]));
            model.temp[1 + z] = (1 - model.rho) * temp;
        }
    }

    private static double logAccum(double up, double bottom) {
        if ((up - bottom) == -1)
            return 0;
        double re = 0;
        for (double i = bottom; i < up + 1; i++)
            re += Math.log(i);
        return re;
    }

    protected static void update(int index, SModel model, int[] xmirror, int[] zmirror, int newCate) {

        SDocument doc = model.documents[index];
        int x, word, topic;
        for (int i = 0; i < doc.contents.size(); i++) {
            //subtract first
            x = doc.xvec.get(i);
            topic = doc.contents.get(i).topic;
            word = doc.contents.get(i).word;
            if (x == 0) {
                model.categoryWord[doc.prediction][word]--;
                model.numCWords4Cate[doc.prediction]--;
                model.docX[index][0]--;
            } else {
                model.wordTopic[word][topic]--;
                model.numRWords4Topic[topic]--;
                model.docTopic[index][topic]--;
                model.docX[index][1]--;
            }
            //then add
            x = xmirror[i];
            topic = zmirror[i];
            if (x == 0) {
                model.categoryWord[newCate][word]++;
                model.numCWords4Cate[newCate]++;
                model.docX[index][0]++;
            } else {
                model.wordTopic[word][topic]++;
                model.numRWords4Topic[topic]++;
                model.docTopic[index][topic]++;
                model.docX[index][1]++;
            }
            doc.xvec.set(i, x);
            doc.contents.set(i, new Pair(word, topic));
        }
        doc.prediction = newCate;
    }

    protected static void rollBack(int index, SModel model, int formerCategory, int[] xmirror, int[] zmirror) {

        SDocument doc = model.documents[index];
        int x, word, topic;
        for (int i = 0; i < doc.contents.size(); i++) {
            x = doc.xvec.get(i);
            word = doc.contents.get(i).word;
            topic = doc.contents.get(i).topic;

            if (x == 0) {
                model.categoryWord[doc.prediction][word]++;
                model.numCWords4Cate[doc.prediction]++;
                model.docX[index][0]++;
            } else {
                model.wordTopic[word][topic]++;
                model.numRWords4Topic[topic]++;
                model.docTopic[index][topic]++;
                model.docX[index][1]++;
            }

            x = xmirror[i];
            topic = zmirror[i];

            if (x == 0) {
                model.categoryWord[formerCategory][word]--;
                model.numCWords4Cate[formerCategory]--;
                model.docX[index][0]--;
            } else {
                model.wordTopic[word][topic]--;
                model.numRWords4Topic[topic]--;
                model.docTopic[index][topic]--;
                model.docX[index][1]--;
            }
        }
    }
}
