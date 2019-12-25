package Client.Handlers;

import Client.Interface.MainPaneReservation;
import Server.Communication.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Set;

public class RegisterHandler extends Thread {
    private Socket socket;
    private RegisterRequest req;
    private MainPaneReservation res;

    public RegisterHandler(Socket socket, MainPaneReservation res, String username, String password){
        this.socket = socket;
        req = new RegisterRequest(username, password);
        this.res = res;
    }

    public void run() {
        try {
            Requests.send(socket, req);
            IReply rep = Replies.read(socket);
            if(res == null) return;
            else if(rep.getState().equals(ReplyStates.FAILED))
                res.setText("Failed creating user '" + req.getUsername() + "'.");
            else res.setText("Successfully created new user '" + req.getUsername() + "'.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
