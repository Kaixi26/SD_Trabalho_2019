package Server.Handlers;

import Server.Communication.*;
import Server.Manager;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class UploadHandler extends Thread {
    private Socket clientSocket;
    private Manager man;
    private UploadRequest req;

    UploadHandler(Socket clientSocket, Manager man, UploadRequest req){
        this.clientSocket = clientSocket;
        this.man = man;
        this.req = req;
    }

    //TODO: get a place for path
    public void run() {
        int key = man.sc.reserveKey();
        try {
            Path p = FileSystems.getDefault().getPath(".","data","songs", key + "");
            Files.createDirectories(p.getParent());
            File tmp = p.toFile();
            FileOutputStream fileOut = new FileOutputStream(tmp);
            BufferedInputStream socketIn = new BufferedInputStream(clientSocket.getInputStream());
            Replies.send(clientSocket, new UploadReply(ReplyStates.SUCESS));
            byte[] buffer = new byte[512];
            int rd = 0;
            long missingRead = req.getSize();
            while(missingRead > 0){
                if((rd = socketIn.read(buffer)) == -1) break;
                fileOut.write(buffer, 0, rd);
                missingRead-=rd;
                System.out.println(req.getAuthor() + " " + missingRead + " " + rd);
            }
            man.sc.add(key, req.getTitle(), req.getAuthor(), req.getYear(), req.getTags());
            Replies.send(clientSocket, new DefaultReply(ReplyStates.SUCESS));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            clientSocket.shutdownOutput();
            clientSocket.shutdownInput();
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
