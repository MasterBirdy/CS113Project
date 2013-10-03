package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.parser.ActorStructure;

public class ActorSkillUpgrade extends ActorSkillDisplay
{
	RoundButton confirmButton, cancelButton;
	float radius;
	protected boolean showDisplay = true;
	int selectedUnit;
	
	public ActorSkillUpgrade(int x, int y)
	{
		super(x, y);
		radius = 30 * scale;
		confirmButton = new RoundButton(x + 253 * scale, y - 115 * scale, radius, EverythingHolder.getObjectTexture("confirmbutton"));
		cancelButton = new RoundButton(x + 318 * scale, y - 115 * scale, radius, EverythingHolder.getObjectTexture("cancelbutton"));
		showDisplay = false;
		
		refreshButtons();
	}
	
	private void refreshButtons()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			if (buttons[i] != null)
			{
				if (!(everything.getSelfPlayer().unitSkillLevel(selectedUnit, i / 2) == i % 2) && selectedUnit != 6)
					buttons[i].setClickable(false);
				else
					buttons[i].setClickable(true);
			}
		}
	}
	
	public void setActor(ActorStructure actor, int unit)
	{
		super.setActor(actor);
		selectedUnit = unit;
		
		refreshButtons();
	}
	
	public int hit(float x, float y)
	{
		if (confirmButton != null && confirmButton.hit(x, y))
		{
			buySelected();
			refreshButtons();
			return 40;
		}
		else if (cancelButton != null && cancelButton.hit(x, y))
		{
			toggleDisplay();
			return 41;
		}
		return super.hit(x, y);
	}
	
	public boolean isShown()
	{
		return showDisplay;
	}
	
	public void toggleDisplay()
	{
		showDisplay = !showDisplay;
	}
	
	public void showDisplay()
	{
		showDisplay = true;
	}
	
	private void buySelected()
	{
		if (selectedUnit != 6)
			everything.buyUpgrade(selectedUnit, selectedSkill / 2, selectedSkill % 2, everything.team());
	}	
	
	@Override
	public void render(SpriteBatch batch)
	{
		batch.setColor(1, 1, 1, .5f);
		batch.draw(EverythingHolder.getObjectTexture("upgradeBackground"), x - 10, y - 145 * scale, 358 * scale, 420 * scale);
		batch.setColor(Color.WHITE);
		super.render(batch);
		if (confirmButton != null)
			confirmButton.draw(batch, 0);
		if (cancelButton != null)
			cancelButton.draw(batch, 0);
	}
}
