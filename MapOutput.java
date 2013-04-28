
import java.util.HashMap;

/**
 *
 * @author Petcu Alexandru
 */
public class MapOutput {
    String fileName;                  //Numele fisierului
    HashMap<String, Float> docVector; //Vectorul de cuvinte si frecventele lor
    int wordCount;                    //Numarul de cuvinte dintr-un fragment
    
    public MapOutput(String fileName){
        this.fileName = fileName;
        docVector = new HashMap<String, Float>();
        wordCount = 0;
    }
    
    String getFileName(){
        return fileName;
    }
    
    HashMap<String, Float> getDocVector(){
        return docVector;
    }
}
