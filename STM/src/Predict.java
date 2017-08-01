
import Util.MathUtil;

/**
 * Created by XJ on 2016/8/5.
 */
public class Predict implements Decorator {

    private SModel model;
    double[] scores;

    public Predict(SModel model) {
        this.model = model;
    }

    private void predict() {

        int[][] xmirror = new int[model.cateNum][model.maxDocLen];
        int[][] zmirror = new int[model.cateNum][model.maxDocLen];
        int[][] wmirror = new int[model.cateNum][model.maxDocLen];

        scores = new double[model.docNum];

        for (int j = 0; j < model.inter; j++) {
            System.out.println("iter: " + j);
            //report result
            if (model.report == 1)
                Report.macroReport(model);
            else if (model.report == 0)
                Report.accReport(model);

              long startTime = System.currentTimeMillis();
            for (int i = 0; i < model.docNum; i++) {
                SDocument doc = model.documents[i];
                if (doc.contents.size() == 0)
                    continue;
                /**recount phi*/
                for (int k = 0; k < model.topicNum; k++) {
                    model.categoryTopic[doc.prediction][k] -= model.docTopic[i][k];
                    model.numRWords4Cate[doc.prediction] -= model.docTopic[i][k];
                }

                model.phiSum[doc.prediction] = 0;
                for (int k = 0; k < model.topicNum; k++) {
                    model.phi[doc.prediction][k] = (model.categoryTopic[doc.prediction][k] + model.alpha0) / (model.numRWords4Cate[doc.prediction] + model.topicNum * model.alpha0);
                    //alpha1
                    model.phi[doc.prediction][k] *= model.alpha1;
                    model.phiSum[doc.prediction] += model.phi[doc.prediction][k];
                }
                /**phi*/

                int res;
                for (int c = 0; c < model.cateNum; c++) {
                    //copy
                    copy(xmirror[c], zmirror[c],wmirror[c], doc);
                    Sampling.sampleXZ(i, model, c, xmirror, zmirror);

                    doc.scores[c] = Sampling.sampleC(i, model, c, xmirror[c], zmirror[c],wmirror[c]);
                    Sampling.rollBack(i, model, c, xmirror[c], zmirror[c]);
                }

                res = MathUtil.sample_neg(doc.scores);
                Sampling.update(i, model, xmirror[res], zmirror[res], res);

                /**recal phi*/
                for (int k = 0; k < model.topicNum; k++) {
                    model.categoryTopic[doc.prediction][k] += model.docTopic[i][k];
                    model.numRWords4Cate[doc.prediction] += model.docTopic[i][k];
                }
                model.phiSum[doc.prediction] = 0;
                for (int k = 0; k < model.topicNum; k++) {
                    model.phi[doc.prediction][k] = (model.categoryTopic[doc.prediction][k] + model.alpha0) / (model.numRWords4Cate[doc.prediction] + model.topicNum * model.alpha0);
                    model.phi[doc.prediction][k] *= model.alpha1;
                    model.phiSum[doc.prediction] += model.phi[doc.prediction][k];
                }
                /**phi*/
            }
              long endTime = System.currentTimeMillis();
              System.out.println("cost time " + (endTime - startTime) + "ms");

        }
    }

    protected static void copy(int[] xmirror, int[] zmirror, int[] wmirror, SDocument doc) {
        for (int w = 0; w < doc.contents.size(); w++) {
            xmirror[w] = doc.xvec.get(w);
            zmirror[w] = doc.contents.get(w).topic;
            wmirror[w] = doc.contents.get(w).word;
        }

    }

    @Override
    public SModel decorateSModel() {
        System.out.println("start to predict...");
        predict();
        return model;
    }
}
