package ligatures;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

import files.BuildingException;

public class LigatureLookup {
	
	public static Ligature lookupLigature(String ligature) throws BuildingException {
		try (FileInputStream fStream = new FileInputStream("Ligatures/Ligatures.properties");
				BufferedInputStream stream = new BufferedInputStream(fStream);){
			Properties properties = new Properties();
			properties.load(stream);
			stream.close();
			fStream.close();

			String upKey = ligature.toUpperCase();
			if(properties.containsKey(upKey+"_POINTS")&&properties.containsKey(upKey)) {
				
				String picturePath = "Ligatures\\"+properties.getProperty(upKey);
				String dataPath	= "Ligatures\\"+properties.getProperty(upKey+"_POINTS");
				Ligature out = new Ligature(ligature,picturePath,dataPath);
				return out;
			}
		} catch (Exception e) {
			throw new BuildingException("Exception while reading Ligature.properties",e);
		}
		throw new BuildingException("Could not find Ligature: "+ligature);
	}
}
