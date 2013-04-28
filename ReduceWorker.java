
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;


/**
 *
 * @author Petcu Alexandru
 */

public class ReduceWorker implements Callable{
    InputReader in;
    Vector<MapOutput> mapOut;
    
    public ReduceWorker(Vector<MapOutput> mapOut, InputReader in){
        this.in = in;
        this.mapOut = mapOut;
    }
    
    @Override
    public ReduceOutput call(){
        ReduceOutput reduceResult = new ReduceOutput(mapOut.elementAt(0).fileName, in.NC);
        for (int i = 0; i < mapOut.size(); i++) {
            reduceResult.wordCount += mapOut.elementAt(i).wordCount;
            Iterator it = mapOut.elementAt(i).docVector.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                for (int j = 0; j < in.keyWords.size(); j++) {
                    if(in.keyWords.elementAt(j).equals(pair.getKey())){
                        Float f = reduceResult.wordFreqs.elementAt(j);
                        reduceResult.wordFreqs.remove(j);
                        reduceResult.wordFreqs.add(j, f + (Float)pair.getValue());
                    }
                }
                it.remove();
            }
        }
        for (int i = 0; i < reduceResult.wordFreqs.size(); i++) {
            Float f = reduceResult.wordFreqs.elementAt(i);
            reduceResult.wordFreqs.remove(i);
            reduceResult.wordFreqs.add(i, (f/reduceResult.wordCount) * 100);
        }
        

        if(reduceResult.containsAll())
            Main.outputDocs.add(reduceResult);
        return reduceResult;
    }
    
}
