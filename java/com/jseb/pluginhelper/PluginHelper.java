package com.jseb.pluginhelper;

import com.jseb.pluginhelper.commands.PluginCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginHelper extends JavaPlugin {
	public void onEnable() {
		getCommand("plugin").setExecutor(new PluginCommand(this));
	}

	public void onDisable() {
		
	}
}