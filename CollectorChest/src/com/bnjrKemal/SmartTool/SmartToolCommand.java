package com.bnjrKemal.SmartTool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SmartToolCommand implements CommandExecutor {
	
	private SmartToolMain main;
	
	public SmartToolCommand(SmartToolMain main) {
		this.main = main;
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length != 1){
            for(String str : Config.usageCmd()){
                sender.sendMessage(str);
            }
            return true;
        }

        switch (args[0]){
	        case "reload":
	        	if(sender instanceof Player) {
		        	if(!((Player) sender).isOp()) {
		        		sender.sendMessage(Config.youAreNotOp());
		        		return true;
		        	}
	        	}
	            main.reloadConfig();
	            sender.sendMessage(Config.reloadedConfig());
	            break;
	        case "give":
	        	if(!(sender instanceof Player)) {
	        		sender.sendMessage(Config.onlyPlayerUse());
	        		return true;	
	        	}
	        	if(!((Player) sender).isOp()) {
	        		sender.sendMessage(Config.youAreNotOp());
	        		return true;
	        	}
	        	for(ItemStack list : getItemList()) {
	        		ItemStack is = list;
	        		ItemMeta isMeta = is.getItemMeta();
		        	isMeta.setDisplayName(Config.getNameOfTool());
		        	is.setItemMeta(isMeta);
		            ((Player) sender).getInventory().addItem(is);
	        	}
	            sender.sendMessage(Config.giveTool());
	            break;
            default:
                for(String str : Config.usageCmd()){
                    sender.sendMessage(str);
                }
                return true;
        }
        return true;
    }
    
    public List<ItemStack> getItemList(){
    	List<ItemStack> list = new ArrayList<>();
    	for(String material : main.getConfig().getStringList("items")) {
    		list.add(new ItemStack(Material.matchMaterial(material)));
    	}
    	return list;
    }
    
}
