package Client.Interface;

public class OutputReservation {

    private TerminalHandler parent;
    private int id;
    private String text = null;
    private String pane;

    OutputReservation(TerminalHandler parent, int id, String pane){
        this.id = id;
        this.parent = parent;
        this.pane = pane;
    }

    int getId() {
        return id;
    }

    String getPane() {
        return pane;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    public void free(){
        parent.updateText(this, null);
        this.parent = null;
        this.pane = null;
    }

    public void updateText(String text){
        if(text == null) return;
        parent.updateText(this, text.split("\n")[0]);
    }

}
