package Tests;

import Server.TransferManager.TransferManager;

import static java.lang.Thread.sleep;

public class TransferReserveThread {

    public static void main(String[] args) throws Exception {
        TransferManager tm = new TransferManager();
        Thread tmp[] = new Thread[5];
        for(int i=0; i<tmp.length; i++)
            tmp[i] = new TransferReservationTests(tm, "Thread " + i);
        tmp[0].start();
        sleep(50);
        tmp[1].start();
        sleep(50);
        tmp[2].start();
        sleep(50);
        tmp[3].start();
        sleep(50);
        tmp[4].start();
        sleep(50);
        for(int i=0; i<tmp.length; i++)
            tmp[i].join();
    }
}
