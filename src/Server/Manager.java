package Server;

import Server.AccountManager.AccountManager;
import Server.SoundCloud.SoundCloud;

public class Manager {
    public AccountManager am;
    public SoundCloud sc;

    Manager(){
        am = new AccountManager();
        sc = new SoundCloud();
    }
}
