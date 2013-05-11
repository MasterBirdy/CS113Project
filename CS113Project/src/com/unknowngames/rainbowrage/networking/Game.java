package com.unknowngames.rainbowrage.networking;

import com.esotericsoftware.kryonet.Server;
import com.unknowngames.rainbowrage.networking.Network.AddUnit;
import com.unknowngames.rainbowrage.networking.Network.Command;
import com.unknowngames.rainbowrage.networking.Network.CommandIn;
import com.unknowngames.rainbowrage.networking.Network.LoginStatus;
import com.unknowngames.rainbowrage.networking.Network.ServerMessage;
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
	int highestTurn1, highestTurn2, highestCount;
	boolean rdy1 = false, rdy2 = false;
	
	
	public Game(UserConnection p1, UserConnection p2, Server s, int ID)//Server s, int ID)
	{
		players[0] = p1;
		players[1] = p2;
		players[0].user.room = ID;
		players[1].user.room = ID;
		server = s;
		gameID = ID;
		
		
    	LoginStatus status = new LoginStatus();
    	status.status = 1;
    	players[0].sendTCP(status);
    	status.status = 2;
    	players[1].sendTCP(status);
    	
    	RainbowRageServer.sentData += 32;
    	
    	ServerMessage msg = new ServerMessage();
    	msg.message = 1;
    	players[0].sendTCP(msg);
    	players[1].sendTCP(msg);
    	
    	RainbowRageServer.sentData += 32;
	}
	
	public int ID()
	{
		return gameID;
	}
	
	public void userMessage(UserMessage msg)
	{
		int team = (msg.team == 1 ? 1 : 0);
		
		server.sendToTCP(players[team].getID(), msg);
	}
	
	public void messageCommand(Command cmd)
	{
		System.out.println("(Game)Recevied command " + cmd.type);
		if (cmd.type == -2)
		{
			if (cmd.team == 1)
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
				highestCount = 0;
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
    	if (cmd.type >= 0 && cmd.type < 6)
    	{
//    		AddUnit add = new AddUnit();
//    		add.team = cmd.team;
//    		add.unit = cmd.type;
//    		add.turn = cmd.turn + turnBuffer;
//    		add.turn = currentTurn;
    		CommandIn cmdIn = new CommandIn();
    		cmdIn.command = cmd.type;
    		cmdIn.team = cmd.team;
    		cmdIn.turn = cmd.turn + turnBuffer;
    		System.out.println("(Game)Sending unit " + cmd.type + " from player " + cmd.team);
    		
    		server.sendToTCP(players[0].getID(), cmdIn);
    		server.sendToTCP(players[1].getID(), cmdIn);
    		
    		RainbowRageServer.sentData += 32;
//        		server.sendToAllTCP(add);
    	}
    	else if (cmd.type > 6 && cmd.type < 10) // 7-9 is retreat, guard, and attack.
    	{
    		System.out.println("(Game)Sending command out (Hero)");
    		CommandIn cmdIn = new CommandIn();
    		cmdIn.command = cmd.type;
    		cmdIn.team = cmd.team;
    		cmdIn.turn = cmd.turn + turnBuffer;
    		server.sendToTCP(players[0].getID(), cmdIn);
    		server.sendToTCP(players[1].getID(), cmdIn);
    		
    		RainbowRageServer.sentData += 32;
//        		server.sendToAllTCP(cmdIn);
    	}
    	else if (cmd.type == 10)
    	{
    		System.out.println("(Game)Sending active skill");
    		CommandIn cmdIn = new CommandIn();
    		cmdIn.command = cmd.type;
    		cmdIn.team = cmd.team;
    		cmdIn.turn = cmd.turn + turnBuffer;
    		server.sendToTCP(players[0].getID(), cmdIn);
    		server.sendToTCP(players[1].getID(), cmdIn);
    		
    		RainbowRageServer.sentData += 32;
    	}
    	
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
