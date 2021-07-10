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

public class BurntForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance 	= 0.01f;
	private static float RockChance 	= 0.012f;
	public static float FoliageChance	= 0.05f;
	
	private static Object[] foliageTable = new Object[] {
			Material.DEAD_BUSH, 1};
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.ANDESITE, 20,
			Material.STONE, 30,
			Material.GRANITE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.DARK_FOREST_HILLS ); }
	
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
					replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
							Material.COARSE_DIRT, 1,
							Material.BLACK_CONCRETE_POWDER, 5,
					});
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.BLACK_CONCRETE_POWDER;
					}
					break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case SAND: 				replacementMaterial = Material.COARSE_DIRT; break;
				case DIORITE: 			replacementMaterial = Material.ANDESITE; break;
				case GRANITE: 			replacementMaterial = Material.ANDESITE; break;
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Dead", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			if (random.nextBoolean()) GenUtil.GenerateRock(random, area, x, y + 1, z, "Marches", world); else GenUtil.GenerateRock(random, area, x, y, z, "Marches", world);
		}
		//	Foliage
		else if (random.nextFloat() <= FoliageChance && blockAbove != Material.WATER && (block == Material.COARSE_DIRT || block == Material.GRASS_BLOCK))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
