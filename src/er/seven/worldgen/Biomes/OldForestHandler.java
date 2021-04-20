package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class OldForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.045f;
	private static float HiveChance = 0.025f;
	private static float RockChance = 0.004f;
	private static float BushChance = 0.006f;
	private static float LogChance 	= 0.003f;
	
	private static Object[] foliageTable = new Object[] {
			Material.RED_MUSHROOM, 6,
			Material.BROWN_MUSHROOM, 6,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.SWEET_BERRY_BUSH, 4,
			Material.FERN, 24,
			Material.GRASS, 12 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.ANDESITE, 30,
			Material.GRANITE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float StumpChance 		= 0.00005f;
	private static float OutcroppingChance 	= 0.00005f;
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.DARK_FOREST ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case GRASS_BLOCK:
					replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
							Material.PODZOL, 4,
							Material.COARSE_DIRT, 3,
							Material.GRASS_BLOCK, 1,
					});
					break;
				case STONE:
					replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					if (area.getBlockData(x, y + 1, z).getMaterial().isOccluding() == false)
					{
						if (random.nextBoolean()) replacementMaterial = Material.DIRT; else replacementMaterial = Material.PODZOL;
					}
					break;
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GOLD_ORE:			replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case SAND: 				replacementMaterial = Material.COARSE_DIRT; break;
				case DIORITE: 			replacementMaterial = Material.ANDESITE; break;
				case GRANITE: 			replacementMaterial = Material.ANDESITE; break;
				default: break;
				}
			}
			
			if (replacementMaterial != null)
			{
				area.setBlock(x, y, z, replacementMaterial);
			}
		}
	}
	
	@Override
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		int realX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + random.nextInt(8);
		int realZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + random.nextInt(8);
		
		int highestY = BlockUtil.getHighestSolidY(realX, realZ, area);
		
		if (random.nextFloat() <= StumpChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "stump", world);
		}
		else if (random.nextFloat() <= OutcroppingChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "outcropping", world);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		//	Bee Hives
		if (random.nextFloat() <= HiveChance)
		{
			y = random.nextInt(16) + 64;
			Material randomBlock = area.getBlock(x, y, z);
			
			if (area.getBlock(x, y - 1, z) == Material.AIR &&
				Tag.LEAVES.getValues().contains(randomBlock) == true)
			{
				area.setBlock(x, y - 1, z, Material.BEE_NEST);
				Main.SpawnEntity(world, x, y - 2, z, EntityType.BEE);
			}
		}
		
		y = highestY;
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "OldForest", world);
		}
		//	Bushes
		else if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.OAK_LEAVES);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.STONE, Material.MOSSY_COBBLESTONE);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.OAK_LOG);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && block == Material.PODZOL && random.nextBoolean())
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
		
		//	Caves
		for (y = highestY; y > 8; y--)
		{
			block = area.getBlock(x, y, z);
			if (block.isOccluding() == false) { continue; }
			blockAbove = area.getBlock(x, y + 1, z);
			
			//	Stalag above
			if (random.nextFloat() <= StalagChance && area.getBlock(x, y - 1, z) == Material.CAVE_AIR)
			{
				area.setBlock(x, y - 1, z, BlockUtil.getStalagMaterial(block));
			}
			
			//	Stalag below
			if (random.nextFloat() <= StalagChance && blockAbove == Material.CAVE_AIR)
			{
				area.setBlock(x, y + 1, z, BlockUtil.getStalagMaterial(block));
			}
			
			//	Growth
			if (random.nextFloat() <= CaveGrowthChance && blockAbove == Material.CAVE_AIR)
			{
				area.setBlock(x, y + 1, z, BlockUtil.weightedRandomMaterial(random, CaveGrowthTable));
			}
		}
	}
}
