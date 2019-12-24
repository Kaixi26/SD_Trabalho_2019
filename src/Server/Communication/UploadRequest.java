package Server.Communication;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class UploadRequest implements IRequest {
    static final String  token = "REQUEST_UPLOAD";
    private String username;
    private String password;
    private String title;
    private String author;
    private int year;
    private Set<String> tags;

    public UploadRequest(String username, String password, String title, String author, int year, Set<String> tags){
        this.username = username;
        this.password = password;
        this.title = title;
        this.author = author;
        this.year = year;
        this.tags = new HashSet<>(tags);
    }

    public String stringSerialize() {
        StringBuilder tmp = new StringBuilder(token + "\\|" + username + "\\|" + password + "\\|" + title + "\\|" + author + "\\|" + year);
        for(String tag : tags) tmp.append("\\|").append(tag);
        return tmp.toString();
    }

    public static IRequest parse(String[] fields){
        Set<String> tmp = new HashSet<>();
        for(int i=6; i<fields.length; i++) tmp.add(fields[i]);
        return new UploadRequest(fields[1], fields[2], fields[3], fields[4], Integer.parseInt(fields[5]), tmp);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
        return tags;
    }


}
