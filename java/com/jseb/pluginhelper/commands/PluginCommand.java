package com.jseb.pluginhelper.commands;

import com.jseb.pluginhelper.PluginHelper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;
import java.util.List;

public class PluginCommand implements CommandExecutor {
	PluginHelper mPlugin;

	public PluginCommand(PluginHelper plugin) {
		this.mPlugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args[0].equalsIgnoreCase("enable")) {
			if (args.length != 2) sender.sendMessage("[PH] syntax: " + ChatColor.GREEN + "[/plugin enable <name>]");
			else {
				Plugin plugin = mPlugin.getServer().getPluginManager().getPlugin(args[1]);	
				if (plugin == null) sender.sendMessage("[PH] no plugin named " + args[1]);
				else {
					mPlugin.getServer().getPluginManager().enablePlugin(plugin);
					sender.sendMessage("[PS] plugin enabled...");
				}
			}
		} else if (args[0].equalsIgnoreCase("disable")) {
			if (args.length != 2) sender.sendMessage("[PH] syntax: " + ChatColor.GREEN + "[/plugin disable <name>]");
			else {
				Plugin plugin = mPlugin.getServer().getPluginManager().getPlugin(args[1]);	
				if (plugin == null) sender.sendMessage("[PH] no plugin named " + args[1]);
				else {
					mPlugin.getServer().getPluginManager().disablePlugin(plugin);
					sender.sendMessage("[PS] plugin disabled...");
				}
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (args.length != 2) sender.sendMessage("[PH] syntax: " + ChatColor.GREEN + "[/plugin reload <name>]");
			else {
				Plugin plugin = mPlugin.getServer().getPluginManager().getPlugin(args[1]);	
				if (plugin == null) sender.sendMessage("[PH] no plugin named " + args[1]);
				else {
					mPlugin.getServer().getPluginManager().disablePlugin(plugin);
					mPlugin.getServer().getPluginManager().enablePlugin(plugin);
					sender.sendMessage("[PS] plugin reloaded...");
				}
			}
		} else if (args[0].equalsIgnoreCase("info")) {
			if (args.length != 2) sender.sendMessage("[PH] syntax: " + ChatColor.GREEN + "[/plugin info <name>]");
			else {
				Plugin plugin = mPlugin.getServer().getPluginManager().getPlugin(args[1]);
				if (plugin == null) sender.sendMessage("[PH] no plugin named " + args[1]);
				else {
					PluginDescriptionFile description = plugin.getDescription();
					sender.sendMessage("[PH] info for " + (plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + plugin.getName() + ChatColor.RESET + ": ");
					sender.sendMessage("	name: " + ChatColor.GREEN + description.getName());
					sender.sendMessage("	version: " + ChatColor.GREEN + description.getVersion());
					sender.sendMessage("	enabled: " + ChatColor.GREEN + plugin.isEnabled());
					sender.sendMessage("	authors: " + ChatColor.GREEN + listToString(description.getAuthors()));
					sender.sendMessage("	website: " + ChatColor.GREEN + description.getWebsite());
				}
			}
		} else if (args[0].equalsIgnoreCase("list")) {
			Plugin plugins[] = mPlugin.getServer().getPluginManager().getPlugins();
			int page = 1, end;
			end = plugins.length;

			if (args.length > 1) {
				try {
					page = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) { }

				if (page > Math.ceil(end / 5.0)) page = (int) Math.ceil(end / 5.0);
				else if (page <= 0) page = 1;
			}

			sender.sendMessage("[PH] installed plugins [page " + page + " of " + (int) Math.ceil(end / 5.0) + "]:");

			int j = 0;
			for (int i = ((page - 1) * 5); i < end; i++) {
				if (((i % 5) == 0) && (j++ != 0)) break;

				Plugin plugin = plugins[i];
				PluginDescriptionFile description = plugin.getDescription();
				List<String> authors = description.getAuthors();

				if (authors.size() > 3) {
					int size = authors.size();

					authors = authors.subList(0, 3);
					authors.add("+ " + (size - 3) + " more");
				}

				sender.sendMessage("    " + (i + 1) + ". " + (plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + plugin.getName() + ChatColor.RESET + " v" + description.getVersion() + " by " + (authors.size() != 0 ? listToString(authors) : "unknown"));
			}
		}

		return true;
	}

	public String listToString(List<String> list) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (String string : list) sb.append(((index == 0) ? "[" : "") + string + (++index == list.size() ? "]" : ", "));

		return sb.toString();
	}
}