package com.bnjrKemal.SmartTool;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartToolMain extends JavaPlugin {
		
	@Override
	public void onEnable() {
		new WorldGuardAPI(this);
		new Config(this);
		getCommand("smarttool").setExecutor(new SmartToolCommand(this));
        Bukkit.getServer().getPluginManager().registerEvents(new SmartToolListener(), this);
	}
	
}
