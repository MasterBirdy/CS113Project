package com.awesomeincorporated.unknowndefense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

import com.awesomeincorporated.unknowndefense.entity.*;
import com.awesomeincorporated.unknowndefense.map.*;
import com.awesomeincorporated.unknowndefense.parser.*;
import com.awesomeincorporated.unknowndefense.skill.Skill;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
//	LinkedList<Actor>[] teams = new LinkedList[2];				// Soon to be depricated
	LinkedList<Integer>[] pools = new LinkedList[4];			// Pools for spawning
	EntityComparator eCompare = new EntityComparator();
	ArrayList<Entity> entities = new ArrayList<Entity>(50);		// Holds all units, towers, projectiles, and spells
//	PriorityQueue<Entity> entityDraw = new PriorityQueue<Entity>(50, eCompare);
//	ArrayList<Entity> changedEntity;
	static private SpriteBatch batch;
	static boolean showRange;
	static Map map;
	Hero playerHeroes[] = new Hero[2];
	Building playerBases[] = new Building[2];
	//Hero hero1, hero2;
//	long waveTimer, spawnTimer1, spawnTimer2, spawnInterval1, spawnInterval2;
//	long waveTime, waveInterval;
	int waveInterval, waveTime, waveTimer, previousTime, spawnTimer1, spawnTimer2, spawnInterval1, spawnInterval2;
//	int waveTime, waveInterval;
//	long totalTime = 0, previousTime;
	boolean spawning;
	int nano = 1000000000;
	int income = 100;
	int funds1 = 200, funds2 = 200;
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	Audio tempMusic = Gdx.audio;
	private Music music;
	static float musicVolume = 1f;
	byte team;
	boolean running = false;
	int turn = 0;
	float stepTime = 0.02f;
	
	int petType = 0;
	
	MinionStructure[][] playerUnits;// = new String[2][6]; 
	HeroStructure[] heroUnits;
	
	HashMap<String, MinionStructure> minionStats = new HashMap<String, MinionStructure>();
	HashMap<String, BuildingStructure> buildingStats = new HashMap<String, BuildingStructure>();
	HashMap<String, HeroStructure> heroStats = new HashMap<String, HeroStructure>();
	HashMap<String, ParticleEffect> particleEffects = new HashMap<String, ParticleEffect>();
	HashMap<String, SkillStructure> skillStats = new HashMap<String, SkillStructure>();
		
	public EverythingHolder()
	{
//		teams[0] = new LinkedList<Actor>();
//		teams[1] = new LinkedList<Actor>();
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
		
		UnitParser unitParser = new UnitParser();
		minionStats = unitParser.getMinionStats();
		buildingStats = unitParser.getBuildingStats();
		heroStats = unitParser.getHeroStats();
		
//		initializeHeroes();
		
		Entity.loadStatics(effects);
		// Wave control
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		waveInterval = 	(int) (1 / stepTime); 	// Turns (15 seconds)
		waveTime = 		(int) (2 / stepTime);	// Turns (2 seconds)
				
		spawning = false;
//		previousTime = System.nanoTime() / 1000000;
		Actor.loadProjectiles(projectiles);
		Entity.linkHolder(this);
		loadEffects();
//		music = tempMusic.newMusic(Gdx.files.internal("audio/506819_Xanax-amp-Bluebird3.wav"));
//		music.setLooping(true);
	}
	
	public void loadEffects()
	{
		ParticleEffect temp = new ParticleEffect();
		temp.load(Gdx.files.internal("data/fire.p"), Gdx.files.internal("images"));
		particleEffects.put("fire", temp);
	}
	
	public ParticleEffect getEffect(String e)
	{
		if (particleEffects.containsKey(e))
			return new ParticleEffect(particleEffects.get(e));
		return null;
	}
	
	public int winCondition()
	{
		if (!playerBases[0].isAlive())
			return 2;
		if (!playerBases[1].isAlive())
			return 1;
		return 0;
	}
	
	public void initializeHeroes()
	{
		playerUnits = new MinionStructure[][]{
				{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("monk"), minionStats.get("mage"), minionStats.get("ninja"), minionStats.get("eagle")}
			   ,{minionStats.get("swordsman"), minionStats.get("archer"), minionStats.get("monk"), minionStats.get("mage"), minionStats.get("ninja"), minionStats.get("elemental")}};
		heroUnits = new HeroStructure[]{heroStats.get("arroweyes"), heroStats.get("mrwizard")};

		playerHeroes[0] = new Hero(map.start1().x(), map.start1().y(), 1, map().getPath().listIterator(), heroUnits[0]);
		playerHeroes[1] = new Hero(map.start2().x(), map.start2().y(), 2, map().getReversePath().listIterator(), heroUnits[1]);
//		playerHeroes[1] = new Hero(map.start2().x(), map.start2().y(), 2, map().getPath().listIterator(map().getPath().size() - 1), heroUnits[1]);
//		add(playerHeroes[0], true, 1);
//		add(playerHeroes[1], true, 2);		
		add(playerHeroes[0], 1);
		add(playerHeroes[1], 2);
	}
	
	public void setHeroStance(int team, int s)
	{
		playerHeroes[team-1].stance(s);
	}
	
	public int turn()
	{
		return turn;
	}
	
	public void setTeam(byte team)
	{
		System.out.println("Team: " + team);
		this.team = team;
	}
	
	public void setRunning(boolean run)
	{
		running = run;
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
//		previousTime = System.nanoTime() / 1000000;
		waveTimer = 0;
		previousTime = 0;
	}
	
	public void upgradeTower(int tower, int team)
	{
//		for (Actor b : teams[team - 1])
//		{
//			if (b instanceof Building)
//			{
//				Building buil = (Building)b;
//				System.out.println("Building: " + tower + " Team: " + team);
//			}
//		}
		//((Building)teams[team].get(tower)).upgrade();
	}
	
	public void end()
	{
//		music.stop();
	}
	
	public void musicPlay()
	{
//		music.play();
	}
	
	static public void setMusicVolume(float v)
	{
		musicVolume = v;
	}
	
	public Actor atPoint(float x, float y)
	{
//		if (team == 0)
//			return null;
//		for (Actor a : teams[team - 1])// - 1])
//		{
//			if (a.getDistanceSquared(x, y) <= 1000)
//				return a;
//		}
		return null;
	}
	
	public int timeLeft()
	{
		if (!running)
			return 0;
		int left = (int) -((turn - waveTimer - waveInterval) * stepTime) + 1;
		return (left > 10 ? 10 : left);
//		return (int) ((waveInterval - waveTimer) / stepTime);
//		return (int) -((System.nanoTime() / 1000000 - waveTimer - waveInterval)/ 1000);
	}
	
	public String totalTime()
	{
		if (!running)
			return "00";
//		int time = (int) (totalTime / 1000);
//		int min = (int)(time / 60);
//		int sec = time % 60;
		
		int time = (int) (turn * stepTime);
		int min = (int)(time / 60);
		int sec = time % 60;
		return (min == 0 ? "00" : (min < 10 ? 0 + min : min)) + ":" + (sec == 0 ? "00" : (sec < 10 ? "0" + sec : sec));
		//return (int) totalTime / 1000;
	}
	
//	public void add(Actor a, boolean front, int team)
//	{
//		if (front)
//			teams[team - 1].add(0, a);
//		else
//			teams[team - 1].add(a);
//		if (a instanceof Building)
//		{
//			Building temp = (Building)a;
//			if (playerBases[temp.team()] == null)
//				playerBases[temp.team()] = temp;
//		}
//		entities.add(a);
//	}
	
	public void add(Entity a, int team)
	{
//		if (front)
//			teams[team - 1].add(0, a);
//		else
//			teams[team - 1].add(a);
		if (a instanceof Building)
		{
			if (playerBases[((Building)a).team() - 1] == null)
				playerBases[((Building)a).team() - 1] = (Building)a;
		}
		int loc = entities.indexOf(null);
		if (loc >= 0)
			entities.add(loc, a);
		else
			entities.add(a);
	}
	
	/*public void addHero(Hero h, int team)
	{
		if (team == 1)
			hero1 = h;
		else
			hero2 = h;
	}*/
	
	public void add(int unit, int team)
	{
		System.out.println("Trying to add " + unit + " on team " + team);
//		if (team == this.team)//(team == 0)
//		{
			if ((team == 1 ? funds1 : funds2) < 20)
				return;
			if (team == 1)
				funds1 -= 20;
			else
				funds2 -= 20;
			
			if (team == this.team)
				Gdx.input.vibrate(50);
//		}
//		else
//		{
//			if ((team == 1 ? funds2 : funds) < 20)
//				return;
//			if (team == 1)
//				funds1 -= 20;
//			else
//				funds2 -= 20;
//		}
		pools[team - 1].add(unit);
	}
	
	public void spawnPools()
	{
		for (int m : pools[0])
			spawnMob(m, 1);
		for (int m : pools[1])
			spawnMob(m, 2);
		
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
	}
	
	private void spawnPool(int team)
	{
		if (team == 1)
			spawnMob(pools[2].pop(), 1);
		else
			spawnMob(pools[3].pop(), 2);
	}
	
	private void spawnMob(int m, int team)
	{
		Coordinate start = (team == 1 ? map.start1() : map.start2());
		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getReversePath().listIterator());
		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team - 1][m], 0), team);
//		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getPath().listIterator(map.getPath().size() - 1));
//		int randX = 0; //(int)(Math.random() * 10);
//		int randY = 0; //(int)(Math.random() * 5);
		
//		add(new Minion(start.x(), start.y(), team, iter, playerUnits[team-1][m], 0), true, team);
//		if (m == 1)
//			add(new Swordsman(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 2)
//			add(new Archer(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 3)
//			add(new Monk(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 4)
//			add(new Mage(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 5)
//			add(new Ninja(start.x() + randX, start.y() + randY, team, iter), true, team);
//		else if (m == 6)
//		{
//			petType++;
//			if (petType > 2)
//				petType = 0;
//			if (petType == 0)
//				add(new Eagle(start.x() + randX, start.y() + randY, team, iter), true, team);
//			else if (petType == 1)
//				add(new Wolf(start.x() + randX, start.y() + randY, team, iter), true, team);
//			else
//				add(new Elemental(start.x() + randX, start.y() + randY, team, iter), true, team);
//		}
	}
	
	public ArrayList<Actor> team(int t)
	{
		ArrayList<Actor> temp = new ArrayList<Actor>();
		for (Entity a : entities)
		{
			if (a instanceof Actor)
			{
				if (t == 0 || ((Actor)a).team() == t)
					temp.add((Actor)a);
			}
		}
		return temp;
//		if (t == 0)
//		if (t == 1)
//			return teams[0];
//		return teams[1];
	}
	
	public void render()
	{
//		for (Actor a : teams[0])
//		{
//			if (a instanceof Stronghold)
//				a.draw(batch);
//		}
//		for (Actor a : teams[1])
//		{
//			if (a instanceof Stronghold)
//				a.draw(batch);
//		}
		for (Projectile p : projectiles)
			p.draw(batch);
		
		// Reordering changed entities for proper depth rendering
//		Iterator<Entity> entityIter = entities.iterator();
//		changedEntity = new ArrayList<Entity>();
//		for (Entity e : entities)
//			if (e.yCoordChanged())
//				changedEntity.add(e);
//		for (Entity e : changedEntity)
//		{
//			entities.remove(e);
//			entities.add(e);
//		}
		
		/*entityDraw.clear();
		for (Entity e : entities)
			entityDraw.add(e);
		
		while (entityDraw.peek() != null)
		{
			Entity e = entityDraw.remove();*/
		Collections.sort(entities, eCompare);
		
		Entity e;
		for (int i = 0; i < entities.size(); i++)
		{
			e = entities.get(i);
			if (e instanceof Building || (e instanceof Actor && ((Entity)e).isAlive()) || (e instanceof Skill && ((Entity)e).isAlive()))
				e.draw(batch);
			else if (!(e instanceof Hero))
			{
//				System.out.println("DEAD");
				entities.set(i, null);
			}
		}
		
//		while (entityIter.hasNext())
//		{
//			e = entityIter.next();
//			if (e instanceof Building || (e instanceof Actor && ((Actor)e).isAlive()))
//			{
//				e.draw(batch);
//			}
//		}
		
		
		
//		for (Entity e : entities)
//		{
////			if (e instanceof Actor && ((Actor)e).isAlive())
//			if (e instanceof Building || (e instanceof Actor && ((Actor)e).isAlive()))
//			{
//				e.draw(batch);
//			}
//			else if (!(e instanceof Hero))
//			{
//				
//			}
//		}
		
//		Iterator<Actor> actorIter = teams[0].iterator();
//		Actor a;
//		while (actorIter.hasNext())
//		{
//			a = actorIter.next();
//			if (a instanceof Stronghold)
//				continue;
//			if (a.isAlive() || (a instanceof ArrowTower))
//				a.draw(batch);
//			else if (!(a instanceof Hero))
//			{
//				//effects.add(a.blood());
//				a.destroy();
//				actorIter.remove();
//			}
//		}
		
		/*if (hero1 != null && hero1.isAlive())
			hero1.draw(batch);*/
		
//		actorIter = teams[1].iterator();
//		while (actorIter.hasNext())
//		{
//			a = actorIter.next();
//			if (a instanceof Stronghold)
//				continue;
//			if (a.isAlive() || (a instanceof ArrowTower))
//			{
//				a.draw(batch);
//			}
//			else if (!(a instanceof Hero))
//			{
//				//effects.add(a.blood());
//				a.destroy();
//				actorIter.remove();
//			}
//		}
		for (ParticleEffect pe : effects)
		{
			pe.draw(batch, 0.01f);
		}
		Iterator<ParticleEffect> iter = effects.iterator();
//		ParticleEffect e = new ParticleEffect();
		while (iter.hasNext())
		{
			if ((iter.next()).isComplete())
				iter.remove();
//			e = (ParticleEffect) iter.next();
//			if (e.isComplete())
//				iter.remove();
		}
		if (false)//showRange)
		{
			for (Entity en : entities)
			{
				if (en instanceof Actor)
				{
					if (((Actor)en).isAlive())
						((Actor)en).rangeIndicator(batch);
				}
			}
		}
//			for (Actor ac : teams[0])
//				if (ac.isAlive())
//					ac.rangeIndicator(batch);
//			for (Actor ac : teams[1])
//				if (ac.isAlive())
//					ac.rangeIndicator(batch);
//		}
	}
	
	public void update()
	{
//		for (Actor a : teams[0])
//			a.checkAlive();
//		for (Actor a : teams[1])
//			a.checkAlive();
		for (Entity e : entities)
		{
			if (e instanceof Actor)
				((Actor)e).checkAlive();
		}
		
		ArrayList<Projectile> removeList = new ArrayList<Projectile>();
		for (Projectile p : projectiles)
		{
				//if (p.xCoord == this.target.xCoord() || )
			p.update();
			if (p.getxSpeed() > 0 && p.xCoord() > p.target().xCoord())
				removeList.add(p);
			else if (p.getxSpeed() < 0 && p.xCoord() < p.target().xCoord())
				removeList.add(p);
			else if (p.getySpeed() > 0 && p.yCoord() > p.target().yCoord())
				removeList.add(p);
			else if (p.getySpeed() < 0 && p.yCoord() < p.target().yCoord())
				removeList.add(p);
		}
		
		projectiles.removeAll(removeList);
		
		/*if (hero1 != null)
			hero1.isAlive();
		if (hero2 != null)
			hero2.isAlive();*/
		
//		for (Actor a : teams[0])
//			if (a.isAlive())
//				a.update();
//		for (Actor a : teams[1])
//			if (a.isAlive())
//				a.update();
		for (int i = 0; i < entities.size(); i++)
		{
			if (entities.get(i) != null)
				entities.get(i).update();
		}
//		for (Entity e : entities)
//		{
//			if (e != null)
//			{
//				try
//				{
//				e.update();
//			}
//		}
		
		/*if (hero1 != null)
			hero1.update();
		if (hero2 != null)
			hero2.update();*/
		
//		music.setVolume(musicVolume);
		if (++turn % 200 == 0)
			System.out.println("Turn: " + turn);
		spawnTimers();
	}
	
	private void spawnTimers()
	{
		if (!running)
			return;
//		int currentTurn = turn++;
//		long currentTime = System.nanoTime() / 1000000;
		//totalTime += currentTime - previousTime;
		previousTime = turn;
		int timeDiff = turn - waveTimer;
		if ((timeDiff) > waveInterval)
//		if (currentTurn % waveInterval == 0)
		{
			spawning = true;
			waveTimer = turn;
			spawnTimer1 = waveTimer;
			
//			spawnInterval1 = (!pools[0].isEmpty() ? waveTime / pools[0].size() : waveInterval);
			spawnInterval1 = 4;	
			spawnTimer2 = waveTimer;
//			spawnInterval2 = (!pools[1].isEmpty() ? waveTime / pools[1].size() : waveInterval);
			spawnInterval2 = 4;
			pools[2] = pools[0];
			pools[3] = pools[1];
			pools[0] = new LinkedList<Integer>();
			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();
			
			if (!playerHeroes[0].isAlive())
				playerHeroes[0].respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
			if (!playerHeroes[1].isAlive())
				playerHeroes[1].respawn(map.start2().x(), map.start2().y(), map.getReversePath().listIterator());
			
//			for (Actor a : teams[0])
//				if (a instanceof Hero && !a.isAlive())
//					((Hero)a).respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
//			for (Actor a : teams[1])
//				if (a instanceof Hero && !a.isAlive())
//					((Hero)a).respawn(map.start2().x(), map.start2().y(), map.getReversePath().listIterator());
//					((Hero)a).respawn(map.start2().x(), map.start2().y(), map.getPath().listIterator(map.getPath().size() - 1));
//			if (Settings.getInstance().getDifficulty() == Difficulty.EASY)
//				funds += income;
//			else if (Settings.getInstance().getDifficulty() == Difficulty.HARD)
//				funds += income * .75;
			
			funds1 += income;
			funds2 += income;
			//Gdx.input.vibrate(1000);
		}
		
		/*if (spawning && pools[2].isEmpty() && pools[3].isEmpty())
			spawning = false;
		
		if (spawning)
		{*/
			if (!pools[2].isEmpty() && turn - spawnTimer1 > spawnInterval1)
			{
				spawnPool(1);
				spawnTimer1 = turn;
			}
			
			if (!pools[3].isEmpty() && turn - spawnTimer2 > spawnInterval2)
			{
				spawnPool(2);
				spawnTimer2 = turn;
			}
		//}
	}
	
	public Map map()
	{
		return map;
	}
	
	public int funds()
	{
		return team == 1 ? funds1 : funds2;
	}
	
	static public void toggleShowRange()
	{
		showRange = (showRange ? false : true);
	}
	
	public void load(SpriteBatch b, Map m)
	{
		batch = b;
		map = m;
//		initializeHeroes();
	}
}
