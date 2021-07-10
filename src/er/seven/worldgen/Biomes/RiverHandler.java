package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.SeaPickle;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class RiverHandler extends ChunkHandler
{	
	private FastNoise lilypadNoise;
	public RiverHandler()
	{
		lilypadNoise = new FastNoise();
		lilypadNoise.SetNoiseType(NoiseType.SimplexFractal);
		lilypadNoise.SetFrequency(1.0f);
	}
	
	//	DECORATION
	private static float LilypadChance = 0.5f;
	private static float ReedsChance = 0.15f;
	private static float WaterFoliageChance = 0.5f;
	
	private static Object[] foliageTable = new Object[] {
			Material.LILAC, 1,
			Material.ROSE_BUSH, 1,
			Material.PEONY, 1,
			Material.TALL_GRASS, 10,
			Material.FERN, 10,
			Material.GRASS, 30 };
	
	private static Object[] waterFoliageTable = new Object[] {
			Material.TALL_SEAGRASS, 1,
			Material.SEAGRASS, 2 };
	
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
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.RIVER ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true || block.getMaterial() == Material.WATER) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case GRASS_BLOCK:		replacementMaterial = Material.COARSE_DIRT; break;
				case SAND:				replacementMaterial = Material.COARSE_DIRT; break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case DIAMOND_ORE:		replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
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
		
		//	Lilypads
		if (blockAbove == Material.WATER && area.getBlock(x, highestY + 2, z) == Material.AIR && random.nextFloat() <= LilypadChance && lilypadNoise.GetNoise(x, z) > 0)
		{
			area.setBlock(x, y + 2, z, Material.LILY_PAD);
		}
		else if (blockAbove == Material.WATER && area.getBlock(x, highestY + 2, z) == Material.WATER && area.getBlock(x, highestY + 3, z) == Material.AIR && random.nextFloat() <= LilypadChance && lilypadNoise.GetNoise(x, z) > 0)
		{
			area.setBlock(x, y + 3, z, Material.LILY_PAD);
		}
		//	Reeds
		else if (random.nextFloat() <= ReedsChance && BlockUtil.isDirt(block) && area.getBlock(x, y + 1, z).isAir() == true && lilypadNoise.GetNoise(x, z) > 0)
		{
			GenUtil.GenerateSugarcane(random, area, x, y, z);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && block == Material.GRASS_BLOCK)
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
		//	Water foliage
		else if (blockAbove == Material.WATER && random.nextFloat() <= WaterFoliageChance)
		{
			Material foliageType = BlockUtil.weightedRandomMaterial(random, waterFoliageTable);
			
			switch (foliageType)
			{
			case SEA_PICKLE:
				area.setBlock(x, y + 1, z, foliageType);
				SeaPickle data = (SeaPickle)foliageType.createBlockData();
				data.setPickles( 1 + (new Random()).nextInt(data.getMaximumPickles()) );
				area.setBlockData(x, y + 1, z, data);
				break;
			case TALL_SEAGRASS:
				if (area.getBlock(x, y + 2, z) != Material.WATER) { foliageType = Material.SEAGRASS; break; }
				BlockUtil.setTallPlant(area, x, y + 1, z, foliageType); break;
			case SEAGRASS: area.setBlock(x, y + 1, z, foliageType); break;
			default: break;
			}
		}
	}
}
