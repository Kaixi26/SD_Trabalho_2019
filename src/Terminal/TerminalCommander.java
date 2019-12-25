package Terminal;

import static java.lang.Thread.sleep;

public class TerminalCommander {

    private static final String escStr = "\u001b[";
    private StringBuilder buffer = new StringBuilder();

    public TerminalCommander(){
    }

    public TerminalCommander saveCursor(){
        buffer.append(escStr + "s");
        return this;
    }

    public TerminalCommander restoreCursor(){
        buffer.append(escStr + "u");
        return this;
    }

    public TerminalCommander cursorGoto(int x, int y){
        buffer.append(escStr).append(y).append(";").append(x).append("H");
        return this;
    }

    public TerminalCommander cursorGotoLineStart(){
        return this.cursorMoveBack(10000);
    }

    public TerminalCommander cursorMoveDown(int v){
        buffer.append(escStr).append(v).append("B");
        return this;
    }

    public TerminalCommander cursorMoveUp(int v){
        buffer.append(escStr).append(v).append("A");
        return this;
    }

    public TerminalCommander cursorMoveBack(int v){
        buffer.append(escStr).append(v).append("D");
        return this;
    }

    public TerminalCommander cursorMoveFoward(int v){
        buffer.append(escStr).append(v).append("C");
        return this;
    }

    public TerminalCommander clearFull(){
        buffer.append(escStr + "2J");
        buffer.append(escStr + "1;1H");
        return this;
    }

    public TerminalCommander clearEntireLine(){
        buffer.append(escStr + "2K");
        buffer.append(escStr + "1000D");
        return this;
    }

    public TerminalCommander print(String str){
        buffer.append(str);
        return this;
    }

    public TerminalCommander println(String str){
        buffer.append(str).append("\n");
        return this;
    }

    public void exec(){
        System.out.print(buffer.toString());
        System.out.flush();
        buffer = new StringBuilder();
    }

    public static void main(String[] args) throws Exception {
        TerminalCommander thandler = new TerminalCommander();
        thandler.print("hi!").exec();
        sleep(1000);
        thandler.clearEntireLine().exec();
        sleep(1000);
        thandler.println("bye!").exec();
        sleep(1000);
    }

}
