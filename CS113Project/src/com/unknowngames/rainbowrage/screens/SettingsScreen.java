package com.unknowngames.rainbowrage.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.Settings;

public class SettingsScreen extends BaseClass implements Screen
{
	private Stage stage;
	Skin skin;
	SpriteBatch batch;
	RainbowRage game;
	Music settingsMusic;
	Settings settings;
	
	float gameSound, musicSound;
	boolean textEffects;
	int particleEffects, graphics, resolution;
	Table table;
	
	// private Actor button;

	public SettingsScreen(RainbowRage game)
	{
		this.game = game;
		loadCurrentSettings();
		create();
		loadMusic();
	}
	
	private void loadCurrentSettings()
	{
		settings = everything.getSettings();
		gameSound = settings.getGameSound();
		musicSound = settings.getMusicSound();
		textEffects = settings.showTextEffect();
		particleEffects = settings.getParticleEffects();
		graphics = settings.graphics();
		resolution = settings.getResolution();
	}
	
	public String getButtonName(ButtonGroup bg, boolean b)
	{
		return getButtonName(bg, b ? 1 : 0);
	}
	
	public String getButtonName(ButtonGroup bg, int b)
	{
		try
		{
			String temp = ((TextButton)(bg.getButtons().get(b))).getLabel().getText().toString();
			return temp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public void create()
	{
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		createSkin();

		ButtonGroup graphicsButtons = new ButtonGroup();
		Label graphicsLabel = new Label("Graphics", skin);
		TextButton graphicsLowButton = new TextButton("Low", skin);
		TextButton graphicsHighButton = new TextButton("High", skin);
		graphicsButtons.add(graphicsLowButton);
		graphicsButtons.add(graphicsHighButton);
		graphicsButtons.setChecked(getButtonName(graphicsButtons, graphics));
//		graphicsButtons.setChecked(graphics == 0 ? "Low" : "High");

		ButtonGroup effectsButtons = new ButtonGroup();
		Label effectsLabel = new Label("Particle Effects", skin);
		TextButton effectsOffButton = new TextButton("Off", skin);
		TextButton effectsOnButton = new TextButton("On", skin);
		effectsButtons.add(effectsOffButton);
		effectsButtons.add(effectsOnButton);
		effectsButtons.setChecked(getButtonName(effectsButtons, particleEffects));
//		effectsButtons.setChecked(particleEffects == 0 ? "Off" : "On");
		
		ButtonGroup textEffectsButtons = new ButtonGroup();
		Label textEffectsLabel = new Label("Text Effects", skin);
		TextButton textEffectsOffButton = new TextButton("Off", skin);
		TextButton textEffectsOnButton = new TextButton("On", skin);
		textEffectsButtons.add(textEffectsOffButton);
		textEffectsButtons.add(textEffectsOnButton);
		textEffectsButtons.setChecked(getButtonName(textEffectsButtons, textEffects));
//		textEffectsButtons.setChecked(!textEffects ? "Off" : "On");
		
		ButtonGroup resolutionButtons = new ButtonGroup();
		Label resolutionLabel = new Label("Resolution", skin);
		TextButton resolutionFullButton = new TextButton("Fullscreen", skin);
		TextButton resolution1Button = new TextButton("800x480", skin);
		TextButton resolution2Button = new TextButton("1680x1050", skin);
		resolutionButtons.add(resolutionFullButton);
		resolutionButtons.add(resolution1Button);
		resolutionButtons.add(resolution2Button);
		System.out.println("Resolution: " + resolution + getButtonName(resolutionButtons, resolution));
		graphicsButtons.setChecked("800x480"); //getButtonName(resolutionButtons, resolution));
//		switch (graphics)
//		{
//		case 0:
//			graphicsButtons.setChecked("Fullscreen");
//			break;
//		case 1:
//			graphicsButtons.setChecked("800 x 480");
//			break;
//		case 2:
//			graphicsButtons.setChecked("1680 x 1050");
//			break;
//		}

		TextButton backButton = new TextButton("Back", skin);
		TextButton saveButton = new TextButton("Save", skin);

		Label musicLabel = new Label("Music", skin);
		Label audioLabel = new Label("Audio", skin);
		final Slider musicSlider = new Slider(0.0f, 1.0f, .01f, false, skin);
		final Slider audioSlider = new Slider(0.0f, 1.0f, .01f, false, skin);
		musicSlider.setValue(musicSound);
		audioSlider.setValue(gameSound);
		String[] strings = new String[3];
		strings[0] = "First";
		strings[1] = "2nd";
		strings[2] = "Last";

		musicSlider.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				musicSound = musicSlider.getValue();
				settingsMusic.setVolume(musicSound);
			}
		});
		
		audioSlider.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				gameSound = audioSlider.getValue();
			}			
		});
		graphicsLowButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				graphics = 0;
			}
		});

		graphicsHighButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				graphics = 1;

			}
		});
		
		effectsOffButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				particleEffects = 0;
			}
		});

		effectsOnButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				particleEffects = 1;
			}
		});
		
		textEffectsOffButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				textEffects = false;
			}
		});

		textEffectsOnButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				textEffects = true;
			}
		});
		
		resolution1Button.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				setResolution(1);
			}
		});
		
		resolution2Button.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				setResolution(2);
			}
		});
		
		resolutionFullButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				setResolution(0);
			}
		});

		backButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				game.setScreen(game.mainMenuScreen);
			}
		});

		saveButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				saveSettings();
			}
		});

		// table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

//		Table table = new Table();
		table = new Table();
		table.setFillParent(true);
		table.setTransform(true);
		table.setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setScale(everything.getScreenScale());
		stage.addActor(table);
		table.add(effectsLabel).left();
		table.add(effectsOffButton);
		table.add(effectsOnButton);
		table.row().padTop(10);
		table.add(graphicsLabel).left();
		table.add(graphicsLowButton);
		table.add(graphicsHighButton);
		table.row().padTop(10);
		table.add(textEffectsLabel).left();
		table.add(textEffectsOffButton);
		table.add(textEffectsOnButton);
		table.row().padTop(10);
		if (!(Gdx.app.getType() == ApplicationType.Android))
		{
			table.add(resolutionLabel).left();
			table.add(resolutionFullButton).padRight(10);
			table.add(resolution1Button);
			table.add(resolution2Button);
			table.row().padTop(10);
		}
		table.add(musicLabel).left().setWidgetWidth(50);
		table.add(musicSlider);
		table.row().padTop(10);
		table.add(audioLabel).left().width(50);
		table.add(audioSlider);
		table.row().padTop(20);
		table.add(saveButton);
		table.add(backButton);
	}
	
	public void setResolution(int r)
	{
		if (r == 0)
			Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		else if (r == 1)
			Gdx.graphics.setDisplayMode(800, 480, false);
		else if (r == 2)
			Gdx.graphics.setDisplayMode(1680, 1050, false);
		
		resolution = r;
		
		settings.setResolution(r);
		everything.rescale();
		table.setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setScale(everything.getScreenScale());
	}

	public void createSkin()
	{
		skin = new Skin();

		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		skin.add("default", new BitmapFont());

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.GRAY);
		textButtonStyle.font = skin.getFont("default");

		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = skin.getFont("default");
		selectBoxStyle.background = skin.newDrawable("white", Color.CYAN);
		selectBoxStyle.backgroundOpen = skin.newDrawable("white", Color.GREEN);
		selectBoxStyle.backgroundOver = skin.newDrawable("white", Color.RED);
		selectBoxStyle.listBackground = skin.newDrawable("white", Color.YELLOW);
		selectBoxStyle.listSelection = skin.newDrawable("white", Color.PINK);

		// skin.add("atest",
		// EverythingHolder.getObjectTexture("selectredflag"));//skin.newDrawable("white",
		// Color.GREEN);
		TextureRegionDrawable knob = new TextureRegionDrawable(
				EverythingHolder.getObjectTexture("timeicon"));
		TextureRegionDrawable line = new TextureRegionDrawable(
				EverythingHolder.getObjectTexture("selectflagpole"));

		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = skin.newDrawable(line); // skin.newDrawable("white",
															// Color.CYAN);
		sliderStyle.knob = skin.newDrawable(knob); // .add(name, resource)
													// newDrawable(EverythingHolder.getObjectTexture("selectredflag"));//skin.newDrawable("white",
													// Color.GREEN);
		sliderStyle.knobAfter = skin.newDrawable("white",
				Color.valueOf("00000000"));
		sliderStyle.knobBefore = skin.newDrawable("white",
				Color.valueOf("00000000")); // Color.WHITE);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.background = skin.newDrawable("white", new Color(0.0f, 0.0f,
				0.0f, 0.0f));
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.WHITE;

		skin.add("default", textButtonStyle);
		skin.add("default", selectBoxStyle);
		skin.add("default-horizontal", sliderStyle);
		skin.add("default", labelStyle);
	}

	public void loadMusic()
	{
		Audio tempMusic = Gdx.audio;
		settingsMusic = tempMusic.newMusic(Gdx.files
				.internal("audio/460436_trapped_in_dreams.mp3"));
		settingsMusic.setLooping(true);
		settingsMusic.setVolume(musicSound);
		settingsMusic.play();
	}

	public void saveSettings()
	{
		settings.setMusicSound(musicSound);
		settings.setGameSound(gameSound);
		settings.setParticleEffects(particleEffects);
		settings.setGraphics(graphics);
		settings.showTextEffect(textEffects);
		settings.setResolution(resolution);
		settings.saveFile();
		System.out.println("Graphics: " + graphics);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height)
	{
		stage.setViewport(width, height, false);
	}

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		settingsMusic.stop();
	}

	@Override
	public void pause()
	{
		settingsMusic.stop();
	}

	@Override
	public void resume()
	{
		if (settingsMusic != null)
			settingsMusic.play();
	}

	@Override
	public void dispose()
	{
		stage.dispose();
		skin.dispose();
	}

}
