/**
*
* Author: Krainov Rodion V. (Chance area) <chancearea@gmail.com>
* Develop started: 19.07.2024
* Description: Cross-platform library for fast and convenient work with GUI based on the LibGDX framework.
*              Designed specifically for the "Servo-motions Control Panel" (https://github.com/chance-area/servo-motions-control-panel)
*              program to simplify the process of its development and debugging.
* License: MIT
*
 **/

package ru.rodionkrainov.libgdxrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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

public class LibGdxRKCustomUILib extends Actor {
    public static final String LIB_NAME    = "LibGdxRKCustomUILib";
    public static final String LIB_VERSION = "0.4";

    private boolean isAllResLoaded;
    private boolean isCanShowUIElements;
    private boolean isShowLoadingLine;
    private final AssetManager assetManager;

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

    public LibGdxRKCustomUILib(String _fontFilePath, float _fontBorderWidth, int _fontSpaceX, String _pngFilesFolder, String[][] _imagesNamesPath, boolean _isShowLoadingLine, float _windowWidth, float _windowHeight, boolean _isDesktop, JFrame _jframe) {
        super();

        windowWidth  = _windowWidth;
        windowHeight = _windowHeight;

        isShowLoadingLine = _isShowLoadingLine;

        isAllResLoaded      = false;
        isCanShowUIElements = false;
        assetManager        = new AssetManager();

        GlobalFontsManager.init(_fontFilePath, _fontBorderWidth, _fontSpaceX);
        if (_pngFilesFolder != null) GlobalImagesManager.init(_pngFilesFolder, _imagesNamesPath, assetManager);

        if (!isShowLoadingLine) {
            for (int i = 0; i < GlobalFontsManager.numBpFonts; i++) GlobalFontsManager.loadNext();
            for (int i = 0; i < GlobalImagesManager.numImgTextures; i++) GlobalImagesManager.loadNext(assetManager);
        }

        IS_DESKTOP = _isDesktop;
        JFRAME     = _jframe;

        // 'unproject' camera already set
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
    public LibGdxRKCustomUILib(String _fontFilePath, float _fontBorderWidth, int _fontSpaceX, float _windowWidth, float _windowHeight, boolean _isDesktop, JFrame _jframe) {
        this(_fontFilePath, _fontBorderWidth, _fontSpaceX, null, null, true, _windowWidth, _windowHeight, _isDesktop, _jframe);
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

    public boolean isAllResLoaded() {
        return isAllResLoaded;
    }

    public void hideLoadingLine() {
        isCanShowUIElements = true;
    }

    public float getLoadingPercent() {
        return (float) (GlobalFontsManager.numLoadedBpFonts + GlobalImagesManager.numLoadedImgTextures) / (GlobalFontsManager.numBpFonts + GlobalImagesManager.numImgTextures) * 100f;
    }

    public boolean isDesktop() {
        return IS_DESKTOP;
    }

    public JFrame getJFrame() {
        return JFRAME;
    }

    /* -------------------------------------------------------------------------------------
    ---------------------- RECOURSES (Images, Models and ect) METHODS ----------------------
    ---------------------------------------------------------------------------------------- */

    public Texture getImageTexture(String _imageName) {
        return (isAllResLoaded ? GlobalImagesManager.getImageTexture(_imageName, assetManager) : null);
    }


    /* -------------------------------------------------------------------------------------
    ------------------------------ 'ADD / REMOVE ELEMENTS' METHODS -------------------------
    ---------------------------------------------------------------------------------------- */

    public void addElement(IRKUIElement _element) {
        if (isAllResLoaded) {
            arrRKUIElements.add(_element);
            arrRKUIElements.sort(Comparator.comparingInt(IRKUIElement::getZIndex));

            ArrayList<ArrayList<IRKUIElement>> tmpArray = new ArrayList<>();
            int tmpIndex = -1, previousZ = -9999;
            for (int i = 0; i < arrRKUIElements.size(); i++) {
                if (tmpIndex == -1 || previousZ != arrRKUIElements.get(i).getZIndex()) {
                    previousZ = arrRKUIElements.get(i).getZIndex();
                    tmpIndex = (tmpIndex == -1 ? 0 : (tmpIndex + 1));

                    tmpArray.add(tmpIndex, new ArrayList<>());
                    if (tmpIndex > 0)
                        tmpArray.get((tmpIndex - 1)).sort(Comparator.comparingInt(IRKUIElement::getLocalZIndex));
                }
                tmpArray.get(tmpIndex).add(arrRKUIElements.get(i));
            }

            arrRKUIElements.clear();
            for (ArrayList<IRKUIElement> list : tmpArray) arrRKUIElements.addAll(list);
        }
    }

    public void removeElement(String _name) {
        if (isAllResLoaded) {
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
    }

    // ---- Label ----
    public RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKLabel rkLabel = new RKLabel(_name, _text, _color, _fontSize, _posX, _posY, _zIndex, _localZIndex, this);

            arrRKLabels.add(rkLabel);
            addElement(rkLabel);
            return rkLabel;
        }
        return null;
    }
    public RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }
    public RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex, int _localZIndex) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, _localZIndex);
    }
    public RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, 0);
    }
    public RKLabel addLabel(String _name, String _text, Color _color, int _fontSize) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }

    // ---- Rect ----
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, boolean _isRoundRadiusTopLeft, boolean _isRoundRadiusTopRight, boolean _isRoundRadiusBottomLeft, boolean _isRoundRadiusBottomRight, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKRect rkRect = new RKRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, _isRoundRadiusTopLeft, _isRoundRadiusTopRight, _isRoundRadiusBottomLeft, _isRoundRadiusBottomRight, _zIndex, _localZIndex);

            arrRKRects.add(rkRect);
            addElement(rkRect);
            return rkRect;
        }
        return null;
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, _localZIndex);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, 0);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, 0, 0);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, _zIndex, 0);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, 0);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex, int _localZIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, _localZIndex);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, 0, 0);
    }
    public RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, 0, 0);
    }

    // ---- Tabs ----
    public RKTabPanelsManager addTabPanelsManager(String _name, float _posX, float _posY, int _labelFontSize, Color _panelsBgColor, Color _panelsBorderColor, float _borderSize, int _zIndex) {
        if (isAllResLoaded) {
            RKTabPanelsManager rkTabPanelsManager = new RKTabPanelsManager(_name, _posX, _posY, _labelFontSize, _panelsBgColor, _panelsBorderColor, _borderSize, _zIndex, 0, this);

            arrRKTabPanelsManagers.add(rkTabPanelsManager);
            addElement(rkTabPanelsManager);
            return rkTabPanelsManager;
        }
        return null;
    }
    public RKTabPanelsManager addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor, int _zIndex) {
        return addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, _zIndex);
    }
    public RKTabPanelsManager addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor) {
        return addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, 0);
    }


    /* -------------------------------------------------------------------------------------
    --------------------------------- 'ELEMENTS' METHODS -----------------------------------
    ---------------------------------------------------------------------------------------- */

    private IRKUIElement foundAndGetElement(String _name) {
        if (isAllResLoaded) {
            for (int i = 0; i < arrRKUIElements.size(); i++) {
                if (arrRKUIElements.get(i).getName().equals(_name)) return arrRKUIElements.get(i);
            }
        }
        return null;
    }
    private RKLabel foundAndGetLabel(String _name) {
        if (isAllResLoaded) {
            for (int i = 0; i < arrRKLabels.size(); i++) {
                if (arrRKLabels.get(i).getName().equals(_name)) return arrRKLabels.get(i);
            }
        }
        return null;
    }
    private RKRect foundAndGetRect(String _name) {
        if (isAllResLoaded) {
            for (int i = 0; i < arrRKRects.size(); i++) {
                if (arrRKRects.get(i).getName().equals(_name)) return arrRKRects.get(i);
            }
        }
        return null;
    }
    private RKTabPanelsManager foundAndGetTabPanelsManager(String _name) {
        if (isAllResLoaded) {
            for (int i = 0; i < arrRKTabPanelsManagers.size(); i++) {
                if (arrRKTabPanelsManagers.get(i).getName().equals(_name)) return arrRKTabPanelsManagers.get(i);
            }
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

    public RKLabel getRKLabel(String _nameLabel) {
        return Objects.requireNonNull(foundAndGetLabel(_nameLabel));
    }

    // ### Rects (getters) ###
    public RKRect getRKRect(String _nameRect) {
        return Objects.requireNonNull(foundAndGetRect(_nameRect));
    }

    // ### Tabs (getters) ###
    public RKTabPanelsManager getRKTabPanelsManager(String _nameTabPanelsManager) {
        return foundAndGetTabPanelsManager(_nameTabPanelsManager);
    }


    /* -------------------------------------------------------------------------------------
    ------------------------------------ UPDATE AND DRAW -----------------------------------
    ---------------------------------------------------------------------------------------- */

    @Override
    public void act(float _delta) {
        super.act(_delta);

        if (!isAllResLoaded()) {
            if (isShowLoadingLine) {
                for (int i = 0; i < _delta / 10; i++) {
                    GlobalFontsManager.loadNext();
                    GlobalImagesManager.loadNext(assetManager);
                }
            }

            isAllResLoaded = (getLoadingPercent() == 100f);
        } else if (isCanShowUIElements) {
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
                    if (i != (arrRKUIElements.size() - 1) && arrRKUIElements.get((i + 1)).getZIndex() != uiElement.getZIndex() && isHasHover)
                        break;
                }
            }

            for (IRKUIElement uiElement : arrRKUIElements) {
                if (uiElement.isVisible()) uiElement.update(_delta);
            }
        }
    }

    @Override
    public void draw(Batch _batch, float _parentAlpha) {
        super.draw(_batch, _parentAlpha);

        shapeRenderer.setProjectionMatrix(_batch.getProjectionMatrix());

        if (isAllResLoaded && isCanShowUIElements) {
            for (IRKUIElement uiElement : arrRKUIElements) {
                if (uiElement.isVisible() && uiElement.getX() < windowWidth && uiElement.getY() > -uiElement.getHeight())
                    uiElement.draw(_batch, shapeRenderer, _parentAlpha);
            }
        } else if (isShowLoadingLine) {
            // draw loading line...
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_WHITE);
            shapeRenderer.rectLine(128f, (windowHeight - 66f) / 2f, windowWidth - 128f, (windowHeight - 66f) / 2f, 8f);
            shapeRenderer.rectLine(128f, (windowHeight - 66f) / 2f - 4f, 128f, (windowHeight - 66f) / 2f + 66f, 8f);
            shapeRenderer.rectLine(124f, (windowHeight - 66f) / 2f + 66f, windowWidth - 128f, (windowHeight - 66f) / 2f + 66f, 8f);
            shapeRenderer.rectLine(windowWidth - 128f, (windowHeight - 66f) / 2f + 70f, windowWidth - 128f, (windowHeight - 66f) / 2f - 4f, 8f);

            shapeRenderer.setColor(GlobalColorsDark.DARK_COLOR_WHITE);
            shapeRenderer.rect(128f + 10f, (windowHeight - 46f) / 2f, (windowWidth - (256f + 20f)) * (getLoadingPercent() / 100f), + 46f);
            shapeRenderer.end();
        }
    }

    public void dispose() {
        shapeRenderer.dispose();

        for (IRKUIElement uiElement : arrRKUIElements) uiElement.dispose();
        GlobalFontsManager.dispose();
        assetManager.dispose();
        this.remove();
    }
}
