package com.awesomeincorporated.unknowndefense.networking;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class User
{
	public String name;
	public String otherStuff;
	public int id, x, y, room;
	static TextureRegion[] texture = new TextureRegion[2];
	
	public User()
	{
		x = 0;
		y = 0;
		id = 0;
		room = -1;
	}
	
//	public User(int team)
//	{
//		x = 0;
//		y = 0;
//		id = team;
//	}
//	
//	public void draw(SpriteBatch batch)
//	{
//		batch.draw(texture[id%2], x, y);
//	}
}
