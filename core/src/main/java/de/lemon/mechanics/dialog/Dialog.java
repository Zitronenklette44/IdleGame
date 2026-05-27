package de.lemon.mechanics.dialog;

import de.lemon.logic.enums.TokenType;
import de.lemon.utilities.DebugLogger;

import java.util.ArrayList;

public class Dialog {

    private int lineIndex = 0;

    private final ArrayList<String> lines;
    private ArrayList<DialogToken> currentTokens;

    private final String speaker;
    private final String name;

    public Dialog(DialogData data) {
        this.lines = new ArrayList<>(data.lines);
        this.name = data.name;
        this.speaker = data.speaker;
        this.currentTokens = new ArrayList<>();
        rebuildTokens();
//        DebugLogger.printList("Tokens", currentTokens);
    }

    public String getLine() {
        return lines.get(lineIndex);
    }

    public boolean next() {
        if (lineIndex + 1 >= lines.size()) return false;
        lineIndex++;
        rebuildTokens();
//        DebugLogger.printList("Tokens", currentTokens);
        return true;
    }

    public void reset() {
        lineIndex = 0;
        rebuildTokens();
    }

    public boolean isLastLine() {
        return lineIndex >= lines.size() - 1;
    }

    private void rebuildTokens() {
        currentTokens.clear();
        currentTokens.addAll(DialogParser.parseLine(getLine()));
    }

    public void replaceVariableWithValue(String variableName, String value) {
        lines.replaceAll(s -> s.replace("{" + variableName + "}", value));
        rebuildTokens();
    }

    public int getTokensSize() {
        return currentTokens.size();
    }

    public DialogToken getToken(int index) {
        return currentTokens.get(index);
    }

    public int getCurrentTokenLength(int tokenIndex) {
        if (tokenIndex < 0 || tokenIndex >= currentTokens.size()) return 0;
        return currentTokens.get(tokenIndex).value.length();
    }

    public int getLastTokenLength() {
        if (currentTokens.isEmpty()) return 0;
        return currentTokens.get(currentTokens.size() - 1).value.length();
    }

    public boolean allVisible(int tokenIndex, int charIndex) {
        if (currentTokens.isEmpty()) return true;
        int lastTokenIndex = currentTokens.size() - 1;
        int lastCharIndex = currentTokens.get(lastTokenIndex).value.length();
        return tokenIndex >= lastTokenIndex
            && charIndex >= lastCharIndex;
    }

    public String getVisibleText(int tokenIndex, int charIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tokenIndex && i < currentTokens.size(); i++) {
            builder.append(currentTokens.get(i).value);
        }
        if (tokenIndex < currentTokens.size()) {
            String partial = currentTokens.get(tokenIndex).value;
            builder.append(partial,0,Math.min(charIndex, partial.length()));
        }
        return builder.toString();
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getName() {
        return name;
    }

    public ArrayList<DialogToken> getVisibleTokens(int tokenIndex, int charIndex) {
        ArrayList<DialogToken> result = new ArrayList<>();

        for (int i = 0; i < tokenIndex && i < currentTokens.size(); i++) result.add(currentTokens.get(i));

        if (tokenIndex < currentTokens.size()) {
            DialogToken token = currentTokens.get(tokenIndex);
            String partial = token.value.substring(0, Math.min(charIndex, token.value.length()));
            if (token.type == TokenType.TEXT) {
                result.add(new DialogToken(token.type, partial));
            } else {
                result.add(token);
            }
        }

        return result;
    }
}
