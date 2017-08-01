
import Util.LuceneWriter;

/**
 * Created by XJ on 2016/8/5.
 */
public class LuceneIndex implements Decorator {

    private SModel model;


    public LuceneIndex(SModel model) {
        this.model = model;
    }

    private void luceneIndex(){
        LuceneWriter.writeIndex(model.luceneIndexPath,model.testSetPath,model.cateNum,model.catalogPath,true);
        LuceneWriter.writeIndex_append(model.luceneIndexPath, model.trainSetPath, model.cateNum, model.catalogPath, false);
    }

    @Override
    public SModel decorateSModel() {
        luceneIndex();
        return model;
    }
}
