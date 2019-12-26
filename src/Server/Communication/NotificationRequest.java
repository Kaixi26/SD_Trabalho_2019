package Server.Communication;

public class NotificationRequest implements IRequest {
    static final String  token = "REQUEST_NOTIFICATION";
    private String username;
    private String password;

    public NotificationRequest(String username, String password){
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
        return new NotificationRequest(fields[1], fields[2]);
    }

}
