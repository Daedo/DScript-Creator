package ligature.dataPoint;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class DataHandler implements org.xml.sax.ContentHandler{

	public HashMap<Integer, LigatureConnectionPoint> data;
	
	@Override
	public void characters(char[] arg0, int arg1, int arg2) {
		return;
	}

	@Override
	public void endDocument() {
		return;
	}

	@Override
	public void endElement(String arg0, String arg1, String arg2) {
		return;
	}

	@Override
	public void endPrefixMapping(String arg0) {
		return;
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) {
		return;
	}

	@Override
	public void processingInstruction(String arg0, String arg1) {
		return;
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		return;
	}

	@Override
	public void skippedEntity(String arg0) {
		return;
	}

	@Override
	public void startDocument() {
		this.data = new HashMap<>();
		return;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attrib) {
		if(localName.equals("point")) {
			String id	= attrib.getValue("id");
			String x	= attrib.getValue("x");
			String y	= attrib.getValue("y");
			String in	= attrib.getValue("in");
			String out	= attrib.getValue("out");

			Integer index 	= new Integer(id);
			double xDoub	= Double.parseDouble(x);
			double yDoub	= Double.parseDouble(y);
			boolean inBool	= Boolean.parseBoolean(in);
			boolean outBool	= Boolean.parseBoolean(out);

			LigatureConnectionPoint point = new LigatureConnectionPoint(xDoub,yDoub,inBool,outBool);
			this.data.put(index, point);
		}

	}

	@Override
	public void startPrefixMapping(String arg0, String arg1) {
		return;
	}

}
