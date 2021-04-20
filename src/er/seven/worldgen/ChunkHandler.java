package er.seven.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Biome;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class ChunkHandler 
{
	public List<Biome> getValidBiomes() { return Arrays.asList(); }
	
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{
	}
	
	public void PopulateAt(Random random, int x, int z, DecorationArea area)
	{
	}

	public void PopulateAt(Random random, int realX, int realZ, DecorationArea area, World world) 
	{
	}
	
	public void Populate(Random random, DecorationArea area, World world)
	{
		
	}
	
	public void PlaceStructure(Random random, DecorationArea area, World world)
	{
		
	}
}
