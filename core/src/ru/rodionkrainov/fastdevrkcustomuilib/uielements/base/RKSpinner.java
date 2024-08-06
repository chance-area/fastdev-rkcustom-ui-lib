package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.DrawingTools;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.MathPlus;

public class RKSpinner implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;

    private Color fontColor;
    private Color fillColor;
    private Color borderColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;
    private float borderSize;

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
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fontColor   = _fontColor.cpy();
        fillColor   = GlobalColorsDark.DARK_COLOR_BUTTON.cpy();
        borderColor = GlobalColorsDark.DARK_COLOR_BUTTON_BORDER.cpy();
        borderSize  = _borderSize;
        arrowsButtonsWidth = _h / 1.9f;

        value    = _defNum;
        minValue = _min;
        maxValue = _max;
        step     = _step;

        spinnerRect  = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rect", _posX, _posY, _w, _h, fillColor, borderColor, borderSize, _roundRadius, _zIndex, localZIndex);
        spinnerLabel = FastDevRKCustomUILib.addLabel("spinner_" + _name + "_label", String.valueOf(value), fontColor, _fontSize, _posX, _posY, _zIndex, localZIndex + 1);

        arrowRectUp   = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rectArrowUp", _posX, _posY, arrowsButtonsWidth, _h / 2f - borderSize, GlobalColorsDark.DARK_COLOR_SPINNER_BUTTON, borderColor, 0, _roundRadius, false, true, false, false, _zIndex, localZIndex + 1);
        arrowRectDown = FastDevRKCustomUILib.addRect("spinner_" + _name + "_rectArrowDown", _posX, _posY, arrowsButtonsWidth, _h / 2f - borderSize, GlobalColorsDark.DARK_COLOR_SPINNER_BUTTON, borderColor, 0, _roundRadius, false, false, false, true, _zIndex,  + 1);

        arrowImageUp   = FastDevRKCustomUILib.addImage("spinner_" + _name + "_imageArrowUp", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_UP), 0, 0, 0, 0, zIndex, localZIndex + 2);
        arrowImageDown = FastDevRKCustomUILib.addImage("spinner_" + _name + "_imageArrowDown", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_DOWN), 0, 0, 0, 0, zIndex, localZIndex + 2);
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (alpha > 0 && localAlpha > 0) {
            isInFocus = (spinnerLabel.isInFocus() || spinnerRect.isInFocus());

            spinnerLabel.setText( (value == 0 ? "0" : String.valueOf(value)) );

            fontColor.a = Math.min(localAlpha, alpha);
            fillColor.a = Math.min(localAlpha, alpha);
            if (borderColor != null) borderColor.a = Math.min(localAlpha, alpha);

            spinnerLabel.setFillColor(fontColor);
            spinnerLabel.setPosition(spinnerRect.getX() + (spinnerRect.getWidth() - spinnerLabel.getWidth()) / 2f, spinnerRect.getY() + (spinnerRect.getHeight() - spinnerLabel.getHeight()) / 2f);

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

                if (arrowRectUp.isPointerHover()) {
                    arrowImageUp.setAlpha(1f);
                    if (_pointersStates[0][1]) value += step;
                }else if (arrowRectDown.isPointerHover()) {
                    arrowImageDown.setAlpha(1f);
                    if (_pointersStates[0][1]) value -= step;
                }

                value = Math.max(Math.min(MathPlus.roundTo(value, 4), maxValue), minValue);
            }
            if (isInFocus) {
                borderColor = GlobalColorsDark.DARK_COLOR_SPINNER_FOCUS_BORDER;
            } else {
                fillColor   = GlobalColorsDark.DARK_COLOR_SPINNER.cpy();
                borderColor = GlobalColorsDark.DARK_COLOR_SPINNER_BORDER.cpy();
            }

            spinnerRect.setFillColor(fillColor);
            spinnerRect.setBorderColor(borderColor);
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (alpha > 0 && localAlpha > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            DrawingTools.enableGLBlend();

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
        isVisible = _isVisible;

        spinnerRect.setVisible(_isVisible);
        spinnerLabel.setVisible(_isVisible);
        arrowRectDown.setVisible(_isVisible);
        arrowRectUp.setVisible(_isVisible);
        arrowImageUp.setVisible(_isVisible);
        arrowImageDown.setVisible(_isVisible);
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
        spinnerRect.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        spinnerRect.setX(_x);
    }

    @Override
    public void setY(float _y) {
        spinnerRect.setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        spinnerRect.setSize(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        spinnerRect.setWidth(_w);
    }

    @Override
    public void setHeight(float _h) {
        spinnerRect.setHeight(_h);
    }

    @Override
    public void setFillColor(Color _color) {
        fillColor = _color.cpy();
    }

    @Override
    public void setBorderColor(Color _color) {
        borderColor = _color.cpy();
    }

    @Override
    public void setAlpha(float _alpha) {
        alpha = _alpha;

        spinnerRect.setAlpha(_alpha);
        spinnerLabel.setAlpha(_alpha);
        arrowRectDown.setAlpha(_alpha);
        arrowRectUp.setAlpha(_alpha);
        arrowImageUp.setAlpha(_alpha);
        arrowImageDown.setAlpha(_alpha);
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;

        spinnerRect.setLocalAlpha(_localAlpha);
        spinnerLabel.setLocalAlpha(_localAlpha);
        arrowRectDown.setLocalAlpha(_localAlpha);
        arrowRectUp.setLocalAlpha(_localAlpha);
        arrowImageUp.setLocalAlpha(_localAlpha);
        arrowImageDown.setLocalAlpha(_localAlpha);
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
        return spinnerRect.getPosition();
    }

    @Override
    public float getX() {
        return spinnerRect.getX();
    }

    @Override
    public float getY() {
        return spinnerRect.getY();
    }

    @Override
    public Vector2 getSize() {
        return spinnerRect.getSize();
    }

    @Override
    public float getWidth() {
        return spinnerRect.getWidth();
    }

    @Override
    public float getHeight() {
        return spinnerRect.getHeight();
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
        return "spinner";
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
