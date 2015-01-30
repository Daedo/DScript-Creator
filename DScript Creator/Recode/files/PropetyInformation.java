package files;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropetyInformation {
	public static String getSVGPath(String ligature) {
		return getFilenameFromProperties(ligature);
	}
	
public static String getXMLPath(String ligature) {
		return getFilenameFromProperties(ligature+"_Points");
	}

private static String getFilenameFromProperties(String key) {
	try (FileInputStream fStream = new FileInputStream("Ligatures/Ligatures.properties");
			BufferedInputStream stream = new BufferedInputStream(fStream);){
		Properties properties = new Properties();
		properties.load(stream);
		stream.close();
		fStream.close();
		String ligature = "Ligatures\\"+properties.getProperty(key.toUpperCase(),"");
		return ligature;
	} catch (IOException e) {
		e.printStackTrace();
	}
	return "";
}
}
