package com.unknowngames.rainbowrage.screens;

import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.networking.ClientNetwork;

public class LoginScreen extends BaseClass implements Screen
{
	private Stage stage;
	Skin skin;
	SpriteBatch batch;
	RainbowRage game;
	ClientNetwork clientNetwork;
	TextField nameText, passwordText;
	Label messageLabel;
	HeroSelectScreen selectScreen;
	LobbyScreen lobbyScreen;
	
	public LoginScreen(RainbowRage game)
	{
		this.game = game;
		clientNetwork = new ClientNetwork(this);
		create();
	}
	
	public void create()
	{
		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		skin = everything.getSkin();
//		createSkin();
		addWidgets();
		
		clientNetwork.connect();
	}
	
	public boolean validateFields()
	{
		
		if (!validate(nameText.getText()))
		{
			messageLabel.setText("Invalid username");
			return false;
		}
		else if (!validate(passwordText.getText()))
		{
			messageLabel.setText("Invalid password");
			return false;
		}
		
		return true;
		/*Pattern pattern;
		Matcher matcher;
		String usernamePattern = "^[a-zA-Z][a-zA-Z0-9]{5,15}$";
		
		pattern = Pattern.compile(usernamePattern);
		
		matcher = pattern.matcher(nameText.getText());
		if (!matcher.matches())
		{
			messageLabel.setText("Invalid username");
			return false;
		}
		
		matcher = pattern.matcher(passwordText.getText());
		if (!matcher.matches())
		{
			messageLabel.setText("Invalid password");
			return false;
		}
		
		return true;*/
	}
	
	public boolean validate(String s)
	{
		Pattern pattern;
		Matcher matcher;
		String usernamePattern = "^[a-zA-Z][a-zA-Z0-9]{4,15}$";
		
		pattern = Pattern.compile(usernamePattern);
		
		matcher = pattern.matcher(s);
		if (!matcher.matches())
		{
//			messageLabel.setText("Invalid username");
			return false;
		}
		
		return true;
	}
	
	public boolean login()
	{
		return login(nameText.getText(), passwordText.getText());
		/*if (!validateFields())
			return false;
		
		messageLabel.setText("Establishing connection...");
		clientNetwork.connect();
		if (!clientNetwork.connected())
		{
			messageLabel.setText("Connection failed...");
			return false;
		}
		
		messageLabel.setText("Sending login information...");
		clientNetwork.login(nameText.getText(), passwordText.getText());
		
		check();*/
		
		/*if (!clientNetwork.loggedIn())
		{
			switch(clientNetwork.failCode())
			{
			case -1:
				messageLabel.setText("Incorrect username/password.");
				break;
			case -2:
				messageLabel.setText("Server is full, try again later.");
				break;
			case -3:
				messageLabel.setText("Server down for maintenance.");
				break;
			default:
				messageLabel.setText("Something new happened: " + clientNetwork.failCode());
				break;
			}
			return false;
		}
		
		messageLabel.setText("Successfully logged in!");*/
		
		//return true;
	}
	
	public boolean login(String name, String pass)
	{
		if (clientNetwork.loggedIn())
			return false;
		
		System.out.println("Name: " + name + " Pass: " + pass);
		if (!validate(name))
		{
			messageLabel.setText("Invalid username");
			return false;
		}
		if (!validate(pass))
		{
			messageLabel.setText("Invalid password");
			return false;
		}
		System.out.println("Name: " + name + " Pass: " + pass);
		
		messageLabel.setText("Establishing connection...");
		clientNetwork.connect();
		if (!clientNetwork.connected())
		{
			messageLabel.setText("Connection failed...");
			return false;
		}
		
		messageLabel.setText("Sending login information...");
		clientNetwork.login(name, pass);
		
		check();
		return true;
	}
	
	public void check()
	{
		System.out.println("Checking");
		if (!clientNetwork.loggedIn())
		{
			switch(clientNetwork.failCode())
			{
			case -1:
				messageLabel.setText("Incorrect username/password");
				break;
			case -2:
				messageLabel.setText("Server is full, try again later");
				break;
			case -3:
				messageLabel.setText("Server down for maintenance");
				break;
			case -4:
				messageLabel.setText("This name is already logged in");
				break;
			/*default:
				messageLabel.setText("Something new happened: " + clientNetwork.failCode());
				break;*/
			}
			return;
		}
		
		messageLabel.setText("Successfully logged in!");
	}
	
	public void joinRoom()
	{
		lobbyScreen = new LobbyScreen(game);
		game.setScreen(lobbyScreen);
		/*selectScreen = new HeroSelectScreen(game, true);
		game.setScreen(selectScreen);*/
	}
	
	public void goBack()
	{
		clientNetwork.disconnect();
		game.setScreen(game.mainMenuScreen);
	}
	
	public void addWidgets()
	{	
//		Image background = new Image(new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480));
		Label nameLabel = new Label("Name:", skin);
		nameText = new TextField("Name", skin);
		Label passwordLabel = new Label("Password:", skin);
		passwordText = new TextField("", skin);
		passwordText.setPasswordMode(true);
		passwordText.setPasswordCharacter('*');
		final TextButton loginButton = new TextButton("Login", skin);
		loginButton.addListener(new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
				{
					login();
//					System.out.println("Clicked! Is Checked: " + loginButton.isChecked());
//					game.setScreen(game.mainMenuScreen);
				}
			}
		);
		
		final TextButton checkButton = new TextButton("Check", skin);
		checkButton.addListener(new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
				{
					check();
				}
			}
		);
		
		final TextButton guestButton = new TextButton("Guest", skin);
		guestButton.addListener(new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
				{
					nameText.setText("Guest");
					passwordText.setText("Guest");
					login();
					//login("Guest", "Guest");
				}
			}
		);
		
		final TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ChangeListener()
			{
				public void changed(ChangeEvent event, Actor actor)
				{
					goBack();
				}
			}
		);
		
		messageLabel = new Label("", skin);
		
		Table table = new Table();
		table.setTransform(true);
		table.setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setScale(everything.getScreenScale());
//		table.setScale(1.5f);
		table.setFillParent(true);
//		table.debug();
//		table.debugTable();
		stage.addActor(table);
		table.setBackground(skin.getDrawable("background"));	
		
		table.add(nameLabel).left();
		table.add(nameText).width(200);//.padLeft(5).padRight(5);
		table.row().spaceTop(10);
		table.add(passwordLabel).left();
		table.add(passwordText).width(200);//.padLeft(20).padRight(20);;
		table.row();
		table.add(messageLabel).colspan(2);
		table.row().spaceTop(15);
		table.add(guestButton).left().width(70);
		table.add(loginButton).right().width(70);
		table.row().spaceTop(15);
		table.add(backButton).right().width(70).colspan(2);
//		table.add(checkButton).right().width(60);
		
		
	}
	
	public void createSkin()
	{
		skin = new Skin();
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		
//		skin.add("default", new BitmapFont());
//		skin.add("default", EverythingHolder.getFont(0));
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kingthings Exeter.ttf"));
		BitmapFont font = generator.generateFont(30);
		skin.add("default", font);
		
		//skin.add("background", new TextureRegion(new Texture(Gdx.files.internal("images/mainmenubackground.jpg")), 0, 0, 800, 480));
		skin.add("background", EverythingHolder.getObjectTexture("mainbackground"));
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.WHITE);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		textButtonStyle.fontColor = Color.BLACK;
		
		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = skin.getFont("default");
		selectBoxStyle.background = skin.newDrawable("white", Color.CYAN);
		selectBoxStyle.backgroundOpen = skin.newDrawable("white", Color.GREEN);
		selectBoxStyle.backgroundOver = skin.newDrawable("white", Color.RED);
//		selectBoxStyle.listBackground = skin.newDrawable("white", Color.YELLOW);
//		selectBoxStyle.listSelection = skin.newDrawable("white", Color.PINK);
		
		SliderStyle sliderStyle = new SliderStyle();
		sliderStyle.background = skin.newDrawable("white", Color.CYAN);
		sliderStyle.knob = skin.newDrawable("white", Color.GREEN);
		sliderStyle.knobAfter = skin.newDrawable("white", Color.RED);
		sliderStyle.knobBefore= skin.newDrawable("white", Color.YELLOW);
		
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
		textFieldStyle.cursor = skin.newDrawable("white", Color.BLACK);
		textFieldStyle.disabledBackground = skin.newDrawable("white", Color.RED);
		textFieldStyle.selection = skin.newDrawable("white", Color.GRAY);
		textFieldStyle.font = skin.getFont("default");
		textFieldStyle.fontColor = Color.BLACK;
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.background = skin.newDrawable("white", new Color(0.0f, 0.0f, 0.0f, 0.0f));
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.WHITE;
		
		skin.add("default", textButtonStyle);
		skin.add("default", selectBoxStyle);
		skin.add("default-vertical", sliderStyle);
		skin.add("default", textFieldStyle);
		skin.add("default", labelStyle);
	}
	
	@Override
	public void render(float delta) 
	{
		update();
		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);
	}
	
	public void update()
	{
		if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
		{
			if (stage.getKeyboardFocus() == nameText || stage.getKeyboardFocus() == passwordText)
				login();
		}
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

}
