package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class SnowyBorealTundraHandler extends ChunkHandler
{	
	//	DECORATION
	private static float BushChance = 0.01f;
	
	//	STRATA
	private static Object[] StrataLayers = new Object[] {
			Material.ANDESITE, 4,
			Material.BLACKSTONE, 20,
			Material.GRANITE, 30,
			Material.STONE, 40,
			Material.DIORITE, 52,
			Material.BLACKSTONE, 58,
			Material.BASALT, 61};
	
	@Override
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.SNOWY_TAIGA_MOUNTAINS ); }
	
	@Override
	public void GenerateAt(Random random, int x, int z, DecorationArea area, World world)
	{		
		for (int y = 128; y > 8; y--) 
		{
			BlockData block = area.getBlockData(x, y, z);
			
			if (BlockUtil.isAir(block.getMaterial()) == true) { continue; }
					
			Material replacementMaterial = null;
			
			if (y > 16 || random.nextInt(y) > 8)
			{
				switch (block.getMaterial())
				{
				case STONE:				replacementMaterial = GenUtil.GenerateStrata(random, area, x, y, z, StrataLayers); break;
				default: break;
				}
			}
			
			if (replacementMaterial != null)
			{
				area.setBlock(x, y, z, replacementMaterial);
			}
		}
	}
	
	@Override
	public void PopulateAt(Random random, int x, int z, DecorationArea area, World world)
	{
		int y = 0;
		int highestY = BlockUtil.getHighestSolidY(x, z, area);
		Material block = area.getBlock(x, highestY, z);
		Material blockAbove = area.getBlock(x, highestY + 1, z);
		
		//	Snow on things!
		for (y = highestY; y < world.getMaxHeight() - 1; y++)
		{
			if (area.getBlock(x, y, z) != Material.DIRT_PATH && area.getBlock(x, y, z).isOccluding() == true && area.getBlock(x, y + 1, z).isOccluding() == false)
			{
				if ((BlockUtil.isDirt(area.getBlock(x + 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x - 1, y + 1, z)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z + 1)) == true ||
					BlockUtil.isDirt(area.getBlock(x, y + 1, z - 1)) == true ) &&
					(BlockUtil.isLeaves(area.getBlock(x + 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x - 1, y + 1, z)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z + 1)) == false &&
					BlockUtil.isLeaves(area.getBlock(x, y + 1, z - 1)) == false ))
				{
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
					area.setBlockData(x, y + 1, z, data);
				}
				else
				{
					area.setBlock(x, y + 1, z, Material.SNOW);
					Snow data = (Snow)area.getBlockData(x, y + 1, z);
					area.setBlockData(x, y + 1, z, data);
				}
			}
		}
		
		y = highestY;
		
		//	Bushes
		if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{
			Material bushMaterial;
			if (random.nextBoolean()) bushMaterial = Material.SPRUCE_LEAVES; else bushMaterial = Material.OAK_LEAVES;
			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, bushMaterial);
		}
	}
}
