package Client.Handlers;

import Client.Interface.MainPaneReservation;
import Server.Communication.*;
import Server.SoundCloud.Song;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchHandler extends Thread {
    private Socket socket;
    private MainPaneReservation res;
    private int n;
    private SearchRequest req;

    public SearchHandler(Socket socket, MainPaneReservation res, int n, String username, String password, String tag){
        this.socket = socket;
        this.res = res;
        this.n = n;
        req = new SearchRequest(username, password, tag);
    }

    public void run() {
        try {
            Requests.send(socket, req);
            SearchReply rep = (SearchReply) Replies.read(socket);
            if(!rep.getState().equals(ReplyStates.SUCESS)) {
                res.setText("Failed request!");
                return;
            }
            List<Map.Entry<Integer, Song>> songsList = rep.getSongs().entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())).collect(Collectors.toList());
            StringBuilder tmp = new StringBuilder();

            for(int i=n; i < songsList.size(); i++)
            for(Map.Entry<Integer, Song> e : rep.getSongs().entrySet())
                tmp.append(e.getKey()).append(" ").append(e.getValue().getTitle())
                        .append(" ").append(e.getValue().getAuthor())
                        .append(" ").append(e.getValue().getYear())
                        .append(" ").append(e.getValue().getTags().toString()).append("\n");
            res.setText(tmp.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
