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

public class MarshHandler extends ChunkHandler
{
	private FastNoise swampNoise;
	private FastNoise grassNoise;
	
	public MarshHandler()
	{
		swampNoise = new FastNoise();
		swampNoise.SetNoiseType(NoiseType.Simplex);
		swampNoise.SetFrequency(0.005f);
		
		grassNoise = new FastNoise();
		grassNoise.SetNoiseType(NoiseType.SimplexFractal);
		grassNoise.SetFrequency(0.005f);
	}
	
	//	DECORATION
	private static float RockChance 			= 0.001f;
	private static float BushChance 			= 0.0015f;
	private static float LogChance 				= 0.0005f;
	private static float LilyPadChance 			= 0.3f;
	private static float ReedsChance 			= 0.05f;
	private static float WaterFoliageChance 	= 0.5f;
	private static float TreeChance 			= 0.01f;
	
	private static Object[] foliageTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.BLUE_ORCHID, 1,
			Material.GRASS, 100 };
	
	private static Object[] foliageTable2 = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.BLUE_ORCHID, 1,
			Material.TALL_GRASS, 40,
			Material.GRASS, 55 };
	
	private static Object[] waterFoliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.SEAGRASS, 100 };
	
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
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				float noise = swampNoise.GetNoise(x, z);
				
				//	Surface
				if (y == seaLevel && area.getBlock(x, y - 1, z).isOccluding() == true && noise > (random.nextInt(10) * 0.03f) && random.nextInt(10) <= 7)
				{					
					replacementMaterial = Material.GRASS_BLOCK;
				}
				//	Crust
				else if (y <= seaLevel && y >= seaLevel - 3)
				{
					if (block.getMaterial() == Material.STONE)
					{
						replacementMaterial = Material.CLAY;
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
							if (random.nextBoolean()) replacementMaterial = Material.DIRT; else replacementMaterial = Material.COARSE_DIRT;
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
					case DIORITE: 			replacementMaterial = Material.GRANITE; break;
					case ANDESITE: 			replacementMaterial = Material.GRANITE; break;
					case GRAVEL: 			replacementMaterial = Material.CLAY; break;
					case COBBLESTONE: 		if (random.nextInt(100) < 40) replacementMaterial = Material.MOSSY_COBBLESTONE; break;
					case MOSSY_COBBLESTONE: if (random.nextInt(100) < 40) replacementMaterial = Material.MOSSY_STONE_BRICKS; break;
					case GOLD_ORE: 			if (random.nextBoolean()) replacementMaterial = Material.IRON_ORE; else replacementMaterial = Material.COAL_ORE;
					case DIAMOND_ORE: 		if (random.nextBoolean()) replacementMaterial = Material.IRON_ORE; else replacementMaterial = Material.COAL_ORE;
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
		if (y > seaLevel && random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Swamp", world);
		}
		//	Bushes
		else if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.OAK_LEAVES);
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
		//	Floating logs
		else if (random.nextFloat() <= LogChance && block == Material.WATER && area.getBlock(x, y + 1, z).isAir() == true)
		{				
			GenUtil.GenerateLog(random, area, x, y, z, Material.DARK_OAK_LOG);
		}
		//	Lily pads
		else if (random.nextFloat() <= LilyPadChance && blockAbove == Material.WATER && area.getBlock(x, y + 2, z) != Material.WATER)
		{
			float noise = swampNoise.GetNoise(x, z);
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
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			Material foliageType = null;
			
			float noise = grassNoise.GetNoise(x, z);
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
	}
}
