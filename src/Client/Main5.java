package Client;

import Client.Handlers.DownloadHandler;
import Client.Handlers.UploadHandler;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class Main5 {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 12345);
        Set<String> tags = new HashSet<>();
        tags.add("Skott");
        //new UploadHandler(s, "./testdata/Mermaid.mp3", "admin", "admin", "Mermaid", "Skott", 2017, tags).start();
        sleep(5000);
        //s = new Socket("localhost", 12345);
        //new DownloadHandler(s, "admin", "admin", 0, "./testdata/").start();
    }
}
