package de.lemon.mechanics.dialog;

import de.lemon.logic.interfaces.Copyable;

import java.util.ArrayList;

public class DialogData implements Copyable<DialogData> {
    public ArrayList<String> lines = new ArrayList<>();
    public String speaker;
    public String name;
    public String title;
    public String textureName;
    public int frameWidth;
    public int frameHeight;

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(name);
        string.append(": \n  Speaker: ").append(speaker);
        string.append("\n  lines:");
        for (String l : lines) string.append("\n    ").append(l).append(",");
        string.setLength(string.length() - 1);
        return string.toString();
    }

    @Override
    public DialogData cpy() {
        DialogData data = new DialogData();
        data.lines = new ArrayList<>(lines);
        data.speaker = speaker;
        data.name = name;
        data.title = title;
        data.textureName = textureName;
        data.frameWidth = frameWidth;
        data.frameHeight = frameHeight;
        return data;
    }
}
