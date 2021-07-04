package er.seven.worldgen.Caves;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class BaseCaveHandler extends CaveHandler
{	
	private FastNoise dripstoneNoise;
	
	public BaseCaveHandler()
	{
		dripstoneNoise = new FastNoise();
		dripstoneNoise.SetNoiseType(NoiseType.Simplex);
		dripstoneNoise.SetFrequency(0.06f);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
		for (int n = 0; n < random.nextInt(4); n++)
			GenUtil.GenerateLichen(random, area, x, random.nextInt(highestY+63)-63, z);
		
		for (int n = 0; n < random.nextInt(12); n++)
			GenUtil.GenerateRoots(random, area, x, random.nextInt(highestY+63)-63, z);
		
		//	Dripstone blobs
		for (int y = -63; y < highestY; y++)
		{
			if (dripstoneNoise.GetNoise(x, y, z) > 0.3 && area.getBlock(x, y, z).isSolid() && area.getBlock(x, y, z) != Material.POINTED_DRIPSTONE)
			{
				area.setBlock(x, y, z, Material.DRIPSTONE_BLOCK);
				
				if (random.nextFloat() < (dripstoneNoise.GetNoise(x, y, z)+1)/2)
				{
					int maxLength = (int)(8 * dripstoneNoise.GetNoise(x, y, z));
					
					GenUtil.GenerateDripstone(random, area, x, y, z, maxLength);
				}
			}
		}
	}
}
