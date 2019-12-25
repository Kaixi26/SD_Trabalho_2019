package Client;

import Client.Handlers.DownloadHandler;
import Client.Handlers.RegisterHandler;

import java.net.Socket;

public class Main3 {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 12345);
        new RegisterHandler(s, null, "kaixi", "12345").start();
    }
}
