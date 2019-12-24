package Server.Communication;

public class UploadReply implements IReply {
    static final String  token = "REPLY_UPLOAD";
    private String state;

    public UploadReply(String state){
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String stringSerialize() {
        return token + "\\|" + state;
    }

}
