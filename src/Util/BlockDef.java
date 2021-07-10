package Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class BlockDef
{		
	public World world;
	public int x, y, z;
	
	public Material material;
	public BlockData blockData;
	
	public BlockDef(Location loc, Material mat, BlockData data)
	{
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = loc.getWorld();
		material = mat;
		blockData = data;
	}
	
	public BlockDef(Location loc, World world, Material mat, BlockData data)
	{
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = world;
		material = mat;
		blockData = data;
	}
	
	public BlockDef(int x, int y, int z, Material mat, BlockData data)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		material = mat;
		blockData = data;
	}
	
	public BlockDef(int x, int y, int z, World world, Material mat, BlockData data)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		material = mat;
		blockData = data;
	}
	
	public BlockDef(Location loc, Material mat)
	{
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = loc.getWorld();
		material = mat;
	}
	
	public BlockDef(Location loc, World world, Material mat)
	{
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = world;
		material = mat;
	}
	
	public BlockDef(int x, int y, int z, Material mat)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		material = mat;
	}
	
	public BlockDef(int x, int y, int z, World world, Material mat)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
		material = mat;
	}
	
	public Location GetLocation()
	{
		return new Location(world, x, y, z);
	}
	
	public static BlockDef GetAt(List<BlockDef> blocks, int x, int y, int z)
	{		
		for (int i = 0; i < blocks.size(); i++)
		{
			BlockDef def = blocks.get(i);
			
			if (def.x == x && def.y == y && def.z == z)
				return def;
		}
		
		return null;
	}
	
	public static BlockDef GetRelative(List<BlockDef> blocks, int x, int y, int z, BlockFace direction)
	{		
		for (int i = 0; i < blocks.size(); i++)
		{
			BlockDef def = blocks.get(i);
			
			if (def.x == x+direction.getModX() && def.y == y+direction.getModY() && def.z == z+direction.getModZ())
				return def;
		}
		
		return null;
	}
	
	public static BlockDef GetFirstNeighbor(List<BlockDef> blocks, int x, int y, int z, boolean solid)
	{		
		for (int i = 0; i < blocks.size(); i++)
		{
			BlockDef def = blocks.get(i);
			
			if (GetRelative(blocks, x, y, z, BlockFace.NORTH).material.isSolid() == solid) return def;
			if (GetRelative(blocks, x, y, z, BlockFace.EAST).material.isSolid() == solid) return def;
			if (GetRelative(blocks, x, y, z, BlockFace.SOUTH).material.isSolid() == solid) return def;
			if (GetRelative(blocks, x, y, z, BlockFace.WEST).material.isSolid() == solid) return def;
		}
		
		return null;
	}
	
	public static List<BlockDef> GetNeighbors(List<BlockDef> blocks, int x, int y, int z)
	{
		List<BlockDef> neighbors = new ArrayList<BlockDef>();
		
		BlockDef neighbor;
		
		neighbor = GetRelative(blocks, x, y, z, BlockFace.NORTH); if (neighbor != null) neighbors.add(neighbor);
		neighbor = GetRelative(blocks, x, y, z, BlockFace.EAST); if (neighbor != null) neighbors.add(neighbor);
		neighbor = GetRelative(blocks, x, y, z, BlockFace.SOUTH); if (neighbor != null) neighbors.add(neighbor);
		neighbor = GetRelative(blocks, x, y, z, BlockFace.WEST); if (neighbor != null) neighbors.add(neighbor);
		
		return null;
	}
}
