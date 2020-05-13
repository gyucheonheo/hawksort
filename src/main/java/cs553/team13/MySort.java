package cs553.team13;

import cs553.team13.ExternalSort.ExternalSort;
import cs553.team13.ExternalSort.ExternalSort_I;
import cs553.team13.InternalSort.MergeSort;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MySort {
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            usage();
            return;
        }
        String input_file_name = args[0];
        String output_file_name = args[1];

        if(isInputFileLessThanMemoryLimit(input_file_name)){
            List<String> data_to_be_sorted = new ArrayList<>();
            MergeSort<String> mg = new MergeSort<>();
            BufferedReader br = new BufferedReader(new FileReader(input_file_name));
            String line;
            while((line = br.readLine()) != null){
                data_to_be_sorted.add(line);
            }
            br.close();
            mg.sort(data_to_be_sorted);

            BufferedWriter bw = new BufferedWriter(new FileWriter(output_file_name));
            for(String record : data_to_be_sorted){
                bw.write(record+"\r"); // \\r causes valsort to throw unsorted
                bw.newLine();
            }
            bw.close();
            return;
        }
		ExternalSort_I ex = new ExternalSort();
        List<File> chunks = ex.chunkFiles(new File(input_file_name));
        ex.mergeLocalChunks(chunks, new File(output_file_name));
    }

    private static boolean isInputFileLessThanMemoryLimit(String input_file_name){
        File f = new File(input_file_name);
        return f.length() < Runtime.getRuntime().freeMemory();
    }

    private static void usage(){
        System.out.println("Hawksort");
        System.out.println("*******************************************");
        System.out.println("*                                         *");
        System.out.println("* Before you run it, make sure to set JVM *");
        System.out.println("* heap size to 8GB.                       *");
        System.out.println("* $>export MAVEN_OPTS=\"-Xms8g -Xmx8g\"   *");
        System.out.println("*******************************************");
        System.out.println("* KNOWN LIMITATIONS");
        System.out.println("  1. It does NOT support parallelism.");
        System.out.println("");
        System.out.println("USAGE ");
        System.out.println("\t$>mvn exec:java -Dexec.args=\"<input_file_name> <output_file_name>\"");
        System.out.println("");
        System.out.println("For example, the below code will sort mysort1GB.log file");
        System.out.println("\t$>mvn exec:java -Dexec.args=\"mysort1GB.log mysort1GB_sorted.log\"");
    }
}
