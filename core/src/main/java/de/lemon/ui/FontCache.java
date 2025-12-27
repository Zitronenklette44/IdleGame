package de.lemon.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontCache {

    private static FreeTypeFontGenerator generator;
    private static Map<FontKey, BitmapFont> fonts = new HashMap<>();

    public static boolean isLoaded = false;

    public static void init(FileHandle font){
        generator = new FreeTypeFontGenerator(font);
        isLoaded = true;
    }

    public static BitmapFont getFont(int size, Color color){
        if(size <= 0) return null;
        FontKey fontKey = new FontKey(size, color);
        if(fonts.containsKey(fontKey)) return fonts.get(fontKey);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;

        BitmapFont bitmap = generator.generateFont(parameter);
        fonts.put(fontKey, bitmap);
        return bitmap;
    }

    public static BitmapFont getFont(int size){
        return getFont(size, Color.WHITE);
    }

    public static int calculateFontSize(float width) {
        int base = 12;
        int steps = 15;
        int increase = 1;

        int stepCount = (int) (width / steps);
        //stepCount = Math.min(stepCount, 11);

        return base + stepCount * increase;
    }

    public static void dispose(){
        for(BitmapFont bF : fonts.values()){
            bF.dispose();
        }
        fonts.clear();
        generator.dispose();
    }

}
