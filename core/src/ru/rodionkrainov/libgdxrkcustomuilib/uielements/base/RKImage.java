package ru.rodionkrainov.libgdxrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ru.rodionkrainov.libgdxrkcustomuilib.LibGdxRKCustomUILib;
import ru.rodionkrainov.libgdxrkcustomuilib.uielements.IRKUIElement;

public class RKImage implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;

    private String imgTextureName;
    private Color fillColor = new Color(0, 0, 0, 1f);

    private final LibGdxRKCustomUILib LIB;

    private final Vector2 position = new Vector2(-1, -1);
    private final Vector2 size = new Vector2(-1, -1);

    private float alpha      = 1f;
    private float localAlpha = 1f;

    public RKImage(String _name, String _imgTextureName, float _posX, float _posY, float _w, float _h, int _zIndex, int _localZIndex, LibGdxRKCustomUILib _lib) {
        name = _name;
        imgTextureName = _imgTextureName;
        position.set(_posX, _posY);
        size.set(_w, _h);
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        LIB = _lib;
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        fillColor.a = Math.min(alpha, localAlpha);
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        Color batchColor = _batch.getColor();
        _batch.setColor(batchColor.r, batchColor.g, batchColor.b, fillColor.a);
        _batch.draw(LIB.getImageTexture(imgTextureName), position.x, position.y, size.x, size.y);
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
        position.set(_x, _y);
    }

    @Override
    public void setX(float _x) {
        position.set(_x, position.y);
    }

    @Override
    public void setY(float _y) {
        position.set(position.x, _y);
    }

    @Override
    public void setSize(float _w, float _h) {
        size.set(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        size.set(_w, size.y);
    }

    @Override
    public void setHeight(float _h) {
        size.set(size.x, _h);
    }

    @Override
    public void setFillColor(Color _color) {
        fillColor = _color.cpy();
    }

    @Override
    public void setBorderColor(Color _color) {
        // ignore
    }

    @Override
    public void setAlpha(float _alpha) {
        alpha = _alpha;
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;
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
        return name;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public float getWidth() {
        return size.x;
    }

    @Override
    public float getHeight() {
        return size.y;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public Color getBorderColor() {
        return null;
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
        return "image";
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
        //
    }
}
