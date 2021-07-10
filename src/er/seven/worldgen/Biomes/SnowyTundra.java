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
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class SnowyTundra extends ChunkHandler
{	
	//	DECORATION
	private static float RockChance = 0.001f;
	
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
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TUNDRA, Biome.FROZEN_RIVER ); }
	
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
				case GRASS_BLOCK: 		replacementMaterial = Material.POWDER_SNOW; break;
				case SNOW: 		replacementMaterial = Material.AIR; break;
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

		//	Rocks
		if (random.nextFloat() <= RockChance && BlockUtil.isDirt(block) )
		{
			GenUtil.GenerateRock(random, area, x, y, z, "Tundra", world);
		}
	}
}
