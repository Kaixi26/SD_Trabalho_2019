package Server.Communication;

public class SearchRequest implements IRequest {
    static final String  token = "REQUEST_SEARCH";
    private String username;
    private String password;
    private String tag;


    public SearchRequest(String username, String password, String tag){
        this.username = username;
        this.password = password;
        this.tag = tag;
    }

    public String stringSerialize() {
        return token + "\\|" + username + "\\|" + password + "\\|" + tag;
    }

    public static IRequest parse(String[] fields) {
        return new SearchRequest(fields[1], fields[2], fields[3]);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTag() {
        return tag;
    }

}
