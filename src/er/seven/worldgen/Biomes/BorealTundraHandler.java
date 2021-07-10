package er.seven.worldgen.Biomes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import Util.BlockUtil;
import Util.GenUtil;
import er.seven.worldgen.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class BorealTundraHandler extends ChunkHandler
{	
	//	DECORATION
	private static float BushChance = 0.01f;
	
	private static Object[] foliageTable = new Object[] {
			Material.SWEET_BERRY_BUSH, 3,
			Material.GRASS, 250};
	
	private static Material[] rockTable = new Material[] {
			Material.COBBLESTONE,
			Material.MOSSY_COBBLESTONE};
	
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
	public List<Biome> getValidBiomes() { return Arrays.asList( Biome.GIANT_TREE_TAIGA ); }
	
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
		
		y = highestY;
		
		//	Bushes
		if (random.nextFloat() <= BushChance && BlockUtil.isDirt(block) )
		{
			Material bushMaterial;
			if (random.nextBoolean()) bushMaterial = Material.SPRUCE_LEAVES; else bushMaterial = Material.OAK_LEAVES;
			
			BlockUtil.buildBlob(random, area, x, y, z, 2, 2, 2, bushMaterial);
		}
		//	Foliage
		else if (blockAbove != Material.WATER && BlockUtil.isDirt(block))
		{
			BlockUtil.setFoliage(area, x, y + 1, z, BlockUtil.weightedRandomMaterial(random, foliageTable));
		}
	}
}
