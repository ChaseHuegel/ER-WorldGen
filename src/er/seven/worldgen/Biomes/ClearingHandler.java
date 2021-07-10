package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class ClearingHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.0075f;
	private static float HiveChance = 0.05f;
	private static float RockChance = 0.001f;
	private static float LogChance 	= 0.001f;
	
	private static Object[] foliageTable = new Object[] {
			Material.AZURE_BLUET, 2,
			Material.DANDELION, 2,
			Material.GRASS, 200 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float StumpChance 		= 0.00004f;
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.FOREST ); }
	
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
					if (area.getBlock(x + 1, y, z) == Material.WATER ||
						area.getBlock(x - 1, y, z) == Material.WATER ||
						area.getBlock(x, y, z + 1) == Material.WATER ||
						area.getBlock(x, y, z - 1) == Material.WATER)
					{
						replacementMaterial = Material.COARSE_DIRT;
					}
					break;		
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case SAND: 				replacementMaterial = Material.COARSE_DIRT; break;
				case ANDESITE: 			replacementMaterial = Material.DIORITE; break;
				case GRANITE: 			replacementMaterial = Material.DIORITE; break;
				case GOLD_ORE:			replacementMaterial = Material.COAL_ORE; break;
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
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		//	Bee Hives
		if (random.nextFloat() <= HiveChance)
		{
			y = random.nextInt(16) + 64;
			Material randomBlock = area.getBlock(x, y, z);
			
			if (area.getBlock(x, y - 1, z) == Material.AIR &&
				Tag.LEAVES.getValues().contains(randomBlock) == true)
			{
				area.setBlock(x, y - 1, z, Material.BEE_NEST);
				Main.SpawnEntity(world, x, y - 2, z, EntityType.BEE);
			}
		}
		
		y = highestY;
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Clearing", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.STONE);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.STRIPPED_SPRUCE_LOG);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && block == Material.GRASS_BLOCK)
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
