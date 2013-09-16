package com.unknowngames.rainbowrage.networking;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.security.*;

import com.unknowngames.rainbowrage.networking.RainbowRageServer.UserConnection;

public class MySQLLogin implements Runnable
{
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	Queue<UserConnection> userConnections = new ConcurrentLinkedQueue<UserConnection>();

	public MySQLLogin() throws Exception
	{
		connectDatabase();
		preparedStatement = connect
				.prepareStatement("SELECT password FROM joomla.phbdt_users WHERE username = ?");
	}

	public void connectDatabase() throws Exception
	{
		try
		{
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306", "root", "new-password");

			// // Statements allow to issue SQL queries to the database
			// statement = connect.createStatement();
			// // Result set get the result of the SQL query
			// resultSet =
			// statement.executeQuery("select * from test.Accounts");
			// writeResultSet(resultSet);
			//
			// preparedStatement =
			// connect.prepareStatement("SELECT password FROM joomla.phbdt_users WHERE username = ?");
			// preparedStatement.setString(1, "ThatGameGuy");
			// resultSet = preparedStatement.executeQuery();
			// resultSet.next();
			// String[] passSalt = resultSet.getString("password").split(":");
			// String myPass = "rainbowsix";
			// System.out.println(passSalt[0]);
			// System.out.println(md5(myPass + passSalt[1]));

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

	public void add()
	{

	}

	public void login()
	{
		try
		{
			preparedStatement.setString(1, "ThatGameGuy");
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			String[] passSalt = resultSet.getString("password").split(":");
			String myPass = "rainbowsix";
			System.out.println(passSalt[0]);
			System.out.println(md5(myPass + passSalt[1]));
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	// private void writeMetaData(ResultSet resultSet) throws SQLException
	// {
	// // Now get some metadata from the database
	// // Result set get the result of the SQL query
	//
	// System.out.println("The columns in the table are: ");
	//
	// System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	// for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
	// {
	// System.out.println("Column " + i + " "
	// + resultSet.getMetaData().getColumnName(i));
	// }
	// }
	//
	// private void writeResultSet(ResultSet resultSet) throws SQLException
	// {
	// // ResultSet is initially before the first data set
	// while (resultSet.next())
	// {
	// // It is possible to get the columns via name
	// // also possible to get the columns via the column number
	// // which starts at 1
	// // e.g. resultSet.getSTring(2);
	// String user = resultSet.getString("username");
	// String wins = resultSet.getString("wins");
	// // String summary = resultSet.getString("summary");
	// // Date date = resultSet.getDate("datum");
	// // String comment = resultSet.getString("comments");
	// System.out.println("User: " + user);
	// System.out.println("Wins: " + wins);
	// }
	// }

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
				connect.close();
			}
		}
		catch (Exception e)
		{

		}
	}

	@Override
	public void run()
	{

	}

}