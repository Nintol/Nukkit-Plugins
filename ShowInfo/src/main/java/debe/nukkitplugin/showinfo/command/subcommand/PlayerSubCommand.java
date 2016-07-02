package debe.nukkitplugin.showinfo.command.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import debe.nukkitplugin.showinfo.command.ShowInfoCommand;
import debe.nukkitplugin.showinfo.utils.Translation;

public abstract class PlayerSubCommand extends SubCommand{
	public PlayerSubCommand(){}

	public PlayerSubCommand(ShowInfoCommand mainCommand, SubCommandData data, String permission){
		super(mainCommand, data, permission);
	}

	public PlayerSubCommand(ShowInfoCommand mainCommand, SubCommandData data, String permission, String usage, int needArgCount){
		super(mainCommand, data, permission, usage, needArgCount);
	}

	public void run(CommandSender sender, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(Translation.failedTranslate("commands.generic.ingame"));
		}else{
			super.run(sender, args);
		}
	}

	public void execute(CommandSender sender, String[] args){
		this.execute((Player) sender, args);
	}

	public abstract void execute(Player player, String[] args);
}
