package com.awesomeincorporated.unknowndefense.networking;

import java.util.LinkedList;

import com.awesomeincorporated.unknowndefense.networking.UnknownDefenseServer.UserConnection;

public class Game 
{
	UserConnection[] players = new UserConnection[2];
	
	public Game(UserConnection p1, UserConnection p2)
	{
		players[0] = p1;
		players[1] = p2;
	}
}
