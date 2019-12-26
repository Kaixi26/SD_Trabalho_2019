package Tests;

import Server.Communication.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Communication {
    public static void main(String[] args) throws Exception {
        String username = "testUser";
        String password = "123456";
        List<String> tmp = new ArrayList<>();
        tmp.add("Hi");
        tmp.add("world");
        tmp.add("test");
        for(String s : tmp)
            System.out.println(s);
        //IRequest req = new UploadRequest(username, password, ".", ".", 1, new HashSet<>());
        //IReply rep = new UploadReply(ReplyStates.SUCESS);
        //IRequest reqParsed = Requests.parse(req.stringSerialize());
        //IReply repParsed = Replies.parse(rep.stringSerialize());
        //assert reqParsed instanceof UploadRequest : "Failed parsing upload request." ;
        //assert repParsed instanceof UploadReply : "Failed parsing upload reply.";
        //assert ((UploadRequest) reqParsed).getUsername().equals(username) : "Wrong username on request.";
        //assert ((UploadRequest) reqParsed).getPassword().equals(password) : "Wrong password on request.";
        //assert ((UploadReply) repParsed).getState().equals(ReplyStates.SUCESS) : "Wrong state on reply.";

    }
}
