package Server.SoundCloud;

import java.util.HashSet;
import java.util.Set;

public class Song {
    private String title;
    private String author;
    private int year;
    private Set<String> tags;

    Song(String title, String author, int year, Set<String> tags){
        this.title = title;
        this.author = author;
        this.year = year;
        this.tags = new HashSet<>(tags);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    public boolean hasTag(String tag){
        return tags.stream().anyMatch(t -> t.equals(tag));
    }
}
