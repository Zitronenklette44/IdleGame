package de.lemon.mechanics.dialog;

import de.lemon.logic.enums.TokenType;

import java.util.ArrayList;

public class DialogParser {

    public static ArrayList<DialogToken> parseLine(String line){
        ArrayList<DialogToken> tokens = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {   //for each char in the line
            char c = line.charAt(i);
            if(c == '<'){   //if char is the start of a tag
                if(!currentText.isEmpty()){ //store all currently stored Text as a TEXT token
                    tokens.add(new DialogToken(TokenType.TEXT, currentText.toString()));
                    currentText.setLength(0);   //clear stored text
                }

                int end = line.indexOf('>', i); //locate the end of the tag
                if(end == -1) break;    //if no end is found the line is over so stop the loop

                String tag = line.substring(i + 1, end);    //get the tag
                if(tag.equals("/")){    //sort if the tag is a RESET token
                    tokens.add(new DialogToken(TokenType.RESET, ""));
                }else if(tag.startsWith("icon=")){  //a ICON token
                    tokens.add(new DialogToken(TokenType.ICON, tag.replace("icon=", "")));
                }else { //or a COLOR token
                    tokens.add(new DialogToken(TokenType.COLOR, tag));
                }

                i = end;    //skip chars inside the tag because we already processed them
            }else{
                currentText.append(c);  //add char to our text
            }
        }

        if(!currentText.isEmpty()){ //store all text that is left
            tokens.add(new DialogToken(TokenType.TEXT, currentText.toString()));
        }
        return tokens;  //return tokens
    }
}
