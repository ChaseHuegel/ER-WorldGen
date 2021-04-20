package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bamboo.Leaves;
import org.bukkit.block.data.type.Cocoa;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CherryBlossomHandler extends ChunkHandler
{
	private FastNoise bambooNoise;
	public CherryBlossomHandler()
	{
		bambooNoise = new FastNoise();
		bambooNoise.SetNoiseType(NoiseType.SimplexFractal);
		bambooNoise.SetFrequency(0.05f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.25f;
	
	private static Object[] foliageTable = new Object[] {
			Material.ALLIUM, 3,
			Material.WITHER_ROSE, 1,
			Material.FERN, 20,
			Material.GRASS, 70 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.GRANITE, 20,
			Material.STONE, 30,
			Material.ANDESITE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.BAMBOO_JUNGLE, Biome.BAMBOO_JUNGLE_HILLS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = world.getMaxHeight() - 1; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "CherryBlossom", world);
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
		
		//	Bamboo
		float noise = bambooNoise.GetNoise(x, z);
		if ( noise > 0.4 && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			int bambooHeight = random.nextInt(5) + 4;
			for (int i = 0; i < bambooHeight; i++)
			{
				area.setBlock(x, y + 1 + i, z, Material.BAMBOO);
				if (i == bambooHeight - 1)
				{
					Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
					data.setLeaves(Leaves.LARGE);
					area.setBlockData(x, y + 1 + i, z, data);
				}
				else if (i >= bambooHeight - 3)
				{
					Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
					data.setLeaves(Leaves.SMALL);
					area.setBlockData(x, y + 1 + i, z, data);
				}
			}
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
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
