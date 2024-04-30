package com.isomapmaker.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	final static int WIDTH = 1600;
	final static float ratio = 0.5625f;
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("JimDangles Iso Map Maker");
		config.setWindowedMode(WIDTH, (int)(WIDTH*ratio));
		new Lwjgl3Application(new IsoMapMaker(), config);
	}
}
