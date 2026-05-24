package de.lemon.logic.enums;

public enum Plants {
    BLATTRUBIN,
    GRAPES;

    @Override
    public String toString() {
        switch (name()){
            case "BLATTRUBIN": return "blattRubin";
            case "GRAPES": return "grapes";
        }
        return super.toString();
    }
}
