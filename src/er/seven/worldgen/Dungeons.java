package er.seven.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import Util.BlockDef;
import Util.BlockUtil;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class Dungeons 
{
	public static Material[] wallMaterial = new Material[] { Material.STONE, Material.MOSSY_COBBLESTONE };
	public static Material[] trimMaterial = new Material[] { Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS };
	public static Material[] floorMaterial = new Material[] { Material.STONE, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS };
	public static Material[] ceilingMaterial = new Material[] { Material.STONE, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS };
	
	public static Material stairColumnMaterial = Material.CHISELED_STONE_BRICKS;
	public static Material stairMaterial = Material.COBBLESTONE;
	public static Material stairStepMaterial = Material.COBBLESTONE_SLAB;
	
	public static BlockFace[] stairSpiralOrder = new BlockFace[] { 
			BlockFace.SOUTH_EAST, BlockFace.EAST, BlockFace.NORTH_EAST, 
			BlockFace.NORTH, BlockFace.NORTH_WEST, BlockFace.WEST,
			BlockFace.SOUTH_WEST, BlockFace.SOUTH };
	
	public static boolean GenerateDungeon(Random random, DecorationArea area, int x, int y, int z, int maxLength)
	{		
		return false;
	}
	
	public static boolean GenerateDungeon(World world, int x, int y, int z)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		
		blocks.addAll(GenerateDungeonBlock(x, y, z, 7));
		blocks.addAll(GenerateDungeonBlock(x, y, z+7, 7));
		blocks.addAll(GenerateDungeonBlock(x+7, y, z, 7));
		blocks.addAll(GenerateDungeonBlock(x+7, y, z+7, 7));
		
		blocks.addAll(PopulateDungeonBlock(x, y, z, 7));
		blocks.addAll(PopulateDungeonBlock(x, y, z+7, 7));
		blocks.addAll(PopulateDungeonBlock(x+7, y, z, 7));
		
		blocks.addAll(StairDungeonBlock(x+7, y, z+7, 7));
		
		blocks.forEach(def -> 
		{
			Block block = world.getBlockAt(def.x, def.y, def.z);
			
			block.setType(def.material, false);
			
			if (def.blockData != null)
				block.setBlockData(def.blockData);
			
			if (def.material == Material.SPAWNER)
				SetSpawnerState(world, def.x, def.y, def.z, EntityType.ZOMBIE);
			
			if (def.material == Material.CHEST)
				SetChestLoot(world, def.x, def.y, def.z, LootTables.SIMPLE_DUNGEON);
		});
		
		return true;
	}
	
	private static List<BlockDef> GenerateDungeonBlock(int x, int y, int z, int size)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		int realSize = size-1;
		Material material = null;
		
		for (int width = 0; width < size; width++)
		for (int height = 0; height < size; height++)
		for (int depth = 0; depth < size; depth++)
		{
			if (height > 0 && height <= 3 && width > size/2-1.5 && width < size/2+1.5) material = Material.AIR;
			else if (height > 0 && height <= 3 && depth > size/2-1.5 && depth < size/2+1.5) material = Material.AIR;
			else if (height == realSize && depth > size/2-1.5 && depth < size/2+1.5 && width > size/2-1.5 && width < size/2+1.5) material = Material.AIR;
			else if (width == 0 || width == realSize) material = BlockUtil.randMaterial(wallMaterial);
			else if (depth == 0 || depth == realSize) material = BlockUtil.randMaterial(wallMaterial);
			else if (height == 0) material = BlockUtil.randMaterial(floorMaterial);
			else if (height == realSize) material = BlockUtil.randMaterial(ceilingMaterial);
			else material = Material.AIR;
			
			BlockDef block = new BlockDef(x + width, y + height, z + depth, material);
			
			blocks.add(block);
		}
		
		return blocks;
	}
	
	public static List<BlockDef> PopulateDungeonBlock(int x, int y, int z, int size)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		int realSize = size-1;
		
		blocks.add( new BlockDef(x + size/2, y + 1, z + size/2, Material.SPAWNER) );
		
		blocks.add( new BlockDef(x + size/2, y + 1, z, Material.CHEST) );
		
		return blocks;
	}
	
	public static List<BlockDef> StairDungeonBlock(int x, int y, int z, int size)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		int realSize = size-1;
		
		Material stepMaterial = stairStepMaterial;
		int spiralIndex = 0;
		
		for (int height = 1; height < size+1; height++)
		{
			//	Column
			blocks.add( new BlockDef(x + size/2, y + height, z + size/2, stairColumnMaterial) );
			
			//	***** Steps ***** //
			for (int run = 0; run < 2; run++)
			{				
				Location loc = new Location(null, x + size/2, y + height, z + size/2);
				
				loc.add( stairSpiralOrder[spiralIndex].getModX(), stairSpiralOrder[spiralIndex].getModY(), stairSpiralOrder[spiralIndex].getModZ() );
				
				blocks.add( new BlockDef(loc, stepMaterial) );
				
				//	Alternate step block
				if (stepMaterial == stairMaterial) stepMaterial = stairStepMaterial;
				else if (stepMaterial == stairStepMaterial) stepMaterial = stairMaterial;
				
				//	Spiral around
				spiralIndex++;
				if (spiralIndex >= stairSpiralOrder.length) spiralIndex = 0;
			}
		}
		
		return blocks;
	}
	
	public static void SetChestLoot(World world, int x, int y, int z, LootTables table)
	{
		if (world.getBlockAt(x, y, z).getType() == Material.CHEST)
			return;
		
		Chest chest = (Chest)world.getBlockAt(x, y, z).getState();
		chest.setLootTable(table.getLootTable());
		chest.update();
	}
	
	public static void SetSpawnerState(World world, int x, int y, int z, EntityType type)
	{
		if (world.getBlockAt(x, y, z).getType() == Material.SPAWNER)
			return;
		
		CreatureSpawner spawner = (CreatureSpawner)world.getBlockAt(x, y, z).getState();
		spawner.setSpawnedType(type);
		spawner.setSpawnRange(10);
		spawner.setMinSpawnDelay(0);
		spawner.setMaxSpawnDelay(20);
		spawner.setRequiredPlayerRange(16);
		spawner.setMaxNearbyEntities(6);
		spawner.setSpawnCount(4);
		spawner.update();
	}
}
