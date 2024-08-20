package ru.rodionkrainov.fastdevrkcustomuilib.uielements.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Arrays;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.RKCustomElement;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.DrawingTools;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public class RKTabPanelsManager extends RKCustomElement {
    private float tabHeight;
    private float tabsWidthSum;

    private final int LABEL_FONT_SIZE;
    private final float SPACE_BETWEEN_TABS     = 0.65f;
    private final float TABS_TITLES_PADDING_LR = 22f; // Left and Right
    private final float TABS_TITLES_PADDING_UB = 8f; // Up and Bottom
    private final float LINE_HEIGHT            = 1.5f; // under all tabs
    private final float LINE_HEIGHT_KOF        = 3f; // for selected tab

    // for animations
    private float lineHeight_animationY = 0f;

    private String selectedTabName = "";
    private String previousSelectedTabName = "";
    private boolean isSelectTabClick     = false; // for line animation
    private boolean isSelectedTabChanged = false;
    private final ArrayList<ArrayList<String>> arrTabs = new ArrayList<>();

    private final ArrayList<RKLabel> arrLabels = new ArrayList<>();
    private final ArrayList<RKRect> arrRects   = new ArrayList<>();
    private final ArrayList<ArrayList<IRKUIElement>> arrAttachElements = new ArrayList<>();

    public RKTabPanelsManager(String _name, float _posX, float _posY, int _labelFontSize, Color _panelsBgColor, Color _panelsBorderColor, float _borderSize, int _zIndex, int _localZIndex) {
        super(_name, "tab_panels_manager", -1, -1, _posX, _posY, 1f, 1f, _zIndex, _localZIndex);

        setFillColor((_panelsBgColor != null ? _panelsBgColor.cpy() : null));
        setBorderColor((_panelsBorderColor != null ? _panelsBorderColor.cpy() : null));
        setBorderSize(_borderSize);

        LABEL_FONT_SIZE = _labelFontSize;
    }

    public void addTab(String _tabName, String _tabTitle, int _tabRectAlign) {
        String labelName = "tabManager_" + getName() + "_tab_" + "-" + _tabName + "_tabLabel_" + arrTabs.size();
        String rectName  = "tabManager_" + getName() + "_tab_" + "-" + _tabName + "_tabRect_" + arrTabs.size();

        arrLabels.add(FastDevRKCustomUILib.addLabel(labelName, _tabTitle, GlobalColorsDark.DARK_COLOR_TABS_TITLE_TEXT, LABEL_FONT_SIZE, getZIndex(), (getLocalZIndex() - 1)));
        arrRects.add(FastDevRKCustomUILib.addRect(rectName, -1, -1, 0, 0, GlobalColorsDark.DARK_COLOR_TABS_TITLE_RECT, getZIndex(), (getLocalZIndex() - 2)));

        arrLabels.get( (arrLabels.size() - 1) ).setVisible(isVisible());
        arrRects.get( (arrRects.size() - 1) ).setVisible(isVisible());

        arrTabs.add(new ArrayList<>(Arrays.asList(_tabName, _tabTitle, (_tabRectAlign == 1 ? "right" : "left"))));

        tabHeight = arrLabels.get( (arrLabels.size() - 1) ).getHeight() + TABS_TITLES_PADDING_UB * 2 + LINE_HEIGHT;
        if (selectedTabName.isEmpty()) {
            selectedTabName         = _tabName;
            previousSelectedTabName = _tabName;
        }
    }
    public void addTab(String _tabName, String _tabTitle) {
        addTab(_tabName, _tabTitle, 0);
    }

    public void attachElementsToTabPanel(String _tabNameToAttach, String[] _attachElementsNames) {
        for (int i = 0; i < arrTabs.size(); i++) {
            if (arrTabs.get(i).get(0).equals(_tabNameToAttach)) {
                arrAttachElements.add(new ArrayList<>());

                for (String attachElementName : _attachElementsNames) {
                    arrAttachElements.get(i).add(FastDevRKCustomUILib.foundAndGetElement(attachElementName));
                    arrAttachElements.get(i).get( (arrAttachElements.get(i).size() - 1) ).setVisible(isVisible());
                }
            }
        }
    }
    public void attachElementsToTabPanel(String _tabNameToAttach, IRKUIElement[] _attachElements) {
        String[] elementsNames = new String[ _attachElements.length ];
        for (int i = 0; i < elementsNames.length; i++) elementsNames[i] = _attachElements[i].getName();

        attachElementsToTabPanel(_tabNameToAttach, elementsNames);
    }

    public void setSelectedTab(String _tabName) {
        if (!_tabName.equals(selectedTabName)) {
            previousSelectedTabName = selectedTabName;

            selectedTabName = _tabName;
            isSelectedTabChanged = true;
        }
    }

    public String getSelectedTabName() {
        return selectedTabName;
    }

    public int getLabelFontSize() {
        return LABEL_FONT_SIZE;
    }

    @Override
    public void update(float _delta, PointersStates[] _pointersStates) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            setIsInFocus(true);

            float sumWidthLeft = 0f, sumWidthRight = 0f;
            for (int i = 0; i < arrTabs.size(); i++) {
                RKLabel rkLabel = arrLabels.get(i);
                RKRect  rkRect  = arrRects.get(i);

                rkLabel.setZIndex(getZIndex());
                rkRect.setZIndex(getZIndex());

                float tabRectWidth = rkLabel.getWidth() + TABS_TITLES_PADDING_LR * 2 - (rkLabel.isHasImage() ? TABS_TITLES_PADDING_LR * 1.4f : 0);
                float tabRectX     = getPosition().x + (arrTabs.get(i).get(2).equals("left") ? (i > 0 ? (SPACE_BETWEEN_TABS + sumWidthLeft) : 0) : (getWidth() - tabRectWidth) - (i > 0 ? (SPACE_BETWEEN_TABS + sumWidthRight) : 0));
                float tabRectY     = getPosition().y + getHeight() + LINE_HEIGHT;

                rkRect.setPosition(tabRectX, tabRectY);
                rkRect.setSize(tabRectWidth, tabHeight - LINE_HEIGHT);
                rkLabel.setPosition(tabRectX + (tabRectWidth - rkLabel.getWidth()) / 2f, tabRectY + (tabHeight - LINE_HEIGHT - rkLabel.getHeight()) / 2f);

                if (arrTabs.get(i).get(2).equals("left")) sumWidthLeft += tabRectWidth + (i > 0 ? SPACE_BETWEEN_TABS : 0);
                else sumWidthRight += tabRectWidth + (i > 0 ? SPACE_BETWEEN_TABS : 0);

                // hover and click events
                if (rkLabel.isPointerHover() || rkRect.isPointerHover()) {
                    FastDevRKCustomUILib.changeCursor(Cursor.HAND_CURSOR);
                    rkRect.setFillColor(GlobalColorsDark.DARK_COLOR_TABS_TITLE_RECT_HOVER);
                    rkLabel.setFontColor(GlobalColorsDark.DARK_COLOR_TABS_TITLE_TEXT_HOVER);

                    for (PointersStates pointerStates : _pointersStates) {
                        if (pointerStates.isUp()) {
                            setSelectedTab(arrTabs.get(i).get(0));
                            isSelectTabClick = true;

                            break;
                        }
                    }
                } else {
                    rkRect.setFillColor((arrTabs.get(i).get(0).equals(selectedTabName) ? GlobalColorsDark.DARK_COLOR_TAB_SELECTED_TITLE_RECT : GlobalColorsDark.DARK_COLOR_TABS_TITLE_RECT));
                    rkLabel.setFontColor(GlobalColorsDark.DARK_COLOR_TABS_TITLE_TEXT);
                }
            }
            tabsWidthSum = sumWidthLeft;

            // tabs content (attached elements) - change visible on select tab
            if (isSelectedTabChanged) {
                // for line animation
                if (isSelectTabClick && !previousSelectedTabName.equals(selectedTabName)) {
                    lineHeight_animationY = LINE_HEIGHT * LINE_HEIGHT_KOF;
                    isSelectTabClick = false;
                }

                for (int i = 0; i < arrTabs.size(); i++) {
                    if (i < arrAttachElements.size()) {
                        boolean isElementsVisible = arrTabs.get(i).get(0).equals(selectedTabName);

                        for (IRKUIElement attachElement : arrAttachElements.get(i)) {
                            //attachElement.setLocalAlpha(isElementsVisible ? attachElement.getAlpha() : 0f);
                            attachElement.setVisible(isElementsVisible);
                        }
                    }
                }
            }
            isSelectedTabChanged = false;

            // line (selected tab) animation
            if (lineHeight_animationY != 0) {
                lineHeight_animationY = Math.max(0, lineHeight_animationY - LINE_HEIGHT / (4.6f - _delta * 100f));
            }
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer) {
        if (isVisible() && getAlpha() > 0 && getLocalAlpha() > 0) {
            if (!arrTabs.isEmpty()) {
                _batch.end();
                _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                DrawingTools.enableGLBlend();

                // ----- draw content panel (bg) -----
                _shapeRenderer.setColor(getFillColor());
                _shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

                // draw line
                _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_UNDER_ALL_TABS_LINE.r, GlobalColorsDark.DARK_COLOR_UNDER_ALL_TABS_LINE.g, GlobalColorsDark.DARK_COLOR_UNDER_ALL_TABS_LINE.b, Math.min(getAlpha(), getLocalAlpha()));
                _shapeRenderer.rect(getX(), getY() + getHeight(), getWidth(), LINE_HEIGHT);

                // draw selected tab line
                RKRect selectedRect = null, previousRect = null;
                for (int i = 0; i < arrTabs.size(); i++) {
                    if (arrTabs.get(i).get(0).equals(selectedTabName))              selectedRect = arrRects.get(i);
                    else if (arrTabs.get(i).get(0).equals(previousSelectedTabName)) previousRect = arrRects.get(i);

                    if (selectedRect != null && previousRect != null) break;
                }
                if (selectedRect != null) {
                    _shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.r, GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.g, GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.b, Math.min(getAlpha(), getLocalAlpha()));
                    _shapeRenderer.rect(selectedRect.getX(), selectedRect.getY() - LINE_HEIGHT, selectedRect.getWidth(), LINE_HEIGHT * LINE_HEIGHT_KOF - lineHeight_animationY);

                    // for line animation
                    if (previousRect != null) _shapeRenderer.rect(previousRect.getX(), previousRect.getY() - LINE_HEIGHT, previousRect.getWidth(), lineHeight_animationY);
                }

                _shapeRenderer.end();
                DrawingTools.disableGLBlend();
                _batch.begin();
            }
        }
    }

    @Override
    public void setVisible(boolean _isVisible) {
        super.setVisible(_isVisible);

        isSelectedTabChanged = true;

        for (int i = 0; i < arrRects.size(); i++) {
            arrRects.get(i).setVisible(_isVisible);
            arrLabels.get(i).setVisible(_isVisible);
        }
        for (int i = 0; i < arrAttachElements.size(); i++) {
            for (int j = 0; j < arrAttachElements.get(i).size(); j++) {
                arrAttachElements.get(i).get(j).setVisible(_isVisible);
            }
        }
    }

    public float getTabsWidthSum() {
        return tabsWidthSum;
    }

    public float getTabHeight() {
        return tabHeight;
    }

    @Override
    public void setAlpha(float _alpha) {
        for (int i = 0; i < arrRects.size(); i++) {
            arrRects.get(i).setAlpha(_alpha);
            arrLabels.get(i).setAlpha(_alpha);
        }
        for (int i = 0; i < arrAttachElements.size(); i++) {
            for (int j = 0; j < arrAttachElements.get(i).size(); j++) {
                arrAttachElements.get(i).get(j).setAlpha(_alpha);
            }
        }
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        isSelectedTabChanged = true;

        for (int i = 0; i < arrRects.size(); i++) {
            arrRects.get(i).setLocalAlpha(_localAlpha);
            arrLabels.get(i).setLocalAlpha(_localAlpha);
        }
        for (int i = 0; i < arrAttachElements.size(); i++) {
            for (int j = 0; j < arrAttachElements.get(i).size(); j++) {
                arrAttachElements.get(i).get(j).setLocalAlpha(_localAlpha);
            }
        }
    }

    @Override
    public void setZIndex(int _zIndex) {
        for (int i = 0; i < arrRects.size(); i++) {
            arrRects.get(i).setZIndex(_zIndex);
            arrLabels.get(i).setZIndex(_zIndex);
        }
        for (int i = 0; i < arrAttachElements.size(); i++) {
            for (int j = 0; j < arrAttachElements.get(i).size(); j++) {
                arrAttachElements.get(i).get(j).setZIndex(_zIndex);
            }
        }
    }
}
