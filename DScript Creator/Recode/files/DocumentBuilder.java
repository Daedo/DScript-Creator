package files;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Stack;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.util.XLinkSupport;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import textparser.Connection;
import textparser.Glyph;
import utils.Point;

public class DocumentBuilder {
	public static SVGDocument buildDocument(Glyph root, double xStart, double yStart) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		Stack<Glyph> workGlyphs				= new Stack<>();
		Stack<Point<Double>> workPositions = new Stack<>();
		Stack<Integer> workConnection		= new Stack<>();

		workGlyphs.push(root);
		workPositions.push(new Point<>(new Double(xStart), new Double(yStart)));
		workConnection.push(new Integer(0));
		double xMax = xStart;
		double yMax = yStart;

		while(!workGlyphs.isEmpty()) {
			Glyph currentGlyph 				= workGlyphs.pop();
			int currentConnectionID			= workConnection.pop().intValue();
			Point<Double> currentPosition	= workPositions.pop();

			String svgPath					= PropetyInformation.getSVGPath(currentGlyph.getLigature());

			if(currentConnectionID==0) {
				Element img = doc.createElementNS(svgNS, "image");

				String jarPath = ClassLoader.getSystemResource("")+"";
				XLinkSupport.setXLinkHref( img, jarPath+"../"+svgPath );
				//TODO Remove "../"

				img.setAttributeNS(null, "x", ""+currentPosition.x.doubleValue());
				img.setAttributeNS(null, "y", ""+currentPosition.y.doubleValue());
				img.setAttributeNS(null, "width", "200");
				img.setAttributeNS(null, "height", "200");
				svgRoot.appendChild(img);
			}			
			Connection currentConnection = currentGlyph.getConnection(currentConnectionID);
			if(currentConnection!=null) {
				workGlyphs.push(currentGlyph);
				workConnection.push(new Integer(currentConnectionID+1));
				workPositions.push(currentPosition);

				//Calculate new Position
				String type = currentConnection.getType();
				System.out.println(type);
				String[] attribs = type.split(",");
				if(attribs.length<2) {
					//TODO Throw Error
					return null;
				}
				int outAtrib = Integer.parseInt(attribs[0].trim());
				int inAtrib  = Integer.parseInt(attribs[1].trim());

				Glyph endGlyph = currentConnection.getEnd();
				Ligature prevLig = new Ligature(currentGlyph.getLigature());
				Ligature newLig   = new Ligature(endGlyph.getLigature());

				ConnectionPoint outP = prevLig.getConnectionPoint(outAtrib);
				ConnectionPoint inP  = newLig.getConnectionPoint(inAtrib);

				double outMoveX = 0;
				double outMoveY = 0;

				if(outP!=null) {
					outMoveX = outP.getX();
					outMoveY = outP.getY();
				}

				double inMoveX  = 0;
				double inMoveY  = 0;
				if(inP!=null) {
					inMoveX = inP.getX();
					inMoveY = inP.getY();
				}

				double xMove = outMoveX - inMoveX;
				double yMove = outMoveY - inMoveY;

				Double newX = new Double(currentPosition.x.intValue()+xMove);
				Double newY = new Double(currentPosition.y.intValue()+yMove);
				Point<Double> newPoint = new Point<>(newX, newY);

				xMax = Math.max(xMax,newX.doubleValue());
				yMax = Math.max(yMax,newY.doubleValue());

				workGlyphs.push(endGlyph);
				workConnection.push(new Integer(0));
				workPositions.push(newPoint);
			}	
		}

		/*
		Element img = doc.createElementNS(svgNS, "image");

		String jarPath = ClassLoader.getSystemResource("")+"";
		XLinkSupport.setXLinkHref( img, jarPath+"../Ligatures/Basic/a1.svg" );
		//TODO Replace with "Ligatures/Basic/a1.svg" for the export

		img.setAttributeNS(null, "x", "0");
		img.setAttributeNS(null, "y", "0");
		img.setAttributeNS(null, "width", "200");
		img.setAttributeNS(null, "height", "200");
		svgRoot.appendChild(img);

		try {
			printDocument(doc, System.out);
		} catch (IOException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// Set the width and height attributes on the root 'svg' element.
		svgRoot.setAttributeNS(null, "width",  xMax+100+"");
		svgRoot.setAttributeNS(null, "height", yMax+100+"");

		return (SVGDocument) doc;
	}

	public static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), 
				new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}

	public static SVGDocument buildWords(Vector<Glyph> words) {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();

		double xPos = 10;
		double yPos = 0;

		for(int i=0;i<words.size();i++) {
			//TODO Use Correct Alignment
			SVGDocument sdoc = buildDocument(words.elementAt(i), xPos, yPos*0);
			Element svgElement = sdoc.getDocumentElement();
			String width  = svgElement.getAttribute("width");
			String height = svgElement.getAttribute("height");
			if(width==null || height==null) {
				continue;
			}

			xPos+=Double.parseDouble(width);
			yPos+=Double.parseDouble(height);

			NodeList list = sdoc.getChildNodes();
			for(int j=0;j<list.getLength();j++) {
				Node imp = doc.importNode(list.item(j), true);
				svgRoot.appendChild(imp);
			}
		}

		svgRoot.setAttributeNS(null, "width",  (xPos+200)+"");
		svgRoot.setAttributeNS(null, "height", (yPos+200)+"");

		return (SVGDocument)doc;
	}

	public static Dimension getSVGSize(Document doc) {
		Element svgElement 	= doc.getDocumentElement();
		if(svgElement.hasAttribute("width") && svgElement.hasAttribute("height")) {
			String width  		= svgElement.getAttribute("width");
			String height 		= svgElement.getAttribute("height");
			
			int    wDim			= (int) Math.ceil(Double.parseDouble(width));
			int    hDim			= (int) Math.ceil(Double.parseDouble(height));

			return new Dimension(wDim,hDim);
		}
		return null;
	}
}
