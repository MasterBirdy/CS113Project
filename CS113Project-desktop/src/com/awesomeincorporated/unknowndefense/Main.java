package com.awesomeincorporated.unknowndefense;

import com.awesomeincorporated.unknowndefense.UnknownDefense;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Unknown Defense";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
//		cfg.width = 1280;
//		cfg.height = 720;
		cfg.resizable = false;
		
		new LwjglApplication(new UnknownDefense(), cfg);
	}
}
