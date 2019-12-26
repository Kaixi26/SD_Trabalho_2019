package Server.TransferManager;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferReservation {
    final Lock l = new ReentrantLock();
    final Condition cond = l.newCondition();
    final TransferManager parent;
    final String username;
    final private int id;
    Boolean inQueue = true;

    TransferReservation(TransferManager parent, String username, int id){
        this.parent = parent;
        this.username = username;
        this.id = id;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public boolean isActive(){
        return !inQueue;
    }

    public void awaitSpot(){
        if(inQueue == null || !inQueue) return;
        l.lock();
        parent.addToQueue(this);
        try {
            while (inQueue)
                try { cond.await(); } catch (Exception e) { e.printStackTrace(); }
        } finally {
            l.unlock();
        }
    }

    public void free(){
        parent.free(this);
    }

    private boolean equals(TransferReservation tRes){
        return this.id == tRes.getId();
    }

    private int getId() {
        return id;
    }
}
