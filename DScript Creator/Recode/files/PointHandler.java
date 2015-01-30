package files;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class PointHandler implements org.xml.sax.ContentHandler{
	private Vector<ConnectionPoint> pointList;
	
	public Vector<ConnectionPoint> getPointList() {
		return this.pointList;
	}
	
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
		this.pointList = new Vector<>();
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
			
			try {
				int idInt		= Integer.parseInt(id);
				int xInt		= Integer.parseInt(x);
				int yInt 		= Integer.parseInt(y);
				boolean inBool	= Boolean.parseBoolean(in);
				boolean outBool	= Boolean.parseBoolean(out);
				ConnectionPoint point = new ConnectionPoint(idInt, xInt, yInt, inBool, outBool);
				this.pointList.add(point);
			} catch (NumberFormatException e) {
				System.err.println("Parse Error with file. Parsed Key: ");
				for(int i=0;i<attrib.getLength();i++) {
					System.err.println(attrib.getLocalName(i)+" = "+attrib.getValue(i));
				}
			}
		}
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1) {
		return;	
	}
}
