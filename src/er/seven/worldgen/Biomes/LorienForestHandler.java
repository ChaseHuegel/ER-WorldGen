package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class LorienForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.75f;
	private static float LightChance = 0.15f;
	private static float BushChance = 0.002f;
	
	private static Object[] foliageTable = new Object[] {
			Material.LILAC, 1,
			Material.SUNFLOWER, 1,
			Material.AZURE_BLUET, 5,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.OXEYE_DAISY, 5,
			Material.GRASS, 75,
			Material.FERN, 20 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.DIORITE, 20,
			Material.STONE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.TALL_BIRCH_FOREST ); }
	
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
					if (area.getBlock(x + 1, y - 1, z).isAir() == true ||
						area.getBlock(x - 1, y - 1, z).isAir() == true ||
						area.getBlock(x, y - 1, z + 1).isAir() == true ||
						area.getBlock(x, y - 1, z - 1).isAir() == true)
					{
						replacementMaterial = Material.WHITE_TERRACOTTA;
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.WHITE_TERRACOTTA;
					}
					break;	
				case ANDESITE:
				case GRANITE: 
					replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
							Material.QUARTZ_BLOCK, 1,
							Material.DIORITE, 3
					});
					break;
				case STONE:			replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case DIAMOND_ORE: 	replacementMaterial = Material.EMERALD_ORE; break;
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
	public void Populate(Random random, DecorationArea area, World world)
	{
		int x = area.getCenterX() - DecorationArea.DECORATION_RADIUS;
		int z = area.getCenterZ() - DecorationArea.DECORATION_RADIUS;
		int y = BlockUtil.getHighestGroundedY(x, z, area);
		
		//	Trees
		if (random.nextFloat() <= TreeChance && area.getBlock(x, y + 1, z) != Material.WATER && BlockUtil.isDirt(area.getBlock(x, y, z)) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "LorienForest", world);
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
		
		// Light
		if (random.nextFloat() <= LightChance)
		{
			for (int n = 1; n < 60; n++)
			{
				if (area.getBlock(x, y + n, z) != Material.WATER && area.getBlock(x, y + n, z) != Material.LAVA)
				{
					area.setBlock(x, y + n, z, Material.AIR);
				}
			}
		}
		
		//	Bushes
		if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.BIRCH_LEAVES);
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
