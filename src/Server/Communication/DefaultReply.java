package Server.Communication;

public class DefaultReply implements IReply {
    static final String  token = "REPLY_DEFAULT";
    private String state;

    public DefaultReply(String state){
        this.state = state;
    }

    public String stringSerialize() {
        return token + "\\|" + state;
    }

    public String getState() {
        return state;
    }

}
