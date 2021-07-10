package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

import Util.BlockUtil;
import Util.GenUtil;
import Util.MaterialGroup;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CrystalDecorator extends Cave3DHandler
{
	public CrystalDecorator()
	{
		blobNoise.SetFrequency(0.2f);
	}
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > -0.5f && random.nextFloat() <= 0.05f)
		{
			Material material = Material.EMERALD_ORE; if (y <= 0) material = Material.DEEPSLATE_EMERALD_ORE;
			GenUtil.Resurface(random, area, x, y, z, material, MaterialGroup.STONE_ARRAY);
		}
		else if (blobNoise.GetNoise(x, y, z) >= 0f)
		{
			GenUtil.Resurface(random, area, x, y, z, Material.AMETHYST_BLOCK, MaterialGroup.STONE_ARRAY);
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (random.nextFloat() <= 0.05f)
			GenUtil.GenerateAmethyst(random, area, x, y, z);
		
		if (BlockUtil.isEarthy(area.getBlock(x, y, z)) && random.nextFloat() <= 0.3f)
		{			
			int maxLength = (int)(6 * random.nextFloat());
			
			if (GenUtil.GenerateCustomDripstone(random, area, x, y, z, maxLength, BlockUtil.randMaterial(Material.LIME_STAINED_GLASS, Material.PURPLE_STAINED_GLASS)))
				 area.setBlock(x, y, z, Material.GLOWSTONE);
		}
	}
}
