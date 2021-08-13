package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class IceDecorator extends Cave3DHandler
{	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.BLUE_ICE, MaterialGroup.STONE_ARRAY);
		}
		else
		{
			GenUtil.Resurface(random, area, x, y, z, Material.ICE, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (area.getBlock(x, y, z) == Material.BLUE_ICE && random.nextFloat() <= 0.3f)
		{			
			int maxLength = (int)(6 * random.nextFloat());
			
			GenUtil.GenerateCustomDripstone(random, area, x, y, z, maxLength, Material.BLUE_ICE);
		}
	}
}
