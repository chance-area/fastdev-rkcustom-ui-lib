package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.DrawingTools;

public class RKRect extends RKCustomElement {
    private final float roundRadius;
    private boolean isRoundRadiusTopLeft;
    private boolean isRoundRadiusTopRight;
    private boolean isRoundRadiusBottomLeft;
    private boolean isRoundRadiusBottomRight;

    private boolean isBorderDown = true;
    private boolean isBorderUp   = true;

    private final Rectangle rectangle;

    public RKRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, boolean _isRoundRadiusTopLeft, boolean _isRoundRadiusTopRight, boolean _isRoundRadiusBottomLeft, boolean _isRoundRadiusBottomRight, int _zIndex, int _localZIndex) {
        super(_name, "rect", _w, _h, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor(_fillColor);
        setBorderColor(_borderColor);
        setBorderSize(_borderSize);

        roundRadius              = _roundRadius;
        isRoundRadiusTopLeft     = _isRoundRadiusTopLeft;
        isRoundRadiusTopRight    = _isRoundRadiusTopRight;
        isRoundRadiusBottomLeft  = _isRoundRadiusBottomLeft;
        isRoundRadiusBottomRight = _isRoundRadiusBottomRight;

        rectangle = new Rectangle(_posX, _posY, _w, _h);
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            rectangle.setSize(getWidth(), getHeight());
            rectangle.setPosition(getX(), getY());
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            _batch.end();
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            DrawingTools.enableGLBlend();

            float borderSize = getBorderSize();

            _shapeRenderer.setColor(getFillColor());
            if (!isRoundRadiusTopLeft && !isRoundRadiusTopRight && !isRoundRadiusBottomLeft && !isRoundRadiusBottomRight) {
                if (borderSize > 0 && getBorderColor() != null) {
                    _shapeRenderer.setColor(getBorderColor());
                    _shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());

                    _shapeRenderer.setColor(getFillColor());
                    if (isBorderDown && isBorderUp) _shapeRenderer.rect(rectangle.getX() + borderSize, rectangle.getY() + borderSize, rectangle.getWidth() - borderSize * 2f, rectangle.getHeight() - borderSize * 2f);
                    else                            _shapeRenderer.rect(rectangle.getX() + borderSize, rectangle.getY(), rectangle.getWidth() - borderSize * 2f, rectangle.getHeight());
                } else {
                    _shapeRenderer.rect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
                }
            } else {
                float posX = rectangle.getX();
                float posY = rectangle.getY();
                float width = rectangle.getWidth();
                float height = rectangle.getHeight();

                if (borderSize > 0 && getBorderColor() != null) {
                    _shapeRenderer.setColor(getBorderColor());
                    drawRoundedRect(_shapeRenderer, posX, posY, width, height, roundRadius);

                    _shapeRenderer.setColor(getFillColor());
                    if (isBorderDown && isBorderUp) drawRoundedRect(_shapeRenderer, posX + borderSize, posY + borderSize, width - borderSize * 2, height - borderSize * 2, (roundRadius - borderSize));
                    else if (isBorderDown)          drawRoundedRect(_shapeRenderer, posX + borderSize, posY + borderSize, width - borderSize * 2, height - borderSize, (roundRadius - borderSize));
                    else if (isBorderUp)            drawRoundedRect(_shapeRenderer, posX + borderSize, posY, width - borderSize * 2, height - borderSize, (roundRadius - borderSize));
                    else                            drawRoundedRect(_shapeRenderer, posX + borderSize, posY, width - borderSize * 2, height, (roundRadius - borderSize));
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
        int arcSegments = 32;

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

    public void setIsRoundRadiusTopLeft(boolean _isRoundRadiusTopLeft) {
        isRoundRadiusTopLeft = _isRoundRadiusTopLeft;
    }
    public void setIsRoundRadiusTopRight(boolean _isRoundRadiusTopRight) {
        isRoundRadiusTopRight = _isRoundRadiusTopRight;
    }
    public void setIsRoundRadiusBottomLeft(boolean _isRoundRadiusBottomLeft) {
        isRoundRadiusBottomLeft = _isRoundRadiusBottomLeft;
    }
    public void setIsRoundRadiusBottomRight(boolean _isRoundRadiusBottomRight) {
        isRoundRadiusBottomRight = _isRoundRadiusBottomRight;
    }

    public void setIsBorderDown(boolean _isBorderDown) {
        isBorderDown = _isBorderDown;
    }
    public void setIsBorderUp(boolean _isBorderUp) {
        isBorderUp = _isBorderUp;
    }
}
