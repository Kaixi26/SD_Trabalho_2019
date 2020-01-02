package Server.AccountManager;

import Server.SoundCloud.Song;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountManager {
    private Map<String, Account> accounts = new HashMap<>();
    private final Lock l = new ReentrantLock();

    public AccountManager(){
    }

    public void newAccount(String username, String password) throws InvalidUsernameException {
        l.lock();
        try{
            if(accounts.containsKey(username)) throw new InvalidUsernameException("The username '" + username + "' is already in use.");
            accounts.put(username, new Account(username, password));
        }finally {
            l.unlock();
        }
    }

    public void authenticate(String username, String password) throws FailedAuthenticationException {
        l.lock();
        try{
            Account tmp = accounts.get(username);
            if(tmp == null) throw new FailedAuthenticationException("Username does not exist.");
            else if(!tmp.getPassword().equals(password)) throw new FailedAuthenticationException("Invalid password.");
        } finally {
            l.unlock();
        }
    }
}
