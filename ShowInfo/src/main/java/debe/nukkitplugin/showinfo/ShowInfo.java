package debe.nukkitplugin.showinfo;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.TaskHandler;
import debe.nukkitplugin.showinfo.listener.SaveCommandListener;
import debe.nukkitplugin.showinfo.task.SendInfoTask;
import debe.nukkitplugin.showinfo.utils.FileUtils;

public class ShowInfo extends PluginBase{
	private static ShowInfo instance;
	private TaskHandler sendInfoTask;
	private String format;
	private boolean enable;
	private int pushLevel;
	private int period;
	private List<String> ignoreTipPlayers;

	public static ShowInfo getInstance(){
		return ShowInfo.instance;
	}

	@Override
	public void onLoad(){
		ShowInfo.instance = this;
		this.loadAll();
	}

	@Override
	public void onEnable(){
		if(this.getEnable()){
			this.taskStart();
		}
		this.getServer().getPluginManager().registerEvents(new SaveCommandListener(), this);
	}

	@Override
	public void onDisable(){
		this.taskStop();
		this.saveAll();
	}

	public void loadAll(){
		FileUtils.mkdirs();
		this.saveDefaultData(false);
		FileUtils.loadSetting();
		FileUtils.loadPermissions();
		FileUtils.loadCommandSetting();
		FileUtils.loadIgnoreTipPlayers();
		FileUtils.loadFormat();
	}

	public void saveAll(){
		FileUtils.mkdirs();
		FileUtils.saveSetting();
		FileUtils.saveIgnoreTipPlayers();
		FileUtils.saveFormat();
	}

	public void saveDefaultData(boolean replace){
		new ArrayList<String>(){
			{
				add("Data_IgnoreTipPlayers.json");
				add("Format.txt");
				add("Permissions.json");
				add("Setting.json");
				add("command/CommandSetting.json");
				add("command/SubCommandSetting.json");
				add("lang/eng.ini");
				add("lang/kor.ini");
			}
		}.forEach(fileName->this.saveResource("defaults/" + fileName, fileName, replace));
	}

	public boolean isTaskStop(){
		return (this.sendInfoTask == null || this.sendInfoTask.isCancelled());
	}

	public void taskStart(){
		if(this.isTaskStop() != true){
			this.taskStop();
		}
		this.sendInfoTask = this.getServer().getScheduler().scheduleDelayedRepeatingTask(new SendInfoTask(this), this.period, this.period);
	}

	public void taskStop(){
		if(this.isTaskStop() != true){
			this.sendInfoTask.cancel();
			this.sendInfoTask = null;
		}
	}

	public String getFormat(){
		return this.format;
	}

	public void setFormat(String format){
		this.format = format;
	}

	public boolean getEnable(){
		return this.enable;
	}

	public void setEnable(boolean enable){
		this.enable = enable;
	}

	public int getPushLevel(){
		return this.pushLevel;
	}

	public void setPushLevel(int pushLevel){
		this.pushLevel = pushLevel;
	}

	public int getPeriod(){
		return this.period;
	}

	public void setPeriod(int period){
		this.period = period;
	}

	public List<String> getIgnoreTipPlayers(){
		return ignoreTipPlayers;
	}

	public void setIgnoreTipPlayers(List<String> ignoreTipPlayers){
		this.ignoreTipPlayers = ignoreTipPlayers;
	}
}
