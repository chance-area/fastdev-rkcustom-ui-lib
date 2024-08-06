package tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.JFrame;

import ru.rodionkrainov.fastdevrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.fastdevrkcustomuilib.FastDevRKCustomUILib;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.IRKUIElement;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKButton;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKDropdownList;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKRadioBox;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKSpinner;
import ru.rodionkrainov.fastdevrkcustomuilib.uielements.base.RKTabPanelsManager;

public class TestsUIElements extends ApplicationAdapter {
    public static boolean isDesktop       = false;
    public static JFrame superDuperJFrame = null;

    public static int windowWidth  = 1280;
    public static int windowHeight = 720;

    public static InputMultiplexer inputMultiplexer;

    private boolean isUIElementsInit = false;

    /* ------------------- Elements names (constants) ------------------- */
    private final String nameLabelFPS = "label_fps";
    private final String nameLabelExampleOne = "label_example_one";
    private final String nameLabelExampleTwo = "label_example_two";

    private RKTabPanelsManager tabPanelsManagerOne; // main
    private RKTabPanelsManager tabPanelsManagerTwo;
    private RKButton buttonOne;
    private RKButton buttonTwo;
    private RKSpinner spinnerOne;
    private RKSpinner spinnerTwo;

    private RKDropdownList dropdownListOne;
    private RKDropdownList dropdownListTwo;

    private RKRadioBox radioBox;

    public TestsUIElements() {
        isDesktop = false;
    }

    public TestsUIElements(JFrame _jframe) {
        isDesktop = true;
        superDuperJFrame = _jframe;
    }

    @Override
    public void create() {
        OrthographicCamera ortCamera   = new OrthographicCamera(windowWidth, windowHeight);
        Viewport extViewport = new ExtendViewport(ortCamera.viewportWidth, ortCamera.viewportHeight, ortCamera);

        extViewport.apply(true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        // ------------ ### Init lib ### ------------
        // format: {'name', 'path to file'}
        String[][] imagesNamesPath = new String[][] {
                //
        };
        FastDevRKCustomUILib.initLib(extViewport, inputMultiplexer, windowWidth, windowHeight, isDesktop, superDuperJFrame, "fonts/ubuntu/ubuntu-regular.ttf", 0f, 2, "png", imagesNamesPath, false);
    }

    private void initUIElements() {
        FastDevRKCustomUILib.addLabel(nameLabelFPS, "FPS: --", GlobalColorsDark.DARK_COLOR_WHITE, (isDesktop ? 14 : 18), 999);
        //FastDevRKCustomUILib.setZIndex(nameLabelFPS, 999);

        // add base elements
        FastDevRKCustomUILib.addLabel(nameLabelExampleOne, "Пример текста («первый»)", GlobalColorsDark.DARK_COLOR_RED, (isDesktop ? 20 : 22), 3);
        FastDevRKCustomUILib.addLabel(nameLabelExampleTwo, "Пример текста («второй»)", GlobalColorsDark.DARK_COLOR_GREEN, (isDesktop ? 20 : 22), 3);

        buttonOne = FastDevRKCustomUILib.addButton("simple_button_1", "Кнопка 1", GlobalColorsDark.DARK_COLOR_WHITE, 24, 200, 100, 130, 50, 2, 6f, (_self) -> {
            _self.setFontColor(GlobalColorsDark.DARK_COLOR_GREEN);
            _self.setText("Нажата!");
        }, 4);
        buttonTwo = FastDevRKCustomUILib.addButton("simple_button_2", "Кнопка 2", GlobalColorsDark.DARK_COLOR_WHITE, 24, 180, 120, 130, 50, 2, 6f, (_self) -> {
            _self.setFontColor(GlobalColorsDark.DARK_COLOR_GREEN);
            _self.setText("Нажата!");
        }, 3);
        buttonOne.setAlpha(0.7f);
        buttonTwo.setAlpha(1f);

        spinnerOne = FastDevRKCustomUILib.addSpinner("simple_spinner_1", 0f, 0f, 4f, 0.02f, GlobalColorsDark.DARK_COLOR_WHITE, 27, 100, 400, 320, 52, 2f, 6f, 3);
        spinnerTwo = FastDevRKCustomUILib.addSpinner("simple_spinner_2", 0f, 0f, 4f, 0.02f, GlobalColorsDark.DARK_COLOR_WHITE, 27, 100, 300, 320, 52, 2f, 6f, 3);

        dropdownListOne = FastDevRKCustomUILib.addDropdownList("dropdown_list_1", GlobalColorsDark.DARK_COLOR_WHITE, 27, 600, 200, 240, 52, 2f, 6f, 3);
        dropdownListOne.setElementsList(new String[]{"Элемент 1", "Элемент 2"});
        dropdownListTwo = FastDevRKCustomUILib.addDropdownList("dropdown_list_2", GlobalColorsDark.DARK_COLOR_WHITE, 27, 600, 100, 240, 52, 2f, 6f, 3);
        //dropdownListTwo.setElementsList(new String[]{"Элемент 1", "Элемент 2"});

        radioBox = FastDevRKCustomUILib.addRadioBox("simple_radioBox", new String[]{"USB (Serial Port)", "Wi-Fi"}, GlobalColorsDark.DARK_COLOR_WHITE, 27, 620, 400, 3);

        // init and add tab panels managers
        tabPanelsManagerOne = FastDevRKCustomUILib.addTabPanelsManager("tab_panels_manager_1", (isDesktop ? 31 : 46), GlobalColorsDark.DARK_COLOR_TABBED_PANEL_1, 1);
        tabPanelsManagerTwo = FastDevRKCustomUILib.addTabPanelsManager("tab_panels_manager_2", (isDesktop ? 31 : 46), GlobalColorsDark.DARK_COLOR_TABBED_PANEL_2, 2);

        // tab panel 1 (main)
        tabPanelsManagerOne.addTab("base_elements", "Базовые элементы");
        tabPanelsManagerOne.addTab("3d_elements", "3D среда");
        tabPanelsManagerOne.addTab("graphs_elements", "Графики");
        tabPanelsManagerOne.addTab("settings_elements", "%#img_" + FastDevRKCustomUILib.getDefaultImageName(FastDevRKCustomUILib.DefaultImages.SETTINGS_ICON) + "#%", 1);

        // tab panel 2 (mini, attach to main)
        tabPanelsManagerTwo.addTab("example_one", "Пример 1");
        tabPanelsManagerTwo.addTab("example_two", "Пример 2");

        // attach elements to main tabPanelsManager (1)
        tabPanelsManagerOne.attachElementsToTabPanel("base_elements", new IRKUIElement[]{tabPanelsManagerTwo, buttonOne, buttonTwo, spinnerOne, spinnerTwo});

        // attach elements to mini tabPanelsManager (2)
        tabPanelsManagerTwo.attachElementsToTabPanel("example_one", new String[]{nameLabelExampleOne, dropdownListOne.getName(), dropdownListTwo.getName(), radioBox.getName()});
        tabPanelsManagerTwo.attachElementsToTabPanel("example_two", new String[]{nameLabelExampleTwo});

        isUIElementsInit = true;
    }

    @Override
    public void resize(int _width, int _height) {
        super.resize(_width, _height);

        FastDevRKCustomUILib.resizeWindow(_width, _height);
        windowWidth  = FastDevRKCustomUILib.getWindowWidth();
        windowHeight = FastDevRKCustomUILib.getWindowHeight();

        HdpiUtils.glViewport((int) (_width / 2f), (int) (_height / 2f), _width, _height);
    }

    private void update(float _deltaTime) {
        FastDevRKCustomUILib.update(_deltaTime);

        if (FastDevRKCustomUILib.isLibInit()) {
            if (FastDevRKCustomUILib.isAllResLoaded()) {
                if (!isUIElementsInit) initUIElements();

                // FPS
                FastDevRKCustomUILib.setLabelText(nameLabelFPS, "FPS: " + Gdx.app.getGraphics().getFramesPerSecond());
                FastDevRKCustomUILib.setPosition(nameLabelFPS, (windowWidth - FastDevRKCustomUILib.getSize(nameLabelFPS).x) - (isDesktop ? 12 : 30), (isDesktop) ? (34 - FastDevRKCustomUILib.getSize(nameLabelFPS).y) / 2f : ((36 - FastDevRKCustomUILib.getSize(nameLabelFPS).y) / 2f) + 0.7f);

                // Example
                FastDevRKCustomUILib.setPosition(nameLabelExampleOne, (windowWidth - FastDevRKCustomUILib.getSize(nameLabelExampleOne).x) / 2f + 100, 200);
                FastDevRKCustomUILib.setPosition(nameLabelExampleTwo, (windowWidth - FastDevRKCustomUILib.getSize(nameLabelExampleTwo).x) / 2f + 100, 300);
                tabPanelsManagerOne.setSize(windowWidth, windowHeight - tabPanelsManagerOne.getTabHeight());
                tabPanelsManagerOne.setPosition(0, 0);

                tabPanelsManagerTwo.setSize((isDesktop ? 520 : 600), 530);
                tabPanelsManagerTwo.setPosition(windowWidth * 0.45f, ((tabPanelsManagerOne.getHeight() - tabPanelsManagerOne.getTabHeight()) - tabPanelsManagerTwo.getHeight()) / 2f);

                if (!buttonOne.isInFocus()) {
                    buttonOne.setFontColor(GlobalColorsDark.DARK_COLOR_WHITE);
                    buttonOne.setText("Кнопка 1");
                }
                if (!buttonTwo.isInFocus()) {
                    buttonTwo.setFontColor(GlobalColorsDark.DARK_COLOR_WHITE);
                    buttonTwo.setText("Кнопка 2");
                }

                if (radioBox.getSelectedElementIndex() == 0) {
                    dropdownListOne.setVisible(true);
                    dropdownListTwo.setVisible(true);
                } else {
                    dropdownListOne.setVisible(false);
                    dropdownListTwo.setVisible(false);
                }

                if (!FastDevRKCustomUILib.isLoadingLineVisible()) FastDevRKCustomUILib.hideLoadingLine();
            }
        }
    }

    private void drawFrame() {
        FastDevRKCustomUILib.draw();
    }

    @Override
    public void render() {
        update(Gdx.app.getGraphics().getDeltaTime());
        drawFrame();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        for (InputProcessor inputProcessor : inputMultiplexer.getProcessors()) inputMultiplexer.removeProcessor(inputProcessor);

        if (FastDevRKCustomUILib.isLibInit()) FastDevRKCustomUILib.dispose();
    }
}
