package ru.rodionkrainov.fastdevrkcustomuilib.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public interface IRKUIElement {
    void update(float _delta, boolean[][] _pointersStates);
    void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha);

    void setVisible(boolean _isVisible);
    void setIsPointerHover(boolean _isPointerHover);
    boolean isVisible();
    boolean isPointerHover();

    void setPosition(float _x, float _y);
    void setX(float _x);
    void setY(float _y);
    void setSize(float _w, float _h);
    void setWidth(float _w);
    void setHeight(float _h);
    void setFillColor(Color _color);
    void setBorderColor(Color _color);
    void setAlpha(float _alpha);
    void setLocalAlpha(float _localAlpha);
    void setZIndex(int _zIndex);
    void setIsInFocus(boolean _isInFocus);

    String getName();
    Vector2 getPosition();
    float getX();
    float getY();
    Vector2 getSize();
    float getWidth();
    float getHeight();
    Color getFillColor();
    Color getBorderColor();
    float getAlpha();
    float getLocalAlpha();

    String getType();

    int getZIndex();
    int getLocalZIndex();
    boolean isInFocus();

    void dispose();
}
