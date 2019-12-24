package Client.Handlers;

import Server.Communication.RegisterRequest;
import Server.Communication.Replies;
import Server.Communication.Requests;
import Server.Communication.UploadRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Set;

public class RegisterHandler extends Thread {
    private Socket socket;
    private RegisterRequest req;

    public RegisterHandler(Socket socket, String username, String password){
        this.socket = socket;
        req = new RegisterRequest(username, password);
    }

    public void run() {
        try {
            Requests.send(socket, req);
            Replies.read(socket);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
