package er.seven.worldgen;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Biome;

import Util.BlockUtil;
import er.seven.worldgen.Caves.BaseCaveHandler;
import nl.rutgerkok.worldgeneratorapi.decoration.Decoration;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class GeneratorManager implements Decoration 
{		
	public BaseCaveHandler baseCaveHandler = new BaseCaveHandler();
	
	public World world;
	public GeneratorManager(World _world)
	{
		world = _world;
	}
	
	@Override
    public void decorate(DecorationArea area, Random random) 
	{
		for (int x = 0; x < 16; x++) 
		{
			for (int z = 0; z < 16; z++) 
			{
				boolean foundBiome = false;
				
				int realX = area.getCenterX() - DecorationArea.DECORATION_RADIUS + x;
				int realZ = area.getCenterZ() - DecorationArea.DECORATION_RADIUS + z;
				int highestY = BlockUtil.getHighestSolidY(realX, realZ, area);
				
				Biome biome = area.getBiome(realX, realZ);
				
				//	Base cave decorator
//				baseCaveHandler.GenerateAt(random, realX, highestY, realZ, area, world);
				
				//	Handle caves first
				for (int i = 0; i < Main.getCaveHandlers().length; i++)
				{
					for (int depth = -63; depth < highestY-1; depth++)
					{
						//	Don't mask air
						if (BlockUtil.isAir(area.getBlock(realX, depth, realZ)) == false)
						{
							if (Main.getCaveHandlers()[i].getValidBiomes().contains(biome) == true)
							{
								//	Don't generate on liquid, liquid alterations are a separate method
								if (BlockUtil.isLiquid(area.getBlock(realX, depth, realZ)) == false)
									Main.getCaveHandlers()[i].GenerateAt(random, realX, depth, realZ, area, world);
								else
									Main.getCaveHandlers()[i].ChangeLiquidAt(random, realX, depth, realZ, area, world);
								
								break;
							}
						}
					}
				}
				
				//	Run secondary biomes first if we are in the 2nd biome layer
				if (Main.GetAltBiomeNoise().GetSimplexFractal(realX, realZ) < 0)
				{
					for (int i = 0; i < Main.getAltChunkHandlers().length; i++)
					{
						if (Main.getAltChunkHandlers()[i].getValidBiomes().contains(biome) == true)
						{							
							Main.getAltChunkHandlers()[i].GenerateAt(random, realX, realZ, area, world);
							
							foundBiome = true;
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
							Main.getChunkHandlers()[i].GenerateAt(random, realX, realZ, area, world);
							
							foundBiome = true;
							break;
						}
					}
				}
			}
		}
    }
}