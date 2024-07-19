package ru.rodionkrainov.libgdxrkcustomuilib.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

import ru.rodionkrainov.libgdxrkcustomuilib.GlobalFontsManager;

public class RKLabel implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isPointerHover = false;

    private Color fillColor;
    private Color borderColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;

    private final Label label;

    public RKLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fillColor = _color;
        borderColor = new Color(0, 0, 0, alpha);

        float realFontSize;
        LabelStyle labelStyle = new LabelStyle();

        if (_fontSize <= 10)      {
            labelStyle.font = GlobalFontsManager.BP_FONT_FROM_5_TO_10;
            realFontSize = 15;
        } else if (_fontSize <= 18) {
            labelStyle.font = GlobalFontsManager.BP_FONT_FROM_11_TO_18;
            realFontSize = 29;
        } else if (_fontSize <= 30) {
            labelStyle.font = GlobalFontsManager.BP_FONT_FROM_19_TO_30;
            realFontSize = 49;
        } else if (_fontSize <= 44) {
            labelStyle.font = GlobalFontsManager.BP_FONT_FROM_31_TO_44;
            realFontSize = 75;
        } else {
            labelStyle.font = GlobalFontsManager.BP_FONT_FROM_45_TO_60;
            realFontSize = 105;
        }

        label = new Label(_text, labelStyle);
        label.setColor(fillColor);
        label.setPosition(_posX, _posY);
        label.setFontScale(Math.round((_fontSize / realFontSize) * 1000) / 1000f);
        label.setAlignment(Align.left);
        label.pack();
    }

    @Override
    public void update(float _delta) {
        if (alpha > 0 && localAlpha > 0) {
            fillColor.a   = Math.min(alpha, localAlpha);
            borderColor.a = Math.min(alpha, localAlpha);

            label.setColor(fillColor);
            label.act(_delta);
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (alpha > 0 && localAlpha > 0) label.draw(_batch, _parentAlpha);
    }

    @Override
    public void setVisible(boolean _isVisible) {
        label.setVisible(_isVisible);
    }

    @Override
    public void setIsPointerHover(boolean _isPointerHover) {
        isPointerHover = _isPointerHover;
    }

    @Override
    public boolean isVisible() {
        return label.isVisible();
    }

    @Override
    public boolean isPointerHover() {
        return isPointerHover;
    }

    @Override
    public void setName(String _name) {
        name = _name;
    }

    public void setText(String _text) {
        label.setText(_text);
        label.pack();
    }

    @Override
    public void setPosition(float _x, float _y) {
        label.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        label.setX(_x);
    }

    @Override
    public void setY(float _y) {
        label.setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        label.setSize(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        label.setWidth(_w);
    }

    @Override
    public void setHeight(float _h) {
        label.setHeight(_h);
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
    public String getName() {
        return name;
    }

    public String getText() {
        return label.getText().toString();
    }

    @Override
    public Vector2 getPosition() {
        return (new Vector2(label.getX(), label.getY()));
    }

    @Override
    public float getX() {
        return label.getX();
    }

    @Override
    public float getY() {
        return label.getY();
    }

    @Override
    public Vector2 getSize() {
        label.pack();
        return (new Vector2(label.getWidth(), label.getHeight()));
    }

    @Override
    public float getWidth() {
        return label.getWidth();
    }

    @Override
    public float getHeight() {
        return label.getHeight();
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
        return "label";
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
        label.remove();
    }
}
