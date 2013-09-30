package com.unknowngames.rainbowrage.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.unknowngames.rainbowrage.EverythingHolder;

public class ActorSkillUpgrade extends ActorSkillDisplay
{
	RoundButton confirmButton, cancelButton;
	float radius;
	protected boolean showDisplay = true;
	
	public ActorSkillUpgrade(int x, int y)
	{
		super(x, y);
		radius = 30 * scale;
		confirmButton = new RoundButton(x + 300 * scale, y - 40 * scale, radius, EverythingHolder.getObjectTexture("confirmbutton"));
		cancelButton = new RoundButton(x + 365 * scale, y - 40 * scale, radius, EverythingHolder.getObjectTexture("cancelbutton"));
		showDisplay = true;
	}
	
	public int hit(float x, float y)
	{
		if (confirmButton != null && confirmButton.hit(x, y))
		{
			buySelected();
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
		
	}	
	
	@Override
	public void render(SpriteBatch batch)
	{
		super.render(batch);
		if (confirmButton != null)
			confirmButton.draw(batch, 0);
		if (cancelButton != null)
			cancelButton.draw(batch, 0);
	}
}
