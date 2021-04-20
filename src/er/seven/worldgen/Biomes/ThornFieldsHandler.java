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

public class ThornFieldsHandler extends ChunkHandler
{
	private FastNoise undergrowthNoise;
	public ThornFieldsHandler()
	{
		undergrowthNoise = new FastNoise();
		undergrowthNoise.SetNoiseType(NoiseType.Simplex);
		undergrowthNoise.SetFrequency(0.02f);
	}
	
	//	DECORATION
	private static float TreeChance 	= 0.008f;
	private static float RockChance 	= 0.01f;
	private static float LogChance 		= 0.003f;
	private static float DeadBushChance = 0.05f;
	
	private static Object[] foliageTable = new Object[] {
			Material.SWEET_BERRY_BUSH, 3,
			Material.DARK_OAK_LEAVES, 12,
			Material.LARGE_FERN, 20,
			Material.FERN, 200 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.BLACKSTONE, 20,
			Material.GRANITE, 30,
			Material.STONE, 40,
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
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GIANT_TREE_TAIGA_HILLS ); }
	
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
				case COARSE_DIRT: 		replacementMaterial = Material.SOUL_SAND; break;
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
			GenUtil.GenerateTree(random, area, x, y + random.nextInt(2) - 3, z, "ThornFields", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateRock(random, area, x, y + 1, z, "Redwood", world);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.ACACIA_LOG);
		}
		//	Foliage
		else if (undergrowthNoise.GetNoise(x, z) > (random.nextInt(10) * 0.06f) && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
		//	Dead bushes
		else if (random.nextFloat() <= DeadBushChance && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, Material.DEAD_BUSH);
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
