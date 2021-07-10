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
import er.seven.worldgen.ChunkHandler;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class MountainsHandler extends ChunkHandler
{
	private FastNoise snowNoise;
	
	public MountainsHandler()
	{
		snowNoise = new FastNoise();
		snowNoise.SetNoiseType(NoiseType.SimplexFractal);
		snowNoise.SetFrequency(0.01f);
	}
	
	//	DECORATION
	private static float RockChance = 0.015f;
	private static float TreeChance = 0.02f;
	
	private static Object[] foliageTable = new Object[] {
			Material.AZURE_BLUET, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.GRASS, 20,
			Material.FERN, 80 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.GRAY_TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = world.getMaxHeight() - 1; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (block.getMaterial() == Material.SNOW)
				replacementMaterial = Material.POWDER_SNOW;
			else if (y > 90 && (y > 112 || random.nextInt(y - 90) > 4) &&
				block.getMaterial() != Material.SNOW &&
				(area.getBlock(x, y + 1, z).isOccluding() == false ||
				area.getBlock(x + 1, y, z).isOccluding() == false ||
				area.getBlock(x - 1, y, z).isOccluding() == false ||
				area.getBlock(x, y, z + 1).isOccluding() == false ||
				area.getBlock(x, y, z - 1).isOccluding() == false ||
				area.getBlock(x + 1, y - 1, z).isOccluding() == false ||
				area.getBlock(x - 1, y - 1, z).isOccluding() == false ||
				area.getBlock(x, y - 1, z + 1).isOccluding() == false ||
				area.getBlock(x, y - 1, z - 1).isOccluding() == false))
			{
				replacementMaterial = Material.SNOW_BLOCK;
			}
			else if (y > 80 && (y > 100 || random.nextInt(y - 80) > 4) &&
					snowNoise.GetNoise(x, z) > 0 &&
					(block.getMaterial() == Material.GRASS_BLOCK || block.getMaterial() == Material.DIRT ||
					(area.getBlock(x + 1, y, z) == Material.AIR ||
					area.getBlock(x - 1, y, z) == Material.AIR ||
					area.getBlock(x, y, z + 1) == Material.AIR ||
					area.getBlock(x, y, z - 1) == Material.AIR))
					)
			{
				replacementMaterial = Material.SNOW_BLOCK;
			}
			else if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case GRASS_BLOCK:
					if (area.getBlock(x + 1, y - 1, z).isAir() == true ||
						area.getBlock(x - 1, y - 1, z).isAir() == true ||
						area.getBlock(x, y - 1, z + 1).isAir() == true ||
						area.getBlock(x, y - 1, z - 1).isAir() == true)
					{
						replacementMaterial = Material.STONE;
					}
					else
					{
						replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
								Material.GRASS_BLOCK, 3,
								Material.COARSE_DIRT, 1
						});
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.STONE;
					}
					else
					{
						replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
								Material.COARSE_DIRT, 1,
								Material.GRAVEL, 3
						});
					}
					break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case SAND: 				replacementMaterial = Material.GRAVEL; break;
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
		
		//	Trees
		if (y < 100 && random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "WoodedMarches", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.STONE);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}