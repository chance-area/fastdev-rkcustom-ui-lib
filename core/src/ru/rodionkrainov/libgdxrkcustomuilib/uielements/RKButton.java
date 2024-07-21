package ru.rodionkrainov.libgdxrkcustomuilib.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;

import ru.rodionkrainov.libgdxrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.libgdxrkcustomuilib.LibGdxRKCustomUILib;

public class RKButton implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;

    private Color fillColor;
    private Color borderColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;
    private float borderSize;

    private final LibGdxRKCustomUILib LIB;
    private final IButtonClickEvent onButtonClickEvent;

    private final RKRect buttonRect;
    private final RKLabel buttonLabel;

    public RKButton(String _name, String _text, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, IButtonClickEvent _onButtonClickEvent, int _zIndex, int _localZIndex, LibGdxRKCustomUILib _lib) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fillColor   = _fillColor.cpy();
        borderColor = (_borderColor != null ? _borderColor.cpy() : null);
        borderSize  = _borderSize;

        LIB = _lib;
        onButtonClickEvent = _onButtonClickEvent;

        buttonRect  = LIB.addRect("button_" + _name + "_rect", _posX, _posY, _w, _h, fillColor, borderColor, borderSize, _roundRadius, _zIndex, localZIndex);
        buttonLabel = LIB.addLabel("button_" + _name + "_label", _text, _fontColor, _fontSize, _posX, _posY, _zIndex, localZIndex + 1);
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (alpha > 0 && localAlpha > 0) {
            fillColor.a = Math.min(localAlpha, alpha);
            if (borderColor != null) borderColor.a = Math.min(localAlpha, alpha);

            buttonLabel.setPosition(buttonRect.getX() + (buttonRect.getWidth() - buttonLabel.getWidth()) / 2f, buttonRect.getY() + (buttonRect.getHeight() - buttonLabel.getHeight()) / 2f);

            if (isPointerHover || LIB.isPointerHover(buttonLabel.getName())) {
                LIB.changeCursor(Cursor.HAND_CURSOR);

                fillColor   = GlobalColorsDark.DARK_COLOR_BUTTON_HOVER;
                borderColor = GlobalColorsDark.DARK_COLOR_BUTTON_HOVER_BORDER;

                if (Gdx.input.isTouched()) fillColor = GlobalColorsDark.DARK_COLOR_BUTTON_TOUCHED;
                if (_pointersStates[0][1]) onButtonClickEvent.onButtonClick();
            } else {
                fillColor   = GlobalColorsDark.DARK_COLOR_BUTTON;
                borderColor = GlobalColorsDark.DARK_COLOR_BUTTON_BORDER;
            }

            buttonRect.setFillColor(fillColor);
            buttonRect.setBorderColor(borderColor);
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        // ignore
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
    public void setName(String _name) {
        name = _name;
    }

    @Override
    public void setPosition(float _x, float _y) {
        buttonRect.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        buttonRect.setX(_x);
    }

    @Override
    public void setY(float _y) {
        buttonRect.setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        buttonRect.setSize(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        buttonRect.setWidth(_w);
    }

    @Override
    public void setHeight(float _h) {
        buttonRect.setHeight(_h);
    }

    @Override
    public void setFillColor(Color _color) {
        fillColor = _color;
    }

    @Override
    public void setBorderColor(Color _color) {
        borderColor = _color;
    }

    @Override
    public void setAlpha(float _alpha) {
        alpha = _alpha;
        buttonRect.setAlpha(_alpha);
        buttonLabel.setAlpha(_alpha);
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;
        buttonRect.setLocalAlpha(_localAlpha);
        buttonLabel.setLocalAlpha(_localAlpha);
    }

    @Override
    public void setZIndex(int _zIndex) {
        zIndex = _zIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Vector2 getPosition() {
        return buttonRect.getPosition();
    }

    @Override
    public float getX() {
        return buttonRect.getX();
    }

    @Override
    public float getY() {
        return buttonRect.getY();
    }

    @Override
    public Vector2 getSize() {
        return buttonRect.getSize();
    }

    @Override
    public float getWidth() {
        return buttonRect.getWidth();
    }

    @Override
    public float getHeight() {
        return buttonRect.getHeight();
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
    public float getAlpha() {
        return alpha;
    }

    @Override
    public float getLocalAlpha() {
        return localAlpha;
    }

    @Override
    public String getType() {
        return "button";
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
    public void dispose() {
        buttonRect.dispose();
        buttonLabel.dispose();
    }
}
