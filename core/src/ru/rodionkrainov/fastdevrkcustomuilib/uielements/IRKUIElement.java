package ru.rodionkrainov.fastdevrkcustomuilib.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public interface IRKUIElement {
    void update(float _delta, PointersStates[] _pointersStates);
    void draw(Batch _batch, ShapeRenderer _shapeRenderer);

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
    void setBorderSize(float _size);
    void setAlpha(float _alpha);
    void setLocalAlpha(float _localAlpha);
    void setZIndex(int _zIndex);
    void setIsInFocus(boolean _isInFocus);

    String getName();
    Vector2 getPosition();
    float getX();
    float getY();
    float getWidth();
    float getHeight();
    Color getFillColor();
    Color getBorderColor();
    float getBorderSize();
    float getAlpha();
    float getLocalAlpha();

    String getType();

    int getZIndex();
    int getLocalZIndex();
    boolean isInFocus();

    void dispose();
}
