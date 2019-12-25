package Server.Communication;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Requests {

    public static void send(Socket s, IRequest r) throws IOException {
        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.println(r.stringSerialize());
        out.flush();
    }

    public static IRequest parse(String s) throws InvalidRequestException {
        String[] fields = s.split("\\\\\\|");
        try{
            System.out.println(s);
            switch (fields[0]){
                case UploadRequest.token:
                    return UploadRequest.parse(fields);
                case DownloadRequest.token:
                    return DownloadRequest.parse(fields);
                case RegisterRequest.token:
                    return RegisterRequest.parse(fields);
                case SearchRequest.token:
                    return SearchRequest.parse(fields);
                case AuthenticationRequest.token:
                    return AuthenticationRequest.parse(fields);
            }
        }
        catch (Exception e){
            throw new InvalidRequestException();
        }
        throw new InvalidRequestException();
    }
}
