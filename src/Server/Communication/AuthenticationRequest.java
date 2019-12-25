package Server.Communication;

public class AuthenticationRequest implements IRequest {
    static final String  token = "REQUEST_AUTHENTICATE";
    private String username;
    private String password;

    public AuthenticationRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String stringSerialize() {
        return token + "\\|" + username + "\\|" + password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static IRequest parse(String[] fields){
        return new AuthenticationRequest(fields[1], fields[2]);
    }

}
