package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bamboo.Leaves;
import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class JungleSwampHandler extends ChunkHandler
{
	private FastNoise bambooNoise;
	public JungleSwampHandler()
	{
		bambooNoise = new FastNoise();
		bambooNoise.SetNoiseType(NoiseType.Simplex);
		bambooNoise.SetFrequency(0.05f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.025f;
	private static float RockChance = 0.001f;
	private static float BushChance = 0.0015f;
	
	private static Object[] foliageTable = new Object[] {
			Material.WITHER_ROSE, 2,
			Material.RED_MUSHROOM, 2,
			Material.BROWN_MUSHROOM, 2,
			Material.CRIMSON_FUNGUS, 2,
			Material.WARPED_FUNGUS, 2,
			Material.TALL_GRASS, 10,
			Material.LARGE_FERN, 60,
			Material.FERN, 100,
			Material.GRASS, 180 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.GRANITE, 20,
			Material.STONE, 30,
			Material.ANDESITE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE); }
	
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
				case GRASS_BLOCK:
					replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
							Material.SOUL_SAND, 1,
							Material.PODZOL, 2,
							Material.WATER, 2,
					});
					
					if (replacementMaterial == Material.WATER)
					{
						if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
						{
							replacementMaterial = Material.PODZOL;
						}
					}
					break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GRAVEL: 			replacementMaterial = Material.SAND; break;
				case DIORITE: 			replacementMaterial = Material.GRANITE; break;
				case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
				case DIAMOND_ORE: 		replacementMaterial = Material.EMERALD_ORE; break;
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
		
		y = highestY;
			
		//	Vines
		for (int n = 1; n  < 32; n++)
		{
			if (area.getBlock(x, y + n, z) == Material.AIR &&
				(area.getBlock(x + 1, y + n, z) == Material.ACACIA_LEAVES ||
				area.getBlock(x - 1, y + n, z) == Material.ACACIA_LEAVES ||
				area.getBlock(x, y + n, z + 1) == Material.ACACIA_LEAVES ||
				area.getBlock(x, y + n, z - 1) == Material.ACACIA_LEAVES))
			{
				if (GenUtil.GenerateVine(random, area, x, y + n, z, 16) == true) { break; }
			}
		}
				
		//	Bamboo
		float noise = bambooNoise.GetNoise(x, z);
		if ((area.getBiome(x, z) == Biome.BAMBOO_JUNGLE || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && noise > random.nextInt(10) * 0.02f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			int bambooHeight = random.nextInt(5) + 6;
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
		//	Bamboo trees
		else if ((area.getBiome(x, z) == Biome.BAMBOO_JUNGLE || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && noise < 0.0f &&
				random.nextFloat() <= TreeChance * 0.2f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "BambooForest", world);
		}
		//	Light trees
		else if ((area.getBiome(x, z) != Biome.BAMBOO_JUNGLE && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) &&
				(area.getBiome(x, z) == Biome.JUNGLE_EDGE || area.getBiome(x, z) == Biome.MODIFIED_JUNGLE_EDGE || 
				area.getBiome(x, z) == Biome.JUNGLE_HILLS || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && 
				random.nextFloat() <= TreeChance * 0.2f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "JungleSwamp", world);
		}
		//	Trees
		else if ((area.getBiome(x, z) != Biome.BAMBOO_JUNGLE && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) &&
				(area.getBiome(x, z) != Biome.JUNGLE_EDGE && area.getBiome(x, z) != Biome.MODIFIED_JUNGLE_EDGE && 
				area.getBiome(x, z) != Biome.JUNGLE_HILLS && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) && 
				random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "JungleSwamp", world);
		}
		//	Bushes
		else if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.JUNGLE_LEAVES);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.BLACKSTONE);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
