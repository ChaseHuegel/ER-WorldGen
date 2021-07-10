package Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
}
