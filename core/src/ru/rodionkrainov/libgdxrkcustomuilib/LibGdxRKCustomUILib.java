package ru.rodionkrainov.libgdxrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import javax.swing.JFrame;

import ru.rodionkrainov.libgdxrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.libgdxrkcustomuilib.uielements.RKLabel;
import ru.rodionkrainov.libgdxrkcustomuilib.uielements.RKRect;
import ru.rodionkrainov.libgdxrkcustomuilib.uielements.RKTabPanelsManager;
import ru.rodionkrainov.libgdxrkcustomuilib.utils.DrawingTools;

public class LibGdxRKCustomUILib extends Actor {
    private final boolean IS_DESKTOP;
    private final JFrame JFRAME;

    private float windowWidth;
    private float windowHeight;

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private boolean isZIndexChanged = false;

    // for click / touch events
    private Vector2 vPointerPosition     = new Vector2(-1, -1);
    private Vector2 vPointerDownPosition = new Vector2(-1, -1);
    private Vector2 vPointerUpPosition   = new Vector2(-1, -1);
    private boolean isPointerDown        = false;

    // all elements
    private final ArrayList<IRKUIElement> arrRKUIElements = new ArrayList<>();

    private final ArrayList<RKLabel>            arrRKLabels            = new ArrayList<>();
    private final ArrayList<RKRect>             arrRKRects             = new ArrayList<>();
    private final ArrayList<RKTabPanelsManager> arrRKTabPanelsManagers = new ArrayList<>();

    public LibGdxRKCustomUILib(String _fontFilePath, float _windowWidth, float _windowHeight, boolean _isDesktop, OrthographicCamera _ortCamera, JFrame _jframe) {
        super();

        windowWidth  = _windowWidth;
        windowHeight = _windowHeight;

        GlobalFontsManager.loadAndInit(_fontFilePath);

        IS_DESKTOP = _isDesktop;
        JFRAME     = _jframe;

        this.setTouchable(Touchable.enabled);
        this.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent _event, float _screenX, float _screenY, int _pointer, int _button) {
                vPointerDownPosition.set(_screenX, _screenY);

                //Gdx.app.log("TouchDown", "x = " + vPointerDownPosition.x + "; y = " + vPointerDownPosition.y);
                return super.touchDown(_event, _screenX, _screenY, _pointer, _button);
            }

            @Override
            public void touchUp(InputEvent _event, float _screenX, float _screenY, int _pointer, int _button) {
                vPointerUpPosition.set(_screenX, _screenY);
                if (!IS_DESKTOP) vPointerDownPosition = new Vector2(-1, -1);

                //Gdx.app.log("TouchUp", "x = " + vPointerUpPosition.x + "; y = " + vPointerUpPosition.y);
                super.touchUp(_event, _screenX, _screenY, _pointer, _button);
            }

            @Override
            public boolean mouseMoved(InputEvent _event, float _screenX, float _screenY) {
                vPointerPosition.set(_screenX, _screenY);

                //Gdx.app.log("TouchMovePos", "x = " + vPointerPosition.x + "; y = " + vPointerPosition.y);
                return super.mouseMoved(_event, _screenX, _screenY);
            }
        });
    }

    public void changeWindowSize(float _windowW, float _windowH) {
        windowWidth  = _windowW;
        windowHeight = _windowH;
    }

    public void changeCursor(int _cursor) {
        if (IS_DESKTOP && JFRAME != null) {
            JFRAME.setCursor(new Cursor(_cursor));
        }
    }

    public boolean isDesktop() {
        return IS_DESKTOP;
    }

    public JFrame getJFrame() {
        return JFRAME;
    }


    /* -------------------------------------------------------------------------------------
    --------------------------------- 'ADD / REMOVE ELEMENTS' METHODS ---------------------------------
    ---------------------------------------------------------------------------------------- */

    private void addElement(IRKUIElement _element) {
        arrRKUIElements.add(_element);
        arrRKUIElements.sort(Comparator.comparingInt(IRKUIElement::getZIndex));

        ArrayList<ArrayList<IRKUIElement>> tmpArray = new ArrayList<>();
        int tmpIndex = -1, previousZ = -9999;
        for (int i = 0; i < arrRKUIElements.size(); i++) {
            if (tmpIndex == -1 || previousZ != arrRKUIElements.get(i).getZIndex()) {
                previousZ = arrRKUIElements.get(i).getZIndex();
                tmpIndex = (tmpIndex == -1 ? 0 : (tmpIndex + 1));

                tmpArray.add(tmpIndex, new ArrayList<>());
                if (tmpIndex > 0) tmpArray.get( (tmpIndex - 1) ).sort(Comparator.comparingInt(IRKUIElement::getLocalZIndex));
            }
            tmpArray.get(tmpIndex).add(arrRKUIElements.get(i));
        }

        arrRKUIElements.clear();
        for (ArrayList<IRKUIElement> list : tmpArray) arrRKUIElements.addAll(list);
    }

    public void removeElement(String _name) {
        String elementType = "none";

        for (int i = 0; i < arrRKUIElements.size(); i++) {
            if (arrRKUIElements.get(i).getName().equals(_name)) {
                elementType = arrRKUIElements.get(i).getType();

                arrRKUIElements.remove(i);
                break;
            }
        }

        switch (elementType) {
            case "label" -> {
                for (int i = 0; i < arrRKLabels.size(); i++) {
                    if (arrRKLabels.get(i).getName().equals(_name)) {
                        arrRKLabels.remove(i);
                        break;
                    }
                }
            }
            case "rect" -> {
                for (int i = 0; i < arrRKRects.size(); i++) {
                    if (arrRKRects.get(i).getName().equals(_name)) {
                        arrRKRects.remove(i);
                        break;
                    }
                }
            }
            case "tab_panels_manager" -> {
                for (int i = 0; i < arrRKTabPanelsManagers.size(); i++) {
                    if (arrRKTabPanelsManagers.get(i).getName().equals(_name)) {
                        arrRKTabPanelsManagers.remove(i);
                        break;
                    }
                }
            }
        }
    }

    // ---- Label ----
    public void addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        RKLabel rkLabel = new RKLabel(_name, _text, _color, _fontSize, _posX, _posY, _zIndex, _localZIndex);

        arrRKLabels.add(rkLabel);
        addElement(rkLabel);
    }
    public void addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY) {
        addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }
    public void addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex, int _localZIndex) {
        addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, _localZIndex);
    }
    public void addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex) {
        addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, 0);
    }
    public void addLabel(String _name, String _text, Color _color, int _fontSize) {
        addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }

    // ---- Rect ----
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, boolean _isRoundRadiusTopLeft, boolean _isRoundRadiusTopRight, boolean _isRoundRadiusBottomLeft, boolean _isRoundRadiusBottomRight, int _zIndex, int _localZIndex) {
        RKRect rkRect = new RKRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, _isRoundRadiusTopLeft, _isRoundRadiusTopRight, _isRoundRadiusBottomLeft, _isRoundRadiusBottomRight, _zIndex, _localZIndex);

        arrRKRects.add(rkRect);
        addElement(rkRect);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, _localZIndex);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, 0);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, 0, 0);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, int _zIndex) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, _zIndex, 0);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, 0);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex, int _localZIndex) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, _localZIndex);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, 0, 0);
    }
    public void addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor) {
        addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, 0, 0);
    }

    // ---- Tabs ----
    public void addTabPanelsManager(String _name, float _posX, float _posY, int _labelFontSize, Color _panelsBgColor, Color _panelsBorderColor, float _borderSize, int _zIndex) {
        RKTabPanelsManager rkTabPanelsManager = new RKTabPanelsManager(_name, _posX, _posY, _labelFontSize, _panelsBgColor, _panelsBorderColor, _borderSize, _zIndex, 0, this);

        arrRKTabPanelsManagers.add(rkTabPanelsManager);
        addElement(rkTabPanelsManager);
    }
    public void addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor, int _zIndex) {
        addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, _zIndex);
    }
    public void addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor) {
        addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, 0);
    }


    /* -------------------------------------------------------------------------------------
    --------------------------------- 'ELEMENTS' METHODS -----------------------------------
    ---------------------------------------------------------------------------------------- */

    private IRKUIElement foundAndGetElement(String _name) {
        for (int i = 0; i < arrRKUIElements.size(); i++) {
            if (arrRKUIElements.get(i).getName().equals(_name)) return arrRKUIElements.get(i);
        }
        return null;
    }
    private RKLabel foundAndGetLabel(String _name) {
        for (int i = 0; i < arrRKLabels.size(); i++) {
            if (arrRKLabels.get(i).getName().equals(_name)) return arrRKLabels.get(i);
        }
        return null;
    }
    private RKTabPanelsManager foundAndGetTabPanelsManager(String _name) {
        for (int i = 0; i < arrRKTabPanelsManagers.size(); i++) {
            if (arrRKTabPanelsManagers.get(i).getName().equals(_name)) return arrRKTabPanelsManagers.get(i);
        }
        return null;
    }

    // ### For All (setters) ###
    public void setName(String _nameElement, String _newName) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setName(_newName);
    }
    public void setPosition(String _nameElement, float _x, float _y) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setPosition(_x, _y);
    }
    public void setX(String _nameElement, float _x) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setX(_x);
    }
    public void setY(String _nameElement, float _y) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setY(_y);
    }
    public void setSize(String _nameElement, float _w, float _h) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setSize(_w, _h);
    }
    public void setWidth(String _nameElement, float _w) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setWidth(_w);
    }
    public void setHeight(String _nameElement, float _h) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setHeight(_h);
    }
    public void setFillColor(String _nameElement, Color _color) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setFillColor(_color);
    }
    public void setBorderColor(String _nameElement, Color _color) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setBorderColor(_color);
    }
    public void setAlpha(String _nameElement, float _alpha) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setAlpha(_alpha);
    }
    public void setLocalAlpha(String _nameElement, float _localAlpha) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setLocalAlpha(_localAlpha);
    }
    public void setVisible(String _nameElement, boolean _isVisible) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setVisible(_isVisible);
    }
    public void setZIndex(String _nameElement, int _zIndex) {
        Objects.requireNonNull(foundAndGetElement(_nameElement)).setZIndex(_zIndex);
        isZIndexChanged = true;
    }

    // ### For All (getters) ###
    public String getType(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getType();
    }
    public Vector2 getPosition(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getPosition();
    }
    public float getX(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getX();
    }
    public float getY(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getY();
    }
    public Vector2 getSize(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getSize();
    }
    public float getWidth(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getWidth();
    }
    public float getHeight(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getHeight();
    }
    public Color getFillColor(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getFillColor();
    }
    public Color getBorderColor(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getBorderColor();
    }
    public float getAlpha(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getAlpha();
    }
    public float getLocalAlpha(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getLocalAlpha();
    }
    public boolean isVisible(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).isVisible();
    }
    public boolean isPointerHover(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).isPointerHover();
    }
    public int getZIndex(String _nameElement) {
        return Objects.requireNonNull(foundAndGetElement(_nameElement)).getZIndex();
    }

    // ### Label (setters) ###
    public void setLabelText(String _nameLabel, String _text) {
        Objects.requireNonNull(foundAndGetLabel(_nameLabel)).setText(_text);
    }

    // ### Label (getters) ###
    public String getLabelText(String _nameLabel) {
        return Objects.requireNonNull(foundAndGetLabel(_nameLabel)).getText();
    }

    // ### Tabs (getters) ###
    public RKTabPanelsManager getTabPanelsManager(String _nameTabPanelsManager) {
        return foundAndGetTabPanelsManager(_nameTabPanelsManager);
    }


    /* -------------------------------------------------------------------------------------
    ------------------------------------ UPDATE AND DRAW -----------------------------------
    ---------------------------------------------------------------------------------------- */

    @Override
    public void act(float _delta) {
        super.act(_delta);

        this.setBounds(0, 0, windowWidth, windowHeight);
        isPointerDown = Gdx.input.isTouched();
        if (!IS_DESKTOP) vPointerPosition = vPointerDownPosition;

        // Sorting elements by zIndex
        if (isZIndexChanged) {
            arrRKUIElements.sort(Comparator.comparingInt(IRKUIElement::getZIndex));
            isZIndexChanged = false;
        }

        // hover event
        boolean isHasHover = false;
        for (IRKUIElement uiElement : arrRKUIElements) uiElement.setIsPointerHover(false);
        for (int i = (arrRKUIElements.size() - 1); i >= 0; i--) {
            IRKUIElement uiElement = arrRKUIElements.get(i);

            if (uiElement.isVisible()) {
                boolean isHover = (vPointerPosition.x >= uiElement.getX() && vPointerPosition.x <= uiElement.getX() + uiElement.getWidth() && vPointerPosition.y >= uiElement.getY() && vPointerPosition.y <= uiElement.getY() + uiElement.getHeight());
                uiElement.setIsPointerHover(isHover);

                if (!isHasHover && isHover) isHasHover = true;
                if (i != (arrRKUIElements.size() - 1) && arrRKUIElements.get( (i + 1) ).getZIndex() != uiElement.getZIndex() && isHasHover) break;
            }
        }

        for (IRKUIElement uiElement : arrRKUIElements) {
            if (uiElement.isVisible()) uiElement.update(_delta);
        }
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);

        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());
        for (IRKUIElement uiElement : arrRKUIElements) {
            if (uiElement.isVisible() && uiElement.getX() < windowWidth && uiElement.getY() > -uiElement.getHeight()) uiElement.draw(_batch, shapeRenderer, _parentAlpha);
        }
    }

    public void dispose() {
        shapeRenderer.dispose();

        for (IRKUIElement uiElement : arrRKUIElements) uiElement.dispose();
        GlobalFontsManager.dispose();
        this.remove();
    }
}
