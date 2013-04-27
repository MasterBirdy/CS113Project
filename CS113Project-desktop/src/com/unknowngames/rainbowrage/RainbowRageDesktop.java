package com.unknowngames.rainbowrage;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.unknowngames.rainbowrage.RainbowRage;

public class RainbowRageDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Unknown Defense";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		
//		cfg.width = 1680;
//		cfg.height = 1050;
		
//		cfg.width = 1280;
//		cfg.height = 720;
		cfg.resizable = false;
//		cfg.fullscreen = true;
		
		new LwjglApplication(new RainbowRage(), cfg);
	}
}
