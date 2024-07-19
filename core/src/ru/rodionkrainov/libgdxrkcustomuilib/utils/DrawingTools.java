package ru.rodionkrainov.libgdxrkcustomuilib.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

public abstract class DrawingTools {
    public static void enableGLBlend() {
        if (Gdx.app.getGraphics().isGL30Available()) {
            Gdx.gl30.glEnable(GL30.GL_BLEND);
            Gdx.gl30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            Gdx.gl20.glEnable(GL20.GL_BLEND);
            Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    public static void disableGLBlend() {
        if (Gdx.app.getGraphics().isGL30Available()) Gdx.gl30.glDisable(GL30.GL_BLEND);
        else Gdx.gl20.glDisable(GL20.GL_BLEND);
    }
}
