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
import er.seven.worldgen.ChunkHandler;
import er.seven.worldgen.Main;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class MagmaFieldsHandler extends ChunkHandler
{
	//	DECORATION
	private static float VolcanoChance = 0.001f;
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.BLACKSTONE, 0,
			Material.ANDESITE, 4,
			Material.BASALT, 20,
			Material.GRANITE, 30,
			Material.BLACKSTONE, 40,
			Material.OBSIDIAN, 52};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.MAGMA_BLOCK, 1,
			Material.BLACKSTONE_SLAB, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GRAVELLY_MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 0; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			switch (block.getMaterial())
			{
			case GRASS_BLOCK:
				replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
						Material.LAVA, 4,
						Material.MAGMA_BLOCK, 1
				});
				
//				if (area.getBlock(x + 1, y, z).isOccluding() == false ||
//					area.getBlock(x - 1, y, z).isOccluding() == false ||
//					area.getBlock(x, y, z + 1).isOccluding() == false ||
//					area.getBlock(x, y, z - 1).isOccluding() == false)
//				{
//					replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
//							Material.BASALT, 6,
//							Material.BLACKSTONE, 1
//					});
//				}
				break;
			case GRAVEL:
			case STONE:
				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers);
				
				//	Floor
				if (BlockUtil.isTopExposed(area, x, y, z))
				{
					replacementMaterial = Material.BASALT;
				}
				//	Ceiling
				else if (BlockUtil.isBottomExposed(area, x, y, z))
				{
					replacementMaterial = Material.BLACKSTONE;
				}
				//	Walls
				else if (BlockUtil.isSideExposed(area, x, y, z))
				{
					replacementMaterial = Material.BLACKSTONE;
				}
				break;
			case SAND: 				replacementMaterial = Material.GRAVEL; break;
			case DIRT: 				replacementMaterial = Material.BASALT; break;
			case DIORITE: 			replacementMaterial = Material.MAGMA_BLOCK; break;
			case ANDESITE: 			replacementMaterial = Material.MAGMA_BLOCK; break;
			case GRANITE: 			replacementMaterial = Material.MAGMA_BLOCK; break;
			default: break;
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
		
		//	Volcanos
		if (random.nextFloat() <= VolcanoChance && block == Material.BLACKSTONE)
		{
			GenUtil.GenerateObject(random, area, x, y + random.nextInt(2) - 3, z, "volcano", world);
		}
		
		//	Caves
		for (y = highestY; y > 0; y--)
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
