import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ex3_client1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow mainWindow = new MainWindow();
            }
        });
    }
}

class MainWindow extends JFrame {
    public MainWindow() {
        // Set up the main window
        setTitle("Chat Application - Main Window");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Create a new connection on button click
        JButton newConnectionButton = new JButton("New Connection");
        newConnectionButton.addActionListener(e -> {
            ChatWindow chatWindow = new ChatWindow();
        });
        add(newConnectionButton);
    }
}

/////////// --------------------------------------------------------/////////

class ChatWindow extends JFrame {
    private JTextField messageInput;
    private JButton sendButton;
    private JTextArea chatArea;
    private Socket socket = null;
    private DataOutputStream out= null;
    String message = "";

    public ChatWindow() {
        // Set up the chat window

        try {
            socket = new Socket("127.0.0.1", 1234);
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            // out = new PrintWriter(socket.getOutputStream(), true);
            // Khởi tạo và bắt đầu luồng xử lý đầu vào để lắng nghe phản hồi từ server
            Thread inputHandlerThread = new Thread(new InputHandler(socket.getInputStream()));
            inputHandlerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(500, 500);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        messageInput = new JTextField();
        sendButton = new JButton("Send");
        box.add(messageInput);
        box.add(sendButton);
        add(box, BorderLayout.SOUTH);

            // Event when click on send button
        sendButton.addActionListener((e -> {
            message = messageInput.getText();
            if (message != null && !message.isEmpty()) {
                if (out != null) {
                    try {
                        out.writeUTF(message);
                        chatArea.append("You: " + message + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            messageInput.setText(""); // Reset text field
        }));
        setVisible(true);
    }
}

/////////// --------------------------------------------------------/////////



class InputHandler implements Runnable {
    private DataInputStream in;
    public InputHandler(InputStream inputStream) {
        this.in = new DataInputStream(new BufferedInputStream(inputStream));
    }
    public void run() {
        String response = "";
        while (!(response.equals("Over"))) {
            try {
                // receive message
                response = in.readUTF();
                System.out.println("Message from server: " + response);
            } catch(IOException i) {
                System.out.println(i);
            }
        }
    }
}