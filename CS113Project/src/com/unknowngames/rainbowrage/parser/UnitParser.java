package com.unknowngames.rainbowrage.parser;

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
	String version;
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
	HashMap<String, SkillContainerStructure> skillContainerStatsMap = new HashMap<String, SkillContainerStructure>();
	String[] parsed = {"minion", "hero", "building"};//, "skill"};
	
	Element element;
	
	boolean external = false;
	
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
				external = true;
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
						
						if (node.getNodeName().equals("minion"))
						{
							MinionStructure minion = new MinionStructure();
							loadActorStructure(minion);
//							minion.maxHealth = tokenizeInt(getElement("maxhealth"));
//							minion.damage = tokenizeInt(getElement("damage"));
//							minion.attackSpeed = tokenizeInt(getElement("attackspeed"));
//							minion.attackRange = tokenizeInt(getElement("attackrange"));
							minion.speed = tokenizeFloat(getElement("speed"));
//							minion.cost = tokenizeInt(getElement("cost"));
//							minion.radius = tokenizeInt(getElement("radius")).get(0);
//							minion.physicalResist = tokenizeInt(getElement("physicalresist"));
//							minion.rangedResist = tokenizeInt(getElement("rangedresist"));
//							minion.magicResist = tokenizeInt(getElement("magicresist"));
//							minion.animation = tokenizeString(getElement("animation"));
//							minion.ranged = tokenizeBool(getElement("ranged"));
//							minion.projectile = tokenizeString(getElement("projectile"));
//							minion.soundPack = tokenizeString(getElement("soundpack"));
//							minion.firstSkill = tokenizeString(getElement("firstskill"));
//							minion.secondSkill = tokenizeString(getElement("secondskill"));
//							minion.thirdSkill = tokenizeString(getElement("thirdskill"));
//							minion.passiveSkill = tokenizeString(getElement("passiveskill"));
//							minion.procSkill = tokenizeString(getElement("procskill"));
							minionStatsMap.put(element.getAttribute("type"), minion);
						}
						else if (node.getNodeName().equals("hero"))
						{
							HeroStructure hero = new HeroStructure();
							loadActorStructure(hero);
//							hero.maxHealth = tokenizeInt(getElement("maxhealth"));
//							hero.damage = tokenizeInt(getElement("damage"));
//							hero.attackSpeed = tokenizeInt(getElement("attackspeed"));
//							hero.attackRange = tokenizeInt(getElement("attackrange"));
							hero.speed = tokenizeFloat(getElement("speed"));
//							hero.cost = tokenizeInt(getElement("cost"));
//							hero.radius = tokenizeInt(getElement("radius")).get(0);
//							hero.physicalResist = tokenizeInt(getElement("physicalresist"));
//							hero.rangedResist = tokenizeInt(getElement("rangedresist"));
//							hero.magicResist = tokenizeInt(getElement("magicresist"));
//							hero.animation = tokenizeString(getElement("animation"));
//							hero.ranged = tokenizeBool(getElement("ranged"));
//							hero.projectile = tokenizeString(getElement("projectile"));
//							hero.soundPack = tokenizeString(getElement("soundpack"));
//							hero.firstSkill = tokenizeString(getElement("firstskill"));
//							hero.secondSkill = tokenizeString(getElement("secondskill"));
//							hero.thirdSkill = tokenizeString(getElement("thirdskill"));
							hero.activeSkill = tokenizeString(getElement("activeskill"));
							hero.pet = tokenizeString(getElement("pet"));
							heroStatsMap.put(element.getAttribute("type"), hero);
						}
						else if (node.getNodeName().equals("building"))
						{
							BuildingStructure building = new BuildingStructure();
							loadActorStructure(building);
//							building.maxHealth = tokenizeInt(getElement("maxhealth"));
//							building.damage = tokenizeInt(getElement("damage"));
//							building.attackSpeed = tokenizeInt(getElement("attackspeed"));
//							building.attackRange = tokenizeInt(getElement("attackrange"));
//							building.cost = tokenizeInt(getElement("cost"));
//							building.radius = tokenizeInt(getElement("radius")).get(0);
//							building.physicalResist = tokenizeInt(getElement("physicalresist"));
//							building.rangedResist = tokenizeInt(getElement("rangedresist"));
//							building.magicResist = tokenizeInt(getElement("magicresist"));
//							building.animation = tokenizeString(getElement("animation"));
//							building.ranged = tokenizeBool(getElement("ranged"));
//							building.projectile = tokenizeString(getElement("projectile"));
//							building.soundPack = tokenizeString(getElement("soundpack"));
//							building.firstSkill = tokenizeString(getElement("firstskill"));
//							building.secondSkill = tokenizeString(getElement("secondskill"));
//							building.thirdSkill = tokenizeString(getElement("thirdskill"));
//							building.passiveSkill = tokenizeString(getElement("passiveskill"));
//							building.procSkill = tokenizeString(getElement("procskill"));
							buildingStatsMap.put(element.getAttribute("type"), building);
						}
						else if (node.getNodeName().equals("skill"))
						{
							SkillStructure skill = new SkillStructure();
							skill.aoe = tokenizeInt(getElement("aoe")).get(0);
							skill.targeting = tokenizeInt(getElement("targeting")).get(0);
							skill.targetCount = tokenizeInt(getElement("targetcount")).get(0);
							skill.effect = tokenizeInt(getElement("effect")).get(0);
							skill.effectAmount = tokenizeInt(getElement("effectamount")).get(0);
							skill.duration = tokenizeInt(getElement("duration")).get(0);
							skill.effectTick = tokenizeInt(getElement("effecttick")).get(0);
							skill.buff = tokenizeInt(getElement("buff")).get(0);
							skill.trigger = tokenizeInt(getElement("trigger")).get(0);
							skill.priority = tokenizeInt(getElement("priority")).get(0);
							skill.damageSplit = tokenizeBool(getElement("damagesplit")).get(0);
							skill.additive = tokenizeBool(getElement("additive")).get(0);
							skill.continuous = tokenizeBool(getElement("continuous")).get(0);
							skill.travelEffect = tokenizeString(getElement("traveleffect")).get(0);
							skill.detonateEffect = tokenizeString(getElement("detonateeffect")).get(0);
							skill.affectedEffect = tokenizeString(getElement("affectedeffect")).get(0);
							skillStatsMap.put(element.getAttribute("type"), skill);
						}
						/*else if (node.getNodeName().equals("skill"))
						{
							SkillStructure skill = new SkillStructure();
							skill.aoe = tokenizeInt(getElement("aoe"));
							skill.targeting = tokenizeInt(getElement("targeting"));
							skill.targetCount = tokenizeInt(getElement("targetcount"));
							skill.targetRange = tokenizeInt(getElement("targetrange"));
							skill.effect = tokenizeInt(getElement("effect"));
							skill.effectAmount = tokenizeInt(getElement("effectamount"));
							skill.duration = tokenizeInt(getElement("duration"));
							skill.effectTick = tokenizeInt(getElement("effecttick"));
							skill.cooldown = tokenizeInt(getElement("cooldown"));
							skill.trigger = tokenizeInt(getElement("trigger"));
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
						}*/
						else if (node.getNodeName().equals("skillcontainer") &&	// Instant skill container
								tokenizeInt(getElement("traveltime")).get(0) == -1) 
						{
							InstantSkillContainerStructure skillcontainer = new InstantSkillContainerStructure();
							loadSkillContainerStructure(skillcontainer);
//							skillcontainer.targetType = tokenizeInt(getElement("targettype")).get(0);
//							skillcontainer.targetRange = tokenizeInt(getElement("targetrange")).get(0);
//							skillcontainer.cooldown = tokenizeInt(getElement("cooldown")).get(0);
//							skillcontainer.trigger = tokenizeInt(getElement("trigger")).get(0);
//							skillcontainer.skills = tokenizeString(getElement("skills"));
//							skillcontainer.cost = tokenizeInt(getElement("cost")).get(0);
//							skillcontainer.relativeX = tokenizeInt(getElement("relativex")).get(0);
//							skillcontainer.relativeY = tokenizeInt(getElement("relativey")).get(0);
//							skillcontainer.castEffect = tokenizeString(getElement("casteffect")).get(0);
//							skillcontainer.castSound = tokenizeString(getElement("castsound")).get(0);
							skillContainerStatsMap.put(element.getAttribute("type"), skillcontainer);
						}
						else if (node.getNodeName().equals("skillcontainer"))	// Traveling skill container
						{
							System.out.println(getElement("traveltime"));
							TravelingSkillContainerStructure skillcontainer = new TravelingSkillContainerStructure();
							loadSkillContainerStructure(skillcontainer);
//							skillcontainer.targetType = tokenizeInt(getElement("targettype")).get(0);
//							skillcontainer.targetRange = tokenizeInt(getElement("targetrange")).get(0);
//							skillcontainer.cooldown = tokenizeInt(getElement("cooldown")).get(0);
//							skillcontainer.trigger = tokenizeInt(getElement("trigger")).get(0);
//							skillcontainer.skills = tokenizeString(getElement("skills"));
//							skillcontainer.cost = tokenizeInt(getElement("cost")).get(0);
//							skillcontainer.relativeX = tokenizeInt(getElement("relativex")).get(0);
//							skillcontainer.relativeY = tokenizeInt(getElement("relativey")).get(0);
//							skillcontainer.castEffect = tokenizeString(getElement("casteffect")).get(0);
//							skillcontainer.castSound = tokenizeString(getElement("castsound")).get(0);
							skillcontainer.travelTime = tokenizeInt(getElement("traveltime")).get(0);
							skillcontainer.speed = tokenizeFloat(getElement("speed")).get(0);
							skillcontainer.skillSprite = tokenizeString(getElement("skillsprite")).get(0);
							skillcontainer.travelEffect = tokenizeString(getElement("traveleffect")).get(0);
							skillContainerStatsMap.put(element.getAttribute("type"), skillcontainer);
						}
						else if (node.getNodeName().equals("stats"))
						{
							version = element.getAttribute("version") + (external ? "_external" : "");
							System.out.println(version);
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
	
	public void loadActorStructure(ActorStructure a)
	{
		a.maxHealth = tokenizeInt(getElement("maxhealth"));
		a.damage = tokenizeInt(getElement("damage"));
		a.attackSpeed = tokenizeInt(getElement("attackspeed"));
		a.attackRange = tokenizeInt(getElement("attackrange"));
		a.cost = tokenizeInt(getElement("cost"));
		a.radius = tokenizeInt(getElement("radius")).get(0);
		a.physicalResist = tokenizeInt(getElement("physicalresist"));
		a.rangedResist = tokenizeInt(getElement("rangedresist"));
		a.magicResist = tokenizeInt(getElement("magicresist"));
		a.animation = tokenizeString(getElement("animation"));
		a.ranged = tokenizeBool(getElement("ranged"));
		a.projectile = tokenizeString(getElement("projectile"));
		a.soundPack = tokenizeString(getElement("soundpack"));
		a.firstSkill = tokenizeString(getElement("firstskill"));
		a.secondSkill = tokenizeString(getElement("secondskill"));
		a.thirdSkill = tokenizeString(getElement("thirdskill"));
	}
	
	public void loadSkillContainerStructure(SkillContainerStructure s)
	{
		s.targetType = tokenizeInt(getElement("targettype")).get(0);
		s.targetRange = tokenizeInt(getElement("targetrange")).get(0);
		s.cooldown = tokenizeInt(getElement("cooldown")).get(0);
		s.trigger = tokenizeInt(getElement("trigger")).get(0);
		s.skills = tokenizeString(getElement("skills"));
		s.cost = tokenizeInt(getElement("cost")).get(0);
		s.relativeX = tokenizeInt(getElement("relativex")).get(0);
		s.relativeY = tokenizeInt(getElement("relativey")).get(0);
		s.castEffect = tokenizeString(getElement("casteffect")).get(0);
		s.castSound = tokenizeString(getElement("castsound")).get(0);
		s.name = getElement("name").trim();
		s.description = getElement("description").trim();
		s.icon = getElement("icon").trim();
	}
	
	public String getVersion()
	{
		return version;
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
	
	//Retrieve map of skill container stats from parser
	public HashMap<String, SkillContainerStructure> getSkillContainerStats()
	{
		return skillContainerStatsMap;
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
