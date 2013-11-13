package com.unknowngames.rainbowrage.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.RainbowRage;

public class SettingsScreen extends BaseClass implements Screen
{
	private Stage stage;
	Skin skin;
	SpriteBatch batch;
	RainbowRage game;
//	private Actor button;
	
	public SettingsScreen(RainbowRage game)
	{
		this.game = game;
		create();
	}
	
	public void create()
	{
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin();
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		
		skin.add("default", new BitmapFont());
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		
		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = skin.getFont("default");
		selectBoxStyle.background = skin.newDrawable("white", Color.CYAN);
		selectBoxStyle.backgroundOpen = skin.newDrawable("white", Color.GREEN);
		selectBoxStyle.backgroundOver = skin.newDrawable("white", Color.RED);
		selectBoxStyle.listBackground = skin.newDrawable("white", Color.YELLOW);
		selectBoxStyle.listSelection = skin.newDrawable("white", Color.PINK);
		
		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = skin.newDrawable("white", Color.CYAN);
		sliderStyle.knob = skin.newDrawable("white", Color.GREEN);
		sliderStyle.knobAfter = skin.newDrawable("white", Color.RED);
		sliderStyle.knobBefore= skin.newDrawable("white", Color.YELLOW);
		
		skin.add("default", textButtonStyle);
		skin.add("default", selectBoxStyle);
		skin.add("default-vertical", sliderStyle);
		
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		final TextButton button = new TextButton("Click me!", skin);
		table.add(button);
		
		button.addListener(new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
				{
					System.out.println("Clicked! Is Checked: " + button.isChecked());
					game.setScreen(game.mainMenuScreen);
//					button.setText("Good job!");
				}
			}
		);
		
		table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
		
		final Slider slider = new Slider(0.0f, 1.0f, .1f, true, skin);
		slider.size(40, 60);
		table.add(slider);
		
		String[] strings = new String[3];
		strings[0] = "First";
		strings[1] = "2nd";
		strings[2] = "Last";
		final SelectBox selectBox = new SelectBox(strings, skin);
		table.add(selectBox);
		
		
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
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() 
	{
		
	}

	@Override
	public void dispose() 
	{
		stage.dispose();
		skin.dispose();
	}

}
