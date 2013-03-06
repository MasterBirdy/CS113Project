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
		unitParser.parseStats("minion");
		unitParser.parseStats("building");
	}
	
	public UnitParser()
	{
		parseStats("minion");
		parseStats("building");
	}

	public void parseStats(String entity) 
	{
		try 
		{
			//Create new instances to build and store in the hashmap
			MinionStructure minion = new MinionStructure();
			BuildingStructure building = new BuildingStructure();
			
			//Prep the doc for parsing
			File stats = new File("../Stats.xml");
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
					NodeList subNodes = node.getChildNodes();
					
					//Iterate through the entity's stat node list
					for (int n = 0; n < subNodes.getLength(); n++) 
					{
						Node subNode = subNodes.item(n);
						
						if (subNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element subElement = (Element) subNode;
							System.out.println(subNode.getNodeName() + ": " + ((Element) subNode).getAttribute("type"));
							
							//Store minion values
							if (entity.equals("minion"))
							{
								if (subElement.getAttribute("type").equals("maxhealth")) 
								{
									minion.maxHealth = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("damage"))
								{
									minion.damage = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackspeed"))
								{
									minion.attackSpeed = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackcooldown"))
								{
									minion.attackCoolDown = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackrange"))
								{
									minion.attackRange = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("speed"))
								{
									minion.speed = tokenizeFloat(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("cost"))
								{
									minion.cost = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("ranged"))
								{
									minion.ranged = tokenizeBool(subNode.getFirstChild().getNodeValue(), subElement);
								}
								
							//Store building values
							}
							else if (entity.equals("building"))
							{
								if (subElement.getAttribute("type").equals("maxhealth")) 
								{
									building.maxHealth = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("damage"))
								{
									building.damage = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackspeed"))
								{
									building.attackSpeed = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackcooldown"))
								{
									building.attackCoolDown = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("attackrange"))
								{
									building.attackRange = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("cost"))
								{
									building.cost = tokenizeInt(subNode.getFirstChild().getNodeValue(), subElement);
								}
								else if (subElement.getAttribute("type").equals("ranged"))
								{
									building.ranged = tokenizeBool(subNode.getFirstChild().getNodeValue(), subElement);
								}
							}
						}
					}
				}
				
				//Store in the HashMap
				if (entity.equals("minion"))
				{
					minionStatsMap.put(((Element) node).getAttribute("type"), minion);
				}
				else if (entity.equals("building"))
				{
					buildingStatsMap.put(((Element) node).getAttribute("type"), building);
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
	private static ArrayList<Integer> tokenizeInt(String values, Element element) 
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
		
	private static ArrayList<Float> tokenizeFloat(String values, Element element) 
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
	
	private static ArrayList<Boolean> tokenizeBool(String values, Element element) 
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
