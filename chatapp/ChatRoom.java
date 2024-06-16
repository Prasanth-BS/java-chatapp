package chatapp;

import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {
    private String roomId;
    private CopyOnWriteArrayList<ClientHandler> clients;

    public ChatRoom(String roomId) {
        this.roomId = roomId;
        this.clients = new CopyOnWriteArrayList<>();
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
        broadcastUserList();
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcastUserList();
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    private void broadcastUserList() {
        StringBuilder userList = new StringBuilder("Active users: ");
        for (ClientHandler client : clients) {
            userList.append(client.getUsername()).append(", ");
        }
        String userListMessage = userList.toString();
        for (ClientHandler client : clients) {
            client.sendMessage(userListMessage);
        }
    }
}