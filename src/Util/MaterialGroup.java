package Util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;

public class MaterialGroup 
{
	public static List<Material> LEAVES = Arrays.asList(
			Material.ACACIA_LEAVES, Material.AZALEA_LEAVES, Material.BIRCH_LEAVES, Material.BIRCH_LEAVES, Material.DARK_OAK_LEAVES,
			Material.FLOWERING_AZALEA_LEAVES, Material.JUNGLE_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES
			);
	public static Material[] LEAVES_ARRAY = LEAVES.toArray(new Material[0]);
	
	
	public static List<Material> LOGS = Arrays.asList(
			Material.ACACIA_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG,
			Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_DARK_OAK_LOG, Material.STRIPPED_JUNGLE_LOG,
			Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.CRIMSON_HYPHAE, Material.WARPED_HYPHAE, 
			Material.STRIPPED_CRIMSON_HYPHAE, Material.STRIPPED_WARPED_HYPHAE
			);
	public static Material[] LOGS_ARRAY = LOGS.toArray(new Material[0]);
	
	
	public static List<Material> STONE = Arrays.asList(
			Tag.BASE_STONE_OVERWORLD.getValues().toArray(new Material[0])
			);
	public static Material[] STONE_ARRAY = STONE.toArray(new Material[0]);
	
	public static List<Material> WATERLOGGABLE = Arrays.asList(
			Material.SEAGRASS, Material.SEA_PICKLE, Material.TALL_SEAGRASS, Material.BIG_DRIPLEAF, Material.BIG_DRIPLEAF_STEM, Material.SMALL_DRIPLEAF,
			Material.POINTED_DRIPSTONE
			);
	public static Material[] WATERLOGGABLE_ARRAY = LOGS.toArray(new Material[0]);

}
