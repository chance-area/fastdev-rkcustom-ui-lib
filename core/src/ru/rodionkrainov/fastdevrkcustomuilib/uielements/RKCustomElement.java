package ru.rodionkrainov.fastdevrkcustomuilib.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public abstract class RKCustomElement implements IRKUIElement {
    private final String NAME;
    private final String ELEMENT_TYPE;

    private int zIndex;
    private final int localZIndex;
    private boolean isPointerHover = false;
    private boolean isInFocus      = false;
    private boolean isVisible      = true;

    private final Vector2 vPosition;
    private float width;
    private float height;

    private Color fillColor;
    private Color borderColor;
    private float borderSize;
    private float alpha;
    private float localAlpha;

    public RKCustomElement(String _name, String _elementType, float _width, float _height, float _x, float _y, float _alpha, float _localAlpha, int _zIndex, int _localZIndex) {
        NAME         = _name;
        ELEMENT_TYPE = _elementType;

        vPosition = new Vector2(_x, _y);
        width     = _width;
        height    = _height;

        zIndex      = _zIndex;
        localZIndex = _localZIndex;
        alpha       = _alpha;
        localAlpha  = _localAlpha;
    }

    @Override
    public void update(float _delta, PointersStates[] _pointersStates) {
        // TODO
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        // TODO
    }

    @Override
    public void setVisible(boolean _isVisible) {
        isVisible = _isVisible;
    }

    @Override
    public void setIsPointerHover(boolean _isPointerHover) {
        isPointerHover = _isPointerHover;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public boolean isPointerHover() {
        return isPointerHover;
    }

    @Override
    public void setPosition(float _x, float _y) {
        vPosition.set(_x, _y);
    }

    @Override
    public void setX(float _x) {
        vPosition.x = _x;
    }

    @Override
    public void setY(float _y) {
        vPosition.y = _y;
    }

    @Override
    public void setSize(float _width, float _height) {
        width  = _width;
        height = _height;
    }

    @Override
    public void setWidth(float _width) {
        width = _width;
    }

    @Override
    public void setHeight(float _height) {
        height = _height;
    }

    @Override
    public void setFillColor(Color _color) {
        if (_color != null) {
            fillColor = _color.cpy();
            fillColor.a = Math.min(alpha, localAlpha);
        }
    }

    @Override
    public void setBorderColor(Color _color) {
        if (_color != null) {
            borderColor = _color.cpy();
            borderColor.a = Math.min(alpha, localAlpha);
        }
    }

    @Override
    public void setBorderSize(float _borderSize) {
        borderSize = _borderSize;
    }

    @Override
    public void setAlpha(float _alpha) {
        alpha = _alpha;

        if (fillColor != null) fillColor.a   = Math.min(getAlpha(), getLocalAlpha());
        if (borderColor != null) borderColor.a = Math.min(getAlpha(), getLocalAlpha());
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;

        if (fillColor != null) fillColor.a   = Math.min(getAlpha(), getLocalAlpha());
        if (borderColor != null) borderColor.a = Math.min(getAlpha(), getLocalAlpha());
    }

    @Override
    public void setZIndex(int _zIndex) {
        zIndex = _zIndex;
    }

    @Override
    public void setIsInFocus(boolean _isInFocus) {
        isInFocus = _isInFocus;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Vector2 getPosition() {
        return vPosition;
    }

    @Override
    public float getX() {
        return vPosition.x;
    }

    @Override
    public float getY() {
        return vPosition.y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public Color getBorderColor() {
        return borderColor;
    }

    @Override
    public float getBorderSize() {
        return borderSize;
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public float getLocalAlpha() {
        return localAlpha;
    }

    @Override
    public String getType() {
        return ELEMENT_TYPE;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public int getLocalZIndex() {
        return localZIndex;
    }

    @Override
    public boolean isInFocus() {
        return isInFocus;
    }

    @Override
    public void dispose() {
        // TODO
    }
}
