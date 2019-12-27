package Server.Handlers;

import Server.Communication.*;
import Server.Manager;
import Server.SoundCloud.Song;
import Server.TransferManager.TransferReservation;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadHandler extends Thread {
    private Socket clientSocket;
    private Manager man;
    private DownloadRequest req;

    DownloadHandler(Socket clientSocket, Manager man, DownloadRequest req){
        this.clientSocket = clientSocket;
        this.man = man;
        this.req = req;
    }

    @Override
    public void run() {
        TransferReservation tRes = null;
        try {
            File file = new File("./data/songs/" + req.getId());
            System.out.println(man.sc.getSong(0));
            Song song = man.sc.getSong(req.getId());
            if (song == null || !file.canRead()) {
                Replies.send(clientSocket, new DownloadReply(ReplyStates.FAILED));
                return;
            }
            tRes = man.tm.reserveTransfer(req.getUsername());
            if(tRes.isInQueue()){
                Replies.send(clientSocket, new DownloadReply(ReplyStates.QUEUED, file.length(), song.getTitle(), song.getAuthor(), song.getYear(), song.getTags()));
                System.out.println(song.getTitle());
                tRes.awaitSpot();
            }
            Replies.send(clientSocket, new DownloadReply(ReplyStates.SUCESS, file.length(), song.getTitle(), song.getAuthor(), song.getYear(), song.getTags()));
            FileInputStream in = new FileInputStream(file);
            BufferedOutputStream socketOut = new BufferedOutputStream(clientSocket.getOutputStream());
            byte[] tmp = new byte[512];
            int rd;
            while ((rd = in.read(tmp)) != -1) {
                socketOut.write(tmp, 0, rd);
                socketOut.flush();
                sleep(0, 10);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(tRes != null) tRes.free();
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
