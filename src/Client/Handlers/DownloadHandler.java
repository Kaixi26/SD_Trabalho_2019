package Client.Handlers;

import Client.Interface.OutputReservation;
import Client.Interface.TerminalHandler;
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
    private Socket socket;
    private TerminalHandler termHandler;
    private String username;
    private String password;
    private String path;
    private int id;

    public DownloadHandler(Socket socket, TerminalHandler termHandler, String username, String password, int id, String path){
        this.socket = socket;
        this.termHandler = termHandler;
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

            OutputReservation res = null;
            if(rep.getState().equals(ReplyStates.FAILED))
                return;
            else if(rep.getState().equals(ReplyStates.QUEUED)){
                res = termHandler.reservePaneLine("QUEUED");
                res.updateText("[D] " + rep.getTitle() + " - " + rep.getAuthor());
                while(rep.getState().equals(ReplyStates.QUEUED) || rep.getState().equals(ReplyStates.FAILED)) {
                    if (rep.getState().equals(ReplyStates.FAILED))
                        return;
                    try {
                        rep = (DownloadReply) Replies.read(socket);
                    } catch (Exception e){
                        rep = new DownloadReply(ReplyStates.FAILED);
                    }
                }
                res.free();
            }

            res = termHandler.reservePaneLine("PROGRESSING");
            res.updateText("[D] " + rep.getTitle() + " - " + rep.getAuthor());

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

            res.free();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
