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

public class DarkForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.025f;
	private static float HiveChance = 0.025f;
	private static float RockChance = 0.004f;
	private static float LogChance 	= 0.003f;
	
	private static Object[] foliageTable = new Object[] {
			Material.RED_MUSHROOM, 6,
			Material.BROWN_MUSHROOM, 6,
			Material.WITHER_ROSE, 1,
			Material.FERN, 20,
			Material.GRASS, 8};
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.ANDESITE, 30,
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
				case STONE:
					replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					if (area.getBlockData(x, y + 1, z).getMaterial().isOccluding() == false)
					{
						if (random.nextBoolean()) replacementMaterial = Material.DIRT; else replacementMaterial = Material.GRAVEL;
					}
					break;
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GOLD_ORE:			replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
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
		
		//	Vines
		for (int n = 1; n  < 16; n++)
		{
			if (area.getBlock(x, y + n, z) == Material.AIR &&
				(area.getBlock(x + 1, y + n, z) == Material.OAK_LEAVES ||
				area.getBlock(x - 1, y + n, z) == Material.OAK_LEAVES ||
				area.getBlock(x, y + n, z + 1) == Material.OAK_LEAVES ||
				area.getBlock(x, y + n, z - 1) == Material.OAK_LEAVES))
			{
				if (GenUtil.GenerateVine(random, area, x, y + n, z, 6) == true) { break; }
			}
		}
		
		//	Cobwebs
		for (int i = 0; i < random.nextInt(4) + 2; i++)
		{
			y = random.nextInt(12) + highestY;
			Material randomBlock = area.getBlock(x, y, z);
			
			if (area.getBlock(x, y - 1, z) == Material.AIR &&
				(randomBlock != Material.AIR &&
				randomBlock != Material.CAVE_AIR))
			{
				area.setBlock(x, y - 1, z, Material.COBWEB);
			}
		}
		y = highestY;
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "ShroomForest", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.ANDESITE);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.DARK_OAK_LOG);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && random.nextBoolean() && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
