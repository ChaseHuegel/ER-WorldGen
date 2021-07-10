package er.seven.worldgen;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;

import Util.BlockUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.Util;
import er.seven.worldgen.Caves.BaseCaveHandler;
import Util.FastNoise.NoiseType;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class PopulatorManager implements Decoration //Listener 
{
	public static float skyObjectChance = 0.003f;
	public static float lootChestChance = 0.005f;
	public static float lootStashChance = 0.005f;
	public static float abandonedChance = 0.003f;
	public static float dungeonChance 	= 0.003f;
	public static float lostWorldChance = 0.001f;
	public static float pitChance 		= 0.003f;
	public static float fossilChance 	= 0.002f;
	public static float ruinsThreshold	= 0.5f;
	public static float ruinsChance		= 0.005f;
	
	private FastNoise ruinsNoise;
	public BaseCaveHandler baseCaveHandler = new BaseCaveHandler();
	
	public World world;
	public PopulatorManager(World _world)
	{
		world = _world;
		
		ruinsNoise = new FastNoise();
		ruinsNoise.SetNoiseType(NoiseType.Perlin);
		ruinsNoise.SetFrequency(0.0075f);
	}
	
	@Override
    public void decorate(DecorationArea area, Random random) 
	{
		int realX = area.getCenterX();
		int realZ = area.getCenterZ();
		
		int highestY = BlockUtil.getHighestGroundedY(realX, realZ, area);
		
		//	Generic structures
		if (highestY > 10 && area.getBlock(realX, highestY + 1, realZ) != Material.WATER && area.getBlock(realX, highestY + 1, realZ) != Material.LAVA)
		{
			if (random.nextFloat() <= skyObjectChance && highestY <= 180)
			{
				highestY += 64;
				
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "sky", world);
			}
			else if (random.nextFloat() <= lootChestChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "loot_chest", world);
			}
			else if (random.nextFloat() <= lootStashChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "loot_stash", world);
			}
			else if (random.nextFloat() <= abandonedChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "abandoned", world);
			}
			else if (random.nextFloat() <= dungeonChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "dungeon", world);
			}
			else if (random.nextFloat() <= lostWorldChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "lostworld", world);
			}
			else if (random.nextFloat() <= pitChance && highestY <= 128)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "pit", world);
			}
			else if (random.nextFloat() <= fossilChance && highestY <= 100)
			{
				GenUtil.GenerateObject(random, area, realX, highestY, realZ, "fossil", world);
			}
		}
		
		boolean firstPass = true;
		boolean firstCavePass = true;
		for (int x = 0; x < 16; x++) 
		{
			for (int z = 0; z < 16; z++) 
			{
				boolean foundBiome = false;
				
				int safeX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + Util.clamp(x, 4, 12);
				int safeZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + Util.clamp(z, 4, 12);
				
				realX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + x;
				realZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + z;
				highestY = BlockUtil.getHighestGroundedY(realX, realZ, area);//area.getHighestBlockYAt(realX, realZ);
				
				Biome biome = area.getBiome(realX, realZ);
				
				//	Clumped structures
				if (random.nextFloat() <= ruinsChance && ruinsNoise.GetNoise(realX, realZ) >= ruinsThreshold)
				{
					GenUtil.GenerateObject(random, area, realX, highestY, realZ, "ruin", world);
				}
				
				//	Run secondary biomes first if we are in the 2nd biome layer
				if (Main.GetAltBiomeNoise().GetSimplexFractal(realX, realZ) < 0)
				{
					for (int i = 0; i < Main.getAltChunkHandlers().length; i++)
					{
						if (Main.getAltChunkHandlers()[i].getValidBiomes().contains(biome) == true)
						{
							Main.getAltChunkHandlers()[i].PopulateAt(random, realX, realZ, area, world);
							
							if (firstPass == true)
							{ 
								Main.getAltChunkHandlers()[i].Populate(random, area, world);
								Main.getAltChunkHandlers()[i].PlaceStructure(random, area, world);
							}
							
							foundBiome = true;
							firstPass = false;
							break;
						}
					}
				}
				
				//	No secondary biome, default to the main biome
				if (foundBiome == false)
				{
					for (int i = 0; i < Main.getChunkHandlers().length; i++)
					{
						if (Main.getChunkHandlers()[i].getValidBiomes().contains(biome) == true)
						{
							Main.getChunkHandlers()[i].PopulateAt(random, realX, realZ, area, world);
							
							if (firstPass == true)
							{
								Main.getChunkHandlers()[i].Populate(random, area, world);
								Main.getChunkHandlers()[i].PlaceStructure(random, area, world);
							}
							
							foundBiome = true;
							firstPass = false;
							break;
						}
					}
				}
				
				//	Caves
				for (int i = 0; i < Main.getCaveHandlers().length; i++)
				{
					if (Main.getCaveHandlers()[i].getValidBiomes().contains(biome) == true)
					{
						Main.getCaveHandlers()[i].PopulateAt(random, realX, highestY, realZ, area, world);
						
						if (firstCavePass == true)
							Main.getCaveHandlers()[i].PlaceStructure(random, area, world);
						
						break;
					}
				}
				
				//	Base cave decorator
				baseCaveHandler.PopulateAt(random, realX, highestY-1, realZ, area, world);
				if (firstCavePass == true) baseCaveHandler.PlaceStructure(random, area, world);
				
				firstCavePass = false;
			}
		}
    }
}
