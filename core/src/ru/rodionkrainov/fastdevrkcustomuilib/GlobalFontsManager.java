package ru.rodionkrainov.fastdevrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public abstract class GlobalFontsManager {
    private static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'«»^<>°φ";

    private static FreeTypeFontGenerator fontGenerator = null;
    private static FreeTypeFontParameter fontParameter = null;

    public static BitmapFont[] ARR_BP_FONTS_10_TO_50 = new BitmapFont[41];
    public static int numBpFonts       = ARR_BP_FONTS_10_TO_50.length;
    public static int numLoadedBpFonts = 0;

    public static void init(String _fontFilePath, float _borderWidth, int _spaceX) {
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(_fontFilePath));
        fontParameter = new FreeTypeFontParameter();

        // base settings
        fontParameter.characters     = FONT_CHARS;
        fontParameter.borderWidth    = _borderWidth;
        fontParameter.borderStraight = true;
        fontParameter.borderColor    = Color.WHITE;
        fontParameter.color          = Color.WHITE;
        fontParameter.spaceX         = _spaceX;
        fontParameter.genMipMaps     = true;
        fontParameter.minFilter      = Texture.TextureFilter.MipMap;
        fontParameter.magFilter      = Texture.TextureFilter.MipMapLinearNearest;
        fontParameter.incremental    = false;
    }

    public static void loadNext() {
        if (numBpFonts != numLoadedBpFonts) {
            fontParameter.size = (10 + numLoadedBpFonts) * 2;

            BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.MipMapLinearNearest);
            bitmapFont.setUseIntegerPositions(true);
            bitmapFont.getData().setScale(0.5f);

            ARR_BP_FONTS_10_TO_50[ numLoadedBpFonts ] = bitmapFont;
            numLoadedBpFonts++;
        }
    }

    public static void dispose() {
        for (BitmapFont bitmapFont : ARR_BP_FONTS_10_TO_50) if (bitmapFont != null) bitmapFont.dispose();
        if (fontGenerator != null) fontGenerator.dispose();
    }
}
