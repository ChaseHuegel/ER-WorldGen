package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Cocoa;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class DesertHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance		= 0.05f;
	private static float CactusChance 	= 0.007f;
	private static float BushChance 	= 0.025f;
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.SANDSTONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	STRUCTURES
	private static float FossilChance = 0.00002f;
	private static float StructureChance = 0.00002f;
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.DESERT, Biome.DESERT_HILLS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = world.getMaxHeight() - 1; y > 8; y--) 
		{
			Material block = area.getBlock(x, y, z);
			
			if (BlockUtil.isAir(block) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block)
				{
				case STONE:
					replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
					
					if (replacementMaterial == Material.SANDSTONE)
					{
						if (area.getBlock(x, y + 1, z).isOccluding() == false)
						{
							replacementMaterial = Material.SAND;
						}
						else if (area.getBlock(x + 1, y, z).isAir() == true ||
								area.getBlock(x - 1, y, z).isAir() == true ||
								area.getBlock(x, y, z + 1).isAir() == true ||
								area.getBlock(x, y, z - 1).isAir() == true)
						{
							if (random.nextInt(8) == 0) replacementMaterial = Material.CHISELED_SANDSTONE;
						}
					}
					break;
				case GRAVEL:				replacementMaterial = Material.SAND; break;
				case GRANITE: 				replacementMaterial = Material.DIORITE; break;
				case ANDESITE: 				replacementMaterial = Material.DIORITE; break;
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
		else if (random.nextFloat() <= StructureChance)
		{
			GenUtil.GenerateObject(random, area, realX, highestY, realZ, "desert", world);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		//	Cocoa
		for (int i = 0; i < 8; i++)
		{
			y = random.nextInt(12) + highestY;
			
			if (area.getBlock(x, y, z) == Material.JUNGLE_LEAVES && area.getBlock(x, y - 1, z) == Material.AIR)
			{
				BlockFace face = null;
				if (area.getBlock(x + 1, y - 1, z) == Material.STRIPPED_BIRCH_WOOD) { face = BlockFace.EAST; }
				if (area.getBlock(x - 1, y - 1, z) == Material.STRIPPED_BIRCH_WOOD) { face = BlockFace.WEST; }
				if (area.getBlock(x, y - 1, z + 1) == Material.STRIPPED_BIRCH_WOOD) { face = BlockFace.SOUTH; }
				if (area.getBlock(x, y - 1, z - 1) == Material.STRIPPED_BIRCH_WOOD) { face = BlockFace.NORTH; }
				
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
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER &&
			(area.getBlock(x + 1, y, z) == Material.WATER ||
			area.getBlock(x - 1, y, z) == Material.WATER ||
			area.getBlock(x, y, z + 1) == Material.WATER ||
			area.getBlock(x, y, z - 1) == Material.WATER ||
			area.getBlock(x + 1, y, z) == Material.GRASS_BLOCK ||
			area.getBlock(x - 1, y, z) == Material.GRASS_BLOCK ||
			area.getBlock(x, y, z + 1) == Material.GRASS_BLOCK ||
			area.getBlock(x, y, z - 1) == Material.GRASS_BLOCK))
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Palm", world);
		}
		//	Cactus
		else if (area.getBiome(x, z) == Biome.DESERT_HILLS && block == Material.SAND && random.nextFloat() <= CactusChance && blockAbove != Material.WATER)
		{
			area.setBlock(x, y + 1, z, Material.CACTUS);
		}
		//	Bushes
		else if (area.getBiome(x, z) == Biome.DESERT_HILLS && random.nextFloat() <= BushChance && blockAbove != Material.WATER)
		{
			area.setBlock(x, y + 1, z, Material.DEAD_BUSH);
		}
	}
}
