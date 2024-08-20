package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.Cursor;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.DrawingTools;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.MathPlus;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public class RKSpinner extends RKCustomElement {
    private Color fontColor;

    private final RKRect spinnerRect;
    private final RKLabel spinnerLabel;

    private final float arrowsButtonsWidth;
    private final RKRect arrowRectUp;
    private final RKRect arrowRectDown;
    private final RKImage arrowImageUp;
    private final RKImage arrowImageDown;

    private float value;
    private float minValue;
    private float maxValue;
    private float step;

    public RKSpinner(String _name, float _defNum, float _min, float _max, float _step, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        super(_name, "spinner", _w, _h, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor(GlobalColorsDark.DARK_COLOR_BUTTON);
        setBorderColor(GlobalColorsDark.DARK_COLOR_BUTTON_BORDER);
        setBorderSize(_borderSize);

        fontColor = _fontColor.cpy();

        arrowsButtonsWidth = _h / 1.9f;

        value    = _defNum;
        minValue = _min;
        maxValue = _max;
        step     = _step;

        spinnerRect  = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rect", _posX, _posY, _w, _h, getFillColor(), getBorderColor(), getBorderSize(), _roundRadius, _zIndex, getLocalZIndex());
        spinnerLabel = FastDevRKCustomUILib.addLabel("spinner_" + _name + "_label", String.valueOf(value), fontColor, _fontSize, _posX, _posY, _zIndex, getLocalZIndex() + 1);

        arrowRectUp   = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rectArrowUp", _posX, _posY, arrowsButtonsWidth, _h / 2f - getBorderSize(), GlobalColorsDark.DARK_COLOR_SPINNER_BUTTON, getBorderColor(), 0, _roundRadius, false, true, false, false, _zIndex, getLocalZIndex() + 1);
        arrowRectDown = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rectArrowDown", _posX, _posY, arrowsButtonsWidth, _h / 2f - getBorderSize(), GlobalColorsDark.DARK_COLOR_SPINNER_BUTTON, getBorderColor(), 0, _roundRadius, false, false, false, true, _zIndex,  1);

        arrowImageUp   = FastDevRKCustomUILib.addImage("spinner_" + _name + "_imageArrowUp", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_UP), 0, 0, 0, 0, getZIndex(), getLocalZIndex() + 2);
        arrowImageDown = FastDevRKCustomUILib.addImage("spinner_" + _name + "_imageArrowDown", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_DOWN), 0, 0, 0, 0, getZIndex(), getLocalZIndex() + 2);
    }

    @Override
    public void update(float _delta, PointersStates[] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            setIsInFocus(spinnerLabel.isInFocus() || spinnerRect.isInFocus());

            updateElements();

            spinnerLabel.setText( (value == 0 ? "0" : String.valueOf(value)) );

            spinnerRect.setSize(getWidth(), getHeight());
            spinnerRect.setPosition(getX(), getY());

            fontColor.a = Math.min(getAlpha(), getLocalAlpha());
            spinnerLabel.setFontColor(fontColor);
            spinnerLabel.setPosition(spinnerRect.getX() + (spinnerRect.getWidth() - spinnerLabel.getWidth()) / 2f, spinnerRect.getY() + (spinnerRect.getHeight() - spinnerLabel.getHeight()) / 2f);

            float borderSize = getBorderSize();

            // arrows (rect-buttons and images)
            arrowRectUp.setPosition(spinnerRect.getX() + (spinnerRect.getWidth() - arrowsButtonsWidth) - borderSize, getY() + (spinnerRect.getHeight() - arrowRectUp.getHeight()) - borderSize);
            arrowRectDown.setPosition(spinnerRect.getX() + (spinnerRect.getWidth() - arrowsButtonsWidth) - borderSize, getY() + borderSize);

            arrowImageUp.setSize(arrowsButtonsWidth, arrowsButtonsWidth + 8f);
            arrowImageDown.setSize(arrowImageUp.getWidth(), arrowImageUp.getHeight());
            arrowImageUp.setPosition(arrowRectUp.getX() + (arrowRectUp.getWidth() - arrowImageUp.getWidth()) / 2f, arrowRectUp.getY() + (arrowRectUp.getHeight() - arrowImageUp.getHeight()) / 2f - 1.5f);
            arrowImageDown.setPosition(arrowRectDown.getX() + (arrowRectDown.getWidth() - arrowImageDown.getWidth()) / 2f, arrowRectDown.getY() + (arrowRectDown.getHeight() - arrowImageDown.getHeight()) / 2f + 1.5f);

            arrowImageUp.setAlpha(0.7f);
            arrowImageDown.setAlpha(0.7f);
            if (spinnerRect.isPointerHover() || spinnerLabel.isPointerHover() || arrowRectUp.isPointerHover() || arrowRectDown.isPointerHover()) {
                FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);

                for (PointersStates pointerStates : _pointersStates) {
                    if (arrowRectUp.isPointerHover()) {
                        arrowImageUp.setAlpha(1f);
                        if (pointerStates.isUp()) {
                            value += step;
                            break;
                        }
                    } else if (arrowRectDown.isPointerHover()) {
                        arrowImageDown.setAlpha(1f);
                        if (pointerStates.isUp()) {
                            value -= step;
                            break;
                        }
                    }
                }

                value = Math.max(Math.min(MathPlus.roundTo(value, 4), maxValue), minValue);
            }
            if (isInFocus()) {
                setBorderColor(GlobalColorsDark.DARK_COLOR_SPINNER_FOCUS_BORDER);
            } else {
                setFillColor(GlobalColorsDark.DARK_COLOR_SPINNER);
                setBorderColor(GlobalColorsDark.DARK_COLOR_SPINNER_BORDER);
            }

            spinnerRect.setFillColor(getFillColor());
            spinnerRect.setBorderColor(getBorderColor());
        }
    }

    private void updateElements() {
        float alpha = getAlpha();
        int zIndex  = getZIndex();

        spinnerRect.setAlpha(alpha);
        spinnerLabel.setAlpha(alpha);
        arrowRectDown.setAlpha(alpha);
        arrowRectUp.setAlpha(alpha);
        arrowImageUp.setAlpha(alpha);
        arrowImageDown.setAlpha(alpha);

        spinnerRect.setZIndex(zIndex);
        spinnerLabel.setZIndex(zIndex);
        arrowRectDown.setZIndex(zIndex);
        arrowRectUp.setZIndex(zIndex);
        arrowImageUp.setZIndex(zIndex);
        arrowImageDown.setZIndex(zIndex);
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            DrawingTools.enableGLBlend();

            float borderSize = getBorderSize();

            _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_SPINNER_BORDER);
            _shapeRenderer.rectLine(arrowRectDown.getX() - borderSize / 2f, arrowRectDown.getY(), arrowRectDown.getX() - borderSize / 2f, arrowRectDown.getY() + (getHeight() - borderSize * 2f), borderSize);
            //_shapeRenderer.rectLine(arrowRectDown.getX() - borderSize / 2f, arrowRectDown.getY() + arrowRectDown.getHeight() + borderSize, arrowRectDown.getX() + borderSize + arrowsButtonsWidth, arrowRectDown.getY() + arrowRectDown.getHeight() + borderSize, borderSize);

            _shapeRenderer.end();
            DrawingTools.disableGLBlend();
            _batch.begin();
        }
    }

    @Override
    public void setVisible(boolean _isVisible) {
        super.setVisible(_isVisible);

        spinnerRect.setVisible(_isVisible);
        spinnerLabel.setVisible(_isVisible);
        arrowImageDown.setVisible(_isVisible);
        arrowImageUp.setVisible(_isVisible);
        arrowRectDown.setVisible(_isVisible);
        arrowRectUp.setVisible(_isVisible);
    }
}
