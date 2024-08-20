package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;

public class RKImage extends RKCustomElement {
    private final String imgTextureName;

    public RKImage(String _name, String _imgTextureName, float _posX, float _posY, float _w, float _h, int _zIndex, int _localZIndex) {
        super(_name, "image", _w, _h, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);
        setFillColor(new Color(0f, 0f, 0f, 1f));

        imgTextureName = _imgTextureName;
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            Color batchColor = _batch.getColor();
            _batch.setColor(batchColor.r, batchColor.g, batchColor.b, Math.min(getAlpha(), getFillColor().a));
            _batch.draw(FastDevRKCustomUILib.getImageTexture(imgTextureName), getX(), getY(), getWidth(), getHeight());
        }
    }
}
