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
import er.seven.worldgen.ChunkHandler;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class MountainSpringsHandler extends ChunkHandler
{
	private FastNoise poolNoise;
	public MountainSpringsHandler()
	{
		poolNoise = new FastNoise();
		poolNoise.SetNoiseType(NoiseType.Simplex);
		poolNoise.SetFrequency(0.1f);
	}
	
	//	DECORATION
	private static float WaterFoliageChance = 0.5f;
	
	private static Object[] foliageTable = new Object[] {
			Material.LILAC, 6,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.GRASS, 80,
			Material.FERN, 20 };
	
	private static Object[] waterFoliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.SEAGRASS, 10 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.GRAY_TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float FossilChance = 0.00002f;
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.MAGMA_BLOCK, 1,
			Material.BLACKSTONE_SLAB, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
//			if (y > 110 && (y > 132 || random.nextInt(y - 110) > 4) &&
//					block.getMaterial() != Material.SNOW &&
//					(area.getBlock(x, y + 1, z).isOccluding() == false ||
//					area.getBlock(x + 1, y, z).isOccluding() == false ||
//					area.getBlock(x - 1, y, z).isOccluding() == false ||
//					area.getBlock(x, y, z + 1).isOccluding() == false ||
//					area.getBlock(x, y, z - 1).isOccluding() == false ||
//					area.getBlock(x + 1, y - 1, z).isOccluding() == false ||
//					area.getBlock(x - 1, y - 1, z).isOccluding() == false ||
//					area.getBlock(x, y - 1, z + 1).isOccluding() == false ||
//					area.getBlock(x, y - 1, z - 1).isOccluding() == false))
//				{
//					replacementMaterial = Material.SNOW_BLOCK;
//				}
				if (y > 16 || random.nextInt(y) > 8)
				{
					switch (block.getMaterial())
					{
					case GRASS_BLOCK:
						if (area.getBlock(x + 1, y - 1, z).isAir() == true ||
							area.getBlock(x - 1, y - 1, z).isAir() == true ||
							area.getBlock(x, y - 1, z + 1).isAir() == true ||
							area.getBlock(x, y - 1, z - 1).isAir() == true)
						{
							replacementMaterial = Material.STONE;
						}
						break;
					case DIRT:
						if (area.getBlock(x + 1, y, z).isAir() == true ||
								area.getBlock(x - 1, y, z).isAir() == true ||
								area.getBlock(x, y, z + 1).isAir() == true ||
								area.getBlock(x, y, z - 1).isAir() == true)
						{
							replacementMaterial = Material.STONE;
						}
						else
						{
							replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
									Material.COARSE_DIRT, 1,
									Material.GRAVEL, 3
							});
						}
						break;
					case STONE:
						if (area.getBlock(x, y + 1, z) != Material.AIR) 
						{
							replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
							break;
						}
						
						if (poolNoise.GetNoise(x, y, z) < -0.5)
						{
							replacementMaterial = Material.WATER;
							
							if (area.getBlock(x + 1, y, z).isAir() == true ||
								area.getBlock(x - 1, y, z).isAir() == true ||
								area.getBlock(x, y, z + 1).isAir() == true ||
								area.getBlock(x, y, z - 1).isAir() == true)
							{
								replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
										Material.SANDSTONE, 1,
										Material.SMOOTH_SANDSTONE, 1
								});
							}
						}
						else if (poolNoise.GetNoise(x, y, z) < -0.15)
						{
							replacementMaterial = Material.GRASS_BLOCK;
						}
						else
						{
							replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); 
						}
						break;
					case SAND: 				replacementMaterial = Material.GRAVEL; break;
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
		
		if (random.nextFloat() <= FossilChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "fossil", world);
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
		
		//	Foliage
		if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
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
