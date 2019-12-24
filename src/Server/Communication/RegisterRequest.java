package Server.Communication;

import java.util.HashSet;
import java.util.Set;

public class RegisterRequest implements IRequest {
    static final String  token = "REQUEST_REGISTER";
    private String username;
    private String password;

    public RegisterRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String stringSerialize() {
        return token + "\\|" + username + "\\|" + password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public static IRequest parse(String[] fields){
        return new RegisterRequest(fields[1], fields[2]);
    }

}
