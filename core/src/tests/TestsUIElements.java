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

import ru.rodionkrainov.libgdxrkcustomuilib.GlobalColorsDark;
import ru.rodionkrainov.libgdxrkcustomuilib.LibGdxRKCustomUILib;

public class TestsUIElements extends ApplicationAdapter {
    public static boolean isDesktop       = false;
    public static JFrame superDuperJFrame = null;

    public static float windowWidth  = 1280f;
    public static float windowHeight = 720f;

    public static OrthographicCamera ortCamera = null;
    public static Viewport extViewport         = null;

    private SpriteBatch            batch;
    private ShapeRenderer          shapeRenderer;
    private Stage                  stage;
    public static InputMultiplexer inputMultiplexer;

    private boolean isUIElementsInit = false;
    private LibGdxRKCustomUILib rkCustomUILib = null;

    /* ------------------- Elements names (constants) ------------------- */
    private final String nameLabelFPS            = "label_fps";
    private final String nameTabManagerExample_1 = "tabPanelsManager_example_1";
    private final String nameTabManagerExample_2 = "tabPanelsManager_example_2";

    public TestsUIElements() {
        isDesktop = false;
    }

    public TestsUIElements(JFrame _jframe) {
        isDesktop = true;
        superDuperJFrame = _jframe;
    }

    @Override
    public void create() {
        ortCamera   = new OrthographicCamera(windowWidth, windowHeight);
        extViewport = new ExtendViewport(ortCamera.viewportWidth, ortCamera.viewportHeight, ortCamera);

        extViewport.apply(true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        batch = new SpriteBatch();
        stage = new Stage(extViewport);
        shapeRenderer = new ShapeRenderer();

        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent _event, float _x, float _y, int _pointer, int _button) {
                if (!(_event.getTarget() instanceof TextField) && !(_event.getTarget() instanceof Image)) {
                    stage.setKeyboardFocus(null);
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }

                return false;
            }
        });

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCatchKey(Input.Keys.BACK, true);

        // ------------ ### Init lib ### ------------
        // format: {'name', 'path to file'}
        String[][] imagesNamesPath = new String[][] {
                {"settings_icon", "settings_512x512.png"},
                {"info_icon", "info_512x512.png"},
                {"help_icon", "help_512x512.png"}
        };
        rkCustomUILib = new LibGdxRKCustomUILib("fonts/ubuntu/ubuntu-regular.ttf", 0f, 2, "png", imagesNamesPath, false, windowWidth, windowHeight, isDesktop, superDuperJFrame);

        // ---------------- ### Add to stage ### ------------------
        stage.addActor(rkCustomUILib);
    }

    private void initUIElements() {
        rkCustomUILib.addLabel(nameLabelFPS, "FPS: --", GlobalColorsDark.DARK_COLOR_WHITE, (isDesktop ? 14 : 18), 999);
        //rkCustomUILib.setZIndex(nameLabelFPS, 999);

        rkCustomUILib.addLabel("label_example_1", "Пример текста («первый»)", GlobalColorsDark.DARK_COLOR_RED, (isDesktop ? 20 : 22), 3);
        rkCustomUILib.addLabel("label_example_2", "Пример текста («второй»)", GlobalColorsDark.DARK_COLOR_GREEN, (isDesktop ? 20 : 22), 3);

        rkCustomUILib.addTabPanelsManager(nameTabManagerExample_1, (isDesktop ? 31 : 46), GlobalColorsDark.DARK_COLOR_TABBED_PANEL_1, 1);
        rkCustomUILib.addTabPanelsManager(nameTabManagerExample_2, (isDesktop ? 31 : 46), GlobalColorsDark.DARK_COLOR_TABBED_PANEL_2, 2);

        // tab panel 1 (main)
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).addTab("base_elements", "Базовые элементы");
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).addTab("3d_elements", "3D среда");
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).addTab("graphs_elements", "Графики");
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).addTab("settings_elements", "%#img_settings_icon#%", 1);

        // tab panel 2 (mini, attach to main)
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_2).addTab("example_one", "Пример 1");
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_2).addTab("example_two", "Пример 2");

        // attach elements to main tabPanelsManager (1)
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).attachElementsToTabPanel("base_elements", new String[]{nameTabManagerExample_2});

        // attach elements to mini tabPanelsManager (2)
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_2).attachElementsToTabPanel("example_one", new String[]{"label_example_1"});
        rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_2).attachElementsToTabPanel("example_two", new String[]{"label_example_2"});

        isUIElementsInit = true;
    }

    @Override
    public void resize(int _width, int _height) {
        super.resize(_width, _height);

        extViewport.update(_width, _height, true);
        ortCamera.position.set(ortCamera.viewportWidth / 2f, ortCamera.viewportHeight / 2f, 0);

        windowWidth  = extViewport.getWorldWidth();
        windowHeight = extViewport.getWorldHeight();
        stage.getViewport().update(_width, _height, true);

        rkCustomUILib.changeWindowSize(windowWidth, windowHeight);
        HdpiUtils.glViewport((int) (_width / 2f), (int) (_height / 2f), _width, _height);
    }

    private void update(float _deltaTime) {
        stage.act(_deltaTime);

        if (rkCustomUILib != null) {
            if (rkCustomUILib.isAllResLoaded()) {
                if (!isUIElementsInit) {
                    initUIElements();
                    rkCustomUILib.hideLoadingLine();
                }

                // FPS
                rkCustomUILib.setLabelText(nameLabelFPS, "FPS: " + Gdx.app.getGraphics().getFramesPerSecond());
                rkCustomUILib.setPosition(nameLabelFPS, (windowWidth - rkCustomUILib.getSize(nameLabelFPS).x) - (isDesktop ? 12 : 30), (isDesktop) ? (34 - rkCustomUILib.getSize(nameLabelFPS).y) / 2f : ((36 - rkCustomUILib.getSize(nameLabelFPS).y) / 2f) + 0.7f);

                // Example
                rkCustomUILib.setPosition("label_example_1", (windowWidth - rkCustomUILib.getSize("label_example_1").x) / 2f + 100, 200);
                rkCustomUILib.setPosition("label_example_2", (windowWidth - rkCustomUILib.getSize("label_example_2").x) / 2f + 100, 300);
                rkCustomUILib.setSize(nameTabManagerExample_1, windowWidth, windowHeight - rkCustomUILib.getRKTabPanelsManager(nameTabManagerExample_1).getTabHeight());
                rkCustomUILib.setPosition(nameTabManagerExample_1, 0, 0);

                rkCustomUILib.setSize(nameTabManagerExample_2, (isDesktop ? 500 : 600), 420);
                rkCustomUILib.setPosition(nameTabManagerExample_2, windowWidth * 0.45f, windowHeight * 0.13f);
            }
        }
    }

    private void renderFrame() {
        stage.draw();

        if (rkCustomUILib != null && rkCustomUILib.isAllResLoaded()) {
            // TODO
        }
    }

    @Override
    public void render() {
        update(Gdx.app.getGraphics().getDeltaTime());

        if (Gdx.app.getGraphics().isGL30Available()) {
            Gdx.gl30.glClearColor(GlobalColorsDark.DARK_COLOR_BG.r, GlobalColorsDark.DARK_COLOR_BG.g, GlobalColorsDark.DARK_COLOR_BG.b, GlobalColorsDark.DARK_COLOR_BG.a);
            Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL30.GL_COVERAGE_BUFFER_BIT_NV : 0));
        } else {
            Gdx.gl20.glClearColor(GlobalColorsDark.DARK_COLOR_BG.r, GlobalColorsDark.DARK_COLOR_BG.g, GlobalColorsDark.DARK_COLOR_BG.b, GlobalColorsDark.DARK_COLOR_BG.a);
            Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
        }

        extViewport.apply();
        ortCamera.update(true);

        batch.setProjectionMatrix(extViewport.getCamera().combined);
        batch.begin();
        renderFrame();
        batch.end();
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

        batch.dispose();
        stage.dispose();
        shapeRenderer.dispose();

        if (rkCustomUILib != null) rkCustomUILib.dispose();
    }
}
