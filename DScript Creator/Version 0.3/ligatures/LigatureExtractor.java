package ligatures;

import java.io.File;
import java.io.IOException;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LigatureExtractor {
	public static NodeList extractDrawData(String path) throws ExtractException {
		try {
			//Load File
			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			
			File file = new File(path);
			Document doc = f.createDocument("file:\\"+file.getAbsolutePath());
			//Extract Draw Groups
			NodeList groups = doc.getElementsByTagName("g");
			for(int i=0;i<groups.getLength();i++) {
				Node nd = groups.item(i);
				System.out.println(nd.getNodeName()+ " - "+nd.getNodeType()+" - "+nd.getNodeValue());
			}
			return groups;

		} catch (IOException ex) {
			throw new ExtractException("Error while extracting draw data of \""+path+"\"", ex);
		}
	}
}
