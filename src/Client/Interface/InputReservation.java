package Client.Interface;

import java.util.Scanner;

public class InputReservation {
    TerminalHandler parent;
    Scanner scan = new Scanner(System.in);

    InputReservation(TerminalHandler parent){
        this.parent = parent;
    }

    public String readLine(){
        String tmp = scan.nextLine();
        parent.resetInput();
        return tmp;
    }

    public void free(){
        parent.freeInput();
        parent = null;
        scan = null;
    }

}
