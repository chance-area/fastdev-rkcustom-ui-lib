package ru.rodionkrainov.libgdxrkcustomuilib.uielements.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.rodionkrainov.libgdxrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.libgdxrkcustomuilib.utils.DrawingTools;

public class RKRect implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;

    private Color fillColor;
    private Color borderColor;
    private float alpha      = 1f;
    private float localAlpha = 1f;
    private float borderSize;

    private int arcSegments = 32;
    private float roundRadius;
    private boolean isRoundRadiusTopLeft;
    private boolean isRoundRadiusTopRight;
    private boolean isRoundRadiusBottomLeft;
    private boolean isRoundRadiusBottomRight;

    private final Rectangle rectangle;

    public RKRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, boolean _isRoundRadiusTopLeft, boolean _isRoundRadiusTopRight, boolean _isRoundRadiusBottomLeft, boolean _isRoundRadiusBottomRight, int _zIndex, int _localZIndex) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fillColor   = _fillColor.cpy();
        borderColor = (_borderColor != null ? _borderColor.cpy() : null);
        borderSize  = _borderSize;

        roundRadius              = _roundRadius;
        isRoundRadiusTopLeft     = _isRoundRadiusTopLeft;
        isRoundRadiusTopRight    = _isRoundRadiusTopRight;
        isRoundRadiusBottomLeft  = _isRoundRadiusBottomLeft;
        isRoundRadiusBottomRight = _isRoundRadiusBottomRight;

        rectangle = new Rectangle(_posX, _posY, _w, _h);
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (alpha > 0 && localAlpha > 0) {
            fillColor.a = Math.min(alpha, localAlpha);
            if (borderColor != null) borderColor.a = Math.min(alpha, localAlpha);
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (alpha > 0 && localAlpha > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            DrawingTools.enableGLBlend();

            _shapeRenderer.setColor(fillColor);
            if (!isRoundRadiusTopLeft && !isRoundRadiusTopRight && !isRoundRadiusBottomLeft && !isRoundRadiusBottomRight) {
                if (borderSize > 0 && borderColor != null) {
                    _shapeRenderer.setColor(borderColor);
                    _shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

                    _shapeRenderer.setColor(fillColor);
                    _shapeRenderer.rect(rectangle.getX() + borderSize, rectangle.getY() + borderSize, rectangle.getWidth() - borderSize * 2f, rectangle.getHeight() - borderSize * 2f);
                } else {
                    _shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                }
            } else {
                float posX = rectangle.getX();
                float posY = rectangle.getY();
                float width = rectangle.getWidth();
                float height = rectangle.getHeight();

                if (borderSize > 0 && borderColor != null) {
                    _shapeRenderer.setColor(borderColor);
                    drawRoundedRect(_shapeRenderer, posX, posY, width, height, roundRadius);

                    _shapeRenderer.setColor(fillColor);
                    drawRoundedRect(_shapeRenderer, posX + borderSize, posY + borderSize, width - borderSize * 2, height - borderSize * 2, (roundRadius - borderSize));
                } else {
                    drawRoundedRect(_shapeRenderer, posX, posY, width, height, roundRadius);
                }
            }

            _shapeRenderer.end();
            DrawingTools.disableGLBlend();
            _batch.begin();
        }
    }

    private void drawRoundedRect(ShapeRenderer _shapeRenderer, float _posX, float _posY, float _width, float _height, float _radius) {
        _shapeRenderer.rect(_posX + _radius, _posY + _radius, _width - _radius * 2f, _height - _radius * 2f);

        if (isRoundRadiusTopLeft) {
            _shapeRenderer.rect(_posX, _posY + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_posX + _radius, _posY + _height - _radius, _radius, 90f, 90f, arcSegments);
        } else _shapeRenderer.rect(_posX, _posY + _radius, _radius, _height - _radius);

        if (isRoundRadiusTopRight) {
            _shapeRenderer.rect(_posX + _radius, _posY + _height - _radius, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_posX + _width - _radius, _posY + _height - _radius, _radius, 0f, 90f, arcSegments);
        } else _shapeRenderer.rect(_posX + _radius, _posY + _height - _radius, _width - _radius, _radius);

        if (isRoundRadiusBottomLeft) {
            _shapeRenderer.rect(_posX + _radius, _posY, _width - _radius * 2f, _radius);
            _shapeRenderer.arc(_posX + _radius, _posY + _radius, _radius, 180f, 90f, arcSegments);
        } else _shapeRenderer.rect(_posX, _posY, _width - _radius, _radius);

        if (isRoundRadiusBottomRight) {
            _shapeRenderer.rect(_posX + _width - _radius, _posY + _radius, _radius, _height - _radius * 2f);
            _shapeRenderer.arc(_posX + _width - _radius, _posY + _radius, _radius, 270f, 90f, arcSegments);
        } else _shapeRenderer.rect(_posX + _width - _radius, _posY, _radius, _height - _radius);
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
        rectangle.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        rectangle.setX(_x);
    }

    @Override
    public void setY(float _y) {
        rectangle.setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        rectangle.setSize(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        rectangle.setWidth(_w);
    }

    @Override
    public void setHeight(float _h) {
        rectangle.setHeight(_h);
    }

    @Override
    public void setFillColor(Color _color) {
        fillColor = _color.cpy();
        fillColor.a = Math.min(alpha, localAlpha);
    }

    @Override
    public void setBorderColor(Color _color) {
        borderColor = _color.cpy();
        borderColor.a = Math.min(alpha, localAlpha);
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
        return (new Vector2(rectangle.getX(), rectangle.getY()));
    }

    @Override
    public float getX() {
        return rectangle.getX();
    }

    @Override
    public float getY() {
        return rectangle.getY();
    }

    @Override
    public Vector2 getSize() {
        return (new Vector2(rectangle.getWidth(), rectangle.getHeight()));
    }

    @Override
    public float getWidth() {
        return rectangle.getWidth();
    }

    @Override
    public float getHeight() {
        return rectangle.getHeight();
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
        return "rect";
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
        // TODO
    }
}
