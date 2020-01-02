package Server.Handlers;

import Constants.ServerConstants;
import Server.Communication.*;
import Server.Manager;
import Constants.ProgramConstants;
import Server.SoundCloud.Song;
import Server.TransferManager.TransferReservation;

import java.io.*;
import java.net.Socket;

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
            byte[] tmp = new byte[ProgramConstants.MAX_SIZE];
            int rd;
            while ((rd = in.read(tmp)) != -1) {
                socketOut.write(tmp, 0, rd);
                socketOut.flush();
                if(ServerConstants.MAX_TRANSFER_RATE != 0) {
                    double waitNanos = ((double)ProgramConstants.MAX_SIZE / ServerConstants.MAX_TRANSFER_RATE) * (10E9);
                    sleep((int) (waitNanos/(10E6)), (int) (waitNanos%(10E6)));
                }
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
        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
