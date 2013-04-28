/**
 *
 * @author Petcu Alexandru
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Callable;

public class MapWorker implements Callable{
    String fragment;  //Fragmentul de fisier citit
    String fileName;  //Numele fisierului
    
    public MapWorker(String fragment, String fileName){
        this.fragment = fragment;
        this.fileName = fileName;
    }
    
    @Override
    public MapOutput call() throws FileNotFoundException, IOException{
        MapOutput mapResult = new MapOutput(fileName);
        String word = "";
        Float freq = new Float(0);
        
        for (int i = 0; i < fragment.length(); i++) {
            if(fragment.charAt(i) >= 'a' && fragment.charAt(i) <= 'z'){
                word += fragment.charAt(i);
            }
            else if(!word.equals("")){
                if(!mapResult.docVector.containsKey(word)){
                    mapResult.docVector.put(word, new Float(1));
                    word = "";
                    mapResult.wordCount++;
                }
                else{
                    freq = mapResult.docVector.get(word);
                    mapResult.docVector.put(word, freq + 1);
                    word = "";
                    mapResult.wordCount++;
                }
            }
        }
 
        return mapResult;
    }
}
