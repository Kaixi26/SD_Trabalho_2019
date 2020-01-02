package Server;

import Constants.ServerConstants;
import Server.AccountManager.AccountManager;
import Server.SoundCloud.SoundCloud;
import Server.TransferManager.TransferManager;

public class Manager {
    public AccountManager am;
    public SoundCloud sc;
    public TransferManager tm;

    Manager(){
        am = new AccountManager();
        sc = new SoundCloud();
        tm = new TransferManager(ServerConstants.MAX_DOWN);
    }
}
