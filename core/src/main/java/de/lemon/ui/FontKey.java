package de.lemon.ui;

import com.badlogic.gdx.graphics.Color;

public class FontKey {

    int size;
    Color color;

    public FontKey(int size, Color color){
        this.size = size;
        this.color = new Color(color);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof FontKey)) return false;
        FontKey o = (FontKey) obj;
        return size == o.size && color.equals(o.color);
    }
}
