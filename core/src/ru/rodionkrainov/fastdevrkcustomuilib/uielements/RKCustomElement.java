package ru.rodionkrainov.fastdevrkcustomuilib.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class RKCustomElement implements IRKUIElement {
    private final String NAME;                 // name current element

    private int zIndex;
    private final int localZIndex;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;
    private boolean isVisible = true;

    private final Vector2 vPosition = new Vector2(-1, -1);
    private final Vector2 vSize     = new Vector2(-1, -1);

    private Color fillColor;
    private Color borderColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;

    public RKCustomElement(String _name, int _zIndex, int _localZIndex) {
        NAME = _name;

        zIndex      = _zIndex;
        localZIndex = _localZIndex;
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {

    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {

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
        //
    }

    @Override
    public void setX(float _x) {

    }

    @Override
    public void setY(float _y) {

    }

    @Override
    public void setSize(float _w, float _h) {

    }

    @Override
    public void setWidth(float _w) {

    }

    @Override
    public void setHeight(float _h) {

    }

    @Override
    public void setFillColor(Color _color) {

    }

    @Override
    public void setBorderColor(Color _color) {

    }

    @Override
    public void setAlpha(float _alpha) {

    }

    @Override
    public void setLocalAlpha(float _localAlpha) {

    }

    @Override
    public void setZIndex(int _zIndex) {

    }

    @Override
    public void setIsInFocus(boolean _isInFocus) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public Vector2 getSize() {
        return null;
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public Color getBorderColor() {
        return null;
    }

    @Override
    public float getAlpha() {
        return 0;
    }

    @Override
    public float getLocalAlpha() {
        return 0;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getZIndex() {
        return 0;
    }

    @Override
    public int getLocalZIndex() {
        return 0;
    }

    @Override
    public boolean isInFocus() {
        return false;
    }

    @Override
    public void dispose() {

    }
}
