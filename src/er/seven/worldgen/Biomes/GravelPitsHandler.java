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

public class GravelPitsHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance		= 0.05f;
	private static float CactusChance 	= 0.005f;
	private static float BushChance 	= 0.005f;
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.RED_SANDSTONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.DEAD_BUSH, 1,
			Material.CACTUS, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.BADLANDS ); }
	
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
				case STONE:
					replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					
					if (replacementMaterial == Material.RED_SANDSTONE)
					{
						if (area.getBlock(x, y + 1, z).isOccluding() == false)
						{
							replacementMaterial = Material.GRAVEL;
						}
						else if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
						{
							if (random.nextInt(8) == 0) replacementMaterial = Material.CHISELED_RED_SANDSTONE;
						}
					}
					break;
				case RED_SAND:			replacementMaterial = Material.GRAVEL; break;
				case DIRT:				replacementMaterial = Material.GRAVEL; break;
				case DIORITE: 			replacementMaterial = Material.GRANITE; break;
				case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
				case YELLOW_TERRACOTTA:	replacementMaterial	= Material.SAND;
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
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER &&
			(area.getBlock(x + 1, y, z) == Material.WATER ||
			area.getBlock(x - 1, y, z) == Material.WATER ||
			area.getBlock(x, y, z + 1) == Material.WATER ||
			area.getBlock(x, y, z - 1) == Material.WATER ||
			area.getBlock(x + 1, y, z) == Material.GRASS_BLOCK ||
			area.getBlock(x - 1, y, z) == Material.GRASS_BLOCK ||
			area.getBlock(x, y, z + 1) == Material.GRASS_BLOCK ||
			area.getBlock(x, y, z - 1) == Material.GRASS_BLOCK))
		{
			if (random.nextInt(2) == 0) GenUtil.GenerateTree(random, area, x, y + 1, z, "Badlands", world); else GenUtil.GenerateTree(random, area, x, y + 1, z, "Dead", world);
		}
		//	Cactus
		else if (random.nextFloat() <= CactusChance && blockAbove != Material.WATER)
		{
			area.setBlock(x, y + 1, z, Material.CACTUS);
			area.setBlock(x, y, z, Material.RED_SAND);
		}
		//	Bushes
		else if (random.nextFloat() <= BushChance && blockAbove != Material.WATER)
		{
			area.setBlock(x, y + 1, z, Material.DEAD_BUSH);
		}
	}
}
