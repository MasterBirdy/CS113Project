package com.unknowngames.rainbowrage.networking;

import com.esotericsoftware.kryonet.Server;
import com.unknowngames.rainbowrage.networking.Network.AddUnit;
import com.unknowngames.rainbowrage.networking.Network.Command;
import com.unknowngames.rainbowrage.networking.Network.CommandIn;
import com.unknowngames.rainbowrage.networking.Network.HeroSelectStart;
import com.unknowngames.rainbowrage.networking.Network.HeroSelectStatus;
//import com.unknowngames.rainbowrage.networking.Network.LoginStatus;
import com.unknowngames.rainbowrage.networking.Network.ServerMessage;
import com.unknowngames.rainbowrage.networking.Network.StartGameInfo;
import com.unknowngames.rainbowrage.networking.Network.StartGameRoom;
import com.unknowngames.rainbowrage.networking.Network.UserMessage;
import com.unknowngames.rainbowrage.networking.RainbowRageServer.UserConnection;

public class Game 
{
	Server server;
	UserConnection[] players = new UserConnection[2];
	int gameID, gameType;
	int currentTurn = 0;
	float stepTime = 0.02f;
	int turnBuffer = 21, turnBlock = 20;
	int highestTurn1 = 19, highestTurn2 = 19; //, highestCount = 0;
	boolean rdy1 = false, rdy2 = false, inGame = false, started = false;
	
	HeroSelectStart[] heroSelectStart = new HeroSelectStart[2];
	
	
	public Game(UserConnection p1, UserConnection p2, Server s, int ID)//Server s, int ID)
	{
		players[0] = p1;
		players[1] = p2;
		players[0].user.room = ID;
		players[1].user.room = ID;
		server = s;
		gameID = ID;
		
		
    	/*LoginStatus status = new LoginStatus();
    	status.status = 1;
    	players[0].sendTCP(status);
    	status.status = 2;
    	players[1].sendTCP(status);*/
		
		/*// Setup gameInfo message
		StartGameInfo gameInfo = new StartGameInfo();
		gameInfo.names = new String[2];
		gameInfo.names[0] = p1.user.name;
		gameInfo.names[1] = p2.user.name;
		
		gameInfo.heroes = new String[2];
		gameInfo.heroes[0] = "mrwizard";
		gameInfo.heroes[1] = "swordface";
		
		gameInfo.colors = new String[2];
		gameInfo.colors[0] = "red";
		gameInfo.colors[1] = "green";
		
		gameInfo.map = "";
		
		for (int i = 0; i < 2; i++)
		{
			gameInfo.team = (byte) (i + 1);
	    	players[i].sendTCP(gameInfo);
		}*/
		
		// Setup hero select
		StartGameRoom gameRoom = new StartGameRoom();
		gameRoom.usernames = new String[players.length];
		for (int i = 0; i < players.length; i++)
			gameRoom.usernames[i] = players[i].user.name;
		
		for (int i = 0; i < players.length; i++)
		{
			gameRoom.team = (byte)i;
			players[i].sendTCP(gameRoom);
		}
		
		/*gameInfo.team = 1;
    	players[0].sendTCP(gameInfo);
    	gameInfo.team = 2;
    	players[1].sendTCP(gameInfo);*/
    	
    	RainbowRageServer.sentData += 32;
    	
    	/*ServerMessage msg = new ServerMessage();
    	msg.message = 1;
    	players[0].sendTCP(msg);
    	players[1].sendTCP(msg);
    	
    	RainbowRageServer.sentData += 32;*/
	}
	
	public int ID()
	{
		return gameID;
	}
	
	public void userMessage(UserMessage msg)
	{
		int team = (msg.team == 0 ? 1 : 0);
		
		server.sendToTCP(players[team].getID(), msg);
	}
	
	public void heroSelectStatus(HeroSelectStatus heroSelect)
	{
		server.sendToTCP(players[0].getID(), heroSelect);
		server.sendToTCP(players[1].getID(), heroSelect);
		
		if (heroSelect.ready)
		{
			if (heroSelect.team == 0)
				rdy1 = true;
			else
				rdy2 = true;
		}
		
		if (rdy1 && rdy2)
		{
			moveToGame();
//			startGame();
		}
		/*if (heroSelect.ready)
		{
			if (heroSelect.team == 1)
			{
				System.out.println("Player 1 rdy");
				rdy1 = true;
			}
			else
			{
				System.out.println("Player 2 rdy");
				rdy2 = true;
			}
			
			if (rdy1 && rdy2)
			{
				startGame();
			}
		}*/
	}
	
	private void moveToGame()
	{
		System.out.println("Moving to game");
		rdy1 = false;
		rdy2 = false;
		inGame = true;
	}
	
	private void startGame()
	{
		System.out.println("Starting");
		started = true;
//		highestCount = 0;
		
		ServerMessage msg1 = new ServerMessage();
    	msg1.message = 1;
    	players[0].sendTCP(msg1);
    	players[1].sendTCP(msg1);
		
		CommandIn msg = new CommandIn();
		msg.command = -2;
		msg.turn = 0 + turnBlock - 1;
		System.out.println("Sending turn update " + msg.turn);
		server.sendToTCP(players[0].getID(), msg);
		server.sendToTCP(players[1].getID(), msg);
	}
	
	public void messageCommand(Command cmd)
	{
		System.out.println("(Game)Recevied command " + cmd.type);
		if (inGame && !started && !(rdy1 && rdy2))
		{
			System.out.println(inGame + " : " + rdy1 + " : " + rdy2 + " : " + cmd.team);
			if (cmd.team == 0)
				rdy1 = true;
			else
				rdy2 = true;
			System.out.println(":: " + inGame + " : " + rdy1 + " : " + rdy2 + " : " + cmd.team);
			if (rdy1 && rdy2)
			{
				startGame();
			}
		}
		else if (cmd.type == -2)
		{
			if (cmd.team == 0)
			{
				System.out.println("Player 1 rdy");
				rdy1 = true;
			}
			else
			{
				System.out.println("Player 2 rdy");
				rdy2 = true;
			}
			
			if (rdy1 && rdy2) //++highestCount == players.length)
			{
//				highestCount = 0;
				CommandIn msg = new CommandIn();
				msg.command = -2;
				msg.turn = cmd.turn + turnBlock - 1;
				System.out.println("Sending turn update " + msg.turn);
				server.sendToTCP(players[0].getID(), msg);
	    		server.sendToTCP(players[1].getID(), msg);
	    		rdy1 = false;
	    		rdy2 = false;
			}
		}
		else if (cmd.type >= 0)
		{
			CommandIn cmdIn = new CommandIn();
    		cmdIn.command = cmd.type;
    		cmdIn.team = cmd.team;
    		cmdIn.turn = cmd.turn + turnBuffer;
    		server.sendToTCP(players[0].getID(), cmdIn);
    		server.sendToTCP(players[1].getID(), cmdIn);
    		
    		RainbowRageServer.sentData += 32;
		}
//    	if (cmd.type >= 0 && cmd.type < 6)
//    	{
////    		AddUnit add = new AddUnit();
////    		add.team = cmd.team;
////    		add.unit = cmd.type;
////    		add.turn = cmd.turn + turnBuffer;
////    		add.turn = currentTurn;
//    		CommandIn cmdIn = new CommandIn();
//    		cmdIn.command = cmd.type;
//    		cmdIn.team = cmd.team;
//    		cmdIn.turn = cmd.turn + turnBuffer;
//    		System.out.println("(Game)Sending unit " + cmd.type + " from player " + cmd.team);
//    		
//    		server.sendToTCP(players[0].getID(), cmdIn);
//    		server.sendToTCP(players[1].getID(), cmdIn);
//    		
//    		RainbowRageServer.sentData += 32;
////        		server.sendToAllTCP(add);
//    	}
//    	else if (cmd.type > 6 && cmd.type < 10) // 7-9 is retreat, guard, and attack.
//    	{
//    		System.out.println("(Game)Sending command out (Hero)");
//    		CommandIn cmdIn = new CommandIn();
//    		cmdIn.command = cmd.type;
//    		cmdIn.team = cmd.team;
//    		cmdIn.turn = cmd.turn + turnBuffer;
//    		server.sendToTCP(players[0].getID(), cmdIn);
//    		server.sendToTCP(players[1].getID(), cmdIn);
//    		
//    		RainbowRageServer.sentData += 32;
////        		server.sendToAllTCP(cmdIn);
//    	}
//    	else if (cmd.type == 10)
//    	{
//    		System.out.println("(Game)Sending active skill");
//    		CommandIn cmdIn = new CommandIn();
//    		cmdIn.command = cmd.type;
//    		cmdIn.team = cmd.team;
//    		cmdIn.turn = cmd.turn + turnBuffer;
//    		server.sendToTCP(players[0].getID(), cmdIn);
//    		server.sendToTCP(players[1].getID(), cmdIn);
//    		
//    		RainbowRageServer.sentData += 32;
//    	}
    	
//    	else if (cmd.type > 9 && cmd.type < 13) // 10-12 is upgrade towers 1, 2, and 3.
//    	{
//    		System.out.println("(Game)Sending command out (tower)");
//    		CommandIn cmdIn = new CommandIn();
//    		cmdIn.command = cmd.type;
//    		cmdIn.team = cmd.team;
//    		cmdIn.turn = cmd.turn + turnBuffer;
//    		server.sendToTCP(players[0].getID(), cmdIn);
//    		server.sendToTCP(players[1].getID(), cmdIn);
//    	}
    }
}
