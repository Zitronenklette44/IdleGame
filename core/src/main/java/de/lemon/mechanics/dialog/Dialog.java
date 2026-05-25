package de.lemon.mechanics.dialog;

import java.util.ArrayList;

public class Dialog {

    private int lineIndex = 0;
    private ArrayList<String> lines;
    String speaker;
    String name;

    public Dialog(DialogData data){
        lines = data.lines;
        name = data.name;;
        speaker = data.speaker;
    }

    public String getLine(){
        return lines.get(lineIndex);
    }

    /**
     * increases the index to display the next line of the Dialog
     * @return true if an increase was possible false if already at the last line
     */
    public boolean next(){
        if(lineIndex + 1 >= lines.size()) return false;
        lineIndex++;
        return true;
    }

    public void reset(){
        lineIndex = 0;
    }

    public boolean isLastLine(){
        return lineIndex >= lines.size() - 1;
    }

    public String getChars(int chars) {
        if(allVisible(chars)) return getLine();
        StringBuilder string = new StringBuilder(getLine());
        string.setLength(chars);
        return string.toString();
    }

    public boolean allVisible(int chars) {
        return getLine().length() - 1 <= chars;
    }

    public int getMaxChars() {
        return getLine().length() - 1;
    }
}
