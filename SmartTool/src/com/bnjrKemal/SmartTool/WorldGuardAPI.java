package com.bnjrKemal.SmartTool;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class WorldGuardAPI {

	private static SmartToolMain main;

	public WorldGuardAPI(SmartToolMain main) {
		WorldGuardAPI.main = main;
	}

	public static WorldGuardPlugin getWorldGuardPlugin() {
		Plugin plugin = main.getServer().getPluginManager().getPlugin("WorldGuard");
		if(plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}
	
	public static boolean playerIsInRegion(Player player, Location loc) {
		RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
		if(!query.testState(BukkitAdapter.adapt(loc), WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD)) {
			if(!player.isOp()) return false;
		}
		return true;
	}	
}
