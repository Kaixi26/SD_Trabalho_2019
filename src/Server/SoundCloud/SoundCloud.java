package Server.SoundCloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SoundCloud {
    private HashMap<Integer, Song> songs = new HashMap<>();
    private final Lock l = new ReentrantLock();
    private final Condition notificationStart = l.newCondition();
    private final Condition notificationEnd = l.newCondition();
    private int waitingNotifications = 0;
    private Song notifySong = null;
    private int N = 0;

    public SoundCloud(){
    }

    public int reserveKey(){
        l.lock();
        try {
            return N++;
        } finally {
            l.unlock();
        }
    }

    public void add(int reservedKey, String title, String author, int year, Set<String> tags) {
        l.lock();
        try {
            Song tmp = new Song(title, author, year, tags);
            songs.put(reservedKey,tmp);
            if(waitingNotifications > 0) notifySong = tmp;
            notificationStart.signalAll();
        } finally {
            l.unlock();
        }
    }

    public Map<Integer, Song> search(String tag){
        l.lock();
        try {
            HashMap<Integer, Song> tmp = new HashMap<>();
            for (Map.Entry<Integer, Song> e : songs.entrySet())
                if(e.getValue().hasTag(tag)) tmp.put(e.getKey(), e.getValue());
            return tmp;
        }finally {
            l.unlock();
        }
    }

    public Song getSong(int id){
        return songs.get(id);
    }

    public Song awaitNew(){
        l.lock();
        try {
            while(notifySong != null)
                try { notificationEnd.await(); } catch (Exception e) { e.printStackTrace(); }

            waitingNotifications++;
            while (notifySong == null) {
                try { notificationStart.await(); } catch (Exception e) { e.printStackTrace(); }
            }
            waitingNotifications--;
            if(waitingNotifications <= 0) {
                Song tmp = notifySong;
                notifySong = null;
                notificationEnd.signalAll();
                return tmp;
            }
            return notifySong;
        } finally {
            l.unlock();
        }
    }

}
