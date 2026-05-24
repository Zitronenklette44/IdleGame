package de.lemon.mechanics.dialog;

import java.util.ArrayList;

public class DialogData {
    public ArrayList<String> lines = new ArrayList<>();
    public String speaker;
    public String name;

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(name);
        string.append(": \n  Speaker: ").append(speaker);
        string.append("\n  lines:");
        for (String l : lines) string.append("\n    ").append(l).append(",");
        string.setLength(string.length() - 1);
        return string.toString();
    }
}
