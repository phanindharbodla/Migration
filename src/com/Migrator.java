/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author Phanindhar
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Phanindhar Bodla
 */
public class Migrator {

    public static void main(String args[]) {
        try {
            File inputFile = new File(args[0]);//Give your input file  ..
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String strLine;
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(args[1]));//this your output file
            while ((strLine = br.readLine()) != null) {
                strLine = process(strLine);
                outputFile.write(strLine);
                outputFile.newLine();
            }
            outputFile.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String process(String strLine) {
        
        if(strLine.contains("package ")) return strLine+'\n'+"import org.slf4j.Logger;"+'\n'+"import org.slf4j.LoggerFactory;";
        if(strLine.contains(" class ")) return strLine+'\n'+"    private static Logger logger = LoggerFactory.getLogger(yourClassName.class);";
        if(strLine.contains("Trace.writeStartMethod")) return "";
        if(!(strLine.contains("Trace.write"))) return strLine;
        
        String line = "";
        String line2 = "";
        int j = strLine.length();
        {
            if (j != 0) {
                for (int i = 0; i < j; i++) {
                    if (strLine.charAt(i) == '"') {
                        i++;
                        while (strLine.charAt(i) != '"') {
                            line = line + strLine.charAt(i);
                            i++;
                        }
                    } else if (strLine.charAt(i) == '+') {
                        i++;
                        line = line + "{}";
                        line2 = line2 + ",";
                        while ((strLine.charAt(i) != ',')) {
                            if( strLine.charAt(i) == '+')break;
                            line2 = line2 + strLine.charAt(i);
                            i++;
                        }
                    }
                }
            }
        }
        
        String temp = "       "+chooseLogger(strLine)+"(\""+line+'"' + line2+");";
     return temp;
    }

    private static String chooseLogger(String strLine) {
        if(strLine.contains("Trace.informaional"))
        {
            return "logger.info";
        } else if(strLine.contains("Trace.warning"))
        {
            return "logger.warn";
        } else if(strLine.contains("Trace.severe"))
        {
            return "logger.error";
        } else if(strLine.contains("Trace.error"))
        {
            return "logger.error";
        } else if(strLine.contains("Trace.terse"))
        {
            return "logger.warn";
        } else if(strLine.contains("Trace.verbose"))
        {
            return "logger.info";
        }
        return null;
    }
}