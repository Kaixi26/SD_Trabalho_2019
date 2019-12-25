package Server.Communication;

import Server.SoundCloud.Song;

import java.util.*;

public class SearchReply implements IReply {
    static final String  token = "REPLY_SEARCH";
    private String state;
    private Map<Integer, Song> songs;

    public SearchReply(String state){
        this.state = state;
        this.songs = new HashMap<>();
    }

    public SearchReply(String state, Map<Integer, Song> songs){
        this.state = state;
        this.songs = new HashMap<>(songs);
    }

    public String stringSerialize() {
        StringBuilder tmp = new StringBuilder().append(token).append("\\|").append(state);
        for(Map.Entry<Integer, Song> e : songs.entrySet()){
            tmp.append("\\|").append(e.getKey());
            tmp.append("\\|").append(e.getValue().getTags().size());
            tmp.append("\\|").append(e.getValue().getAuthor());
            tmp.append("\\|").append(e.getValue().getTitle());
            tmp.append("\\|").append(e.getValue().getYear());
            for(String tag : e.getValue().getTags())
                tmp.append("\\|").append(tag);
        }
        return tmp.toString();
    }

    public static IReply parse(String[] fields){
        if(fields.length == 2) return new SearchReply(fields[1]);
        Map<Integer, Song> tmp = new HashMap<>();
        int i=2;
        while(i<fields.length){
            int tagSize = Integer.parseInt(fields[i+1]);
            Set<String> tags = new HashSet<>();
            for(int j=i+5; j<i+5+tagSize; j++)
                tags.add(fields[j]);
            tmp.put(Integer.parseInt(fields[i]), new Song(fields[i+2], fields[i+3], Integer.parseInt(fields[i+4]), tags));
            i += 5 + tagSize;
        }
        return new SearchReply(fields[1], tmp);
    }

    public String getState() {
        return state;
    }

    public Map<Integer, Song> getSongs() {
        return new HashMap<>(songs);
    }
}
