package Util;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.loot.LootTables;

import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class BlockUtil 
{
	public static boolean isSideExposed(DecorationArea area, int x, int y, int z)
	{
		return (
				area.getBlock(x + 1, y, z).isOccluding() == false ||
				area.getBlock(x - 1, y, z).isOccluding() == false ||
				area.getBlock(x, y, z + 1).isOccluding() == false ||
				area.getBlock(x, y, z - 1).isOccluding() == false
			);
	}
	
	public static boolean isTopExposed(DecorationArea area, int x, int y, int z)
	{
		return (
				area.getBlock(x, y + 1, z).isOccluding() == false
			);
	}
	
	public static boolean isBottomExposed(DecorationArea area, int x, int y, int z)
	{
		return (
				area.getBlock(x, y - 1, z).isOccluding() == false
			);
	}
	
	public static boolean isExposed(DecorationArea area, int x, int y, int z)
	{
		return (
				isTopExposed(area, x, y, z) ||
				isBottomExposed(area, x, y, z) ||
				isSideExposed(area, x, y, z)
			);
	}
	
	public static boolean isGroundMaterial(Material material)
	{
		return (material == Material.GRASS_BLOCK ||
				material == Material.PODZOL ||
				material == Material.DIRT ||
				material == Material.SAND ||
				material == Material.COARSE_DIRT ||
				material == Material.GRAVEL ||
				material == Material.MYCELIUM ||
				material == Material.CRIMSON_NYLIUM ||
				material == Material.WARPED_NYLIUM ||
				material == Material.SOUL_SOIL);
	}
	
	public static boolean isDirt(Material material)
	{
		return (material == Material.DIRT ||
				material == Material.COARSE_DIRT||
				material == Material.GRASS_BLOCK||
				material == Material.PODZOL ||
				material == Material.MYCELIUM ||
				material == Material.CRIMSON_NYLIUM ||
				material == Material.WARPED_NYLIUM);
	}
	
	public static boolean isStoneBricks(Material material)
	{
		return (material == Material.STONE_BRICKS ||
				material == Material.MOSSY_STONE_BRICKS ||
				material == Material.CRACKED_STONE_BRICKS);
	}
	
	public static boolean isCobblestone(Material material)
	{
		return (material == Material.COBBLESTONE ||
				material == Material.MOSSY_COBBLESTONE);
	}
	
	public static boolean isAir(Material material)
	{
		return (material == Material.AIR ||
				material == Material.CAVE_AIR);
	}
	
	public static boolean isLeaves(Material material)
	{
		return Tag.LEAVES.getValues().contains(material);
	}
	
	public static boolean isLog(Material material)
	{
		return Tag.LOGS.getValues().contains(material);
	}
	
	public static boolean isTallPlant(Material material)
	{
		return (material == Material.TALL_GRASS ||
				material == Material.TALL_SEAGRASS ||
				material == Material.LARGE_FERN ||
				material == Material.SUNFLOWER ||
				material == Material.ROSE_BUSH ||
				material == Material.LILAC ||
				material == Material.PEONY);
	}
	
	public static boolean isNatural(Material material)
	{
		return (material == Material.ACACIA_PLANKS ||
				material == Material.BIRCH_PLANKS ||
				material == Material.DARK_OAK_PLANKS ||
				material == Material.JUNGLE_PLANKS ||
				material == Material.OAK_PLANKS ||
				material == Material.SPRUCE_PLANKS ||
				material == Material.COBBLESTONE ||
				material == Material.MOSSY_COBBLESTONE);
	}
	
	public static boolean isTreeMaterial(Material material)
	{
		if (BlockUtil.isLog(material) == true || BlockUtil.isLeaves(material) == true)
		{
			return true;
		}
		
		return false;
	}
	
	public static Material ReskinMaterial(Material material, Material seed)
	{
		switch (seed)
		{
		case PRISMARINE:
			switch(material)
			{
			case COBBLESTONE: 			return Material.PRISMARINE;
			case MOSSY_COBBLESTONE: 	return Material.PRISMARINE;
			case COBBLESTONE_STAIRS: 	return Material.PRISMARINE_STAIRS;
			case COBBLESTONE_SLAB: 		return Material.PRISMARINE_SLAB;
			case STONE_BRICKS: 			return Material.PRISMARINE_BRICKS;
			case CRACKED_STONE_BRICKS: 	return Material.PRISMARINE_BRICKS;
			case MOSSY_STONE_BRICKS: 	return Material.PRISMARINE_BRICKS;
			case STONE_BRICK_STAIRS: 	return Material.PRISMARINE_BRICK_STAIRS;
			case STONE_BRICK_SLAB: 		return Material.PRISMARINE_BRICK_SLAB;
			default:
				break;
			}
			
			if (isLog(material)) 	return Material.DARK_PRISMARINE;
			if (isLeaves(material)) return Material.BRAIN_CORAL_BLOCK;
		
		case RED_SANDSTONE:
			switch(material)
			{
			case COBBLESTONE: 			return Material.RED_SANDSTONE;
			case MOSSY_COBBLESTONE: 	return Material.CHISELED_RED_SANDSTONE;
			case COBBLESTONE_STAIRS: 	return Material.RED_SANDSTONE_STAIRS;
			case COBBLESTONE_SLAB: 		return Material.RED_SANDSTONE_SLAB;
			case STONE_BRICKS: 			return Material.RED_SANDSTONE;
			case CRACKED_STONE_BRICKS: 	return Material.CUT_RED_SANDSTONE;
			case MOSSY_STONE_BRICKS: 	return Material.SMOOTH_RED_SANDSTONE;
			case STONE_BRICK_STAIRS: 	return Material.RED_SANDSTONE_STAIRS;
			case STONE_BRICK_SLAB: 		return Material.RED_SANDSTONE_SLAB;
			default:
				break;
			}
			
			if (isLog(material)) 	return Material.RED_SANDSTONE;
			if (isLeaves(material)) return Material.ACACIA_LEAVES;
		
		case SANDSTONE:
			switch(material)
			{
			case COBBLESTONE: 			return Material.SANDSTONE;
			case MOSSY_COBBLESTONE: 	return Material.CHISELED_SANDSTONE;
			case COBBLESTONE_STAIRS: 	return Material.SANDSTONE_STAIRS;
			case COBBLESTONE_SLAB: 		return Material.SANDSTONE_SLAB;
			case STONE_BRICKS: 			return Material.SANDSTONE;
			case CRACKED_STONE_BRICKS: 	return Material.CUT_SANDSTONE;
			case MOSSY_STONE_BRICKS: 	return Material.SMOOTH_SANDSTONE;
			case STONE_BRICK_STAIRS: 	return Material.SANDSTONE_STAIRS;
			case STONE_BRICK_SLAB: 		return Material.SANDSTONE_SLAB;
			default:
				break;
			}
			
			if (isLog(material)) 	return Material.SANDSTONE;
			if (isLeaves(material)) return Material.ACACIA_LEAVES;
			
		default:
			break;
		}
		
		return material;
	}
	
	public static Material getStalagMaterial(Material material)
	{
		switch (material)
		{
			case SANDSTONE: return Material.SANDSTONE_WALL;
			case RED_SANDSTONE: return Material.RED_SANDSTONE_WALL;
			case SAND: return Material.SANDSTONE_WALL;
			case DIORITE: return Material.DIORITE_WALL;
			case ANDESITE: return Material.ANDESITE_WALL;
			case GRANITE: return Material.GRANITE_WALL;
			case TERRACOTTA: return Material.GRANITE_WALL;
			case DIRT: return Material.MOSSY_COBBLESTONE_WALL;
			case BLACKSTONE: return Material.BLACKSTONE_WALL;
			case OBSIDIAN: return Material.BLACKSTONE_WALL;
			case BASALT: return Material.BLACKSTONE_WALL;
			case RED_MUSHROOM_BLOCK: return Material.MUSHROOM_STEM;
			case BROWN_MUSHROOM_BLOCK: return Material.MUSHROOM_STEM;
			case PACKED_ICE: return Material.BLUE_ICE;
			case BLUE_ICE: return Material.PACKED_ICE;
			case SNOW_BLOCK: return Material.BLUE_ICE;
			default: break;
		}
		
		return Material.COBBLESTONE_WALL;
	}
	
	public static int getHighestSolidY(int x, int z, DecorationArea area)
	{
		for (int i = 250; i > 4; i--)
		{
			if (area.getBlock(x, i, z).isSolid() == true && isLeaves(area.getBlock(x, i, z)) == false && isLog(area.getBlock(x, i, z)) == false)
			{
				return i;
			}
		}
		
		return 1;
	}
	
	public static int getHighestGroundedY(int x, int z, DecorationArea area)
	{
		for (int i = 250; i > 20; i--)
		{
			if (area.getBlock(x, i, z).isSolid() == true && isLeaves(area.getBlock(x, i, z)) == false && isLog(area.getBlock(x, i, z)) == false && isGroundMaterial(area.getBlock(x, i, z)) == true)
			{
				return i;
			}
		}
		
		return 1;
	}
	
	public static Block getHighestSolidBlock(int x, int z, Chunk chunk)
	{
		for (int i = chunk.getWorld().getMaxHeight() - 1; i > 0; i--)
		{
			if (chunk.getBlock(x, i, z).isPassable() == false && isLeaves(chunk.getBlock(x, i, z).getType()) == false)
			{
				return chunk.getBlock(x, i, z);
			}
		}
		
		return chunk.getBlock(x, 1, z);
	}
	
	public static boolean removeTreeMaterials(Block block)
	{
		if (BlockUtil.isLog(block.getType()) == true || BlockUtil.isLeaves(block.getType()) == true)
		{
			block.setType(Material.AIR, false);
			return true;
		}
		
		return false;
	}
	
	  public static final Material[] stoneBricks = new Material[] { Material.STONE_BRICKS, 
	      Material.MOSSY_STONE_BRICKS, 
	      Material.CRACKED_STONE_BRICKS };
	  
	  public static Material stoneBrick(Random rand) {
	    return randMaterial(rand, stoneBricks);
	  }
	  
	  public static final Material[] stoneBrickSlabs = new Material[] { Material.STONE_BRICK_SLAB, 
	      Material.MOSSY_STONE_BRICK_SLAB };
	  
	  public static Material stoneBrickSlab(Random rand) {
	    return randMaterial(rand, stoneBrickSlabs);
	  }
	  
	  public static Material randMaterial(Random rand, Material... candidates) {
	    return candidates[rand.nextInt(candidates.length)];
	  }
	  
	  public static Material randMaterial(Material... candidates) {
	    return randMaterial(new Random(), candidates);
	  }
	  
	  public static Material weightedRandomMaterial(Random rand, Object... candidates) {
	    if (candidates.length % 2 != 0)
	      throw new IllegalArgumentException(); 
	    ArrayList<Material> types = new ArrayList<>();
	    for (int i = 0; i < candidates.length; i++) {
	      Material type = (Material)candidates[i];
	      i++;
	      int freq = ((Integer)candidates[i]).intValue();
	      for (int z = 0; z < freq; z++)
	        types.add(type); 
	    } 
	    return types.get(rand.nextInt(types.size()));
	  }
	  
	  public static Material pickFlower() {
	    return randMaterial(new Material[] { 
	          Material.DANDELION, 
	          Material.POPPY, 
	          Material.WHITE_TULIP, 
	          Material.ORANGE_TULIP, 
	          Material.RED_TULIP, 
	          Material.PINK_TULIP, 
	          Material.BLUE_ORCHID, 
	          Material.ALLIUM, 
	          Material.AZURE_BLUET, 
	          Material.OXEYE_DAISY, 
	          Material.CORNFLOWER, 
	          Material.LILY_OF_THE_VALLEY, 
	          Material.PINK_TULIP });
	  }
	  
	  public static Material pickTallFlower() {
	    return randMaterial(new Material[] { Material.LILAC, 
	          Material.ROSE_BUSH, 
	          Material.PEONY, 
	          Material.SUNFLOWER,
	          Material.LILAC });
	  }
	  
	  public static double distanceSquared(float x1, float y1, float z1, float x2, float y2, float z2) {
	    return Math.pow((x2 - x1), 2.0D) + Math.pow((y2 - y1), 2.0D) + Math.pow((z2 - z1), 2.0D);
	  }
	  
	  public static void setPersistentLeaves(Block block, Material material)
	  {
		  block.setType(material, false);
		  Leaves data = (Leaves)material.createBlockData();
		  data.setPersistent(true);
		  block.setBlockData(data, false);
	  }
	  
	  public static void setTallPlant(Block block, Material material)
	  {
		  block.setType(material, false);
		  block.getRelative(0, 1, 0).setType(material, false);
		  Bisected data = (Bisected)material.createBlockData();
		  data.setHalf(Bisected.Half.TOP);
		  block.getRelative(0, 1, 0).setBlockData(data, false);
	  }
	  
	  public static void setPersistentLeaves(BlockState block, Material material)
	  {
		  block.setType(material);
		  Leaves data = (Leaves)material.createBlockData();
		  data.setPersistent(true);
		  block.setBlockData(data);
	  }
	  
	  public static void setTallPlant(BlockState block, BlockState block2, Material material)
	  {
		  block.setType(material);
		  block2.setType(material);
		  Bisected data = (Bisected)material.createBlockData();
		  data.setHalf(Bisected.Half.TOP);
		  block2.setBlockData(data);
	  }
	  
	  public static void setPersistentLeaves(DecorationArea area, int x, int y, int z, Material material)
	  {
		  area.setBlock(x, y, z, material);
		  Leaves data = (Leaves)material.createBlockData();
		  data.setPersistent(true);
		  area.setBlockData(x, y, z, data);
	  }
	  
	  public static void setTallPlant(DecorationArea area, int x, int y, int z, Material material)
	  {
		  area.setBlock(x, y, z, material);
		  area.setBlock(x, y + 1, z, material);
		  Bisected data = (Bisected)material.createBlockData();
		  data.setHalf(Bisected.Half.TOP);
		  area.setBlockData(x, y + 1, z, data);
	  }
	  
	  public static void setGrownPlant(DecorationArea area, int x, int y, int z, Material material, boolean randomAge, int minAge, int maxAge)
	  {		  
		  if (randomAge == true)
		  {
			  area.setBlock(x, y, z, material);
			  Ageable data = (Ageable)material.createBlockData();
			  if (maxAge == -1) { maxAge = data.getMaximumAge(); }
			  data.setAge( minAge + (new Random()).nextInt(maxAge - minAge) );
			  area.setBlockData(x, y, z, data);
		  }
		  else
		  {
			  area.setBlock(x, y, z, material);
			  Ageable data = (Ageable)material.createBlockData();
			  data.setAge( data.getMaximumAge() );
			  area.setBlockData(x, y, z, data);
		  }
	  }
	  
	  public static void setGrownPlant(DecorationArea area, int x, int y, int z, Material material, boolean randomAge)
	  {
		  setGrownPlant(area, x, y, z, material, randomAge, 1, -1);
	  }
	  
	  public static void setGrownPlant(DecorationArea area, int x, int y, int z, Material material)
	  {
		  setGrownPlant(area, x, y, z, material, false, 1, 01);
	  }
	  
	  public static void setFoliage(DecorationArea area, int x, int y, int z, Material material)
	  {
		  if (new Random().nextFloat() > 0.8) return;
		  
		  if (BlockUtil.isLeaves(material))
		  {
			  BlockUtil.setPersistentLeaves(area, x, y, z, material);
		  }
		  else if (BlockUtil.isTallPlant(material))
		  {
			  BlockUtil.setTallPlant(area, x, y, z, material);
		  }
		  else if (material == Material.SWEET_BERRY_BUSH)
		  {
			  BlockUtil.setGrownPlant(area, x, y, z, material);
		  }
		  else if (Tag.CROPS.getValues().contains(material))
		  {
			  BlockUtil.setGrownPlant(area, x, y, z, material, true);
		  }
		  else
		  {
			  area.setBlock(x, y, z, material);
		  }
	  }
	  	  
	  public static void generateClayDeposit(int x, int y, int z, Block data, Random random) {
	    int length = new Random().nextInt(4) + 4;
	    int nx = x;
	    int ny = y;
	    int nz = z;
	    while (length > 0) {
	      length--;
	      if (data.getRelative(nx, ny, nz).getType() == Material.SAND || 
	        data.getRelative(nx, ny, nz).getType() == Material.GRAVEL)
	        data.getRelative(nx, ny, nz).setType(Material.CLAY, false); 
	      switch (random.nextInt(5)) {
	        case 0:
	          nx++;
	          break;
	        case 1:
	          ny++;
	          break;
	        case 2:
	          nz++;
	          break;
	        case 3:
	          nx--;
	          break;
	        case 4:
	          ny--;
	          break;
	        case 5:
	          nz--;
	          break;
	      } 
	      if (ny > y)
	        ny = y; 
	      if (ny < 2)
	        ny = 2; 
	    } 
	  }
	  
	  public static void buildBlob(Random random, DecorationArea area, int blockX, int blockY, int blockZ, int rX, int rY, int rZ, Material... candidates) {
		    if (rX <= 0.0F && 
		      rY <= 0.0F && 
		      rZ <= 0.0F)
		      return; 
		    if (rX <= 0.5D && 
		      rY <= 0.5D && 
		      rZ <= 0.5D) {
		      area.setBlock(blockX, blockY, blockZ, randMaterial(candidates));
		      return;
		    }
		    FastNoise noise = new FastNoise(random.nextInt());
		    noise.SetNoiseType(FastNoise.NoiseType.Simplex);
		    noise.SetFrequency(0.09F);
		    for (int x = -rX; x <= rX; x++) {
		      for (int y = -rY; y <= rY; y++) {
		        for (int z = -rZ; z <= rZ; z++) {
		        	Material rel = area.getBlock(blockX + x, blockY + y, blockZ + z);
		          double equationResult = Math.pow(x, 2.0D) / Math.pow(rX, 2.0D) + 
		            Math.pow(y, 2.0D) / Math.pow(rY, 2.0D) + 
		            Math.pow(z, 2.0D) / Math.pow(rZ, 2.0D);
		          if (equationResult <= 1.0D + 0.7D * noise.GetNoise(blockX + x, blockY + y, blockZ + z))
		            if (!rel.isSolid())
		            {
		            	Material material = randMaterial(candidates);
		            	area.setBlock(blockX + x, blockY + y, blockZ + z, material);
		            	if (BlockUtil.isLeaves(material))
			      		  {
			      			  BlockUtil.setPersistentLeaves(area, blockX + x, blockY + y, blockZ + z, material);
			      		  }
		            }
		        } 
		      } 
		    } 
		  }
	  
	  public static void buildBlob(Random random, DecorationArea area, int blockX, int blockY, int blockZ, int rX, int rY, int rZ, Material material) {
		    if (rX <= 0.0F && 
		      rY <= 0.0F && 
		      rZ <= 0.0F)
		      return; 
		    if (rX <= 0.5D && 
		      rY <= 0.5D && 
		      rZ <= 0.5D) {
		      area.setBlock(blockX, blockY, blockZ, material);
		      return;
		    }
		    FastNoise noise = new FastNoise(random.nextInt());
		    noise.SetNoiseType(FastNoise.NoiseType.Simplex);
		    noise.SetFrequency(0.09F);
		    for (int x = -rX; x <= rX; x++) {
		      for (int y = -rY; y <= rY; y++) {
		        for (int z = -rZ; z <= rZ; z++) {
		        	Material rel = area.getBlock(blockX + x, blockY + y, blockZ + z);
		          double equationResult = Math.pow(x, 2.0D) / Math.pow(rX, 2.0D) + 
		            Math.pow(y, 2.0D) / Math.pow(rY, 2.0D) + 
		            Math.pow(z, 2.0D) / Math.pow(rZ, 2.0D);
		          if (equationResult <= 1.0D + 0.7D * noise.GetNoise(blockX + x, blockY + y, blockZ + z))
		            if (!rel.isSolid())
		            {
		            	area.setBlock(blockX + x, blockY + y, blockZ + z, material);  
		            	if (BlockUtil.isLeaves(material))
			      		  {
			      			  BlockUtil.setPersistentLeaves(area, blockX + x, blockY + y, blockZ + z, material);
			      		  }
		            }
		        } 
		      } 
		    } 
		  }
	  
	  public static void buildBlob(int seed, float rX, float rY, float rZ, Block block, Material material) {
		    if (rX <= 0.0F && 
		      rY <= 0.0F && 
		      rZ <= 0.0F)
		      return; 
		    if (rX <= 0.5D && 
		      rY <= 0.5D && 
		      rZ <= 0.5D) {
		      block.setType(material, false);
		      return;
		    }
		    FastNoise noise = new FastNoise(seed);
		    noise.SetNoiseType(FastNoise.NoiseType.Simplex);
		    noise.SetFrequency(0.09F);
		    for (float x = -rX; x <= rX; x++) {
		      for (float y = -rY; y <= rY; y++) {
		        for (float z = -rZ; z <= rZ; z++) {
		        	Block rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
		          double equationResult = Math.pow(x, 2.0D) / Math.pow(rX, 2.0D) + 
		            Math.pow(y, 2.0D) / Math.pow(rY, 2.0D) + 
		            Math.pow(z, 2.0D) / Math.pow(rZ, 2.0D);
		          if (equationResult <= 1.0D + 0.7D * noise.GetNoise(rel.getX(), rel.getY(), rel.getZ()))
		            if (!rel.getType().isSolid())
		              rel.setType(material, false);  
		        } 
		      } 
		    } 
		  }
	  
	  public static void carveCaveAir(int seed, float rX, float rY, float rZ, Block block, ArrayList<Material> toReplace) {
	    if (rX <= 0.0F && 
	      rY <= 0.0F && 
	      rZ <= 0.0F)
	      return; 
	    if (rX <= 0.5D && 
	      rY <= 0.5D && 
	      rZ <= 0.5D) {
	      block.setType(Material.CAVE_AIR, false);
	      return;
	    }
	    FastNoise noise = new FastNoise(seed);
	    noise.SetNoiseType(FastNoise.NoiseType.Simplex);
	    noise.SetFrequency(0.09F);
	    for (float x = -rX; x <= rX; x++) {
	      for (float y = -rY; y <= rY; y++) {
	        for (float z = -rZ; z <= rZ; z++) {
	        	Block rel = block.getRelative(Math.round(x), Math.round(y), Math.round(z));
	          double equationResult = Math.pow(x, 2.0D) / Math.pow(rX, 2.0D) + 
	            Math.pow(y, 2.0D) / Math.pow(rY, 2.0D) + 
	            Math.pow(z, 2.0D) / Math.pow(rZ, 2.0D);
	          if (equationResult <= 1.0D + 0.7D * noise.GetNoise(rel.getX(), rel.getY(), rel.getZ()))
	            if (!rel.getType().isSolid() || 
	              toReplace.contains(rel.getType()))
	              rel.setType(Material.CAVE_AIR, false);  
	        } 
	      } 
	    } 
	  }
}
