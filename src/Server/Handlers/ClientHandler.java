package Server.Handlers;

import Server.AccountManager.FailedAuthenticationException;
import Server.AccountManager.InvalidUsernameException;
import Server.Communication.*;
import Server.Manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private Manager man;

    public ClientHandler(Socket clientSocket, Manager man){
        this.clientSocket = clientSocket;
        this.man = man;
    }

    private void disconnect() throws Exception {
        clientSocket.shutdownOutput();
        clientSocket.shutdownInput();
        clientSocket.close();
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            IRequest req = Requests.parse(in.readLine());

            if(!(req instanceof RegisterRequest))
                try{man.am.authenticate(req.getUsername(), req.getPassword());
                } catch (FailedAuthenticationException e){System.out.println("Failed authentication.");disconnect();}

            if (req instanceof UploadRequest)
                new UploadHandler(clientSocket, man, (UploadRequest) req).start();
            else if (req instanceof DownloadRequest)
                new DownloadHandler(clientSocket, man, (DownloadRequest) req).start();
            else if (req instanceof RegisterRequest)
                new RegisterHandler(clientSocket, man, (RegisterRequest) req).start();
            else if (req instanceof SearchRequest)
                new SearchHandler(clientSocket, man, (SearchRequest) req).start();
            else
                disconnect();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
