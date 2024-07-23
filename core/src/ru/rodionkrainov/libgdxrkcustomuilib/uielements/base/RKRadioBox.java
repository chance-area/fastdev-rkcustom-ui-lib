package ru.rodionkrainov.libgdxrkcustomuilib.uielements.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;
import java.util.ArrayList;

import ru.rodionkrainov.libgdxrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.libgdxrkcustomuilib.LibGdxRKCustomUILib;
import ru.rodionkrainov.libgdxrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.libgdxrkcustomuilib.utils.DrawingTools;

public class RKRadioBox implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;

    private Color fontColor;
    private Color fillColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;

    private final LibGdxRKCustomUILib LIB;

    private final float SPACE_RECT_LABEL = 11f;
    private final float SPACE_ELEMENTS   = 115f;

    private final ArrayList<RKRect> arrRects   = new ArrayList<>();
    private final ArrayList<RKLabel> arrLabels = new ArrayList<>();

    private int selectedElementIndex = 0;

    public RKRadioBox(String _name, String[] _elements, Color _fontColor, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex, LibGdxRKCustomUILib _lib) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fontColor = _fontColor.cpy();

        LIB = _lib;

        for (int i = 0; i < _elements.length; i++) {
            float basePoxX = i * (i > 0 ? (arrRects.get( (i - 1) ).getWidth() + arrLabels.get( (i - 1) ).getWidth() + SPACE_RECT_LABEL) + SPACE_ELEMENTS : 0);

            arrRects.add(LIB.addRect("radioBox_" + _name + "_rectButton_" + i, _posX + basePoxX, _posY, _fontSize, _fontSize, GlobalColorsDark.DARK_COLOR_BLUE, _zIndex, localZIndex + 1));
            arrLabels.add(LIB.addLabel("spinner_" + _name + "_label_" + i, _elements[i], fontColor, _fontSize, _posX + arrRects.get(i).getWidth() + basePoxX + SPACE_RECT_LABEL, _posY, _zIndex, localZIndex + 2));

            arrRects.get(i).setAlpha(0.01f);
        }
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (alpha > 0 && localAlpha > 0) {
            for (int i = 0; i < arrRects.size(); i++) {
                RKRect rect = arrRects.get(i);
                RKLabel label = arrLabels.get(i);

                if (rect.isPointerHover() || label.isPointerHover()) {
                    LIB.changeCursor(Cursor.HAND_CURSOR);

                    if (_pointersStates[0][1]) selectedElementIndex = i;
                }
            }
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (alpha > 0 && localAlpha > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            DrawingTools.enableGLBlend();

            Gdx.gl.glLineWidth(2f);
            for (int i = 0; i < arrRects.size(); i++) {
                RKRect rect = arrRects.get(i);

                if (i == selectedElementIndex) {
                    _shapeRenderer.end();
                    DrawingTools.disableGLBlend();
                    _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_POINT);
                    _shapeRenderer.circle(rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.getHeight() / 4.5f, 500);
                    _shapeRenderer.end();
                    DrawingTools.disableGLBlend();
                    _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    DrawingTools.enableGLBlend();
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_SELECTED);
                } else {
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON);
                }
                _shapeRenderer.circle(rect.getX() + rect.getWidth() / 2f, rect.getY() + rect.getHeight() / 2f, rect.getHeight() / 2f - 1f, 500);
            }

            _shapeRenderer.end();
            DrawingTools.disableGLBlend();
            _batch.begin();
        }
    }

    public int getSelectedElementIndex() {
        return selectedElementIndex;
    }

    @Override
    public void setVisible(boolean _isVisible) {
        isVisible = _isVisible;

        for (RKRect rect : arrRects) rect.setVisible(_isVisible);
        for (RKLabel label : arrLabels) label.setVisible(_isVisible);
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
        if (!arrRects.isEmpty()) arrRects.get(0).setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        if (!arrRects.isEmpty()) arrRects.get(0).setX(_x);
    }

    @Override
    public void setY(float _y) {
        if (!arrRects.isEmpty()) arrRects.get(0).setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        // ignore
    }

    @Override
    public void setWidth(float _w) {
        // ignore
    }

    @Override
    public void setHeight(float _h) {
        // ignore
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

        for (RKRect rect : arrRects) rect.setAlpha(_alpha);
        for (RKLabel label : arrLabels) label.setAlpha(_alpha);
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;

        for (RKRect rect : arrRects) rect.setLocalAlpha(_localAlpha);
        for (RKLabel label : arrLabels) label.setLocalAlpha(_localAlpha);
    }

    @Override
    public void setZIndex(int _zIndex) {
        zIndex = _zIndex;

        for (RKRect rect : arrRects) rect.setZIndex(zIndex);
        for (RKLabel label : arrLabels) label.setZIndex(zIndex);
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
        return (!arrRects.isEmpty() ? arrRects.get(0).getPosition() : null);
    }

    @Override
    public float getX() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getX() : -1);
    }

    @Override
    public float getY() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getY() : -1);
    }

    @Override
    public Vector2 getSize() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getSize() : null);
    }

    @Override
    public float getWidth() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getWidth() : -1);
    }

    @Override
    public float getHeight() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getHeight() : -1);
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
        return "radioBox";
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
