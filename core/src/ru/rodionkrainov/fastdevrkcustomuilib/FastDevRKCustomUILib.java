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

package ru.rodionkrainov.fastdevrkcustomuilib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import javax.swing.JFrame;

import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.IButtonClickEvent;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKButton;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKDropdownList;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKImage;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKLabel;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKRadioBox;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKRect;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKSpinner;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKTabPanelsManager;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.CustomInputProcessorUI;
import ru.rodionkrainov.fastdevrkcustomuilib.utils.PointersStates;

public final class FastDevRKCustomUILib {
    public static final String LIB_NAME    = "FastDevRKCustomUILib";
    public static final String LIB_VERSION = "0.4";

    private static boolean IS_DESKTOP;
    private static JFrame JFRAME;

    private static CustomInputProcessorUI customInputProcessorUI;

    private static Batch batch;
    private static ShapeRenderer shapeRenderer;

    private static ExtendViewport extViewport;
    private static int windowWidth;
    private static int windowHeight;

    private static IOnInitAndResLoadedEvent onInitAndResLoadedEvent = null;
    private static boolean isLibInit           = false;
    private static boolean isAllResLoaded      = false;
    private static boolean isCanShowUIElements = false;
    private static boolean isShowLoadingLine   = true;
    private static AssetManager assetManager;

    public enum DefaultImages {
        SETTINGS_ICON,
        INFO_ICON,
        HELP_ICON,
        ARROW_UP,
        ARROW_DOWN
    }
    private static final String[][] DEFAULT_IMAGES_NAMES_PATH = new String[][] {
            {"defImg_settings_icon_defImg", "settings_256x256.png"},
            {"defImg_info_icon_defImg", "info_256x256.png"},
            {"defImg_help_icon_defImg", "help_256x256.png"},
            {"defImg_arrow_up_defImg", "arrow_up_256x256.png"},
            {"defImg_arrow_down_defImg", "arrow_down_256x256.png"},
    };

    private static boolean isZIndexChanged = false;

    // all UI elements (objects)
    private static final ArrayList<IRKUIElement> arrRKUIElements = new ArrayList<>();

    private FastDevRKCustomUILib() { /* ...ignore... */ }

    public static void initLib(InputMultiplexer _inputMultiplexer, int _windowWidth, int _windowHeight, boolean _isDesktop, JFrame _jframe, String _fontFilePath, float _fontBorderWidth, int _fontSpaceX, String _pngFilesFolder, String[][] _imagesNamesPath, boolean _isShowLoadingLine) {
        if (!isLibInit) {
            extViewport = new ExtendViewport(_windowWidth, _windowHeight, (new OrthographicCamera(_windowWidth, _windowHeight)));
            extViewport.apply(true);
            extViewport.getCamera().position.set(_windowWidth / 2f, _windowHeight / 2f, 0);

            customInputProcessorUI = new CustomInputProcessorUI(_isDesktop);
            _inputMultiplexer.addProcessor(customInputProcessorUI);

            batch         = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();

            windowWidth  = _windowWidth;
            windowHeight = _windowHeight;

            IS_DESKTOP = _isDesktop;
            JFRAME     = _jframe;

            isAllResLoaded      = false;
            isCanShowUIElements = false;
            assetManager        = new AssetManager();

            String[][] imagesNamesPath = new String[ (DEFAULT_IMAGES_NAMES_PATH.length + (_imagesNamesPath != null ? _imagesNamesPath.length : 0)) ][2];
            for (int i = 0; i < imagesNamesPath.length; i++) {
                if (i < DEFAULT_IMAGES_NAMES_PATH.length) imagesNamesPath[i] = DEFAULT_IMAGES_NAMES_PATH[i];
                else if (_imagesNamesPath != null) imagesNamesPath[i] = _imagesNamesPath[ (i - DEFAULT_IMAGES_NAMES_PATH.length) ];
            }

            GlobalFontsManager.init(_fontFilePath, _fontBorderWidth, _fontSpaceX);
            GlobalImagesManager.init(_pngFilesFolder, imagesNamesPath, assetManager);

            isShowLoadingLine = _isShowLoadingLine;
            if (!isShowLoadingLine) {
                for (int i = 0; i < GlobalFontsManager.numBpFonts; i++) GlobalFontsManager.loadNext();
                for (int i = 0; i < GlobalImagesManager.numImgTextures; i++) GlobalImagesManager.loadNext(assetManager);
            }

            isLibInit = true;
        }
    }
    public static void initLib(InputMultiplexer _inputMultiplexer, int _windowWidth, int _windowHeight, boolean _isDesktop, JFrame _jframe, String _fontFilePath, float _fontBorderWidth, int _fontSpaceX, boolean _isShowLoadingLine) {
        initLib(_inputMultiplexer, _windowWidth, _windowHeight, _isDesktop, _jframe, _fontFilePath, _fontBorderWidth, _fontSpaceX, null, null, _isShowLoadingLine);
    }
    public static void initLib(InputMultiplexer _inputMultiplexer, int _windowWidth, int _windowHeight, boolean _isDesktop, JFrame _jframe, String _fontFilePath, float _fontBorderWidth, int _fontSpaceX) {
        initLib(_inputMultiplexer, _windowWidth, _windowHeight, _isDesktop, _jframe, _fontFilePath, _fontBorderWidth, _fontSpaceX, null, null, true);
    }

    public static void onInitAndResLoaded(IOnInitAndResLoadedEvent _onInitAndResLoadedEvent) {
        onInitAndResLoadedEvent = _onInitAndResLoadedEvent;
    }


    /* ---------------------------------------------------------
    --------------------- SETTERS / CHANGERS -------------------
    ------------------------------------------------------------ */

    public static void resizeWindow(int _newWindowWidth, int _newWindowHeight) {
        if (isLibInit() && _newWindowWidth != 1 && _newWindowHeight != 1) {
            extViewport.update(_newWindowWidth, _newWindowHeight, true);
            extViewport.getCamera().position.set(extViewport.getCamera().viewportWidth / 2f, extViewport.getCamera().viewportHeight / 2f, 0);

            windowWidth  = (int) unproject(_newWindowWidth, 0).x;
            windowHeight = (int) unproject(_newWindowHeight, 0).x;
        }
    }

    public static Vector3 unproject(float _x, float _y) {
        if (isLibInit()) return extViewport.getCamera().unproject(new Vector3(_x, _y, 0));
        else return new Vector3(-1, -1, -1);
    }

    public static void changeCursor(int _cursorType) {
        if (isLibInit() && IS_DESKTOP && JFRAME != null) {
            JFRAME.setCursor(new Cursor(_cursorType));
        }
    }

    public static void hideLoadingLine() {
        isCanShowUIElements = true;
    }


    /* -------------------------------------------------------------------------------------
    ------------------------------------ UPDATE && DRAW ------------------------------------
    ---------------------------------------------------------------------------------------- */

    public static void update(float _delta) {
        if (isLibInit()) {
            extViewport.apply();
            extViewport.getCamera().update(true);

            batch.setProjectionMatrix(extViewport.getCamera().combined);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            if (!isAllResLoaded()) {
                if (isShowLoadingLine) {
                    for (int i = 0; i < _delta / 10; i++) {
                        GlobalFontsManager.loadNext();
                        GlobalImagesManager.loadNext(assetManager);
                    }
                }

                isAllResLoaded = (getLoadingPercent() == 100f);
                if (isAllResLoaded() && onInitAndResLoadedEvent != null) onInitAndResLoadedEvent.onInitAndResLoaded();
            } else if (isCanShowUIElements) {
                customInputProcessorUI.update();

                // sorting elements by zIndex (from least to most)
                if (isZIndexChanged) {
                    arrRKUIElements.sort(Comparator.comparingInt(IRKUIElement::getZIndex));
                    isZIndexChanged = false;
                }

                // hover event (taking into account zIndex); check focus state
                String checkStep = "hover"; // 'hover' or 'focus'
                boolean isHasHover = false;
                for (IRKUIElement uiElement : arrRKUIElements) uiElement.setIsPointerHover(false);
                for (int i = (arrRKUIElements.size() - 1); i >= 0; i--) {
                    IRKUIElement uiElement = arrRKUIElements.get(i);

                    if (uiElement.isVisible() && uiElement.getAlpha() > 0 && (uiElement.getType().equals("label") || uiElement.getType().equals("rect"))) {
                        if (checkStep.equals("hover")) {
                            Vector2 vMovePos = customInputProcessorUI.getVecPointerMovePosition();
                            isHasHover = (vMovePos.x >= uiElement.getX() && vMovePos.x <= uiElement.getX() + uiElement.getWidth() && vMovePos.y >= uiElement.getY() && vMovePos.y <= uiElement.getY() + uiElement.getHeight());

                            if (isHasHover) {
                                uiElement.setIsPointerHover(true);

                                checkStep = "focus";
                                i = arrRKUIElements.size(); // continue and reset 'i'
                            }
                        }

                        // check focus (is hover and pointer down - element in focus)
                        else {
                            if (uiElement.isPointerHover() && customInputProcessorUI.getPointersStates(false)[0].isDown()) {
                                for (IRKUIElement uiElementUnFocus : arrRKUIElements) uiElementUnFocus.setIsInFocus(false);

                                uiElement.setIsInFocus(true);
                                break;
                            }
                        }
                    }
                }
                if (!isHasHover && customInputProcessorUI.getPointersStates(false)[0].isUp()) for (IRKUIElement uiElementUnFocus : arrRKUIElements) uiElementUnFocus.setIsInFocus(false);

                // update elements
                PointersStates[] pointersStates = customInputProcessorUI.getPointersStates(); // Warning: local variable is important! Do not move into a method argument!
                for (IRKUIElement uiElement : arrRKUIElements) {
                    if (uiElement.isVisible()) uiElement.update(_delta, pointersStates);
                }
            }
        }
    }

    public static void draw() {
        if (isLibInit()) {
            if (Gdx.app.getGraphics().isGL30Available()) {
                Gdx.gl30.glClearColor(GlobalColorsDark.DARK_COLOR_BG.r, GlobalColorsDark.DARK_COLOR_BG.g, GlobalColorsDark.DARK_COLOR_BG.b, GlobalColorsDark.DARK_COLOR_BG.a);
                Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL30.GL_COVERAGE_BUFFER_BIT_NV : 0));
            } else {
                Gdx.gl20.glClearColor(GlobalColorsDark.DARK_COLOR_BG.r, GlobalColorsDark.DARK_COLOR_BG.g, GlobalColorsDark.DARK_COLOR_BG.b, GlobalColorsDark.DARK_COLOR_BG.a);
                Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
            }

            if (isAllResLoaded && isCanShowUIElements) {
                for (IRKUIElement uiElement : arrRKUIElements) {
                    if (uiElement.isVisible() && uiElement.getX() < windowWidth && uiElement.getY() > -uiElement.getHeight()) {
                        batch.begin();
                        uiElement.draw(batch, shapeRenderer);
                        batch.end();
                    }
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
    }


    /* ---------------------------------------------------------
    -------------------------- GETTERS -------------------------
    ------------------------------------------------------------ */

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static boolean isLibInit() {
        if (isLibInit) return true;
        else {
            Gdx.app.log(LIB_NAME + "_ERROR", "The library is not initialized! Use the \"initLib()\" method before you start.");
            return false;
        }
    }

    public static boolean isAllResLoaded() {
        return isAllResLoaded;
    }

    public static boolean isLoadingLineVisible() {
        return isCanShowUIElements;
    }

    public static float getLoadingPercent() {
        if (isLibInit()) return (float) (GlobalFontsManager.numLoadedBpFonts + GlobalImagesManager.numLoadedImgTextures) / (GlobalFontsManager.numBpFonts + GlobalImagesManager.numImgTextures) * 100f;
        return -1;
    }

    public static Viewport getViewport() {
        return (isLibInit() ? extViewport : null);
    }
    public static ExtendViewport getExtendViewport() {
        return (isLibInit() ? extViewport : null);
    }

    public static Camera getCamera() {
        return (isLibInit() ? extViewport.getCamera() : null);
    }
    public static OrthographicCamera getOrthographicCamera() {
        return (isLibInit() ? (OrthographicCamera) extViewport.getCamera() : null);
    }

    public static String getDefaultImageName(DefaultImages _defaultImgName) {
        return (isLibInit() ? DEFAULT_IMAGES_NAMES_PATH[ _defaultImgName.ordinal() ][0] : null);
    }

    public static PointersStates[] getPointersStates() {
        return (isLibInit() ? customInputProcessorUI.getPointersStates(false) : null);
    }

    public static boolean isDesktop() {
        return IS_DESKTOP;
    }

    public static JFrame getJFrame() {
        return JFRAME;
    }


    /* -------------------------------------------------------------------------------------
    ---------------------- RECOURSES (Images, Models and ect) METHODS ----------------------
    ---------------------------------------------------------------------------------------- */

    public static Texture getImageTexture(String _imageName) {
        return (isLibInit() && isAllResLoaded ? GlobalImagesManager.getImageTexture(_imageName, assetManager) : null);
    }


    /* -------------------------------------------------------------------------------------
    ------------------------------ 'ADD / REMOVE ELEMENTS' METHODS -------------------------
    ---------------------------------------------------------------------------------------- */

    public static void addElement(IRKUIElement _element) {
        if (isLibInit() && isAllResLoaded) {
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

    public static void removeElement(String _name) {
        if (isLibInit() && isAllResLoaded) {
            for (int i = 0; i < arrRKUIElements.size(); i++) {
                if (arrRKUIElements.get(i).getName().equals(_name)) {
                    arrRKUIElements.remove(i);
                    break;
                }
            }
        }
    }

    // ---- Label ----
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKLabel rkLabel = new RKLabel(_name, _text, _color, _fontSize, _posX, _posY, _zIndex, _localZIndex);

            addElement(rkLabel);
            return rkLabel;
        }
        return null;
    }
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY, int _zIndex) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, 0);
    }
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, float _posX, float _posY) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex, int _localZIndex) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, _localZIndex);
    }
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize, int _zIndex) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, _zIndex, 0);
    }
    public static RKLabel addLabel(String _name, String _text, Color _color, int _fontSize) {
        return addLabel(_name, _text, _color, _fontSize, 0f, 0f, 0, 0);
    }

    // ---- Rect ----
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, boolean _isRoundRadiusTopLeft, boolean _isRoundRadiusTopRight, boolean _isRoundRadiusBottomLeft, boolean _isRoundRadiusBottomRight, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKRect rkRect = new RKRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, _isRoundRadiusTopLeft, _isRoundRadiusTopRight, _isRoundRadiusBottomLeft, _isRoundRadiusBottomRight, _zIndex, _localZIndex);

            addElement(rkRect);
            return rkRect;
        }
        return null;
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, _localZIndex);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, _zIndex, 0);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, float _roundRadius) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, _roundRadius, true, true, true, true, 0, 0);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, _zIndex, 0);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, 0);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, int _zIndex, int _localZIndex) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, _zIndex, _localZIndex);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor, Color _borderColor, float _borderSize) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, _borderColor, _borderSize, 0, false, false, false, false, 0, 0);
    }
    public static RKRect addRect(String _name, float _posX, float _posY, float _w, float _h, Color _fillColor) {
        return addRect(_name, _posX, _posY, _w, _h, _fillColor, null, -1, 0, false, false, false, false, 0, 0);
    }

    // ---- Tabs ----
    public static RKTabPanelsManager addTabPanelsManager(String _name, float _posX, float _posY, int _labelFontSize, Color _panelsBgColor, Color _panelsBorderColor, float _borderSize, int _zIndex) {
        if (isAllResLoaded) {
            RKTabPanelsManager rkTabPanelsManager = new RKTabPanelsManager(_name, _posX, _posY, _labelFontSize, _panelsBgColor, _panelsBorderColor, _borderSize, _zIndex, 0);

            addElement(rkTabPanelsManager);
            return rkTabPanelsManager;
        }
        return null;
    }
    public static RKTabPanelsManager addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor, int _zIndex) {
        return addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, _zIndex);
    }
    public static RKTabPanelsManager addTabPanelsManager(String _name, int _labelFontSize, Color _panelsBgColor) {
        return addTabPanelsManager(_name, 0f, 0f, _labelFontSize, _panelsBgColor, null, -1, 0);
    }

    // -------- Buttons ---------
    public static RKButton addButton(String _name, String _text, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, IButtonClickEvent _onClickButtonEvent, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKButton rkButton = new RKButton(_name, _text, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _onClickButtonEvent, _zIndex, _localZIndex);

            addElement(rkButton);
            return rkButton;
        }
        return null;
    }
    public static RKButton addButton(String _name, String _text, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, IButtonClickEvent _onClickButtonEvent, int _zIndex) {
        return addButton(_name, _text, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _onClickButtonEvent, _zIndex, 0);
    }
    public static RKButton addButton(String _name, String _text, Color _fontColor, int _fontSize, float _w, float _h, float _borderSize, float _roundRadius, IButtonClickEvent _onClickButtonEvent, int _zIndex) {
        return addButton(_name, _text, _fontColor, _fontSize, 0, 0, _w, _h, _borderSize, _roundRadius, _onClickButtonEvent, _zIndex, 0);
    }

    // --------- Images ----------
    public static RKImage addImage(String _name, String _imgTextureName, float _posX, float _posY, float _w, float _h, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKImage rkImage = new RKImage(_name, _imgTextureName, _posX, _posY, _w, _h, _zIndex, _localZIndex);

            addElement(rkImage);
            return rkImage;
        }
        return null;
    }
    public static RKImage addImage(String _name, String _imgTextureName, float _posX, float _posY, float _w, float _h, int _zIndex) {
        return addImage(_name, _imgTextureName, _posX, _posY, _w, _h, _zIndex, 0);
    }

    // -------- Spinners ---------
    public static RKSpinner addSpinner(String _name, float _defNum, float _min, float _max, float _step, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKSpinner rkSpinner = new RKSpinner(_name, _defNum, _min, _max, _step, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _zIndex, _localZIndex);

            addElement(rkSpinner);
            return rkSpinner;
        }
        return null;
    }
    public static RKSpinner addSpinner(String _name, float _defNum, float _min, float _max, float _step, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex) {
        return addSpinner(_name, _defNum, _min, _max, _step, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _zIndex, 0);
    }
    public static RKSpinner addSpinner(String _name, float _defNum, float _min, float _max, float _step, Color _fontColor, int _fontSize, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex) {
        return addSpinner(_name, _defNum, _min, _max, _step, _fontColor, _fontSize, 0, 0, _w, _h, _borderSize, _roundRadius, _zIndex, 0);
    }

    // ---------- Dropdown lists ----------
    public static RKDropdownList addDropdownList(String _name, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKDropdownList rkDropdownList = new RKDropdownList(_name, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _zIndex, _localZIndex);

            addElement(rkDropdownList);
            return rkDropdownList;
        }
        return null;
    }
    public static RKDropdownList addDropdownList(String _name, Color _fontColor, int _fontSize, float _posX, float _posY, float _w, float _h, float _borderSize, float _roundRadius, int _zIndex) {
        return addDropdownList(_name, _fontColor, _fontSize, _posX, _posY, _w, _h, _borderSize, _roundRadius, _zIndex, 0);
    }

    // ---------- Radio Box ----------
    public static RKRadioBox addRadioBox(String _name, String[] _elements, Color _fontColor, int _fontSize, float _posX, float _posY, int _zIndex, int _localZIndex) {
        if (isAllResLoaded) {
            RKRadioBox rkRadioBox = new RKRadioBox(_name, _elements, _fontColor, _fontSize, _posX, _posY, _zIndex, _localZIndex);

            addElement(rkRadioBox);
            return rkRadioBox;
        }
        return null;
    }
    public static RKRadioBox addRadioBox(String _name, String[] _elements, Color _fontColor, int _fontSize, float _posX, float _posY, int _zIndex) {
        return addRadioBox(_name, _elements, _fontColor, _fontSize, _posX, _posY, _zIndex, 0);
    }


    /* -------------------------------------------------------------------------------------
    --------------------------------- 'ELEMENTS' METHODS -----------------------------------
    ---------------------------------------------------------------------------------------- */

    public static IRKUIElement foundAndGetElement(String _elementName) {
        if (isLibInit() && isAllResLoaded) {
            for (int i = 0; i < arrRKUIElements.size(); i++) {
                if (arrRKUIElements.get(i).getName().equals(_elementName)) return arrRKUIElements.get(i);
            }
        }
        return null;
    }
    public static RKLabel foundAndGetLabel(String _labelName) {
        if (isLibInit() && isAllResLoaded) return (RKLabel) foundAndGetElement(_labelName);
        return null;
    }
    public static RKRect foundAndGetRect(String _rectName) {
        if (isLibInit() && isAllResLoaded) return (RKRect) foundAndGetElement(_rectName);
        return null;
    }
    public static RKTabPanelsManager foundAndGetTabPanelsManager(String _tabPanelsManagerName) {
        if (isLibInit() && isAllResLoaded) return (RKTabPanelsManager) foundAndGetElement(_tabPanelsManagerName);
        return null;
    }
    public static RKButton foundAndGetButton(String _buttonName) {
        if (isLibInit() && isAllResLoaded) return (RKButton) foundAndGetElement(_buttonName);
        return null;
    }


    public static void dispose() {
        for (IRKUIElement uiElement : arrRKUIElements) uiElement.dispose();

        shapeRenderer.dispose();

        GlobalFontsManager.dispose();
        GlobalImagesManager.dispose();
        assetManager.dispose();
    }
}
