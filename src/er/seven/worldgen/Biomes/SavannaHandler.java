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

public class SavannaHandler extends ChunkHandler
{
	private FastNoise grassNoise;
	public SavannaHandler()
	{
		grassNoise = new FastNoise();
		grassNoise.SetNoiseType(NoiseType.SimplexFractal);
		grassNoise.SetFrequency(0.005f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.001f;
	
	private static Object[] foliageTable = new Object[] {
			Material.SUNFLOWER, 1,
			Material.ACACIA_LEAVES, 5,
			Material.DEAD_BUSH, 100,
			Material.GRASS, 400 };
	
	private static Object[] foliageTable2 = new Object[] {
			Material.ROSE_BUSH, 1,
			Material.DEAD_BUSH, 10,
			Material.TALL_GRASS, 400 };

	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.ANDESITE, 4,
			Material.TERRACOTTA, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.STONE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SAVANNA, Biome.SHATTERED_SAVANNA ); }
	
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
						replacementMaterial = Material.TERRACOTTA;
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.TERRACOTTA;
					}
					break;	
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GRANITE: 			replacementMaterial = Material.ANDESITE; break;
				case DIORITE: 			replacementMaterial = Material.ANDESITE; break;
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
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Savanna", world);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			Material foliageType = null;
			
			float noise = grassNoise.GetNoise(x, z);
			if (noise > -(random.nextInt(10) * 0.01f))
			{
				foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable);
			}
			else
			{
				foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable2);
			}
			
			BlockUtil.setFoliage(area, x, y + 1, z, foliageType);
		}
	}
}
