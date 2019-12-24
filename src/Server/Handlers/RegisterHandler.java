package Server.Handlers;

import Server.AccountManager.InvalidUsernameException;
import Server.Communication.*;
import Server.Manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegisterHandler extends Thread {
    private Socket clientSocket;
    private Manager man;
    private RegisterRequest req;

    RegisterHandler(Socket clientSocket, Manager man, RegisterRequest req){
        this.clientSocket = clientSocket;
        this.man = man;
        this.req = req;
    }

    public void run() {
        try {
            try {
                man.am.newAccount(req.getUsername(), req.getPassword());
                Replies.send(clientSocket, new UploadReply(ReplyStates.SUCESS));
            } catch (InvalidUsernameException e){
                Replies.send(clientSocket, new DefaultReply(ReplyStates.FAILED));
            }
            clientSocket.shutdownOutput();
            clientSocket.shutdownInput();
            clientSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
