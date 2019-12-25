package Client.Handlers;

import Server.Communication.*;
import Server.SoundCloud.Song;

import java.net.Socket;
import java.util.Map;

public class SearchHandler extends Thread {
    private Socket socket;
    private SearchRequest req;

    public SearchHandler(Socket socket, String username, String password, String tag){
        this.socket = socket;
        req = new SearchRequest(username, password, tag);
    }

    public void run() {
        try {
            Requests.send(socket, req);
            SearchReply rep = (SearchReply) Replies.read(socket);
            for(Map.Entry<Integer, Song> e : rep.getSongs().entrySet())
                System.out.println(e.getKey() + " " + e.getValue().getTitle() + " " + e.getValue().getAuthor() + " " + e.getValue().getYear() + " " + e.getValue().getTags().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
