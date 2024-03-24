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