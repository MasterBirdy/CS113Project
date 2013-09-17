package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.BaseClass;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.entity.Actor;
import com.unknowngames.rainbowrage.parser.ActorStructure;
import com.unknowngames.rainbowrage.parser.HeroStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;

public class ActorSkillDisplay extends BaseClass
{
	ActorStructure shownActor;
	SpriteBatch batch;
	int x, y;
	
//	TextureRegion[] skillIcons = new TextureRegion[4];
	RectangularButton[] buttons = new RectangularButton[7];
	TextureRegion chibi;
	String description = "";
	BitmapFont font = everything.getFont(1);
	
	public ActorSkillDisplay(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setActor(ActorStructure actor)
	{
		shownActor = actor;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				if (!actor.getSkill(i, j).equals("empty"))
					buttons[i * 2 + j] = new RectangularButton(x + 300 - (j * 60), y + 200 - (i * 60), 50, 50, getIcon(actor, i));
				else
					buttons[i * 2 + j] = null;
			}
			if (shownActor instanceof HeroStructure)
				buttons[6] = new RectangularButton(x + 300, y - 40, 50, 50, getIcon(actor, 3));
		}
		
		description = actor.description();
//			skillIcons[i] = getIcon(actor, i);
//		if (shownActor instanceof HeroStructure)
//			skillIcons[3] = getIcon(actor, 3);
//		else
//			skillIcons[3] = null;
		chibi = getActorImage(shownActor.name());
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(chibi, x, y - 50);//, 350, 40);
		for (Button b : buttons)
		{
			if (b != null)
				b.draw(batch, 0);
		}
		
		font.draw(batch, description, x, y - 50);
//		batch.draw(skillIcons[0], x + 300, y + 200, 40, 40);
//		batch.draw(skillIcons[1], x + 300, y + 150, 40, 40);
//		batch.draw(skillIcons[2], x + 300, y + 100, 40, 40);
//		if (skillIcons[3] != null)
//			batch.draw(skillIcons[3], x + 300, y + 250, 40, 40);
	}
	
	private TextureRegion getIcon(ActorStructure a, int skill)
	{
		SkillContainerStructure s = null;
		
		if (skill == 0)
			s = everything.getSkillContainer(a.firstSkill(0));
		else if (skill == 1)
			s = everything.getSkillContainer(a.secondSkill(0));
		else if (skill == 2)
			s = everything.getSkillContainer(a.thirdSkill(0));
		else if (skill == 3)
			s = everything.getSkillContainer(((HeroStructure)a).activeSkill(0));
		if (s != null)
			return getSkillIcon(s.icon);
		return getSkillIcon("defaulticon");
	}
	
	private TextureRegion getActorImage(String name)
	{
		TextureRegion t = EverythingHolder.getObjectTexture(name + "image");
		if (t == null)
			t = EverythingHolder.getObjectTexture("defaultimage");
		return t;
	}
	
	private TextureRegion getSkillIcon(String s)
	{
		TextureRegion t = EverythingHolder.getObjectTexture(s);
		if (t == null)
			t = EverythingHolder.getObjectTexture("defaulticon");
		return t;
	}
}
