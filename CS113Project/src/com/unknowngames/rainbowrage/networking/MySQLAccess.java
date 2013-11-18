package com.unknowngames.rainbowrage.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;
import java.security.*;

import com.unknowngames.rainbowrage.player.PlayerInfo;
import com.unknowngames.rainbowrage.player.PrivatePlayerInfo;
import com.unknowngames.rainbowrage.player.PublicPlayerInfo;


public class MySQLAccess
{
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null, loggedInStatement = null;
	private ResultSet resultSet = null;
	private String accountTable = "test.Accounts",
				   playersTable = "test.Players",
				   playerSkinsTable = "test.playerSkins",
				   playerHeroesTable = "test.playerHeroes",
				   friendsTable = "test.Friends",
				   skinstable = "test.Skins",
				   heroesTable = "test.Heroes",
				   userTable = "joomla.phbdt_users",
				   dbLocation = "jdbc:mysql://localhost:3306";
	private String adminName, adminPass;
	
	public MySQLAccess()
	{
		Scanner scanner = new Scanner(System.in);
		do
		{
		System.out.print("Username: ");
		adminName = scanner.nextLine();
		System.out.print("Password: ");
		adminPass = scanner.nextLine();
		} while (!connect());
		
		close();
	}
	
	public MySQLAccess(String adminName, String adminPassword)
	{
		this.adminName = adminName;
		this.adminPass = adminPassword;
		connect();
		close();
	}
	
	public boolean connect()
	{
		try
		{
			if (connect != null && connect.isValid(5))
				return true;
			System.out.println("Start connect");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Loaded driver");
			connect = DriverManager.getConnection(dbLocation, adminName, adminPass);
			System.out.println("Connected to db");
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error: " + e.getMessage());
			return false;
		}
		catch (Exception e)
		{
			System.out.println("Error durring connect");
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}
	
	public void rotateProfilePic(String username)
	{
		String sqlUpdate = "UPDATE " + playersTable +
						   " SET profilePic = (profilePic + 1) % 6" +
						   " WHERE playerID = ?";
		executeStatement(username, sqlUpdate);
	}
	
	// Adds win to user profile
	public void addWin(String username)
	{
		System.out.println("Attempting to add win to: " + username);
		String sqlUpdate = "UPDATE " + playersTable +
						   " SET " + "wins = wins + 1" +
						   " WHERE " + "playerID = ?";
		executeStatement(username, sqlUpdate);
//		if (username == null)
//			return;
//		
//		try
//		{
//			if (!connect())
//				return;
//			
//			int userID = getUserID(username);
//			
//			if (userID == 0)
//				return;
//			
//			preparedStatement = 
//				connect.prepareStatement("UPDATE " + playersTable +
//										 " SET " + playersTable + ".wins = " + playersTable + ".wins + 1" +
//										 " WHERE " + playersTable + ".playerID = ?");
//			preparedStatement.setInt(1, userID);
//			
//			preparedStatement.execute();
//		}
//		catch (SQLException e)
//		{
//			System.out.println("Error durring addWin");
//			e.printStackTrace();
//		}
	}
	
	// Adds loss to user profile
	public void addLoss(String username)
	{
		System.out.println("Attempting to add loss to: " + username);
		String sqlUpdate = "UPDATE " + playersTable +
						   " SET losses = losses + 1" +
						   " WHERE playerID = ?";
		executeStatement(username, sqlUpdate);
		
		/*if (username == null)
			return;
		
		try
		{
			if (!connect())
				return;
			
			int userID = getUserID(username);
			
			if (userID == 0)
				return;
			
			preparedStatement = 
				connect.prepareStatement("UPDATE " + playersTable +
										 " SET losses = losses + 1" +
										 " WHERE playerID = ?");
			preparedStatement.setInt(1, userID);
			
			preparedStatement.execute();
		}
		catch (SQLException e)
		{
			System.out.println("Error durring addLoss");
			e.printStackTrace();
		}*/
	}
	
	public void executeStatement(String username, String s)
	{
		if (username == null)
			return;
		
		try
		{
			if (!connect())
				return;
			
			int userID = getUserID(username);
			
			if (userID == 0)
				return;
			
			preparedStatement = connect.prepareStatement(s);
			preparedStatement.setInt(1, userID);
			preparedStatement.execute();
		}
		catch (SQLException e)
		{
			System.out.println("Error durring createPrivatePlayerInfo");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				preparedStatement.close();
			}
			catch (SQLException e)
			{
				System.out.println("PreparedStatement close fail");
				e.printStackTrace();
			}
		}
	}
	
	public PrivatePlayerInfo createPrivatePlayerInfo(String username)
	{
		System.out.println("Attempting to create new player: " + username);
		
		if (username == null)
			return null;
		
		PrivatePlayerInfo returnInfo = new PrivatePlayerInfo();
		
		try
		{
			if (!connect())
				return null;
			
			int userID = getUserID(username);
			
			if (userID == 0)
				return null;
			
			preparedStatement = 
				connect.prepareStatement("INSERT INTO " + playersTable +
										 " (playerID, wins, losses, earnedPoints, paidPoints, exp, profilePic, elo, timePlayed)" +
										 " VALUES (?, 0, 0, 0, 0, 0, 0, 1800, 0)");
			
			/*preparedStatement = 
				connect.prepareStatement("INSERT INTO " + playersTable +
										 " (" + playersTable + ".playerID, " + playersTable + ".wins, " + playersTable + ".losses, " + playersTable + ".earnedPoints, " + 
										 playersTable + ".paidPoints, " + playersTable + ".exp, " + playersTable + ".profilePic, " + playersTable + ".elo, " + playersTable + ".timePlayed)" +
										 " VALUES (" + userID + "0, 0, 0, 0, 0, 0, 1800, 0)");*/
			preparedStatement.setInt(1, userID);
			
			preparedStatement.execute();
			
			System.out.println("Second setop of adding new player! " + username);
			returnInfo.playerName = username;
			returnInfo.wins = 0;
			returnInfo.losses = 0;
			returnInfo.earnedPoints = 0;
			returnInfo.paidPoints = 0;
			returnInfo.exp = 0;
			returnInfo.profilePic = 0;
			returnInfo.timePlayed = 0;
		}
		catch (SQLException e)
		{
			System.out.println("Error durring createPrivatePlayerInfo");
			e.printStackTrace();
			return null;
		}
		
		System.out.println("Successfully added new player! " + returnInfo.playerName);
		return returnInfo;
	}
	
	public PublicPlayerInfo getPublicPlayerInfo(String username)
	{
		if (username == null)
			return null;
		
		PublicPlayerInfo returnInfo = new PublicPlayerInfo();
		
		try
		{
			if (!connect())
				return null;
			
			returnInfo.playerID = getUserID(username);
			loadPlayerInfo(returnInfo);
//			int selfUserID = getUserID(selfUsername);
//			loadPlayerInfo(userID, returnInfo);
//			loadPublicPlayerInfo(userID, selfUserID, returnInfo);
		}
		catch (SQLException e)
		{
			System.out.println("Error durring getPublicPlayerInfo");
			e.printStackTrace();
		}
		
		return returnInfo;
	}
	
	public PrivatePlayerInfo getPrivatePlayerInfo(String username)
	{
		if (username == null)
			return null;
		
		PrivatePlayerInfo returnInfo = new PrivatePlayerInfo();
		
		try
		{
			if (!connect())
				return null;
			
//			int userID = getUserID(username);
			returnInfo.playerName = username;
			returnInfo.playerID = getUserID(username);
			
			preparedStatement = 
				connect.prepareStatement("SELECT COUNT(*)" +
										 " FROM " + playersTable +
										 " WHERE " + playersTable + ".playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next())
				return null;
			
			if (resultSet.getInt(1) == 0)
				return createPrivatePlayerInfo(username);
			
			loadPlayerInfo(returnInfo);
//			loadPlayerInfo(userID, returnInfo);
			loadPrivatePlayerInfo(returnInfo);
			loadPrivateSkins(returnInfo);
			loadPrivateHeroes(returnInfo);
			loadPrivateFriends(returnInfo);
			preparedStatement.close();
		}
		catch (SQLException e)
		{
			System.out.println("Error durring getPrivatePlayerInfo");
			e.printStackTrace();
		}
		
		return returnInfo;
	}
	
	public void loadPlayerInfo(PlayerInfo returnInfo)
	{
		if (returnInfo.playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			preparedStatement = 
				connect.prepareStatement("SELECT " + "name, " + playersTable + ".wins, " + playersTable + ".exp, " + playersTable + ".profilePic" +
										 " FROM " + userTable +
										 " RIGHT JOIN " + playersTable +
										 " ON " + playersTable + ".playerID = " + userTable + ".id" +
										 " WHERE " + userTable + ".id = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
			{
				System.out.println("Couldn't find player info: " + returnInfo.playerID);
			}
			
			returnInfo.shownName = resultSet.getString(1);
			returnInfo.wins = resultSet.getInt(2);
			returnInfo.exp = resultSet.getInt(3);
			returnInfo.profilePic = resultSet.getInt(4);
			preparedStatement.close();
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPlayerInfo");
			e.printStackTrace();
		}
	}
	
	/*public void loadPlayerInfo(int playerID, PlayerInfo returnInfo)
	{
		if (playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			
			preparedStatement = 
				connect.prepareStatement("SELECT " + userTable + ".username, " + playersTable + ".wins, " + playersTable + ".exp, " + playersTable + ".profilePic" +
										 " FROM " + userTable +
										 " RIGHT JOIN " + playersTable +
										 " ON " + playersTable + ".playerID = " + userTable + ".id" +
										 " WHERE " + userTable + ".id = ?");
			preparedStatement.setInt(1, playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
			{
				System.out.println("Couldn't find player info: " + playerID);
			}
			
			returnInfo.username = resultSet.getString(1);
			returnInfo.wins = resultSet.getInt(2);
			returnInfo.exp = resultSet.getInt(3);
			returnInfo.profilePic = resultSet.getInt(4);
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPlayerInfo");
			e.printStackTrace();
		}
	}*/
	
	/*public void loadPublicPlayerInfo(int playerID, int selfPlayerID, PublicPlayerInfo returnInfo)
	{
		if (playerID == 0)
			return;
		
		try
		{			
			preparedStatement = 
				connect.prepareStatement("SELECT COUNT(*)" +
										 "FROM " + friendsTable +
										 "WHERE ((" + friendsTable + ".player1ID = " + playerID + " AND " + friendsTable + ".player2ID = " + selfPlayerID + ") OR (" +
										 friendsTable + ".player1ID = " + selfPlayerID + " AND " + friendsTable + ".player2ID = " + playerID + ")) AND " + 
										 friendsTable + ".accepted = 1");
			preparedStatement.setInt(1, playerID);
			resultSet = preparedStatement.executeQuery();
			
			returnInfo.friend = resultSet.getInt(1) == 0 ? false : true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}*/
	
	public void loadPrivatePlayerInfo(PrivatePlayerInfo returnInfo)
	{
		if (returnInfo.playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			
			preparedStatement = 
				connect.prepareStatement("SELECT earnedPoints, paidPoints, losses, timePlayed" +
										 " FROM " + playersTable +
										 " WHERE playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
			{
				System.out.println("Couldn't find private player info: " + returnInfo.playerID);
				return;
			}
			
			returnInfo.earnedPoints = resultSet.getInt(1);
			returnInfo.paidPoints = resultSet.getInt(2);
			returnInfo.losses = resultSet.getInt(3);
			returnInfo.timePlayed = resultSet.getInt(4);
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPrivatePlayerInfo");
			e.printStackTrace();
		}
	}
	
	private void loadPrivateSkins(PrivatePlayerInfo returnInfo)
	{
		if (returnInfo.playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			
			preparedStatement = 
				connect.prepareStatement("SELECT COUNT(*)" +
										 " FROM " + playerSkinsTable +
										 " WHERE " + playerSkinsTable + ".playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
				return;
			
			int count = resultSet.getInt(1);
			
			preparedStatement = 
				connect.prepareStatement("SELECT " + playerSkinsTable + ".skinID" +
										 " FROM " + playerSkinsTable +
										 " WHERE " + playerSkinsTable + ".playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (count <= 0)
				return;
			
			returnInfo.skins = new int[count];
			
			for (int i = 0; i < count; i++)
			{
				resultSet.next();
				returnInfo.skins[i] = resultSet.getInt(1);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPrivateSkins");
			e.printStackTrace();
		}
	}
	
	private void loadPrivateHeroes(PrivatePlayerInfo returnInfo)
	{
		if (returnInfo.playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			
			preparedStatement = 
				connect.prepareStatement("SELECT COUNT(*)" +
										 " FROM " + playerHeroesTable +
										 " WHERE " + playerHeroesTable + ".playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
				return;
			
			int count = resultSet.getInt(1);
			
			preparedStatement = 
				connect.prepareStatement("SELECT " + playerHeroesTable + ".heroID" +
										 " FROM " + playerHeroesTable +
										 " WHERE " + playerHeroesTable + ".playerID = ?");
			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (count <= 0)
				return;
			
			returnInfo.heroes = new int[count];
			
			for (int i = 0; i < count; i++)
			{
				resultSet.next();
				returnInfo.heroes[i] = resultSet.getInt(1);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPrivateHeroes");
			e.printStackTrace();
		}
	}
	
	private void loadPrivateFriends(PrivatePlayerInfo returnInfo)
	{
		if (returnInfo.playerID == 0)
			return;
		
		try
		{
			if (!connect())
				return;
			
			preparedStatement = 
				connect.prepareStatement("SELECT COUNT(*)" +
										 " FROM " + friendsTable +
										 " WHERE (player1ID = " + returnInfo.playerID + " OR player2ID = " + returnInfo.playerID + ") AND" + 
										 " accepted = 1");
//			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next())
				return;
			
			int count = resultSet.getInt(1);
			
			preparedStatement = 
				connect.prepareStatement("SELECT player2ID" +
										 " FROM " + friendsTable +
										 " WHERE player1ID = " + returnInfo.playerID + " AND accepted = 1" +
										 " UNION " + 
										 " SELECT player1ID" +
										 " FROM " + friendsTable +
										 " WHERE player2ID = " + returnInfo.playerID + " AND accepted = 1");
//			preparedStatement.setInt(1, returnInfo.playerID);
			resultSet = preparedStatement.executeQuery();
			
			if (count <= 0)
				return;
			
			returnInfo.friends = new String[count];
			
			for (int i = 0; i < count; i++)
			{
				resultSet.next();
				returnInfo.friends[i] = resultSet.getString(1);
			}
		}
		catch (SQLException e)
		{
			System.out.println("Error durring loadPrivateFriends");
			e.printStackTrace();
		}
	}
	
	// Returns userID or 0 if it doesn't exist
	private int getUserID(String username) throws SQLException
	{
		preparedStatement = connect.prepareStatement("SELECT " + userTable + ".id FROM " + userTable + " WHERE " + userTable + ".username = ?");
		preparedStatement.setString(1, username);
		resultSet = preparedStatement.executeQuery();
		if (resultSet.next())
			return resultSet.getInt(1);
		return 0;
	}	
	
	public boolean login(String username, String userPass)
	{
		if (username.equals("Guest"))
			return true;
		
		try
		{
			if (!connect())
				return false;
			
//			Class.forName("com.mysql.jdxb.Driver");
//			connect = DriverManager.getConnection(dbLocation, adminName, adminPass);
			preparedStatement = connect.prepareStatement("SELECT password FROM " + userTable + 
														 " WHERE username = ?");
			
			/*String[] passSalt = {null, null};
			String passAndSalt;*/
			String hash;
			
//			boolean loggedIn = false;
			
//			while (!loggedIn)
//			{
				try
				{
					preparedStatement.setString(1, username);
					resultSet = preparedStatement.executeQuery();
					
					if (resultSet.next())
					{
//						hash = resultSet.getString("password");
						if (checkPassword(userPass, resultSet.getString("password")))
						{
							System.out.println("Logging in!");
							close();
							return true;
						}
						else
						{
							System.out.println("Incorrect login info");
							close();
							return false;
						}
						/*passAndSalt = resultSet.getString("password");
						if (passAndSalt != null)
						{
							passSalt = passAndSalt.split(":");
						}
						else
						{
							close();
							return false;
						}*/
					}
					else
					{
//						passAndSalt = null;
						close();
						return false;
					}
					
					/*if (passAndSalt != null && md5(userPass + passSalt[1]).equals(passSalt[0]))
					{
						System.out.println("Logging in!");
//						loggedIn = true;
						close();
						return true;
					}
					else
					{
//						System.out.println("Incorrect username and/or password." + username + " " + password);
						close();
						return false;
					}*/
				}
				catch(Exception e)
				{
					System.out.println("Error durring inner login");
					close();
					e.printStackTrace();
				}
//			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			System.out.println("Error durring outer login");
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Error durring outer outer login");
			close();
			e.printStackTrace();
		}
		close();
		return false;
	}
	
	public boolean checkPassword(String pass, String hash)
	{
		if (pass == null || hash == null)
			return false;
		
		System.out.println(pass + " " + hash);
		
		return BCrypt.checkpw(pass, hash);
	}
	
	public void readDataBase() throws Exception
	{
		try
		{
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306", adminName, adminPass);

			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from " + accountTable);
			writeResultSet(resultSet);

			preparedStatement = connect
					.prepareStatement("SELECT password FROM " + userTable
							+ " WHERE username = ?");
			loggedInStatement = connect.prepareStatement("SELECT * FROM " + accountTable + 
			 											 " WHERE username = ?");
//			preparedStatement.setString(1, "ThatGameGuy");
//			resultSet = preparedStatement.executeQuery();
//			resultSet.next();

//			String[] passSalt = resultSet.getString("password").split(":");
//			String myPass = "rainbowsix";
//			System.out.println(passSalt[0]);
//			System.out.println(md5(myPass + passSalt[1]));
			
			String[] passSalt = {null, null};
			String myPass;

			boolean loggedIn = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String username = "", password, passAndSalt;

			while (!loggedIn)
			{
				try
				{
					System.out.print("Username: ");
					username = br.readLine();
					System.out.print("Password: ");
					password = br.readLine();

					preparedStatement.setString(1, username);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next())
					{
						passAndSalt = resultSet.getString("password");
						if (passAndSalt != null)
						{
							passSalt = passAndSalt.split(":");
						}
					}
					else
					{
						passAndSalt = null;
					}
					if (passAndSalt != null
							&& md5(password + passSalt[1]).equals(passSalt[0]))
					{
						System.out.println("Logging in!");
						loggedIn = true;
					}
					else
					{
						System.out.println("Incorrect username and/or password." + username + " " + password);
					}
				}
				catch (IOException e)
				{
					System.out.println("Error with input");
				}

			}
			if (loggedIn && username != null)
			{
				System.out.println("=====================" + username);
				loggedInStatement.setString(1, username);
				
				resultSet = loggedInStatement.executeQuery();
				writeResultSet(resultSet);
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			close();
		}

	}

	private String md5(String input)
	{
		String md5 = null;
		if (input == null)
			return null;

		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("Error durring md5");
			e.printStackTrace();
		}
		return md5;
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException
	{
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
		{
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException
	{
		// ResultSet is initially before the first data set
		System.out.println("Printing from result set.");
		while (resultSet.next())
		{
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("username");
			String wins = resultSet.getString("wins");
			// String summary = resultSet.getString("summary");
			// Date date = resultSet.getDate("datum");
			// String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Wins: " + wins);
		}
	}

	// You need to close the resultSet
	private void close()
	{
		try
		{
			if (resultSet != null)
			{
				resultSet.close();
			}

			if (statement != null)
			{
				statement.close();
			}

			if (connect != null)
			{
//				try
//				{
//					connect.commit();
//				}
//				catch (SQLException e)
//				{
//					System.out.println("Error while closing SQL: " + e);
//				}
//				finally
//				{
					connect.close();
//				}
			}
		}
		catch (Exception e)
		{

		}
	}

}