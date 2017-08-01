
import Util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XJ on 2016/3/10.
 */
public class SDocument {
    protected int prediction; //the prediction by STM
    protected int groundTruth;
    protected boolean check; // for documents of testSet , 'check'= true
    protected String title;
    protected List<Pair> contents;
    protected List<Integer> xvec;
    protected int index;
    protected double[] scores;

    protected SDocument(int prediction, List<Pair> contents) {
        this.prediction = prediction;
        this.contents = contents;
    }

    protected SDocument() {
        contents = new ArrayList<>();
        xvec = new ArrayList<>();
    }
}
