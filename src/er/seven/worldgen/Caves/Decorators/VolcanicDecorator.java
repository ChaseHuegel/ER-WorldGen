package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class VolcanicDecorator extends Cave3DHandler
{
	private Object[] caveGrowthTable = new Object[]
			{
				Material.CRIMSON_ROOTS, 1,
				Material.CRIMSON_FUNGUS, 2,
			};
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		Material poolBlock = Material.LAVA;
		if (blobNoise.GetNoise(x, y, z) > 0.4f) poolBlock = Material.MAGMA_BLOCK;
		
		GenUtil.Resurface(random, area, x, y, z, poolBlock, Material.BASALT, Material.BLACKSTONE, Material.BLACKSTONE, MaterialGroup.STONE_ARRAY);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
		if (random.nextFloat() <= 0.05f)
			GenUtil.GenerateDripstone(random, area, x, y, z, 2);
		
		if (blobNoise.GetNoise(x, y, z) < 0f && random.nextFloat() <= 0.1f)
			GenUtil.GenerateFoliage(random, area, x, y, z, caveGrowthTable);
	}
}
