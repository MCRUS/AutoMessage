package com.TeamNovus.AutoMessage.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.TeamNovus.AutoMessage.AutoMessage;
import com.TeamNovus.AutoMessage.Permission;
import com.TeamNovus.AutoMessage.Commands.Common.BaseCommand;
import com.TeamNovus.AutoMessage.Models.Message;
import com.TeamNovus.AutoMessage.Models.MessageList;
import com.TeamNovus.AutoMessage.Models.MessageLists;
import com.TeamNovus.AutoMessage.Util.StringUtil;

public class PluginCommands {

	@BaseCommand(aliases = "test", permission = Permission.NONE, desc = "")
	public void testCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage(String.format("Это %5s тест!", "test"));
	}
	
	@BaseCommand(aliases = "reload", desc = "Перезагружает конфигурацию с диска.", usage = "", permission = Permission.COMMAND_RELOAD)
	public void onReloadCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		AutoMessage.getPlugin().loadConfig();

		sender.sendMessage(ChatColor.GREEN + "Конфигурация была перезагружена!");
	}
	
	@BaseCommand(aliases = "add", desc = "Добавляет список или сообщение в список.", usage = "<Список> [Порядок] [Сообщение]", min = 1, permission = Permission.COMMAND_ADD)
	public void onAddCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 1) {
			if(MessageLists.getExactList(args[0]) == null) {
				MessageLists.setList(args[0], new MessageList());
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Список успешно создан!");
			} else {
				sender.sendMessage(ChatColor.RED + "Список уже существует!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				if(args.length >= 2) {
					if(args.length >= 3 && StringUtil.isInteger(args[1]) ) {
						Message message = new Message(StringUtil.concat(args, 2, args.length));
						
						list.addMessage(Integer.valueOf(args[1]), message);
					} else {
						Message message = new Message(StringUtil.concat(args, 1, args.length));

						list.addMessage(message);
					}

					AutoMessage.getPlugin().saveConfiguration();

					sender.sendMessage(ChatColor.GREEN + "Сообщение добавлено!");
				} else {
					sender.sendMessage(ChatColor.RED + "Пожалуйста, кажите больше парметров!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
			}
		}
	}

	@BaseCommand(aliases = "edit", desc = "Изменяет сообщение.", usage = "<Список> <Порядок> <Сообщение>", min = 3, permission = Permission.COMMAND_EDIT)
	public void onEditCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				Message message = new Message(StringUtil.concat(args, 2, args.length));
				
				if(list.editMessage(Integer.valueOf(args[1]), message)) {
					AutoMessage.getPlugin().saveConfiguration();
					
					sender.sendMessage(ChatColor.GREEN + "Сообщение отредактировано!");
				} else {
					sender.sendMessage(ChatColor.RED + "Указанный номер не найден!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный номер не найден!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "remove", desc = "Удаляет список или сообщение.", usage = "<Список> [Порядок]", min = 1, max = 3, permission = Permission.COMMAND_REMOVE)
	public void onRemoveCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 1) {
			if(MessageLists.getExactList(args[0]) != null) {
				MessageLists.setList(args[0], null);
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Список успешно удален!");
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				if(StringUtil.isInteger(args[1])) {
					if(list.removeMessage(Integer.valueOf(args[1]))) {
						MessageLists.schedule();
						AutoMessage.getPlugin().saveConfiguration();
						
						sender.sendMessage(ChatColor.GREEN + "Сообщение удалено!");
					} else {
						sender.sendMessage(ChatColor.RED + "Указанный номер не найден!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Указанный номер не найден!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный номер не найден!");
			}
		}
	}

	@BaseCommand(aliases = "enabled", desc = "Переключет состояние публикации списка.", usage = "<Списка>", min = 1, max = 1, permission = Permission.COMMAND_ENABLE)
	public void onEnableCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			list.setEnabled(!(list.isEnabled()));
			
			AutoMessage.getPlugin().saveConfiguration();

			sender.sendMessage(ChatColor.GREEN + "Включено: " + ChatColor.YELLOW + list.isEnabled() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "interval", desc = "Указывает время между публикацией сообщений.", usage = "<Список> <Интервал>", min = 2, max = 2, permission = Permission.COMMAND_INTERVAL)
	public void onIntervalCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				list.setInterval(Integer.valueOf(args[1]));
				
				MessageLists.schedule();
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Интвервал: " + ChatColor.YELLOW + Integer.valueOf(args[1]) + ChatColor.GREEN + "!");
			} else {
				sender.sendMessage(ChatColor.RED + "Интервал может быть только числовым!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "expiry", desc = "Указывает срок истечения списка.", usage = "<Список> <Срок>", min = 2, max = 2, permission = Permission.COMMAND_EXPIRY)
	public void onExpiryCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			try {
				if(StringUtil.isInteger(args[1])) {
					if(Integer.valueOf(args[1]).longValue() >= 0) {
						list.setExpiry(System.currentTimeMillis() + Integer.valueOf(args[1]).longValue());
					} else {
						list.setExpiry(Integer.valueOf(-1).longValue());
					}
				} else {
					list.setExpiry(System.currentTimeMillis() + StringUtil.parseTime(args[1]));
				}

				AutoMessage.getPlugin().saveConfiguration();
				
				if(list.getExpiry() != -1) {
					sender.sendMessage(ChatColor.GREEN + "Истекает через " + ChatColor.YELLOW + StringUtil.millisToLongDHMS(list.getExpiry() - System.currentTimeMillis()) + ChatColor.GREEN + "!");
				} else {
					sender.sendMessage(ChatColor.GREEN + "Срок истечения выключен!");
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Неверный вормат. Для выключения используйте -1.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "random", desc = "Переключет режим показа сообщений.", usage = "<Список>", min = 1, max = 1, permission = Permission.COMMAND_RANDOM)
	public void onRandomCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			list.setRandom(!(list.isRandom()));
			
			AutoMessage.getPlugin().saveConfiguration();
			
			sender.sendMessage(ChatColor.GREEN + "Случайно: " + ChatColor.YELLOW + list.isRandom() + ChatColor.GREEN + "!");
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "prefix", desc = "Указывает префикс для сообщений.", usage = "<Список> [Префикс]", min = 1, permission = Permission.COMMAND_PREFIX)
	public void onPrefixCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(args.length == 1) {
				list.setPrefix("");
				
				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Префикс обновлен!");
			} else {
				list.setPrefix(StringUtil.concat(args, 1, args.length) + " ");

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Префикс обновлен!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "suffix", desc = "Указывает суффикс для сообщений.", usage = "<Список> [Суффикс]", min = 1, permission = Permission.COMMAND_SUFFIX)
	public void onSuffixCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(args.length == 1) {
				list.setSuffix("");

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Суффикс обновлен!");
			} else {
				list.setSuffix(" " + StringUtil.concat(args, 1, args.length));

				AutoMessage.getPlugin().saveConfiguration();
				
				sender.sendMessage(ChatColor.GREEN + "Суффикс обновлен!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "broadcast", desc = "Рассылает сообщение.", usage = "<Список> <Порядок>", min = 2, max = 2, permission = Permission.COMMAND_BROADCAST)
	public void onBroadcast(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		MessageList list = MessageLists.getBestList(args[0]);

		if(list != null) {
			if(StringUtil.isInteger(args[1])) {
				int index = Integer.valueOf(args[1]);

				if(list.getMessage(index) != null) {
					list.broadcast(index);
				} else {
					sender.sendMessage(ChatColor.RED + "Указанный номер не найден в списке!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный номер не найден в списке!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
		}
	}

	@BaseCommand(aliases = "list", desc = "Показывает список сообщение в списке.", usage = "[Список]", max = 1, permission = Permission.COMMAND_LIST)
	public void onListCmd(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(args.length == 0) {
			if(MessageLists.getMessageLists().keySet().size() != 0) {
				sender.sendMessage(ChatColor.DARK_RED + "Доступные списки:");
				
				for(String key : MessageLists.getMessageLists().keySet()) {
					sender.sendMessage(ChatColor.GOLD + key);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Нет доступных списков!");
			}
		} else {
			MessageList list = MessageLists.getBestList(args[0]);

			if(list != null) {
				sender.sendMessage(ChatColor.DARK_RED + MessageLists.getBestKey(args[0]));
				
				List<Message> messages = list.getMessages();
				for (int i = 0; i < messages.size(); i++) {
					sender.sendMessage(ChatColor.YELLOW + "" + i + ": " + ChatColor.RESET + ChatColor.translateAlternateColorCodes("&".charAt(0), list.getPrefix() + messages.get(i).getFormat() + list.getSuffix()));
					
					for(int j = 0; j < messages.get(i).getArguments().size(); j++) {
						sender.sendMessage(" Параметр #" + j + ":" + messages.get(i).getArguments().get(j));
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Указанный список не найден!");
			}
		}
	}

}
