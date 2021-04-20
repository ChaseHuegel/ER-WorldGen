package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.SeaPickle;

import Util.BlockUtil;
import Util.FastNoise;
import Util.FastNoise.NoiseType;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class ForgottenValeHandler extends ChunkHandler
{	
	//	DECORATION
	private static float TreeChance 			= 0.006f;
	private static float LilyPadChance 			= 0.05f;
	private static float ReedsChance 			= 0.15f;
	private static float WaterFoliageChance 	= 0.5f;
	
	private static Object[] foliageTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.BLUE_ORCHID, 1,
			Material.TALL_GRASS, 40,
			Material.GRASS, 150 };
	
	private static Object[] waterFoliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.SEAGRASS, 100 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float StumpChance 		= 0.05f;
	private static float LogChance 			= 0.05f;
	private static float FossilChance 		= 0.05f;
	
	//	CAVES
	private static float StalagChance 			= 0.07f;
	private static float CaveGrowthChance 		= 0.09f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SWAMP_HILLS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int seaLevel = world.getSeaLevel() - 1;
		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{				
				switch (block.getMaterial())
				{
				case STONE:
					replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					
					if (area.getBlock(x, y + 1, z).isOccluding() == false)
					{
						if (random.nextBoolean()) replacementMaterial = Material.DIRT; else replacementMaterial = Material.COARSE_DIRT;
					}
					else
					{
						if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
						{
							if (random.nextInt(4) == 0) replacementMaterial = Material.MOSSY_COBBLESTONE;
						}
					}
					break;
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
		
		int highestY = BlockUtil.getHighestGroundedY(realX, realZ, area);
		if (highestY < 60) return;
		
		if (random.nextFloat() <= StumpChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "stump", world);
		}
		else if (random.nextFloat() <= LogChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "log", world);
		}
		else if (random.nextFloat() <= FossilChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "fossil", world);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		y = highestY;
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "ForgottenVale", world);
		}
		//	Lily pads
		else if (random.nextFloat() <= LilyPadChance && blockAbove == Material.WATER && area.getBlock(x, y + 2, z) != Material.WATER)
		{
			area.setBlock(x, y + 2, z, Material.LILY_PAD);
		}
		//	Reeds
		else if (random.nextFloat() <= ReedsChance && BlockUtil.isDirt(block) && area.getBlock(x, y + 1, z).isAir() == true)
		{
			GenUtil.GenerateSugarcane(random, area, x, y, z);
		}
		//	Water foliage
		else if (random.nextFloat() <= WaterFoliageChance && blockAbove == Material.WATER)
		{
			Material foliageType = BlockUtil.weightedRandomMaterial(random, waterFoliageTable);
			
			switch (foliageType)
			{
			case SEA_PICKLE:
				area.setBlock(x, y + 1, z, foliageType);
				SeaPickle data = (SeaPickle)area.getBlockData(x, y + 1, z);
				data.setPickles( 1 + (new Random()).nextInt(data.getMaximumPickles()) );
				area.setBlockData(x, y + 1, z, data);
				break;
			case SEAGRASS: area.setBlock(x, y + 1, z, foliageType); break;
			default: break;
			}
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			Material foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable);
			
			if (BlockUtil.isLeaves(foliageType))
			{
				BlockUtil.setPersistentLeaves(area, x, y + 1, z, foliageType);
			}
			else if (BlockUtil.isTallPlant(foliageType))
			{
				BlockUtil.setTallPlant(area, x, y + 1, z, foliageType);
			}
			else
			{
				area.setBlock(x, y + 1, z, foliageType);
			}
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
