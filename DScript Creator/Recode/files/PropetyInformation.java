package files;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

public class PropetyInformation {
	public static String getSVGPath(String ligature) {
		try{
			return getFilenameFromProperties(ligature);
		} catch (BuildingException e) {
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}

	public static String getXMLPath(String ligature) {
		try {
			return getFilenameFromProperties(ligature+"_Points");
		} catch (BuildingException e) {
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		return "";
	}

	private static String getFilenameFromProperties(String key) throws BuildingException {
		try (FileInputStream fStream = new FileInputStream("Ligatures/Ligatures.properties");
				BufferedInputStream stream = new BufferedInputStream(fStream);){
			Properties properties = new Properties();
			properties.load(stream);
			stream.close();
			fStream.close();

			String upKey = key.toUpperCase();
			if(properties.containsKey(upKey)) {
				String ligature = "Ligatures\\"+properties.getProperty(upKey);
				return ligature;
			}
		} catch (IOException e) {
			throw new BuildingException("Exception while reading Ligature.properties");
		}

		throw new BuildingException("Could not find Key: "+key);
	}

	public static HashMap<String, String> getFiltertPropertyMap() {
		try (FileInputStream fStream = new FileInputStream("Ligatures/Ligatures.properties");
				BufferedInputStream stream = new BufferedInputStream(fStream);){
			Properties properties = new Properties();
			properties.load(stream);
			stream.close();
			fStream.close();

			HashMap<String, String> map = new HashMap<>();
			Set<Entry<Object, Object>> set = properties.entrySet();

			for(Entry<?,?> e: set) {
				String key = (String) e.getKey();
				String val = (String) e.getValue();
				map.put(key, val);
			}

			Set<String> keys = map.keySet();
			for(String key:keys) {
				if(key.endsWith("_POINTS")) {
					String nKey = key.substring(0, key.length()-"_POINTS".length());
					if(!keys.contains(nKey)) {
						map.remove(key);
					}
				} else {
					if(!keys.contains(key+"_POINTS")) {
						map.remove(key);
					}
				}
			}

			return map;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
