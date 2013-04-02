package com.awesomeincorporated.unknowndefense.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.awesomeincorporated.unknowndefense.SoundPack;
import com.awesomeincorporated.unknowndefense.parser.ActorStructure;
import com.awesomeincorporated.unknowndefense.parser.SkillStructure;
import com.awesomeincorporated.unknowndefense.skill.PassiveSkill;
import com.awesomeincorporated.unknowndefense.skill.SkillEffect;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Actor extends Entity
{
	int currentHealth, maxHealth, damage,
		damageBoost = 0;								// For buffs
	float attackSpeed, attackCooldown, attackRange,
		  attackSpeedBoost = 0, attackRangeBoost = 0;	// For buffs
	boolean attacking, ranged;
	Actor target;
	ArrayList<SkillEffect> skillEffects;// = new ArrayList<SkillEffect>(5);
//	ArrayList<ParticleEffect> peEffect = new ArrayList<ParticleEffect>(5);
	static SkillEffect nullSkillEffect = new SkillEffect();
	static ParticleEffect nullParticleEffect = new ParticleEffect();
//	static LinkedList<Actor> team1;
//	static LinkedList<Actor> team2;
	static ParticleEffect fire = new ParticleEffect();
	static ArrayList<Projectile> projectiles;
	int firstEmpty;
	static TextureRegion[] rangeIndicator;
	PassiveSkill passiveSkill;
	Sound attackSound;
	SoundPack soundPack;
	
	int animation = 0, level = 0;

	public Actor(int x, int y, boolean ranged, int team, ActorStructure a)
	{
		super(x, y, team);
		alive = true;
		this.ranged = ranged;
		skillEffects = new ArrayList<SkillEffect>(5);
		if (this instanceof Building)
		{
			this.attackSound = everything.getSound("thwp");
			this.soundPack = everything.getUnitSounds("minionarcher");
		}
		else
		{
			if (!a.passiveSkill(level).equals("empty"))
				this.loadPassiveSkill(everything.getSkill(a.passiveSkill(level)));
			if (!a.soundPack(level).equals("empty"))
				this.soundPack = everything.getUnitSounds(a.soundPack(level));
//			if (!a.attackSound(level).equals("empty"))
//				this.attackSound = everything.getSound(a.attackSound(level));
		}
	}
	
	public void stun(int stun)
	{
		System.out.println("Stun");
		attackCooldown = stun;
//		if (isAlive())
//		{
//			if (attackCooldown < stun)
//				attackCooldown = stun;
//		}
	}
	
	public void loadPassiveSkill(SkillStructure pSkill)
	{
		passiveSkill = new PassiveSkill(pSkill, this);
	}
	
	public float getHealthRatio()
	{
		return (float)currentHealth / maxHealth;
	}
	
	static public void loadRange()
	{
		rangeIndicator = new TextureRegion[2];
		rangeIndicator[0] = new TextureRegion(spriteSheet, 472, 40, 40, 40);
		rangeIndicator[1] = new TextureRegion(spriteSheet, 472, 0, 40, 40);
		fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		fire.setPosition(50, 50);
		fire.start();
	}
	
	public void takeSkillEffect(SkillEffect skill)
	{
		System.out.println("Taking SKILL");
		firstEmpty = skillEffects.indexOf(nullSkillEffect);
		skill.affected.start();
		if (firstEmpty >= 0)
			skillEffects.add(firstEmpty, skill);
		else
			skillEffects.add(skill);
		
//		firstEmpty = peEffect.indexOf(nullParticleEffect);
//		if (firstEmpty >= 0)
//			peEffect.add(firstEmpty, skill.affected);
//		else
//			peEffect.add(skill.affected);
	}
	
	public void update()
	{
		if (passiveSkill != null)
			passiveSkill.update();
		
		for (SkillEffect skill : skillEffects)
		{
			skill.update();
//			if (skill.ticksLeft <= 0)
//				skill = nullSkillEffect;
//			if (skill == nullSkillEffect)
//				continue;
//			
//			switch (skill.effect)
//			{
//				case 1:
//					takeDamage(skill.effectAmount);
//					break;
//				case 2:
//					takeDamage(-1 * skill.effectAmount);
//					break;
//				case 3:
//					damageBoost += skill.effectAmount;
//					break;
//				case 4:
//					attackSpeedBoost += skill.effectAmount;
//					break;
//				case 5:
//					attackRangeBoost += skill.effectAmount;
//					break;
//			}
//			skill.ticksLeft--;
			
			// For continuous effects
//			if (skill.continuous)
//			{
//				firstEmpty = peEffect.indexOf(nullParticleEffect);
//				if (firstEmpty >= 0)
//					peEffect.add(firstEmpty, skill.affected);
//				else
//					peEffect.add(skill.affected);
//			}
		}
	}
	
//	public void draw(SpriteBatch batch)
//	{
//		for (ParticleEffect e : peEffect)
//			e.draw(batch);
//		for (SkillEffect skill : skillEffects)
//		{
//			skill.draw(batch);
//		}
//	}
	
	public void drawParticleEffects(SpriteBatch batch)
	{
		for (SkillEffect skill : skillEffects)
		{
			skill.draw(batch);
		}
//		for (ParticleEffect effect : peEffect)
//		{
//			effect.setPosition(xCoord, yCoord + 10);
//			effect.draw(batch);
//		}
	}
	
	static public void loadProjectiles(ArrayList<Projectile> p)
	{
		projectiles = p;
	}
	
	public ParticleEffect fire()
	{
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		e.setPosition(xCoord, yCoord);
		e.start();
		return e;
	}
	
	public ParticleEffect blood()
	{
		ParticleEffect e = new ParticleEffect();
		e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
		e.setPosition(xCoord + 20, yCoord + 20);
		e.start();
		return e;
	}
	
	public ParticleEffect spark()
	{
		ParticleEffect e = new ParticleEffect();
		if (!(Gdx.app.getType() == ApplicationType.Android))
		{
			e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/SparkEffectAndroid.p" : "data/sparkeffect.p")), Gdx.files.internal("images"));
			e.setPosition(xCoord + 20, yCoord + 20);
			e.start();
		}
		return e;
	}
	
	
	public void rangeIndicator(SpriteBatch batch)
	{
		batch.setColor(1, 1, 1, .35f);
		batch.draw(rangeIndicator[attacking ? 1 : 0], -attackRange + 16 + xCoord, -attackRange + 20 + yCoord, 2 * attackRange, 2 * attackRange);
		batch.setColor(1, 1, 1, 1);
	}

	public void takeDamage(int damage)
	{
		currentHealth -= damage;
		if (currentHealth > maxHealth)
			currentHealth = maxHealth;
		if (currentHealth < 0)
			currentHealth = 0;
	}
	
	public void takeDamage(int damage, int type)
	{
		takeDamage(damage);
//		if (effects.isEmpty())
//			effects.add(this.fire());
//		if (type == 0)
//			effects.add(this.spark());
//		else if (type == 1)
//			effects.add(this.fire());
//		else if (type == 2)
//			effects.add(this.blood());
		System.out.println("Effects");
	}
	
	public void checkAlive()
	{
		if (currentHealth <= 0)
		{
			if (alive)// && Gdx.app.getType() == Application.ApplicationType.Desktop)
			{
//				effects.add(this.fire());
				for (SkillEffect skill : skillEffects)
					skill.kill();
				effects.add(this.blood());
				soundPack.playDie();
				alive = false;
			}
		}
	}
	
//	public static void linkActors(LinkedList<Actor> t1, LinkedList<Actor> t2)
//	{
//		team1 = t1;
//		team2 = t2;
//	}
	
	public void targetSelector()
	{
		float currentDistance;
		if (target != null && target.isAlive())
		{
			currentDistance = getDistanceSquared(target);
			if (target instanceof ArrowTower)
				currentDistance -= 3600;
			if (currentDistance < attackRange * attackRange)
			{
				return;
			}
		}		
		
		ArrayList<Actor> enemy = (team == 1 ? everything.team(2) : everything.team(1));
//		LinkedList<Actor> enemy = (team == 1 ? team2 : team1);
		Iterator<Actor> actorIter = enemy.iterator();
		Actor newTarget = null;
		float newDistance = 1000000;
		currentDistance = 0;
		while(actorIter.hasNext())
		{
			Actor e = actorIter.next();
			
			if (e.isAlive())
			{
				currentDistance = this.getDistanceSquared(e);
				if (e instanceof ArrowTower)
					currentDistance -= 3600;
				if (currentDistance < newDistance && currentDistance < attackRange * attackRange)
				{
					newDistance = currentDistance;
					newTarget = e;
				}
			}
		}
		
		target = newTarget;
		
		attacking = (target == null) ? false : true; 
	}

	public abstract void destroy();

	protected void attack()
	{
		if (target == null || !target.isAlive())
			return;
		soundPack.playAttack();
//		if (attackSound != null)
//			attackSound.play(volume);
		if (ranged)
			rangeAttack();
		else
			meleeAttack();
	}
	
	protected void rangeAttack() 
	{
		if (!(this instanceof Stronghold))
		{
			projectiles.add(new ArrowProjectile(this.xCoord + (this instanceof ArrowTower ? 10 : 0), this.yCoord + (this instanceof ArrowTower ? 40 : 0), this.team, 3, target));
		}
		else
		{
			projectiles.add(new CannonProjectile(this.xCoord, this.yCoord + 50, this.team, 3, target));
		}
		target.takeDamage(damage);
//		if (target == null || !target.isAlive())
//			return;
		
//		if (!(target == null || !target.isAlive()))
//		{
//			if (this.attackCooldown <= 0)
//			{
////				sounds.get("thwp").play(volume);
////				everything.getSound("thwp");
////				attackSound.play(volume);
//				if (!(this instanceof Stronghold))
//				{
//					projectiles.add(new ArrowProjectile(this.xCoord + (this instanceof ArrowTower ? 10 : 0), this.yCoord + (this instanceof ArrowTower ? 40 : 0), this.team, 3, target));
//				}
//				else
//				{
//					projectiles.add(new CannonProjectile(this.xCoord, this.yCoord + 50, this.team, 3, target));
//				}
//				target.takeDamage(damage);
//			}
//		}
//		else
//			if (projectiles.size() != 0)
//				projectiles.removeAll(projectiles);
	}
	
	protected void meleeAttack()
	{
//		if (target == null || !target.isAlive())
//			return;
		target.takeDamage(damage);
	}
}
