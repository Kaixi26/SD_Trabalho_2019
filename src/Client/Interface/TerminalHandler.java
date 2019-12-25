package Client.Interface;

import Terminal.TerminalCommander;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TerminalHandler {
    private TerminalCommander termComm = new TerminalCommander();
    private Lock l = new ReentrantLock();

    private int nextID = 0;
    private Map<String, Integer> subPaneStartIndex = new HashMap<>();
    private Map<String, Integer> subPaneLines = new HashMap<>();
    private Map<String, List<OutputReservation>> subPaneQueues = new HashMap<>();

    private final int mainPaneLines;
    private boolean inUseMainPane = false;
    private boolean inUseInput = false;

    public TerminalHandler(int mainPaneLines, List<Map.Entry<String, Integer>> subPanes){
        int i=1;
        this.mainPaneLines = mainPaneLines;
        termComm.clearFull().print("$> ").saveCursor();
        i += 1 + mainPaneLines;

        for(Map.Entry<String, Integer> e : subPanes){
            termComm.cursorGoto(1, i).print("## " + e.getKey() + " (0)");// + " " + i + " " + e.getValue());
            this.subPaneStartIndex.put(e.getKey(), i);
            this.subPaneLines.put(e.getKey(), e.getValue());
            this.subPaneQueues.put(e.getKey(), new ArrayList<OutputReservation>());
            i += 1 + e.getValue();
        }
        termComm.cursorGoto(1, i).print("#-------------------------#").restoreCursor().exec();
    }

    public MainPaneReservation reserveMainPane(){
        l.lock();
        try {
            if(inUseMainPane) return null;
            else {
                inUseMainPane = true;
                return new MainPaneReservation(this);
            }
        } finally {
            l.unlock();
        }
    }

    void freeMainPane(){
        l.lock();
        inUseMainPane = false;
        l.unlock();
    }

    void setTextMainPane(String text){
        String[] lines = text.split("\n");
        int max = Integer.min(lines.length, mainPaneLines);
        l.lock();
        try {
            termComm.saveCursor();
            for(int i=1; i<=mainPaneLines; i++)
                termComm.cursorGoto(1, 1+i).clearEntireLine();
            termComm.cursorGoto(1, 2);
            for(int i=0; i<max; i++)
                termComm.print("\t").println(lines[i]);
            termComm.restoreCursor().exec();
        } finally {
            l.unlock();
        }
    }


    public InputReservation reserveInput(){
        l.lock();
        try {
            if(inUseInput) return null;
            else {
                inUseInput = true;
                return new InputReservation(this);
            }
        } finally {
            l.unlock();
        }
    }

    void freeInput(){
        l.lock();
        inUseInput = false;
        l.unlock();
    }

    void resetInput(){
        l.lock();
        try {
            termComm.cursorGoto(1,1).clearEntireLine().print("$> ").exec();
        }finally {
            l.unlock();
        }
    }

    public void exit(int status){
        l.lock();
        termComm.clearFull().exec();
        System.exit(status);
    }

    void updateText(OutputReservation res, String text){
        l.lock();
        try {
            String pane = res.getPane();
            int sInd = subPaneStartIndex.get(pane);
            int lines = subPaneLines.get(pane);
            List<OutputReservation> queue = subPaneQueues.get(pane);

            if (text == null) queue.removeIf(o -> o.getId() == res.getId());
            else res.setText(text);

            termComm.saveCursor().cursorGoto(1, sInd).clearEntireLine().print("## " + pane + " (" + queue.stream().filter(o -> o.getText() != null).count() + ")");
            for (int j = 0; j < lines; j++)
                termComm.cursorGoto(1, 1 + sInd + j).clearEntireLine();
            int i = 0;
            for (OutputReservation tmp : queue)
                if (i >= lines) break;
                else if (tmp.getText() != null) termComm.cursorGoto(1, 1 + sInd + (i++)).print("\t" + tmp.getText());
            termComm.restoreCursor().exec();
        } finally {
            l.unlock();
        }
    }

    public OutputReservation reservePaneLine(String pane){
        l.lock();
        try {
            List<OutputReservation> tmp = subPaneQueues.get(pane);
            if(tmp == null) return null;
            else {
                OutputReservation res = new OutputReservation(this, nextID++, pane);
                tmp.add(res);
                return res;
            }
        } finally {
            l.unlock();
        }
    }

}
