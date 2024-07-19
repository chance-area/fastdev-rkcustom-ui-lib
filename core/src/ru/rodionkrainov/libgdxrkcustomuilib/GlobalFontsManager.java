package ru.rodionkrainov.libgdxrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GlobalFontsManager {
    private static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>°φ";

    private static FreeTypeFontGenerator fontGenerator = null;
    private static FreeTypeFontParameter fontParameter = null;

    public static BitmapFont BP_FONT_FROM_5_TO_10  = null;
    public static BitmapFont BP_FONT_FROM_11_TO_18 = null;
    public static BitmapFont BP_FONT_FROM_19_TO_30 = null;
    public static BitmapFont BP_FONT_FROM_31_TO_44 = null;
    public static BitmapFont BP_FONT_FROM_45_TO_60 = null;

    public static void loadAndInit(String _fontFilePath) {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(_fontFilePath));
        fontParameter = new FreeTypeFontParameter();

        // base settings
        fontParameter.characters = FONT_CHARS;
        fontParameter.borderWidth = 0.05f;
        fontParameter.borderStraight = true;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.color = Color.WHITE;
        fontParameter.spaceX = 2;
        fontParameter.genMipMaps = true;
        fontParameter.minFilter = Texture.TextureFilter.MipMap;
        fontParameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        fontParameter.incremental = false;

        // init bitmap fonts (optimization)
        for (int i = 0; i < 5; i++) {
            int fontSize;
            if (i == 0) fontSize      = 15;
            else if (i == 1) fontSize = 29;
            else if (i == 2) fontSize = 49;
            else if (i == 3) fontSize = 75;
            else             fontSize = 105;

            FreeTypeFontParameter fontParameter = GlobalFontsManager.getFontParameter();
            fontParameter.size = fontSize;
            fontParameter.color = Color.WHITE;

            BitmapFont bitmapFont = GlobalFontsManager.getFontGenerator().generateFont(fontParameter);
            bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
            bitmapFont.setUseIntegerPositions(false);

            if (i == 0)      BP_FONT_FROM_5_TO_10  = bitmapFont;
            else if (i == 1) BP_FONT_FROM_11_TO_18 = bitmapFont;
            else if (i == 2) BP_FONT_FROM_19_TO_30 = bitmapFont;
            else if (i == 3) BP_FONT_FROM_31_TO_44 = bitmapFont;
            else             BP_FONT_FROM_45_TO_60 = bitmapFont;
        }
    }

    public static FreeTypeFontGenerator getFontGenerator() {
        return fontGenerator;
    }

    public static FreeTypeFontParameter getFontParameter() {
        return fontParameter;
    }

    public static void dispose() {
        if (fontGenerator != null) fontGenerator.dispose();
    }
}
