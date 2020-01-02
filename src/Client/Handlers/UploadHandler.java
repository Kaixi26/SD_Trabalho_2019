package Client.Handlers;

import Client.Interface.OutputReservation;
import Client.Interface.TerminalHandler;
import Server.Communication.*;
import Constants.ProgramConstants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class UploadHandler extends Thread {
    private Socket socket;
    private TerminalHandler termHandler;
    private String username;
    private String password;
    private String title;
    private String author;
    private int year;
    private Set<String> tags = new HashSet<>();
    private String path;
    private UploadRequest req;

    public UploadHandler(Socket socket, TerminalHandler termHandler, String username, String password, String[] fields){
        this.socket = socket;
        this.termHandler = termHandler;
        this.username = username;
        this.password = password;
        this.path  = fields[1];
        this.title = fields[2];
        this.author = fields[3];
        this.year = Integer.parseInt(fields[4]);
        for(int i=5; i<fields.length; i++) tags.add(fields[i]);
    }

    @Override
    //TODO: check if file is readable | MAX_SIZE | check server reply
    public void run() {
        try {
            File f = new File(path);
            FileInputStream in = new FileInputStream(f);
            BufferedOutputStream socketOut = new BufferedOutputStream(socket.getOutputStream());
            req = new UploadRequest(username, password, f.length(), title, author, year, tags);
            Requests.send(socket, req);
            IReply rep = Replies.read(socket);
            OutputReservation res = null;
            if(rep.getState().equals(ReplyStates.FAILED))
                return;
            else if(rep.getState().equals(ReplyStates.QUEUED)){
                OutputReservation outRes = termHandler.reservePaneLine("QUEUED");
                outRes.updateText("[U] " + req.getTitle() + " - " + req.getAuthor());
                while(rep.getState().equals(ReplyStates.QUEUED) || rep.getState().equals(ReplyStates.FAILED)) {
                    if (rep.getState().equals(ReplyStates.FAILED))
                        return;
                    try {
                        rep = Replies.read(socket);
                    } catch (Exception e){
                        rep = new DefaultReply(ReplyStates.FAILED);
                    }
                }
                outRes.free();
            }

            OutputReservation outRes = termHandler.reservePaneLine("PROGRESSING");
            outRes.updateText("[U] " + req.getTitle() + " - " + req.getAuthor());
            byte[] tmp = new byte[ProgramConstants.MAX_SIZE];
            int rd;
            while((rd = in.read(tmp)) != -1) {
                socketOut.write(tmp, 0, rd);
                socketOut.flush();
            }

            Replies.read(socket);
            outRes.free();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
