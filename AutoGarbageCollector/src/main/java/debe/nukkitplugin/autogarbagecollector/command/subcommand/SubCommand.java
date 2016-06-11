package debe.nukkitplugin.autogarbagecollector.command.subcommand;

import java.util.Arrays;

import cn.nukkit.command.CommandSender;
import cn.nukkit.permission.Permissible;
import debe.nukkitplugin.autogarbagecollector.command.AutoGarbageCollectorCommand;
import debe.nukkitplugin.autogarbagecollector.utils.Translation;

public abstract class SubCommand{
	protected AutoGarbageCollectorCommand mainCommand;
	protected String name = "";
	protected String permission = "";
	protected String usage = "";
	protected int needArgCount = 0;

	public SubCommand(){}

	public SubCommand(AutoGarbageCollectorCommand mainCommand, String name, String permission){
		this.mainCommand = mainCommand;
		this.name = name;
		this.permission = permission;
	}

	public SubCommand(AutoGarbageCollectorCommand mainCommand, String name, String permission, String usage, int needArgCount){
		this(mainCommand, name, permission);
		this.usage = usage;
		this.needArgCount = needArgCount;
	}

	public void run(CommandSender sender, String[] args){
		if(args.length < this.getNeedArgCount() || Arrays.stream(Arrays.copyOfRange(args, 0, this.getNeedArgCount())).anyMatch(arg->arg.equals(""))){
				sender.sendMessage(Translation.translate("prefix") + " " + Translation.translate("commands.generic.usage", this.getUsage()));
		}else{
			this.execute(sender, args);
		}
	}

	public abstract void execute(CommandSender sender, String[] args);

	public String getName(){
		return this.name;
	}

	public String getPermission(){
		return this.permission;
	}

	public String getUsage(){
		return Translation.translate("commands.generic.usages", new String[]{this.mainCommand.getLabel(), this.getName(), this.usage});
	}

	public int getNeedArgCount(){
		return this.needArgCount;
	}

	public boolean hasPermission(Permissible sender){
		return sender.hasPermission(this.getPermission());
	}
}
