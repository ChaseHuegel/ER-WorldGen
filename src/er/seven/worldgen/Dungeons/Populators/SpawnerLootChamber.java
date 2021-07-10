package er.seven.worldgen.Dungeons.Populators;

import java.util.List;

import org.bukkit.Material;

import Util.BlockDef;
import Util.BlockUtil;
import er.seven.worldgen.Dungeons.DungeonBlockPopulator;
import er.seven.worldgen.Dungeons.Dungeons;

public class SpawnerLootChamber extends DungeonBlockPopulator
{
	@Override
	public void Populate(List<BlockDef> blocks, int x, int y, int z, int size)
	{		
		blocks.add( new BlockDef(x + size/2-1, y + 6, z + size/2, BlockUtil.randMaterial(Dungeons.ceilingMaterial)) );
		blocks.add( new BlockDef(x + size/2, y + 6, z + size/2, BlockUtil.randMaterial(Dungeons.ceilingMaterial)) );
		blocks.add( new BlockDef(x + size/2, y + 5, z + size/2, Material.CHAIN) );
		blocks.add( new BlockDef(x + size/2, y + 4, z + size/2, Material.CHAIN) );
		blocks.add( new BlockDef(x + size/2, y + 3, z + size/2, Material.SPAWNER) );
		
		blocks.add( new BlockDef(x + size/2, y + 1, z + size/2, Material.CHEST) );
	}
}
