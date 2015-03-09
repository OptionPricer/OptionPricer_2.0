package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class has just one method used to read the file
 * 
 * @author Castiel
 * @since 2015.03.09
 * @version 1.0.0
 */
public class FileScanner {    
    public static void readByLines(String filename, ArrayList algoList) throws IOException{
        File file = new File(filename);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;            
            while((tempString = reader.readLine()) != null){    
                //just store those non-space string without space
                if(!tempString.replace(" ", "").equals("")){
                    algoList.add(tempString.replace(" ", "")); 
                }
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        } 
        
    }
}
