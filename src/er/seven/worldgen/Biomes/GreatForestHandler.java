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

public class GreatForestHandler extends ChunkHandler
{
	private FastNoise undergrowthNoise;
	public GreatForestHandler()
	{
		undergrowthNoise = new FastNoise();
		undergrowthNoise.SetNoiseType(NoiseType.Simplex);
		undergrowthNoise.SetFrequency(0.02f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.008f;
	private static float RockChance = 0.002f;
	private static float BushChance = 0.01f;
	
	private static Object[] foliageTable = new Object[] {
			Material.SWEET_BERRY_BUSH, 3,
			Material.LARGE_FERN, 20,
			Material.FERN, 212 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.BLACKSTONE, 20,
			Material.GRANITE, 30,
			Material.STONE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float StumpChance 		= 0.00005f;
	private static float LogChance 			= 0.00005f;
	private static float FossilChance 		= 0.00005f;
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GIANT_SPRUCE_TAIGA_HILLS ); }
	
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
							if (random.nextBoolean()) replacementMaterial = Material.COBBLESTONE; else replacementMaterial = Material.MOSSY_COBBLESTONE;
						}
						else
						{
							replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
									Material.COARSE_DIRT, 1,
									Material.PODZOL, 4
							});
						}
						break;
					case DIRT:
						if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
						{
							if (random.nextBoolean()) replacementMaterial = Material.COBBLESTONE; else replacementMaterial = Material.MOSSY_COBBLESTONE;
						}
						break;	
				case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
				case DIORITE: 			replacementMaterial = Material.GRANITE; break;
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
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		int realX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + random.nextInt(8);
		int realZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + random.nextInt(8);
		
		int highestY = BlockUtil.getHighestSolidY(realX, realZ, area);
		
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "GreatForest", world);
		}
		//	Bushes
		else if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{			
			BlockUtil.buildBlob(random, area, x, y, z, 4, 4, 4, Material.DARK_OAK_LEAVES);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 5, 5, 5, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE);
		}
		//	Foliage
		else if (undergrowthNoise.GetNoise(x, z) > (random.nextInt(10) * 0.06f) && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
