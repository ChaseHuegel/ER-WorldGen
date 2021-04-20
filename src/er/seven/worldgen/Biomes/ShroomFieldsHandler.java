package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class ShroomFieldsHandler extends ChunkHandler
{	
	private FastNoise grassNoise;
	public ShroomFieldsHandler()
	{
		grassNoise = new FastNoise();
		grassNoise.SetNoiseType(NoiseType.SimplexFractal);
		grassNoise.SetFrequency(0.1f);
	}
	
	//	DECORATION
	private static float TreeChance 	= 0.005f;
	private static float ShroomChance 	= 0.075f;
	
	private static Object[] foliageTable = new Object[] {
			Material.NETHER_SPROUTS, 1,
			Material.WARPED_ROOTS, 1,
			Material.FERN, 12,
			Material.GRASS, 18};
	
	private static Object[] shroomTable = new Object[] {
			Material.RED_MUSHROOM, 4,
			Material.BROWN_MUSHROOM, 4,
			Material.CRIMSON_FUNGUS, 1,
			Material.WARPED_FUNGUS, 1};
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.DIORITE, 4,
			Material.BROWN_TERRACOTTA, 20,
			Material.RED_TERRACOTTA, 30,
			Material.BROWN_TERRACOTTA, 40,
			Material.GRANITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.08f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.MUSHROOM_FIELDS, Biome.MUSHROOM_FIELD_SHORE ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 0; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			switch (block.getMaterial())
			{
			case MYCELIUM:
				if (grassNoise.GetNoise(x, z) > 0)
				{
					replacementMaterial = Material.GRASS_BLOCK;
				}
				break;
			case STONE:
				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
				
				if (BlockUtil.isTopExposed(area, x, y, z))
				{
					replacementMaterial = Material.MYCELIUM;
				}
				break;
			default: break;
			}
			
			if (replacementMaterial != null)
			{
				area.setBlock(x, y, z, replacementMaterial);
			}
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Mushroom", world);
		}
		//	Shrooms
		else if (blockAbove != Material.WATER && block == Material.MYCELIUM && random.nextFloat() <= ShroomChance)
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, shroomTable));
		}
		//	Foliage
		else if (blockAbove != Material.WATER && block != Material.MYCELIUM && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
		
		//	Caves
		for (y = highestY; y > 0; y--)
		{
			block = area.getBlock(x, y, z);
			if (block.isOccluding() == false) { continue; }
			blockAbove = area.getBlock(x, y + 1, z);
			
			if (random.nextFloat() <= TreeChance * 3 && blockAbove == Material.CAVE_AIR && BlockUtil.isDirt(block) )
			{
				GenUtil.GenerateTree(random, area, x, y + 1, z, "SmallShrooms", world);
				continue;
			}
			
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
