package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;
import java.util.ArrayList;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.IRKUIElement;

public class RKDropdownList implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;
    private boolean isInFocus = false;

    private Color fontColor;
    private Color fillColor;
    private Color borderColor;
    private int fontSize;
    private float alpha      = 1f;
    private float localAlpha = 1f;
    private float borderSize;
    private float roundRadius;

    private final RKRect titleRect;
    private final RKLabel titleLabel;

    private final RKImage arrowImageUp;
    private final RKImage arrowImageDown;

    private String selectedElementText = "";
    private final ArrayList<RKLabel> arrElementsLabels = new ArrayList<>();
    private final ArrayList<RKRect> arrElementsRects   = new ArrayList<>();

    public RKDropdownList(String _name, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fontColor   = _fontColor.cpy();
        fillColor   = GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX.cpy();
        borderColor = GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX_BORDER.cpy();
        borderSize  = _borderSize;
        roundRadius = _roundRadius;
        fontSize = _fontSize;

        titleRect  = FastDevRKCustomUILib.addRect("dropdownList_" + _name + "_titleRect", _posX, _posY, _w, _h, fillColor, borderColor, borderSize, roundRadius, zIndex, (localZIndex + 1));;
        titleLabel = FastDevRKCustomUILib.addLabel("dropdownList_" + _name + "_titleLabel", selectedElementText, fontColor, _fontSize, _posX, _posY, zIndex, localZIndex + 2);

        arrowImageUp   = FastDevRKCustomUILib.addImage("dropdownList_" + _name + "_imageArrowUp", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_UP), 0, 0, 0, 0, zIndex, localZIndex + 3);
        arrowImageDown = FastDevRKCustomUILib.addImage("dropdownList_" + _name + "_imageArrowDown", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_DOWN), 0, 0, 0, 0, zIndex, localZIndex + 3);

        setElementsList(new String[0]);
    }

    public void setElementsList(String[] _elementsList) {
        for (int i = 0; i < arrElementsRects.size(); i++) {
            FastDevRKCustomUILib.removeElement(arrElementsLabels.get(i).getName());
            FastDevRKCustomUILib.removeElement(arrElementsRects.get(i).getName());
        }
        arrElementsLabels.clear();
        arrElementsRects.clear();

        if (_elementsList.length == 0) {
            arrElementsLabels.add(FastDevRKCustomUILib.addLabel("dropdownList_" + name + "_elementLabel_0", selectedElementText, fontColor, fontSize, getX(), getY(), zIndex + 1, localZIndex + 3));
            arrElementsRects.add(FastDevRKCustomUILib.addRect("dropdownList_" + name + "_elementRect_0", getX(), getY(), getWidth(), getHeight(), fillColor, borderColor, borderSize, roundRadius, false, false, true, true, zIndex + 1, (localZIndex + 2)));
            arrElementsRects.get( (arrElementsRects.size() - 1) ).setIsBorderUp(false);
        } else {
            for (int i = 0; i < _elementsList.length; i++) {
                arrElementsLabels.add(FastDevRKCustomUILib.addLabel("dropdownList_" + name + "_elementLabel_" + i, _elementsList[i], fontColor, fontSize, getX(), getY(), zIndex + 1, localZIndex + 3 + i));
                arrElementsRects.add(FastDevRKCustomUILib.addRect("dropdownList_" + name + "_elementRect_" + i, getX(), getY(), getWidth(), getHeight(), fillColor, borderColor, borderSize, roundRadius, false, false, (i == (_elementsList.length - 1)), (i == (_elementsList.length - 1)), zIndex + 1, (localZIndex + 2 + i)));
                arrElementsRects.get(i).setIsBorderUp(false);
                if (i != (_elementsList.length - 1)) arrElementsRects.get(i).setIsBorderDown(false);
            }
            if (selectedElementText.isEmpty()) selectedElementText = arrElementsLabels.get(0).getText();
        }
    }

    public String getSelectedElementText() {
        return selectedElementText;
    }

    @Override
    public void update(float _delta, boolean[][] _pointersStates) {
        if (alpha > 0 && localAlpha > 0) {
            boolean isElementHover = false;
            for (int i = 0; i < arrElementsRects.size(); i++) {
                if (arrElementsRects.get(i).isPointerHover() || arrElementsLabels.get(i).isPointerHover()) {
                    isElementHover = true;
                    break;
                }
            }
            if (titleRect.isPointerHover() || titleLabel.isPointerHover() || isElementHover) FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);
            if (titleRect.isInFocus() || titleLabel.isInFocus()) {
                arrowImageDown.setAlpha(0f);
                arrowImageUp.setAlpha(1f);

                borderColor = GlobalColorsDark.DARK_COLOR_BUTTON_HOVER_BORDER.cpy();
                titleRect.setIsRoundRadiusBottomLeft(false);
                titleRect.setIsRoundRadiusBottomRight(false);

                for (int i = 0; i < arrElementsRects.size(); i++) {
                    arrElementsRects.get(i).setAlpha(1f);
                    arrElementsLabels.get(i).setAlpha(1f);
                }
            } else {
                arrowImageDown.setAlpha(1f);
                arrowImageUp.setAlpha(0f);

                borderColor = GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX_BORDER.cpy();
                titleRect.setIsRoundRadiusBottomLeft(true);
                titleRect.setIsRoundRadiusBottomRight(true);

                for (int i = 0; i < arrElementsRects.size(); i++) {
                    arrElementsRects.get(i).setAlpha(0f);
                    arrElementsLabels.get(i).setAlpha(0f);
                }
            }
            titleRect.setFillColor(fillColor);
            titleRect.setBorderColor(borderColor);

            //if (arrElementsTexts.size() == 1 && arrElementsTexts.get(0).isEmpty()) selectedElementText = "";
            for (int i = 0; i < arrElementsRects.size(); i++) {
                RKLabel elementLabel = arrElementsLabels.get(i);
                RKRect elementRect   = arrElementsRects.get(i);

                elementRect.setSize(getWidth(), getHeight());
                elementRect.setPosition(getX(), getY() - (i + 1) * getHeight());
                elementRect.setFillColor(((elementRect.isPointerHover() || elementLabel.isPointerHover()) ? GlobalColorsDark.DARK_COLOR_BUTTON_HOVER : fillColor));
                elementRect.setBorderColor(borderColor);

                elementLabel.setPosition(elementRect.getX() + (elementRect.getWidth() - elementLabel.getWidth()) / 2f, elementRect.getY() + (elementRect.getHeight() - elementLabel.getHeight()) / 2f);

                if ((elementRect.isPointerHover() || elementLabel.isPointerHover()) && _pointersStates[0][0]) selectedElementText = elementLabel.getText();
            }

            titleLabel.setText(selectedElementText);
            titleLabel.setPosition(getX() + (getWidth() - titleLabel.getWidth()) / 2f, getY() + (getHeight() - titleLabel.getHeight()) / 2f);

            arrowImageDown.setSize(40f, 54f);
            arrowImageUp.setSize(arrowImageDown.getWidth(), arrowImageDown.getHeight());

            arrowImageDown.setPosition(getX() + getWidth() - arrowImageDown.getWidth() - 2f, getY() + (getHeight() - arrowImageDown.getHeight()) / 2f);
            arrowImageUp.setPosition(arrowImageDown.getX(), arrowImageDown.getY());
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (alpha > 0 && localAlpha > 0) {
            //
        }
    }

    @Override
    public void setVisible(boolean _isVisible) {
        isVisible = _isVisible;

        titleRect.setVisible(_isVisible);
        titleLabel.setVisible(_isVisible);
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
        titleRect.setPosition(_x, _y);
    }

    @Override
    public void setX(float _x) {
        titleRect.setX(_x);
    }

    @Override
    public void setY(float _y) {
        titleRect.setY(_y);
    }

    @Override
    public void setSize(float _w, float _h) {
        titleRect.setSize(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        titleRect.setWidth(_w);
    }

    @Override
    public void setHeight(float _h) {
        titleRect.setHeight(_h);
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

        titleRect.setAlpha(_alpha);
        titleLabel.setAlpha(_alpha);
        arrowImageUp.setAlpha(_alpha);
        arrowImageDown.setAlpha(_alpha);
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;

        titleRect.setLocalAlpha(_localAlpha);
        titleLabel.setLocalAlpha(_localAlpha);
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
        return titleRect.getPosition();
    }

    @Override
    public float getX() {
        return titleRect.getX();
    }

    @Override
    public float getY() {
        return titleRect.getY();
    }

    @Override
    public Vector2 getSize() {
        return null;
    }

    @Override
    public float getWidth() {
        return titleRect.getWidth();
    }

    @Override
    public float getHeight() {
        return titleRect.getHeight();
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
        return "dropdownList";
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
