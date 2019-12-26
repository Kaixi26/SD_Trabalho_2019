package Tests;

import Server.TransferManager.TransferManager;
import Server.TransferManager.TransferReservation;

import java.util.Random;

public class TransferReservationTests extends Thread {
    TransferManager tm;
    String str;

    TransferReservationTests(TransferManager tm, String str){
        this.tm = tm;
        this.str = str;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            TransferReservation tRes = tm.reserveTransfer(str);
            if(tRes.isInQueue()) {
                System.out.println(str + ": In queue.");
                tRes.awaitSpot();
            }
            System.out.println(str + ": Started.");
            sleep((long) ((rand.nextDouble() + 0.5) * 2000));
            System.out.println(str + ": Ended.");
            tRes.free();

        }catch (Exception e){
            System.out.println(str + "->" + e.toString());
        }
    }
}
