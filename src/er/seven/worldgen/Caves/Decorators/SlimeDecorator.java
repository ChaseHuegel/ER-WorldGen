package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class SlimeDecorator extends Cave3DHandler
{
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
		{
			Material mat = BlockUtil.randMaterial(Material.MOSSY_COBBLESTONE, Material.COBBLESTONE, null);
			GenUtil.Resurface(random, area, x, y, z, mat, MaterialGroup.STONE_ARRAY);
		}
		else if (blobNoise.GetNoise(x, y, z) > -0.3)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.SLIME_BLOCK, null, Material.SLIME_BLOCK, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
		if (random.nextFloat() <= 0.05f)
			GenUtil.GenerateVine(random, area, x, y, z, 25);
	}
}
