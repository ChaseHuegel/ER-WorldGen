package er.seven.worldgen.Caves;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class ColdCaveHandler extends CaveHandler
{	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.GIANT_SPRUCE_TAIGA, Biome.GIANT_SPRUCE_TAIGA_HILLS,
			Biome.GIANT_TREE_TAIGA, Biome.GIANT_TREE_TAIGA_HILLS, Biome.TAIGA, Biome.TAIGA_HILLS, Biome.TAIGA_MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{		
		if (blobNoise.GetNoise(x, y, z) > 0)
			GenUtil.Resurface(random, area, x, y, z, Material.BLUE_ICE, Material.STONE);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
	}
}
