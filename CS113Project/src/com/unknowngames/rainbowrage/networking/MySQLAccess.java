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

public class MySQLAccess
{
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null, loggedInStatement = null;
	private ResultSet resultSet = null;
	private String accountTable = "test.Accounts",
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
	
	public boolean connect()
	{
		try
		{
			Class.forName("com.mysql.jdxb.Driver");
			connect = DriverManager.getConnection(dbLocation, adminName, adminPass);
		}
		catch (SQLException e)
		{
			System.out.println("SQL Error: " + e.getMessage());
			return false;
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
			return false;
		}

		return true;
	}
	
	public boolean login(String username, String userPass)
	{
		try
		{
			if (!connect())
				return false;
//			Class.forName("com.mysql.jdxb.Driver");
//			connect = DriverManager.getConnection(dbLocation, adminName, adminPass);
			preparedStatement = connect.prepareStatement("SELECT password FROM " + userTable + 
														 " WHERE username = ?");
			
			String[] passSalt = {null, null};
			String passAndSalt;
			
			boolean loggedIn = false;
			
			while (!loggedIn)
			{
				try
				{
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
//						passAndSalt = null;
						close();
						return false;
					}
					if (passAndSalt != null
							&& md5(userPass + passSalt[1]).equals(passSalt[0]))
					{
						System.out.println("Logging in!");
						loggedIn = true;
						close();
						return true;
					}
					else
					{
//						System.out.println("Incorrect username and/or password." + username + " " + password);
						close();
						return false;
					}
				}
				catch(Exception e)
				{
					close();
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			close();
			e.printStackTrace();
		}
		close();
		return false;
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
				try
				{
					connect.commit();
				}
				catch (SQLException e)
				{
					System.out.println("Error while closing SQL: " + e);
				}
				finally
				{
					connect.close();
				}
			}
		}
		catch (Exception e)
		{

		}
	}

}