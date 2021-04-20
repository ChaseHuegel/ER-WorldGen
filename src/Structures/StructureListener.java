package Structures;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;

import Util.GenUtil;
import net.md_5.bungee.api.ChatColor;

public class StructureListener implements Listener 
{
	@EventHandler
	public void onGrow(StructureGrowEvent event)
	{
		World world = event.getWorld();
		
		switch (event.getSpecies())
		{
		case BIG_TREE: case TREE: GenUtil.GrowTree(new Random(), event.getLocation(), "Oak", world); event.setCancelled(true); break;
		
		case ACACIA: GenUtil.GrowTree(new Random(), event.getLocation(), "Savanna", world); event.setCancelled(true); break;
		
		case BIRCH: case TALL_BIRCH: GenUtil.GrowTree(new Random(), event.getLocation(), "Birch", world); event.setCancelled(true); break;
		
		case BROWN_MUSHROOM: case RED_MUSHROOM: GenUtil.GrowTree(new Random(), event.getLocation(), "Shrooms", world); event.setCancelled(true); break;

		case DARK_OAK: GenUtil.GrowTree(new Random(), event.getLocation(), "DarkOak", world); event.setCancelled(true); break;
		
		case REDWOOD: case TALL_REDWOOD: GenUtil.GrowTree(new Random(), event.getLocation(), "Spruce", world); event.setCancelled(true); break;
			
		case JUNGLE: case JUNGLE_BUSH: case SMALL_JUNGLE: case COCOA_TREE: GenUtil.GrowTree(new Random(), event.getLocation(), "Jungle", world); event.setCancelled(true); break;
			
		default: break;
		}
	}
	
	@EventHandler
    public void onClick(PlayerInteractEvent event)
    {
		Player player = event.getPlayer();
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		Block block = event.getClickedBlock();
		
		if (mainHand.getType() == Material.WOODEN_SHOVEL && player.isOp())
		{
			if (player.isSneaking() == true)
			{				
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
				{
					if (block != null && block.getType() != Material.AIR)
					{
						StructureUtil.setPlayerCenter(player, block.getLocation());
						player.sendMessage(ChatColor.GREEN + "Center set.");
						event.setCancelled(true);
					}
				}
				else if (event.getAction() == Action.LEFT_CLICK_BLOCK)
				{
					if (block != null && block.getType() != Material.AIR)
					{
						StructureUtil.setPlayerCenter(player, block.getLocation());
						player.sendMessage(ChatColor.GREEN + "Center set.");
						event.setCancelled(true);
					}
				}
			}
			else if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if (block != null && block.getType() != Material.AIR)
				{
					StructureUtil.setPlayerP2(player, block.getLocation());
					player.sendMessage(ChatColor.GREEN + "Position 2 set.");
					event.setCancelled(true);
				}
			}
			else if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				if (block != null && block.getType() != Material.AIR)
				{
					StructureUtil.setPlayerP1(player, block.getLocation());
					player.sendMessage(ChatColor.GREEN + "Position 1 set.");
					event.setCancelled(true);
				}
			}
		}
    }
}
