package com.me.mygdxgame.entity;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.me.mygdxgame.map.Coordinate;

public class Minion extends Actor {

	public Minion(int x, int y, LinkedList<Coordinate> p, Sprite s) {
		super(x, y, p, s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double giveSpeed() {
		return 2.0;
	}

}
