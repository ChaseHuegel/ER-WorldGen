package er.seven.worldgen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Biome;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.loot.LootTables;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.ImmutableSet;

import Structures.StructureListener;
import Structures.StructureUtil;
import Util.FastNoise;
import Util.GenUtil;
import Util.FastNoise.NoiseType;
import er.seven.worldgen.Biomes.*;
import er.seven.worldgen.Caves.*;
import er.seven.worldgen.Dungeons.Dungeons;
import net.md_5.bungee.api.ChatColor;
import nl.rutgerkok.doughworldgenerator.ImporterForCustomized;
import nl.rutgerkok.doughworldgenerator.PluginConfig;
import nl.rutgerkok.doughworldgenerator.chunkgen.ChunkGeneratorOverworld;
import nl.rutgerkok.doughworldgenerator.chunkgen.OverworldGenSettings;
import nl.rutgerkok.worldgeneratorapi.*;
import nl.rutgerkok.worldgeneratorapi.decoration.DecorationType;

public class Main extends JavaPlugin
{
	public final JavaPlugin plugin = this;
	private static Main instance;
	public static Main Instance() { return instance; }
	  
	private PluginConfig pluginConfig;
    static YamlConfiguration mainConfig = new YamlConfiguration();
    static boolean savingMainYaml = false;
    static boolean queueSavingMainYaml = false;
    
    private List<Location> entitySpawnLocations = new ArrayList<Location>();
    private List<EntityType> entitySpawnTypes = new ArrayList<EntityType>();
    
    private List<Location> chestSpawnLocations = new ArrayList<Location>();
    private List<LootTables> chestSpawnTables = new ArrayList<LootTables>();
    
    private List<Location> barrelSpawnLocations = new ArrayList<Location>();
    private List<LootTables> barrelSpawnTables = new ArrayList<LootTables>();
    
    private List<Location> mobSpawnerLocations = new ArrayList<Location>();
    private List<EntityType> mobSpawnerEntities = new ArrayList<EntityType>();
    
	private FastNoise secondaryBiomeNoise;
	public Main()
	{		
		secondaryBiomeNoise = new FastNoise();
		secondaryBiomeNoise.SetNoiseType(NoiseType.Cellular);
		secondaryBiomeNoise.SetFrequency(0.002f);
	}
	
	public static FastNoise GetAltBiomeNoise()
	{
		return Main.Instance().secondaryBiomeNoise;
	}

    public static void SpawnEntity(World world, int x, int y, int z, EntityType entity)
    {
    	SpawnEntity( new Location(world, x, y, z), entity );
    }
    
    public static void SpawnEntity(Location location, EntityType entity)
    {
    	Main.Instance().entitySpawnLocations.add(location);
    	Main.Instance().entitySpawnTypes.add(entity);
    }
    
    public static void SpawnChest(World world, int x, int y, int z, LootTables lootTable)
    {
    	SpawnChest( new Location(world, x, y, z), lootTable );
    }
    
    public static void SpawnChest(Location location, LootTables lootTable)
    {
    	Main.Instance().chestSpawnLocations.add(location);
    	Main.Instance().chestSpawnTables.add(lootTable);
    }
    
    public static void SpawnBarrel(World world, int x, int y, int z, LootTables lootTable)
    {
    	SpawnBarrel( new Location(world, x, y, z), lootTable );
    }
    
    public static void SpawnBarrel(Location location, LootTables lootTable)
    {
    	Main.Instance().barrelSpawnLocations.add(location);
    	Main.Instance().barrelSpawnTables.add(lootTable);
    }
    
    public static void SetMobSpawner(World world, int x, int y, int z, EntityType mob)
    {
    	Main.Instance().mobSpawnerLocations.add(new Location(world, x, y, z));
    	Main.Instance().mobSpawnerEntities.add(mob);
    }
    
    public CaveHandler[] caveHandlers = new CaveHandler[] 
    		{
    				new IceCaveHandler(),
    				new DesertCaveHandler(),
    				new CalciteCaveHandler(),
    				new ColdCaveHandler()
    		};
    
    public static CaveHandler[] getCaveHandlers()
    {
    	return Main.Instance().caveHandlers;
    }
    
    public ChunkHandler[] chunkHandlers = new ChunkHandler[] 
    		{ 
    				new OldForestHandler(),
    				new WoodedMarchesHandler(),
    				new ForestHandler(),
    				new GoldwoodHandler(),
    				new RiverHandler(),
    				new MarshHandler(),
    				new PlainsHandler(),
    				new MeadowHandler(),
    				new LimeCragsHandler(),
    				new MountainsHandler(),
    				new PineForestHandler(),
    				new TaigaHandler(),
    				new TaigaMarchesHandler(),
    				new FlowerFields(),
    				new OceanHandler(),
    				new LorienForestHandler(),
    				new DesertHandler(),
    				new SavannaHandler(),
    				new SnowyForestHandler(),
    				new SnowyMountainHandler(),
    				new SnowyTundra(),
    				new BurntForestHandler(),
    				new WoodedBadlandsHandler(),
    				new BadlandsHandler(),
    				new ShroomFieldsHandler(),
    				new JungleHandler(),
    				new RedwoodHandler(),
    				new GreatForestHandler(),
    				new ThornFieldsHandler(),
    				new IceSpikesHandler(),
    				new OasisSpringsHandler(),
    				new HighlandHandler(),
    				new SavannaMesaHandler(),
    				new BeachHandler(),
    				new BlackForestHandler(),
    				new CherryBlossomHandler(),
    				new StoneSpiresHandler(),
    				new BorealForestHandler(),
    				new MysticForestHandler(),
    				new ForgottenValeHandler(),
    				new BadlandsPlateauHandler(),
    				new SnowyBorealHandler()
    		};
    
    public static ChunkHandler[] getChunkHandlers()
    {
    	return Main.Instance().chunkHandlers;
    }
    
    public ChunkHandler[] altChunkHandlers = new ChunkHandler[] 
    		{ 
    				new SnowyMarchesHandler(),
    				new AutumnForestHandler(),
    				new MagmaFieldsHandler(),
    				new DarkForestHandler(),
    				new MireHandler(),
    				new BirchForestHandler(),
    				new FlowerForestHandler(),
    				new JungleSwampHandler(),
    				new TaigaFieldsHandler(),
    				new MountainSpringsHandler(),
    				new RollingHillsHandler(),
    				new ClearingHandler(),
    				new ShrublandHandler(),
    				new PlateauHandler(),
    				new SaltFlatsHandler(),
    				new WhiteSandsHandler(),
    				new BorealTundraHandler(),
    				new GravelPitsHandler(),
    				new DirtPlateauHandler(),
    				new MangroveHandler(),
    				new WinterPineHandler(),
    				new SnowyBirchHandler(),
    				new SnowyBorealTundraHandler()
    		};
    
    public static ChunkHandler[] getAltChunkHandlers()
    {
    	return Main.Instance().altChunkHandlers;
    }
    
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) 
    {
    	ChunkGenerator gen =  WorldGeneratorApi.getInstance(this, 1, 0).createCustomGenerator(WorldRef.ofName(worldName), generator ->
        {
        	File file = new File(getDataFolder(), worldName + ".yml");

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            pluginConfig.readSettings(WorldRef.ofName(worldName), config);

            config.getKeys(false).forEach(key -> config.set(key, null));
            pluginConfig.writeSettings(WorldRef.ofName(worldName), config);
            try {
                config.save(file);
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Failed to save configuration of world \"" + worldName + "\"", e);
            }
        	
        	OverworldGenSettings overworldSettings = new OverworldGenSettings(pluginConfig, WorldRef.ofName(worldName));
        	generator.setBaseNoiseGenerator(new ChunkGeneratorOverworld(overworldSettings));
        	
        	generator.getWorldDecorator().setDefaultDecoratorsEnabled(DecorationType.LAKES, false);
        	generator.getWorldDecorator().setDefaultDecoratorsEnabled(DecorationType.VEGETAL_DECORATION, false);
//        	generator.getWorldDecorator().setDefaultBaseDecoratorsEnabled(BaseDecorationType.CARVING_LIQUID, false);
//        	generator.getWorldDecorator().setDefaultBaseDecoratorsEnabled(BaseDecorationType.CARVING_AIR, false);
        	generator.getWorldDecorator().withCustomDecoration(DecorationType.RAW_GENERATION, new CarvingManager( plugin.getServer().getWorld(worldName) ));
        	generator.getWorldDecorator().withCustomDecoration(DecorationType.UNDERGROUND_DECORATION, new GeneratorManager( plugin.getServer().getWorld(worldName) ));
        	generator.getWorldDecorator().withCustomDecoration(DecorationType.VEGETAL_DECORATION, new PopulatorManager( plugin.getServer().getWorld(worldName) ));
        	
        	BiomeGenerator vanillaBiomeGenerator = generator.getBiomeGenerator();
        	generator.setBiomeGenerator(new BiomeGenerator() {

                @Override
                public Biome getZoomedOutBiome(int x, int y, int z) 
                {
                	Biome biome = vanillaBiomeGenerator.getZoomedOutBiome(x, y, z);
                	
                	//	Wider rivers
//                    if ((vanillaBiomeGenerator.getZoomedOutBiome(x + 1, y, z) == Biome.RIVER &&
//                    	vanillaBiomeGenerator.getZoomedOutBiome(x, y, z + 1) == Biome.RIVER)
//                    	||
//                    	(vanillaBiomeGenerator.getZoomedOutBiome(x - 1, y, z) == Biome.RIVER &&
//                    	vanillaBiomeGenerator.getZoomedOutBiome(x, y, z - 1) == Biome.RIVER))
                	if (vanillaBiomeGenerator.getZoomedOutBiome(x + 1, y, z) == Biome.RIVER ||
                    	vanillaBiomeGenerator.getZoomedOutBiome(x, y, z + 1) == Biome.RIVER ||
                    	vanillaBiomeGenerator.getZoomedOutBiome(x - 1, y, z) == Biome.RIVER ||
                    	vanillaBiomeGenerator.getZoomedOutBiome(x, y, z - 1) == Biome.RIVER)
                    {
                        return Biome.RIVER;
                    }
                    
                    return biome;
                }

                @Override
                public ImmutableSet<Biome> getStructureBiomes() {
                    // Need to add this, otherwise structures will not be allowed anywhere
                    //return vanillaBiomeGenerator.getStructureBiomes();
                	
                	// Allows structures (villages, strongholds) in all vanilla overworld biomes
                    return ImmutableSet.copyOf(VANILLA_OVERWORLD_STRUCTURE_BIOMES);
                }
            });
        });
    	
    	return gen;
    }
    
    public void RunScheduler()
    {
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(Main.Instance(), new Runnable() 
		{
			@Override
			public void run()
			{				
				for (int index = 0; index < entitySpawnLocations.size(); index++)
				{
					World world = entitySpawnLocations.get(index).getWorld();
					world.spawnEntity(entitySpawnLocations.get(index), entitySpawnTypes.get(index));
				}
				
				entitySpawnLocations.clear();
				entitySpawnTypes.clear();
				
				for (int index = 0; index < chestSpawnLocations.size(); index++)
				{				
					if ( !(chestSpawnLocations.get(index).getBlock().getState() instanceof Chest) ) chestSpawnLocations.get(index).getBlock().setType(Material.CHEST);
					
					Chest chest = (Chest)chestSpawnLocations.get(index).getBlock().getState();
					chest.setLootTable(chestSpawnTables.get(index).getLootTable());
					chest.update();
				}
				
				chestSpawnLocations.clear();
				chestSpawnTables.clear();
				
				for (int index = 0; index < barrelSpawnLocations.size(); index++)
				{				
					if ( !(barrelSpawnLocations.get(index).getBlock().getState() instanceof Barrel) ) barrelSpawnLocations.get(index).getBlock().setType(Material.BARREL);
					
					Barrel barrel = (Barrel)barrelSpawnLocations.get(index).getBlock().getState();
					barrel.setLootTable(barrelSpawnTables.get(index).getLootTable());
					barrel.update();
				}
				
				barrelSpawnLocations.clear();
				barrelSpawnTables.clear();
				
				for (int index = 0; index < mobSpawnerLocations.size(); index++)
				{				
					if ( !(mobSpawnerLocations.get(index).getBlock().getState() instanceof CreatureSpawner) ) mobSpawnerLocations.get(index).getBlock().setType(Material.SPAWNER);
					
					CreatureSpawner spawner = (CreatureSpawner)mobSpawnerLocations.get(index).getBlock().getState();
					spawner.setSpawnedType(mobSpawnerEntities.get(index));
					spawner.setSpawnRange(10);
					spawner.setMinSpawnDelay(0);
					spawner.setMaxSpawnDelay(20);
					spawner.setRequiredPlayerRange(16);
					spawner.setMaxNearbyEntities(6);
					spawner.setSpawnCount(4);
					spawner.update();
				}
				
				mobSpawnerLocations.clear();
				mobSpawnerEntities.clear();
			}
		}, 1, 1);
    }
    
    @Override
    public void onEnable() 
    {
    	instance = this;
    	
    	mainConfig = loadYaml("plugins/EternalRealms-WorldGen/config.yml");
    	pluginConfig = new PluginConfig(this, WorldGeneratorApi.getInstance(this, 1, 0).getPropertyRegistry());
    	
    	GenUtil.Instance();
    	
        getServer().getPluginManager().registerEvents(new StructureListener(), this);
        
        RunScheduler();
    }
    
    @Override
    public void onDisable() 
    {
        org.bukkit.event.HandlerList.unregisterAll(plugin);
        Bukkit.getScheduler().cancelTasks(plugin);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
    	Player player = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("erworldgen"))
    	{
			if (args[0].equalsIgnoreCase("reload") && sender.isOp())
			{
				sender.sendMessage(ChatColor.GREEN + "Reloading config...");
				mainConfig = loadYaml("plugins/EternalRealms-WorldGen/config.yml");
				sender.sendMessage(ChatColor.GREEN + "Finished reloading config.");
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("info") && sender.isOp())
			{
				sender.sendMessage(ChatColor.YELLOW + "Moisture: " + player.getWorld().getBlockAt(player.getLocation()).getHumidity() );
				sender.sendMessage(ChatColor.YELLOW + "Temperature: " + player.getWorld().getBlockAt(player.getLocation()).getTemperature() );
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("dungeon") && sender.isOp())
			{
				Dungeons.GenerateDungeon(
						player.getWorld(), player.getLocation().getBlockX(),
						player.getLocation().getBlockY(),
						player.getLocation().getBlockZ(),
						7, 4, 4, 4
						);
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("tower") && sender.isOp())
			{
				Dungeons.GenerateDungeon(
						player.getWorld(), player.getLocation().getBlockX(),
						player.getLocation().getBlockY(),
						player.getLocation().getBlockZ(),
						7, 2, 2, 10
						);
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("structure") && sender.isOp())
			{
				if (args[1].equalsIgnoreCase("save") && sender.isOp())
				{
					StructureUtil.SaveStructure(
							args[2],
							StructureUtil.getPlayerP1(player),
							StructureUtil.getPlayerP2(player),
							StructureUtil.getPlayerCenter(player)
						);
					sender.sendMessage(ChatColor.GREEN + "Saved structure: " + args[2]);
				}
				else if (args[1].equalsIgnoreCase("load") && sender.isOp())
				{
					StructureUtil.setPlayerClipboard( player, StructureUtil.LoadStructure(args[2]) );
					sender.sendMessage(ChatColor.GREEN + "Loaded structure: " + args[2]);
				}
				else if (args[1].equalsIgnoreCase("paste") && sender.isOp())
				{
					if (StructureUtil.getPlayerClipboard(player) != null)
					{
						StructureUtil.PasteStructure( StructureUtil.getPlayerClipboard(player), player.getLocation() );
						sender.sendMessage(ChatColor.GREEN + "Pasted structure");
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Clipboard is empty!");
					}
				}
				else if (args[1].equalsIgnoreCase("p1") && sender.isOp())
				{
					StructureUtil.setPlayerP1(player, player.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Position 1 set.");
				}
				else if (args[1].equalsIgnoreCase("p2") && sender.isOp())
				{
					StructureUtil.setPlayerP2(player, player.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Position 2 set.");
				}
				
				return true;
			}
			else if (args[0].equalsIgnoreCase("import")) 
			{
	            String remaining = String.join(" ", Arrays.asList(args).subList(1, args.length));
	            LocalTime time = LocalTime.now();
	            File out = new File(getDataFolder(), "export-" + time.getHour() + "-" + time.getMinute() + ".yml");
	            
	            try 
	            {
	                new ImporterForCustomized(out).convert(remaining);
	            } 
	            catch (IOException e) 
	            {
	                sender.sendMessage(ChatColor.RED + "Error. " + e.getMessage());
	                return true;
	            }
	            
	            sender.sendMessage(ChatColor.GREEN + "Saved settings to " + out);
	            return true;
	        }
		}
			
        return false;
    }
    
    public void saveMainYaml()
    {
        if(savingMainYaml==false)
        {
            savingMainYaml = true;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable()
            {
            	public void run()
	            {
	                saveYaml(mainConfig, "plugins/EternalRealms-WorldGen/config.yml");
	                savingMainYaml = false;
	            }
            });
        }
        else
        {
            if(queueSavingMainYaml==false)
            {
                queueSavingMainYaml = true;
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
                {
                	public void run()
	                {
	                    saveMainYaml();	//Recursion
	                    queueSavingMainYaml = false;
	                }
                },20*5);
            }
        }
    }
  
    public static YamlConfiguration loadYaml(String ymlFile)
    {  
        YamlConfiguration yml = new YamlConfiguration();
        
        try
        {
            yml.load(ymlFile);
        }
        catch(FileNotFoundException e)
        {
            try
            {
            	List<String> worldList = new ArrayList<String>();
            	worldList.add("world");
            	yml.set("worlds", worldList);
                yml.save(ymlFile);	//Create the file if it didn't exist
            }
            catch(Exception e2){e.printStackTrace();}
        }
        catch(Exception e){e.printStackTrace();}
        
        return yml;
    }

    public static boolean saveYaml(YamlConfiguration yamlConfig, String ymlFile)
    {
        try
        {
            yamlConfig.save(ymlFile);
            return true;
        }
        catch(Exception e){e.printStackTrace();}
        
        return false;
    }
}
