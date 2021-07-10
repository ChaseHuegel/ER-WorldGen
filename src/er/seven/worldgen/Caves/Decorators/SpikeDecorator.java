package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class SpikeDecorator extends Cave3DHandler
{
	public SpikeDecorator()
	{
		blobNoise.SetFrequency(0.2f);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
		if (BlockUtil.isEarthy(area.getBlock(x, y, z)) && random.nextFloat() <= 0.6f)
		{			
			int maxLength = (int)(8 * random.nextFloat());
			
			GenUtil.GenerateCustomDripstone(random, area, x, y, z, maxLength, Material.STONE);
		}
	}
}
