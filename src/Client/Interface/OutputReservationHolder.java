package Client.Interface;

public class OutputReservationHolder extends Thread {
    private OutputReservation res;
    private long millis;

    public OutputReservationHolder(OutputReservation res, long millis){
        this.res = res;
        this.millis = millis;
    }

    @Override
    public void run() {
        try {
            sleep(millis);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            res.free();
        }
    }
}
