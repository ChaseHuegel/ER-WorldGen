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

public class StoneSpiresHandler extends ChunkHandler
{
	//	DECORATION
	private static float SpikeChance = 0.01f;
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.TERRACOTTA, 4,
			Material.STONE, 20,
			Material.DIORITE, 30,
			Material.GRANITE, 40,
			Material.ANDESITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GRAVELLY_MOUNTAINS ); }
	
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
				case DIRT:			replacementMaterial = Material.GRAVEL; break;
				case GRASS_BLOCK:	replacementMaterial = Material.GRAVEL; break;
				case STONE:	replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
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
		int highestY = BlockUtil.getHighestGroundedY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		y = highestY;
		if (y < 60) return;
		
		//	Stone spikes
		if (random.nextFloat() <= SpikeChance && block != Material.GRASS_BLOCK)
		{			
			GenUtil.GenerateRock(random, area, x, highestY + 1, z, "StoneSpikes", world);
		}
	}
}
