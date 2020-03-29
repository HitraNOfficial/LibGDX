package com.hitran.watertracker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hitran.watertracker.WaterTrackerGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = WaterTrackerGame.WIDTH;
		config.height = WaterTrackerGame.HEIGHT;
		new LwjglApplication(new WaterTrackerGame(), config);
	}
}
