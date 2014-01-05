package com.TeamNovus.AutoMessage.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.TeamNovus.AutoMessage.Permission;
import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Commands.Common.BaseCommand;
import com.TeamNovus.AutoMessage.Commands.Common.CommandManager;
import com.TeamNovus.AutoMessage.Util.StringUtil;

public class DefaultCommands {

	@BaseCommand(aliases = { "help", "?" }, desc = "Просмотр всех команд и их предназначения.", usage = "[Страница]", permission = Permission.NONE, min = 0, max = 1, hidden = true)
	public void helpCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// help [Page]
		int maxLines = 6;
		
		if(args.length != 0) {
			if(!(StringUtil.isInteger(args[0])) || Math.abs(Integer.valueOf(args[0])) * maxLines - maxLines >= CommandManager.getCommands().size()) {
				sender.sendMessage(ChatColor.RED + "Указанная страница не найдена.");
				return;
			}
		}
		
		int page = args.length == 0 ? 1 : Math.abs(Integer.valueOf(args[0]));
		int total = 0;
		sender.sendMessage(CommandManager.getExtra() + "__________________.[ " + CommandManager.getHighlight() + AutoMessage.getPlugin().getName() + CommandManager.getExtra() + " ].__________________");

		sender.sendMessage(ChatColor.GRAY + "Требуется: < > Опционально: [ ]");
		for (int i = maxLines * page - maxLines; i < CommandManager.getCommands().size() && total < maxLines - 1; i++) {
			BaseCommand command = CommandManager.getCommands().get(i);
			if(!(command.hidden()) && Permission.has(command.permission(), sender)) {
				sender.sendMessage(CommandManager.getExtra() + " - " + CommandManager.getDark() + "/" + commandLabel + " " + command.aliases()[0] + (!(command.usage().equals("")) ? " " + command.usage() : "") + ": " + CommandManager.getLight() + command.desc());
				total++;
			}
		}
		sender.sendMessage(CommandManager.getLight() + "Для помощи введите: " + CommandManager.getHighlight() + "/" + commandLabel + " help [Страница]");
		sender.sendMessage(CommandManager.getExtra() + "---------------------------------------------------");
	}
	
}
