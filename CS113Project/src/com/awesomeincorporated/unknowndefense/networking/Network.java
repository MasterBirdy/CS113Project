package com.awesomeincorporated.unknowndefense.networking;

import java.util.Comparator;
import java.util.LinkedList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
//import com.test.gdxkyro.Unit;

public class Network 
{
	static public final int port = 54555;
	
	// This registers objects that are going to be sent over the network.
	static public void register(EndPoint endPoint)
	{
		Kryo kryo = endPoint.getKryo();
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
	
	static public class ClientMessage
	{
		int message;
	}
	
	
	// From server
	static public class LoginStatus
	{
		public byte status;
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
		byte status;
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
		byte field;
		String parameter;
	}
}