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
import Util.FastNoise.NoiseType;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class MireHandler extends ChunkHandler
{
	private FastNoise swampNoise;
	private FastNoise grassNoise;
	
	public MireHandler()
	{
		swampNoise = new FastNoise();
		swampNoise.SetNoiseType(NoiseType.Simplex);
		swampNoise.SetFrequency(0.005f);
		
		grassNoise = new FastNoise();
		grassNoise.SetNoiseType(NoiseType.SimplexFractal);
		grassNoise.SetFrequency(0.05f);
	}
	
	//	DECORATION
	private static float LogChance 				= 0.0005f;
	private static float LilyPadChance 			= 0.3f;
	private static float ReedsChance 			= 0.02f;
	private static float WaterFoliageChance 	= 0.5f;
	private static float TreeChance 			= 0.025f;
	private static float FoliageChance			= 0.4f;
	
	private static Object[] foliageTable = new Object[] {
			Material.BROWN_MUSHROOM, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.BLUE_ORCHID, 1,
			Material.OAK_LEAVES, 5,
			Material.GRASS, 90 };
	
	private static Object[] foliageTable2 = new Object[] {
			Material.BROWN_MUSHROOM, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.BLUE_ORCHID, 1,
			Material.OAK_LEAVES, 5,
			Material.FERN, 40,
			Material.GRASS, 50 };
	
	private static Object[] waterFoliageTable = new Object[] {
			Material.SEAGRASS, 1 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.ANDESITE, 4,
			Material.STONE, 20,
			Material.GRANITE, 30,
			Material.DIORITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.07f;
	private static float CaveGrowthChance 		= 0.09f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SWAMP ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		swampNoise.SetSeed( (int)world.getSeed() );
		int seaLevel = world.getSeaLevel() - 1;
		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (block.getMaterial() == Material.GRASS_BLOCK) replacementMaterial = Material.PODZOL;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				float noise = swampNoise.GetNoise(x, z);
				
				//	Surface
				if (y == seaLevel && area.getBlock(x, y - 1, z).isOccluding() == true && noise > (random.nextInt(10) * 0.03f) && random.nextInt(10) <= 7)
				{					
					if (random.nextBoolean()) replacementMaterial = Material.PODZOL; else replacementMaterial = Material.BROWN_CONCRETE_POWDER;
				}
				//	Crust
				else if (y <= seaLevel && y >= seaLevel - 3)
				{
					if (block.getMaterial() == Material.STONE)
					{
						replacementMaterial = Material.COARSE_DIRT;
					}
				}
				else
				{
					switch (block.getMaterial())
					{
					case STONE:
						replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
						
						if (area.getBlock(x, y + 1, z).isOccluding() == false)
						{
							if (random.nextBoolean()) replacementMaterial = Material.PODZOL; else replacementMaterial = Material.COARSE_DIRT;
						}
						else if (replacementMaterial == Material.STONE)
						{
							if (area.getBlock(x + 1, y, z).isAir() == true ||
								area.getBlock(x - 1, y, z).isAir() == true ||
								area.getBlock(x, y, z + 1).isAir() == true ||
								area.getBlock(x, y, z - 1).isAir() == true)
							{
								if (random.nextInt(4) == 0) replacementMaterial = Material.MOSSY_COBBLESTONE;
							}
						}
						break;
					case GRASS_BLOCK: 		if (random.nextBoolean()) replacementMaterial = Material.PODZOL; else replacementMaterial = Material.BROWN_CONCRETE_POWDER; break;
					case DIORITE: 			replacementMaterial = Material.GRANITE; break;
					case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
					case GRAVEL: 			replacementMaterial = Material.CLAY; break;
					case COBBLESTONE: 		if (random.nextInt(100) < 40) replacementMaterial = Material.MOSSY_COBBLESTONE; break;
					case MOSSY_COBBLESTONE: if (random.nextInt(100) < 40) replacementMaterial = Material.MOSSY_STONE_BRICKS; break;
					case GOLD_ORE: 			if (random.nextBoolean()) replacementMaterial = Material.IRON_ORE; else replacementMaterial = Material.COAL_ORE; break;
					case DIAMOND_ORE: 		if (random.nextBoolean()) replacementMaterial = Material.IRON_ORE; else replacementMaterial = Material.COAL_ORE; break;
					default: break;
					}
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
		int seaLevel = world.getSeaLevel() - 1;
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		y = highestY;
		
		//	Trees
		float noise = grassNoise.GetNoise(x, z);
		if (random.nextFloat() <= TreeChance && noise > 0 && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "WoodedMountains", world);
		}
		//	Fallen Logs
		else if (random.nextFloat() <= LogChance && BlockUtil.isDirt(block) )
		{				
			GenUtil.GenerateLog(random, area, x, y + 1, z, Material.SPRUCE_LOG);
		}
		//	Floating logs
		else if (random.nextFloat() <= LogChance && block == Material.WATER && area.getBlock(x, y + 1, z).isAir() == true)
		{				
			GenUtil.GenerateLog(random, area, x, y, z, Material.SPRUCE_LOG);
		}
		//	Lily pads
		else if (random.nextFloat() <= LilyPadChance && blockAbove == Material.WATER && area.getBlock(x, y + 2, z) != Material.WATER)
		{
			noise = swampNoise.GetNoise(x, z);
			if (noise > -(random.nextInt(10) * 0.03f))
			{
				area.setBlock(x, y + 2, z, Material.LILY_PAD);
			}
		}
		//	Reeds
		else if (random.nextFloat() <= ReedsChance && BlockUtil.isDirt(block) && area.getBlock(x, y + 1, z).isAir() == true)
		{
			GenUtil.GenerateSugarcane(random, area, x, y, z);
		}
		//	Water foliage
		else if (random.nextFloat() <= WaterFoliageChance && blockAbove == Material.WATER)
		{
			Material foliageType = BlockUtil.weightedRandomMaterial(random, waterFoliageTable);
			
			switch (foliageType)
			{
			case SEA_PICKLE:
				area.setBlock(x, y + 1, z, foliageType);
				SeaPickle data = (SeaPickle) Material.SEA_PICKLE.createBlockData();
				data.setPickles( 1 + (new Random()).nextInt(data.getMaximumPickles()) );
				area.setBlockData(x, y + 1, z, data);
				break;
			case SEAGRASS: area.setBlock(x, y + 1, z, foliageType); break;
			default: break;
			}
		}
		//	Foliage
		else if (random.nextFloat() <= FoliageChance && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			Material foliageType = null;
			
			noise = grassNoise.GetNoise(x, z);
			if (noise > -(random.nextInt(10) * 0.01f))
			{
				foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable);
			}
			else
			{
				foliageType = BlockUtil.weightedRandomMaterial(random, foliageTable2);
			}
			
			BlockUtil.setFoliage(area, x, y + 1, z, foliageType);
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
