package com.awesomeincorporated.unknowndefense.parser;

import java.io.File;
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
	
	public static void main(String args[]) 
	{
		UnitParser unitParser = new UnitParser();
	}
	
	public UnitParser()
	{
		parseStats("minion");
		parseStats("hero");
		parseStats("building");
	}

	public void parseStats(String entity) 
	{
		try 
		{			
			//Prep the doc for parsing
			File stats;
			stats = new File("../Stats.xml");
			if (stats.length() < 1)
			{
				System.out.println("Yeah");
				
//				stats = new File("assets/Stats.xml");
//				FileHandle stats1 = Gdx.files.internal("Stats.xml");
				stats = Gdx.files.local("assets/Stats.xml").file();
//				System.out.println(stats1.toString());
//				stats = stats1.file();
//				stats = new Gdx.files.internal("data/Stats.xml");//new File("Stats.xml");
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stats);
			doc.getDocumentElement().normalize();

			//Begin parsing: pass the entity type that is to be parsed and start with creating node lists
			System.out.println("\n" + "\n" + doc.getDocumentElement().getNodeName() + ": " + entity);
			System.out.println("==========================");
			NodeList nodes = doc.getElementsByTagName(entity);

			//Iterate through each instance of the entity node list
			for (int i = 0; i < nodes.getLength(); i++) 
			{
				Node node = nodes.item(i);
				System.out.println("\n" + node.getNodeName() + ": " + ((Element) node).getAttribute("type"));

				//From borrowed code.. I'm guessing this is so that you don't have to worry about exceptions.
				if (node.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element element = (Element) node;
					if (entity.equals("minion"))
					{
						MinionStructure minion = new MinionStructure();
						minion.maxHealth = tokenizeInt(element.getElementsByTagName("maxhealth").item(0).getTextContent());
						minion.damage = tokenizeInt(element.getElementsByTagName("damage").item(0).getTextContent());
						minion.attackSpeed = tokenizeInt(element.getElementsByTagName("attackspeed").item(0).getTextContent());
						minion.attackRange = tokenizeInt(element.getElementsByTagName("attackrange").item(0).getTextContent());
						minion.speed = tokenizeFloat(element.getElementsByTagName("speed").item(0).getTextContent());
						minion.cost = tokenizeInt(element.getElementsByTagName("cost").item(0).getTextContent());
						minion.animation = tokenizeInt(element.getElementsByTagName("animation").item(0).getTextContent());
						minion.ranged = tokenizeBool(element.getElementsByTagName("ranged").item(0).getTextContent());
						minionStatsMap.put(element.getAttribute("type"), minion);
					}
					else if (entity.equals("hero"))
					{
						HeroStructure hero = new HeroStructure();
						hero.maxHealth = tokenizeInt(element.getElementsByTagName("maxhealth").item(0).getTextContent());
						hero.damage = tokenizeInt(element.getElementsByTagName("damage").item(0).getTextContent());
						hero.attackSpeed = tokenizeInt(element.getElementsByTagName("attackspeed").item(0).getTextContent());
						hero.attackRange = tokenizeInt(element.getElementsByTagName("attackrange").item(0).getTextContent());
						hero.speed = tokenizeFloat(element.getElementsByTagName("speed").item(0).getTextContent());
						hero.cost = tokenizeInt(element.getElementsByTagName("cost").item(0).getTextContent());
						hero.animation = tokenizeInt(element.getElementsByTagName("animation").item(0).getTextContent());
						hero.ranged = tokenizeBool(element.getElementsByTagName("ranged").item(0).getTextContent());
						heroStatsMap.put(element.getAttribute("type"), hero);
					}
					else if (entity.equals("building"))
					{
						BuildingStructure building = new BuildingStructure();
						building.maxHealth = tokenizeInt(element.getElementsByTagName("maxhealth").item(0).getTextContent());
						building.damage = tokenizeInt(element.getElementsByTagName("damage").item(0).getTextContent());
						building.attackSpeed = tokenizeInt(element.getElementsByTagName("attackspeed").item(0).getTextContent());
						building.attackRange = tokenizeInt(element.getElementsByTagName("attackrange").item(0).getTextContent());
						building.cost = tokenizeInt(element.getElementsByTagName("cost").item(0).getTextContent());
						building.animation = tokenizeInt(element.getElementsByTagName("animation").item(0).getTextContent());
						building.ranged = tokenizeBool(element.getElementsByTagName("ranged").item(0).getTextContent());
						buildingStatsMap.put(element.getAttribute("type"), building);
					}
				}
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
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
	
	//Retrieve map of building stats from parser
	public HashMap<String, HeroStructure> getHeroStats()
	{
		return heroStatsMap;
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
			System.out.println("Level " + level + ": " + currentStat);
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
			System.out.println("Level " + level + ": " + currentStat);
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
			System.out.println("Level " + level + ": " + currentStat);
			level++;
		}
		return stats;		
	}	
}
