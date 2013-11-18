package com.unknowngames.rainbowrage.networking;

import java.util.Comparator;
import java.util.LinkedList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.unknowngames.rainbowrage.AllEnums.TeamColor;
//import com.test.gdxkyro.Unit;
import com.unknowngames.rainbowrage.player.PrivatePlayerInfo;
import com.unknowngames.rainbowrage.player.PublicPlayerInfo;

public class Network 
{
	static public final int port = 54555;
	
	// This registers objects that are going to be sent over the network.
	static public void register(EndPoint endPoint)
	{
		Kryo kryo = endPoint.getKryo();
		
		kryo.register(String[].class);
		kryo.register(int[].class);
		kryo.register(PublicPlayerInfo[].class);
		kryo.register(TeamColor.class);
		
		kryo.register(Login.class);
		kryo.register(AccountMessage.class);
		kryo.register(Command.class);
		kryo.register(ClientMessage.class);
        kryo.register(LoginStatus.class);
        kryo.register(CommandIn.class);
        kryo.register(AddUnit.class);
        kryo.register(UpdateUnit.class);
        kryo.register(UpdateHero.class);
        kryo.register(GameStatus.class);
        kryo.register(ServerMessage.class);
        kryo.register(AccountInfo.class);
        kryo.register(UserMessage.class);
        kryo.register(StartGameInfo.class);
        kryo.register(HeroSelectStatus.class);
        kryo.register(StartGameRoom.class);
        kryo.register(PublicPlayerInfo.class);
        kryo.register(PrivatePlayerInfo.class);
        kryo.register(ClientToServerMessage.class);
        kryo.register(FinishedGame.class);
        kryo.register(LobbyChatMessage.class);
	}
	
	// From client
	static public class Login
	{
		public String name;
		public String password;
	}
	
	static public class AccountMessage
	{
		public byte message;
	}
	
	static public class Command
	{
		public byte type;
		public byte team;
		public int 	turn;
		
//		public Command(byte type, byte team)
//		{
//			this.type = type;
//			this.team = team;
//		}
	}
	
	static public class FinishedGame
	{
		public int team;
		public int winner;
	}
	
	static public class ClientMessage
	{
		public int message;
	}
	
	static public class HeroSelectStatus
	{
		public boolean ready;
		public String heroname;
		public TeamColor teamColor;
		public byte team;
	}
	
	static public class HeroSelectStart
	{
		public String[] heroname;
		public TeamColor[] teamColor;
	}
	
	// From server
	static public class LoginStatus
	{
		public byte status;
	}
	
	static public class StartGameInfo
	{
		public String[] names;
		public String[] heroes;
		public String[] colors;
		public byte team;
		public String map;
	}
	
	static public class StartGameRoom
	{
//		public String[] usernames;
		public PublicPlayerInfo[] publicPlayerInfos;
		public byte team;
	}
	
	static public class CommandIn
	{
		public byte command;
		public byte team;
		public int turn;
		
//		public CommandIn(byte command, byte team, int turn)
//		{
//			this.command = command;
//			this.team = team;
//			this.turn = turn;
//		}
	}
	
	static public class AddUnit
	{
//		LinkedList<Byte>[] pools = new LinkedList[2];
		public byte unit;
		public int turn;
		public byte team;
		
//		public AddUnit(byte unit, int turn, byte team)
//		{
//			this.unit = unit;
//			this.turn = turn;
//			this.team = team;
//		}
	}
	
	static public class UpdateUnit
	{
		int id;
		float x, y;
		short health;
		int turn;
	}
	
	static public class UpdateHero
	{
		int id;
		float x, y;
		short health;
		byte stance, skill;
		int turn;
	}
	
	static public class GameStatus
	{
		public byte status;
	}
	
	static public class ServerMessage
	{
		public byte message;
		public int gameID;
		
//		ServerMessage(byte msg)
//		{
//			message = msg;
//		}
	}
	
	static public class AccountInfo
	{
		public PrivatePlayerInfo privatePlayerInfo;
	}
	
	static public class UserMessage
	{
		public String message;
		public byte team;
		public int turn;
	}
	
	static public class ClientToServerMessage
	{
		public int msg;
	}
	
	static public class LobbyChatMessage
	{
		public String message;
	}
	
//	static public class 
}