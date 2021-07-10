package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class IceSpikesHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.01f;
	private static float RockChance = 0.004f;
	
	private static Object[] foliageTable = new Object[] {
			Material.PEONY, 1,
			Material.SWEET_BERRY_BUSH, 2,
			Material.DARK_OAK_LEAVES, 4,
			Material.LARGE_FERN, 10,
			Material.FERN, 50,
			Material.GRASS, 80 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.PACKED_ICE, 4,
			Material.DIORITE, 40,
			Material.STONE, 52,
			Material.ANDESITE, 58,
			Material.BLACKSTONE, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.SNOW_BLOCK, 1,
			Material.SNOW, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TAIGA_HILLS ); }
	
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
					if (area.getBlock(x + 1, y - 1, z).isAir() == true ||
						area.getBlock(x - 1, y - 1, z).isAir() == true ||
						area.getBlock(x, y - 1, z + 1).isAir() == true ||
						area.getBlock(x, y - 1, z - 1).isAir() == true)
					{
						replacementMaterial = random.nextInt(4) != 0 ? Material.PACKED_ICE : Material.BLUE_ICE;
					}
					else
					{
						replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
								Material.COARSE_DIRT, 2,
								Material.GRASS_BLOCK, 3
						});
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = random.nextInt(4) != 0 ? Material.PACKED_ICE : Material.BLUE_ICE;
					}
					break;
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
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
		
		//	Snow on things!
		for (y = highestY; y < world.getMaxHeight() - 1; y++)
		{
			if (area.getBlock(x, y, z).isOccluding() == true && area.getBlock(x, y + 1, z).isOccluding() == false)
			{
				if ((BlockUtil.isDirt(area.getBlock(x + 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x - 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z + 1)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z - 1)) == true ) &&
					(BlockUtil.isLeaves(area.getBlock(x + 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x - 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z + 1)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z - 1)) == false ))
				{
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
					data.setLayers(  5 + random.nextInt(3) );
					area.setBlockData(x, y + 1, z, data);
				}
				else
				{
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
//					data.setLayers(  1 + random.nextInt(3) );
					area.setBlockData(x, y + 1, z, data);
				}
			}
		}
		
		y = highestY;
		
		//	Spikes
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "IceSpikes", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.ANDESITE);
		}
		//	Foliage
		else if (random.nextInt(4) == 0 && blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
