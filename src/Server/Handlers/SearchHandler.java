package Server.Handlers;

import Server.Communication.*;
import Server.Manager;
import Server.SoundCloud.Song;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

public class SearchHandler extends Thread {
    private Socket clientSocket;
    private Manager man;
    private SearchRequest req;

    SearchHandler(Socket clientSocket, Manager man, SearchRequest req){
        this.clientSocket = clientSocket;
        this.man = man;
        this.req = req;
    }

    @Override
    public void run() {
        try {
            Replies.send(clientSocket, new SearchReply(ReplyStates.SUCESS, man.sc.search(req.getTag())));
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
