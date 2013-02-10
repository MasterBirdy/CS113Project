package com.awesomeincorporated.unknowndefense.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.Timer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.awesomeincorporated.unknowndefense.networking.Network.*;
import com.esotericsoftware.minlog.Log;

public class UnknownDefenseServer 
{
	Server server;
	//LinkedList<User> waitingUsers = new LinkedList<User>();
	LinkedList<UserConnection> userConnection = new LinkedList<UserConnection>();
	LinkedList<Game[]> games = new LinkedList<Game[]>();
//    HashSet<User> loggedIn = new HashSet();
    int totalUsers = 0;
    int currentTurn = 0;
    long timeLastUsed;
    int allowedUptime = 7200000;
    NetworkClock networkClock = new NetworkClock(this, allowedUptime);
    public UnknownDefenseServer() throws IOException 
    {
    	System.out.println("Clock");
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
        		System.out.println("Received message");
        		// We know all connections for this server are actually UserConnections.
        		UserConnection connection = (UserConnection)c;
                User user = connection.user;

                if (object instanceof Login) 
                {
                	// Ignore if already logged in.
                    if (user != null) 
                    	return;
                    
                    // Reject if the name is invalid.
                    String name = ((Login)object).name;
                    if (!isValid(name)) 
                    {
                    	c.close();
                        return;
                    }
                    
                    // Reject if already logged in.
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
                    
                    user = new User(totalUsers);
                    user.name = ((Login)object).name;
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
                	Command cmd = (Command)object; 
                	System.out.println("Recevied command " + ((Command)object).type);
                	if (cmd.type > 0 && cmd.type < 7)
                	{
                		AddUnit add = new AddUnit();
                		add.team = cmd.team;
                		add.unit = cmd.type;
                		add.turn = currentTurn;
                		System.out.println("Sending unit " + cmd.type + " from player " + cmd.team);
                		
                		server.sendToTCP(userConnection.get(0).getID(), add);
                		server.sendToTCP(userConnection.get(1).getID(), add);
//                		server.sendToAllTCP(add);
                	}
                	if (cmd.type > 6 && cmd.type < 10) // 7-9 is retreat, guard, and attack.
                	{
                		System.out.println("Sending command out (Hero)");
                		CommandIn cmdIn = new CommandIn();
                		cmdIn.command = cmd.type;
                		cmdIn.team = cmd.team;
                		server.sendToTCP(userConnection.get(0).getID(), cmdIn);
                		server.sendToTCP(userConnection.get(1).getID(), cmdIn);
//                		server.sendToAllTCP(cmdIn);
                	}
                	if (cmd.type > 9 && cmd.type < 13) // 10-12 is upgrade towers 1, 2, and 3.
                	{
                		System.out.println("Sending command out (tower)");
                		CommandIn cmdIn = new CommandIn();
                		cmdIn.command = cmd.type;
                		cmdIn.team = cmd.team;
                		server.sendToTCP(userConnection.get(0).getID(), cmdIn);
                		server.sendToTCP(userConnection.get(1).getID(), cmdIn);
//                		server.sendToAllTCP(cmdIn);
                	}
//                	if (cmd.type > 9 && cmd.type < 13)
//                	{
//                		System.out.println("Upgrading tower");
//                		CommandIn cmdIn = new CommandIn();
//                		cmdIn.command = (byte) (cmd.type - 10);
//                		cmdIn.team = cmd.team;
//                		
//                	}
                	return;
                }
                
                if (object instanceof ClientMessage)
                {
                	return;
                }
                
                
//                if (object instanceof Register) 
//                {
//                	// Ignore if already logged in.
//                    if (unit != null) 
//                    	return;
//                    
//                    Register register = (Register)object;
//
//                    // Reject if the login is invalid.
//                    if (!isValid(register.name)) 
//                    {
//                    	c.close();
//                        return;
//                    }
//                    
//                    if (!isValid(register.otherStuff)) 
//                    {
//                    	c.close();
//                        return;
//                    }
//                    
//                    // Reject if character alread exists.
//                    if (loadUnit(register.name) != null) 
//                    {
//                    	c.close();
//                        return;
//                    }
//                    
//                    unit = new Unit(users++);
//                    unit.name = register.name;
//                    unit.otherStuff = register.otherStuff;
//                    unit.x = 0;
//                    unit.y = 0;
//                    if (!saveUnit(unit)) 
//                    {
//                    	c.close();
//                        return;
//                    }
//                    
//                    loggedIn(connection, unit);
//                    return;
//                }
//                
//                if (object instanceof MoveUnit) 
//                {
//                	// Ignore if not logged in.
//                    if (unit == null) 
//                    	return;
//                    
//                    MoveUnit msg = (MoveUnit)object;
//                    
//                    // Ignore if invalid move.
//                    if (Math.abs(msg.x) == 0 && Math.abs(msg.y) == 0) 
//                    	return;
//                    
//                    if (unit != null)
//                    	System.out.println(unit.id + " " + unit.name);
//                    
//                    unit.x += msg.x;
//                    unit.y += msg.y;
//                    if (!saveUnit(unit)) 
//                    {
//                    	connection.close();
//                        return;
//                    }
//                    
//                    UpdateUnit update = new UpdateUnit();
//                    update.id = unit.id;
//                    update.x = unit.x;
//                    update.y = unit.y;
//                    server.sendToAllTCP(update);
//                    return;
//                }
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
        		//waitingUsers.remove(waitingUsers.indexOf(c));
        		//for ()
        		totalUsers--;
        	}
        });
        
        server.bind(Network.port);
        server.start();
        System.out.println("Server started");
    }
    
    public void kill()
    {
    	this.server.stop();
    }
    
    void loggedIn (UserConnection c, User user) 
    {
    	c.user = user;
    	
    	// Add existing characters to new logged in connection.
//        for (Unit other : loggedIn) 
//        {
//        	AddUnit addUnit = new AddUnit();
//        	addUnit.unit = other;
//        	c.sendTCP(addUnit);
//        }
        System.out.println("Player " + user.name + userConnection.size() + " just joined.");
        
        LoginStatus status = new LoginStatus();
        status.status = (byte)userConnection.size();//waitingUsers.size();
        
        if (userConnection.size() > 2)
        {
        	status.status = -1;
        	c.sendTCP(status);
        	totalUsers++;
        	System.out.println("Too many users");
        	return;
        }
        
//        waitingUsers.add(user);
        userConnection.add(c);
        
        c.sendTCP(status);
        
        if (userConnection.size() >= 2)
        {
        	ServerMessage msg = new ServerMessage();
        	msg.message = 1;
        	//server.sendToAllTCP(msg);
        	server.sendToTCP(userConnection.get(0).getID(), msg);
        	server.sendToTCP(userConnection.get(1).getID(), msg);
        	//waitingUsers.get(0)., msg)
        }
        
        
        
        // Add logged in character to all connections.
//        AddUnit addUnit = new AddUnit();
//        addUnit.unit = unit;
//        server.sendToAllTCP(addUnit);
    }
    
//    boolean saveUnit(Unit unit) 
//    {
//    	File file = new File("units", unit.name.toLowerCase());
//    	file.getParentFile().mkdirs();
//    	
//    	if (unit.id == 0) 
//    	{
//    		String[] children = file.getParentFile().list();
//            if (children == null) 
//            	return false;
//            unit.id = children.length + 1;
//    	}
//    	
//    	DataOutputStream output = null;
//        try 
//        {
//        	output = new DataOutputStream(new FileOutputStream(file));
//            output.writeInt(unit.id);
//            output.writeUTF(unit.otherStuff);
//            output.writeInt(unit.x);
//            output.writeInt(unit.y);
//            return true;
//        } 
//        catch (IOException ex) 
//        {
//        	ex.printStackTrace();
//            return false;
//        } 
//        finally 
//        {
//        	try 
//        	{
//        		output.close();
//        	} 
//        	catch (IOException ignored) 
//        	{
//        	}
//        }
//    }
//    
//    Unit loadUnit(String name) 
//    {
//    	File file = new File("units", name.toLowerCase());
//    	if (!file.exists()) 
//    		return null;
//        DataInputStream input = null;
//        try 
//        {
//        	input = new DataInputStream(new FileInputStream(file));
//            Unit unit = new Unit(1);
//            unit.id = input.readInt();
//            unit.name = name;
//            unit.otherStuff = input.readUTF();
//            unit.x = input.readInt();
//            unit.y = input.readInt();
//            input.close();
//            return unit;
//        } 
//        catch (IOException ex) 
//        {
//        	ex.printStackTrace();
//            return null;
//        } 
//        finally 
//        {
//        	try 
//        	{
//        		if (input != null) 
//        			input.close();
//        	} 
//        	catch (IOException ignored) 
//        	{
//        	}
//        }
//    }
    
    // This holds per connection state.
    static class UserConnection extends Connection 
    {
    	public User user;
    }
    
    public static void main (String[] args) throws IOException 
    {
    	Log.set(Log.LEVEL_DEBUG);
        new UnknownDefenseServer();
    }   
}