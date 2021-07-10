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
import Util.FastNoise.NoiseType;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CarvingManager implements Decoration 
{		
	public static int caveDepth = 20;
	public static int maxHeight = 60;
	public static int minHeight = -62;
	public static int undergroundSeaLevel = -54;
	public static int undergroundSeaMaxHeight = -30;
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
	private FastNoise floodNoise;
	
	private FastNoise infestationNoise;
	
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
		
		floodNoise = new FastNoise(); floodNoise.SetNoiseType(NoiseType.ValueFractal); floodNoise.SetFrequency(0.03f);
		
		undergroundSeaNoise = new FastNoise(); undergroundSeaNoise.SetNoiseType(NoiseType.Perlin); undergroundSeaNoise.SetFrequency(0.035f);
		
		infestationNoise = new FastNoise();
		infestationNoise.SetNoiseType(NoiseType.Simplex);
		infestationNoise.SetFrequency(0.08f);
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
		for (int y = minHeight; y < height; y++) 
		{
			material = null;
			
			if (y > -64 && y <= -62)
			{
				area.setBlock(realX, y, realZ, Material.DEEPSLATE);
				continue;
			}
			
			if (BlockUtil.isLiquid(area.getBlock(realX, y, realZ))) continue;
			
			int val = 16 - y; if (val <= 0) val = 1;
			if (y <= -32 || random.nextInt(val) > 8) material = Material.DEEPSLATE;
			else if (y <= 0) material = Material.TUFF;
			
			if (infestationNoise.GetNoise(realX, y, realZ) > 0.8f)
				if (random.nextFloat()+0.5f < (infestationNoise.GetNoise(realX, y, realZ)+1)/2)
					if (area.getBlock(realX, y, realZ) == Material.STONE)
						material = Material.INFESTED_STONE;
					else if (material == Material.DEEPSLATE)
						material = Material.INFESTED_DEEPSLATE;
			
			noise = tunnelNoise.GetCellular(realX, y, realZ);
			noise += baseNoise.GetPerlin(realX, y, realZ);
			noise /= 2;
			
			if (noise < -0.9 ||
				cavernNoise.GetCellular(realX, y, realZ) < -0.9 ||
				(passageNoise.GetNoise(realX, y, realZ) > -0.2 && tunnelNoise.GetNoise(realX, y, realZ) > 0) ||
				(undergroundSeaNoise.GetPerlinFractal(realX, y, realZ) < 0.05 && y <= undergroundSeaMaxHeight) ||
				(massiveCavernNoise.GetPerlin(realX, y, realZ) < -0.5 && y <= massiveCavernMaxHeight)
				)
			{
				material = Material.CAVE_AIR;
			}
			
			if (y <= undergroundSeaLevel && (area.getBlock(realX, y, realZ) == Material.CAVE_AIR || material == Material.CAVE_AIR))
			{
				material = Material.WATER;
			}
			
			if ((material == Material.CAVE_AIR || area.getBlock(realX, y, realZ).isAir()) && floodNoise.GetNoise(realX, y, realZ) >= 0.5)
			{
				float value = floodNoise.GetNoise(realX, y, realZ);
				value = value*2-1f;
				
				if (value >= 0.2f)
					material = Material.WATER;
				else
				{
					if (y <= -32 || random.nextInt(val) > 8) material = Material.DEEPSLATE;
					else if (y <= 0) material = Material.TUFF;
					else material = Material.STONE;
				}
			}
			
			if (material != null)
			{
				area.setBlock(realX, y, realZ, material);
			}
		
		}
		}}
    }
}