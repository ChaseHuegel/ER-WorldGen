package Structures;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.loot.LootTables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import Util.Util;
import Util.GenUtil;
import er.seven.worldgen.Main;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationArea;

public class StructureUtil 
{
	private static StructureUtil instance;
	public static StructureUtil Instance() { if (instance == null) instance = new StructureUtil(); return instance; }
	
	public HashMap<Player, Location> structureP1 = new HashMap<>();
    public HashMap<Player, Location> structureP2 = new HashMap<>();
    public HashMap<Player, Location> structureCenter = new HashMap<>();
    public HashMap<Player, StructureData> structureClipboard = new HashMap<>();
    
    public static void setPlayerP1(Player player, Location loc)
    {
    	Instance().structureP1.put(player, loc);
    }
    
    public static void setPlayerP2(Player player, Location loc)
    {
    	Instance().structureP2.put(player, loc);
    }
	
    public static void setPlayerCenter(Player player, Location loc)
    {
    	Instance().structureCenter.put(player, loc);
    }
    
    public static void setPlayerClipboard(Player player, StructureData data)
    {
    	Instance().structureClipboard.put(player, data);
    }
    
    public static Location getPlayerP1(Player player)
    {
    	return Instance().structureP1.get(player);
    }
    
    public static Location getPlayerP2(Player player)
    {
    	return Instance().structureP2.get(player);
    }
    
    public static Location getPlayerCenter(Player player)
    {
    	return Instance().structureCenter.get(player);
    }
    
    public static StructureData getPlayerClipboard(Player player)
    {
    	return Instance().structureClipboard.get(player);
    }
    
	public static void SaveStructure(String name, Location loc1, Location loc2, Location center)
	{
		World world = loc1.getWorld();
		
		List<Material> materials = new ArrayList<Material>();
		List<Location> locations = new ArrayList<Location>();
		 
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
 
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
 
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++) {
        for(int z = bottomBlockZ; z <= topBlockZ; z++) {
        for(int y = bottomBlockY; y <= topBlockY; y++) 
        {
            Block block = world.getBlockAt(x, y, z);
            
            if (block.getType() == Material.AIR) { continue; }
            
            materials.add(block.getType());
            locations.add( 
            		new Location(
            			null,
        				x - center.getX(),
        				y - center.getY(),
        				z - center.getZ()
        				)
            		);
        }}}
        
        List<String> structureData = new ArrayList<String>();        
        for (int i = 0; i < materials.size(); i++)
        {
        	structureData.add
        	(
        		materials.get(i).toString() + ":" + Math.round(locations.get(i).getX()) + ":" + Math.round(locations.get(i).getY()) + ":" + Math.round(locations.get(i).getZ())
        	);
        }
        
        YamlConfiguration structureYAML = new YamlConfiguration();
        structureYAML.set("data", structureData);
        Main.saveYaml(structureYAML, "plugins/EternalRealms-WorldGen/structures/" + name + ".yml");
	}
	
	public static StructureData LoadStructure(String name)
	{
		YamlConfiguration structureYAML = Main.loadYaml("plugins/EternalRealms-WorldGen/structures/" + name + ".yml");
		StructureData data = new StructureData(name);
		
		List<String> rawData = structureYAML.getStringList("data");
		
		for (int i = 0; i < rawData.size(); i++)
		{
			String str = rawData.get(i);
			String[] entries = str.split(":");
			
			Material thisMaterial = Material.valueOf(entries[0]);
			if (thisMaterial == Material.BARRIER) thisMaterial = Material.AIR;
			data.materials.add( thisMaterial );
			
			data.locations.add( new Location( null, Float.parseFloat(entries[1]), Float.parseFloat(entries[2]), Float.parseFloat(entries[3]) ) );
		}
		
		return data;
	}
	
	public static StructureData LoadStructureFrom(String path, String category)
	{
		YamlConfiguration structureYAML = Main.loadYaml(path);
		StructureData data = new StructureData( Util.removeExtention( Paths.get(path).getFileName().toString() ) );
		data.category = category;
		
		List<String> rawData = structureYAML.getStringList("data");
		
		for (int i = 0; i < rawData.size(); i++)
		{
			String str = rawData.get(i);
			String[] entries = str.split(":");
			if (Tag.LEAVES.getValues().contains(Material.valueOf(entries[0])) == false)
			{				
				Material thisMaterial = Material.valueOf(entries[0]);
				if (thisMaterial == Material.BARRIER) thisMaterial = Material.AIR;
				data.materials.add( thisMaterial );
				
				data.locations.add( new Location( null, Float.parseFloat(entries[1]), Float.parseFloat(entries[2]), Float.parseFloat(entries[3]) ) );
			}
		}
		
		for (int i = 0; i < rawData.size(); i++)
		{
			String str = rawData.get(i);
			String[] entries = str.split(":");
			if (Tag.LEAVES.getValues().contains(Material.valueOf(entries[0])) == true)
			{
				Material thisMaterial = Material.valueOf(entries[0]);
				if (thisMaterial == Material.BARRIER) thisMaterial = Material.AIR;
				data.materials.add( thisMaterial );
				data.locations.add( new Location( null, Float.parseFloat(entries[1]), Float.parseFloat(entries[2]), Float.parseFloat(entries[3]) ) );
			}
		}
		
		return data;
	}
	
	public static void PasteStructure(StructureData structure, Location loc)
	{
		PasteStructure(structure, loc, true);
	}
	
	public static void PasteStructure(StructureData structure, Location loc, boolean ignoreSolids)
	{
		World world = loc.getWorld();
		Location center = new Location(null, loc.getX(), loc.getY(), loc.getZ());
		
		for (int i = 0; i < structure.size(); i++)
		{
			center = new Location (
					null, 
					loc.getX() + structure.getLocation(i).getX(),
					loc.getY() + structure.getLocation(i).getY(),
					loc.getZ() + structure.getLocation(i).getZ()
				);
			
			
			if (ignoreSolids == true)
			{
				if (world.isChunkLoaded( loc.getBlockX() / 16 , loc.getBlockZ() / 16 ) == false)
				{
					world.loadChunk( loc.getBlockX() / 16 , loc.getBlockZ() / 16 );
				}
				
				world.getBlockAt( center )
					 .setType( structure.getMaterial(i), false );
			}
			else if (world.getBlockAt( center ).getType().isSolid() == false)
			{
				if (world.isChunkLoaded( loc.getBlockX() / 16 , loc.getBlockZ() / 16 ) == false)
				{
					world.loadChunk( loc.getBlockX() / 16 , loc.getBlockZ() / 16 );
				}
				
				world.getBlockAt( center )
				 .setType( structure.getMaterial(i), false );
			}
		}
	}
	
	public static void PasteStructure(StructureData structure, int x, int y, int z, DecorationArea area, World world)
	{
		PasteStructure(structure, x, y, z, area, world, true);
	}
	
	public static void PasteStructure(StructureData structure, int x, int y, int z, DecorationArea area, World world, boolean ignoreSolids)
	{
		int pointX = 0;
		int pointY = 0;
		int pointZ = 0;
		
		for (int i = 0; i < structure.size(); i++)
		{			
			pointX = x + structure.getLocation(i).getBlockX();
			pointY = y + structure.getLocation(i).getBlockY();
			pointZ = z + structure.getLocation(i).getBlockZ();
			
			if (ignoreSolids == true)
			{
				area.setBlock(pointX, pointY, pointZ, structure.getMaterial(i));
				
				if (Tag.LEAVES.getValues().contains(structure.getMaterial(i)))
				{
					Leaves data = (Leaves)Bukkit.createBlockData(structure.getMaterial(i));
					data.setDistance(5);
					area.setBlockData(pointX, pointY, pointZ, data);
				}
				
				if (structure.getMaterial(i) == Material.CHEST)
				{
					PlaceLootChest(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.SPAWNER)
				{
					PlaceSpawner(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.BARREL)
				{
					PlaceLootBarrel(structure, world, pointX, pointY, pointZ);
				}
			}
			else if (area.getBlock(pointX, pointY, pointZ).isAir() == true)
			{
				area.setBlock(pointX, pointY, pointZ, structure.getMaterial(i));
				
				if (Tag.LEAVES.getValues().contains(structure.getMaterial(i)))
				{
					Leaves data = (Leaves)Bukkit.createBlockData(structure.getMaterial(i));
					data.setDistance(5);
					area.setBlockData(pointX, pointY, pointZ, data);
				}
				
				if (structure.getMaterial(i) == Material.CHEST)
				{
					PlaceLootChest(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.SPAWNER)
				{
					PlaceSpawner(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.BARREL)
				{
					PlaceLootBarrel(structure, world, pointX, pointY, pointZ);
				}
			}
		}
	}
	
	public static void PasteStructure(StructureData structure, int x, int y, int z, DecorationArea area, World world, List<Material> blacklist)
	{
		int pointX = 0;
		int pointY = 0;
		int pointZ = 0;
		
		for (int i = 0; i < structure.size(); i++)
		{			
			pointX = x + structure.getLocation(i).getBlockX();
			pointY = y + structure.getLocation(i).getBlockY();
			pointZ = z + structure.getLocation(i).getBlockZ();
			
			int xDist = 0;
			if (pointX > area.getCenterX()) {
				xDist = pointX - area.getCenterX();
			} else if (pointX > area.getCenterX()) {
				xDist = area.getCenterX() - pointX;
			}
			
			int zDist = 0;
			if (pointZ > area.getCenterZ()) {
				zDist = pointZ - area.getCenterZ();
			} else if (pointX > area.getCenterZ()) {
				zDist = area.getCenterZ() - pointZ;
			}
			
			int maxDistance = DecorationArea.DECORATION_RADIUS + DecorationArea.DECORATION_RADIUS;
			if (xDist > maxDistance || zDist > maxDistance)
			{
				continue;
			}
			
			if ( blacklist.contains(area.getBlock(pointX, pointY, pointZ)) == false )
			{
				area.setBlock(pointX, pointY, pointZ, structure.getMaterial(i));
				
				if (Tag.LEAVES.getValues().contains(structure.getMaterial(i)))
				{
					Leaves data = (Leaves)Bukkit.createBlockData(structure.getMaterial(i));
					data.setDistance(5);
					area.setBlockData(pointX, pointY, pointZ, data);
				}
				
				if (structure.getMaterial(i) == Material.CHEST)
				{
					PlaceLootChest(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.SPAWNER)
				{
					PlaceSpawner(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.BARREL)
				{
					PlaceLootBarrel(structure, world, pointX, pointY, pointZ);
				}
			}
		}
	}
	
	public static void PasteStructureSafe(StructureData structure, int x, int y, int z, DecorationArea area, World world)
	{
		int pointX = 0;
		int pointY = 0;
		int pointZ = 0;
		
		for (int i = 0; i < structure.size(); i++)
		{			
			pointX = x + structure.getLocation(i).getBlockX();
			pointY = y + structure.getLocation(i).getBlockY();
			pointZ = z + structure.getLocation(i).getBlockZ();
			
			if ( area.getBlock(pointX, pointY, pointZ).isSolid() == false )
			{
				area.setBlock(pointX, pointY, pointZ, structure.getMaterial(i));
				
				if (Tag.LEAVES.getValues().contains(structure.getMaterial(i)))
				{
					Leaves data = (Leaves)Bukkit.createBlockData(structure.getMaterial(i));
					data.setDistance(5);
					area.setBlockData(pointX, pointY, pointZ, data);
				}
				
				if (structure.getMaterial(i) == Material.CHEST)
				{
					PlaceLootChest(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.SPAWNER)
				{
					PlaceSpawner(structure, world, pointX, pointY, pointZ);
				}
				else if (structure.getMaterial(i) == Material.BARREL)
				{
					PlaceLootBarrel(structure, world, pointX, pointY, pointZ);
				}
			}
		}
	}
	
	public static void PlaceSpawner(StructureData structure, World world, int x, int y, int z)
	{
		EntityType mob = EntityType.ZOMBIE;
		
		switch (structure.category)
		{
		case "abandoned":
			mob = EntityType.ZOMBIE;
			break;
		case "desert":
			mob = EntityType.HUSK;
			break;
		case "dungeon":
			EntityType[] mobs = new EntityType[] { EntityType.SKELETON, EntityType.ZOMBIE, EntityType.SLIME, EntityType.CAVE_SPIDER, EntityType.SPIDER, EntityType.DROWNED };
			mob = mobs[(new Random()).nextInt(mobs.length)];
			break;
		case "jungle":
			mob = EntityType.WITHER_SKELETON;
			break;
		case "lostworld":
			mob = EntityType.VEX;
			break;
		case "ruin":
			mob = EntityType.PILLAGER;
			break;
		case "sky":
			mob = EntityType.BLAZE;
			break;
		default: break;
		}
		
		Main.SetMobSpawner(world, x, y, z, mob);
	}
	
	public static void PlaceLootChest(StructureData structure, World world, int x, int y, int z)
	{
		LootTables table = LootTables.SIMPLE_DUNGEON;
		
		switch (structure.category)
		{
		case "abandoned":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.VILLAGE_PLAINS_HOUSE, LootTables.VILLAGE_TAIGA_HOUSE, LootTables.VILLAGE_SNOWY_HOUSE, LootTables.VILLAGE_DESERT_HOUSE, LootTables.VILLAGE_SAVANNA_HOUSE });
			break;
		case "desert":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.DESERT_PYRAMID, LootTables.VILLAGE_TEMPLE });
			break;
		case "dungeon":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.SIMPLE_DUNGEON });
			break;
		case "jungle":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.BASTION_OTHER });
			break;
		case "loot_chest":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.SHIPWRECK_TREASURE, LootTables.SIMPLE_DUNGEON });
			break;
		case "loot_stash":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.PILLAGER_OUTPOST, LootTables.SPAWN_BONUS_CHEST, LootTables.IGLOO_CHEST, LootTables.SHIPWRECK_TREASURE });
			break;
		case "lostworld":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.END_CITY_TREASURE });
			break;
		case "pit":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.ABANDONED_MINESHAFT });
			break;
		case "ruin":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.VILLAGE_ARMORER, LootTables.VILLAGE_BUTCHER, LootTables.VILLAGE_CARTOGRAPHER, LootTables.VILLAGE_FISHER, LootTables.VILLAGE_FLETCHER, 
					LootTables.VILLAGE_MASON, LootTables.VILLAGE_SHEPHERD, LootTables.VILLAGE_TANNERY, LootTables.VILLAGE_TEMPLE, LootTables.VILLAGE_TOOLSMITH, LootTables.VILLAGE_WEAPONSMITH });
			break;
		case "sky":
			table = GenUtil.randLootTable(new LootTables[] { LootTables.STRONGHOLD_LIBRARY, LootTables.SHIPWRECK_SUPPLY, LootTables.SHIPWRECK_MAP });
			break;
		default: break;
		}
		
		Main.SpawnChest(world, x, y, z, table);
	}
	
	public static void PlaceLootBarrel(StructureData structure, World world, int x, int y, int z)
	{
		LootTables table = GenUtil.randLootTable(new LootTables[] 
		{
			LootTables.ABANDONED_MINESHAFT, LootTables.VILLAGE_ARMORER, LootTables.VILLAGE_BUTCHER,
			LootTables.VILLAGE_CARTOGRAPHER, LootTables.VILLAGE_FISHER, LootTables.VILLAGE_FLETCHER, 
			LootTables.VILLAGE_MASON, LootTables.VILLAGE_SHEPHERD, LootTables.VILLAGE_TANNERY,
			LootTables.VILLAGE_TEMPLE, LootTables.VILLAGE_TOOLSMITH, LootTables.VILLAGE_WEAPONSMITH
		});
		
		Main.SpawnBarrel(world, x, y, z, table);
	}
}
