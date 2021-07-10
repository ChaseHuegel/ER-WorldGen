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

public class MysticForestHandler extends ChunkHandler
{	
	//	DECORATION
	private static float TreeChance = 0.02f;
	private static float ShroomChance = 0.0035f;
	
	private static Object[] foliageTable = new Object[] {
			Material.GRASS, 300,
			Material.CRIMSON_ROOTS, 3,
			Material.WARPED_ROOTS, 6,
			Material.NETHER_SPROUTS, 6,
			Material.CRIMSON_FUNGUS, 1,
			Material.WARPED_FUNGUS, 1,
			Material.LIGHT, 1 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.GRAVEL, 4,
			Material.BLACKSTONE, 20,
			Material.BASALT, 30,
			Material.STONE, 40,
			Material.GRANITE, 52,
			Material.BLACKSTONE, 58,
			Material.DIORITE, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.CRIMSON_FUNGUS, 1,
			Material.WARPED_FUNGUS, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.TALL_BIRCH_HILLS ); }
	
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
		
		//	Vines
		for (int n = 1; n  < 16; n++)
		{
			if (area.getBlock(x, y + n, z) == Material.AIR &&
				(area.getBlock(x + 1, y + n, z) == Material.DARK_OAK_LEAVES ||
				area.getBlock(x - 1, y + n, z) == Material.DARK_OAK_LEAVES ||
				area.getBlock(x, y + n, z + 1) == Material.DARK_OAK_LEAVES ||
				area.getBlock(x, y + n, z - 1) == Material.DARK_OAK_LEAVES))
			{
				if (GenUtil.GenerateVine(random, area, x, y + n, z, 16) == true) { break; }
			}
		}
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y, z, "Mystic", world);
		}
		//	Shrooms
		else if (random.nextFloat() <= ShroomChance && BlockUtil.isDirt(block) )
		{
			Material bushMaterial;
			if (random.nextBoolean()) bushMaterial = Material.BROWN_MUSHROOM_BLOCK; else bushMaterial = Material.RED_MUSHROOM_BLOCK;
			
			BlockUtil.buildBlob(random, area, x, y + 1, z, 1, 1, 1, bushMaterial);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
