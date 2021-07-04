package er.seven.worldgen.Caves;

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
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class IceCaveHandler extends CaveHandler
{	
	private FastNoise dripstoneNoise;
	
	public IceCaveHandler()
	{		
		dripstoneNoise = new FastNoise();
		dripstoneNoise.SetNoiseType(NoiseType.Simplex);
		dripstoneNoise.SetFrequency(0.06f);
	}
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TUNDRA ); }
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		BlockData block = area.getBlockData(x, y, z);
		
		if (BlockUtil.isAir(block.getMaterial()) == true) return;
		
		Material replacementMaterial = null;
		
		if (block.getMaterial() == Material.STONE)
			if (blobNoise.GetNoise(x, y, z) > 0.5)
				replacementMaterial = Material.FROSTED_ICE;
			else
				replacementMaterial = Material.BLUE_ICE;
		
		if (replacementMaterial != null)
			area.setBlock(x, y, z, replacementMaterial);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
	}
}
