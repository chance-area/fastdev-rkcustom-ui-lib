package ru.rodionkrainov.libgdxrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GlobalFontsManager {
    private static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'«»^<>°φ";

    public static BitmapFont[] ARR_BP_FONTS_10_TO_50 = new BitmapFont[41];

    public static void loadAndInit(String _fontFilePath, float _borderWidth, int _spaceX) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(_fontFilePath));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();

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

        // init bitmap fonts (optimization)
        for (int i = 0; i < ARR_BP_FONTS_10_TO_50.length; i++) {
            fontParameter.size = (10 + i) * 2;

            BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
            bitmapFont.setUseIntegerPositions(false);
            bitmapFont.getData().setScale(0.5f);

            ARR_BP_FONTS_10_TO_50[i] = bitmapFont;
        }

        fontGenerator.dispose();
    }

    public static void dispose() {
        for (BitmapFont bitmapFont : ARR_BP_FONTS_10_TO_50) bitmapFont.dispose();
    }
}
