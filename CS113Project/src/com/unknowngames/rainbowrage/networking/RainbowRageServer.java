package com.unknowngames.rainbowrage.networking;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Date;

import javax.swing.Timer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.unknowngames.rainbowrage.networking.Network.*;

public class RainbowRageServer 
{
	Server server;
	//LinkedList<User> waitingUsers = new LinkedList<User>();
	LinkedList<UserConnection> userConnection = new LinkedList<UserConnection>();
	LinkedList<UserConnection> waitingUsers = new LinkedList<UserConnection>();
//	LinkedList<Game[]> games = new LinkedList<Game[]>();
	HashMap<Integer, Game> games = new HashMap<Integer, Game>();
	int gameCount = 0;
//    HashSet<User> loggedIn = new HashSet();
    int totalUsers = 0;
    int currentTurn = 0;
    long timeLastUsed;
//    int allowedUptime = 14400000; // 4 hours
    int allowedUptime = -1; // infinite
    NetworkClock networkClock; // = new NetworkClock(this, allowedUptime);
    
    DateFormat df = DateFormat.getDateTimeInstance (DateFormat.SHORT, DateFormat.SHORT, new Locale("en", "EN"));
    
    public static long sentData = 0, receivedData = 0;
    private MySQLAccess mySQLAccess;
    
    
    public RainbowRageServer() throws IOException 
    {
//    	Log.set(Log.LEVEL_DEBUG);
    	if (allowedUptime > 0)
    	{
	    	System.out.println("Clock");
	    	networkClock = new NetworkClock(this, allowedUptime);
    	}
    	
//    	waitingUsers.get(0).
    	server = new Server() 
    	{
    		protected Connection newConnection() 
    		{
    			// By providing our own connection implementation, we can store per
                // connection state without a connection ID to state look up.
                return new UserConnection();
    		}
    	};
    	
    	// For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        Network.register(server);
        
        System.out.println(InetAddress.getByName(InetAddress.getLocalHost().getHostName()) + " : " + Network.port);
        
//        Listener listen = new Listener();
//        server.addListener();
        server.addListener(new Listener() 
        {
        	public void received (Connection c, Object object) 
        	{
        		receivedData += 16;
        		System.out.println("Received message");
        		// We know all connections for this server are actually UserConnections.
        		UserConnection connection = (UserConnection)c;
                User user = connection.user;
                
                if (object instanceof Login) 
                {
                	// Ignore if already logged in.
                	LoginStatus loginMsg = new LoginStatus();
                    if (user != null) 
                    {
                    	loginMsg.status = -4;
                    	c.sendTCP(loginMsg);
                    	return;
                    }
                    
                    // Reject if the name is invalid.
                    String name = ((Login)object).name;
                    String password = ((Login)object).password;
                    
                    if (!isValid(name) || !isValid(password)) // || !mySQLAccess.login(name, password)) 
                    {
                    	loginMsg.status = -1;
                    	c.sendTCP(loginMsg);
                    	c.close();
                        return;
                    }
                                        
                    loginMsg.status = 0;
                    c.sendTCP(loginMsg);
                    
                    // Reject if already logged in.
                    if (!name.equals("Guest"))
                    {
	                    for (UserConnection userCon : userConnection)
	                    {
	                    	if (userCon.user.name.equals(name))
	                    	{
	                    		loginMsg.status = -4;
	                        	c.sendTCP(loginMsg);
	                        	return;
	                    	}
	                    }
                    }
//                    for (Unit other : loggedIn) 
//                    {
//                    	if (other.name.equals(name)) 
//                    	{
//                    		c.close();
//                            return;
//                    	}
//                    }
                    
                    if (userConnection.size() >= 2)//totalUsers >= 5)
                    {
                    	System.out.println("Server full with " + userConnection.size() + " users");
                    	ServerMessage msg = new ServerMessage();
                    	msg.message = -2;
                    	c.sendTCP(msg);
                    	c.close();
                    	return;
                    }
//                    unit = loadUnit(name);

                    // Reject if couldn't load unit.
//                    if (unit == null) 
//                    {
//                    	c.sendTCP(new RegistrationRequired());
//                        return;
//                    }
                    
//                    user = new User(totalUsers);
                    
                    user = loadUserInfo(((Login)object).name);
                    /*user = new User();
                    user.name = ((Login)object).name;*/
//                    unit.otherStuff = register.otherStuff;
//                    unit.x = 0;
//                    unit.y = 0;
                    
                    loggedIn(connection, user);
                    return;
                }
                
                if (object instanceof AccountMessage)
                {
                	return;
                }
                
                if (object instanceof Command)
                {
                	System.out.println("Lets do this at room " + user.room);
                	Command cmd = (Command)object;
                	if (user.room >= 0)
                		games.get(user.room).messageCommand(cmd);
                	return;
                }
                
                if (object instanceof UserMessage)
                {
                	UserMessage msg = (UserMessage)object;
//                	if (((UserMessage)object).message.equals("kill"))
                	if (msg.message.equals("kill"))
            			kill();
                	if (user.room >= 0)
                		games.get(user.room).userMessage(msg);
                }
                
                if (object instanceof ClientMessage)
                {
                	return;
                }
                
                if (object instanceof HeroSelectStatus)
                {
                	if (user.room >= 0)
                		games.get(user.room).heroSelectStatus((HeroSelectStatus)object);
                }
        	}
        	
        	private User loadUserInfo(String name)
        	{
        		User user = new User();
        		user.name = name;
        		return user;
        	}
        	
        	private boolean isValid (String value) 
        	{
        		if (value == null) 
        			return false;
                value = value.trim();
                if (value.length() == 0) 
                	return false;
                return true;
        	}
        	
        	public void disconnected (Connection c) 
        	{
//        		UserConnection connection = (UserConnection)c;
//                if (connection.unit != null) 
//                {
//                	loggedIn.remove(connection.unit);
//                	
//                	RemoveUnit removeUnit = new RemoveUnit();
//                	removeUnit.id = connection.unit.id;
//                	server.sendToAllTCP(removeUnit);
//                	users--;
//                }
        		System.out.println("Player logged out.");
        		if (userConnection.contains(c))
        		{
        			System.out.println("Player removed");
        			userConnection.remove(c);
        		}
        		
        		writeToLog(df.format(new Date()) + ": Player " + c.getID() + userConnection.size() + " logged out.");
        		
//        		try 
//            	{
//            	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("TestLog.txt", true)));
//            	    out.println(df.format(new Date()) + ": Player " + c.getID() + userConnection.size() + " logged out.");
//            	    out.close();
//            	}
//            	catch (IOException e) 
//            	{
//            	    System.out.println(e);
//            	}
        		//waitingUsers.remove(waitingUsers.indexOf(c));
        		//for ()
        		totalUsers--;
        	}
        });
        
        server.bind(Network.port);
        server.start();
        System.out.println("Server started");
        
//        mySQLAccess = new MySQLAccess();
        
        writeToLog("-------------------------------");
        writeToLog(df.format(new Date()) + ": Server Started!");
        writeToLog("-------------------------------");
        
    }
    
    public void writeToLog(String txt)
    {
    	try 
    	{
    	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("TestLog.txt", true)));
    	    out.println(txt);
    	    out.close();
    	}
    	catch (IOException e) 
    	{
    	    System.out.println(e);
    	}
    }
    
    public void kill()
    {
    	writeToLog(df.format(new Date()) + ": Server Killed =(");
    	writeToLog("Total data sent: " + sentData);
    	writeToLog("Total data received: " + receivedData);
    	this.server.stop();
    }
    
    public int userCount()
    {
    	return userConnection.size();
    }
    
    public int roomCount()
    {
    	return games.size();
    }
    
    void loggedIn (UserConnection c, User user) 
    {
    	c.user = user;
    	
        System.out.println("Player " + user.name + userConnection.size() + " just joined.");
        
        writeToLog(df.format(new Date()) + ": " + user.name + " joined.");
        /*try 
    	{
    	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("TestLog.txt", true)));
    	    out.println(df.format(new Date()) + ": " + user.name + " joined.");
    	    out.close();
    	}
    	catch (IOException e) 
    	{
    	    System.out.println(e);
    	}*/
    	
//        LoginStatus status = new LoginStatus();
//        status.status = (byte)userConnection.size();//waitingUsers.size();
        
//        if (userConnection.size() > 2)
//        {
//        	status.status = -1;
//        	c.sendTCP(status);
//        	totalUsers++;
//        	System.out.println("Too many users");
//        	return;
//        }
        
//        waitingUsers.add(user);
        userConnection.add(c);
        waitingUsers.add(c);
        
//        c.sendTCP(status);
        
//        if (userConnection.size() >= 2)
//        {
//        	UserConnection p1 = null, p2 = null;
//        	while (p1 == null)
//        		p1 = userConnection.pop();
//        	while (p2 == null)
//        		p2 = userConnection.pop();
//        	
//        	Game game = new Game(p1, p2, server, gameCount);
//        	games.put(gameCount++, game);
//        	
//        	writeToLog(df.format(new Date()) + ": " + p1.user.name + " and " + p1.user.name + " started.");
//        	/*try 
//        	{
//        	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("TestLog.txt", true)));
//        	    out.println(df.format(new Date()) + ": " + p1.user.name + userConnection.size() + " and " + p1.user.name + userConnection.size() + 
//        	    			" started.");
//        	    out.close();
//        	}
//        	catch (IOException e) 
//        	{
//        	    System.out.println(e);
//        	}*/
//        }
        
        // Change to send array of players to accommodate different modes
        if (waitingUsers.size() >= 2)
        {
        	UserConnection p1 = null, p2 = null;
        	while (p1 == null)
        		p1 = waitingUsers.pop();
        	while (p2 == null)
        		p2 = waitingUsers.pop();
        	
        	Game game = new Game(p1, p2, server, gameCount);
        	games.put(gameCount++, game);
        	
        	writeToLog(df.format(new Date()) + ": " + p1.user.name + " and " + p1.user.name + " started.");
        }
    }
    
    // This holds per connection state.
    static class UserConnection extends Connection 
    {
    	public User user;
    }
    
    public static void main (String[] args) throws IOException 
    {
    	Log.set(Log.LEVEL_DEBUG);
        new RainbowRageServer();
    }   
}