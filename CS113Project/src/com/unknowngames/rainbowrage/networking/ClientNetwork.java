package com.unknowngames.rainbowrage.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.networking.Network.CommandIn;
import com.unknowngames.rainbowrage.networking.Network.Login;
import com.unknowngames.rainbowrage.networking.Network.LoginStatus;
import com.unknowngames.rainbowrage.networking.Network.ServerMessage;
import com.unknowngames.rainbowrage.networking.Network.StartGameInfo;
import com.unknowngames.rainbowrage.networking.Network.StartGameRoom;
import com.unknowngames.rainbowrage.networking.Network.UserMessage;
import com.unknowngames.rainbowrage.player.Player;
import com.unknowngames.rainbowrage.player.PrivatePlayerInfo;
import com.unknowngames.rainbowrage.screens.LoginScreen;

public class ClientNetwork extends BaseClass
{
//	Client client;
	boolean connected = false, loggedIn = false, response = false;
	int failCode = 0;
	LoginScreen loginScreen;
	
	public ClientNetwork(final LoginScreen loginScreen)
	{
		this.loginScreen = loginScreen;
		/*client = new Client();
		client.start();
		Network.register(client);
				
		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener()*/
		everything.setClient(new Client());
		everything.getClient().start();
		Network.register(everything.getClient());
				
		// ThreadedListener runs the listener methods on a different thread.
		everything.getClient().addListener(new ThreadedListener(new Listener()
		{
			public void connected(Connection connection)
			{
			}

			public void received(Connection connection, Object object)
			{
				if (object instanceof LoginStatus)
				{
					LoginStatus status = (LoginStatus) object;
					if (status.status >= 0)
					{
						//everything.setTeam((byte)status.status);
						connected = true;
						loggedIn = true;

						response = true;
					}
					else //if (status.status == -1)
					{
						failCode = status.status;

						response = true;
						//failCode = -1;
					}
				}
				
				else if (object instanceof ServerMessage)
				{
					failCode = ((ServerMessage) object).message;
					response = true;
					
					if (failCode < 0)
						System.out.println("Some error");
//					else
//						loginScreen.joinRoom();
				}
				else if (object instanceof StartGameRoom)
				{
					StartGameRoom info = (StartGameRoom)object;
					everything.setTeam((byte)(info.team));
					Player[] ps = new Player[info.publicPlayerInfos.length];
					for (int i = 0; i < info.publicPlayerInfos.length; i++)
					{
						ps[i] = new Player(info.publicPlayerInfos[i]);
					}
					/*Player[] ps = new Player[info.usernames.length];
					for (int i = 0; i < info.usernames.length; i++)
					{
						ps[i] = new Player(info.usernames[i]);
						//everything.getPlayer(i) = new Player(info.usernames[i]);
					}*/
					everything.setPlayers(ps);
					loginScreen.joinRoom();
				}
				else if (object instanceof StartGameInfo)
				{
					if (((StartGameInfo)object).team > 0)
						loginScreen.joinRoom();
				}
				else if (object instanceof PrivatePlayerInfo)
				{
					everything.setPrivatePlayerInfo((PrivatePlayerInfo)object);
				}
				loginScreen.check();
			}

			public void disconnected(Connection connection)
			{
				connected = false;
				loggedIn = false;
			}
		}));
	}
	
	public boolean response()
	{
		return response;
	}
	
	public void connect()
	{
		response = false;
		
		if (connected)
			return;
		
		try
		{
			everything.getClient().connect(5000, EverythingHolder.serverIP(), Network.port);
			connected = true;
		}
		catch (IOException ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	
	public void login(String name, String password)
	{
		response = false;
		
		if (!connected)
			return;
		
		Login login = new Login();
		login.name = name;
		login.password = password;
		everything.getClient().sendTCP(login);
	}
	
	public boolean connected()
	{
		return connected;
	}
	
	public boolean loggedIn()
	{
		return loggedIn;
	}
	
	public int failCode()
	{
		return failCode;
	}
	
	public void disconnect()
	{
		everything.getClient().close();
	}
}
