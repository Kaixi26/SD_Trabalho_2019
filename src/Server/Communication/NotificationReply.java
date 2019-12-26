package Server.Communication;

import Server.SoundCloud.Song;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotificationReply implements IReply {
    static final String  token = "REPLY_NOTIFICATION";
    private String state;
    private Song song;

    public NotificationReply(String state, Song song){
        this.state = state;
        this.song = song;
    }

    public String stringSerialize() {
        StringBuilder tmp = new StringBuilder().append(token).append("\\|").append(state);
        tmp.append("\\|").append(song.getAuthor());
        tmp.append("\\|").append(song.getTitle());
        tmp.append("\\|").append(song.getYear());
        for(String tag : song.getTags())
            tmp.append("\\|").append(tag);
        return tmp.toString();
    }

    static public IReply parse(String[] fields){
        if(fields.length == 2) return new DefaultReply(fields[1]);

        Set<String> tags = new HashSet<>();
        for(int j=2; j<fields.length; j++)
            tags.add(fields[j]);
        return new NotificationReply(fields[1],new Song(fields[2], fields[3], Integer.parseInt(fields[4]), tags));
    }

    public String getState() {
        return state;
    }

    public Song getSong() {
        return song;
    }
}
