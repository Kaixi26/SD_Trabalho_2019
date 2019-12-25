package Client;

import Client.Handlers.DownloadHandler;
import Client.Handlers.UploadHandler;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class Main2 {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 12345);
        //new DownloadHandler(s, "admin", "admin", 0, "./testdata/lulw").start();
    }
}
