package com.awesomeincorporated.unknowndefense;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import com.awesomeincorporated.unknowndefense.entity.*;
import com.awesomeincorporated.unknowndefense.map.Coordinate;
import com.awesomeincorporated.unknowndefense.map.Map;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EverythingHolder 
{
	@SuppressWarnings("unchecked")
	LinkedList<Actor>[] teams = new LinkedList[2];
	LinkedList<Integer>[] pools = new LinkedList[4];
	static private SpriteBatch batch;
	static boolean showRange;
	static Map map;
	//Hero hero1, hero2;
	long waveTimer, spawnTimer1, spawnTimer2, spawnInterval1, spawnInterval2;;
	long waveTime, waveInterval;
//	int waveTime, waveInterval;
	long totalTime = 0, previousTime;
	boolean spawning;
	int nano = 1000000000;
	int income = 100;
	int funds = 200;
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	Audio tempMusic = Gdx.audio;
	private Music music;
	static float musicVolume = 1f;
	byte team;
	boolean running = false;
	int turn = 0;
		
	public EverythingHolder()
	{
		teams[0] = new LinkedList<Actor>();
		teams[1] = new LinkedList<Actor>();
		pools[0] = new LinkedList<Integer>();
		pools[1] = new LinkedList<Integer>();
		pools[2] = new LinkedList<Integer>();
		pools[3] = new LinkedList<Integer>();
		
		Entity.loadStatics(effects);
		// Wave control
//		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		waveInterval = 10000;// // Milliseconds between waves
		
		waveTime = 2000;//// Milliseconds to spawn a wave
				
		spawning = false;
//		previousTime = System.nanoTime() / 1000000;
		Actor.loadProjectiles(projectiles);
		
//		music = tempMusic.newMusic(Gdx.files.internal("audio/506819_Xanax-amp-Bluebird3.wav"));
//		music.setLooping(true);
	}
	
	public void setTeam(byte team)
	{
		this.team = team;
	}
	
	public void setRunning(boolean run)
	{
		running = run;
		waveTimer = System.nanoTime() / 1000000; // Timer to keep track of waves
		previousTime = System.nanoTime() / 1000000;
	}
	
	public void upgradeTower(int tower, int team)
	{
		for (Actor b : teams[team - 1])
		{
			if (b instanceof Building)
			{
				Building buil = (Building)b;
				System.out.println("Building: " + tower + " Team: " + team);
			}
		}
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
		for (Actor a : teams[team])// - 1])
		{
			if (a.getDistanceSquared(x, y) <= 1000)
				return a;
		}
		return null;
	}
	
	public int timeLeft()
	{
		if (!running)
			return 0;
		return (int) -((System.nanoTime() / 1000000 - waveTimer - waveInterval)/ 1000);
	}
	
	public String totalTime()
	{
		if (!running)
			return "00";
		int time = (int) (totalTime / 1000);
		int min = (int)(time / 60);
		int sec = time % 60;
		return (min == 0 ? "00" : (min < 10 ? 0 + min : min)) + ":" + (sec == 0 ? "00" : (sec < 10 ? "0" + sec : sec));
		//return (int) totalTime / 1000;
	}
	
	public void add(Actor a, boolean front, int team)
	{
		if (front)
			teams[team - 1].add(0, a);
		else
			teams[team - 1].add(a);		
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
		if (team == this.team)//(team == 0)
		{
			if (funds < 20)
				return;
			funds -= 20;
			Gdx.input.vibrate(50);
		}
		pools[team].add(unit);
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
		ListIterator<Coordinate> iter = (team == 1 ? map.getPath().listIterator() : map.getPath().listIterator(map.getPath().size() - 1));
		int randX = 0; //(int)(Math.random() * 10);
		int randY = 0; //(int)(Math.random() * 5);
		
		if (m == 1)
			add(new Swordsman(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 2)
			add(new Archer(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 3)
			add(new Monk(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 4)
			add(new Mage(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 5)
			add(new Ninja(start.x() + randX, start.y() + randY, team, iter), true, team);
		else if (m == 6)
			add(new Eagle(start.x() + randX, start.y() + randY, team, iter), true, team);
	}
	
	public LinkedList<Actor> team(int t)
	{
		if (t == 1)
			return teams[0];
		return teams[1];
	}
	
	public void render()
	{
		for (Actor a : teams[0])
		{
			if (a instanceof Stronghold)
				a.draw(batch);
		}
		for (Actor a : teams[1])
		{
			if (a instanceof Stronghold)
				a.draw(batch);
		}
		for (Projectile p : projectiles)
			p.draw(batch);
		
		Iterator<Actor> actorIter = teams[0].iterator();
		Actor a;
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a instanceof Stronghold)
				continue;
			if (a.isAlive() || (a instanceof ArrowTower))
				a.draw(batch);
			else if (!(a instanceof Hero))
			{
				//effects.add(a.blood());
				a.destroy();
				actorIter.remove();
			}
		}
		/*if (hero1 != null && hero1.isAlive())
			hero1.draw(batch);*/
		
		actorIter = teams[1].iterator();
		while (actorIter.hasNext())
		{
			a = actorIter.next();
			if (a instanceof Stronghold)
				continue;
			if (a.isAlive() || (a instanceof ArrowTower))
			{
				a.draw(batch);
			}
			else if (!(a instanceof Hero))
			{
				//effects.add(a.blood());
				a.destroy();
				actorIter.remove();
			}
		}
		for (ParticleEffect e : effects)
		{
			e.draw(batch, 0.01f);
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
			for (Actor ac : teams[0])
				if (ac.isAlive())
					ac.rangeIndicator(batch);
			for (Actor ac : teams[1])
				if (ac.isAlive())
					ac.rangeIndicator(batch);
		}
	}
	
	public void update()
	{
		for (Actor a : teams[0])
			a.checkAlive();
		for (Actor a : teams[1])
			a.checkAlive();
		
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
		
		for (Actor a : teams[0])
			if (a.isAlive())
				a.update();
		for (Actor a : teams[1])
			if (a.isAlive())
				a.update();
		
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
		long currentTime = System.nanoTime() / 1000000;
		totalTime += currentTime - previousTime;
		previousTime = currentTime;
		long timeDiff = currentTime - waveTimer;
		if ((timeDiff) > waveInterval)
//		if (currentTurn % waveInterval == 0)
		{
			spawning = true;
			waveTimer = currentTime;
			spawnTimer1 = waveTimer;
			
			spawnInterval1 = (!pools[0].isEmpty() ? waveTime / pools[0].size() : 10000);
				
			spawnTimer2 = waveTimer;
			spawnInterval2 = (!pools[1].isEmpty() ? waveTime / pools[1].size() : 10000);
			
			pools[2] = pools[0];
			pools[3] = pools[1];
			pools[0] = new LinkedList<Integer>();
			pools[1] = new LinkedList<Integer>();
			if (pools[2] == null)
				pools[2] = new LinkedList<Integer>();
			if (pools[3] == null)
				pools[3] = new LinkedList<Integer>();
			
			for (Actor a : teams[0])
				if (a instanceof Hero && !a.isAlive())
					((Hero)a).respawn(map.start1().x(), map.start1().y(), map.getPath().listIterator());
			for (Actor a : teams[1])
				if (a instanceof Hero && !a.isAlive())
					((Hero)a).respawn(map.start2().x(), map.start2().y(), map.getPath().listIterator(map.getPath().size() - 1));
			if (Settings.getInstance().getDifficulty() == Difficulty.EASY)
				funds += income;
			else if (Settings.getInstance().getDifficulty() == Difficulty.HARD)
				funds += income * .75;
			//Gdx.input.vibrate(1000);
		}
		
		/*if (spawning && pools[2].isEmpty() && pools[3].isEmpty())
			spawning = false;
		
		if (spawning)
		{*/
			if (!pools[2].isEmpty() && currentTime - spawnTimer1 > spawnInterval1)
			{
				spawnPool(1);
				spawnTimer1 = currentTime;
			}
			
			if (!pools[3].isEmpty() && currentTime - spawnTimer2 > spawnInterval2)
			{
				spawnPool(2);
				spawnTimer2 = currentTime;
			}
		//}
	}
	
	public Map map()
	{
		return map;
	}
	
	public int funds()
	{
		return funds;
	}
	
	static public void toggleShowRange()
	{
		showRange = (showRange ? false : true);
	}
	
	static void load(SpriteBatch b, Map m)
	{
		batch = b;
		map = m;
	}
}
