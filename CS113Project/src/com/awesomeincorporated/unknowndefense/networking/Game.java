package com.awesomeincorporated.unknowndefense.networking;

import com.awesomeincorporated.unknowndefense.networking.Network.AddUnit;
import com.awesomeincorporated.unknowndefense.networking.Network.Command;
import com.awesomeincorporated.unknowndefense.networking.Network.CommandIn;
import com.awesomeincorporated.unknowndefense.networking.Network.LoginStatus;
import com.awesomeincorporated.unknowndefense.networking.Network.ServerMessage;
import com.awesomeincorporated.unknowndefense.networking.UnknownDefenseServer.UserConnection;
import com.esotericsoftware.kryonet.Server;

public class Game 
{
	Server server;
	UserConnection[] players = new UserConnection[2];
	int gameID, gameType;
	int currentTurn = 0;
	float stepTime = 0.02f;
	int turnBuffer = 30;
	
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
    	
    	ServerMessage msg = new ServerMessage();
    	msg.message = 1;
    	players[0].sendTCP(msg);
    	players[1].sendTCP(msg);
	}
	
	public int ID()
	{
		return gameID;
	}
	
	public void messageCommand(Command cmd)
	{
		System.out.println("(Game)Recevied command " + cmd.type);
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
//        		server.sendToAllTCP(cmdIn);
    	}
    	else if (cmd.type > 9 && cmd.type < 13) // 10-12 is upgrade towers 1, 2, and 3.
    	{
    		System.out.println("(Game)Sending command out (tower)");
    		CommandIn cmdIn = new CommandIn();
    		cmdIn.command = cmd.type;
    		cmdIn.team = cmd.team;
    		cmdIn.turn = cmd.turn + turnBuffer;
    		server.sendToTCP(players[0].getID(), cmdIn);
    		server.sendToTCP(players[1].getID(), cmdIn);
    	}
    }
}
