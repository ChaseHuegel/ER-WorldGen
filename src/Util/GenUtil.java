package Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.PointedDripstone.Thickness;
import org.bukkit.loot.LootTables;

import Structures.StructureData;
import Structures.StructureUtil;
import er.seven.worldgen.Main;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class GenUtil 
{
	private static GenUtil instance;
	public static GenUtil Instance() { if (instance == null) instance = new GenUtil(); return instance; }
	
	public HashMap<String, List<StructureData>> treeStructures = new HashMap<String, List<StructureData>>();
	public HashMap<String, List<StructureData>> rockStructures = new HashMap<String, List<StructureData>>();
	public HashMap<String, List<StructureData>> objectStructures = new HashMap<String, List<StructureData>>();
	
	public List<Material> treeBlacklist = new ArrayList<Material>();
	
	public GenUtil()
	{
		//	Trees cant overwrite these materials
		treeBlacklist.add(Material.OAK_LOG);
		treeBlacklist.add(Material.SPRUCE_LOG);
		treeBlacklist.add(Material.BIRCH_LOG);
		treeBlacklist.add(Material.JUNGLE_LOG);
		treeBlacklist.add(Material.ACACIA_LOG);
		treeBlacklist.add(Material.DARK_OAK_LOG);
		treeBlacklist.add(Material.OAK_WOOD);
		treeBlacklist.add(Material.SPRUCE_WOOD);
		treeBlacklist.add(Material.BIRCH_WOOD);
		treeBlacklist.add(Material.JUNGLE_WOOD);
		treeBlacklist.add(Material.ACACIA_WOOD);
		treeBlacklist.add(Material.DARK_OAK_WOOD);
		
		LoadStructuresFrom("trees", treeStructures);
		LoadStructuresFrom("rocks", rockStructures);
		LoadStructuresFrom("objects", objectStructures);
	}
	
	public void LoadStructuresFrom(String subfolder, HashMap<String, List<StructureData>> hashmap)
	{
		File dir = new File("plugins/EternalRealms-WorldGen/" + subfolder + "/");
		String[] files = dir.list();
		Main.Instance().getServer().getConsoleSender().sendMessage("Loading structures in /plugins/EternalRealms-WorldGen/" + subfolder + "/");
		
		List<StructureData> loadedStructures = new ArrayList<StructureData>();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].contains(".yml") == false) 
			{
				String[] subFiles = (new File("plugins/EternalRealms-WorldGen/" + subfolder + "/" + files[i] + "/")).list();
				for (int n = 0; n < subFiles.length; n++)
				{
					if (subFiles[n].contains(".yml") == true)
					{
						Main.Instance().getServer().getConsoleSender().sendMessage("       Found structure: " + subFiles[n] + " in " + files[i]);
						loadedStructures.add( StructureUtil.LoadStructureFrom("plugins/EternalRealms-WorldGen/" + subfolder + "/" + files[i] + "/" + subFiles[n], files[i]) );
					}
				}
			}
			else
			{
				Main.Instance().getServer().getConsoleSender().sendMessage("       Found structure: " + files[i] + " in " + subfolder);
				loadedStructures.add( StructureUtil.LoadStructureFrom("plugins/EternalRealms-WorldGen/" + subfolder + "/" + files[i], files[i]) );
			}
		}
		
		Main.Instance().getServer().getConsoleSender().sendMessage("              Structures loaded: " + loadedStructures.size());

		//	Fill the hashmap
		for (int i = 0; i < loadedStructures.size(); i++)
		{
			if (hashmap.get(loadedStructures.get(i).category) == null)
			{
				//	Create this category if it doesn't exist
				hashmap.put(loadedStructures.get(i).category, new ArrayList<StructureData>());
			}
			
			hashmap.get(loadedStructures.get(i).category).add(loadedStructures.get(i));
		}
	}
	
	public static void GrowTree(Random rand, DecorationArea area, int x, int y, int z, String category, World world)
	{
		StructureUtil.PasteStructureSafe( Instance().treeStructures.get(category).get( rand.nextInt(Instance().treeStructures.get(category).size()) ), x, y, z, area, world);
	}
	
	public static void GenerateTree(Random rand, DecorationArea area, int x, int y, int z, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().treeStructures.get(category).get( rand.nextInt(Instance().treeStructures.get(category).size()) ), x, y, z, area, world, Instance().treeBlacklist);
	}
	
	public static void GrowTree(Random rand, Location loc, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().treeStructures.get(category).get( rand.nextInt(Instance().treeStructures.get(category).size()) ), loc, false);
	}
	
	public static void GenerateTree(Random rand, Location loc, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().treeStructures.get(category).get( rand.nextInt(Instance().treeStructures.get(category).size()) ), loc, true);
	}
	
	public static void GenerateTree(Random rand, Block block, String category, World world)
	{
		GenerateTree(rand, block.getLocation(), category, world);
	}
	
	public static void GenerateRock(Random rand, DecorationArea area, int x, int y, int z, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().rockStructures.get(category).get( rand.nextInt(Instance().rockStructures.get(category).size()) ), x, y, z, area, world, Instance().treeBlacklist);
	}
	
	public static void GenerateRock(Random rand, Location loc, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().rockStructures.get(category).get( rand.nextInt(Instance().rockStructures.get(category).size()) ), loc);
	}
	
	public static void GenerateRock(Random rand, Block block, String category, World world)
	{
		GenerateTree(rand, block.getLocation(), category, world);
	}
	
	public static void GenerateObject(Random rand, DecorationArea area, int x, int y, int z, String category, World world)
	{
		StructureUtil.PasteStructure( Instance().objectStructures.get(category).get( rand.nextInt(Instance().objectStructures.get(category).size()) ), x, y, z, area, world, Instance().treeBlacklist);
	}
	
	public static void GenerateLog(Random rand, DecorationArea area, int x, int y, int z, Material log)
	{
		Orientable data;
		switch ( rand.nextInt(2) )
		{
		case 0:
			area.setBlock(x, y, z, log);
			data = (Orientable)log.createBlockData();
			data.setAxis(Axis.X);
			area.setBlockData(x, y, z, data);
			
			area.setBlock(x - 1, y, z, log);
			area.setBlockData(x - 1, y, z, data);
			area.setBlock(x + 1, y, z, log);
			area.setBlockData(x + 1, y, z, data);
			area.setBlock(x - 2, y, z, log);
			area.setBlockData(x - 2, y, z, data);
			area.setBlock(x + 2, y, z, log);
			area.setBlockData(x + 2, y, z, data);
			break;
		case 1:			
			area.setBlock(x, y, z, log);
			data = (Orientable)log.createBlockData();
			data.setAxis(Axis.Z);
			area.setBlockData(x, y, z, data);
			
			area.setBlock(x, y, z - 1, log);
			area.setBlockData(x, y, z - 1, data);
			area.setBlock(x, y, z + 1, log);
			area.setBlockData(x, y, z + 1, data);
			area.setBlock(x, y, z - 2, log);
			area.setBlockData(x, y, z - 2, data);
			area.setBlock(x, y, z + 2, log);
			area.setBlockData(x, y, z + 2, data);
			break;
		}
	}
	
	public static void GenerateSugarcane(Random rand, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x + 1, y, z) == Material.WATER ||
			area.getBlock(x - 1, y, z) == Material.WATER ||
			area.getBlock(x, y, z + 1) == Material.WATER ||
			area.getBlock(x, y, z - 1) == Material.WATER)
		{
			area.setBlock(x, y + 1, z, Material.SUGAR_CANE);
			
			int length = rand.nextInt(4);
			for (int i = 1; i < length; i++)
			{
				if (area.getBlock(x, y + 1 + i, z).isAir() == true)
				{
					area.setBlock(x, y + 1 + i, z, Material.SUGAR_CANE);
				}
			}
		}
	}
	
	public static void GenerateSugarcane(Random rand, Block block)
	{
		if (block.getRelative(1, 0, 0).getType() == Material.WATER ||
			block.getRelative(-1, 0, 0).getType() == Material.WATER ||
			block.getRelative(0, 0, 1).getType() == Material.WATER ||
			block.getRelative(0, 0, -1).getType() == Material.WATER)
		{
			block.getRelative(0, 1, 0).setType(Material.SUGAR_CANE, false);
			
			int length = rand.nextInt(4);
			for (int i = 1; i < length; i++)
			{
				if (block.getRelative(0, 1+i, 0).getType().equals(Material.AIR))
				{
					block.getRelative(0, 1+i, 0).setType(Material.SUGAR_CANE, false);
				}
			}
		}
	}
	
	public static boolean GenerateVine(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{
		MultipleFacing facing = (MultipleFacing)Material.VINE.createBlockData();
		BlockFace face = null;
		
		if (area.getBlock(x, y, z).isSolid() == false)
		{ 
			if (area.getBlock(x + 1, y, z).isSolid() == true) { face = BlockFace.EAST; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x - 1, y, z).isSolid() == true) { face = BlockFace.WEST; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x, y, z + 1).isSolid() == true) { face = BlockFace.NORTH; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x, y, z - 1).isSolid() == true) { face = BlockFace.SOUTH; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x, y + 1, z).isSolid() == true) { face = BlockFace.DOWN; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x, y - 1, z).isSolid() == true) { face = BlockFace.UP; facing.setFace(face.getOppositeFace(), true); }
			
			if (face != null)
			{
				area.setBlock(x, y, z, Material.VINE);
				area.setBlockData(x, y, z, facing);
				
				for (int n = 1; n < random.nextInt(maxLength) + 1; n++)
				{
					if (area.getBlock(x, y - n, z).isOccluding() == false)
					{
						area.setBlock(x, y - n, z, Material.VINE);
						area.setBlockData(x, y - n, z, facing);
					}
				}
				
				return true;
			}
		}
		
		return true;
	}
	
	public static boolean GenerateRoots(Random random, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x, y, z).isSolid() == false && BlockUtil.isEarthy(area.getBlock(x, y + 1, z)))
		{ 
			area.setBlock(x, y, z, Material.HANGING_ROOTS);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateLichen(Random random, DecorationArea area, int x, int y, int z)
	{
		MultipleFacing facing = (MultipleFacing)Material.GLOW_LICHEN.createBlockData();
		BlockFace face = null;
		
		if (area.getBlock(x, y, z).isSolid() == false && BlockUtil.isEarthy(area.getBlock(x, y, z)))
		{ 
			if (area.getBlock(x, y + 1, z).isSolid() == true) { face = BlockFace.DOWN; facing.setFace(face.getOppositeFace(), true); }
			if (area.getBlock(x, y - 1, z).isSolid() == true) { face = BlockFace.UP; facing.setFace(face.getOppositeFace(), true); }
			
			if (face != null)
			{
				area.setBlock(x, y, z, Material.GLOW_LICHEN);
				area.setBlockData(x, y, z, facing);
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean GenerateDripstone(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{
		if (maxLength <= 0) maxLength = 1;
		int targetLength = random.nextInt(maxLength) + 1;
		int length = 0;
		int dir = 1;
		int mergeAt = 0;
		boolean shouldMerge = false;
		
		PointedDripstone dripstone = (PointedDripstone)Material.POINTED_DRIPSTONE.createBlockData();
		
		if (area.getBlock(x, y, z) == Material.DRIPSTONE_BLOCK)
			if (area.getBlock(x, y + 1, z).isSolid() == false)
				y++;
			else
				y--;
		
		if (area.getBlock(x, y, z).isSolid() == false)
		{
			//	Find a direction to grow in or else return
			if (area.getBlock(x, y + 1, z).isSolid() == false) dir = 1;
			else if (area.getBlock(x, y - 1, z).isSolid() == false) dir = -1;
			else return false;
			
			if (dir == 1)
				dripstone.setVerticalDirection(BlockFace.UP);
			else
				dripstone.setVerticalDirection(BlockFace.DOWN);
			
			//	Calculate the length and determine merge behavior
			for (int n = 1; n < targetLength; n++)
			{
				if (area.getBlock(x, y + (n*dir), z) == Material.POINTED_DRIPSTONE)
					break;
				
				if (area.getBlock(x, y + (n*dir), z).isSolid() == false)
					length++;
				else if (length > 2)
					shouldMerge = true;	//	Should merge if the dripstone would touch the ground
			}
			
			mergeAt = Math.round(length/2);
			dripstone.setThickness(Thickness.TIP);
			
			//	Create the dripstone
			for (int n = 1; length > 1 && n <= length; n++)
			{
				dripstone.setThickness(Thickness.MIDDLE);
				
				if (shouldMerge)
				{
					if (n >= mergeAt - 1 && n <= mergeAt + 1)
					{
						if (n == mergeAt || n == mergeAt + 1)
							dripstone.setThickness(Thickness.TIP_MERGE);
						else
							dripstone.setThickness(Thickness.FRUSTUM);
						
						if (n == (length/2) + 1)
						{
							//	Flip direction as we grow the merged half
							if (dir == 1)
								dripstone.setVerticalDirection(BlockFace.DOWN);
							else
								dripstone.setVerticalDirection(BlockFace.UP);
						}
					}
					else if (n == length)
						dripstone.setThickness(Thickness.BASE);
				}
				else
				{
					if (n == length-1)
						dripstone.setThickness(Thickness.FRUSTUM);
					if (n == length)
						dripstone.setThickness(Thickness.TIP);
				}
				
				dripstone.setWaterlogged(area.getBlock(x, y + (n*dir), z) == Material.WATER);
				
				area.setBlock(x, y + (n*dir), z, Material.POINTED_DRIPSTONE);					
				area.setBlockData(x, y + (n*dir), z, dripstone);
				
				dripstone.setThickness(Thickness.BASE);
			}
			
			dripstone.setWaterlogged(area.getBlock(x, y, z) == Material.WATER);
			area.setBlock(x, y, z, Material.POINTED_DRIPSTONE);
			area.setBlockData(x, y, z, dripstone);
			
			return true;
		}
		
		return false;
	}
	
	public static Material GenerateStrata(Random random, DecorationArea area, int x, int y, int z, Object[] layers)
	{
//		int highestY = BlockUtil.getHighestSolidY(x, z, area);
//		float scale = (highestY + 1) / 63; if (scale < 1.0f) scale = 1.0f;
//		int depth = Math.abs((int)(y - 63));
//		
//		for (int i = 0; i < layers.length; i += 2)
//		{
//			if (depth <= (int)layers[i + 1] + random.nextInt(3) * scale)
//			{
//				return (Material)layers[i];
//			}
//		}
		
		return Material.STONE;
	}
	
	public static LootTables randLootTable(Random rand, LootTables... candidates) {
	    return candidates[rand.nextInt(candidates.length)];
	  }
		  
	  public static LootTables randLootTable(LootTables... candidates) {
	    return randLootTable(new Random(), candidates);
	  }
}
