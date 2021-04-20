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

public class HighlandHandler extends ChunkHandler
{
	//	DECORATION
	private static float FoliageChance = 0.8f;
	private static float TreeChance = 0.003f;
	private static float HiveChance = 0.05f;
	
	private static Object[] foliageTable = new Object[] {
			Material.BLUE_ORCHID, 2,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.LILAC, 1,
			Material.GRASS, 100,
			Material.FERN, 40 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.MODIFIED_GRAVELLY_MOUNTAINS ); }
	
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
				case STONE:				
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.ANDESITE;
					}
					else if (area.getBlock(x, y + 1, z) == Material.AIR)
					{
						replacementMaterial = Material.GRASS_BLOCK;
					}
					else
					{
						replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					}
					
					break;
				case GRAVEL:			
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.ANDESITE;
					}
					else
					{
						replacementMaterial = Material.GRASS_BLOCK;
					}
					break;
				case DIRT:			
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.ANDESITE;
					}
					else
					{
						replacementMaterial = Material.GRAVEL;
					}
					break;
				case GRASS_BLOCK: 		replacementMaterial = Material.GRAVEL; break;
				case SAND: 				replacementMaterial = Material.GRAVEL; break;
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "AutumnForest", world);
		}
		//	Foliage
		else if (random.nextFloat() <= FoliageChance && blockAbove != Material.WATER && block == Material.GRASS_BLOCK)
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
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
