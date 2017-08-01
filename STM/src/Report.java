
/**
 * Created by XJ on 2016/8/5.
 */
public class Report {

    protected static void accReport(SModel model) {
        int correct = 0;
        int total = 0;
        int[] resT = new int[model.cateNum];
        int[] resS = new int[model.cateNum];

        for (int i = 0; i < model.docNum; i++) {
            if (model.documents[i].check) {
                total++;
                if (model.documents[i].groundTruth == model.documents[i].prediction) {
                    correct++;
                    resT[model.documents[i].prediction]++;
                }
                resS[model.documents[i].prediction]++;
            }
        }

        double re = (double) correct / total;
        System.out.println("accuracy: " + re);

        // detail of result

//        System.out.println("-------------------");
//        System.out.println("P: ");
//        for (int i = 0; i < model.cateNum; i++)
//            System.out.println((double) resT[i] / resS[i]);
//        System.out.println("R: ");
//        for (int i = 0; i < model.cateNum; i++)
//            System.out.println((double) resT[i] / model.groundTruth[i]);
//        System.out.println("-------------------");
    }

    protected static void macroReport(SModel model) {

        int[] resT = new int[model.cateNum];
        int[] resS = new int[model.cateNum];
        for (int i = 0; i < model.docNum; i++) {
            if (model.documents[i].check) {
                if (model.documents[i].groundTruth == model.documents[i].prediction) {
                    resT[model.documents[i].prediction]++;
                }
                resS[model.documents[i].prediction]++;
            }
        }
        double p, r, macroF1 = 0;
        for (int i = 0; i < model.cateNum; i++) {
            p = (double) resT[i] / resS[i];
            r = (double) resT[i] / model.groundTruth[i];
            if (p == 0 || r == 0)
                continue;
            macroF1 += 2 * p * r / (p + r);
        }
        double re = macroF1 / model.cateNum;
        System.out.println("f1: " + re);
    }
}
