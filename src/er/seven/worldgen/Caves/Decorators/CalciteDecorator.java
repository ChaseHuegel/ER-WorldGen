package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CalciteDecorator extends Cave3DHandler
{	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.WATER, Material.CALCITE, Material.CALCITE, Material.CALCITE, MaterialGroup.STONE_ARRAY);
		}
		else
		{
			GenUtil.Resurface(random, area, x, y, z, Material.CALCITE, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{		
	}
}
