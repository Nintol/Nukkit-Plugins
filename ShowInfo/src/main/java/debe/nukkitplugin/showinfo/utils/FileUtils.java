package debe.nukkitplugin.showinfo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import cn.nukkit.Server;
import cn.nukkit.permission.Permission;
import debe.nukkitplugin.showinfo.ShowInfo;
import debe.nukkitplugin.showinfo.command.ShowInfoCommand;
import debe.nukkitplugin.showinfo.command.subcommand.SubCommandData;

public class FileUtils{
	public static void mkdirs(){
		new File(ShowInfo.getInstance().getDataFolder() + "/lang").mkdirs();
	}

	public static String getResource(String fileName){
		ShowInfo plugin = ShowInfo.getInstance();
		File file = new File(plugin.getDataFolder() + "/" + fileName);
		plugin.saveResource("defaults/" + fileName, fileName, false);
		return FileUtils.loadFile(file);
	}

	public static void loadSetting(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			JSONObject jsonSetting = (JSONObject) new JSONParser().parse(FileUtils.getResource("Setting.json"));
			Translation.load((String) jsonSetting.get("Language"));
			plugin.setEnable((boolean) jsonSetting.get("Enable"));
			plugin.setPeriod((int) ((long) jsonSetting.get("PushLevel")));
			plugin.setPeriod((int) ((long) jsonSetting.get("Period")));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveSetting(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			JSONObject jsonSetting = new JSONObject(){
				{
					put("Language", Translation.getLang());
					put("Enable", plugin.getEnable());
					put("PushLevel", plugin.getPushLevel());
					put("Period", plugin.getPeriod());
				}
			};
			FileUtils.saveFile(new File(plugin.getDataFolder(), "Setting.json"), jsonSetting);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void loadPermissions(){
		try{
			JSONObject jsonPermissions = (JSONObject) new JSONParser().parse(FileUtils.getResource("Permissions.json"));
			ShowInfo.getInstance().getDescription().getPermissions().stream().filter(permission->jsonPermissions.containsKey(permission.getName())).forEach(permission->permission.setDefault(Permission.getByName(String.valueOf(jsonPermissions.get(permission.getName())))));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void loadCommandSetting(){
		try{
			JSONObject jsonCommandSetting = (JSONObject) new JSONParser().parse(FileUtils.getResource("command/CommandSetting.json"));
			String command = (String) jsonCommandSetting.get("Command");
			JSONArray jsonAliases = (JSONArray) jsonCommandSetting.get("Aliases");
			String[] aliases = new String[jsonAliases.size()];
			for(int i = 0; i < jsonAliases.size(); i++){
				aliases[i] = ((String) jsonAliases.get(i)).toLowerCase();
			}
			JSONObject jsonSubCommandSetting = (JSONObject) new JSONParser().parse(FileUtils.getResource("command/SubCommandSetting.json"));
			HashMap<String, SubCommandData> subCommands = new HashMap<String, SubCommandData>();
			new ArrayList<String>(){
				{
					add("on");
					add("off");
					add("enable");
					add("disable");
					add("push");
					add("period");
					add("reload");
					add("save");
					add("reset");
				}
			}.forEach(subCommandName->{
				JSONObject jsonSubCommandData = (JSONObject) jsonSubCommandSetting.get(subCommandName);
				String subCommand = (String) jsonSubCommandData.get("Command");
				JSONArray jsonSubCommandAliases = (JSONArray) jsonSubCommandData.get("Aliases");
				ArrayList<String> subCommandAliases = new ArrayList<String>();
				for(int i = 0; i < jsonSubCommandAliases.size(); i++){
					subCommandAliases.add(((String) jsonSubCommandAliases.get(i)).toLowerCase());
				}
				subCommands.put(subCommandName, new SubCommandData(subCommand, subCommandAliases));;
			});
			Server.getInstance().getCommandMap().register(command, new ShowInfoCommand(command, aliases, subCommands));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void loadIgnoreTipPlayers(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			ArrayList<String> ignoreTipPlayers = new ArrayList<String>();
			for(Object nametagViewer : (JSONArray) new JSONParser().parse(FileUtils.getResource("Data_IgnoreTipPlayers.json"))){
				ignoreTipPlayers.add((String) nametagViewer);
			}
			plugin.setIgnoreTipPlayers(ignoreTipPlayers);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void saveIgnoreTipPlayers(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			JSONArray jsonData = new JSONArray();
			plugin.getIgnoreTipPlayers().forEach(jsonData::add);
			FileUtils.saveFile(new File(plugin.getDataFolder() + "/Data_IgnoreTipPlayers.json"), jsonData);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void loadFormat(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			plugin.setFormat(FileUtils.loadFile(new File(plugin.getDataFolder() + "/Format.txt")));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void saveFormat(){
		try{
			ShowInfo plugin = ShowInfo.getInstance();
			FileUtils.saveFile(new File(plugin.getDataFolder() + "/Format.txt"), plugin.getFormat());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String loadFile(File file){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			String temp;
			StringBuilder stringBuilder = new StringBuilder();
			while((temp = reader.readLine()) != null){
				stringBuilder.append(temp).append("\n");
			}
			reader.close();
			return stringBuilder.toString();
		}catch(Exception e){
			return "";
		}
	}

	public static boolean saveFile(File file, JSONObject content){
		return FileUtils.saveFile(file, new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content.toJSONString())));
	}

	public static boolean saveFile(File file, JSONArray content){
		return FileUtils.saveFile(file, new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(content.toJSONString())));
	}

	public static boolean saveFile(File file, String content){
		try{
			if(file.exists() || file.createNewFile()){
				InputStream contentStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
				FileOutputStream saveStream = new FileOutputStream(file);
				int length;
				byte[] buffer = new byte[1024];
				while((length = contentStream.read(buffer)) != -1){
					saveStream.write(buffer, 0, length);
				}
				saveStream.close();
				contentStream.close();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static LinkedHashMap<String, String> parseProperties(String content, LinkedHashMap<String, String> defaultMap){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(defaultMap);
		String[] block;
		for(String line : content.split("\n")){
			if(Pattern.compile("[^#][a-zA-Z0-9\\-_\\.]*+=+[^\\r\\n]*").matcher(line).matches()){
				block = line.split("=", -1);
				map.put(block[0], block[1].trim());
			}
		}
		return map;
	}
}
