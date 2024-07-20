package ru.rodionkrainov.libgdxrkcustomuilib;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import tests.TestsUIElements;

public class DesktopLauncher extends JFrame {
	public DesktopLauncher() {
		double screenScaleFactor = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f;

		SwingUtilities.invokeLater(() -> {
			setTitle(LibGdxRKCustomUILib.LIB_NAME + " (v. " + LibGdxRKCustomUILib.LIB_VERSION + ") " + " - Tests UI elements");
			setSize(new Dimension((int) (TestsUIElements.windowWidth / screenScaleFactor + 14), (int) (TestsUIElements.windowHeight / screenScaleFactor + 37)));
			setPreferredSize(new Dimension(getWidth(), getHeight()));
			setLocationRelativeTo(null);

			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setUndecorated(false);
			setResizable(true);
			setLayout(new GridLayout(1, 1));
			setAlwaysOnTop(false);
			setAutoRequestFocus(true);

			add(new LwjglCanvas(new TestsUIElements(this), getAppConfig()).getCanvas(), SwingConstants.CENTER);

			createBufferStrategy(1);
			setVisible(true);
			toFront();
			requestFocus();
		});
	}

	private LwjglApplicationConfiguration getAppConfig() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width         = (int) TestsUIElements.windowWidth;
		config.height        = (int) TestsUIElements.windowHeight;
		config.resizable     = false;
		config.vSyncEnabled  = true;
		config.forceExit     = false;
		config.backgroundFPS = 90;
		config.foregroundFPS = 90;
		config.r       = 8;
		config.g       = 8;
		config.b       = 8;
		config.a       = 8;
		config.samples = 4;
		config.depth   = 16;
		config.stencil = 4;
		config.useGL30 = true;

		return config;
	}

	public static void main (String[] arg) {
		new DesktopLauncher();
	}
}
