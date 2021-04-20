package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class SnowyTundra extends ChunkHandler
{
	private FastNoise undergrowthNoise;
	public SnowyTundra()
	{
		undergrowthNoise = new FastNoise();
		undergrowthNoise.SetNoiseType(NoiseType.Simplex);
		undergrowthNoise.SetFrequency(0.02f);
	}
	
	//	DECORATION
	private static float UndergrowthChance = 0.95f;
	private static float RockChance = 0.001f;
	
	private static Object[] undergrowthTable = new Object[] {
			Material.SWEET_BERRY_BUSH, 3,
			Material.OAK_LEAVES, 10,
			Material.GRASS, 50 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.ANDESITE, 4,
			Material.STONE, 20,
			Material.GRANITE, 30,
			Material.DIORITE, 40,
			Material.BLACKSTONE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TUNDRA, Biome.FROZEN_RIVER ); }
	
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
				case GRASS_BLOCK: 		replacementMaterial = Material.SNOW_BLOCK; break;
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
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		//	Snow on things!
		for (y = highestY; y < world.getMaxHeight() - 1; y++)
		{
			if (area.getBlock(x, y, z).isOccluding() == true && area.getBlock(x, y + 1, z).isOccluding() == false)
			{
				if ((BlockUtil.isDirt(area.getBlock(x + 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x - 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z + 1)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z - 1)) == true ||
					area.getBlock(x + 1, y + 1, z) == Material.SNOW_BLOCK ||
					area.getBlock(x - 1, y + 1, z) == Material.SNOW_BLOCK ||
					area.getBlock(x, y + 1, z + 1) == Material.SNOW_BLOCK ||
					area.getBlock(x, y + 1, z - 1) == Material.SNOW_BLOCK) &&
					(BlockUtil.isLeaves(area.getBlock(x + 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x - 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z + 1)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z - 1)) == false ))
				{
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
					data.setLayers(7);
					area.setBlockData(x, y + 1, z, data);
				}
				else
				{					
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
					data.setLayers(3);
					area.setBlockData(x, y + 1, z, data);
				}
			}
		}
		
		y = highestY;
		
		//	Undergrowth
//		if (random.nextFloat() <= UndergrowthChance && block == Material.SNOW_BLOCK)
//		{
//			area.setBlock(x, y, z, Material.GRASS_BLOCK);
//			float noise = undergrowthNoise.GetNoise(x, z);
//			if ((noise > random.nextInt(10) * 0.05f) && (
//				BlockUtil.isDirt(area.getBlock(x + 1, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x - 1, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z + 1)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z - 1)) == true ||
//				area.getBlock(x + 1, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x - 1, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z + 1) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z - 1) == Material.SNOW_BLOCK ||
//				BlockUtil.isDirt(area.getBlock(x + 2, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x - 2, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z + 2)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z - 2)) == true ||
//				area.getBlock(x + 2, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x - 2, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z + 2) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z - 2) == Material.SNOW_BLOCK ||
//				BlockUtil.isDirt(area.getBlock(x + 3, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x - 3, y + 1, z)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z + 3)) == true ||
//				BlockUtil.isDirt(area.getBlock(x, y + 1, z - 3)) == true ||
//				area.getBlock(x + 3, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x - 3, y + 1, z) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z + 3) == Material.SNOW_BLOCK ||
//				area.getBlock(x, y + 1, z - 3) == Material.SNOW_BLOCK))
//			{
//				BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, undergrowthTable));
//			}
//			else if (noise < -0.5f)
//			{
//				area.setBlock(x, y + 1, z, Material.SNOW);
//				Snow data = (Snow)area.getBlockData(x, y + 1, z);
//				data.setLayers( 3 + random.nextInt(3) );
//				area.setBlockData(x, y + 1, z, data);
//			}
//		}
		//	Rocks
		if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateRock(random, area, x, y, z, "Tundra", world);
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
