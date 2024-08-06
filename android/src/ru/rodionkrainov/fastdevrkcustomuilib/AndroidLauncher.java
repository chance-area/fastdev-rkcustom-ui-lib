package ru.rodionkrainov.fastdevrkcustomuilib;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import tests.TestsUIElements;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
			Gdx.app.log("FATAL_ERROR", String.valueOf(error));

			if(handler != null) handler.uncaughtException(thread, error);
			else {
				Gdx.app.log("FATAL_ERROR", String.valueOf(error));
				System.exit(1);
			}
		});
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useImmersiveMode = true;
		config.useGL30          = true; // Warring: this is experimental!

		// RGBA8888
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;

		// Smooth
		config.numSamples = 4;
		config.depth      = 16;
		config.stencil    = 2;

		config.useCompass       = false;
		config.useGyroscope     = false;
		config.useAccelerometer = false;
		config.useWakelock      = true; // <- FLAG_KEEP_SCREEN_ON

		initialize(new TestsUIElements(), config);
	}
}
