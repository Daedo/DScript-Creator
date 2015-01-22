package textparser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GlyphDisplayer {
	public static String getDSVGFile(String Ligature) {
		try (FileInputStream fStream = new FileInputStream("Ligatures/Ligatures.properties");
				BufferedInputStream stream = new BufferedInputStream(fStream);){
			Properties properties = new Properties();
			properties.load(stream);
			stream.close();
			fStream.close();
			String ligature = properties.getProperty(Ligature.toUpperCase(),"");
			return ligature;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
