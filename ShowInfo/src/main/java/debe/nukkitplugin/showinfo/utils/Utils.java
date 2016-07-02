package debe.nukkitplugin.showinfo.utils;

public class Utils{
	public static int toInt(String content){
		try{
			return (int) Double.parseDouble(content);
		}catch(Exception e){}
		return 0;
	}

	public static boolean toBoolean(String content){
		switch(content.toLowerCase().trim()){
			case "on":
			case "true":
			case "yes":
				return true;
			case "off":
			case "false":
			case "no":
				return false;
		}
		return false;
	}
}
