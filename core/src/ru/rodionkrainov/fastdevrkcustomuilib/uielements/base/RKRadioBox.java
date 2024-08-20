package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.Cursor;
import java.util.ArrayList;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.DrawingTools;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public class RKRadioBox extends RKCustomElement {
    private Color fontColor;

    private float MARGIN_POINT_LABEL = 14f;
    private float MARGIN_ELEMENTS    = 115f;

    private final ArrayList<RKRect>  arrRects  = new ArrayList<>();
    private final ArrayList<RKLabel> arrLabels = new ArrayList<>();

    private int selectedElementIndex = 0;

    public RKRadioBox(String _name, String[] _elements, Color _fontColor, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        super(_name, "radioBox", -1, -1, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        fontColor = _fontColor.cpy();

        for (int i = 0; i < _elements.length; i++) {
            arrRects.add(FastDevRKCustomUILib.addRect("radioBox_" + _name + "_rectButton_" + i, _posX, _posY, _fontSize, _fontSize, GlobalColorsDark.DARK_COLOR_BLUE, _zIndex, getLocalZIndex() + 1));
            arrLabels.add(FastDevRKCustomUILib.addLabel("spinner_" + _name + "_label_" + i, _elements[i], fontColor, _fontSize, _posX, _posY, _zIndex, getLocalZIndex() + 2));

            arrRects.get(i).setAlpha(0.01f);
        }
    }

    @Override
    public void update(float _delta, PointersStates[] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            for (int i = 0; i < arrRects.size(); i++) {
                RKRect rect = arrRects.get(i);
                RKLabel label = arrLabels.get(i);

                label.setFillColor(fontColor);

                rect.setZIndex(getZIndex());
                label.setZIndex(getZIndex());
                rect.setAlpha(0.001f);
                label.setAlpha(getAlpha());

                rect.setSize(label.getFontSize() + MARGIN_POINT_LABEL, label.getFontSize());

                float basePosX = i * (i > 0 ? ((arrRects.get( (i - 1) ).getWidth() + arrLabels.get( (i - 1) ).getWidth()) + MARGIN_ELEMENTS) : 0);
                rect.setPosition(getX() + basePosX, getY());
                label.setPosition(getX() + arrRects.get(i).getWidth() + basePosX, getY());

                if (rect.isPointerHover() || label.isPointerHover()) {
                    FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);

                    for (PointersStates pointerStates : _pointersStates) {
                        if (pointerStates.isUp()) {
                            selectedElementIndex = i;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            DrawingTools.enableGLBlend();

            int circleSegments = 1024;
            float pointsAlpha  = Math.min(getAlpha(), getLocalAlpha());

            Gdx.gl.glLineWidth(2f);
            for (int i = 0; i < arrRects.size(); i++) {
                RKRect rect = arrRects.get(i);

                // draw point
                if (i == selectedElementIndex) {
                    _shapeRenderer.end();
                    _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_POINT.r, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_POINT.g, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_POINT.b, pointsAlpha);
                    _shapeRenderer.circle(rect.getX() + rect.getWidth() / 2f - MARGIN_POINT_LABEL / 2f, rect.getY() + rect.getHeight() / 2f, rect.getHeight() / 4.5f, circleSegments);
                    _shapeRenderer.end();
                    _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_SELECTED.r, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_SELECTED.g, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON_SELECTED.b, pointsAlpha);
                } else {
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_RADIO_BUTTON.r, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON.g, GlobalColorsDark.DARK_COLOR_RADIO_BUTTON.b, pointsAlpha);
                }

                // draw main (big) circle (empty point)
                _shapeRenderer.circle(rect.getX() + rect.getWidth() / 2f - MARGIN_POINT_LABEL / 2f, rect.getY() + rect.getHeight() / 2f, rect.getHeight() / 2f - 1f, circleSegments);
            }

            _shapeRenderer.end();
            DrawingTools.disableGLBlend();
            _batch.begin();
        }
    }

    @Override
    public void setVisible(boolean _isVisible) {
        super.setVisible(_isVisible);

        for (int i = 0; i < arrRects.size(); i++) {
            RKRect rect   = arrRects.get(i);
            RKLabel label = arrLabels.get(i);

            rect.setVisible(_isVisible);
            label.setVisible(_isVisible);
        }
    }

    public void setFontColor(Color _fontColor) {
        fontColor = _fontColor;
    }

    public void setMarginPointLabel(float _margin) {
        MARGIN_POINT_LABEL = _margin;
    }
    public void setMarginElements(float _margin) {
        MARGIN_ELEMENTS = _margin;
    }

    public int getSelectedElementIndex() {
        return selectedElementIndex;
    }

    public String getSelectedElementText() {
        return arrLabels.get(selectedElementIndex).getText();
    }

    @Override
    public float getWidth() {
        float sumWidth = 0;
        for (int i = 0; i < arrRects.size(); i++) sumWidth += arrRects.get(i).getWidth() + MARGIN_POINT_LABEL + arrLabels.get(i).getWidth() + (i != (arrRects.size() - 1) ? MARGIN_ELEMENTS : 0);

        return sumWidth;
    }

    @Override
    public float getHeight() {
        return (!arrRects.isEmpty() ? arrRects.get(0).getHeight() : -1);
    }

    @Override
    public void dispose() {
        for (RKRect rect : arrRects) rect.dispose();
        for (RKLabel label : arrLabels) label.dispose();
    }
}
