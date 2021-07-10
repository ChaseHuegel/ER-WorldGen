package er.seven.worldgen.Caves;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.MaterialGroup;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class LavaCavernsHandler extends CaveHandler
{	
	private FastNoise poolNoise;
	public LavaCavernsHandler()
	{
		poolNoise = new FastNoise();
		poolNoise.SetNoiseType(NoiseType.Simplex);
		poolNoise.SetFrequency(0.04f);
	}
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GRAVELLY_MOUNTAINS, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.MOUNTAINS, Biome.WOODED_MOUNTAINS, Biome.TAIGA_MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		BlockData block = area.getBlockData(x, y, z);
		Material replacementMaterial = null;
		
		if (y >= -53 && block.getMaterial().isSolid())
		{
			//	Lava pools
			if (poolNoise.GetNoise(x, y, z) < 0 && BlockUtil.isTopExposed(area, x, y, z) && !BlockUtil.isBottomExposed(area, x, y, z))
			{
				replacementMaterial = Material.LAVA;
				
				if (area.getBlock(x + 1, y, z).isAir() == true ||
					area.getBlock(x - 1, y, z).isAir() == true ||
					area.getBlock(x, y, z + 1).isAir() == true ||
					area.getBlock(x, y, z - 1).isAir() == true ||
					area.getBlock(x + 1, y, z) == Material.WATER ||
					area.getBlock(x - 1, y, z) == Material.WATER ||
					area.getBlock(x, y, z + 1) == Material.WATER ||
					area.getBlock(x, y, z - 1) == Material.WATER)
				{
					replacementMaterial = Material.BASALT;
				}
			}
		}
		
		if (blobNoise.GetNoise(x, y, z) > 0.5)
			replacementMaterial = Material.TUFF;
		else
			GenUtil.Resurface(random, area, x, y, z, Material.BASALT, null, null, MaterialGroup.STONE_ARRAY);
		
		if (replacementMaterial != null)
			area.setBlock(x, y, z, replacementMaterial);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
	}
	
	@Override
	public void ChangeLiquidAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		if (y <= -53) return;
		
		Levelled data = (Levelled)area.getBlockData(x, y, z);
		Levelled lava = (Levelled)Material.LAVA.createBlockData();
		lava.setLevel(data.getLevel());
		area.setBlock(x, y, z, Material.LAVA);
		area.setBlockData(x, y, z, lava);
	}
}
