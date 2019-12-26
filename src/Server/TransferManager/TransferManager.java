package Server.TransferManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransferManager {
    private static final int MAX_DOWN = 3;
    private int nextID = 0;
    private Lock l = new ReentrantLock();
    private List<TransferReservation> reservationQueues = new ArrayList<>();
    private TransferReservation[] transferSlots = new TransferReservation[MAX_DOWN];
    private Map<String, Integer> transferSlotsUsers = new HashMap<>();

    public TransferManager(){

    }

    private int freeSlots(){
        return (int) Arrays.stream(transferSlots).filter(Objects::isNull).count();
    }

    private void notifyNext(){
        if(reservationQueues.size() == 0)  return;
        TransferReservation next = null;
        for(int i=0; i<reservationQueues.size(); i++)
            if(transferSlotsUsers.getOrDefault(reservationQueues.get(i).username, 0) == 0){
                next = reservationQueues.get(i);
                reservationQueues.remove(i);
                break;
            }
        if(next == null) {
            next = reservationQueues.get(0);
            reservationQueues.remove(0);
        }
        for(int i=0; i<transferSlots.length; i++)
            if(transferSlots[i] == null) {
                transferSlots[i] = next;
                break;
            }
        next.l.lock();
        try{
            next.inQueue = false;
            next.cond.signal();
        }finally {
            transferSlotsUsers.put(next.username, transferSlotsUsers.getOrDefault(next.username, 0) + 1);
            next.l.unlock();
        }
    }

    public TransferReservation reserveTransfer(String username){
        l.lock();
        try {
            TransferReservation tmp = new TransferReservation(this, username, nextID++);
            for(int i=0; i<transferSlots.length; i++)
                if(transferSlots[i] == null){
                    transferSlots[i] = tmp;
                    tmp.inQueue = false;
                    transferSlotsUsers.put(tmp.username, transferSlotsUsers.getOrDefault(tmp.username, 0) + 1);
                    break;
                }
            return tmp;
        } finally {
            l.unlock();
        }
    }

    void addToQueue(TransferReservation tRes){
        l.lock();
        try {
            reservationQueues.add(tRes);
        } finally {
            l.unlock();
        }
    }

    void free(TransferReservation tRes){
        l.lock();
        try {
            for(int i=0; i<transferSlots.length; i++) {
                if (transferSlots[i] != null && transferSlots[i].equals(tRes)) {
                    transferSlots[i] = null;
                    Integer activeUserTransfers = transferSlotsUsers.get(tRes.username);
                    if(activeUserTransfers == 1) transferSlotsUsers.remove(tRes.username);
                    else transferSlotsUsers.put(tRes.username, activeUserTransfers-1);
                    break;
                }
            }
            notifyNext();
        } finally {
            l.unlock();
        }
    }
}
