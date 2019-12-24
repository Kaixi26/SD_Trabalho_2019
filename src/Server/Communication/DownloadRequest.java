package Server.Communication;

import java.util.HashSet;
import java.util.Set;

public class DownloadRequest implements IRequest {
    static final String  token = "REQUEST_DOWNLOAD";
    private String username;
    private String password;
    private int id;

    public DownloadRequest(String username, String password, int id){
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String stringSerialize() {
        return token + "\\|" + username + "\\|" + password + "\\|" + id;
    }

    public static IRequest parse(String[] fields){
        return new DownloadRequest(fields[1], fields[2], Integer.parseInt(fields[3]));
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

}
