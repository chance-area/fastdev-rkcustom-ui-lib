package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;
import java.util.ArrayList;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public class RKDropdownList extends RKCustomElement {
    private int fontSize;
    private float roundRadius;

    private Color fontColor;

    private final RKRect titleRect;
    private final RKLabel titleLabel;

    private final RKImage arrowImageUp;
    private final RKImage arrowImageDown;

    private String selectedElementText = "";
    private final ArrayList<RKLabel> arrElementsLabels = new ArrayList<>();
    private final ArrayList<RKRect> arrElementsRects   = new ArrayList<>();

    public RKDropdownList(String _name, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        super(_name, "dropdownList", _w, _h, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor(GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX);
        setBorderColor(GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX_BORDER);
        setBorderSize(_borderSize);

        fontColor = _fontColor.cpy();
        fontSize  = _fontSize;

        roundRadius = _roundRadius;

        titleRect  = FastDevRKCustomUILib.addRect("dropdownList_" + _name + "_titleRect", _posX, _posY, _w, _h, getFillColor(), getBorderColor(), getBorderSize(), roundRadius, getZIndex(), (getLocalZIndex() + 1));;
        titleLabel = FastDevRKCustomUILib.addLabel("dropdownList_" + _name + "_titleLabel", selectedElementText, fontColor, _fontSize, _posX, _posY, getZIndex(), getLocalZIndex() + 2);

        arrowImageUp   = FastDevRKCustomUILib.addImage("dropdownList_" + _name + "_imageArrowUp", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_UP), 0, 0, 0, 0, getZIndex(), getLocalZIndex() + 3);
        arrowImageDown = FastDevRKCustomUILib.addImage("dropdownList_" + _name + "_imageArrowDown", FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.ARROW_DOWN), 0, 0, 0, 0, getZIndex(), getLocalZIndex() + 3);

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
            arrElementsLabels.add(FastDevRKCustomUILib.addLabel("dropdownList_" + getName() + "_elementLabel_0", selectedElementText, fontColor, fontSize, getX(), getY(), getZIndex() + 1, getLocalZIndex() + 3));
            arrElementsRects.add(FastDevRKCustomUILib.addRect("dropdownList_" + getName() + "_elementRect_0", getX(), getY(), getWidth(), getHeight(), getFillColor(), getBorderColor(), getBorderSize(), roundRadius, false, false, true, true, getZIndex() + 1, (getLocalZIndex() + 2)));
            arrElementsRects.get( (arrElementsRects.size() - 1) ).setIsBorderUp(false);
        } else {
            for (int i = 0; i < _elementsList.length; i++) {
                arrElementsLabels.add(FastDevRKCustomUILib.addLabel("dropdownList_" + getName() + "_elementLabel_" + i, _elementsList[i], fontColor, fontSize, getX(), getY(), getZIndex() + 1, getLocalZIndex() + 3 + i));
                arrElementsRects.add(FastDevRKCustomUILib.addRect("dropdownList_" + getName() + "_elementRect_" + i, getX(), getY(), getWidth(), getHeight(), getFillColor(), getBorderColor(), getBorderSize(), roundRadius, false, false, (i == (_elementsList.length - 1)), (i == (_elementsList.length - 1)), getZIndex() + 1, (getLocalZIndex() + 2 + i)));
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
    public void update(float _delta, PointersStates[] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            updateElements();

            boolean isElementHover = false;
            for (int i = 0; i < arrElementsRects.size(); i++) {
                if (arrElementsRects.get(i).isPointerHover() || arrElementsLabels.get(i).isPointerHover()) {
                    isElementHover = true;
                    break;
                }
            }
            if (titleRect.isPointerHover() || titleLabel.isPointerHover() || isElementHover) FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);
            if (titleRect.isInFocus() || titleLabel.isInFocus()) {
                arrowImageDown.setVisible(false);
                arrowImageUp.setVisible(true);

                setBorderColor(GlobalColorsDark.DARK_COLOR_BUTTON_HOVER_BORDER);
                titleRect.setIsRoundRadiusBottomLeft(false);
                titleRect.setIsRoundRadiusBottomRight(false);

                for (int i = 0; i < arrElementsRects.size(); i++) {
                    arrElementsRects.get(i).setVisible(true);
                    arrElementsLabels.get(i).setVisible(true);
                }
            } else {
                arrowImageDown.setVisible(true);
                arrowImageUp.setVisible(false);

                setBorderColor(GlobalColorsDark.DARK_COLOR_DROPDOWN_BOX_BORDER);
                titleRect.setIsRoundRadiusBottomLeft(true);
                titleRect.setIsRoundRadiusBottomRight(true);

                for (int i = 0; i < arrElementsRects.size(); i++) {
                    arrElementsRects.get(i).setVisible(false);
                    arrElementsLabels.get(i).setVisible(false);
                }
            }
            titleRect.setFillColor(getFillColor());
            titleRect.setBorderColor(getBorderColor());

            //if (arrElementsTexts.size() == 1 && arrElementsTexts.get(0).isEmpty()) selectedElementText = "";
            for (int i = 0; i < arrElementsRects.size(); i++) {
                RKLabel elementLabel = arrElementsLabels.get(i);
                RKRect elementRect   = arrElementsRects.get(i);

                elementRect.setSize(getWidth(), getHeight());
                elementRect.setPosition(getX(), getY() - (i + 1) * getHeight());
                elementRect.setFillColor(((elementRect.isPointerHover() || elementLabel.isPointerHover()) ? GlobalColorsDark.DARK_COLOR_BUTTON_HOVER : getFillColor()));
                elementRect.setBorderColor(getBorderColor());

                elementLabel.setFontColor(fontColor);
                elementLabel.setPosition(elementRect.getX() + (elementRect.getWidth() - elementLabel.getWidth()) / 2f, elementRect.getY() + (elementRect.getHeight() - elementLabel.getHeight()) / 2f);

                if (elementRect.isPointerHover() || elementLabel.isPointerHover()) {
                    for (PointersStates pointerStates : _pointersStates) {
                        if (pointerStates.isDown()) {
                            selectedElementText = elementLabel.getText();
                            break;
                        }
                    }
                }
            }

            titleLabel.setText(selectedElementText);
            titleLabel.setPosition(getX() + (getWidth() - titleLabel.getWidth()) / 2f, getY() + (getHeight() - titleLabel.getHeight()) / 2f);

            arrowImageDown.setSize(40f, 54f);
            arrowImageUp.setSize(arrowImageDown.getWidth(), arrowImageDown.getHeight());

            arrowImageDown.setPosition(getX() + getWidth() - arrowImageDown.getWidth() - 2f, getY() + (getHeight() - arrowImageDown.getHeight()) / 2f);
            arrowImageUp.setPosition(arrowImageDown.getX(), arrowImageDown.getY());
        }
    }

    private void updateElements() {
        float alpha       = getAlpha();
        int zIndex        = getZIndex();

        titleRect.setAlpha(alpha);
        titleLabel.setAlpha(alpha);
        arrowImageUp.setAlpha(alpha);
        arrowImageDown.setAlpha(alpha);

        titleRect.setZIndex(zIndex);
        titleLabel.setZIndex(zIndex);
        arrowImageUp.setZIndex(zIndex);
        arrowImageDown.setZIndex(zIndex);

        titleRect.setSize(getWidth(), getHeight());
        titleRect.setPosition(getX(), getY());
    }

    @Override
    public void setVisible(boolean _isVisible) {
        super.setVisible(_isVisible);

        titleRect.setVisible(_isVisible);
        titleLabel.setVisible(_isVisible);

        arrowImageUp.setVisible((titleRect.isInFocus() || titleLabel.isInFocus()) && _isVisible);
        arrowImageDown.setVisible(!arrowImageUp.isVisible() && _isVisible);
    }

    @Override
    public void dispose() {
        titleRect.dispose();
        titleLabel.dispose();
        arrowImageDown.dispose();
        arrowImageUp.dispose();
    }
}
