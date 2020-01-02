package Client;

import Client.Handlers.CommandHandler;
import Client.Handlers.DownloadHandler;
import Client.Interface.InputReservation;
import Client.Interface.MainPaneReservation;
import Client.Interface.OutputReservation;
import Client.Interface.TerminalHandler;
import Constants.ClientConstants;
import Terminal.TerminalCommander;

import java.net.Socket;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) throws Exception {
        List<Map.Entry<String, Integer>> subpanes = new ArrayList<>();
        subpanes.add(new AbstractMap.SimpleImmutableEntry<>("QUEUED", ClientConstants.LINES_QUEUED));
        subpanes.add(new AbstractMap.SimpleImmutableEntry<>("PROGRESSING", ClientConstants.LINES_PROGRESSING));
        subpanes.add(new AbstractMap.SimpleImmutableEntry<>("NOTIFICATIONS", ClientConstants.LINES_NOTIFICATIONS));

        TerminalHandler termHandler = new TerminalHandler(6, subpanes);
        MainPaneReservation mainPane = termHandler.reserveMainPane();
        InputReservation inRes = termHandler.reserveInput();

        CommandHandler commHandler = new CommandHandler();

        while (true){
            String command = inRes.readLine();
            if(command.equals("exit")) break;
            commHandler.execute(termHandler, mainPane, command);
        }
        termHandler.exit(0);
    }
}
