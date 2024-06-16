package chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private ChatRoom chatRoom;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {
        try {
            out.println("Enter your username:");
            username = in.readLine();
            out.println("Enter chat room ID:");
            String roomId = in.readLine();

            chatRoom = ChatRoomManager.getInstance().getChatRoom(roomId);
            chatRoom.addClient(this);

            out.println("Welcome to the chat room, " + username + "!");
            out.println("Type your message:");

            String inputLine;
            while (!((inputLine = in.readLine()).equalsIgnoreCase("exit"))) {
                System.out.println("[" + username + "]: " + inputLine + "Typed one");
                chatRoom.broadcastMessage("[" + username + "]: " + inputLine, this);
            }

            chatRoom.removeClient(this);
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}