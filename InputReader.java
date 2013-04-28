
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Petcu Alexandru
 */

public class InputReader {
    int NC;  //Numarul de cuvinte cheie
    Vector<String> keyWords; //Cele NC cuvinte cheie
    int D;   //Dimensiunea in octeti a fragmentelor de fisier
    int N;   //Numarul de cuvinte retinute ptr fiecare document
    int X;   //Numarul de documente raspuns
    int ND;  //Numarul de documente in care se face cautarea
    Vector<String> documents;  //Numele celor ND documente
    
    public InputReader(String fileName) throws FileNotFoundException{
        Scanner in = new Scanner(new FileReader(fileName));
        NC = in.nextInt();
        
        keyWords = new Vector<String>();
        for (int i = 0; i < NC; i++) {
            keyWords.add(in.next());
        }
        
        D = in.nextInt();
        N = in.nextInt();
        X = in.nextInt();
        ND = in.nextInt();
        
        documents = new Vector<String>();
        for (int i = 0; i < ND; i++) {
            documents.add(in.next());
        }
    }
    
    String getFragment(int startPos, int endPos, String fileName) throws FileNotFoundException, IOException{
        String docFragment = "";
        RandomAccessFile doc = new RandomAccessFile(fileName, "r");
        int aByte = 0;
        
        doc.seek(startPos);
        if(startPos != 0){
            doc.seek(startPos - 1);
            aByte = doc.readByte();
            aByte = Character.toLowerCase(aByte);
            
            if(aByte >= 'a' && aByte <= 'z')
                while(aByte >= 'a' && aByte <= 'z' && startPos < endPos){
                    aByte = doc.readByte();
                    startPos++;
                }
        }
        while(startPos < endPos){
            aByte = doc.readByte();
            aByte = Character.toLowerCase(aByte);
            docFragment += (char)aByte;
            startPos++;
        }
        
         while(aByte >= 'a' && aByte <= 'z'){
            aByte = doc.readByte();
            aByte = Character.toLowerCase(aByte);
            docFragment += (char)aByte;
        }
         
        return docFragment;
    }
    
    int getNC(){
        return NC;
    }
    
    Vector<String> getKeyWords(){
        return keyWords;
    }
    
    int getD(){
        return D;
    }
    
    int getN(){
        return N;
    }
    
    int getX(){
        return X;
    }
    
    int getND(){
        return ND;
    }
    
    Vector<String> getDocuments(){
        return documents;
    }
}
