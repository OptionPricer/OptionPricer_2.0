package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class has just one method used to read the file
 * 
 * @author Wangyu Huang(Castiel)
 * @since 2015.03.09
 * @version 1.1.0
 */
public class FileHandler {    
    public static void readByLines(String fileName, ArrayList algoList) throws IOException{
        File file = new File(fileName);
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
    
    public static void addNewFormula(String fileName, String Formula) throws IOException{
        File file = new File(fileName);
//        System.out.println(1);
        FileWriter fileWriter = new FileWriter(file, true);
//        System.out.println(2);
        BufferedWriter writer = new BufferedWriter(fileWriter);
//        System.out.println(3);
        writer.write(Formula + "\n");
//        System.out.println(4);
        writer.close();
    }
}
