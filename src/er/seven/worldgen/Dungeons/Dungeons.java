package er.seven.worldgen.Dungeons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTables;

import Util.BlockDef;
import Util.BlockUtil;
import er.seven.worldgen.Main;
import er.seven.worldgen.Dungeons.Populators.*;
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
	
	public static EntityType[] mobs = new EntityType[] {
			EntityType.SKELETON, EntityType.ZOMBIE, EntityType.SLIME, EntityType.CAVE_SPIDER, 
			EntityType.SPIDER, EntityType.CREEPER, EntityType.HUSK
			};
	
	public static DungeonBlockPopulator[] populators = new DungeonBlockPopulator[] {
			new SpawnerChamber(),
			new LootChamber(),
			new SpawnerLootChamber()
		};
	
	public static void GenerateDungeon(Random random, DecorationArea area, World world, int x, int y, int z, int roomSize, int width, int depth, int layers)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		
		int lastStairX = -1, lastStairZ = -1;
		boolean placedStair = false;
		
		//	Build the dungeon layout
		for (int level = layers-1; level >= 0; level--)
		{
			placedStair = false;
			
			for (int w = 0; w < width; w++)
			for (int d = 0; d < depth; d++)
			{
				blocks.addAll(GenerateDungeonBlock(w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize));
				
				//	Try stairs
				if (placedStair == false && level < layers-1 && w != lastStairX && d != lastStairZ)
				{					
					placedStair = true;
					lastStairX = w;
					lastStairZ = d;
					
					PlaceStairsInBlock(blocks, w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize);
				}
				//	Otherwise populate
				else if ((w != lastStairX || d != lastStairZ) && random.nextInt(5) <= 2)
				{
					PopulateBlock(blocks, w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize);
				}
			}
		}
		
		EntityType mob = mobs[random.nextInt(mobs.length)];
		
		//	Place the blocks
		blocks.forEach(def -> 
		{			
			area.setBlock(def.x, def.y, def.z, def.material);
			
			if (def.material == Material.SPAWNER)
				Main.SetMobSpawner(world, def.x, def.y, def.z, mob);
			
			if (def.material == Material.CHEST)
				Main.SpawnChest(world, def.x, def.y, def.z, LootTables.SIMPLE_DUNGEON);
		});
	}
	
	public static void GenerateDungeon(World world, int x, int y, int z, int roomSize, int width, int depth, int layers)
	{
		List<BlockDef> blocks = new ArrayList<BlockDef>();
		
		int lastStairX = -1, lastStairZ = -1;
		boolean placedStair = false;
		
		//	Build the dungeon layout
		for (int level = layers-1; level >= 0; level--)
		{
			placedStair = false;
			
			for (int w = 0; w < width; w++)
			for (int d = 0; d < depth; d++)
			{
				blocks.addAll(GenerateDungeonBlock(w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize));
				
				//	Try stairs
				if (placedStair == false && level < layers-1 && w != lastStairX && d != lastStairZ)
				{					
					placedStair = true;
					lastStairX = w;
					lastStairZ = d;
					
					PlaceStairsInBlock(blocks, w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize);
				}
				//	Otherwise place chambers
				else if (w != lastStairX || d != lastStairZ)
				{
					PopulateBlock(blocks, w*roomSize+x, level*roomSize+y, d*roomSize+z, roomSize);
				}
			}
		}
		
		EntityType mob = mobs[(new Random()).nextInt(mobs.length)];
		
		//	Place the blocks
		blocks.forEach(def -> 
		{
			Block block = world.getBlockAt(def.x, def.y, def.z);
			
			block.setType(def.material, false);
			
			if (def.blockData != null)
				block.setBlockData(def.blockData);
			
			if (def.material == Material.SPAWNER)
				SetSpawnerState(world, def.x, def.y, def.z, mob);
			
			if (def.material == Material.CHEST)
				SetChestLoot(world, def.x, def.y, def.z, LootTables.SIMPLE_DUNGEON);
		});
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
	
	public static void PopulateBlock(List<BlockDef> blocks, int x, int y, int z, int size)
	{		
		int populatorIndex = 0;
		
		//	Pick a random populator
		if (populators.length > 1)
			populatorIndex = new Random().nextInt(populators.length);
		
		populators[populatorIndex].Populate(blocks, x, y, z, size);
	}
	
	public static void PlaceStairsInBlock(List<BlockDef> blocks, int x, int y, int z, int size)
	{		
		Material stepMaterial = stairStepMaterial;
		int spiralIndex = 0;
		
		for (int height = 1; height < size+1; height++)
		{
			//	Column
			blocks.add( new BlockDef(x + size/2, y + height, z + size/2, stairColumnMaterial) );
			
			//	Padding around column
			blocks.add( new BlockDef(x + size/2 + 1, y + height, z + size/2, Material.AIR) );
			blocks.add( new BlockDef(x + size/2 - 1, y + height, z + size/2, Material.AIR) );
			blocks.add( new BlockDef(x + size/2, y + height, z + size/2 + 1, Material.AIR) );
			blocks.add( new BlockDef(x + size/2, y + height, z + size/2 - 1, Material.AIR) );
			
			//	Padding around column
			blocks.add( new BlockDef(x + size/2 + 1, y + height, z + size/2 + 1, Material.AIR) );
			blocks.add( new BlockDef(x + size/2 - 1, y + height, z + size/2 - 1, Material.AIR) );
			blocks.add( new BlockDef(x + size/2 + 1, y + height, z + size/2 - 1, Material.AIR) );
			blocks.add( new BlockDef(x + size/2 - 1, y + height, z + size/2 + 1, Material.AIR) );
			
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
	}
	
	public static void SetChestLoot(World world, int x, int y, int z, LootTables table)
	{
		if (world.getBlockAt(x, y, z).getType() == Material.CHEST)
			return;
		
		Main.SpawnChest(world, x, y, z, table);
	}
	
	public static void SetSpawnerState(World world, int x, int y, int z, EntityType type)
	{
		if (world.getBlockAt(x, y, z).getType() == Material.SPAWNER)
			return;
		
		Main.SetMobSpawner(world, x, y, z, type);
	}
}
