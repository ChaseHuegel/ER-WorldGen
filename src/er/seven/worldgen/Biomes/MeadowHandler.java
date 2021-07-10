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

public class MeadowHandler extends ChunkHandler
{
	private FastNoise flowerNoise;
	public MeadowHandler()
	{
		flowerNoise = new FastNoise();
		flowerNoise.SetNoiseType(NoiseType.SimplexFractal);
		flowerNoise.SetFrequency(0.05f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.0005f;
	
	private static Object[] foliageTable = new Object[] {
			Material.GRASS, 1};
	
	private static Material[] flowers = new Material[] {
			Material.DANDELION, 
			Material.WHITE_TULIP, 
			Material.ORANGE_TULIP, 
			Material.RED_TULIP, 
			Material.PINK_TULIP, 
			Material.BLUE_ORCHID, 
			Material.ALLIUM, 
			Material.AZURE_BLUET, 
			Material.OXEYE_DAISY, 
			Material.CORNFLOWER, 
			Material.LILY_OF_THE_VALLEY, 
			Material.PINK_TULIP,};
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.GRANITE, 20,
			Material.DIORITE, 30,
			Material.STONE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.FLOWER_FOREST ); }
	
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
							Material.MOSS_BLOCK, 2,
							Material.GRASS_BLOCK, 3
					});
					break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case DIORITE: 			replacementMaterial = Material.GRANITE; break;
				case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
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
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Meadow", world);
		}
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block) && flowerNoise.GetNoise(x, z) > 0)
		{			
			int index = (int) (flowers.length * flowerNoise.GetNoise(x, z));
			
			area.setBlock(x, y + 1, z, flowers[index]);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			Material foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable);
			
			if (BlockUtil.isLeaves(foliageType))
			{
				BlockUtil.setPersistentLeaves(area, x, y + 1, z, foliageType);
			}
			else if (BlockUtil.isTallPlant(foliageType))
			{
				BlockUtil.setTallPlant(area, x, y + 1, z, foliageType);
			}
			else
			{
				area.setBlock(x, y + 1, z, foliageType);
			}
		}
	}
}