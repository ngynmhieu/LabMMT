// A Java program for a Server
import java.net.*;
import java.io.*;
 
public class ex2_server
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream send_in       =  null;
    private DataOutputStream send_out       =  null;
    private DataInputStream receive_in       =  null;
 
    // constructor with port
    public ex2_server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
    
            System.out.println("Waiting for a client ...");
    
            socket = server.accept();
            System.out.println("Client accepted");
    
            // takes input from the client socket

            // takes input from terminal
            send_in = new DataInputStream(System.in);
 
            // sends output to the socket
            send_out = new DataOutputStream(
                socket.getOutputStream());
            receive_in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            // sends output to the client socket
    
            String send = "";
            
            // reads message from client until "Over" is sent
            new Thread(() -> {
                String receive = "";
                while (!(receive.equals("Over"))) {
                    try {
                        // receive message
                        receive = receive_in.readUTF();
                        System.out.println("Message from client: " + receive);
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
                } catch(IOException i) {
                    System.out.println(i);
                }
            }
            
            System.out.println("Closing connection");
    
            // close connection
            socket.close();
            send_in.close();
            send_out.close();
            receive_in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        ex2_server server = new ex2_server(5000);
    }
}