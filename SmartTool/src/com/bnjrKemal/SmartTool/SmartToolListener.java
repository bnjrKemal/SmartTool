package com.bnjrKemal.SmartTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SmartToolListener implements Listener {
	
	@EventHandler
    public void onClick(PlayerInteractEvent e) {
    	
    	if(e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        
        if(!e.getPlayer().isSneaking()) return;
        
        if(!Config.hasTool(e.getPlayer())) return;
    	
        e.setCancelled(true);
        
    	if(e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
    		if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
    		if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;
    		String locStr = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
    		Block block = e.getPlayer().getWorld().getBlockAt(Config.fromStringToLocation(locStr));
            if(!block.getType().equals(Material.CHEST)) {
                e.getPlayer().sendMessage(Config.noLinked());
                ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
                meta.setLocalizedName(null);
                e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
                return;
            }
    		Inventory chest = ((InventoryHolder) block.getState()).getInventory();
    		e.getPlayer().openInventory(chest);
    		return;
    	}

        if(WorldGuardAPI.getWorldGuardPlugin() != null) {
        	Location loc = null;
        	if(e.getClickedBlock() == null) loc = e.getPlayer().getLocation();
        	if(e.getClickedBlock() != null) loc = e.getClickedBlock().getLocation();
        	if(!WorldGuardAPI.playerIsInRegion(e.getPlayer(), loc)) return;
        }
        
        ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
        if(meta.getLocalizedName().equals(e.getClickedBlock().getLocation().toString())) {
            e.getPlayer().sendMessage(Config.sameLinked());
        	return;
        }
        meta.setLocalizedName(e.getClickedBlock().getLocation().toString());
        e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);

        e.getPlayer().sendMessage(Config.save());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
    	
    	if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
    	
    	if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Config.getNameOfTool())) return;
        
    	if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;

        String locStr = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
        Block block = e.getBlock().getWorld().getBlockAt(Config.fromStringToLocation(locStr));
        if(block.getLocation().equals(e.getBlock().getLocation())) {
            e.getPlayer().sendMessage(Config.removedLinked());
            ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            meta.setLocalizedName(null);
            e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            return;
        }
        if(!block.getType().equals(Material.CHEST)) {
            e.getPlayer().sendMessage(Config.noLinked());
            ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            meta.setLocalizedName(null);
            e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            return;
        }
        Inventory chest = ((InventoryHolder) block.getState()).getInventory();
        
        List<ItemStack> itemStacks = new ArrayList<>();
        Collection<ItemStack> drops = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand());
        itemStacks.addAll(drops);
        Map<Integer, ItemStack> maps = ((Inventory) chest).addItem(itemStacks.toArray(new ItemStack[0]));
        e.setDropItems(false);
        if(!maps.isEmpty()) {
        	e.getPlayer().sendMessage(Config.yourChestIsFully());
        	for(ItemStack map : maps.values()) e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), map);
        }
    }
    
    @EventHandler
    public void onDeath(EntityDeathEvent e) {
    
    	Entity victim = (Entity) e.getEntity();
    	if(victim == null) return;
    	
    	Player attacker = (Player) e.getEntity().getKiller();
    	if(attacker == null) return;
    	
    	if(!attacker.getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;
        String locStr = attacker.getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
        Block block = attacker.getWorld().getBlockAt(Config.fromStringToLocation(locStr));
        if(!block.getType().equals(Material.CHEST)) {
        	attacker.sendMessage(Config.noLinked());
            ItemMeta meta = attacker.getInventory().getItemInMainHand().getItemMeta();
            meta.setLocalizedName(null);
            attacker.getInventory().getItemInMainHand().setItemMeta(meta);
            return;
        }

        Inventory chest = ((InventoryHolder) block.getState()).getInventory();
        
        List<ItemStack> itemStacks = new ArrayList<>();
        for(ItemStack item : e.getDrops()) {
        	itemStacks.add(item);
        }
        Map<Integer, ItemStack> map = ((Inventory) chest).addItem(itemStacks.toArray(new ItemStack[0]));
        e.getDrops().clear();
        if(!map.isEmpty()) {
        	attacker.sendMessage(Config.yourChestIsFully());
        	for(ItemStack drops : map.values()) {
        		victim.getWorld().dropItemNaturally(victim.getLocation(), drops);
        	}
        }
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
    	if(!(e.getEntity() instanceof Sheep)) return;
    	Sheep sheep = (Sheep) e.getEntity();
    	if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;
    	
        String locStr = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
        Block block = e.getPlayer().getWorld().getBlockAt(Config.fromStringToLocation(locStr));
        if(!block.getType().equals(Material.CHEST)) {
        	e.getPlayer().sendMessage(Config.noLinked());
            ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            meta.setLocalizedName(null);
            e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            return;
        }
        
        Inventory chest = ((InventoryHolder) block.getState()).getInventory();
        
        e.setCancelled(true);
        sheep.setSheared(true);
        
       	String color = sheep.getColor().name();
       	
       	String material = (color + "_" + "WOOL").toUpperCase();
       	ItemStack sheepDrop = new ItemStack(Material.valueOf(material), (int) (Math.random()*3 + 1));
        
		Map<Integer, ItemStack> map = ((Inventory) chest).addItem(sheepDrop);
        if(!map.isEmpty()) {
        	e.getPlayer().sendMessage(Config.yourChestIsFully());
        	for(ItemStack drops : map.values()) {
        		e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), drops);
        	}
        }
    }
    
    @EventHandler
    public void onFish(PlayerFishEvent e) {
    	if(!e.getState().equals(State.CAUGHT_FISH) && !e.getState().equals(State.CAUGHT_ENTITY)) return;
    	
    	if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLocalizedName()) return;

        String locStr = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
        Block block = e.getPlayer().getWorld().getBlockAt(Config.fromStringToLocation(locStr));
        if(!block.getType().equals(Material.CHEST)) {
        	e.getPlayer().sendMessage(Config.noLinked());
            ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            meta.setLocalizedName(null);
            e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
            return;
        }

        Item item = (Item) e.getCaught();
        
        if(item == null) return;

        List<ItemStack> itemStacks = new ArrayList<>();
        
        itemStacks.add((ItemStack) item.getItemStack());

        Inventory chest = ((InventoryHolder) block.getState()).getInventory();
        
        Map<Integer, ItemStack> map = ((Inventory) chest).addItem(itemStacks.toArray(new ItemStack[0]));

        e.getCaught().remove();
        
        if(!map.isEmpty()) {
        	e.getPlayer().sendMessage(Config.yourChestIsFully());
        	for(ItemStack drops : map.values())  e.getCaught().getWorld().dropItemNaturally(e.getCaught().getLocation(), drops);
        }
    }
}
