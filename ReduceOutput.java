
import java.util.Vector;

/**
 *
 * @author Petcu Alexandru
 */
public class ReduceOutput {
    String docName;          //Numele fisierului
    Vector<Float> wordFreqs; //Frecventele celor NC cuvinte
    int wordCount;           //Numarul de cuvinte din document
    boolean written;
    
    public ReduceOutput(String docName, int NC){
        this.docName = docName;
        wordFreqs = new Vector<Float>();
        for (int i = 0; i < NC; i++) {
            wordFreqs.add(new Float(0));
        }
        written = false;
    }
    
    /*
     * Verifica daca documentul contine toate cuvintele cheie
     */
    boolean containsAll(){
        for (int i = 0; i < wordFreqs.size(); i++) {
            if(wordFreqs.elementAt(i) == 0)
                return false;
        }
        
        return true;
    }
    
    String getDocName(){
        return docName;
    }
    
    Vector<Float> getWordFreqs(){
        return wordFreqs;
    }
}
