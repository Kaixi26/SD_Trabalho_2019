package Client;

import Client.Handlers.RegisterHandler;
import Client.Handlers.SearchHandler;

import java.net.Socket;

public class Main4 {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 12345);
        new SearchHandler(s, "kaixi", "12345", "Skott").start();
    }
}
