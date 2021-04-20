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

public class OceanHandler extends ChunkHandler
{
	private FastNoise kelpNoise;
	private FastNoise reefNoise;
	private FastNoise spikeNoise;
	public OceanHandler()
	{
		kelpNoise = new FastNoise();
		kelpNoise.SetNoiseType(NoiseType.Simplex);
		kelpNoise.SetFrequency(0.03f);
		
		reefNoise = new FastNoise();
		reefNoise.SetNoiseType(NoiseType.SimplexFractal);
		reefNoise.SetFrequency(0.01f);
		
		spikeNoise = new FastNoise();
		spikeNoise.SetNoiseType(NoiseType.SimplexFractal);
		spikeNoise.SetFrequency(0.005f);
	}
	
	//	DECORATION
	private static float FoliageChance 	= 0.1f;
	private static float ReefChance 	= 0.025f;
	private static float SpikeChance 	= 0.025f;
	private static float CoralChance 	= 0.6f;
	
	private static Object[] foliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.TALL_SEAGRASS, 40,
			Material.SEAGRASS, 80 };
	
	private static Object[] coralTable = new Object[] {
			Material.SEA_PICKLE, 2,
			Material.BUBBLE_CORAL, 1,
			Material.FIRE_CORAL, 1,
			Material.TUBE_CORAL, 1,
			Material.HORN_CORAL, 1,
			Material.BRAIN_CORAL, 1,
			Material.BUBBLE_CORAL_FAN, 1,
			Material.FIRE_CORAL_FAN, 1,
			Material.TUBE_CORAL_FAN, 1,
			Material.HORN_CORAL_FAN, 1,
			Material.BRAIN_CORAL_FAN, 1};
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float OceanFloorChance = 0.00005f;
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN,
			Biome.DEEP_OCEAN, Biome.DEEP_WARM_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.OCEAN, Biome.WARM_OCEAN); }
	
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
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		int realX = area.getCenterX();
		int realZ = area.getCenterZ();
		
		int highestY = BlockUtil.getHighestGroundedY(realX, realZ, area);
		
		if (random.nextFloat() <= OceanFloorChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "oceanfloor", world);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		y = highestY;
		
		boolean placedReef = false;
		//	Reefs
		if (reefNoise.GetValue(x, z) > 0.45f)
		{
			//	Place reefs
			if (random.nextFloat() <= ReefChance && blockAbove == Material.WATER)
			{
				GenUtil.GenerateRock(random, area, x, y, z, "Reef", world);
				y = BlockUtil.getHighestSolidY(x, z, area);
				blockAbove = area.getBlock(x, highestY + 1, z);
				placedReef = true;
			}
			
			//	Place coral
			if (random.nextFloat() <= CoralChance && blockAbove == Material.WATER)
			{
				Material coralType = BlockUtil.weightedRandomMaterial(random, coralTable);
				
				switch (coralType)
				{
				case SEA_PICKLE:
					area.setBlock(x, y + 1, z, coralType);
					SeaPickle data = (SeaPickle)coralType.createBlockData();
					data.setPickles( 1 + (new Random()).nextInt(data.getMaximumPickles()) );
					area.setBlockData(x, y + 1, z, data);
					break;
				case SEAGRASS: area.setBlock(x, y + 1, z, coralType); break;
				case TALL_SEAGRASS: BlockUtil.setTallPlant(area, x, y + 1, z, coralType); break;
				default: area.setBlock(x, y + 1, z, coralType); break;
				}
			}
		}
		
		//	Sea spikes
		if (random.nextFloat() <= SpikeChance && spikeNoise.GetValue(x, z) > 0.6f)
		{
			int groundedY = BlockUtil.getHighestGroundedY(x, z, area);
			
			if (area.getBlock(x, groundedY + 1, z) == Material.WATER)
			{
				GenUtil.GenerateRock(random, area, x, groundedY + 1, z, "StoneSpikes", world);
			}
		}
		
		if (placedReef == false)
		{
			//	Kelp
			if (blockAbove == Material.WATER && kelpNoise.GetValue(x, z) > (random.nextInt(10) * 0.06f))
			{
				int maxHeight = 0;
				for (int n = highestY + 1; n < world.getSeaLevel() - 1; n++)
				{
					if (area.getBlock(x, n, z) == Material.WATER && area.getBlock(x, n + 1, z) == Material.WATER)
					{
						maxHeight = n - highestY - 1;
					}
					else
					{
						break;
					}
				}
				if (maxHeight > 0) { maxHeight = random.nextInt(maxHeight) + 5; }
				maxHeight = (int)( maxHeight * kelpNoise.GetValue(x, z) * random.nextFloat() );
				if (maxHeight > 25) { maxHeight = 25; }
				
				if (maxHeight >= 5)
				{
					for (int n = 1; n < maxHeight; n++)
					{
						area.setBlock(x, y + n, z, Material.KELP_PLANT);
					}
				}
			}
			
			//	Foliage
			if (blockAbove == Material.WATER && random.nextFloat() <= FoliageChance)
			{
				Material foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable);
				
				switch (foliageType)
				{
				case SEA_PICKLE:
					area.setBlock(x, y + 1, z, foliageType);
					SeaPickle data = (SeaPickle)foliageType.createBlockData();
					data.setPickles( 1 + (new Random()).nextInt(data.getMaximumPickles()) );
					area.setBlockData(x, y + 1, z, data);
					break;
				case SEAGRASS: area.setBlock(x, y + 1, z, foliageType); break;
				case TALL_SEAGRASS: BlockUtil.setTallPlant(area, x, y + 1, z, foliageType); break;
				default: break;
				}
			}
		}
		
		//	Caves
		for (y = highestY; y > 8; y--)
		{
			Material block = area.getBlock(x, y, z);
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
