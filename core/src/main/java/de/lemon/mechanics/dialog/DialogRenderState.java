package de.lemon.mechanics.dialog;

public class DialogRenderState {

    public final Dialog dialog;
    public final int tokenIndex;
    public final int charIndex;

    public DialogRenderState(Dialog dialog, int tokenIndex, int charIndex) {
        this.dialog = dialog;
        this.tokenIndex = tokenIndex;
        this.charIndex = charIndex;
    }
}
