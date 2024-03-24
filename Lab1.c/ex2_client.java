// A Java program for a Client
import java.io.*;
import java.net.*;
 
public class ex2_client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream send_in = null;
    private DataOutputStream send_out = null;
    private DataInputStream receive_in = null;
 
    // constructor to put ip address and port
    public ex2_client(String address, int port)
    {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");
 
            // takes input from terminal
            send_in = new DataInputStream(System.in);
 
            // sends output to the socket
            send_out = new DataOutputStream(
                socket.getOutputStream());
            receive_in = new DataInputStream(new 
            BufferedInputStream(socket.getInputStream()));
        }
        catch (UnknownHostException u) {
            System.out.println(u);
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
 
        // string to read message from input
        String send = "";
 
        // keep reading until "Over" is input
        final boolean[] continueReading = {true};
        new Thread(() -> {
            String receive = "";
            while (!(receive.equals("Over")) && continueReading[0]) {
                try {
                    // receive message
                    receive = receive_in.readUTF();
                    System.out.println("Message from server: " + receive);
                } catch(IOException i) {
                    System.out.println(i);
                }
            }
        }).start();
        
        while (!(send.equals("Over"))) {
            try {
                // send a response to the client
                send = send_in.readLine();
                send_out.writeUTF(send);
                if (send.equals("Over")) {
                    continueReading[0] = false;
                }
            } catch(IOException i) {
                System.out.println(i);
            }
        }
 
        // close the connection
        try {
            send_in.close();
            send_out.close();
            receive_in.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        ex2_client client = new ex2_client("127.0.0.1", 1234);
    }
}