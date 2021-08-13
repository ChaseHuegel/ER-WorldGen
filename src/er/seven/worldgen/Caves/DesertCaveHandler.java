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

public class DesertCaveHandler extends CaveHandler
{		
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES ); }
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		Material replacementMaterial = null;
		
		if (blobNoise.GetNoise(x, y, z) > 0.5)
			replacementMaterial = Material.SAND;
		else
			GenUtil.Resurface(random, area, x, y, z, Material.SAND, Material.SANDSTONE, Material.SANDSTONE, MaterialGroup.STONE_ARRAY);
		
		if (replacementMaterial != null)
			area.setBlock(x, y, z, replacementMaterial);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
	}
}
