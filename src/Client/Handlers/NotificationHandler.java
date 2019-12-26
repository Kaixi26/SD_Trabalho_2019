package Client.Handlers;

import Client.Interface.MainPaneReservation;
import Client.Interface.OutputReservation;
import Client.Interface.OutputReservationHolder;
import Client.Interface.TerminalHandler;
import Server.Communication.*;
import Server.SoundCloud.Song;

import java.net.Socket;

public class NotificationHandler extends Thread {
    private Socket socket;
    private NotificationRequest req;
    private TerminalHandler termHandler;

    public NotificationHandler(Socket socket, TerminalHandler termHandler, String username, String password){
        this.socket = socket;
        req = new NotificationRequest(username, password);
        this.termHandler = termHandler;
    }

    public void run() {
        try {
            Requests.send(socket, req);
            IReply rep = Replies.read(socket);
            if(rep.getState().equals(ReplyStates.SUCESS)) {
                while (true) {
                    NotificationReply tmp = (NotificationReply) Replies.read(socket);
                    if(tmp.getState().equals(ReplyStates.FAILED)) return;
                    OutputReservation res = termHandler.reservePaneLine("NOTIFICATIONS");
                    res.updateText("[New Song] " + tmp.getSong().getTitle() + " - " + tmp.getSong().getAuthor());
                    new OutputReservationHolder(res, 3000).start();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
