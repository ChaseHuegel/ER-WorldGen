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

public class AutumnForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float FoliageChance = 0.1f;
	private static float TreeChance = 0.02f;
	private static float HiveChance = 0.05f;
	private static float RockChance = 0.001f;
	private static float LogChance 	= 0.001f;
	
	private static Object[] foliageTable = new Object[] {
			Material.PUMPKIN, 2,
			Material.RED_MUSHROOM, 3,
			Material.OXEYE_DAISY, 2,
			Material.BROWN_MUSHROOM, 2,
			Material.DEAD_BUSH, 60,
			Material.LILAC, 1,
			Material.FERN, 100 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SAVANNA ); }
	
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
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GRASS_BLOCK: 		replacementMaterial = Material.PODZOL; break;
				case SAND: 				replacementMaterial = Material.GRAVEL; break;
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
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.STONE);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.OAK_LOG);
		}
		//	Foliage
		else if (random.nextFloat() <= FoliageChance && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
