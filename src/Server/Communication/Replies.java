package Server.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Replies {

    public static void send(Socket s, IReply r) throws IOException {
        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.println(r.stringSerialize());
        out.flush();
    }

    public static IReply read(Socket s) throws IOException, InvalidReplyException {
        BufferedReader in = new BufferedReader(new InputStreamReader((s.getInputStream())));
        return Replies.parse(in.readLine());
    }

    public static IReply parse(String s) throws InvalidReplyException {
        String[] fields = s.split("\\\\\\|");
        try{
            switch (fields[0]){
                case UploadReply.token:
                    return new UploadReply(fields[1]);
                case DownloadReply.token:
                    return DownloadReply.parse(fields);
                case DefaultReply.token:
                    return new DefaultReply(fields[1]);
            }
        }
        catch (Exception e){
            throw new InvalidReplyException();
        }
        throw new InvalidReplyException();
    }
}
