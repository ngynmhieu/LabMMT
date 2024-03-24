import java.net.*;
import java.io.*;

public class ex3_server {
    public static void main(String[] args) {
        new ListenerThread().start(); // Thread 1: Main server thread to listen for connections
    }
}

class ListenerThread extends Thread {
    private int clientCount = 0; 
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (!Thread.interrupted()) {
                System.out.println("waiting...");;
                clientCount++;
                Socket clientSocket = serverSocket.accept(); // Thread 2: Accepts incoming connections
                System.out.println("Client " + clientCount + " accepted");
                new ConnectionHandlerThread(clientSocket,clientCount).start(); // Creates a new thread to handle the connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ConnectionHandlerThread extends Thread {
    private Socket clientSocket;
    private int clientCount;

    public ConnectionHandlerThread(Socket clientSocket, int clientCount) {
        this.clientSocket = clientSocket;
        this.clientCount = clientCount;
    }

    public void run() {
        try {
            // Thread 3: Handles the connection and creates a new thread for InputStream
            new InputStreamHandlerThread(clientSocket, clientSocket.getInputStream(), clientCount).start();
            // Additional code to handle OutputStream or other tasks
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class InputStreamHandlerThread extends Thread {
    private DataInputStream in;
    private DataInputStream send_in = null;
    private DataOutputStream send_out = null;
    private Socket socket;
    private InputStream inputStream;
    private int clientCount;
    
    public InputStreamHandlerThread(Socket socket, InputStream inputStream, int clientCount) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.clientCount = clientCount;
    }

    public void run() {
        // Thread 4: Handles the InputStream
        try{
            in = new DataInputStream((new BufferedInputStream(inputStream)));
            send_in = new DataInputStream(System.in);
 
            send_out = new DataOutputStream(socket.getOutputStream());
            String message = "";
            final boolean[] continueReading = {true};
            new Thread(() -> {
                String message_send = "";
                while (!(message_send.equals("Over")) && continueReading[0]) {
                    try {
                        message_send = send_in.readLine();
                        send_out.writeUTF(message_send);
                        if (message_send.equals("Over")) {
                            continueReading[0] = false;
                        }
                    } catch(IOException i) {
                        System.out.println(i);
                    }
                }
            }).start();
            while (!message.equals("Over")) {
                try{
                    message = in.readUTF();
                    System.out.println("Message from client " + clientCount + ": "+ message  );
                }
                catch (IOException i){
                    System.out.println(i);
                }
            }
            System.out.println("Client " + clientCount + " closed connection");
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
}