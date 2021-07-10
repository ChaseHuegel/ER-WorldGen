package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class EarthDecorator extends Cave3DHandler
{
	private Object[] caveGrowthTable = new Object[]
			{
				Material.FERN, 4,
				Material.GRASS, 10,
				Material.LARGE_FERN, 1,
				Material.BROWN_MUSHROOM, 1
			};
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.COARSE_DIRT, Material.ROOTED_DIRT, Material.DIRT, MaterialGroup.STONE_ARRAY);
		}
		else
		{
			GenUtil.Resurface(random, area, x, y, z, Material.PODZOL, Material.ROOTED_DIRT, Material.DIRT, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
		if (random.nextFloat() <= 0.2f)
			GenUtil.GenerateRoots(random, area, x, y, z);
		
		if (random.nextFloat() <= 0.03f)
			GenUtil.GenerateVine(random, area, x, y, z, 25);
		
		if (blobNoise.GetNoise(x, y, z) < 0f)
			GenUtil.GenerateFoliage(random, area, x, y, z, caveGrowthTable);
	}
}
