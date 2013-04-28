
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author Petcu Alexandru
 */
public class Main {
    public static Vector<ReduceOutput> outputDocs;
    
    public static void printResult(InputReader in, String outputFile) throws IOException{
        FileWriter f = new FileWriter(outputFile);
        BufferedWriter outFile = new BufferedWriter(f);
        
        outFile.write("Rezultate pentru: (");
        for (int i = 0; i < in.NC; i++) {
            outFile.write(in.keyWords.elementAt(i));
            if(i != in.NC - 1)
                outFile.write(", ");
        }
        outFile.write(")\n\n");
        
        for(int k = 0; k < in.documents.size(); k++)
            for (int i = 0; i < outputDocs.size(); i++) {
               if(!outputDocs.elementAt(i).written && 
                  in.documents.elementAt(k).equals(outputDocs.elementAt(i).docName)){
                   outFile.write(outputDocs.elementAt(i).docName + " ");
                   outFile.write("(");
                   for (int j = 0; j < outputDocs.elementAt(i).wordFreqs.size(); j++) {
                       outFile.write(truncFloat(outputDocs.elementAt(i).wordFreqs.elementAt(j)));
                       if(j != outputDocs.elementAt(i).wordFreqs.size() - 1)
                           outFile.write(", ");
                   }
                   outputDocs.elementAt(i).written = false;
                   outFile.write(")");
                   outFile.write('\n');
               }
            }
        outFile.close();
    }
    
    public static String truncFloat(Float x){
        int a;
        a = (int)(x*100);
        x = (float)a/100;
        
        NumberFormat nf = new DecimalFormat("#.00");
        return nf.format(x);
    }
    
    public static void main(String[] args) throws FileNotFoundException, ExecutionException, InterruptedException, IOException { 
        int NT = Integer.parseInt(args[0]);
        String inputFile = args[1];
        String outputFile = args[2];
      
        InputReader in = new InputReader(inputFile);
        String fragment = new String();
        long fileSize;
        int byteNumber = 0;
        RandomAccessFile f;
        String currentDoc = new String();
   
        outputDocs = new Vector<ReduceOutput>();
        /*
         * Operatiile de tip map
         */
        Vector<Future<MapOutput>> mapResults = new Vector<Future<MapOutput>>();
        Callable<MapOutput> mWorker;
        Future<MapOutput> mFuture;
        ExecutorService mapPool = Executors.newFixedThreadPool(NT);
        
        for (int i = 0; i < in.ND; i++) {
            currentDoc = in.documents.elementAt(i);
            f = new RandomAccessFile(currentDoc, "r");
            fileSize = f.length();
            f.close();

            /*
             * Citeste cate un fragment si creeaza un MapWorker pentru el
             */
            while(byteNumber < fileSize){
                if(byteNumber + in.D > fileSize){
                    fragment = in.getFragment(byteNumber, (int)fileSize, currentDoc);
                    mWorker = new MapWorker(fragment, currentDoc);
                    mFuture = mapPool.submit(mWorker);
                    mapResults.add(mFuture);
                    break;
                }
                
                fragment = in.getFragment(byteNumber, byteNumber + in.D, currentDoc);
                mWorker = new MapWorker(fragment, currentDoc);
                mFuture = mapPool.submit(mWorker);
                mapResults.add(mFuture);
                
                byteNumber += in.D;
            }
            
            byteNumber = 0;
        }
        mapPool.shutdown();
        while(!mapPool.isTerminated()){}
        
        
        /*
         * Operatiile de tip reduce
         */
        Callable<ReduceOutput> rWorker;
        Future<ReduceOutput> rFuture;
        Vector<MapOutput> reduceInput = new Vector<MapOutput>();
        ExecutorService reducePool = Executors.newFixedThreadPool(NT);
        
        for (int i = 0; i < in.ND; i++) {
            currentDoc = in.documents.elementAt(i);
            for (int j = 0; j < mapResults.size(); j++) {
                if(currentDoc.equals(mapResults.elementAt(j).get().fileName))
                    reduceInput.add(mapResults.elementAt(j).get());
            }
            
            rWorker = new ReduceWorker(reduceInput, in);
            rFuture = reducePool.submit(rWorker);
            reduceInput = new Vector<MapOutput>();
        }
        
        
        reducePool.shutdown();
        while(!reducePool.isTerminated()){}
     
        printResult(in, outputFile);
    }
}
