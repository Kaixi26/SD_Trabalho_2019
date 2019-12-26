package Server.Handlers;

import Server.Communication.*;
import Server.Manager;
import Server.SoundCloud.Song;

import java.io.IOException;
import java.net.Socket;

public class NotificationHandler extends Thread {
    private Socket clientSocket;
    private Manager man;
    private NotificationRequest req;

    public NotificationHandler(Socket clientSocket, Manager man, NotificationRequest req){
        this.clientSocket = clientSocket;
        this.man = man;
        this.req = req;
    }

    public void run() {
        try {
            Replies.send(clientSocket, new DefaultReply(ReplyStates.SUCESS));
            while (true) {
                Replies.send(clientSocket, new NotificationReply(ReplyStates.SUCESS, man.sc.awaitNew()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
