package er.seven.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import Util.BlockUtil;
import Util.FastNoise;
import Util.FastNoise.CellularDistanceFunction;
import Util.FastNoise.CellularReturnType;
import Util.FastNoise.FractalType;
import Util.FastNoise.NoiseType;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CarvingManager implements Decoration 
{		
	public static int caveDepth = 4;
	public static int maxHeight = 60;
	public static int minHeight = 2;
	public static int undergroundSeaLevel = 10;
	public static int undergroundSeaMaxHeight = 20;
	public static int massiveCavernMaxHeight = 48;
	
	public World world;
	private FastNoise baseNoise;
	private FastNoise detailNoise;
	private FastNoise cavernNoise;
	private FastNoise lakeNoise;
	private FastNoise tunnelNoise;
	private FastNoise passageNoise;
	private FastNoise undergroundSeaNoise;
	private FastNoise massiveCavernNoise;
	
	private List<Biome> oceanBiomes = Arrays.asList( Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN,
			Biome.DEEP_OCEAN, Biome.DEEP_WARM_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.OCEAN, Biome.WARM_OCEAN);
	
	public CarvingManager(World _world)
	{
		world = _world;
		
		baseNoise = new FastNoise(); baseNoise.SetNoiseType(NoiseType.Perlin); baseNoise.SetFrequency(0.1f);
		detailNoise = new FastNoise(); detailNoise.SetNoiseType(NoiseType.Simplex); detailNoise.SetFrequency(0.2f);
		cavernNoise = new FastNoise(); cavernNoise.SetNoiseType(NoiseType.Cellular); cavernNoise.SetFrequency(0.1f);
		lakeNoise = new FastNoise(); lakeNoise.SetNoiseType(NoiseType.Cubic); lakeNoise.SetFrequency(0.005f);
		tunnelNoise = new FastNoise(); tunnelNoise.SetNoiseType(NoiseType.Cellular); tunnelNoise.SetFrequency(0.1f);
		passageNoise = new FastNoise(); passageNoise.SetNoiseType(NoiseType.Cellular); passageNoise.SetFrequency(0.04f);
		passageNoise.SetCellularDistanceFunction(CellularDistanceFunction.Euclidean);
		passageNoise.SetCellularReturnType(CellularReturnType.Distance2);
		
		massiveCavernNoise = new FastNoise(); massiveCavernNoise.SetNoiseType(NoiseType.Cellular); massiveCavernNoise.SetFrequency(0.03f);
		
		undergroundSeaNoise = new FastNoise(); undergroundSeaNoise.SetNoiseType(NoiseType.Perlin); undergroundSeaNoise.SetFrequency(0.05f);
	}
	
	@Override
    public void decorate(DecorationArea area, Random random) 
	{
		Material material = null;
		double noise = 0.0;
		
		for (int x = 0; x < 16; x++) {
		for (int z = 0; z < 16; z++) {
			int realX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + x;
			int realZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + z;
			
			int highestY = BlockUtil.getHighestSolidY(realX, realZ, area);
			int height = highestY - caveDepth;
		for (int y = minHeight; y < height; y++) {
		
		if (y == minHeight) { area.setBlock(realX, y, realZ, Material.STONE); continue; }
		
		if (area.getBlock(realX, y, realZ) == Material.WATER || area.getBlock(realX, y, realZ) == Material.LAVA) continue;
		if (area.getBlock(realX, y + 1, realZ) == Material.WATER || area.getBlock(realX, y + 1, realZ) == Material.LAVA) continue;
		
		noise = tunnelNoise.GetCellular(realX, y, realZ);
		noise += baseNoise.GetPerlin(realX, y, realZ);
//		noise += detailNoise.GetSimplex(realX, y, realZ);
//		noise += cavernNoise.GetCellular(realX, y, realZ);
//		noise += lakeNoise.GetCubic(realX, y, realZ);
		noise /= 2;
		
		material = null;
		
//		if (noise < -0.9 || 
//			passageNoise.GetNoise(realX, y, realZ) < -0.6 ||
//			cavernNoise.GetCellular(realX, y, realZ) < -0.9 ||
//			(undergroundSeaNoise.GetPerlinFractal(realX, y, realZ) < -0.2 && y <= undergroundSeaMaxHeight) ||
//			(massiveCavernNoise.GetPerlin(realX, y, realZ) < -0.5 && y <= massiveCavernMaxHeight)
//			)
		if (noise < -0.9 ||
			cavernNoise.GetCellular(realX, y, realZ) < -0.9 ||
			(passageNoise.GetNoise(realX, y, realZ) > -0.2 && tunnelNoise.GetNoise(realX, y, realZ) > 0) ||
			(undergroundSeaNoise.GetPerlinFractal(realX, y, realZ) < 0 && y <= undergroundSeaMaxHeight) ||
			(massiveCavernNoise.GetPerlin(realX, y, realZ) < -0.5 && y <= massiveCavernMaxHeight)
			)
		{
			material = Material.CAVE_AIR;
			
			if (oceanBiomes.contains(area.getBiome(realX, realZ)))
			{
				material = Material.WATER;
				if (area.getBlock(realX + 1, y, realZ) == Material.LAVA || 
					area.getBlock(realX - 1, y, realZ) == Material.LAVA ||
					area.getBlock(realX, y, realZ + 1) == Material.LAVA || 
					area.getBlock(realX, y, realZ + 1) == Material.LAVA || 
					area.getBlock(realX, y + 1, realZ) == Material.LAVA || 
					area.getBlock(realX, y - 1, realZ) == Material.LAVA)
				{
					material = Material.OBSIDIAN;
				}
			}
		}
		
		if (y <= undergroundSeaLevel && (area.getBlock(realX, y, realZ) == Material.CAVE_AIR || material == Material.CAVE_AIR))
		{
			if (undergroundSeaNoise.GetPerlinFractal(realX, y, realZ) > 0)
			{
				material = Material.LAVA;
				if (area.getBlock(realX + 1, y, realZ) == Material.WATER || 
					area.getBlock(realX - 1, y, realZ) == Material.WATER ||
					area.getBlock(realX, y, realZ + 1) == Material.WATER || 
					area.getBlock(realX, y, realZ + 1) == Material.WATER || 
					area.getBlock(realX, y + 1, realZ) == Material.WATER || 
					area.getBlock(realX, y - 1, realZ) == Material.WATER)
				{
					material = Material.OBSIDIAN;
				}
			}
			else
			{
				material = Material.WATER;
				if (area.getBlock(realX + 1, y, realZ) == Material.LAVA || 
					area.getBlock(realX - 1, y, realZ) == Material.LAVA ||
					area.getBlock(realX, y, realZ + 1) == Material.LAVA || 
					area.getBlock(realX, y, realZ + 1) == Material.LAVA || 
					area.getBlock(realX, y + 1, realZ) == Material.LAVA || 
					area.getBlock(realX, y - 1, realZ) == Material.LAVA)
				{
					material = Material.OBSIDIAN;
				}
			}
		}
		
		if (material != null)
		{
			area.setBlock(realX, y, realZ, material);
		}
		
		}}}
    }
}