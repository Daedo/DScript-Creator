package files;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import textparser.Glyph;
import textparser.ParseException;
import textparser.Parser;

public class DScriptPresenter {
	public static SVGDocument buildFullPresentationDocument() {
		HashMap<String,String> letterMap = PropetyInformation.getFiltertPropertyMap();
		Vector<String> letters = getLetters(letterMap);
		Collections.sort(letters);
		
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document doc = impl.createDocument(svgNS, "svg", null);

		// Get the root element (the 'svg' element).
		Element svgRoot = doc.getDocumentElement();
		
		double maxX 	 = 0;
		double maxY		 = 0;
		
		double xPosition = 20;
		double yPosition = 20;
		
		for(String letter:letters) {			
			System.out.println(letter+" Pos: "+xPosition+" - "+yPosition);
			Parser parser = new Parser();
			try {
				Glyph glyph = parser.parse("["+letter+"]").get(0);
				
				
				
				String key  = letter+"_POINTS";
				//key 		= key.toUpperCase();
				
				String XMLURI = "Ligatures\\"+letterMap.get(key);
				//String XMLURI = PropetyInformation.getXMLPath(letter);
				
				System.out.println(XMLURI);
				Vector<ConnectionPoint> points = PointParser.parsePoints(XMLURI);
				
				Document letterDoc = DocumentBuilder.buildDocument(glyph, 0, 0);
				Element letterRoot = letterDoc.getDocumentElement();
				String height = letterRoot.getAttribute("height");
				String width  = letterRoot.getAttribute("width");
				
				//Merge Documents		
				Element glyphGroup = doc.createElementNS(svgNS, "g");
				glyphGroup.setAttributeNS(null, "transform", "translate("+xPosition+","+yPosition+")");
				svgRoot.appendChild(glyphGroup);
				
				NodeList list = letterDoc.getChildNodes();
				for(int i=0;i<list.getLength();i++) {
					Node node = list.item(i);
					node = doc.importNode(node, true);
					glyphGroup.appendChild(node);
				}
				
				
				//Add Connection Points
				for(ConnectionPoint point:points) {
					double xPoint = point.getX()+xPosition;
					double yPoint = point.getY()+yPosition;
					int id		  = point.getID();
					boolean	isIn  = point.isInpoint();
					boolean isOut = point.isOutpoint();
					
					if(isIn) {
						Element cir = doc.createElementNS(svgNS, "circle");
						cir.setAttributeNS(null, "cx", xPoint+"");
						cir.setAttributeNS(null, "cy", yPoint+"");
						cir.setAttributeNS(null, "r", "8");
						cir.setAttributeNS(null, "fill", "red");
						svgRoot.appendChild(cir);
					}
					
					if(isOut) {
						Element cir = doc.createElementNS(svgNS, "circle");
						cir.setAttributeNS(null, "cx", xPoint+"");
						cir.setAttributeNS(null, "cy", yPoint+"");
						cir.setAttributeNS(null, "r", "4");
						cir.setAttributeNS(null, "fill", "blue");
						svgRoot.appendChild(cir);
					}
					
					//Label
					Element lab = doc.createElementNS(svgNS, "text");
					lab.setTextContent(id+"");
					lab.setAttributeNS(null, "x", (xPoint+8)+"");
					lab.setAttributeNS(null, "y", (yPoint+10)+"");
					lab.setAttributeNS(null, "fill", "green");
					svgRoot.appendChild(lab);
				}
				
				Element lab = doc.createElementNS(svgNS, "text");
				lab.setTextContent(letter);
				lab.setAttributeNS(null, "x", (xPosition+10)+"");
				lab.setAttributeNS(null, "y", yPosition+"");
				lab.setAttributeNS(null, "fill", "black");
				svgRoot.appendChild(lab);
				
				
				double deltaX = Double.parseDouble(width);
				double deltaY = Double.parseDouble(height);
				
				System.out.println("Change: "+deltaX+" - "+deltaY);
				
				yPosition+= Double.parseDouble(height)+10;
				
				maxX = Math.max(maxX, xPosition);
				maxY = Math.max(maxY, yPosition);
				
				if(yPosition>=3000) {
					yPosition=20;
					xPosition+=Double.parseDouble(width)+20;
				}
			} catch (ParseException e) {
				System.err.println("Error while building the Presenttion Document (Letter: "+letter+")");
			}
		}
		
		svgRoot.setAttributeNS(null, "width",  (maxX+100)+"");
		svgRoot.setAttributeNS(null, "height", maxY+"");
		
		return (SVGDocument) doc;
	}
	
	private static Vector<String> getLetters(HashMap<String,String> letterMap) {
		Vector<String> letters = new Vector<>();
		//HashMap<String, String> map = PropetyInformation.getFiltertPropertyMap();
		
		Set<String> keys = letterMap.keySet();//map.keySet();
		for(String key:keys) {
			if(!key.endsWith("_POINTS")) {
				letters.add(key);
			}
		}
		return letters;
	}
	
}
