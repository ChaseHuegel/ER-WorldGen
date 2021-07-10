package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class LushShroomDecorator extends Cave3DHandler
{
	private Object[] caveGrowthTable = new Object[]
			{
				Material.AZALEA, 2,
				Material.FLOWERING_AZALEA, 1,
				Material.GRASS, 20,
				Material.TALL_GRASS, 8,
				Material.BROWN_MUSHROOM, 4,
				Material.RED_MUSHROOM, 4,
				Material.SHROOMLIGHT, 1
			};
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.WATER, Material.MYCELIUM, null, null, MaterialGroup.STONE_ARRAY);
		}
		else
		{
			GenUtil.Resurface(random, area, x, y, z, Material.MOSS_BLOCK, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
		if (random.nextFloat() <= 0.04f && area.getBlock(x, y, z).isAir() && BlockUtil.isEarthy(area.getBlock(x, y - 1, z)) )
			GenUtil.GenerateTree(random, area, x, y, z, "TinyShrooms", world);
		
		if (random.nextFloat() <= 0.015f)
			GenUtil.GenerateSpore(random, area, x, y, z);
		
		if (random.nextFloat() <= 0.1f)
			GenUtil.GenerateGlowberries(random, area, x, y, z, 25);
		
		if (blobNoise.GetNoise(x, y, z) < 0f)
			if (random.nextFloat() <= 0.3f)
				GenUtil.GenerateMoss(random, area, x, y, z);
			else
				GenUtil.GenerateFoliage(random, area, x, y, z, caveGrowthTable);
		
		if (random.nextFloat() <= 0.15f)
			GenUtil.GenerateBigDripleaf(random, area, x, y, z, 1, 3);
		else if (random.nextFloat() <= 0.15f)
			GenUtil.GenerateSmallDripleaf(random, area, x, y, z);
	}
}
