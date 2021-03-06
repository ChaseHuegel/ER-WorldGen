package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class MushroomDecorator extends Cave3DHandler
{
	private Object[] caveGrowthTable = new Object[]
			{
				Material.BROWN_MUSHROOM, 1,
				Material.RED_MUSHROOM, 1,
			};
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (random.nextFloat() <= 0.04f && area.getBlock(x, y, z).isAir() && BlockUtil.isEarthy(area.getBlock(x, y - 1, z)) )
			GenUtil.GenerateTree(random, area, x, y, z, "TinyShrooms", world);
		
		if (blobNoise.GetNoise(x, y, z) > 0.7f)
			GenUtil.GenerateFoliage(random, area, x, y, z, caveGrowthTable);
	}
}
