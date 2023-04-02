import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.zip.ZipInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Main {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "mysecretpassword";
    
    public static void main(String[] args) throws Exception {
        // Download and unpack the file
        downloadMessage2txt();
      
        String message = loadTxtMessage();
        System.out.println("Decoded message: " + message);
        //System.out.println(new String(decrypted));
        
        // Encrypt and decrypt the message
        //String message = "Hello world";
        //byte[] encrypted = encrypt(message.getBytes(), KEY);
        //byte[] decrypted = decrypt(encrypted, KEY);
        //System.out.println(new String(decrypted));
        String input = "0x7d0xe9|0x7e0xfdxxxx0xb60x840x9d0xc80xa50xc4";
        String[] pairs = input.split("\\|");
        
        int stopIndex = 0;
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].contains("xxxx")) {
                stopIndex = i;
                break;
            }
        }

        byte[][] firstBytes = new byte[stopIndex][2];
        for (int i = 0; i < stopIndex; i++) {
            String[] hexValues = pairs[i].split("0x");
            for (int j = 1; j < hexValues.length; j++) {
                String hexValue = hexValues[j];
                byte b = (byte) Integer.parseInt(hexValue, 16);
                firstBytes[i][j-1] = b;
            }
        }

        for (int i = 0; i < stopIndex; i++) {
            String hexPair = String.format("%02X %02X", firstBytes[i][0], firstBytes[i][1]);
            System.out.println(hexPair);
        }


      
        System.out.println("First bytes: " + Arrays.toString(firstBytes));
       // System.out.println("Second bytes: " + Arrays.toString(secondBytes));
    
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

    public static String stringExtractBytes(String data) {
            String[] byteStrings = data.split("\\|");
            byte[] bytes = new byte[byteStrings.length];
            for (int i = 0; i < byteStrings.length; i++) {
                bytes[i] = (byte) Integer.parseInt(byteStrings[i].substring(2), 16);
            }
            String byteString = new String(bytes);
            return byteString;
    }



    






  
    
    private static byte[] encrypt(byte[] data, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
    
    private static byte[] decrypt(byte[] data, String key) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}
