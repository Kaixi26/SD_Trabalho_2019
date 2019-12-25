package Tests;

import Server.Communication.ReplyStates;
import Server.Communication.SearchReply;
import Server.SoundCloud.Song;

import java.util.*;

public class SearchRequest {
    public static void main(String[] args) throws Exception {
        Set<String> tags_0 = new HashSet<>();
        Set<String> tags_1 = new HashSet<>();
        Set<String> tags_2 = new HashSet<>();
        tags_1.add("testtag0");
        tags_2.add("testtag0");
        tags_1.add("testtag1");
        tags_2.add("testtag1");
        tags_2.add("testtag2");
        Map<Integer, Song> songs = new HashMap<>();
        songs.put(0, new Song("song0", "author0", 0, tags_0));
        songs.put(1, new Song("song1", "author1", 1, tags_1));
        songs.put(2, new Song("song2", "author2", 2, tags_2));
        SearchReply rep = new SearchReply(ReplyStates.SUCESS, songs);
        System.out.println(rep.stringSerialize());
        SearchReply rep_parsed = (SearchReply) SearchReply.parse(rep.stringSerialize().split("\\\\\\|"));
        for(Map.Entry<Integer, Song> e : rep_parsed.getSongs().entrySet()){
            System.out.println(e.getKey() + " " + e.getValue().getTitle() + " " + e.getValue().getAuthor() + " " + e.getValue().getYear() + " " + e.getValue().getTags().toString());
        }
    }
}
