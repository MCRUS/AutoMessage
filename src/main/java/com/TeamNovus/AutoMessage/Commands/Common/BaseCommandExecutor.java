package com.TeamNovus.AutoMessage.Commands.Common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Permission;

public class BaseCommandExecutor implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(CommandManager.getExtra() + "__________________.[ " + CommandManager.getHighlight() + AutoMessage.getPlugin().getName() + CommandManager.getExtra() + " ].__________________");
			sender.sendMessage(CommandManager.getDark() + "Описание: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getDescription());
			sender.sendMessage(CommandManager.getDark() + "Автор: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getAuthors().get(0));
			sender.sendMessage(CommandManager.getDark() + "Перевод: " + CommandManager.getLight() + "Alex_Bond_UA");
			sender.sendMessage(CommandManager.getDark() + "Версия: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getVersion());
			sender.sendMessage(CommandManager.getDark() + "Вебсайт: " + CommandManager.getLight() + AutoMessage.getPlugin().getDescription().getWebsite());
			sender.sendMessage(CommandManager.getExtra() + "---------------------------------------------------");
			return true;
		}

		if(CommandManager.getCommand(args[0]) == null) {
			sender.sendMessage(CommandManager.getError() + "Указанная команда не найдена!");
			return true;
		}

		BaseCommand command = CommandManager.getCommand(args[0]);
		Object[] commandArgs = ArrayUtils.remove(args, 0);

		if(sender instanceof Player && !(command.player())) {
			sender.sendMessage(CommandManager.getError() + "Данная команда не может быть исполнена от имени игрока!");
			return true;
		}

		if(sender instanceof ConsoleCommandSender && !(command.console())) {
			sender.sendMessage(CommandManager.getError() + "Данная команда не может быть исполнена из консоли!");
			return true;
		}

		if(command.permission() != null && !(command.permission().equals(Permission.NONE)) && !(Permission.has(command.permission(), sender))) {
			sender.sendMessage(CommandManager.getError() + "У Вас недостаточно прав!");
			return true;
		}

		if((commandArgs.length < command.min()) || (commandArgs.length > command.max() && command.max() != -1)) {
			sender.sendMessage(CommandManager.getError() + "Использование: /" + commandLabel + " " + command.aliases()[0] + " " + command.usage());
			return true;
		}

		CommandManager.execute(command, sender, cmd, commandLabel, commandArgs);
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		ArrayList<String> list = new ArrayList<String>();

		for(BaseCommand command : CommandManager.getCommands()) {
			for(String alias : command.aliases()) {
				list.add(alias);
			}
		}

		return list;
	}
}
