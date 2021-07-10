package er.seven.worldgen.Caves.Decorators;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CoralDecorator extends Cave3DHandler
{	
	private Object[] foliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.TALL_SEAGRASS, 10,
			Material.SEAGRASS, 20 };
	
	private Object[] coralTable = new Object[] {
			Material.SEA_PICKLE, 2,
			Material.BUBBLE_CORAL, 1,
			Material.FIRE_CORAL, 1,
			Material.TUBE_CORAL, 1,
			Material.HORN_CORAL, 1,
			Material.BRAIN_CORAL, 1,
			Material.BUBBLE_CORAL_FAN, 1,
			Material.FIRE_CORAL_FAN, 1,
			Material.TUBE_CORAL_FAN, 1,
			Material.HORN_CORAL_FAN, 1,
			Material.BRAIN_CORAL_FAN, 1};
	
	@Override
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (!BlockUtil.isAir(area.getBlock(x, y, z)) && !BlockUtil.isSolid(area.getBlock(x, y, z)))
			GenUtil.GenerateWater(random, area, x, y, z);
	}
	
	@Override
	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise)
	{
		if (blobNoise.GetNoise(x, y, z) > 0f)
			if (random.nextFloat() <= 0.05f)
				GenUtil.GenerateKelp(random, area, x, y, z, 3, 16);
		
		if (random.nextFloat() <= 0.2f)
			GenUtil.GenerateFoliage(random, area, x, y, z, foliageTable, true);		
	}
}
