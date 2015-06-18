package ligature.dataPoint;

import java.io.FileReader;
import java.util.HashMap;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DataExtractor {
	public static HashMap<Integer, LigatureConnectionPoint> extractData(String path) {
		HashMap<Integer,LigatureConnectionPoint> out = new HashMap<>();
		
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			FileReader reader = new FileReader(path);
			InputSource inputSource = new InputSource(reader);

			DataHandler handler = new DataHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			
			return handler.data;
		} catch (Exception e) {
			return out;
		}
	}
}
