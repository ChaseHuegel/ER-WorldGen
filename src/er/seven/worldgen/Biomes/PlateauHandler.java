package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.ChunkHandler;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class PlateauHandler extends ChunkHandler
{	
	//	DECORATION
	private static float TreeChance = 0.0004f;
	private static float RockChance = 0.008f;
	
	private static Object[] foliageTable = new Object[] {
			Material.DEAD_BUSH, 1,
			Material.GRASS, 4 };
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.ANDESITE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.STONE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	//	CAVES
	private static float StalagChance 			= 0.06f;
	private static float CaveGrowthChance 		= 0.06f;
	
	private static Object[] CaveGrowthTable = new Object[] {
			Material.RED_MUSHROOM, 1,
			Material.BROWN_MUSHROOM, 1 };
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SAVANNA_PLATEAU, Biome.SHATTERED_SAVANNA_PLATEAU ); }
	
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
						replacementMaterial = Material.ANDESITE;
					}
					break;
				case DIRT:
					if (area.getBlock(x + 1, y, z).isAir() == true ||
							area.getBlock(x - 1, y, z).isAir() == true ||
							area.getBlock(x, y, z + 1).isAir() == true ||
							area.getBlock(x, y, z - 1).isAir() == true)
					{
						replacementMaterial = Material.ANDESITE;
					}
					break;	
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GOLD_ORE:			replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				case GRANITE: 			replacementMaterial = Material.ANDESITE; break;
				case DIORITE: 			replacementMaterial = Material.ANDESITE; break;
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
		
		//	Trees
		if (random.nextFloat() <= TreeChance && blockAbove != Material.WATER && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateTree(random, area, x, y + 1, z, "Plateau", world);
		}
		//	Rocks
		else if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateRock(random, area, x, y + 1, z, "Plateau", world);
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
