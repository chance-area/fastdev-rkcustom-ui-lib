package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalFontsManager;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;

public class RKLabel extends RKCustomElement {
    private boolean isImage;
    private RKImage image;

    private final int fontSize;
    private Color fontColor;
    private final Label label;

    public RKLabel(String _name, String _text, Color _fontColor, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        super(_name, "label", -1, -1, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor(_fontColor);
        setBorderColor(new Color(0, 0, 0, 1f));
        setBorderSize(0f);

        fontSize  = _fontSize;
        fontColor = _fontColor;
        checkIsImg(_text);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = GlobalFontsManager.ARR_BP_FONTS_10_TO_50[(Math.max(0, Math.min((_fontSize - 10), 40)))];
        label = new Label(_text, labelStyle);
        label.setColor(getFillColor());
        label.setPosition(_posX, _posY);
        label.setAlignment(Align.left);

        label.pack();
        setSize(label.getWidth(), label.getHeight());
    }

    private void checkIsImg(String _text) {
        String imgName = "label_" + getName() + "_img";
        isImage = _text.contains("%#img_") && _text.contains("#%");

        if (isImage) {
            String imgTextureName = _text.substring(_text.indexOf("%#img_") + 6, _text.indexOf("#%"));
            image = FastDevRKCustomUILib.addImage(imgName, imgTextureName, 0, 0, 0, 0, getZIndex(), getLocalZIndex() + 1);
        } else {
            FastDevRKCustomUILib.removeElement(imgName);
        }
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            setFillColor(fontColor);

            if (isImage) {
                setSize(fontSize * 1.27f, fontSize * 1.27f);

                image.setSize(getWidth(), getHeight());
                image.setPosition(getX(), getY());
            } else {
                setSize(label.getWidth(), label.getHeight());

                label.setPosition(getX(), getY());
                label.setVisible(isVisible());

                label.setColor(fontColor);
                label.act(_delta);
            }
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            if (!isImage) label.draw(_batch, 1f);
        }
    }

    public void setFontColor(Color _fontColor) {
        fontColor = _fontColor;
    }

    public void setText(String _text) {
        checkIsImg(_text);
        label.setText(_text);
        label.pack();
    }

    public boolean isHasImage() {
        return isImage;
    }

    public String getText() {
        return label.getText().toString();
    }

    @Override
    public void dispose() {
        label.remove();
    }
}
