package builder;


import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import scriptRepräsentation.Connection;
import scriptRepräsentation.DScriptText;
import scriptRepräsentation.Glyph;
import scriptRepräsentation.Wordstart;

public class DScriptBuilder {
	public  SVGDocument buildDScript(DScriptText text) {

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		svgRoot.setAttributeNS(null, "width",  text.width+"");
		svgRoot.setAttributeNS(null, "height", text.height+"");

		for(Wordstart word:text.words) {
			buildWord(doc,word);
		}


		return (SVGDocument) doc;
	}

	private void buildWord(Document doc, Connection connection) {
		if(connection.end==null) {
			return;
		}
		
		//Build Transformations
		
		//Build Glyph
		Glyph glyph = connection.end;
		
		for(Connection con:glyph.connections) {
			buildWord(doc, con);
		}
		
		
	}
}
