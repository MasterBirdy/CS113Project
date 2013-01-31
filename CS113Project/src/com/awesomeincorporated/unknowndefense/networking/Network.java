package com.awesomeincorporated.unknowndefense.networking;

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
        kryo.register(UnitQueue.class);
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
		byte message;
	}
	
	static public class Command
	{
		byte type;
		int target;
	}
	
	static public class ClientMessage
	{
		int message;
	}
	
	
	// From server
	static public class LoginStatus
	{
		byte status;
	}
	
	static public class UnitQueue
	{
		LinkedList<Byte>[] pools = new LinkedList[2];
		int turn;
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
		byte message;
	}
	
	static public class AccountInfo
	{
		byte field;
		String parameter;
	}
}