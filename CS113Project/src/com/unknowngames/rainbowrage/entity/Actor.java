package com.unknowngames.rainbowrage.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.unknowngames.rainbowrage.EverythingHolder;
import com.unknowngames.rainbowrage.parser.ActorStructure;
import com.unknowngames.rainbowrage.parser.InstantSkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillContainerStructure;
import com.unknowngames.rainbowrage.parser.SkillStructure;
import com.unknowngames.rainbowrage.parser.TravelingSkillContainerStructure;
import com.unknowngames.rainbowrage.skill.BasicAttack;
import com.unknowngames.rainbowrage.skill.PassiveSkill;
import com.unknowngames.rainbowrage.skill.ProcSkill;
import com.unknowngames.rainbowrage.skill.Skill;
import com.unknowngames.rainbowrage.skill.SkillContainer;
import com.unknowngames.rainbowrage.skill.SkillEffect;
import com.unknowngames.rainbowrage.skill.SkillEffectInjector;
import com.unknowngames.rainbowrage.skill.SkillSpawner;
import com.unknowngames.rainbowrage.skill.TargetedSkill;

public abstract class Actor extends Entity
{
	int currentHealth, maxHealth, damage, attackSpeed, attackCooldown, aoe,
		aoeBoost = 0,					 				// For buffs
		fear = -1, 										// For debuffs
		invis = -1;										// Ticks left of invis
//		procCooldown, procCooldownCounter;
	
	int radius;
	
	float attackRange;
	
	// Buffs/debuffs
	float attackSpeedBoost = 0, attackDamageBoost = 0, attackRangeBoost = 0,
		  armor = 0, dodge = 0, parry = 0;
	
	boolean attacking, ranged;
	
	Actor target;
	ArrayList<SkillEffect> skillEffects;// = new ArrayList<SkillEffect>(5);
//	ArrayList<ParticleEffect> peEffect = new ArrayList<ParticleEffect>(5);
	static SkillEffect nullSkillEffect = new SkillEffect();
	static ParticleEffect nullParticleEffect = new ParticleEffect();
	ParticleEffect temp = new ParticleEffect();
//	static LinkedList<Actor> team1;
//	static LinkedList<Actor> team2;
	static ParticleEffect fire = new ParticleEffect();
//	static ArrayList<Projectile> projectiles;
	int firstEmpty;
	static TextureRegion[] rangeIndicator;
	String projectileSprite;
	
//	PassiveSkill passiveSkill;
//	ProcSkill procSkill;
//	SkillStructure procStruct, attackStruct; //projectileStruct, meleeStruct;
//	SkillContainerStructure[] skillContainerStructures = new SkillContainerStructure[3];
	SkillSpawner[] skillSpawners = new SkillSpawner[4];
//	int[] skillLevels = {0, 0, 0};
	Sound attackSound;
//	SoundPack soundPack;
	String sounds;
//	TextureRegion projectileSprite;
	
	int attackers = 0;
	
	int level = 0;
	
	TextureRegion radiusTexture, rangeTexture;
	
	private static Entity currentCenterEntity;
	
	public Actor(int x, int y, int team, ActorStructure a, int[] skillLevels)
	{
		super(x, y, team);
		alive = true;
		remove = false;
		maxHealth = a.maxHealth(level);
		currentHealth = maxHealth;
		damage = a.damage(level);
		attackSpeed = a.attackSpeed(level);
		attackRange = a.attackRange(level);
		ranged = a.ranged(level);
		skillEffects = new ArrayList<SkillEffect>(5);
		aoe = -1;
		projectileSprite = a.projectile(0);
		radius = a.radius();
		
		radiusTexture = EverythingHolder.getObjectTexture("bluerange");
		rangeTexture = EverythingHolder.getObjectTexture("redrange");
		
		SkillContainerStructure sContainer;
		for (int i = 0; i < 3; i++)
		{
			sContainer = everything.getSkillContainer(a.getSkill(i, skillLevels[i]));
			if (sContainer == null)
				skillSpawners[i] = null;
			else
				skillSpawners[i] = new SkillSpawner(this, sContainer);
		}
		// NEED TO REWORK!!!		
//		SkillContainerStructure sContainer = everything.getSkillContainer(a.firstSkill(skillLevels[0]));
//		if (sContainer != null)
//			skillSpawners[0] = new SkillSpawner(this, sContainer);
//		else
//			skillSpawners[0] = null;
//			
//		sContainer = everything.getSkillContainer(a.secondSkill(skillLevels[1]));
//		if (sContainer != null)
//			skillSpawners[1] = new SkillSpawner(this, sContainer);
//		else
//			skillSpawners[1] = null;
//		
//		sContainer = everything.getSkillContainer(a.thirdSkill(skillLevels[2]));
//		if (sContainer != null)
//			skillSpawners[2] = new SkillSpawner(this, sContainer);
//		else
//			skillSpawners[2] = null;
		
		
//		SkillContainerStructure sContainer;
//		if (a.ranged(level))
//			sContainer = new TravelingSkillContainerStructure(everything.getSkillContainer("ranged"));
//		else
//			sContainer = new InstantSkillContainerStructure(everything.getSkillContainer("melee"));
//		SkillContainer sContainer = new SkillContainer(this, everything.getSkillContainer(a.ranged(level) ? "ranged" : "melee"));
		
		skillSpawners[3] = new BasicAttack(this); //, sContainer);
//		skillContainerStructures[0] = everything.getSkillContainer(a.firstSkill(skillLevels[0]));
//		skillContainerStructures[1] = everything.getSkillContainer(a.secondSkill(skillLevels[1]));
//		skillContainerStructures[2] = everything.getSkillContainer(a.thirdSkill(skillLevels[2]));
		
//		if (!a.projectile(level).equals("empty"))
//		{
//			if (a.projectile(level).equals("arrow") || a.projectile(level).equals("fireattack"))
//				projectileSprite = EverythingHolder.getObjectTexture(a.projectile(level) + everything.teamColor(this.team));
//			else
//				projectileSprite = EverythingHolder.getObjectTexture(a.projectile(level));
//		}
		
//		if (!a.passiveSkill(level).equals("empty"))
//			this.loadPassiveSkill(everything.getSkill(a.passiveSkill(level)));
//		if (!a.procSkill(level).equals("empty"))
//		{
//			this.loadProcSkill(everything.getSkill(a.procSkill(level)));
//			procStruct = everything.getSkill(a.procSkill(level));
//			procCooldown = procStruct.cooldown.get(0);
//			procCooldownCounter = procCooldown;
//		}
		
//		if (!a.soundPack(level).equals("empty"))
//			this.soundPack = everything.getUnitSounds(a.soundPack(level));
		this.sounds = a.soundPack(level);
//		loadProjectile(a);
		
//		if (this instanceof Building)
//		{
////			this.attackSound = everything.getSound("thwp");
//			this.soundPack = everything.getUnitSounds("minionarcher");
//		}
//		else
//		{
//			if (!a.passiveSkill(level).equals("empty"))
//				this.loadPassiveSkill(everything.getSkill(a.passiveSkill(level)));
//			if (!a.procSkill(level).equals("empty"))
//				this.loadProcSkill(everything.getSkill(a.procSkill(level)));
//			if (!a.soundPack(level).equals("empty"))
//				this.soundPack = everything.getUnitSounds(a.soundPack(level));
//			if (!a.attackSound(level).equals("empty"))
//				this.attackSound = everything.getSound(a.attackSound(level));
//		}
	}
	
	public void parry(int i)
	{
		parry += i;
	}
	
	public void dodge(int i)
	{
		dodge += i;
	}
	
	public void addSkillToBasic(Skill s)
	{
		((BasicAttack)skillSpawners[3]).addExtraSkills(s);
	}
	
	public static void setCenterActor(Entity a)
	{
		currentCenterEntity = a;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public String getProjectileSprite()
	{
		return projectileSprite;
	}
	
	public boolean isRanged()
	{
		return ranged;
	}
	
	public float getAttackRange()
	{
		if (attackRange > -attackRangeBoost)
			return attackRange + attackRangeBoost;
		else 
			return 0;
	}
	
	public int getDamage()
	{
		if (damage > -attackDamageBoost)
			return (int) (damage + attackDamageBoost);
		return 0;
	}
	
	public int getCooldown()
	{
		if (attackCooldown > attackSpeedBoost + 2)
			return (int) (attackCooldown - attackSpeedBoost);
		return 2;
	}
	
	public int getAoe()
	{
		if (aoe > -aoeBoost + 5)
			return aoe + aoeBoost;
		return -1;
	}
	
	public void addAttacker()
	{
		attackers++;
	}
	
	public void subAttacker()
	{
		attackers--;
		if (attackers < 0)
			attackers = 0;
	}
	
	public int getAttacker()
	{
		return attackers;
	}
	
	public void fear(int i)
	{
		if (fear < i)
			fear = i;
	}
	
	public void invis(int i)
	{
//		System.out.println("Going invis");
		invis = i;
		
		this.attacking = false;
//		this.target = null;
//		loseTarget();
		if (i > 0)
			particleOnSelf("smokebomb");
	}
	
	public void loseTarget()
	{
		if (target == null)
			return;
		target.subAttacker();
		target = null;
	}
	
	public void stun(int stun)
	{
//		System.out.println("Stun");
		attackCooldown = stun;
		everything.addTextEffect(xCoord, yCoord, "Stun", 2);
		particleOnSelf("stunattackeffect");
//		particleOnSelf("attackspeedbuff");
//		if (isAlive())
//		{
//			if (attackCooldown < stun)
//				attackCooldown = stun;
//		}
	}
	
	public void attackSpeedBoost(int boost)
	{
		attackSpeedBoost += boost;
		if (attackSpeedBoost >= attackSpeed)
			attackSpeedBoost = attackSpeed - 2;
	}
	
	public void attackDamageBoost(int boost)
	{
		attackDamageBoost += boost;
		if (attackDamageBoost < 0)
			attackDamageBoost = 0;
	}
	
	public void attackRangeBoost(int boost)
	{
		attackRangeBoost += boost;
		if (attackRangeBoost < 0)
			attackRangeBoost = 0;
	}
	
	public void armor(int boost)
	{
//		System.out.println("Armor: " + boost);
		armor += boost;
		if (armor < 0)
			armor = 0;
	}
	
//	public void loadPassiveSkill(SkillStructure pSkill)
//	{
//		passiveSkill = new PassiveSkill(pSkill, this);
//	}
	
//	public void loadProcSkill(SkillStructure pSkill)
//	{
//		procSkill = new ProcSkill(pSkill, this);
//	}
	
	public Actor getTarget()
	{
		return target;
	}
	
	public float getHealthRatio()
	{
		return (float)currentHealth / maxHealth;
	}
	
	public int getHealth()
	{
		return currentHealth;
	}
	
//	static public void loadRange()
//	{
//		rangeIndicator = new TextureRegion[2];
//		rangeIndicator[0] = new TextureRegion(spriteSheet[0], 472, 40, 40, 40);
//		rangeIndicator[1] = new TextureRegion(spriteSheet[0], 472, 0, 40, 40);
//		fire.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		fire.setPosition(50, 50);
//		fire.start();
//	}
	
	public void takeSkillEffect(SkillEffect skill)
	{
		if (skill.priority == 3)
		{
			if (parry > 0 && everything.getRandom(100) < parry)
			{
				if (skill.caster != null && skill.caster.isAlive())
				{
					if (this.getDistanceSquared(skill.caster) < 
							(getAttackRange() + getRadius() + skill.caster.getRadius()) * 
							(getAttackRange() + getRadius() + skill.caster.getRadius()))
						((BasicAttack)skillSpawners[3]).cast(skill.caster);
				}
				everything.addTextEffect(xCoord, yCoord, "Parry", 1);
				return;
			}
			if (dodge > 0 && everything.getRandom(100) < dodge)
			{
				everything.addTextEffect(xCoord, yCoord, "Dodged", 1);
				return;
			}
		}

		if (skill instanceof SkillEffectInjector)
		{
			if (skill.effect > 0)
				skillSpawners[skill.effect].addExtraSkill((SkillEffectInjector)skill);
			//SkillEffectInjector temp = (SkillEffectInjector)skill;
			
		}
		else
		{
	//		System.out.println("Taking SKILL");
			firstEmpty = skillEffects.indexOf(nullSkillEffect);
	//		skill.affected.start();
			if (firstEmpty >= 0)
				skillEffects.add(firstEmpty, skill);
			else
				skillEffects.add(skill);
		}
		for (SkillSpawner s : skillSpawners)
		{
			if (s != null && s.getTrigger() == 2)
				s.cast();
		}
		
//		firstEmpty = peEffect.indexOf(nullParticleEffect);
//		if (firstEmpty >= 0)
//			peEffect.add(firstEmpty, skill.affected);
//		else
//			peEffect.add(skill.affected);
	}
	
	public void resetBoosts()
	{
		attackSpeedBoost = 0;
		attackDamageBoost = 0;
		attackRangeBoost = 0;
		aoeBoost = 0;
		armor = 0;
		dodge = 0;
		parry = 0;
	}
	
	public void update()
	{
		resetBoosts();
		
//		if (passiveSkill != null)
//			passiveSkill.update();
//		
////		procCooldownCounter--;
//		
//		if(procSkill != null)
//			procSkill.update();
		Collections.sort(skillEffects, SkillEffect.PriorityComparator);
		for (SkillEffect skill : skillEffects)
			skill.update();
		
		for (SkillSpawner s : skillSpawners)
		{
			if (s != null)
				s.update();
		}
		
//		for (SkillContainerStructure s : skillContainerStructures)
//			s
		
		// Once skills are added
//		for (int i = 0; i < skills.length; i++)
//		{
//			if (skills[i] != null)
//				skills[i].update;
//		}
		
//		for (SkillEffect skill : skillEffects)
//		{
//			skill.update();
////			if (skill.ticksLeft <= 0)
////				skill = nullSkillEffect;
////			if (skill == nullSkillEffect)
////				continue;
////			
////			switch (skill.effect)
////			{
////				case 1:
////					takeDamage(skill.effectAmount);
////					break;
////				case 2:
////					takeDamage(-1 * skill.effectAmount);
////					break;
////				case 3:
////					damageBoost += skill.effectAmount;
////					break;
////				case 4:
////					attackSpeedBoost += skill.effectAmount;
////					break;
////				case 5:
////					attackRangeBoost += skill.effectAmount;
////					break;
////			}
////			skill.ticksLeft--;
//			
//			// For continuous effects
////			if (skill.continuous)
////			{
////				firstEmpty = peEffect.indexOf(nullParticleEffect);
////				if (firstEmpty >= 0)
////					peEffect.add(firstEmpty, skill.affected);
////				else
////					peEffect.add(skill.affected);
////			}
//		}
		--fear;
		--invis;
	}
	public void draw(SpriteBatch batch)
	{
		if (everything.getSettings().showRange())
			rangeIndicator(batch);
		if (everything.getSettings().showRadius())
			radiusIndicator(batch);
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
	
	public void drawParticleEffects(SpriteBatch batch, float delta)
	{
		for (SkillEffect skill : skillEffects)
		{
			skill.draw(batch, delta);
		}
//		for (ParticleEffect effect : peEffect)
//		{
//			effect.setPosition(xCoord, yCoord + 10);
//			effect.draw(batch);
//		}
	}
	
//	static public void loadProjectiles(ArrayList<Projectile> p)
//	{
//		projectiles = p;
//	}
	
//	public ParticleEffect fire()
//	{
//		ParticleEffect e = new ParticleEffect();
//		e.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
//		e.setPosition(xCoord, yCoord);
//		e.start();
//		return e;
//	}
//	
//	public ParticleEffect blood()
//	{
//		ParticleEffect e = new ParticleEffect();
//		e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/BloodEffectAndroid.p" : "data/BloodEffect.p")), Gdx.files.internal("images"));
//		e.setPosition(xCoord + 20, yCoord + 20);
//		e.start();
//		return e;
//	}
//	
//	public ParticleEffect spark()
//	{
//		ParticleEffect e = new ParticleEffect();
//		if (!(Gdx.app.getType() == ApplicationType.Android))
//		{
//			e.load(Gdx.files.internal((Gdx.app.getType() == ApplicationType.Android ? "data/SparkEffectAndroid.p" : "data/sparkeffect.p")), Gdx.files.internal("images"));
//			e.setPosition(xCoord + 20, yCoord + 20);
//			e.start();
//		}
//		return e;
//	}
	
	public void particleOnSelf(String s)
	{
		ParticleEffect p = EverythingHolder.getEffect(s);
		p.setPosition(xCoord, yCoord + 20);
		p.start();
		addParticle(p);
//		effects.add(p);
	}
	
	public void particleOnSelf(ParticleEffect p)
	{
		p.setPosition(xCoord, yCoord + 20);
		p.start();
		addParticle(p);
//		effects.add(p);
	}
	
	
	public void rangeIndicator(SpriteBatch batch)
	{
		batch.setColor(1, 1, 1, .25f);
		batch.draw(rangeTexture, -attackRange - radius + xCoord, -attackRange - radius + yCoord, 2 * (attackRange + radius), 2 * (attackRange + radius));
//		batch.draw(rangeIndicator[attacking ? 1 : 0], -attackRange + 16 + xCoord, -attackRange + 20 + yCoord, 2 * attackRange, 2 * attackRange);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void radiusIndicator(SpriteBatch batch)
	{
		if (this instanceof Actor)
		{
			batch.setColor(1, 1, 1, .25f);
			batch.draw(radiusTexture, -radius + xCoord, -radius + yCoord, 2 * radius, 2 * radius);
			batch.setColor(1, 1, 1, 1);
		}
	}

	public void takeDamage(int damage)
	{
//		if (procSkill != null && procStruct.trigger == 0)
//		{
////			System.out.println("TRIP!");
//			procSkill.trip(0);
//		}
		
		damage = ((damage -= armor) < 0 ? 0 : damage);
		currentHealth -= damage;
		if (currentHealth < 0)
			currentHealth = 0;
		
		everything.addTextEffect(xCoord, yCoord, damage + "", 0);
//		System.out.println("Hit me!");
	}
	
	public void takeDamage(int damage, int type)
	{
		takeDamage(damage);
		
//		System.out.println("Hit me!");
//		if (procSkill != null)
//		{
////			System.out.println("TRIP!");
//			procSkill.trip(0);
//		}
//		if (effects.isEmpty())
//			effects.add(this.fire());
//		if (type == 0)
//			effects.add(this.spark());
//		else if (type == 1)
//			effects.add(this.fire());
//		else if (type == 2)
//			effects.add(this.blood());
//		System.out.println("Effects");
	}
	
	public void heal(int heal)
	{
		currentHealth += heal;
		if (currentHealth > maxHealth)
			currentHealth = maxHealth;
		
		everything.addTextEffect(xCoord, yCoord, heal + "", 1);
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
				this.particleOnSelf("blood");
//				effects.add(this.blood());
//				soundPack.playDie();
				if (!sounds.equals("empty"))
				{
					try
					{
						EverythingHolder.getUnitSounds(sounds).playDie(everything.getSoundLevel());
					}
					catch (NullPointerException npe)
					{
						
					}
				}
				alive = false;
				
				if (this instanceof Minion)
				{
//					System.out.println("DEAD");
					everything.minionDeath(this.team);
				}
				
				else if (this instanceof Hero)
				{
					everything.heroDeath(this.team());
				}
				loseTarget();
			}
		}
	}
	
	// Sorts from lowest health ratio to highest	
	public static Comparator<Actor> HealthComparator = new Comparator<Actor>()
	{
		public int compare(Actor a1, Actor a2)
		{
			if (a1 == null)
			{
				if (a2 != null)
					return 1;
				return 0;
			}
			else if (a2 == null)
				return -1;
			
			int y1 = (int)a1.getHealthRatio();
			int y2 = (int)a2.getHealthRatio();
			if (y1 < y2)
				return -1;
			if (y1 > y2)
				return 1;	
			return 0;
		}
	};
	
	// Sorts from lowest targeted unit to highest, then by distance
	// Used by basic attack
	public static Comparator<Actor> TargetedComparator = new Comparator<Actor>()
	{
		public int compare(Actor a1, Actor a2)
		{
			if (a1 == null)
			{
				if (a2 != null)
					return 1;
				return 0;
			}
			else if (a2 == null)
				return -1;
			
			int y1 = a1.getAttacker();
			int y2 = a2.getAttacker();
			float d1 = currentCenterEntity.getDistanceSquared(a1);
			float d2 = currentCenterEntity.getDistanceSquared(a2);
			
			if (y1 < y2)
				return -1;
			if (y1 > y2)
				return 1;	
			if (d1 < d2)
				return -1;
			if (d1 > d2)
				return 1;
			return 0;
		}
	};
	
//	public static void linkActors(LinkedList<Actor> t1, LinkedList<Actor> t2)
//	{
//		team1 = t1;
//		team2 = t2;
//	}
	
//	public float getDistanceSquared(Actor a)
//	{
//		
//	}
	
	public void targetSelector()
	{
//		float currentDistance;
		if (target != null && target.isAlive() && target.invis < 0)
		{
			if (getDistanceSquared(target) <= (this.getAttackRange() + 
				this.radius + target.radius) * (this.getAttackRange() + this.radius + target.radius))
				return;
//			currentDistance = getDistanceSquared(target);
////			if (target instanceof Building)
////				currentDistance -= 2375;
//			if (currentDistance < attackRange * attackRange)
//			{
//				return;
//			}
//			loseTarget();
		}
		
		loseTarget();
		
//		Actor newTarget = null;
//		float newDistance = 1000000;
		float newDistance = attackRange * attackRange + 1;
		for (Actor a : everything.actorsInRange(this, (int)getAttackRange(), -1))
		{
			if (a.invis < 0)
			{
				target = a;
				break;
			}
		}
//		int lowest = 1000;
//		currentDistance = 0;
//		for (Actor a : (team == 1 ? everything.team(2) : everything.team(1)))
//		{			
//			if (a.isAlive() && a.invis < 0)
//			{
//				currentDistance = this.getDistanceSquared(a);
//				if (a instanceof Building)
//					currentDistance -= 2375;
//				if ((a.getAttacker() < lowest && currentDistance < attackRange * attackRange + 1) ||
//						(currentDistance < newDistance))
//				{
//					newDistance = currentDistance;
//					lowest = a.getAttacker();
//					target = a;
//				}
////				else if (currentDistance < newDistance)
////				{
////					newDistance = currentDistance;
////					lowest = a.getAttacker();
////					target = a;
////				}
//			}
//		}
		
		
//		ArrayList<Actor> enemy = (team == 1 ? everything.team(2) : everything.team(1));
////		LinkedList<Actor> enemy = (team == 1 ? team2 : team1);
//		Iterator<Actor> actorIter = enemy.iterator();
//		Actor newTarget = null;
//		float newDistance = 1000000;
//		currentDistance = 0;
//		while(actorIter.hasNext())
//		{
//			Actor e = actorIter.next();
//			
//			if (e.isAlive() && e.invis < 0)
//			{
//				currentDistance = this.getDistanceSquared(e);
//				if (e instanceof ArrowTower)
//					currentDistance -= 3600;
//				if (currentDistance < newDistance && currentDistance < attackRange * attackRange)
//				{
//					newDistance = currentDistance;
//					newTarget = e;
//				}
//			}
//		}
//		
//		target = newTarget;
		
		if (target != null)
		{
			attacking = true;
			target.addAttacker();
		}
		else
			attacking = false;
		
	}

	public abstract void destroy();

	protected void attack()
	{
		if (target == null || !target.isAlive() || !this.isAlive())
			return;
//		if (soundPack != null)
//			soundPack.playAttack();
		if (sounds != null && !sounds.equals("empty"))
		{
			try
			{
				EverythingHolder.getUnitSounds(sounds).playAttack(everything.getSoundLevel());
			}
			catch (NullPointerException npe)
			{
				
			}
		}
//		if (attackSound != null)
//			attackSound.play(volume);
//		if (procSkill != null)
//		{
////			System.out.println("TRIP!");
//			if (procSkill.trigger == 1)
//				castProc();
////			procSkill.trip(1);
//		}
		boolean cast = false;
		for (int i = 0; i < 3; i++)
		{
			if (skillSpawners[i] != null && skillSpawners[i].getTrigger() == 1)
				cast = (skillSpawners[i].cast() ? true : cast);
//			if ((!cast || skillSpawners[i].getTrigger() != 1) && skillSpawners[i].cast())
//				break;
		}
		if (!cast && skillSpawners[3] != null)
		{
			skillSpawners[3].cast();
		}
//		if (!castProc())
//		{
//			everything.add(new TargetedSkill(attackStruct, this, target), team);
////			if (ranged)
////				rangeAttack();
////			else
////				meleeAttack();
//		}
	}
	
//	protected boolean castProc()
//	{
////		System.out.println("attemptProcCast");
//		if (procStruct != null && this.isAlive() && procCooldownCounter < 0 && procStruct.trigger.get(0) == 1)
//		{
//			Actor targetActor = findTarget(this, procStruct);
//			if (targetActor != null)
//			{
//	//			System.out.println("procCast");
//				procCooldownCounter = procCooldown;
//				
//				everything.add(new TargetedSkill(procStruct, this, targetActor), team);
//	//			everything.add(new TargetedSkill(procStruct, this, target), team);
//				return true;
//			}
//		}
//		return false;
//	}
	
	
	
	protected Actor findTarget(Actor caster, SkillContainerStructure skill)
	{
		if (skill.targetType == 1)
		{
//			System.out.println("Caster targeted");
			return caster;
		}
		else if (skill.targetType == -1 && caster.target != null)
		{
//			System.out.println("Target targeted");
			return caster.target;
		}
		
		
		return everything.actorInRange(this, skill.targetRange, skill.targetType);
			
//		ArrayList<Actor> temp = new ArrayList<Actor>();
//		for (Actor a : everything.team(caster.team(), skill.targetType))
//			if (this.getDistanceSquared(a) < skill.targetRange * skill.targetRange)
//				temp.add(a);
		
//		if (temp.size() > 1 && skill.targetCount > 0)
//		{
//			switch(Math.abs(skill.targeting.get(0)))
//			{
//			case 4:
//			case 5:
//				Collections.sort(temp, Actor.HealthComparator);
//				break;
//			}
//		}
//		if (temp.size() == 0)
//			return null;
//		
//		return temp.get(0);
	}
	
//	protected void loadProjectile(ActorStructure a)
//	{
		//SkillStructure s = everything.getSkill("blank");
//		projectileStruct = new SkillStructure(everything.getSkill("blank"));
//		projectileStruct.targetTeam.set(0, 1);
//		projectileStruct.effect.set(0, 0);
//		projectileStruct.effectAmount.set(0, a.damage(level));
//		projectileStruct.duration.set(0, 1);
//		projectileStruct.effectTick.set(0, 0);
//		projectileStruct.travelTime.set(0, 70);
//		projectileStruct.additive.set(0, true);
//		projectileStruct.speed.set(0, 3f);
		
//		if (!a.projectile(level).equals("empty"))
//		{
//			if (a.projectile(level).equals("arrow") || a.projectile(level).equals("fireattack"))
//				projectileStruct.sprite.set(0, a.projectile(level) + everything.teamColor(this.team));
//			else
//				projectileStruct.sprite.set(0, a.projectile(level));
//		}
		
//		attackStruct = new SkillStructure(everything.getSkill("blank"));
//		attackStruct.aoe.set(0, -1);
//		attackStruct.targeting.set(0, -1);
//		attackStruct.effect.set(0, 0);
//		attackStruct.effectAmount.set(0, a.damage(level));
//		attackStruct.duration.set(0, 1);
//		attackStruct.effectTick.set(0, 0);
//		attackStruct.travelTime.set(0, (a.ranged(level) ? 70 : -1));
//		attackStruct.additive.set(0, true);
//		attackStruct.speed.set(0, 3f);
//		
//		if (a.ranged(level) && !a.projectile(level).equals("empty"))
//		{
//			if (a.projectile(level).equals("arrow") || a.projectile(level).equals("fireattack"))
//				attackStruct.sprite.set(0, a.projectile(level) + everything.teamColor(this.team));
//			else
//				attackStruct.sprite.set(0, a.projectile(level));
//		}
		
//		projectileStruct.travel.set(0, "fireball");
//	}
	
	/*protected void rangeAttack() 
	{
//		if (!(this instanceof Stronghold))
//		{
//			if (this.maxHealth == 65)
//				projectiles.add(new MageProjectile(this.xCoord, this.yCoord, this.team, 3, target));
//			else
//				projectiles.add(new ArrowProjectile(this.xCoord + (this instanceof Building ? 10 : 0), this.yCoord + (this instanceof Building ? 40 : 0), this.team, 3, target));
//		}
//		else
//		{
//			projectiles.add(new CannonProjectile(this.xCoord, this.yCoord + 50, this.team, 3, target));
//		}
		everything.add(new TargetedSkill(projectileStruct, this, target), team);
//		everything.add(new TargetedSkill(procStruct, this, target), team);f
//		projectiles.add(new Projectile(this.xCoord, this.yCoord + (this instanceof Building ? 50 : 0), this.team, 3, target, projectileSprite));
		
//		target.takeDamage(damage);
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
	}*/

	public void changeToLevel(int level, ActorStructure a) 
	{
		maxHealth = a.maxHealth(level);
		damage = a.damage(level);
		attackSpeed = a.attackSpeed(level);
		attackRange = a.attackRange(level);
		ranged = a.ranged(level);
		if (!a.soundPack(level).equals("empty"))
			this.attackSound = EverythingHolder.getSound(a.soundPack(level));
//		if (!a.passiveSkill(level).equals("empty"))
//			this.loadPassiveSkill(everything.getSkill(a.passiveSkill(level)));
//		if (!a.procSkill(level).equals("empty"))
//			this.loadProcSkill(everything.getSkill(a.procSkill(level)));
//		cost = a.cost(level);
		
	}
}
