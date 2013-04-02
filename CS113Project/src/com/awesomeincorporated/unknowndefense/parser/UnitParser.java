package com.awesomeincorporated.unknowndefense.parser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.minlog.Log;


public class UnitParser 
{
	int[] maxHealth;
	int[] damage;
	int[] attackSpeed;
	int[] attackCoolDown;
	int[] attackRange;
	int[] cost;
	float[] speed;
	boolean ranged;
	String type;
	HashMap<String, MinionStructure> minionStatsMap = new HashMap<String, MinionStructure>();
	HashMap<String, BuildingStructure> buildingStatsMap  = new HashMap<String, BuildingStructure>();
	HashMap<String, HeroStructure> heroStatsMap  = new HashMap<String, HeroStructure>();
	HashMap<String, SkillStructure> skillStatsMap = new HashMap<String, SkillStructure>();
	String[] parsed = {"minion", "hero", "building"};//, "skill"};
	
	Element element;
	
	public static void main(String args[]) 
	{
		UnitParser unitParser = new UnitParser();
	}
	
	public UnitParser()
	{
		parseStats();
//		parseStats("minion");
//		parseStats("hero");
//		parseStats("building");
	}

//	public void parseStats(String entity) 
	public void parseStats()
	{
		try 
		{			
			//Prep the doc for parsing
			File stats;
			Log.info("Reading");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc;
			
			stats = new File("../Stats.xml");
			if (stats.length() < 1)
			{
//				System.out.println("Yeah");
				InputStream stream = Gdx.files.internal("data/Stats.xml").read();
				doc = dBuilder.parse(stream);
			}
			else
			{
				doc = dBuilder.parse(stats);
			}
			doc.getDocumentElement().normalize();

			//Begin parsing: pass the entity type that is to be parsed and start with creating node lists
			for (int i = 0; i < 1; i++)
			{
//				System.out.println("\n" + "\n" + doc.getDocumentElement().getNodeName() + ": " + parsed[i]);//entity);
//				System.out.println("==========================");
//				NodeList nodes = doc.getElementsByTagName(parsed[i]);
				NodeList nodes = doc.getElementsByTagName("*");
//				NodeList nodes = doc.getElementsByTagName(entity);
	
				//Iterate through each instance of the entity node list
				for (int j = 0; j < nodes.getLength(); j++) 
				{
					Node node = nodes.item(j);
//					System.out.println("\n" + node.getNodeName() + ": " + ((Element) node).getAttribute("type"));
	
					//From borrowed code.. I'm guessing this is so that you don't have to worry about exceptions.
					if (node.getNodeType() == Node.ELEMENT_NODE) 
					{
						element = (Element) node;
//						if (entity.equals("minion"))
//						if (parsed[i].equals("minion"))
						if (node.getNodeName().equals("minion"))
						{
							MinionStructure minion = new MinionStructure();
							minion.maxHealth = tokenizeInt(getElement("maxhealth"));
							minion.damage = tokenizeInt(getElement("damage"));
							minion.attackSpeed = tokenizeInt(getElement("attackspeed"));
							minion.attackRange = tokenizeInt(getElement("attackrange"));
							minion.speed = tokenizeFloat(getElement("speed"));
							minion.cost = tokenizeInt(getElement("cost"));
							minion.animation = tokenizeInt(getElement("animation"));
							minion.ranged = tokenizeBool(getElement("ranged"));
							minion.soundPack = tokenizeString(getElement("soundpack"));
							minion.passiveSkill = tokenizeString(getElement("passiveskill"));
							minionStatsMap.put(element.getAttribute("type"), minion);
						}
						else if (node.getNodeName().equals("hero"))
//						else if (parsed[i].equals("hero"))
//						else if (entity.equals("hero"))
						{
							HeroStructure hero = new HeroStructure();
							hero.maxHealth = tokenizeInt(getElement("maxhealth"));
							hero.damage = tokenizeInt(getElement("damage"));
							hero.attackSpeed = tokenizeInt(getElement("attackspeed"));
							hero.attackRange = tokenizeInt(getElement("attackrange"));
							hero.speed = tokenizeFloat(getElement("speed"));
							hero.cost = tokenizeInt(getElement("cost"));
							hero.animation = tokenizeInt(getElement("animation"));
							hero.ranged = tokenizeBool(getElement("ranged"));
							hero.soundPack = tokenizeString(getElement("soundpack"));
							hero.activeSkill = tokenizeString(getElement("activeskill"));
							hero.passiveSkill = tokenizeString(getElement("passiveskill"));
							heroStatsMap.put(element.getAttribute("type"), hero);
						}
						else if (node.getNodeName().equals("building"))
//						else if (parsed[i].equals("building"))
//						else if (entity.equals("building"))
						{
							BuildingStructure building = new BuildingStructure();
							building.maxHealth = tokenizeInt(getElement("maxhealth"));
							building.damage = tokenizeInt(getElement("damage"));
							building.attackSpeed = tokenizeInt(getElement("attackspeed"));
							building.attackRange = tokenizeInt(getElement("attackrange"));
							building.cost = tokenizeInt(getElement("cost"));
							building.animation = tokenizeInt(getElement("animation"));
							building.ranged = tokenizeBool(getElement("ranged"));
							building.passiveSkill = tokenizeString(getElement("passiveskill"));
							buildingStatsMap.put(element.getAttribute("type"), building);
						}
						else if (node.getNodeName().equals("skill"))
						{
							SkillStructure skill = new SkillStructure();
							skill.aoe = tokenizeInt(getElement("aoe"));
							skill.targetTeam = tokenizeInt(getElement("targetteam"));
							skill.effect = tokenizeInt(getElement("effect"));
							skill.effectAmount = tokenizeInt(getElement("effectamount"));
							skill.duration = tokenizeInt(getElement("duration"));
							skill.effectTick = tokenizeInt(getElement("effecttick"));
							skill.travelTime = tokenizeInt(getElement("traveltime"));
							skill.damageSplit = tokenizeBool(getElement("damagesplit"));
							skill.additive = tokenizeBool(getElement("additive"));
							skill.continuous = tokenizeBool(getElement("continuous"));
							skill.speed = tokenizeFloat(getElement("speed"));
							skill.sprite = tokenizeString(getElement("sprite"));
							skill.cast = tokenizeString(getElement("cast"));
							skill.travel = tokenizeString(getElement("travel"));
							skill.detonateEffect = tokenizeString(getElement("detonateeffect"));
							skill.affected = tokenizeString(getElement("affected"));
							skillStatsMap.put(element.getAttribute("type"), skill);
						}
					}
				}
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	public String getElement(String s)
	{
		return element.getElementsByTagName(s).item(0).getTextContent();
	}

	
	//Retrieve map of minion stats from parser
	public HashMap<String, MinionStructure> getMinionStats()
	{
		return minionStatsMap;
	}
	
	
	//Retrieve map of building stats from parser
	public HashMap<String, BuildingStructure> getBuildingStats()
	{
		return buildingStatsMap;
	}
	
	//Retrieve map of hero stats from parser
	public HashMap<String, HeroStructure> getHeroStats()
	{
		return heroStatsMap;
	}
	
	//Retrieve map of skill stats from parser
	public HashMap<String, SkillStructure> getSkillStats()
	{
		return skillStatsMap;
	}
	
	//Pulls and returns a particular string stat value 
	//given the tag name and instance or item number (i) of that stat value.
	private static String getValue(Element element) 
	{	
		return element.getNodeValue();
	}
	
	
	//Breaks down string values from the XML and storing them in and returning an int ArrayList. 
	//The strings may hold multiple stat values - up to one per level.	
	private static ArrayList<Integer> tokenizeInt(String values) 
	{
		StringTokenizer st = new StringTokenizer(values);
		ArrayList<Integer> stats = new ArrayList<Integer>();
		int level = 1;
		
		//Check if these are "int" type stats	
		while (st.hasMoreTokens()) 
		{
			int currentStat = Integer.parseInt(st.nextToken());
			stats.add(currentStat);
//			System.out.println("Level " + level + ": " + currentStat);
			level++;
		}
		return stats;
	}
		
	private static ArrayList<Float> tokenizeFloat(String values) 
	{
		StringTokenizer st = new StringTokenizer(values);
		ArrayList<Float> stats = new ArrayList<Float>();
		int level = 1;

		while (st.hasMoreTokens()) 
		{
			float currentStat = Float.parseFloat(st.nextToken());
			stats.add(currentStat);
//			System.out.println("Level " + level + ": " + currentStat);
			level++;
		}
		return stats;
	}
	
	private static ArrayList<Boolean> tokenizeBool(String values) 
	{	
		StringTokenizer st = new StringTokenizer(values);
		ArrayList<Boolean> stats = new ArrayList<Boolean>();
		int level = 1;
		
		//Check if these are "boolean" type stats
		//}else if(element.getAttribute("type") == "ranged"){
		while (st.hasMoreTokens()) 
		{
			boolean currentStat = Boolean.parseBoolean(st.nextToken());
			stats.add(currentStat);
//			System.out.println("Level " + level + ": " + currentStat);
			level++;
		}
		return stats;		
	}
	
	private static ArrayList<String> tokenizeString(String values) 
	{
		StringTokenizer st = new StringTokenizer(values);
		ArrayList<String> stats = new ArrayList<String>();
		int level = 1;

		while (st.hasMoreTokens()) 
		{
			String currentStat = st.nextToken();
			stats.add(currentStat);
//			System.out.println("Level " + level + ": " + currentStat);
			level++;
		}
		return stats;
	}
}
