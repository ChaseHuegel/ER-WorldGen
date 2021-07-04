package er.seven.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Biome;

import Util.FastNoise;
import Util.FastNoise.NoiseType;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class CaveHandler 
{
	protected FastNoise blobNoise;
	public CaveHandler()
	{
		blobNoise = new FastNoise();
		blobNoise.SetNoiseType(NoiseType.Simplex);
		blobNoise.SetFrequency(0.05f);
	}
	
	public List<Biome> getValidBiomes() { return Arrays.asList(); }
	
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world)
	{
		
	}

	public void PopulateAt(Random random, int x, int highestY, int z, DecorationArea area, World world) 
	{
		
	}
	
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		
	}
}
