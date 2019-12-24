package Server.SoundCloud;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SoundCloud {
    private HashMap<Integer, Song> songs = new HashMap<>();
    private final Lock l = new ReentrantLock();
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
            songs.put(reservedKey, new Song(title, author, year, tags));
        } finally {
            l.unlock();
        }
    }

    public HashMap<Integer, Song> search(String tag){
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

}
