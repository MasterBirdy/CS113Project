package com.unknowngames.rainbowrage.player;

public class PrivatePlayerInfo extends PlayerInfo
{
	public int earnedPoints, paidPoints, losses, timePlayed;
	public int[] skins, heroes, friendsID;
	public String[] friends;
	public String playerName;
	
	public PublicPlayerInfo getPublicPlayerInfo()
	{
		PublicPlayerInfo publicPlayerInfo = new PublicPlayerInfo();
		publicPlayerInfo.exp = exp;
		publicPlayerInfo.profilePic = profilePic;
		publicPlayerInfo.shownName = shownName;
		publicPlayerInfo.wins = wins;
		
		return publicPlayerInfo;
	}
}
