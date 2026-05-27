package de.lemon.mechanics.dialog;

import de.lemon.logic.enums.TokenType;

public class DialogToken {

    public TokenType type;
    public final String value;

    public DialogToken(TokenType type, String value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" + type.name() + "|" + value + "}";
    }
}
