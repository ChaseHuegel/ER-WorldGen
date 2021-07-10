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

public class SnowyForestHandler extends ChunkHandler
{
	//	DECORATION
	private static float TreeChance = 0.04f;
	private static float RockChance = 0.01f;
	
	private static Object[] foliageTable = new Object[] {
			Material.AZURE_BLUET, 1,
			Material.LILY_OF_THE_VALLEY, 1,
			Material.PEONY, 1,
			Material.SWEET_BERRY_BUSH, 5,
			Material.FERN, 90 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.ANDESITE, 4,
			Material.STONE, 20,
			Material.GRANITE, 30,
			Material.DIORITE, 40,
			Material.BLACKSTONE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TAIGA ); }
	
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
				case GRASS_BLOCK:
					if (area.getBlock(x + 1, y - 1, z).isAir() == true ||
						area.getBlock(x - 1, y - 1, z).isAir() == true ||
						area.getBlock(x, y - 1, z + 1).isAir() == true ||
						area.getBlock(x, y - 1, z - 1).isAir() == true)
					{
						replacementMaterial = random.nextInt(4) != 0 ? Material.ANDESITE : Material.MOSSY_COBBLESTONE;
					}
					else
					{
						replacementMaterial = BlockUtil.weightedRandomMaterial(random, new Object[] {
								Material.COARSE_DIRT, 3,
								Material.GRASS_BLOCK, 1
						});
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
						area.getBlock(x - 1, y, z).isAir() == true ||
						area.getBlock(x, y, z + 1).isAir() == true ||
						area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = random.nextInt(4) != 0 ? Material.ANDESITE : Material.MOSSY_COBBLESTONE;
					}
					break;
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
					area.setBlockData(x, y + 1, z, data);
				}
			}
		}
		
		y = highestY;
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "SnowyForest", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, Material.ANDESITE);
		}
		//	Foliage
		else if (random.nextInt(4) == 0 && blockAbove != Material.WATER && block == Material.GRASS_BLOCK)
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
