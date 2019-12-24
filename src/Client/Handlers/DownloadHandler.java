package Client.Handlers;

import Server.Communication.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class DownloadHandler extends Thread {
    Socket socket;
    String username;
    String password;
    String path;
    int id;

    public DownloadHandler(Socket socket, String username, String password, int id, String path){
        this.socket = socket;
        this.username = username;
        this.password = password;
        this.id = id;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            Requests.send(socket, new DownloadRequest(username, password, id));
            DownloadReply rep = (DownloadReply) Replies.read(socket);

            Path p = FileSystems.getDefault()
                    .getPath(path + (path.charAt(path.length()-1) == '/' ? rep.getTitle() + " - " + rep.getAuthor() + ".mp3" : ""));
            Files.createDirectories(p.getParent());
            File tmp = p.toFile();
            FileOutputStream fileOut = new FileOutputStream(tmp);
            BufferedInputStream socketIn = new BufferedInputStream(socket.getInputStream());

            byte[] buffer = new byte[512];
            int rd;
            while ((rd = socketIn.read(buffer)) != -1)
                fileOut.write(buffer, 0, rd);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
