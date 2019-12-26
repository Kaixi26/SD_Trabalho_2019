package Client.Handlers;

import Client.Interface.HelpText;
import Client.Interface.MainPaneReservation;
import Client.Interface.TerminalHandler;
import Server.Communication.AuthenticationRequest;
import Server.Communication.Replies;
import Server.Communication.ReplyStates;
import Server.Communication.Requests;

import java.io.IOException;
import java.net.Socket;

public class CommandHandler {

    private String username = null;
    private String password = null;

    public CommandHandler(){

    }

    static private Socket newSocket() throws IOException  {
        return new Socket("localhost", 12345);
    }

    public void execute(TerminalHandler termHandler, MainPaneReservation res, String command){
        try {
            String[] fields = command.split(" ");
            res.setText("");
            switch (fields[0]){
                case "help":
                    if(fields.length == 1) res.setText(HelpText.help);
                    else switch (fields[1]) {
                        case "help":
                            res.setText(HelpText.help_help);
                            break;
                        case "register":
                            res.setText(HelpText.help_register);
                            break;
                        case "search":
                            res.setText(HelpText.help_search);
                            break;
                        case "authenticate":
                            res.setText(HelpText.help_authenticate);
                            break;
                        case "upload":
                            res.setText(HelpText.help_upload);
                            break;
                        default:
                            res.setText("Invalid command.");
                        case "download":
                            res.setText(HelpText.help_download);
                            break;
                    }
                    break;
                case "register":
                    if(fields.length == 3){
                        Thread tmp = new RegisterHandler(CommandHandler.newSocket(), res, fields[1], fields[2]);
                        tmp.start();
                        tmp.join();
                    } else res.setText("Invalid command.");
                    break;
                case "authenticate":
                    if(username != null)
                        res.setText("Already authenticated as " + username + ".");
                    else if(fields.length == 3) {
                        Socket tmp = newSocket();
                        Requests.send(tmp, new AuthenticationRequest(fields[1], fields[2]));
                        if(Replies.read(tmp).getState().equals(ReplyStates.SUCESS)) {
                            username = fields[1];
                            password = fields[2];
                            res.setText("Successfully authenticated.");
                            new NotificationHandler(newSocket(), termHandler, username, password).start();
                        } else res.setText("Failed authentication.");

                    } else res.setText("Invalid command.");
                    break;
                case "search":
                    if(username == null || password == null) res.setText("You need to authenticate first.");
                    else {
                        Thread tmp = new SearchHandler(newSocket(), res, Integer.parseInt(fields[2]), username, password, fields[1]);
                        tmp.start();
                        tmp.join();
                    }
                    break;
                case "upload":
                    if(username == null || password == null) res.setText("You need to authenticate first.");
                    else if(fields.length < 5) res.setText("Not enough arguments.");
                    else new UploadHandler(newSocket(), termHandler, username, password, fields).start();
                    break;
                case "download":
                    if(username == null || password == null) res.setText("You need to authenticate first.");
                    else if(fields.length < 3) res.setText("Not enough arguments.");
                    else new DownloadHandler(newSocket(), termHandler, username, password, Integer.parseInt(fields[2]), fields[1]).start();
                    break;
                default:
                    res.setText("Invalid command.");
            }
        } catch (IOException e) {
            res.setText("Unable to communicate with server.");
        } catch (Exception e){
            res.setText("Invalid command.");
            //res.setText(e.toString());
        }
    }
}
