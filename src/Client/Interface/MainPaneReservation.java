package Client.Interface;

public class MainPaneReservation {

    private TerminalHandler parent;

    MainPaneReservation(TerminalHandler parent){
        this.parent = parent;
    }

    public void setText(String text){
        parent.setTextMainPane(text);
    }

    public void free(){
        this.parent.freeMainPane();
        parent = null;
    }

}
