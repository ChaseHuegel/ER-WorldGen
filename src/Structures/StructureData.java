package Structures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

public class StructureData 
{
	public String name = "";
	public String category = "";
	public List<Material> materials = new ArrayList<Material>();
	public List<Location> locations = new ArrayList<Location>();
	
	public StructureData() {}
	public StructureData(String str) { name = str; }
	
	public Material getMaterial(int index)
	{
		return materials.get(index);
	}
	
	public Location getLocation(int index)
	{
		return locations.get(index);
	}
	
	public int size()
	{
		return materials.size();
	}
}
