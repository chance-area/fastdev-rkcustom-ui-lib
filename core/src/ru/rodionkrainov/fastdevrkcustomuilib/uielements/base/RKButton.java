package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.awt.Cursor;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;

public class RKButton extends RKCustomElement {
    private final IButtonClickEvent onButtonClickEvent;

    private Color fontColor;

    private final RKRect buttonRect;
    private final RKLabel buttonLabel;

    public RKButton(String _name, String _text, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, IButtonClickEvent _onButtonClickEvent, int _zIndex, int _localZIndex) {
        super(_name, "button", _w, _h, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor(GlobalColorsDark.DARK_COLOR_BUTTON);
        setBorderColor(GlobalColorsDark.DARK_COLOR_BUTTON_BORDER);
        setBorderSize(_borderSize);

        fontColor = _fontColor.cpy();

        onButtonClickEvent = _onButtonClickEvent;

        buttonRect  = FastDevRKCustomUILib.addRect("button_" + _name + "_rect", _posX, _posY, _w, _h, getFillColor(), getBorderColor(), getBorderSize(), _roundRadius, _zIndex, getLocalZIndex());
        buttonLabel = FastDevRKCustomUILib.addLabel("button_" + _name + "_label", _text, fontColor, _fontSize, _posX, _posY, _zIndex, getLocalZIndex() + 1);
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            setIsInFocus((buttonLabel.isInFocus() || buttonRect.isInFocus()));

            updateElements();

            fontColor.a = Math.min(getAlpha(), getLocalAlpha());
            buttonLabel.setFontColor(fontColor);
            buttonLabel.setPosition(buttonRect.getX() + (buttonRect.getWidth() - buttonLabel.getWidth()) / 2f, buttonRect.getY() + (buttonRect.getHeight() - buttonLabel.getHeight()) / 2f);

            buttonRect.setSize(getWidth(), getHeight());
            buttonRect.setPosition(getX(), getY());

            if (buttonRect.isPointerHover() || buttonLabel.isPointerHover()) {
                FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);

                setFillColor(GlobalColorsDark.DARK_COLOR_BUTTON_HOVER);
                setBorderColor(GlobalColorsDark.DARK_COLOR_BUTTON_HOVER_BORDER);

                if (Gdx.input.isTouched()) setFillColor(GlobalColorsDark.DARK_COLOR_BUTTON_TOUCHED);
                if (_pointersStates[0][1]) onButtonClickEvent.onButtonClick(this);
            } else {
                setFillColor(GlobalColorsDark.DARK_COLOR_BUTTON);
                setBorderColor(GlobalColorsDark.DARK_COLOR_BUTTON_BORDER);
            }

            buttonRect.setFillColor(getFillColor());
            buttonRect.setBorderColor(getBorderColor());
        }
    }

    private void updateElements() {
        buttonLabel.setAlpha(getAlpha());
        buttonRect.setAlpha(getAlpha());

        buttonLabel.setZIndex(getZIndex());
        buttonRect.setZIndex(getZIndex());
    }

    @Override
    public void setVisible(boolean _isVisible) {
        super.setVisible(_isVisible);

        buttonLabel.setVisible(_isVisible);
        buttonRect.setVisible(_isVisible);
    }

    public void setText(String _text) {
        buttonLabel.setText(_text);
    }

    public void setFontColor(Color _color) {
        fontColor = _color.cpy();
        buttonLabel.setFontColor(fontColor);
    }

    @Override
    public void dispose() {
        buttonRect.dispose();
        buttonLabel.dispose();
    }
}
