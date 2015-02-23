package files;

import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class PointParser {
	public static Vector<ConnectionPoint> parsePoints(String URI) {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			FileReader reader = new FileReader(URI);
			InputSource inputSource = new InputSource(reader);

			PointHandler handler = new PointHandler();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			
			return handler.getPointList();
		} catch (SAXException | IOException e) {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		parsePoints("Ligatures\\Basic\\a1.xml");
	}

}
