package Client.Handlers;

import Server.Communication.Replies;
import Server.Communication.Requests;
import Server.Communication.UploadRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Set;

public class UploadHandler extends Thread {
    private Socket socket;
    private String username;
    private String password;
    private String path;
    private UploadRequest req;

    public UploadHandler(Socket socket, String path, String username, String password, String title, String author, int year, Set<String> tags){
        this.socket = socket;
        this.path = path;
        req = new UploadRequest(username, password, title, author, year, tags);
    }

    @Override
    //TODO: check if file is readable | MAX_SIZE | check server reply
    public void run() {
        try {
            File f = new File(path);
            FileInputStream in = new FileInputStream(f);
            BufferedOutputStream socketOut = new BufferedOutputStream(socket.getOutputStream());
            Requests.send(socket, req);
            Replies.read(socket);
            byte[] tmp = new byte[512];
            int rd;
            while((rd = in.read(tmp)) != -1) {
                socketOut.write(tmp, 0, rd);
                socketOut.flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
