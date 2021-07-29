package com.bnjrKemal.SmartTool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Config {

	private static SmartToolMain main;
	
	public Config(SmartToolMain main) {
		Config.main = main;
		main.getConfig().options().copyDefaults();
		main.saveDefaultConfig();
	}

    public static List<String> usageCmd(){
        List<String> list = new ArrayList<>();
        for(String str : main.getConfig().getStringList("usage")){
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return list;
    }

    public static String onlyPlayerUse() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("only-player-use"));
    }

    public static boolean hasTool(Player player){
		if(player.getInventory().getItemInMainHand().getItemMeta() != null 
				&& player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(getNameOfTool())) {
			return true;
		}
    	return false;
    }
    
    public static String getNameOfTool() {
    	return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("tool-name"));
    }
    
    public static String save(){
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("save-successfully"));
    }

    public static Location fromStringToLocation(String str){
        String locLists = str
                .replace("Location{world=\"", "")
                .replace("\"", "")
                .replace("x=", "")
                .replace("y=", "")
                .replace("z=", "")
                .replace("yaw=", "")
                .replace("pitch=", "")
                .replace("}", "");

        return new Location(
                Bukkit.getWorld(locLists.split(",")[0]),
                Double.parseDouble(locLists.split(",")[1]),
                Double.parseDouble(locLists.split(",")[2]),
                Double.parseDouble(locLists.split(",")[3])
        );
    }

    public static String noLinked() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("no-linked"));
    }

    public static String yourChestIsFully() {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("your-chest-is-fully"));
    }

    public static String reloadedConfig() {
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("reloaded-config"));
	}

	public static String giveTool() {
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("given-tool"));
	}

	public static String removedLinked() {
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("removed-linked"));
	}

	public static String sameLinked() {
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("same-linked"));
	}

	public static String youAreNotOp() {
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("no-permission"));
	}
}
