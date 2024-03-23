import java.io.*;
import java.net.*;

public class ex1{
    public static void main(String[] args) {
        String webUrl = "http://www.google.com";
        String filePath = "ex1.html";
        
        try {
            // Create a URL object
            URL url = new URL(webUrl);
            // Open a connection to the URL
            URLConnection conn = url.openConnection();
            // Create a BufferedReader to read from the URL connection
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            // Create a FileWriter to write to the local file
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);
            
            String inputLine;
            // Read each line from the web page and write to the file
            while ((inputLine = br.readLine()) != null) {
                bw.write(inputLine);
                bw.newLine();
            }
            
            // Close the BufferedWriter and BufferedReader
            bw.close();
            br.close();
            System.out.println("The homepage has been downloaded successfully to " + filePath);
        } catch (MalformedURLException e) {
            System.out.println("The URL provided is not valid.");
        } catch (IOException e) {
            System.out.println("An I/O error occurred while connecting to the web server.");
        }
    }
}
