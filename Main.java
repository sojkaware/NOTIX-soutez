//import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.zip.ZipInputStream;

import java.util.Map;
import java.util.Arrays;


import java.util.HashMap;
import java.lang.Integer;

public class Main {

    public static void main(String[] args) throws Exception {
        // Download and unpack the file
        downloadMessage2txt(); // 

        String inputStr = loadTxtMessage();
    
        System.out.println("\n" + inputStr);

        String[] parts_inputStr = inputStr.split("xxxx");
        String[] firstPairsHexaStr = parts_inputStr[0].split("\\|");
        String[] afterxxxxHexa_splittedDigits = parts_inputStr[1].split("0x");
    
        int[] firstBytes_left = new int[firstPairsHexaStr.length];
        int[] firstBytes_right = new int[firstPairsHexaStr.length];

        for (int j = 0; j < firstPairsHexaStr.length; j++) {
            String firstHexPairStr = firstPairsHexaStr[j];
            String[] firstHexPairStr_splittedDigits = firstHexPairStr.split("0x");
            firstBytes_left[j] = (int) Integer.parseInt(firstHexPairStr_splittedDigits[1], 16);
            firstBytes_right[j] = (int) Integer.parseInt(firstHexPairStr_splittedDigits[2], 16);
            // System.out.println( firstBytes_left[j] ); 
            // System.out.println( firstBytes_right[j] ); 
        }
       
       //////////////////////////INVESTIGATION////////////////////////////

        int[] unique_min_max__left = arr_unique_min_max(firstBytes_left);
        int[] unique_min_max__right = arr_unique_min_max(firstBytes_right);
        if (unique_min_max__left[0] == firstBytes_left.length && unique_min_max__right[0] == firstBytes_right.length) {
            System.out.println("\n"); 
            System.out.println("Not a single byte in the left column is present twice.");
            System.out.println("Not a single byte in the right column is present twice.");
            System.out.println("It seems that the pairs of the bytes form a bijective mapping.");
       }     
       System.out.println("\n"); 
       System.out.println("Min-Max for the left bytes is: " + unique_min_max__left[1] + " - " + unique_min_max__left[2]);
       System.out.println("Min-Max for the right bytes is: " + unique_min_max__right[1] + " - " + unique_min_max__right[2]);


       int[] secondBytes = new int[afterxxxxHexa_splittedDigits.length-1];
       for (int j = 1; j < afterxxxxHexa_splittedDigits.length; j++) {
            String twoHexDigitsStr = afterxxxxHexa_splittedDigits[j];
            secondBytes[j-1] = Integer.parseInt(twoHexDigitsStr, 16);
        }

        int[] unique_min_max__secondBytes = arr_unique_min_max(secondBytes);
        System.out.println("Min-Max for the bytes in the payload is: " + unique_min_max__secondBytes[1] + " - " + unique_min_max__secondBytes[2]);

       System.out.println("\n"); 
       System.out.println("Seems that the bytes in the payload section of the message are part of the firstBytes_right column.");
       System.out.println("This suggests I should use this mapping by replacing the bytes by corresponding bytes in the firstBytes_left and convert it to ASCII");
       
        //////////////////////////SOLVING THE PUZZLE////////////////////////////
        
        Map<Integer, Integer> asciiMap = new HashMap<>();
        for (int i = 0; i < firstBytes_left.length; i++) {
            asciiMap.put( firstBytes_right[i] , firstBytes_left[i]);
        }

        String output = "";
        for (int i = 0; i < secondBytes.length; i++) {
            int translated_value = asciiMap.get(secondBytes[i]);
            char c = (char) translated_value;
            output += c;
        }
        System.out.println("\n");
        System.out.println(output);
    }

        ////////////////////////////////////////////////////////////////////////


    public static int[] arr_unique_min_max(int[] arr) {
        int[] arr_sorted = Arrays.copyOf(arr, arr.length);
        Arrays.sort(arr_sorted);
        int numUniqueElements = 0;
        for (int i = 0; i < arr_sorted.length - 1; i++) {
            if (arr_sorted[i] != arr_sorted[i+1]) {
                numUniqueElements++;
            }
        }
        // Add 1 for the last element
        numUniqueElements++;
        int[] arrstats = { numUniqueElements , arr_sorted[0] , arr_sorted[arr_sorted.length-1] };
        return arrstats;
    }

    public static void downloadMessage2txt() throws Exception {

            URL url = new URL("https://www.notixtodelajinak.cz/soutez/zprava.zip");
            try (ZipInputStream zip = new ZipInputStream(url.openStream())) {
                zip.getNextEntry();
                Files.copy(zip, Paths.get("zprava.txt"));
                System.out.println("Successfully downloaded and extracted zprava.txt");
            } 
    }

    public static String loadTxtMessage() throws Exception {
          byte[] bytes = Files.readAllBytes(Paths.get("zprava.txt"));
          String message = new String(bytes);
          return message;
    }

}


    






