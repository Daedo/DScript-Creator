package files;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DSVGParser {
	public static Ligature parseDSVFile(String SVGURI,String XMLURI) {
		if(SVGURI==null || XMLURI==null) {
			return null;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//Speed Workarounds: http://jdevel.wordpress.com/2011/03/28/java-documentbuilder-xml-parsing-is-very-slow/
			factory.setNamespaceAware(false);
			factory.setValidating(true);																//I Want to keep validation, but you might want to set it to false to increase the speed
			factory.setFeature("http://xml.org/sax/features/namespaces", false);
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);		
			DocumentBuilder builder = factory.newDocumentBuilder();
			File DSVGFile = new File(XMLURI);
			Document DSVGDocument = builder.parse(DSVGFile);

			Element rootElement = DSVGDocument.getDocumentElement(); 
			String rootName = rootElement.getNodeName();
			boolean isValidSVGDocument = rootName.equals("points");
			if(isValidSVGDocument) {
				Ligature lig = new Ligature(SVGURI);

				NodeList pointList = rootElement.getChildNodes();
				for(int i=0;i<pointList.getLength();i++) {
					Node currentPoint = pointList.item(i);
					if(currentPoint.getNodeName().equals("point")) {
						ConnectionPoint con = generatePoint(currentPoint);
						if(con!=null) {
							lig.addConnectionPoint(con);
						}
					}
				}
				return lig;
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void printDocument(Document doc) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			// Output to console for testing
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		} catch (TransformerFactoryConfigurationError
				| TransformerException e) {
			e.printStackTrace();
		}

	}

	private static ConnectionPoint generatePoint(Node infoNode) {
		NamedNodeMap attrib = infoNode.getAttributes();

		Node idNode = attrib.getNamedItem("id");
		Node xNode = attrib.getNamedItem("x");
		Node yNode = attrib.getNamedItem("y");
		Node inNode = attrib.getNamedItem("in");
		Node outNode = attrib.getNamedItem("out");

		String idStr = "1";
		if(idNode!=null) {
			idStr = idNode.getNodeValue();
		}
		
		String xStr 	= "0";
		if(xNode!=null) {
			xStr = xNode.getNodeValue();
		}

		String yStr 	= "0";
		if(yNode!=null) {
			yStr = yNode.getNodeValue();
		}

		String inStr	= "false";
		if(inNode!=null) {
			inStr = inNode.getNodeValue();
		}

		String outStr	="false";
		if(outNode!=null) {
			outStr = outNode.getNodeValue();
		}

		try {
			int id		= Integer.parseInt(idStr);
			int x 		= Integer.parseInt(xStr);
			int y 		= Integer.parseInt(yStr);
			boolean in	= Boolean.parseBoolean(inStr);
			boolean out = Boolean.parseBoolean(outStr);
			return new ConnectionPoint(id,x, y, in, out);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

}
