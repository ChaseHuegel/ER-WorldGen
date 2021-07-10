package Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.AmethystCluster;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.Bamboo.Leaves;
import org.bukkit.block.data.type.BigDripleaf;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.block.data.type.GlowLichen;
import org.bukkit.block.data.type.PointedDripstone.Thickness;
import org.bukkit.block.data.type.SmallDripleaf;
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
	
	public static boolean GenerateWeepingVine(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{
		if (BlockUtil.isAir(area.getBlock(x, y, z)) && BlockUtil.isSolid(area.getBlock(x, y + 1, z)))
		{			
			for (int n = 0; n < random.nextInt(maxLength) + 1; n++)
			{				
				if (BlockUtil.isAir(area.getBlock(x, y - n, z)))
				{
					area.setBlock(x, y - n, z, Material.WEEPING_VINES_PLANT);
				}
				else
				{
					area.setBlock(x, y - n + 1, z, Material.WEEPING_VINES);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean GenerateGlowberries(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{
		if (BlockUtil.isAir(area.getBlock(x, y, z)) && BlockUtil.isSolid(area.getBlock(x, y + 1, z)))
		{
			CaveVinesPlant data = (CaveVinesPlant)Material.CAVE_VINES_PLANT.createBlockData();
			
			for (int n = 0; n < random.nextInt(maxLength) + 1; n++)
			{
				boolean hasBerries = (random.nextInt(9) == 0);
				data.setBerries(hasBerries);
				
				if (BlockUtil.isAir(area.getBlock(x, y - n, z)))
				{
					area.setBlock(x, y - n, z, Material.CAVE_VINES_PLANT);
					area.setBlockData(x, y - n, z, data);
				}
				else
				{
					area.setBlock(x, y - n + 1, z, Material.CAVE_VINES);
					area.setBlockData(x, y - n + 1, z, data);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean GenerateVine(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{
		if (BlockUtil.isAir(area.getBlock(x, y, z)))
		{ 
			MultipleFacing facing = (MultipleFacing)Material.VINE.createBlockData();
			
			facing.setFace(BlockFace.UP, BlockUtil.isEarthy(area.getBlock(x, y + 1, z)));
			facing.setFace(BlockFace.NORTH, BlockUtil.isEarthy(area.getBlock(x, y, z - 1)));
			facing.setFace(BlockFace.SOUTH, BlockUtil.isEarthy(area.getBlock(x, y, z + 1)));
			facing.setFace(BlockFace.EAST, BlockUtil.isEarthy(area.getBlock(x + 1, y, z)));
			facing.setFace(BlockFace.WEST, BlockUtil.isEarthy(area.getBlock(x - 1, y, z)));
			
			if (facing.getFaces().size() == 1 && facing.getFaces().contains(BlockFace.UP))
				return false;
			
			if (!facing.getFaces().isEmpty())
			{
				area.setBlock(x, y, z, Material.VINE);
				area.setBlockData(x, y, z, facing);
				facing.setFace(BlockFace.UP, false);
				
				for (int n = 1; n < random.nextInt(maxLength) + 1; n++)
				{
					if (BlockUtil.isAir(area.getBlock(x, y - n, z)))
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
	
	public static boolean GenerateWater(Random random, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x, y, z).isAir())
		{
			area.setBlock(x, y, z, Material.WATER);
			return true;
		}
		else if (area.getBlockData(x, y, z) instanceof Waterlogged)
		{
			BlockData data = area.getBlockData(x, y, z);
			((Waterlogged)data).setWaterlogged(true);
			area.setBlockData(x, y, z, data);
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateFoliage(Random random, DecorationArea area, int x, int y, int z, Object[] table)
	{
		return GenerateFoliage(random, area, x, y, z, table, true);
	}
	
	public static boolean GenerateFoliage(Random random, DecorationArea area, int x, int y, int z, Object[] table, boolean checkWater)
	{
		Material material = BlockUtil.weightedRandomMaterial(random, table);
		
		//	If submerged and this is not a waterlogged foliage, return
		if (checkWater && area.getBlock(x, y, z) == Material.WATER && !MaterialGroup.WATERLOGGABLE.contains(material))
			return false;
		else if (checkWater && MaterialGroup.WATERLOGGABLE.contains(material) && area.getBlock(x, y, z) != Material.WATER)
			return false;
		else if (area.getBlock(x, y, z).isAir() == false && !MaterialGroup.WATERLOGGABLE.contains(material))
			return false;
		
		if (checkWater && BlockUtil.isTallPlant(material) && area.getBlock(x, y + 1, z) == Material.WATER && !(MaterialGroup.WATERLOGGABLE.contains(material)))
			return false;
		
		if (area.getBlock(x, y, z).isSolid() == false && BlockUtil.isEarthy(area.getBlock(x, y - 1, z)))
		{ 
			BlockUtil.setFoliage(area, x, y, z, material);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateFoliage(Random random, DecorationArea area, int x, int y, int z, Material material)
	{
		return GenerateFoliage(random, area, x, y, z, material, true);
	}
	
	public static boolean GenerateFoliage(Random random, DecorationArea area, int x, int y, int z, Material material, boolean checkWater)
	{		
		//	If submerged and this is not a waterlogged foliage, return
		if (checkWater && area.getBlock(x, y, z) == Material.WATER && !MaterialGroup.WATERLOGGABLE.contains(material))
			return false;
		else if (checkWater && MaterialGroup.WATERLOGGABLE.contains(material) && area.getBlock(x, y, z) != Material.WATER)
			return false;
		else if (area.getBlock(x, y, z).isAir() == false && !MaterialGroup.WATERLOGGABLE.contains(material))
			return false;
		
		if (checkWater && BlockUtil.isTallPlant(material) && area.getBlock(x, y + 1, z) == Material.WATER && !(MaterialGroup.WATERLOGGABLE.contains(material)))
			return false;
			
		if (BlockUtil.isEarthy(area.getBlock(x, y - 1, z)))
		{ 
			BlockUtil.setFoliage(area, x, y, z, material);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateMoss(Random random, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x, y, z).isSolid() == false && !BlockUtil.isLiquid(area.getBlock(x, y, z)) && BlockUtil.isSolid(area.getBlock(x, y - 1, z)))
		{ 
			area.setBlock(x, y, z, Material.MOSS_CARPET);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateRoots(Random random, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x, y, z).isSolid() == false && !BlockUtil.isLiquid(area.getBlock(x, y, z)) && BlockUtil.isEarthy(area.getBlock(x, y + 1, z)))
		{ 
			area.setBlock(x, y, z, Material.HANGING_ROOTS);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateSpore(Random random, DecorationArea area, int x, int y, int z)
	{
		if (area.getBlock(x, y, z).isSolid() == false && !BlockUtil.isLiquid(area.getBlock(x, y, z)) && BlockUtil.isEarthy(area.getBlock(x, y + 1, z)))
		{ 
			area.setBlock(x, y, z, Material.SPORE_BLOSSOM);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateAmethyst(Random random, DecorationArea area, int x, int y, int z)
	{		
		if (area.getBlock(x, y, z).isSolid() == false)
		{ 
			AmethystCluster amethyst = (AmethystCluster)Material.AMETHYST_CLUSTER.createBlockData();
			boolean canPlace = false;
			
			if (BlockUtil.isEarthy(area.getBlock(x, y + 1, z))) { amethyst.setFacing(BlockFace.DOWN); canPlace = true; }
			else if (BlockUtil.isEarthy(area.getBlock(x, y - 1, z))) { amethyst.setFacing(BlockFace.UP); canPlace = true; }
			else if (BlockUtil.isEarthy(area.getBlock(x + 1, y, z))) { amethyst.setFacing(BlockFace.WEST); canPlace = true; }
			else if (BlockUtil.isEarthy(area.getBlock(x - 1, y, z))) { amethyst.setFacing(BlockFace.EAST); canPlace = true; }
			else if (BlockUtil.isEarthy(area.getBlock(x, y, z + 1))) { amethyst.setFacing(BlockFace.SOUTH); canPlace = true; }
			else if (BlockUtil.isEarthy(area.getBlock(x, y, z - 1))) { amethyst.setFacing(BlockFace.NORTH); canPlace = true; }
			
			if (canPlace)
			{
				amethyst.setWaterlogged(area.getBlock(x, y, z) == Material.WATER);
				area.setBlock(x, y, z, Material.AMETHYST_CLUSTER);
				area.setBlockData(x, y, z, amethyst);
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean GenerateLichen(Random random, DecorationArea area, int x, int y, int z)
	{		
		if (!BlockUtil.isSolid(area.getBlock(x, y, z)) && area.getBlock(x, y, z) != Material.LAVA)
		{ 
			GlowLichen lichen = (GlowLichen)Material.GLOW_LICHEN.createBlockData();
			
			lichen.setFace(BlockFace.UP, BlockUtil.isEarthy(area.getBlock(x, y + 1, z)));
			lichen.setFace(BlockFace.DOWN, BlockUtil.isEarthy(area.getBlock(x, y - 1, z)));
			lichen.setFace(BlockFace.NORTH, BlockUtil.isEarthy(area.getBlock(x, y, z - 1)));
			lichen.setFace(BlockFace.SOUTH, BlockUtil.isEarthy(area.getBlock(x, y, z + 1)));
			lichen.setFace(BlockFace.EAST, BlockUtil.isEarthy(area.getBlock(x + 1, y, z)));
			lichen.setFace(BlockFace.WEST, BlockUtil.isEarthy(area.getBlock(x - 1, y, z)));
			
			if (!lichen.getFaces().isEmpty())
			{
				lichen.setWaterlogged(area.getBlock(x, y, z) == Material.WATER);
				area.setBlock(x, y, z, Material.GLOW_LICHEN);
				area.setBlockData(x, y, z, lichen);
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean GenerateCustomDripstone(Random random, DecorationArea area, int x, int y, int z, int maxLength, Material material)
	{
		if (maxLength <= 0) maxLength = 1;
		int targetLength = random.nextInt(maxLength) + 1;
		int length = 0;
		int dir = 1;
		
		if (BlockUtil.isSolid(area.getBlock(x, y, z)))
		{
			if (area.getBlock(x, y + 1, z).isSolid() == false)
				y++;
			else
				y--;
		}
		else
		{
			return false;
		}
		
		if (BlockUtil.isSolid(area.getBlock(x, y, z)) == false)
		{
			//	Find a direction to grow in or else return
			if (area.getBlock(x, y + 1, z).isSolid() == false) dir = 1;
			else if (area.getBlock(x, y - 1, z).isSolid() == false) dir = -1;
			else return false;
			
			//	Calculate the length and determine merge behavior
			for (int n = 1; n < targetLength; n++)
			{				
				if (BlockUtil.isSolid(area.getBlock(x, y + (n*dir), z)) == false)
					length++;
			}
			
			if (length <= 1) return false;
			
			//	Create the dripstone
			for (int n = 1; n <= length; n++)
				area.setBlock(x, y + (n*dir), z, material);
			
			area.setBlock(x, y, z, material);
			
			return true;
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
		
		if (BlockUtil.isSolid(area.getBlock(x, y, z)))
		{
			if (area.getBlock(x, y + 1, z).isSolid() == false)
				y++;
			else
				y--;
		}
		else
		{
			return false;
		}
		
		if (BlockUtil.isSolid(area.getBlock(x, y, z)) == false)
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
				
				if (BlockUtil.isSolid(area.getBlock(x, y + (n*dir), z)) == false)
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
	
	public static boolean GenerateKelp(Random random, DecorationArea area, int x, int y, int z, int minLength, int maxLength)
	{
		return GenerateKelp(random, area, x, y, z, minLength, maxLength, true);
	}
	
	public static boolean GenerateKelp(Random random, DecorationArea area, int x, int y, int z, int minLength, int maxLength, boolean checkWater)
	{
		boolean placed = false;
		
		if (BlockUtil.isSolid(area.getBlock(x, y - 1, z)))
		{			
			for (int n = 0; n < random.nextInt(maxLength - minLength) + minLength; n++)
			{
				if ((checkWater && area.getBlock(x, y + n, z) == Material.WATER && area.getBlock(x, y + n + 1, z) == Material.WATER) 
				|| (!checkWater && !BlockUtil.isSolid(area.getBlock(x, y + n, z)) && !BlockUtil.isSolid(area.getBlock(x, y + n + 1, z))))
				{
					area.setBlock(x, y + n, z, Material.KELP_PLANT);
					placed = true;
				}
			}
		}
		
		return placed;
	}
	
	public static boolean GenerateBamboo(Random random, DecorationArea area, int x, int y, int z, int minLength, int maxLength)
	{
		int bambooHeight = random.nextInt(maxLength) + minLength;
		
		if (area.getBlock(x, y, z).isSolid() || BlockUtil.isEarthy(area.getBlock(x, y - 1, z)) == false)
			return false;
		
		for (int i = 0; i < bambooHeight; i++)
		{
			area.setBlock(x, y + i, z, Material.BAMBOO);
			if (i == bambooHeight - 1)
			{
				Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
				data.setLeaves(Leaves.LARGE);
				area.setBlockData(x, y + i, z, data);
			}
			else if (i >= bambooHeight - 3)
			{
				Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
				data.setLeaves(Leaves.SMALL);
				area.setBlockData(x, y + i, z, data);
			}
		}
		
		return true;
	}
	
	public static boolean GenerateBigDripleaf(Random random, DecorationArea area, int x, int y, int z, int minLength, int maxLength)
	{		
		if (area.getBlock(x, y, z) == Material.WATER && BlockUtil.isEarthy(area.getBlock(x, y - 1, z)) == true)
		{
			int targetHeight = random.nextInt(maxLength) + minLength;
			int height = 0;
			
			for (int i = 0; i < targetHeight; i++)
				if (!area.getBlock(x, y + i, z).isSolid()) height++;
			
			BigDripleaf data = (BigDripleaf)Material.BIG_DRIPLEAF.createBlockData();
			data.setFacing( BlockUtil.randFacing(random, data.getFaces().toArray(new BlockFace[0])) );
			
			for (int i = 0; i < height; i++)
			{
				data.setWaterlogged(area.getBlock(x, y + i, z) == Material.WATER);
				
				if (i == height - 1)
				{					
					area.setBlock(x, y + i, z, Material.BIG_DRIPLEAF);
					area.setBlockData(x, y + i, z, data);
				}
				else
				{
					BlockData data2 = Material.BIG_DRIPLEAF_STEM.createBlockData();
					((Waterlogged)data2).setWaterlogged(data.isWaterlogged());
					((Directional)data2).setFacing(data.getFacing());
					area.setBlock(x, y + i, z, Material.BIG_DRIPLEAF_STEM);
					area.setBlockData(x, y + i, z, data2);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean GenerateSmallDripleaf(Random random, DecorationArea area, int x, int y, int z)
	{		
		if ((area.getBlock(x, y+1, z) == Material.WATER || area.getBlock(x, y+1, z).isAir()) && BlockUtil.isMoistGround(area, x, y - 1, z))
		{
			SmallDripleaf data = (SmallDripleaf)Material.SMALL_DRIPLEAF.createBlockData();
			
			data.setHalf(Bisected.Half.BOTTOM);
			data.setFacing( BlockUtil.randFacing(random, data.getFaces().toArray(new BlockFace[0])) );
			data.setWaterlogged(area.getBlock(x, y, z) == Material.WATER);
			area.setBlock(x, y, z, Material.SMALL_DRIPLEAF);
			area.setBlockData(x, y, z, data);
			
			data.setHalf(Bisected.Half.TOP);
			data.setWaterlogged(area.getBlock(x, y + 1, z) == Material.WATER);
			area.setBlock(x, y + 1, z, Material.SMALL_DRIPLEAF);
			area.setBlockData(x, y + 1, z, data);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean Resurface(Random random, DecorationArea area, int x, int y, int z, Material top, Material bottom, Material walls, List<Material> mask)
	{
		return Resurface(random, area, x, y, z, top, bottom, walls, mask.toArray(new Material[0]));
	}
	
	public static boolean Resurface(Random random, DecorationArea area, int x, int y, int z, Material top, Material bottom, Material walls, Material... mask)
	{
		for (int i = 0; i < mask.length; i++)
		{
			if (area.getBlock(x, y, z) == mask[i])
			{
				//	Floor
				if (top != null && BlockUtil.isTopExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, top);
					return true;
				}
				//	Ceiling
				else if (bottom != null && BlockUtil.isBottomExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, bottom);
					return true;
				}
				//	Walls
				else if (walls != null && BlockUtil.isSideExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, walls);
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}
	
	public static boolean Resurface(Random random, DecorationArea area, int x, int y, int z, Material top, Material trim, Material bottom, Material walls, Material... mask)
	{
		for (int i = 0; i < mask.length; i++)
		{
			if (area.getBlock(x, y, z) == mask[i])
			{
				//	Floor
				if (top != null && BlockUtil.isTopExposed(area, x, y, z) && !BlockUtil.isBottomExposed(area, x, y, z))
				{
					//	Try trimming
					if (trim != null && BlockUtil.isSideObscured(area, x, y, z))
						area.setBlock(x, y, z, trim);
					else
						area.setBlock(x, y, z, top);
					
					return true;
				}
				//	Ceiling
				else if (bottom != null && BlockUtil.isBottomExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, bottom);
					return true;
				}
				//	Walls
				else if (walls != null && BlockUtil.isSideExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, walls);
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}
	
	public static boolean Resurface(Random random, DecorationArea area, int x, int y, int z, Material material, Material... mask)
	{
		if (material == null) return false;
		
		for (int i = 0; i < mask.length; i++)
		{
			if (area.getBlock(x, y, z) == mask[i])
			{
				if (BlockUtil.isExposed(area, x, y, z))
				{
					area.setBlock(x, y, z, material);
					return true;
				}
				
				return false;
			}
		}
		
		return false;
	}
	
	public static boolean Replace(Random random, DecorationArea area, int x, int y, int z, Material material, Material... mask)
	{
		if (material == null) return false;
		
		for (int i = 0; i < mask.length; i++)
		{
			if (area.getBlock(x, y, z) == mask[i])
			{
				area.setBlock(x, y, z, material);
				return true;
			}
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
