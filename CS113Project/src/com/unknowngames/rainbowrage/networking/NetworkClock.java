package com.unknowngames.rainbowrage.networking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class NetworkClock implements ActionListener
{
	RainbowRageServer udServer;
	Timer t;
	
	public NetworkClock(RainbowRageServer udServer, int uptime)
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
