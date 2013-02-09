package com.awesomeincorporated.unknowndefense.networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class NetworkClock implements ActionListener
{
	UnknownDefenseServer udServer;
	Timer t;
	
	public NetworkClock(UnknownDefenseServer udServer, int uptime)
	{
		this.udServer = udServer;
		t = new Timer(uptime, this);
		t.start();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		System.out.println("Tick");
		udServer.kill();
		System.out.println("KILL");
		t.stop();
	}
}
