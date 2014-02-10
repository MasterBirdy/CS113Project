package com.unknowngames.rainbowrage.screens;

import java.util.Calendar;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.RainbowRage;
import com.unknowngames.rainbowrage.networking.Network.ClientToServerMessage;
import com.unknowngames.rainbowrage.networking.Network.LobbyChatMessage;
import com.unknowngames.rainbowrage.networking.Network.StartGameRoom;
import com.unknowngames.rainbowrage.player.Player;

public class LobbyScreen extends BaseClass implements Screen
{
	private Stage stage;
	SpriteBatch batch;
	Image profilePic;
	RainbowRage game;
	Client client;
	HeroSelectScreen selectScreen;
	Table table, chatTable, chatAndFriendArea, topArea, bottomArea;
	ThreadedListener threadedListener;
	Label chatArea, friendArea, timeLabel, userWinLoss;
	TextField chatMessage;
	String chatAreaText = "", friendAreaText = "";
	ScrollPane chatAreaPane, friendAreaPane;
	Skin skin;
	Calendar calendar;
	float lastPaneY = 0;
	
	public LobbyScreen(RainbowRage game)
	{
		this.game = game;
		stage = new Stage();
		batch = new SpriteBatch();
		Gdx.input.setInputProcessor(stage);
		
		setupNetworking();
		
		addWidgets();
	}
	
	public void setupNetworking()
	{
		client = everything.getClient();
		
		threadedListener = new ThreadedListener(new Listener()
		{
			public void received(Connection connection, Object object)
			{
				System.out.println("Lobby recevied server message");
				if (object instanceof StartGameRoom)
				{
					System.out.println("Lobby recevied start room");
					StartGameRoom info = (StartGameRoom)object;
					everything.setTeam((byte)(info.team));
					Player[] ps = new Player[info.publicPlayerInfos.length];
					for (int i = 0; i < info.publicPlayerInfos.length; i++)
					{
						ps[i] = new Player(info.publicPlayerInfos[i]);
					}
					everything.setPlayers(ps);
					joinRoom();
				}
				else if (object instanceof LobbyChatMessage)
				{
					addLobbyChatMessage((LobbyChatMessage)object);
				}
			}
		});
		
		client.addListener(threadedListener);
	}
	
	private void addLobbyChatMessage(LobbyChatMessage msg)
	{
		System.out.println("Received message: " + msg.message);
		chatAreaText += " " + msg.message + "\n";
		chatArea.setText(chatAreaText);
//		System.out.println("Scroll Y: " + chatAreaPane.getScrollY() + " : " + chatAreaPane.getMaxY());
	}
	
	public void joinRoom()
	{
		selectScreen = new HeroSelectScreen(game, true, this);
		game.setScreen(selectScreen);
	}	
	
	public void addWidgets()
	{
		skin = everything.getSkin();
		final TextButton playButton = new TextButton("Play", skin);
		final TextButton profileButton = new TextButton("Profile", skin);
		profilePic = new Image(EverythingHolder.getObjectTexture("profilepic" + everything.getPrivatePlayerInfo().profilePic));
		final TextButton backButton = new TextButton("Main", skin);
		final TextButton settingsButton = new TextButton("Settings", skin);
		Label userName = new Label(everything.getPrivatePlayerInfo().shownName, skin, "small");
		userWinLoss = new Label("W " + everything.getPrivatePlayerInfo().wins + " / " + everything.getPrivatePlayerInfo().losses + " L", skin, "small");
		
		// Chat area
		chatArea = new Label("", skin, "chat");
		chatArea.setAlignment(Align.left | Align.bottom);
		chatArea.setWrap(true);
		chatAreaPane = new ScrollPane(chatArea);
		chatAreaPane.setOverscroll(false, false);
		chatAreaPane.setScrollBarPositions(true, false);
		chatAreaPane.setColor(Color.BLACK);
		chatMessage = new TextField("", skin);
		
		// Friend area
		friendArea = new Label("", skin, "chat");
		friendArea.setAlignment(Align.left | Align.top);
		friendArea.setWrap(false);
		friendAreaPane = new ScrollPane(friendArea);
		friendAreaPane.setOverscroll(false, false);
		friendAreaPane.setScrollBarPositions(true, false);
		friendAreaPane.setColor(Color.BLACK);		
		
		final TextButton sendButton = new TextButton("Send", skin);
		timeLabel = new Label("7:02pm", skin);
		
		topArea = new Table();
		topArea.setTransform(true);
		topArea.add(playButton).pad(20);
		topArea.add(settingsButton).padLeft(40);
		topArea.add(backButton).padLeft(40);
		topArea.add().expandX().fillX();
		topArea.add(profileButton).padLeft(20);
		topArea.add(userName).padLeft(20);
		topArea.add(userWinLoss).padLeft(10);
		topArea.add(profilePic).padLeft(20).size(70);
		
		
		Table chatAreaBorder = new Table();
		chatAreaBorder.setTransform(true);
		chatAreaBorder.setBackground(skin.getDrawable("background"));
		chatAreaBorder.setColor(new Color(Color.rgba8888(0.0f, 0.0f, 0.0f, 0.9f)));
		chatAreaBorder.add(chatAreaPane).expand().fill().pad(5);
		
		Table friendAreaBorder = new Table();
		friendAreaBorder.setTransform(true);
		friendAreaBorder.setBackground(skin.getDrawable("background"));
		friendAreaBorder.setColor(new Color(Color.rgba8888(0.0f, 0.0f, 0.0f, 0.9f)));
		friendAreaBorder.add(friendAreaPane).expand().fill().pad(5);
		
		bottomArea = new Table();
		bottomArea.setTransform(true);
		bottomArea.add(chatMessage).expandX().fillX().padLeft(5).width(everything.getScreenSizeX() * 0.75f).left();
		bottomArea.add(sendButton).padLeft(5);
		bottomArea.add().expandX();
		bottomArea.add(timeLabel).padLeft(20).padRight(20);
		
		chatAndFriendArea = new Table();
		chatAndFriendArea.setTransform(true);
		chatAndFriendArea.add(chatAreaBorder).expandY().fillY().width(everything.getScreenSizeX() * 0.75f);
		chatAndFriendArea.add(friendAreaBorder).expandY().fillY().padLeft(10).width(everything.getScreenSizeX() * 0.25f);

		playButton.addListener(new ChangeListener()
		{
			public void changed(ChangeEvent event, Actor actor)
			{
				joinWaitlist();
			}
		});
		
		backButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				backToMain();
			}
		});
		
		settingsButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				goToSettings();
			}
		});
		
		sendButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				sendMessage();
			}
		});
		
		profilePic.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				rotateProfilePic();
				profilePic.setDrawable(new Image(EverythingHolder.getObjectTexture("profilepic" + everything.getPrivatePlayerInfo().profilePic)).getDrawable());
			}
		});
		
		chatAreaPane.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				chatAreaPane.setScrollY(chatAreaPane.getMaxY());
			}
		});
		
		table = new Table();
		table.setTransform(true);
		
		table.setFillParent(true);
		stage.addActor(table);
		table.setBackground(skin.getDrawable("background"));
		table.add(topArea).expandX().fillX();
		table.row().padTop(10);
		table.add(chatAndFriendArea).expand().fill();
		table.row().padTop(10);
		table.add(bottomArea).expandX().fillX();
	}
	
	public void sendMessage()
	{
		String msg = chatMessage.getText().trim();
		
		if (msg.equals(""))
			return;
		
		LobbyChatMessage message = new LobbyChatMessage();
		message.message = msg;
		
		client.sendTCP(message);
		
		System.out.println("Sent message: " + msg);
		
		chatMessage.setText("");
	}
	
	public void refreshSize()
	{
		table.setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setScale(everything.getScreenScale());
	}
	
	public void goToSettings()
	{
		game.settingsScreen = new SettingsScreen(game, this);
		game.setScreen(game.settingsScreen);
	}
	
	public void rotateProfilePic()
	{
		if (everything.getPrivatePlayerInfo().playerName.equals("Guest"))
			return;
		
		ClientToServerMessage msg = new ClientToServerMessage();
		msg.msg = 4;
		client.sendTCP(msg);
		
		everything.getPrivatePlayerInfo().profilePic = (everything.getPrivatePlayerInfo().profilePic + 1) % 6;
		
//		profilePic.(new Image(EverythingHolder.getObjectTexture("profilePic" + everything.getPrivatePlayerInfo().profilePic)));
	}
	
	private String currentTime()
	{
		calendar = Calendar.getInstance();
		return calendar.get(Calendar.HOUR) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "") + calendar.get(Calendar.MINUTE);
	}
	
	public void backToMain()
	{
		client.close();
		game.setScreen(game.mainMenuScreen);
	}
	
	public void joinWaitlist()
	{
		ClientToServerMessage msg = new ClientToServerMessage();
		msg.msg = 1;
		client.sendTCP(msg);
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
		timeLabel.setText(currentTime());
		
		if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
		{
			if (stage.getKeyboardFocus() == chatMessage)
				sendMessage();
		}
		
		if (lastPaneY != chatAreaPane.getMaxY())
		{
			chatAreaPane.setScrollY(chatAreaPane.getMaxY());
			lastPaneY = chatAreaPane.getMaxY();
		}
	}

	@Override
	public void resize(int width, int height)
	{
		stage.setViewport(width, height, false);
		chatAndFriendArea.getCells().get(0).width(everything.getScreenSizeX() * 0.75f - 5).pad(5).expand().fill();
		chatAndFriendArea.getCells().get(1).width(everything.getScreenSizeX() * 0.25f - 5).pad(5).expand().fill();
		
		bottomArea.getCells().get(0).expandX().fillX().padLeft(5).width(everything.getScreenSizeX() * 0.75f).left();
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(stage);
		client.addListener(threadedListener);
		userWinLoss.setText("W " + everything.getPrivatePlayerInfo().wins + " / " + everything.getPrivatePlayerInfo().losses + " L");
	}

	@Override
	public void hide()
	{
		client.removeListener(threadedListener);
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume()
	{
		System.out.println("Lobby resuming");
		client.addListener(threadedListener);
	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

}
