package Server;


import Server.Handlers.ClientHandler;
import Server.Handlers.NotificationHandler;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        Manager man = new Manager();
        man.am.newAccount("admin", "admin");
        while(true){
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket, man).start();
            System.out.println("Accepted new connection.");
        }

    }
}
