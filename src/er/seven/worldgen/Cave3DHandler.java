package er.seven.worldgen;

import java.util.Random;

import org.bukkit.World;
import Util.FastNoise;
import Util.FastNoise.NoiseType;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class Cave3DHandler 
{
	protected FastNoise blobNoise;
	public Cave3DHandler()
	{
		blobNoise = new FastNoise();
		blobNoise.SetNoiseType(NoiseType.Simplex);
		blobNoise.SetFrequency(0.05f);
	}
	
	public void GenerateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise) 
	{
		
	}

	public void PopulateAt(Random random, int x, int y, int z, DecorationArea area, World world, float noise) 
	{
		
	}
	
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		
	}
}
