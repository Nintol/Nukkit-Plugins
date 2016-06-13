package debe.nukkitplugin.itemdisplay.task.touchtask;

import cn.nukkit.Player;
import debe.nukkitplugin.itemdisplay.ItemDisplay;

public abstract class TouchTask{
	protected Player player;

	public TouchTask(Player player){
		this.player = player;
	}

	public Player getPlayer(){
		return this.player;
	}

	public void onRun(){
		ItemDisplay.getInstance().removeTouchTask(this);
	}
}
