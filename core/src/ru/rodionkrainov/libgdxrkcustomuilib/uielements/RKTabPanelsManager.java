package ru.rodionkrainov.libgdxrkcustomuilib.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Arrays;

import ru.rodionkrainov.libgdxrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.libgdxrkcustomuilib.LibGdxRKCustomUILib;
import ru.rodionkrainov.libgdxrkcustomuilib.utils.DrawingTools;

public class RKTabPanelsManager implements IRKUIElement {
    private String name;
    private int zIndex;
    private final int localZIndex;
    private boolean isVisible = true;
    private boolean isPointerHover = false;

    private Color fillColor;
    private Color borderColor;
    private float borderSize;

    private float alpha      = 1f;
    private float localAlpha = 1f;

    private final LibGdxRKCustomUILib LIB;
    private final ShapeRenderer shapeRenderer;

    private final Vector2 position;
    private final Vector2 size;
    private float tabHeight;

    private final int LABEL_FONT_SIZE;
    private final float SPACE_BETWEEN_TABS     = 0.75f;
    private final float TABS_TITLES_PADDING_LR = 22f; // Left and Right
    private final float TABS_TITLES_PADDING_UB = 7f; // Up and Bottom
    private final float LINE_HEIGHT            = 1.5f;

    private String selectedTabName = "";
    private boolean isSelectedTabChanged = false;
    private final ArrayList<ArrayList<String>> arrTabs = new ArrayList<>();

    public RKTabPanelsManager(String _name, float _posX, float _posY, int _labelFontSize, Color _panelsBgColor, Color _panelsBorderColor, float _borderSize, int _zIndex, int _localZIndex, LibGdxRKCustomUILib _lib) {
        name   = _name;
        zIndex = _zIndex;
        localZIndex = _localZIndex;

        fillColor   = (_panelsBgColor != null ? _panelsBgColor.cpy() : null);
        borderColor = (_panelsBorderColor != null ? _panelsBorderColor.cpy() : null);
        borderSize  = _borderSize;

        LABEL_FONT_SIZE = _labelFontSize;
        LIB             = _lib;

        position = new Vector2(_posX, _posY);
        size     = new Vector2(-1, -1);

        shapeRenderer = new ShapeRenderer();
    }

    public void addTab(String _tabName, String _tabTitle) {
        String labelName = "tabManager_" + name + "_tab_" + "-" + _tabName + "_tabLabel_" + arrTabs.size();
        String rectName  = "tabManager_" + name + "_tab_" + "-" + _tabName + "_tabRect_" + arrTabs.size();

        LIB.addLabel(labelName, _tabTitle, GlobalColorsDark.DARK_COLOR_TABBED_TEXT, LABEL_FONT_SIZE, zIndex, (localZIndex - 1));
        LIB.addRect(rectName, -1, -1, 0, 0, GlobalColorsDark.DARK_COLOR_TABS, zIndex, (localZIndex - 2));

        LIB.setVisible(labelName, isVisible);
        LIB.setVisible(rectName, isVisible);

        arrTabs.add(new ArrayList<>(Arrays.asList(labelName, rectName, _tabName, _tabTitle)));

        tabHeight = LIB.getSize(labelName).y + TABS_TITLES_PADDING_UB * 2 + LINE_HEIGHT;
        if (selectedTabName.isEmpty()) selectedTabName = _tabName;
    }

    public void attachElementsToTabPanel(String _tabNameToAttach, String[] _attachElementsNames) {
        for (int i = 0; i < arrTabs.size(); i++) {
            if (arrTabs.get(i).get(2).equals(_tabNameToAttach)) {
                for (String attachElementName : _attachElementsNames) {
                    arrTabs.get(i).add(attachElementName);
                    LIB.setVisible(attachElementName, isVisible);
                }
            }
        }
    }

    public void setSelectedTab(String _tabName) {
        selectedTabName = _tabName;
        isSelectedTabChanged = true;
    }

    public String getSelectedTabName() {
        return selectedTabName;
    }

    public ArrayList<ArrayList<String>> getArrTabs() {
        return arrTabs;
    }

    public int getLabelFontSize() {
        return LABEL_FONT_SIZE;
    }

    @Override
    public void update(float _delta) {
        if (isVisible && alpha > 0 && localAlpha > 0) {
            fillColor.a = Math.min(alpha, localAlpha);
            if (borderColor != null) borderColor.a = Math.min(alpha, localAlpha);

            float sumW = 0;
            for (int i = 0; i < arrTabs.size(); i++) {
                String labelName = arrTabs.get(i).get(0);
                String rectName  = arrTabs.get(i).get(1);

                LIB.setZIndex(labelName, zIndex);
                LIB.setZIndex(rectName, zIndex);

                float tabWidth = LIB.getSize(labelName).x + TABS_TITLES_PADDING_LR * 2;

                LIB.setPosition(labelName, getPosition().x + (i > 0 ? (SPACE_BETWEEN_TABS + sumW) : 0) + (tabWidth - LIB.getSize(labelName).x) / 2f, getPosition().y + getHeight() + ((tabHeight + LINE_HEIGHT * 1.5f) - LIB.getSize(labelName).y) / 2f);
                LIB.setPosition(rectName, getPosition().x + (i > 0 ? (SPACE_BETWEEN_TABS + sumW) : 0), getPosition().y + getHeight() + LINE_HEIGHT);
                LIB.setSize(rectName, tabWidth, tabHeight - LINE_HEIGHT);

                sumW += tabWidth + (i > 0 ? SPACE_BETWEEN_TABS : 0);

                // hover event
                if (LIB.isPointerHover(labelName) || LIB.isPointerHover(rectName)) {
                    LIB.changeCursor(Cursor.HAND_CURSOR);
                    LIB.setFillColor(rectName, GlobalColorsDark.DARK_COLOR_TAB_HOVER);

                    if (Gdx.input.justTouched()) setSelectedTab(arrTabs.get(i).get(2));
                } else {
                    LIB.setFillColor(rectName, GlobalColorsDark.DARK_COLOR_TABS);
                }
            }

            // tabs content (attached elements) - change visible on select tab
            if (isSelectedTabChanged) {
                for (int i = 0; i < arrTabs.size(); i++) {
                    if (arrTabs.get(i).size() > 4) {
                        boolean isElementsVisible = arrTabs.get(i).get(2).equals(selectedTabName);

                        for (int j = 4; j < arrTabs.get(i).size(); j++) {
                            String element = arrTabs.get(i).get(j);
                            LIB.setLocalAlpha(element, (isElementsVisible ? LIB.getAlpha(element) : 0f));
                        }
                    }
                }
            }
            isSelectedTabChanged = false;
        }
    }

    @Override
    public void draw(Batch _batch, ShapeRenderer _shapeRenderer, float _parentAlpha) {
        if (isVisible && alpha > 0 && localAlpha > 0) {
            if (!arrTabs.isEmpty()) {
                shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
                _batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                DrawingTools.enableGLBlend();

                // ----- draw content panel (bg) -----
                shapeRenderer.setColor(fillColor);
                shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

                // draw line
                shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_TAB_LINE.r, GlobalColorsDark.DARK_COLOR_TAB_LINE.g, GlobalColorsDark.DARK_COLOR_TAB_LINE.b, Math.min(alpha, localAlpha));
                shapeRenderer.rect(getX(), getY() + getHeight(), getWidth(), LINE_HEIGHT);

                // draw selected tab line
                String selectedRectName = null;
                for (ArrayList<String> tab : arrTabs) {
                    if (tab.get(2).equals(selectedTabName)) {
                        selectedRectName = tab.get(1);
                        break;
                    }
                }
                if (selectedRectName != null) {
                    shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.r, GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.g, GlobalColorsDark.DARK_COLOR_TAB_SELECTED_LINE.b, Math.min(alpha, localAlpha));
                    shapeRenderer.rect(LIB.getX(selectedRectName), LIB.getY(selectedRectName) - LINE_HEIGHT, LIB.getWidth(selectedRectName), LINE_HEIGHT * 3f);
                }

                shapeRenderer.end();
                DrawingTools.disableGLBlend();
                _batch.begin();
            }
        }
    }

    @Override
    public void setVisible(boolean _isVisible) {
        isVisible = _isVisible;
        isSelectedTabChanged = true;

        for (int i = 0; i < arrTabs.size(); i++) {
            LIB.setVisible(arrTabs.get(i).get(0), _isVisible);
            LIB.setVisible(arrTabs.get(i).get(1), _isVisible);

            if (arrTabs.get(i).size() > 4) {
                for (int j = 4; j < arrTabs.get(i).size(); j++) {
                    LIB.setVisible(arrTabs.get(i).get(j), _isVisible);
                }
            }
        }
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
        position.set(_x, _y);
    }

    @Override
    public void setX(float _x) {
        position.set(_x, position.y);
    }

    @Override
    public void setY(float _y) {
        position.set(position.x, _y);
    }

    @Override
    public void setSize(float _w, float _h) {
        size.set(_w, _h);
    }

    @Override
    public void setWidth(float _w) {
        size.x = _w;
    }

    @Override
    public void setHeight(float _h) {
        size.y = _h;
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

        for (int i = 0; i < arrTabs.size(); i++) {
            LIB.setAlpha(arrTabs.get(i).get(0), alpha);
            LIB.setAlpha(arrTabs.get(i).get(1), alpha);

            if (arrTabs.get(i).size() > 4) {
                for (int j = 4; j < arrTabs.get(i).size(); j++) {
                    LIB.setAlpha(arrTabs.get(i).get(j), alpha);
                }
            }
        }
    }

    @Override
    public void setLocalAlpha(float _localAlpha) {
        localAlpha = _localAlpha;
        isSelectedTabChanged = true;

        for (int i = 0; i < arrTabs.size(); i++) {
            LIB.setLocalAlpha(arrTabs.get(i).get(0), localAlpha);
            LIB.setLocalAlpha(arrTabs.get(i).get(1), localAlpha);

            if (arrTabs.get(i).size() > 4) {
                for (int j = 4; j < arrTabs.get(i).size(); j++) {
                    LIB.setLocalAlpha(arrTabs.get(i).get(j), localAlpha);
                }
            }
        }
    }

    @Override
    public void setZIndex(int _zIndex) {
        zIndex = _zIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public float getY() {
        return position.y;
    }

    @Override
    public Vector2 getSize() {
        return size;
    }

    @Override
    public float getWidth() {
        return size.x;
    }

    @Override
    public float getHeight() {
        return size.y;
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

    public float getTabHeight() {
        return tabHeight;
    }

    @Override
    public String getType() {
        return "tab_panels_manager";
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
        //
    }
}
