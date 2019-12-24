package Server.Communication;

import java.util.HashSet;
import java.util.Set;

public class DownloadReply implements IReply {
    static final String  token = "REPLY_DOWNLOAD";
    private String state;
    private String title;
    private String author;
    private int year;
    private Set<String> tags;

    public DownloadReply(String state){
        this.state = state;
    }

    public DownloadReply(String state, String title, String author, int year, Set<String> tags){
        this.state = state;
        this.title = title;
        this.author = author;
        this.year = year;
        this.tags = new HashSet<>(tags);
    }

    public static IReply parse(String[] fields){
        if(fields.length == 2) return new DownloadReply(fields[1]);
        Set<String> tmp = new HashSet<>();
        for(int i=5; i<fields.length; i++) tmp.add(fields[i]);
        return new DownloadReply(fields[1], fields[2], fields[3], Integer.parseInt(fields[4]), tmp);
    }

    public String stringSerialize() {
        if(state.equals(ReplyStates.FAILED) || state.equals(ReplyStates.QUEUED))
            return token + "\\|" + state;
        StringBuilder tmp = new StringBuilder(new String(token + "\\|" + state + "\\|" + title + "\\|" + author + "\\|" + year));
        for(String tag : tags) tmp.append("\\|").append(tag);
        System.out.println(tmp.toString());
        return tmp.toString();
    }

    public String getState() {
        return state;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getTags() {
        return tags;
    }
}
