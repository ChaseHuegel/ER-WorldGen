package er.seven.worldgen.Caves;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class IceCaveHandler extends CaveHandler
{	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TUNDRA, Biome.SNOWY_MOUNTAINS, Biome.SNOWY_TAIGA_HILLS, Biome.ICE_SPIKES ); }
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		BlockData block = area.getBlockData(x, y, z);
		Material replacementMaterial = null;
		
		if (blobNoise.GetNoise(x, y, z) > 0.5)
			replacementMaterial = Material.FROSTED_ICE;
		else
			GenUtil.Resurface(random, area, x, y, z, Material.BLUE_ICE, Material.STONE);
		
		if (replacementMaterial != null)
			area.setBlock(x, y, z, replacementMaterial);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
	}
}
