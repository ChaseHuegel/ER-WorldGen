package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bamboo.Leaves;
import org.bukkit.block.data.type.Cocoa;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class JungleHandler extends ChunkHandler
{
	private FastNoise bambooNoise;
	public JungleHandler()
	{
		bambooNoise = new FastNoise();
		bambooNoise.SetNoiseType(NoiseType.SimplexFractal);
		bambooNoise.SetFrequency(0.035f);
	}
	
	//	DECORATION
	private static float TreeChance = 0.02f;
	private static float RockChance = 0.001f;
	
	private static Object[] foliageTable = new Object[] {
			Material.WITHER_ROSE, 3,
			Material.MELON, 2,
			Material.TALL_GRASS, 10,
			Material.LARGE_FERN, 60,
			Material.FERN, 140,
			Material.GRASS, 140 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.ANDESITE, 20,
			Material.STONE, 30,
			Material.GRANITE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float StructureChance = 0.00002f;
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.JUNGLE, Biome.JUNGLE_EDGE, Biome.JUNGLE_HILLS, Biome.MODIFIED_JUNGLE, Biome.MODIFIED_JUNGLE_EDGE); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = world.getMaxHeight() - 1; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GRASS_BLOCK: 		replacementMaterial = Material.GRASS_BLOCK; break;
				case GRAVEL: 			replacementMaterial = Material.SAND; break;
				case DIORITE: 			replacementMaterial = Material.ANDESITE; break;
				case GRANITE: 			replacementMaterial = Material.ANDESITE; break;
				case DIAMOND_ORE: 		replacementMaterial = Material.EMERALD_ORE; break;
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
		
		if (random.nextFloat() <= StructureChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "jungle", world);
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
		
		//	Cocoa
		for (int i = 0; i < 8; i++)
		{
			y = random.nextInt(12) + highestY;
			
			if (area.getBlock(x, y, z) == Material.JUNGLE_LEAVES && area.getBlock(x, y - 1, z) == Material.AIR)
			{				
				BlockFace face = null;
				if (area.getBlock(x + 1, y - 1, z) == Material.JUNGLE_WOOD) { face = BlockFace.EAST; }
				if (area.getBlock(x - 1, y - 1, z) == Material.JUNGLE_WOOD) { face = BlockFace.WEST; }
				if (area.getBlock(x, y - 1, z + 1) == Material.JUNGLE_WOOD) { face = BlockFace.SOUTH; }
				if (area.getBlock(x, y - 1, z - 1) == Material.JUNGLE_WOOD) { face = BlockFace.NORTH; }
				
				if (face != null)
				{
					area.setBlock(x, y - 1, z, Material.COCOA);
					Cocoa data = (Cocoa)Material.COCOA.createBlockData();
					data.setFacing(face);
					data.setAge( random.nextInt(data.getMaximumAge()) );
					area.setBlockData(x, y - 1, z, data);
					break;
				}
			}
		}
		
		y = highestY;
		
		//	Vines
		for (int n = 1; n  < 16; n++)
		{
			if (area.getBlock(x, y + n, z) == Material.AIR &&
				(area.getBlock(x + 1, y + n, z) == Material.JUNGLE_LEAVES ||
				area.getBlock(x - 1, y + n, z) == Material.JUNGLE_LEAVES ||
				area.getBlock(x, y + n, z + 1) == Material.JUNGLE_LEAVES ||
				area.getBlock(x, y + n, z - 1) == Material.JUNGLE_LEAVES))
			{
				if (GenUtil.GenerateVine(random, area, x, y + n, z, 16) == true) { break; }
			}
		}
			
		//	Bamboo
		float noise = bambooNoise.GetNoise(x, z);
		if ((area.getBiome(x, z) == Biome.BAMBOO_JUNGLE || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && noise > random.nextInt(10) * 0.02f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			int bambooHeight = random.nextInt(5) + 4;
			for (int i = 0; i < bambooHeight; i++)
			{
				area.setBlock(x, y + 1 + i, z, Material.BAMBOO);
				if (i == bambooHeight - 1)
				{
					Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
					data.setLeaves(Leaves.LARGE);
					area.setBlockData(x, y + 1 + i, z, data);
				}
				else if (i >= bambooHeight - 3)
				{
					Bamboo data = (Bamboo)Material.BAMBOO.createBlockData();
					data.setLeaves(Leaves.SMALL);
					area.setBlockData(x, y + 1 + i, z, data);
				}
			}
		}
		//	Bamboo trees
		else if ((area.getBiome(x, z) == Biome.BAMBOO_JUNGLE || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && noise < 0.0f &&
				random.nextFloat() <= TreeChance * 0.35f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "BambooForest", world);
		}
		//	Light trees
		else if ((area.getBiome(x, z) != Biome.BAMBOO_JUNGLE && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) &&
				(area.getBiome(x, z) == Biome.JUNGLE_EDGE || area.getBiome(x, z) == Biome.MODIFIED_JUNGLE_EDGE || 
				area.getBiome(x, z) == Biome.JUNGLE_HILLS || area.getBiome(x, z) == Biome.BAMBOO_JUNGLE_HILLS) && 
				random.nextFloat() <= TreeChance * 0.2f && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Jungle", world);
		}
		//	Trees
		else if ((area.getBiome(x, z) != Biome.BAMBOO_JUNGLE && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) &&
				(area.getBiome(x, z) != Biome.JUNGLE_EDGE && area.getBiome(x, z) != Biome.MODIFIED_JUNGLE_EDGE && 
				area.getBiome(x, z) != Biome.JUNGLE_HILLS && area.getBiome(x, z) != Biome.BAMBOO_JUNGLE_HILLS) && 
				random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Jungle", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.MOSSY_COBBLESTONE);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
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
