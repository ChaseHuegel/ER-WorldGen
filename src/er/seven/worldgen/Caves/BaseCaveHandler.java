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

import Util.*;
import Util.FastNoise.*;
import er.seven.worldgen.*;
import er.seven.worldgen.Caves.Decorators.*;
import er.seven.worldgen.Dungeons.Dungeons;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class BaseCaveHandler extends CaveHandler
{	
	private FastNoise maskNoise;
	private FastNoise decoratorNoise;
	private FastNoise dripstoneNoise;
	private FastNoise lichenNoise;
	
	public BaseCaveHandler()
	{
		dripstoneNoise = new FastNoise();
		dripstoneNoise.SetNoiseType(NoiseType.Simplex);
		dripstoneNoise.SetFrequency(0.05f);
		
		lichenNoise = new FastNoise();
		lichenNoise.SetNoiseType(NoiseType.Simplex);
		lichenNoise.SetFrequency(0.07f);
		
		decoratorNoise = new FastNoise();
		decoratorNoise.SetNoiseType(NoiseType.Cellular);
		decoratorNoise.SetCellularDistanceFunction(CellularDistanceFunction.Natural);
		FastNoise whiteNoise = new FastNoise();
		whiteNoise.SetNoiseType(NoiseType.WhiteNoise);
		decoratorNoise.SetCellularNoiseLookup(whiteNoise);
		decoratorNoise.SetCellularReturnType(CellularReturnType.NoiseLookup);
		decoratorNoise.SetFrequency(0.02f);
		
		maskNoise = new FastNoise();
		maskNoise.SetNoiseType(NoiseType.Perlin);
		maskNoise.SetFrequency(0.015f);
	}
	
	public Cave3DHandler[] decorators3D = new Cave3DHandler[] 
		{
				new LushDecorator(),
				new EarthDecorator(),
				new VolcanicDecorator(),
				new MyceliumDecorator(),
				new LushShroomDecorator(),
				new CrystalDecorator(),
				new SlimeDecorator(),
				new MushroomDecorator(),
				new SpikeDecorator()
		};
	
	private float FossilChance = 0.05f;
	private float StructureChance = 0.05f;
	private float DungeonChance = 0.01f;
	
	private Object[] caveGrowthTable = new Object[]
		{
			Material.BROWN_MUSHROOM, 1,
			Material.RED_MUSHROOM, 1
		};
	
	private Object[] waterFoliageTable = new Object[] {
			Material.SEA_PICKLE, 1,
			Material.TALL_SEAGRASS, 10,
			Material.SEAGRASS, 20 };
	
	@Override
	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world)
	{
		for (int y = highestY-1; y > -64; y--)
		{
			float noise = maskNoise.GetNoise(x, y, z);
			float placementNoise = decoratorNoise.GetNoise(x, y, z);
			
			//	Run 3D cave decorators
			if (noise >= 0f && decorators3D.length > 0)
			{
				float factor = 1f / decorators3D.length;
				
				for (int i = 0; i < decorators3D.length; i++)
				{
					float max = placementNoise+1f;
					float min = max - (0.25f / decorators3D.length);
					float value = (i+1)*factor;
					
					if (value <= max && value >= min)
						decorators3D[i].GenerateAt(random, x, y, z, area, world, max);
				}
			}
		}
		
		//	Column decorations
		for (int y = highestY-1; y > -64; y--)
		{
			float noise = maskNoise.GetNoise(x, y, z);
			float placementNoise = decoratorNoise.GetNoise(x, y, z);
			
			//	Run 3D cave decorators
			if (noise >= 0f && decorators3D.length > 0)
			{
				float factor = 1f / decorators3D.length;
				
				for (int i = 0; i < decorators3D.length; i++)
				{
					float max = placementNoise+1f;
					float min = max - (0.25f / decorators3D.length);
					float value = (i+1)*factor;
					
					if (value <= max && value >= min)
						decorators3D[i].PopulateAt(random, x, y, z, area, world, max);
				}
			}
			
			//	Lichen blobs
			if (lichenNoise.GetNoise(x, y, z) > 0.7f)
				if (random.nextFloat()+0.3f < (lichenNoise.GetNoise(x, y, z)+1)/2)
					GenUtil.GenerateLichen(random, area, x, y, z);
			
			//	Dripstone blobs
			if (dripstoneNoise.GetNoise(x, y, z) > 0.3f 
				&& BlockUtil.isEarthy(area.getBlock(x, y, z))
				&& (area.getBlock(x, y+1, z) == Material.CAVE_AIR || area.getBlock(x, y-1, z) == Material.CAVE_AIR))
			{
				area.setBlock(x, y, z, Material.DRIPSTONE_BLOCK);
				
				if (random.nextFloat()+0.5f < (dripstoneNoise.GetNoise(x, y, z)+1)/2)
				{
					int maxLength = (int)(8 * dripstoneNoise.GetNoise(x, y, z));
					
					GenUtil.GenerateDripstone(random, area, x, y, z, maxLength);
				}
			}
			
			//	Kelp
			int maxKelpHeight = 25; if (y < -54) maxKelpHeight = 6;
			
			if (blobNoise.GetNoise(x, y, z) > 0f)
				if (random.nextFloat() <= 0.05f)
					GenUtil.GenerateKelp(random, area, x, y, z, 3, maxKelpHeight);
		}
		
		//	Water foliage
		for (int n = 0; n < random.nextInt(64); n++)
			GenUtil.GenerateFoliage(random, area, x, random.nextInt(highestY+63)-63, z, waterFoliageTable);
		
		//	Hanging roots
		for (int n = 0; n < random.nextInt(16); n++)
			GenUtil.GenerateRoots(random, area, x, random.nextInt(highestY+63)-63, z);
		
		//	Mushrooms
		for (int n = 0; n < random.nextInt(8); n++)
			GenUtil.GenerateFoliage(random, area, x, random.nextInt(highestY+63)-63, z, caveGrowthTable);
	}
	
	@Override
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		int x = area.getCenterX();
		int z = area.getCenterZ();
		int y = -99;
		
		for (int n = 0; n < random.nextInt(16); n++)
		{
			int highestY = BlockUtil.getHighestSolidY(x, z, area);
			y = random.nextInt(highestY+60)-60;
			
			if (area.getBlock(x, y, z).isSolid())
				break;
			
			y = -99;
		}
		if (y < -63) return;	//	Dont place structures if we didn't find a solid point
		
		if (random.nextFloat() <= DungeonChance)
		{
			Dungeons.GenerateDungeon(random, area, world, x-DecorationArea.DECORATION_RADIUS, y, z-DecorationArea.DECORATION_RADIUS, 
					7,
					random.nextInt(4)+1,
					random.nextInt(4)+1,
					random.nextInt(10)+1
				);
		}
		else if (random.nextFloat() <= StructureChance)
		{			
			GenUtil.GenerateObject(random, area, x, y, z, "cave", world);
		}
		else if (random.nextFloat() <= FossilChance)
		{			
			GenUtil.GenerateObject(random, area, x, y, z, "fossil", world);
		}
	}
}
