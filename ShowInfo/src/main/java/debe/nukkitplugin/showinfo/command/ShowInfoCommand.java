package debe.nukkitplugin.showinfo.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import debe.nukkitplugin.showinfo.ShowInfo;
import debe.nukkitplugin.showinfo.command.subcommand.PlayerSubCommand;
import debe.nukkitplugin.showinfo.command.subcommand.SubCommand;
import debe.nukkitplugin.showinfo.command.subcommand.SubCommandData;
import debe.nukkitplugin.showinfo.utils.Translation;
import debe.nukkitplugin.showinfo.utils.Utils;

public class ShowInfoCommand extends Command{
	private LinkedHashMap<String, SubCommand> subCommands = new LinkedHashMap<String, SubCommand>();

	public ShowInfoCommand(String name, String[] aliases, Map<String, SubCommandData> subCommands){
		super(name);
		this.setPermission("showinfo.command.showinfo");
		this.setAliases(aliases);
		this.registerSubCommands(subCommands);
		this.description = this.getUsage();
		this.usageMessage = this.getUsage();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		if(!this.testPermission(sender)){
			return true;
		}else if(args.length == 0){
			sender.sendMessage(Translation.translate("commands.generic.usage", this.getUsage(sender)));
			return true;
		}else{
			SubCommand subCommand = this.getSubCommand(args[0]);
			if(subCommand == null){
				sender.sendMessage(Translation.translate("commands.generic.notFound", this.getUsage(sender)));
			}else if(!subCommand.hasPermission(sender)){
				sender.sendMessage(Translation.translate("commands.generic.permission"));
			}else{
				subCommand.run(sender, Arrays.copyOfRange(args, 1, args.length));
			}
		}
		return true;
	}

	@Override
	public String getUsage(){
		return "/" + this.getLabel() + " <" + String.join(" | ", this.subCommands.values().stream().map(SubCommand::getName).collect(Collectors.toList())) + ">";
	}

	public String getUsage(CommandSender sender){
		return "/" + this.getLabel() + " <" + String.join(" | ", this.subCommands.values().stream().filter(subCommand->subCommand.hasPermission(sender)).map(SubCommand::getName).collect(Collectors.toList())) + ">";
	}

	public void registerSubCommands(Map<String, SubCommandData> subCommands){
		ShowInfo plugin = ShowInfo.getInstance();
		this.registerSubCommand(new PlayerSubCommand(this, subCommands.get("on"), "showinfo.command.showinfo.on"){
			public void execute(Player player, String[] args){
				if(!plugin.getIgnoreTipPlayers().contains((player.getName().toLowerCase()))){
					player.sendMessage(Translation.failedTranslate("commands.on.failed"));
				}else{
					plugin.getIgnoreTipPlayers().remove(player.getName().toLowerCase());
					player.sendMessage(Translation.successedTranslate("commands.on.success"));
				}
			}
		});
		this.registerSubCommand(new PlayerSubCommand(this, subCommands.get("off"), "showinfo.command.showinfo.off"){
			public void execute(Player player, String[] args){
				if(plugin.getIgnoreTipPlayers().contains(player.getName().toLowerCase())){
					player.sendMessage(Translation.failedTranslate("commands.off.failed"));
				}else{
					plugin.getIgnoreTipPlayers().add(player.getName().toLowerCase());
					player.sendMessage(Translation.successedTranslate("commands.off.success"));
				}
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("enable"), "showinfo.command.showinfo.enable"){
			public void execute(CommandSender sender, String[] args){
				if(!plugin.isTaskStop()){
					sender.sendMessage(Translation.failedTranslate("commands.enable.failed"));
				}else{
					plugin.setEnable(true);
					plugin.taskStart();
					sender.sendMessage(Translation.successedTranslate("commands.enable.success"));
				}
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("disable"), "showinfo.command.showinfo.disable"){
			public void execute(CommandSender sender, String[] args){
				if(plugin.isTaskStop()){
					sender.sendMessage(Translation.failedTranslate("commands.disable.failed"));
				}else{
					plugin.setEnable(false);
					plugin.taskStop();
					sender.sendMessage(Translation.successedTranslate("commands.disable.success"));
				}
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("push"), "showinfo.command.showinfo.push", Translation.translate("commands.push.usage"), 1){
			public void execute(CommandSender sender, String[] args){
				if(!Pattern.matches("(^-[0-9]*$)|(^[0-9]+$)", args[0])){
					sender.sendMessage(Translation.failedTranslate("commands.generic.invalidNumber", args[0]));
				}else{
					int pushLevel = Utils.toInt(args[0]);
					plugin.setPushLevel(pushLevel);
					sender.sendMessage(Translation.successedTranslate("commands.push.success", String.valueOf(pushLevel)));
				}
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("period"), "showinfo.command.showinfo.period", Translation.translate("commands.period.usage"), 1){
			public void execute(CommandSender sender, String[] args){
				if(!Pattern.matches("(^[1-9][0-9]*$)|(^[1-9]+$)", args[0])){
					sender.sendMessage(Translation.failedTranslate("commands.generic.invalidNumber", args[0]));
				}else{
					int period = Utils.toInt(args[0]);
					plugin.setPeriod(period);
					if(plugin.getEnable()){
						plugin.taskStart();
					}
					sender.sendMessage(Translation.successedTranslate("commands.period.success", String.valueOf(period)));
				}
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("reload"), "showinfo.command.showinfo.reload"){
			public void execute(CommandSender sender, String[] args){
				plugin.saveDefaultData(false);
				plugin.loadAll();
				sender.sendMessage(Translation.successedTranslate("commands.reload.success"));
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("save"), "showinfo.command.showinfo.save"){
			public void execute(CommandSender sender, String[] args){
				plugin.saveAll();
				sender.sendMessage(Translation.successedTranslate("commands.save.success"));
			}
		});
		this.registerSubCommand(new SubCommand(this, subCommands.get("reset"), "showinfo.command.showinfo.reset"){
			public void execute(CommandSender sender, String[] args){
				plugin.saveDefaultData(true);
				plugin.loadAll();
				if(plugin.getEnable()){
					plugin.taskStart();
				}
				sender.sendMessage(Translation.successedTranslate("commands.reset.success"));
			}
		});
	}

	public void registerSubCommand(SubCommand subCommand){
		this.subCommands.put(subCommand.getName().toLowerCase(), subCommand);
	}

	public SubCommand getSubCommand(String command){
		for(SubCommand subCommand : this.subCommands.values()){
			if(subCommand.getName().equalsIgnoreCase(command)){
				return subCommand;
			}
		}
		for(SubCommand subCommand : this.subCommands.values()){
			if(subCommand.getData().equals(command)){
				return subCommand;
			}
		}
		return null;
	}

	public ArrayList<SubCommand> getSubCommands(){
		return new ArrayList<SubCommand>(this.subCommands.values());
	}
}
